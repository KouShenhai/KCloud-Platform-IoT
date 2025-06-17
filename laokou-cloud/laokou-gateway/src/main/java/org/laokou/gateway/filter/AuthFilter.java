/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.laokou.gateway.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.config.OAuth2ResourceServerProperties;
import org.laokou.common.core.util.MapUtils;
import org.laokou.common.core.util.SpringUtils;
import org.laokou.common.crypto.util.RSAUtils;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.StringUtils;
import org.laokou.common.nacos.util.ReactiveResponseUtils;
import org.laokou.gateway.util.ReactiveI18nUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static org.laokou.common.i18n.common.constant.StringConstants.AND;
import static org.laokou.common.i18n.common.constant.StringConstants.EMPTY;
import static org.laokou.common.i18n.common.exception.StatusCode.UNAUTHORIZED;
import static org.laokou.common.nacos.util.ReactiveRequestUtils.getContentType;
import static org.laokou.common.nacos.util.ReactiveRequestUtils.getMethodName;
import static org.laokou.common.nacos.util.ReactiveRequestUtils.getParamValue;
import static org.laokou.common.nacos.util.ReactiveRequestUtils.getRequestURL;
import static org.laokou.common.nacos.util.ReactiveRequestUtils.pathMatcher;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_LENGTH;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

/**
 * 认证过滤器.
 *
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthFilter implements GlobalFilter, Ordered, InitializingBean {

	/**
	 * 用户名.
	 */
	private static final String USERNAME = "username";

	/**
	 * 密码.
	 */
	private static final String PASSWORD = "password";

	/**
	 * 用户名密码认证【OAuth2】.
	 */
	private static final String USERNAME_PASSWORD = "username_password";

	/**
	 * 令牌URL.
	 */
	private static final String TOKEN_URL = "/oauth2/token";

	/**
	 * Chunked.
	 */
	private static final String CHUNKED = "chunked";

	/**
	 * 认证类型.
	 */
	private static final String GRANT_TYPE = "grant_type";

	private final Map<String, Set<String>> uriMap = new ConcurrentHashMap<>(1024);

	private final OAuth2ResourceServerProperties oAuth2ResourceServerProperties;

	private final SpringUtils springUtils;

	// @formatter:off
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		try {
			// 国际化
			ReactiveI18nUtils.set(exchange);
			// 获取request对象
			ServerHttpRequest request = exchange.getRequest();
			// 获取uri
			String requestURL = getRequestURL(request);
			// 请求放行，无需验证权限
			if (pathMatcher(getMethodName(request), requestURL, uriMap)) {
				// 无需验证权限的URL，需要将令牌置空
				return chain.filter(exchange.mutate().request(request.mutate().header(AUTHORIZATION, EMPTY).build()).build());
			}
			if (requestURL.contains(TOKEN_URL) && POST.matches(getMethodName(request)) && APPLICATION_FORM_URLENCODED.isCompatibleWith(getContentType(request))) {
				return decodeOAuth2Password(exchange, chain);
			}
			// 获取token
			String token = getParamValue(request, AUTHORIZATION);
			if (StringUtils.isEmpty(token)) {
				return ReactiveResponseUtils.responseOk(exchange, Result.fail(UNAUTHORIZED));
			}
			// 增加令牌
			return chain.filter(exchange.mutate().request(request.mutate().header(AUTHORIZATION, token).build()).build());
		}
		finally {
			ReactiveI18nUtils.reset();
		}
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE - 1000;
	}

	/**
	 * OAuth2解密. see {@link ModifyRequestBodyGatewayFilterFactory}
	 * @param chain chain
	 * @param exchange exchange
	 * @return 响应式
	 */
	private Mono<Void> decodeOAuth2Password(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerRequest serverRequest = ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());
		Mono<String> modifiedBody = serverRequest.bodyToMono(String.class).flatMap(decrypt());
		BodyInserter<Mono<String>, ReactiveHttpOutputMessage> bodyInserter = BodyInserters.fromPublisher(modifiedBody, String.class);
		HttpHeaders headers = new HttpHeaders();
		headers.putAll(exchange.getRequest().getHeaders());
		headers.remove(CONTENT_LENGTH);
		headers.set(CONTENT_TYPE, APPLICATION_FORM_URLENCODED_VALUE);
		CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);
		return bodyInserter.insert(outputMessage, new BodyInserterContext()).then(Mono.defer(() -> chain.filter(exchange.mutate().request(requestDecorator(exchange, headers, outputMessage)).build()))).onErrorResume((Function<Throwable, Mono<Void>>) throwable -> release(outputMessage, throwable));
	}

	/**
	 * 释放缓存.
	 * @param outputMessage 输出消息
	 * @param throwable 异常
	 * @return 释放结果
	 */
	private Mono<Void> release(CachedBodyOutputMessage outputMessage, Throwable throwable) {
		return outputMessage.getBody().map(DataBufferUtils::release).then(Mono.error(throwable));
	}

	/**
	 * 用户名/密码 解密.
	 * @return 解密结果
	 */
	private Function<String, Mono<String>> decrypt() {
		return s -> {
			// 获取请求密码并解密
			Map<String, String> paramMap = MapUtils.getParameterMap(s, AND).asSingleValueMap();
			if (ObjectUtils.equals(USERNAME_PASSWORD, paramMap.getOrDefault(GRANT_TYPE, EMPTY)) && paramMap.containsKey(PASSWORD) && paramMap.containsKey(USERNAME)) {
				try {
					String password = paramMap.get(PASSWORD);
					String username = paramMap.get(USERNAME);
					// 返回修改后报文字符
					if (StringUtils.isNotEmpty(password)) {
						paramMap.put(PASSWORD, RSAUtils.decryptByPrivateKey(password));
					}
					if (StringUtils.isNotEmpty(username)) {
						paramMap.put(USERNAME, RSAUtils.decryptByPrivateKey(username));
					}
				}
				catch (Exception e) {
					log.error("用户名密码RSA解密失败【用户名密码认证模式】，错误信息：{}", e.getMessage());
				}
			}
			return Mono.just(MapUtils.parseParamterString(paramMap));
		};
	}

	/**
	 * 构建请求装饰器.
	 * @param exchange 服务网络交换机
	 * @param headers 请求头
	 * @param outputMessage 输出消息
	 * @return 请求装饰器
	 */
	private ServerHttpRequestDecorator requestDecorator(ServerWebExchange exchange, HttpHeaders headers, CachedBodyOutputMessage outputMessage) {
		return new ServerHttpRequestDecorator(exchange.getRequest()) {

			@Override
			@NonNull
			public HttpHeaders getHeaders() {
				long contentLength = headers.getContentLength();
				HttpHeaders httpHeaders = new HttpHeaders();
				httpHeaders.putAll(super.getHeaders());
				if (contentLength > 0) {
					httpHeaders.setContentLength(contentLength);
				}
				else {
					httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, CHUNKED);
				}
				return httpHeaders;
			}

			@Override
			@NonNull
			public Flux<DataBuffer> getBody() {
				return outputMessage.getBody();
			}
		};
	}

	@Override
	public void afterPropertiesSet() {
		uriMap.putAll(MapUtils.toUriMap(oAuth2ResourceServerProperties.getRequestMatcher().getIgnorePatterns(), springUtils.getServiceId()));
	}
	// @formatter:on

}

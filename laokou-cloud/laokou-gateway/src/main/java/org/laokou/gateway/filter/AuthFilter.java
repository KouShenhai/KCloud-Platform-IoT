/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.config.OAuth2ResourceServerProperties;
import org.laokou.common.core.utils.MapUtil;
import org.laokou.common.crypto.utils.RsaUtil;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.nacos.utils.ConfigUtil;
import org.laokou.common.nacos.utils.ReactiveRequestUtil;
import org.laokou.common.nacos.utils.ReactiveResponseUtil;
import org.laokou.gateway.utils.I18nReactiveUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Function;

import static org.laokou.common.i18n.common.RequestHeaderConstant.AUTHORIZATION;
import static org.laokou.common.i18n.common.exception.StatusCode.UNAUTHORIZED;
import static org.laokou.common.i18n.common.StringConstant.EMPTY;
import static org.laokou.common.nacos.utils.ReactiveRequestUtil.*;
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
@RefreshScope
@RequiredArgsConstructor
public class AuthFilter implements GlobalFilter, Ordered, InitializingBean {

	/**
	 * 用户名.
	 */
	public static final String USERNAME = "username";

	/**
	 * 密码.
	 */
	public static final String PASSWORD = "password";

	/**
	 * Chunked.
	 */
	private static final String CHUNKED = "chunked";

	/**
	 * 令牌URL.
	 */
	public static final String TOKEN_URL = "/oauth2/token";

	/**
	 * Nacos公共配置标识.
	 */
	private static final String COMMON_DATA_ID = "application-common.yaml";

	private final Environment env;

	private final OAuth2ResourceServerProperties oAuth2ResourceServerProperties;

	private volatile Map<String, Set<String>> urlMap;

	private final ConfigUtil configUtil;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		try {
			// 国际化
			I18nReactiveUtil.set(exchange);
			// 获取request对象
			ServerHttpRequest request = exchange.getRequest();
			// 获取uri
			String requestURL = getRequestURL(request);
			// 请求放行，无需验证权限
			if (pathMatcher(getMethodName(request), requestURL, urlMap)) {
				// 无需验证权限的URL，需要将令牌置空
				return chain
					.filter(exchange.mutate().request(request.mutate().header(AUTHORIZATION, EMPTY).build()).build());
			}
			// 表单提交
			MediaType mediaType = getContentType(request);
			if (requestURL.contains(TOKEN_URL) && POST.matches(getMethodName(request))
					&& APPLICATION_FORM_URLENCODED.isCompatibleWith(mediaType)) {
				return decode(exchange, chain);
			}
			// 获取token
			String token = ReactiveRequestUtil.getParamValue(request, AUTHORIZATION);
			if (StringUtil.isEmpty(token)) {
				return ReactiveResponseUtil.response(exchange, Result.fail(UNAUTHORIZED));
			}
			// 增加令牌
			return chain
				.filter(exchange.mutate().request(request.mutate().header(AUTHORIZATION, token).build()).build());
		}
		finally {
			I18nReactiveUtil.reset();
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
	private Mono<Void> decode(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerRequest serverRequest = ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());
		Mono<String> modifiedBody = serverRequest.bodyToMono(String.class).flatMap(decrypt());
		BodyInserter<Mono<String>, ReactiveHttpOutputMessage> bodyInserter = BodyInserters.fromPublisher(modifiedBody,
				String.class);
		HttpHeaders headers = new HttpHeaders();
		headers.putAll(exchange.getRequest().getHeaders());
		headers.remove(CONTENT_LENGTH);
		headers.set(CONTENT_TYPE, APPLICATION_FORM_URLENCODED_VALUE);
		CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);
		return bodyInserter.insert(outputMessage, new BodyInserterContext()).then(Mono.defer(() -> {
			ServerHttpRequest decorator = requestDecorator(exchange, headers, outputMessage);
			return chain.filter(exchange.mutate().request(decorator).build());
		})).onErrorResume((Function<Throwable, Mono<Void>>) throwable -> release(outputMessage, throwable));
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
			Map<String, String> paramMap = MapUtil.parseParamMap(s);
			if (paramMap.containsKey(PASSWORD) && paramMap.containsKey(USERNAME)) {
				// log.info("密码模式认证");
				try {
					String privateKey = RsaUtil.getPrivateKey();
					String password = paramMap.get(PASSWORD);
					String username = paramMap.get(USERNAME);
					// 返回修改后报文字符
					if (StringUtil.isNotEmpty(password)) {
						paramMap.put(PASSWORD, RsaUtil.decryptByPrivateKey(password, privateKey));
					}
					if (StringUtil.isNotEmpty(username)) {
						paramMap.put(USERNAME, RsaUtil.decryptByPrivateKey(username, privateKey));
					}
				}
				catch (Exception e) {
					log.error("错误信息：{}，详情见日志", LogUtil.record(e.getMessage()), e);
				}
			}
			return Mono.just(MapUtil.parseParams(paramMap));
		};
	}

	/**
	 * 构建请求装饰器.
	 * @param exchange 服务网络交换机
	 * @param headers 请求头
	 * @param outputMessage 输出消息
	 * @return 请求装饰器
	 */
	private ServerHttpRequestDecorator requestDecorator(ServerWebExchange exchange, HttpHeaders headers,
			CachedBodyOutputMessage outputMessage) {
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

	/**
	 * 订阅nacos消息通知，用于实时更新白名单URL.
	 */
	@PostConstruct
	@SneakyThrows
	public void initURL() {
		String group = configUtil.getGroup();
		ConfigService configService = configUtil.getConfigService();
		configService.addListener(COMMON_DATA_ID, group, new Listener() {
			@Override
			public Executor getExecutor() {
				return Executors.newSingleThreadExecutor();
			}

			@Override
			public void receiveConfigInfo(String configInfo) {
				log.info("接收到URL变动通知");
				initURLMap();
			}
		});
	}

	@Override
	public void afterPropertiesSet() {
		initURLMap();
	}

	private void initURLMap() {
		urlMap = Optional.of(MapUtil.toUriMap(oAuth2ResourceServerProperties.getRequestMatcher().getIgnorePatterns(),
				env.getProperty("spring.application.name")))
			.orElseGet(ConcurrentHashMap::new);
	}

}

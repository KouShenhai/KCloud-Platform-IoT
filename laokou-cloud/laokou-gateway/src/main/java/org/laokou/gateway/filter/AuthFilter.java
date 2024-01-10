/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.crypto.utils.RsaUtil;
import org.laokou.common.nacos.utils.ConfigUtil;
import org.laokou.common.nacos.utils.ResponseUtil;
import org.laokou.gateway.utils.I18nUtil;
import org.laokou.gateway.utils.RequestUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Function;

import static org.laokou.common.i18n.common.OAuth2Constants.*;
import static org.laokou.common.i18n.common.PropertiesConstants.SPRING_APPLICATION_NAME;
import static org.laokou.common.i18n.common.RequestHeaderConstants.AUTHORIZATION;
import static org.laokou.common.i18n.common.RequestHeaderConstants.CHUNKED;
import static org.laokou.common.i18n.common.StatusCodes.UNAUTHORIZED;
import static org.laokou.common.i18n.common.StringConstants.EMPTY;
import static org.laokou.common.i18n.common.SysConstants.COMMON_DATA_ID;
import static org.laokou.gateway.utils.RequestUtil.pathMatcher;

/**
 * 认证Filter.
 *
 * @author laokou
 */
@Slf4j
@Component
@RefreshScope
@RequiredArgsConstructor
public class AuthFilter implements GlobalFilter, Ordered, InitializingBean {

	private final Environment env;

	private final OAuth2ResourceServerProperties oAuth2ResourceServerProperties;

	private volatile Map<String, Set<String>> uriMap;

	private final ConfigUtil configUtil;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		try {
			// 国际化
			I18nUtil.set(exchange);
			// 获取request对象
			ServerHttpRequest request = exchange.getRequest();
			// 获取uri
			String requestUri = request.getPath().pathWithinApplication().value();
			// 请求放行，无需验证权限
			if (pathMatcher(request.getMethod().name(), requestUri, uriMap)) {
				// 无需验证权限的URL，需要将令牌置空
				return chain
					.filter(exchange.mutate().request(request.mutate().header(AUTHORIZATION, EMPTY).build()).build());
			}
			// 表单提交
			MediaType mediaType = request.getHeaders().getContentType();
			if (requestUri.contains(TOKEN_URL) && HttpMethod.POST.matches(request.getMethod().name())
					&& MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(mediaType)) {
				return decode(exchange, chain);
			}
			// 获取token
			String token = RequestUtil.getParamValue(request, AUTHORIZATION);
			if (StringUtil.isEmpty(token)) {
				return ResponseUtil.response(exchange, Result.fail(UNAUTHORIZED));
			}
			// 增加令牌
			return chain
				.filter(exchange.mutate().request(request.mutate().header(AUTHORIZATION, token).build()).build());
		}
		finally {
			I18nUtil.reset();
		}
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE - 1000;
	}

	/**
	 * OAuth2解密.
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
		headers.remove(HttpHeaders.CONTENT_LENGTH);
		headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);
		return bodyInserter.insert(outputMessage, new BodyInserterContext()).then(Mono.defer(() -> {
			ServerHttpRequest decorator = requestDecorator(exchange, headers, outputMessage);
			return chain.filter(exchange.mutate().request(decorator).build());
		}));
	}

	private Function<String, Mono<String>> decrypt() {
		return s -> {
			// 获取请求密码并解密
			Map<String, String> paramMap = MapUtil.parseParamMap(s);
			if (paramMap.containsKey(PASSWORD) && paramMap.containsKey(USERNAME)) {
				log.info("密码模式认证");
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
					log.error("错误信息：{}，详情见日志", LogUtil.result(e.getMessage()), e);
				}
			}
			return Mono.just(MapUtil.parseParams(paramMap));
		};
	}

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
		uriMap = Optional.of(MapUtil.toUriMap(oAuth2ResourceServerProperties.getRequestMatcher().getIgnorePatterns(),
				env.getProperty(SPRING_APPLICATION_NAME)))
			.orElseGet(HashMap::new);
	}

}

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

import com.google.common.net.HttpHeaders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.laokou.common.core.util.MapUtils;
import org.laokou.common.i18n.util.SpringUtils;
import org.laokou.common.i18n.common.exception.StatusCode;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.i18n.util.StringExtUtils;
import org.laokou.common.reactor.util.ReactiveRequestUtils;
import org.laokou.common.reactor.util.ReactiveResponseUtils;
import org.laokou.gateway.config.RequestMatcherProperties;
import org.laokou.gateway.util.ReactiveI18nUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 认证过滤器.
 *
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthFilter implements GlobalFilter, Ordered, InitializingBean {

	private final Map<String, Set<String>> uriMap = new ConcurrentHashMap<>(1024);

	private final RequestMatcherProperties requestMatcherProperties;

	private final SpringUtils springUtils;

	// @formatter:off
	@NonNull
	@Override
	public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull GatewayFilterChain chain) {
		try {
			// 国际化
			ReactiveI18nUtils.set(exchange);
			// 获取request对象
			ServerHttpRequest request = exchange.getRequest();
			// 获取uri
			String requestURL = ReactiveRequestUtils.getRequestURL(request);
			// 请求放行，无需验证权限
			if (ReactiveRequestUtils.pathMatcher(ReactiveRequestUtils.getMethodName(request), requestURL, uriMap)) {
				return chain.filter(exchange.mutate().request(request.mutate().build()).build());
			}
			// 获取令牌
			String token = ReactiveRequestUtils.getParamValue(request, HttpHeaders.AUTHORIZATION);
			if (StringExtUtils.isEmpty(token)) {
				return ReactiveResponseUtils.responseOk(exchange, Result.fail(StatusCode.UNAUTHORIZED));
			}
			// 增加令牌
			return chain.filter(exchange.mutate().request(request.mutate().header(HttpHeaders.AUTHORIZATION, token).build()).build());
		}
		finally {
			ReactiveI18nUtils.reset();
		}
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE - 1000;
	}

	@Override
	public void afterPropertiesSet() {
		uriMap.putAll(MapUtils.toUriMap(requestMatcherProperties.getIgnorePatterns(), springUtils.getServiceId()));
	}
	// @formatter:on

}

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
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.i18n.util.StringUtils;
import org.laokou.common.nacos.util.ReactiveResponseUtils;
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
import static org.laokou.common.i18n.common.exception.StatusCode.UNAUTHORIZED;
import static org.laokou.common.nacos.util.ReactiveRequestUtils.getMethodName;
import static org.laokou.common.nacos.util.ReactiveRequestUtils.getParamValue;
import static org.laokou.common.nacos.util.ReactiveRequestUtils.getRequestURL;
import static org.laokou.common.nacos.util.ReactiveRequestUtils.pathMatcher;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

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
				return chain.filter(exchange.mutate().request(request.mutate().build()).build());
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

	@Override
	public void afterPropertiesSet() {
		uriMap.putAll(MapUtils.toUriMap(oAuth2ResourceServerProperties.getRequestMatcher().getIgnorePatterns(), springUtils.getServiceId()));
	}
	// @formatter:on

}

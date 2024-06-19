/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

import io.micrometer.common.lang.NonNullApi;
import lombok.RequiredArgsConstructor;
import org.laokou.common.crypto.utils.RSAUtil;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.i18n.utils.ValidatorUtil;
import org.laokou.common.nacos.utils.ReactiveRequestUtil;
import org.laokou.common.nacos.utils.ReactiveResponseUtil;
import org.laokou.gateway.config.GatewayExtProperties;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import static org.laokou.common.i18n.common.exception.AuthException.OAUTH2_USERNAME_PASSWORD_ERROR;
import static org.laokou.common.i18n.common.exception.ParamException.OAUTH2_PASSWORD_REQUIRE;
import static org.laokou.common.i18n.common.exception.ParamException.OAUTH2_USERNAME_REQUIRE;
import static org.laokou.gateway.filter.AuthFilter.PASSWORD;
import static org.laokou.gateway.filter.AuthFilter.USERNAME;

/**
 * API过滤器.
 *
 * @author laokou
 */
@NonNullApi
@RequiredArgsConstructor
public class ApiFilter implements WebFilter {

	private static final String API_PATTERN = "/v3/routers/**";

	private final RequestMappingHandlerMapping requestMappingHandlerMapping;

	private final GatewayExtProperties gatewayExtProperties;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		return requestMappingHandlerMapping.getHandler(exchange)
			.switchIfEmpty(chain.filter(exchange))
			.flatMap(handler -> {
				ServerHttpRequest request = exchange.getRequest();
				String requestURL = ReactiveRequestUtil.getRequestURL(request);
				if (ReactiveRequestUtil.pathMatcher(requestURL, API_PATTERN)) {
					return checkUsernamePassword(exchange, request, chain);
				}
				return chain.filter(exchange);
			});
	}

	/**
	 * 校验用户名和密码.
	 * @param exchange 服务网络交换机
	 * @param request 请求对象
	 * @param chain 链式过滤器
	 * @return 响应结果
	 */
	private Mono<Void> checkUsernamePassword(ServerWebExchange exchange, ServerHttpRequest request,
			WebFilterChain chain) {
		String username = ReactiveRequestUtil.getParamValue(request, USERNAME);
		String password = ReactiveRequestUtil.getParamValue(request, PASSWORD);
		if (StringUtil.isEmpty(username)) {
			// 用户名不能为空
			return ReactiveResponseUtil.response(exchange,
					Result.fail(ValidatorUtil.getMessage(OAUTH2_USERNAME_REQUIRE)));
		}
		if (StringUtil.isEmpty(password)) {
			// 密码不能为空
			return ReactiveResponseUtil.response(exchange,
					Result.fail(ValidatorUtil.getMessage(OAUTH2_PASSWORD_REQUIRE)));
		}
		try {
			username = RSAUtil.decryptByPrivateKey(username);
			password = RSAUtil.decryptByPrivateKey(password);
		}
		catch (Exception e) {
			// 用户名或密码错误
			return ReactiveResponseUtil.response(exchange, Result.fail(OAUTH2_USERNAME_PASSWORD_ERROR));
		}
		if (gatewayExtProperties.isEnabled()) {
			if (!ObjectUtil.equals(gatewayExtProperties.getPassword(), password)
					|| !ObjectUtil.equals(gatewayExtProperties.getUsername(), username)) {
				// 用户名或密码错误
				return ReactiveResponseUtil.response(exchange, Result.fail(OAUTH2_USERNAME_PASSWORD_ERROR));
			}
		}
		return chain.filter(exchange);
	}

}

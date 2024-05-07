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

package org.laokou.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import static org.laokou.common.i18n.common.constants.StringConstant.TRUE;

/**
 * 跨域配置.
 *
 * @author laokou
 */
@Configuration
@Slf4j
public class CorsConfig {

	@Bean
	@ConditionalOnMissingBean(WebFilter.class)
	public WebFilter corsFilter() {
		return (ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) -> {
			// 获取request对象
			ServerHttpRequest request = serverWebExchange.getRequest();
			if (!CorsUtils.isCorsRequest(request)) {
				return webFilterChain.filter(serverWebExchange);
			}
			// 获取请求头
			HttpHeaders requestHeaders = request.getHeaders();
			// 获取response对象
			ServerHttpResponse response = serverWebExchange.getResponse();
			// 获取请求头的请求方法
			HttpMethod requestMethod = requestHeaders.getAccessControlRequestMethod();
			// 获取响应头
			HttpHeaders responseHeaders = response.getHeaders();
			// 允许跨域
			responseHeaders.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, requestHeaders.getOrigin());
			// 允许请求头
			responseHeaders.addAll(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,
					requestHeaders.getAccessControlRequestHeaders());
			// 允许方法
			if (requestMethod != null) {
				responseHeaders.add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, requestMethod.name());
			}
			// 允许证书
			responseHeaders.add(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, TRUE);
			// 暴露响应头
			responseHeaders.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, CorsConfiguration.ALL);
			// 每1个小时发送一次预请求
			responseHeaders.add(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "3600");
			// 获取方法
			HttpMethod method = request.getMethod();
			if (method == HttpMethod.OPTIONS) {
				response.setStatusCode(HttpStatus.OK);
				return Mono.empty();
			}
			return webFilterChain.filter(serverWebExchange);
		};
	}

}

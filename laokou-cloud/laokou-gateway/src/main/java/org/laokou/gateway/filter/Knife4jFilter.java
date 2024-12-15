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
import org.laokou.common.banner.utils.ResourceUtil;
import org.laokou.common.nacos.utils.ReactiveResponseUtil;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;

import static org.laokou.common.nacos.utils.ReactiveRequestUtil.*;
import static org.springframework.http.HttpMethod.GET;

/**
 * @author laokou
 */
@Component
@NonNullApi
public class Knife4jFilter implements WebFilter {

	/**
	 * Knife4j OAuth2认证页面.
	 */
	private static final String OAUTH2_PAGE_URL = "/webjars/oauth/oauth2.html";

	/**
	 * OAuth2页面.
	 */
	private static final String HTML_CONTENT;

	static {
		try {
			HTML_CONTENT = ResourceUtil.getResource("oauth2.html").getContentAsString(StandardCharsets.UTF_8).trim();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		// 获取request对象
		ServerHttpRequest request = exchange.getRequest();
		// 获取uri
		String requestURL = getRequestURL(request);
		// OAuth2认证页面
		if (pathMatcher(getMethodName(request), requestURL, Map.of(GET.name(), Set.of(OAUTH2_PAGE_URL)))) {
			return ReactiveResponseUtil.responseOk(exchange, HTML_CONTENT, MediaType.TEXT_HTML);
		}
		return chain.filter(exchange);
	}

}

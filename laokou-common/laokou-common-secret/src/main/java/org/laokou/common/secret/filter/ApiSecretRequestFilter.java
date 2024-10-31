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

package org.laokou.common.secret.filter;

import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.laokou.common.core.utils.RequestUtil;
import org.laokou.common.secret.annotation.ApiSecret;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;

/**
 * @author laokou
 */
@NonNullApi
public class ApiSecretRequestFilter extends OncePerRequestFilter {

	@Autowired
	@Qualifier("requestMappingHandlerMapping")
	private HandlerMapping handlerMapping;

	@Override
	@SneakyThrows
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
		HandlerMethod handlerMethod = RequestUtil.getHandlerMethod(request, handlerMapping);
		if (handlerMethod != null && handlerMethod.getMethod().isAnnotationPresent(ApiSecret.class)) {
			ServletRequest requestWrapper = new RequestUtil.RequestWrapper(request);
			chain.doFilter(requestWrapper, response);
		}
		else {
			chain.doFilter(request, response);
		}
	}

}

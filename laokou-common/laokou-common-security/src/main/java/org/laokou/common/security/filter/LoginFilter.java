/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.ResourceExtUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Order(Ordered.HIGHEST_PRECEDENCE + 10000)
public final class LoginFilter extends OncePerRequestFilter {

	private final String LOGIN_HTML = new String(ResourceExtUtils.getResource("static/index.html").getInputStream().readAllBytes(), StandardCharsets.UTF_8);

	public LoginFilter() throws IOException {
	}

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws IOException, ServletException {
		if (ObjectUtils.equals("GET", request.getMethod()) && ObjectUtils.equals(request.getServletPath(), "/login")) {
			response.setContentType("text/html;charset=UTF-8");
			response.setCharacterEncoding(StandardCharsets.UTF_8.name());
			response.setContentLength(LOGIN_HTML.getBytes(StandardCharsets.UTF_8).length);
			response.getWriter().write(LOGIN_HTML);
			return;
		}
		filterChain.doFilter(request, response);
	}

}

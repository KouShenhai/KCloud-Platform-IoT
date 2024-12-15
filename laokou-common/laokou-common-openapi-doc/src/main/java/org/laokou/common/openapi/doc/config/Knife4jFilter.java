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

package org.laokou.common.openapi.doc.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.laokou.common.core.utils.ResponseUtil;
import org.laokou.common.i18n.utils.ResourceUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author laokou
 */
@Slf4j
@Component
public class Knife4jFilter extends OncePerRequestFilter {

	private static final String OAUTH2_PAGE_URL = "/webjars/oauth/oauth2.html";

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
	@SneakyThrows
	protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
			@NotNull FilterChain chain) {
		if (isOAuth2UrlRequest(request)) {
			ResponseUtil.responseOk(response, HTML_CONTENT, MimeTypeUtils.TEXT_HTML_VALUE);
		}
		else {
			chain.doFilter(request, response);
		}
	}

	private boolean isOAuth2UrlRequest(HttpServletRequest request) {
		if (!"GET".equals(request.getMethod())) {
			return false;
		}
		String uri = request.getRequestURI();
		int pathParamIndex = uri.indexOf(';');
		if (pathParamIndex > 0) {
			uri = uri.substring(0, pathParamIndex);
		}
		if (request.getQueryString() != null) {
			uri += "?" + request.getQueryString();
		}
		if (StringUtil.isEmpty(request.getContextPath())) {
			return uri.equals(OAUTH2_PAGE_URL);
		}
		return uri.equals(request.getContextPath() + OAUTH2_PAGE_URL);
	}

}

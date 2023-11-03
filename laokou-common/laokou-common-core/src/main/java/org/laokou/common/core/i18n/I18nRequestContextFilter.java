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

package org.laokou.common.core.i18n;

import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.laokou.common.i18n.utils.LocaleUtil;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.RequestContextFilter;

import java.io.IOException;

/**
 * @author laokou
 */
@NonNullApi
public class I18nRequestContextFilter extends RequestContextFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		ServletRequestAttributes attributes = new ServletRequestAttributes(request, response);
		initContextHolders(request, attributes);
		try {
			filterChain.doFilter(request, response);
		}
		finally {
			resetContextHolders();
			if (logger.isTraceEnabled()) {
				logger.trace("Cleared thread-bound request context: " + request);
			}
			attributes.requestCompleted();
		}
	}

	private void initContextHolders(HttpServletRequest request, ServletRequestAttributes requestAttributes) {
		String language = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
		LocaleContextHolder.setLocale(LocaleUtil.toLocale(language), true);
		RequestContextHolder.setRequestAttributes(requestAttributes, true);
		if (logger.isTraceEnabled()) {
			logger.trace("Bound request context to thread: " + request);
		}
	}

	private void resetContextHolders() {
		LocaleContextHolder.resetLocaleContext();
		RequestContextHolder.resetRequestAttributes();
	}

}

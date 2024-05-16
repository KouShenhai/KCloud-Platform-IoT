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

package org.laokou.common.core.i18n;

import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.laokou.common.core.utils.I18nUtil;
import org.springframework.boot.web.servlet.filter.OrderedRequestContextFilter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;

/**
 * 上下文请求拦截器（国际化）.
 *
 * @author laokou
 */
@Setter
@NonNullApi
public final class I18nRequestContextFilter extends OrderedRequestContextFilter {

	public static final String LANG = "lang";

	public I18nRequestContextFilter() {
	}

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

	/**
	 * 往国际化本地线程变量写入初始化上下文请求的值.
	 * @param request 请求对象
	 * @param requestAttributes 请求属性
	 */
	private void initContextHolders(HttpServletRequest request, ServletRequestAttributes requestAttributes) {
		I18nUtil.set(request);
		RequestContextHolder.setRequestAttributes(requestAttributes, true);
		if (logger.isTraceEnabled()) {
			logger.trace("Bound request context to thread: " + request);
		}
	}

	/**
	 * 注销国际化本地线程变量.
	 */
	private void resetContextHolders() {
		I18nUtil.reset();
		RequestContextHolder.resetRequestAttributes();
	}

}

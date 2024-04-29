/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.auth.config.filter;

import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.laokou.common.core.context.ShutdownHolder;
import org.laokou.common.core.utils.I18nUtil;
import org.laokou.common.core.utils.ResponseUtil;
import org.laokou.common.i18n.dto.Result;
import org.springframework.web.filter.OncePerRequestFilter;

import static org.laokou.common.i18n.common.StatusCode.SERVICE_UNAVAILABLE;
import static org.laokou.common.i18n.common.TraceConstant.TRACE_ID;

/**
 * 认证过滤器.
 *
 * @author laokou
 */
@Slf4j
@NonNullApi
public class OAuth2AuthorizationFilter extends OncePerRequestFilter {

	@Override
	@SneakyThrows
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
		try {
			// 国际化
			I18nUtil.set(request);
			ThreadContext.put(TRACE_ID, request.getHeader(TRACE_ID));
			if (ShutdownHolder.status()) {
				ResponseUtil.response(response, Result.fail(SERVICE_UNAVAILABLE));
				return;
			}
			chain.doFilter(request, response);
		}
		finally {
			ThreadContext.clearMap();
			I18nUtil.reset();
		}
	}

}

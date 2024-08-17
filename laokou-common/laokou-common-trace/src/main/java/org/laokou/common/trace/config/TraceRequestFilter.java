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

package org.laokou.common.trace.config;

import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.laokou.common.core.context.ShutdownHolder;
import org.laokou.common.core.utils.ResponseUtil;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.log4j2.utils.TraceUtil;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.laokou.common.core.utils.RequestUtil.getParamValue;
import static org.laokou.common.i18n.common.constant.TraceConstant.*;
import static org.laokou.common.i18n.common.exception.StatusCode.SERVICE_UNAVAILABLE;

/**
 * @author laokou
 */
@NonNullApi
public class TraceRequestFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		try {
			if (ShutdownHolder.status()) {
				ResponseUtil.response(response, Result.fail(SERVICE_UNAVAILABLE));
				return;
			}
			String traceId = getParamValue(request, TRACE_ID);
			String userId = getParamValue(request, USER_ID);
			String username = getParamValue(request, USER_NAME);
			String tenantId = getParamValue(request, TENANT_ID);
			String spanId = getParamValue(request, SPAN_ID);
			TraceUtil.putContext(traceId, userId, tenantId, username, spanId);
			chain.doFilter(request, response);
		}
		finally {
			TraceUtil.clearContext();
		}
	}

}

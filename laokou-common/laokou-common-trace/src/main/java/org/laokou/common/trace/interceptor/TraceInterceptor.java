/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

package org.laokou.common.trace.interceptor;

import io.micrometer.common.lang.NonNullApi;
import io.micrometer.common.lang.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.ThreadContext;
import org.laokou.common.i18n.utils.StringUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import static org.laokou.common.i18n.common.StringConstant.EMPTY;
import static org.laokou.common.i18n.common.TraceConstant.*;

/**
 * @author laokou
 */
@NonNullApi
public class TraceInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		String traceId = getParamValue(request, TRACE_ID);
		String userId = getParamValue(request, USER_ID);
		String username = getParamValue(request, USER_NAME);
		String tenantId = getParamValue(request, TENANT_ID);
		ThreadContext.put(TRACE_ID, traceId);
		ThreadContext.put(USER_ID, userId);
		ThreadContext.put(TENANT_ID, tenantId);
		ThreadContext.put(USER_NAME, username);
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable Exception ex) {
		ThreadContext.clearMap();
	}

	private String getParamValue(HttpServletRequest request, String paramName) {
		String paramValue = request.getHeader(paramName);
		// 从参数中获取
		if (StringUtil.isEmpty(paramValue)) {
			paramValue = request.getParameter(paramName);
		}
		return StringUtil.isEmpty(paramValue) ? EMPTY : paramValue.trim();
	}

}

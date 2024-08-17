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

package org.laokou.common.trace.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.ThreadContext;
import org.laokou.common.i18n.utils.StringUtil;
import org.springframework.http.server.reactive.ServerHttpRequest;

import static org.laokou.common.i18n.common.constant.StringConstant.EMPTY;
import static org.laokou.common.i18n.common.constant.TraceConstant.*;

/**
 * @author laokou
 */
public class TraceUtil {

	public static void putContext(String traceId, String userId, String username, String tenantId) {
		ThreadContext.put(TRACE_ID, traceId);
		ThreadContext.put(USER_ID, userId);
		ThreadContext.put(TENANT_ID, tenantId);
		ThreadContext.put(USER_NAME, username);
//		Span currentSpan = tracer.currentSpan();
//		if (currentSpan != null) {
//			currentSpan.tag(SPAN_TRACE_ID, traceId);
//			currentSpan.tag(SPAN_USER_ID, userId);
//			currentSpan.tag(SPAN_TENANT_ID, tenantId);
//			currentSpan.tag(SPAN_USER_NAME, username);
//		}
	}

	public static void clearContext() {
		ThreadContext.clearMap();
	}

	public static String getParamValue(HttpServletRequest request, String paramName) {
		String paramValue = request.getHeader(paramName);
		// 从参数中获取
		if (StringUtil.isEmpty(paramValue)) {
			paramValue = request.getParameter(paramName);
		}
		return StringUtil.isEmpty(paramValue) ? EMPTY : paramValue.trim();
	}

	public static String getParamValue(ServerHttpRequest request, String paramName) {
		// 从header中获取
		String paramValue = request.getHeaders().getFirst(paramName);
		// 从参数中获取
		if (StringUtil.isEmpty(paramValue)) {
			paramValue = request.getQueryParams().getFirst(paramName);
		}
		return StringUtil.isEmpty(paramValue) ? EMPTY : paramValue.trim();
	}

}

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

package org.laokou.common.trace.interceptor;

import io.micrometer.common.lang.NonNullApi;
import org.apache.logging.log4j.ThreadContext;
import org.laokou.common.i18n.utils.StringUtil;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import static org.laokou.common.i18n.common.constants.StringConstant.EMPTY;
import static org.laokou.common.i18n.common.constants.TraceConstant.*;

/**
 * @author laokou
 */
@NonNullApi
public class ReactiveTraceInterceptor implements WebFilter {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		try {
			ServerHttpRequest request = exchange.getRequest();
			String userId = getParamValue(request, USER_ID);
			String tenantId = getParamValue(request, TENANT_ID);
			String username = getParamValue(request, USER_NAME);
			String traceId = getParamValue(request, TRACE_ID);
			ThreadContext.put(TRACE_ID, traceId);
			ThreadContext.put(USER_ID, userId);
			ThreadContext.put(TENANT_ID, tenantId);
			ThreadContext.put(USER_NAME, username);
			return chain.filter(exchange);
		}
		finally {
			ThreadContext.clearMap();
		}
	}

	private String getParamValue(ServerHttpRequest request, String paramName) {
		// 从header中获取
		String paramValue = request.getHeaders().getFirst(paramName);
		// 从参数中获取
		if (StringUtil.isEmpty(paramValue)) {
			paramValue = request.getQueryParams().getFirst(paramName);
		}
		return StringUtil.isEmpty(paramValue) ? EMPTY : paramValue.trim();
	}

}

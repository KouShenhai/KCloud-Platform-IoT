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

package org.laokou.gateway.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.gateway.utils.RequestUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.laokou.common.i18n.common.Constant.*;

/**
 * 请求链路
 *
 * @author laokou
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class TraceFilter implements GlobalFilter, Ordered {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		String userId = RequestUtil.getParamValue(request, USER_ID);
		String tenantId = RequestUtil.getParamValue(request, TENANT_ID);
		String username = RequestUtil.getParamValue(request, USER_NAME);
		String traceId = RequestUtil.getParamValue(request, TRACE_ID);
		ThreadContext.put(TRACE_ID, StringUtil.isEmpty(traceId) ? userId + IdGenerator.defaultSnowflakeId() : traceId);
		ThreadContext.put(USER_ID, userId);
		ThreadContext.put(TENANT_ID, tenantId);
		ThreadContext.put(USER_NAME, username);
		// 获取uri
		String requestUri = request.getPath().pathWithinApplication().value();
		log.info("请求路径：{}， 用户ID：{}， 用户名：{}，租户ID：{}，链路ID：{}", requestUri, userId, username, tenantId, ThreadContext.get(TRACE_ID));
		// 清除
		ThreadContext.clearMap();
		return chain.filter(exchange.mutate()
			.request(request.mutate()
				.header(USER_NAME, username)
				.header(TENANT_ID, tenantId)
				.header(USER_ID, userId)
				.header(TRACE_ID, traceId)
				.build())
			.build());
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE + 800;
	}

}

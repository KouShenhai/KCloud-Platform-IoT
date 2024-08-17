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

package org.laokou.gateway.filter;

import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.log4j2.utils.TraceUtil;
import org.laokou.common.nacos.utils.ReactiveRequestUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.alibaba.cloud.nacos.loadbalancer.NacosLoadBalancer.*;
import static org.laokou.common.i18n.common.constant.TraceConstant.*;
import static org.laokou.common.nacos.utils.ReactiveRequestUtil.getHost;

/**
 * 分布式请求链路过滤器.
 *
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TraceFilter implements GlobalFilter, Ordered {

	private final Tracer tracer;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		try {
			ServerHttpRequest request = exchange.getRequest();
			String host = getHost(request);
			String userId = ReactiveRequestUtil.getParamValue(request, USER_ID);
			String tenantId = ReactiveRequestUtil.getParamValue(request, TENANT_ID);
			String username = ReactiveRequestUtil.getParamValue(request, USER_NAME);
			String serviceGray = ReactiveRequestUtil.getParamValue(request, SERVICE_GRAY);
			String serviceHost = ReactiveRequestUtil.getParamValue(request, SERVICE_HOST);
			String servicePort = ReactiveRequestUtil.getParamValue(request, SERVICE_PORT);
			String traceId = ObjectUtil.requireNotNull(tracer.currentTraceContext().context()).traceId();
			String spanId = ObjectUtil.requireNotNull(tracer.currentTraceContext().context()).spanId();
			TraceUtil.putContext(traceId, userId, tenantId, username, spanId);
			log.info("请求路径：{}，用户ID：{}，用户名：{}，租户ID：{}，链路ID：{}，标签ID：{}，主机：{}", ReactiveRequestUtil.getRequestURL(request),
					LogUtil.record(userId), LogUtil.record(username), LogUtil.record(tenantId), LogUtil.record(traceId),
					LogUtil.record(spanId), host);
			return chain.filter(exchange.mutate()
				.request(request.mutate()
					.header(USER_NAME, username)
					.header(TENANT_ID, tenantId)
					.header(USER_ID, userId)
					.header(TRACE_ID, traceId)
					.header(SPAN_ID, spanId)
					.header(SERVICE_HOST, serviceHost)
					.header(SERVICE_PORT, servicePort)
					.header(SERVICE_GRAY, serviceGray)
					.header(DOMAIN_NAME, host)
					.build())
				.build());
		}
		finally {
			// 清除
			TraceUtil.clearContext();
		}
	}

	@Override
	public int getOrder() {
		return HIGHEST_PRECEDENCE + 800;
	}

}

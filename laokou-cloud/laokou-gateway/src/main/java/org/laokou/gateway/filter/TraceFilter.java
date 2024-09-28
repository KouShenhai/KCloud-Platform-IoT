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

import lombok.extern.slf4j.Slf4j;
import org.laokou.common.nacos.utils.ReactiveRequestUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.laokou.common.i18n.common.constant.TraceConstant.*;
import static org.laokou.common.nacos.utils.ReactiveRequestUtil.getHost;

/**
 * 分布式请求链路过滤器.
 *
 * @author laokou
 */
@Slf4j
@Component
public class TraceFilter implements GlobalFilter, Ordered {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		String serviceGray = ReactiveRequestUtil.getParamValue(request, SERVICE_GRAY);
		String serviceHost = ReactiveRequestUtil.getParamValue(request, SERVICE_HOST);
		String servicePort = ReactiveRequestUtil.getParamValue(request, SERVICE_PORT);
		return chain.filter(exchange.mutate()
			.request(request.mutate()
				.header(SERVICE_HOST, serviceHost)
				.header(SERVICE_PORT, servicePort)
				.header(SERVICE_GRAY, serviceGray)
				.header(DOMAIN_NAME, getHost(request))
				.build())
			.build());
	}

	@Override
	public int getOrder() {
		return HIGHEST_PRECEDENCE + 800;
	}

}

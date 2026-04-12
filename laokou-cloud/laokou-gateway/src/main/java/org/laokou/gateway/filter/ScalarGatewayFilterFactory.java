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
import org.jspecify.annotations.NonNull;
import org.laokou.common.reactor.util.ReactiveRequestUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.net.URI;

/**
 * @author laokou
 */
@Slf4j
@Component
public class ScalarGatewayFilterFactory extends AbstractGatewayFilterFactory<ScalarGatewayFilterFactory.@NonNull Config> implements Ordered {

	public ScalarGatewayFilterFactory() {
		super(Config.class);
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE - 2000;
	}

	@NonNull
	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();
			// 获取uri
			String requestURL = ReactiveRequestUtils.getRequestURL(request);
			// scalar重写地址
			if (ReactiveRequestUtils.pathMatcher("/**/scalar", requestURL)) {
				String scalarUrl = ReactiveRequestUtils.getParamValue(request,"scalar_url");
				URI uri = URI.create(scalarUrl);
				return chain.filter(exchange.mutate().request(request.mutate().uri(uri).path(uri.getPath()).build()).build());
			}
			return chain.filter(exchange);
		};
	}

	public static class Config {}

}

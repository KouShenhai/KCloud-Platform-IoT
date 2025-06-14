/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

/*
 * Copyright 2013-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.gateway.filter.factory;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.nacos.util.ReactiveResponseUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.HasRouteId;
import org.springframework.cloud.gateway.support.HttpStatusHolder;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;

import static org.laokou.common.i18n.common.exception.StatusCode.TOO_MANY_REQUESTS;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.setResponseStatus;

/**
 * User Request Rate Limiter filter. See
 * <a href="https://stripe.com/blog/rate-limiters">...</a> and <a href=
 * "https://gist.github.com/ptarjan/e38f45f2dfe601419ca3af937fff574d#file-1-check_request_rate_limiter-rb-L11-L34">...</a>.
 */
@Getter
@ConfigurationProperties("spring.cloud.gateway.server.webflux.filter.request-rate-limiter")
public class RequestRateLimiterGatewayFilterFactory
		extends AbstractGatewayFilterFactory<RequestRateLimiterGatewayFilterFactory.Config> {

	private static final String EMPTY_KEY = "____EMPTY_KEY__";

	private final RateLimiter<?> defaultRateLimiter;

	private final KeyResolver defaultKeyResolver;

	/**
	 * Switch to deny requests if the Key Resolver returns an empty key, defaults to true.
	 */
	@Setter
	private boolean denyEmptyKey = true;

	/** HttpStatus to return when denyEmptyKey is true, defaults to FORBIDDEN. */
	@Setter
	private String emptyKeyStatusCode = HttpStatus.FORBIDDEN.name();

	public RequestRateLimiterGatewayFilterFactory(RateLimiter<?> defaultRateLimiter, KeyResolver defaultKeyResolver) {
		super(Config.class);
		this.defaultRateLimiter = defaultRateLimiter;
		this.defaultKeyResolver = defaultKeyResolver;
	}

	@Override
	public GatewayFilter apply(Config config) {
		KeyResolver resolver = getOrDefault(config.keyResolver, defaultKeyResolver);
		RateLimiter<?> limiter = getOrDefault(config.rateLimiter, defaultRateLimiter);
		boolean denyEmpty = getOrDefault(config.denyEmptyKey, this.denyEmptyKey);
		HttpStatusHolder emptyKeyStatus = HttpStatusHolder
			.parse(getOrDefault(config.emptyKeyStatus, this.emptyKeyStatusCode));
		return (exchange, chain) -> resolver.resolve(exchange).defaultIfEmpty(EMPTY_KEY).flatMap(key -> {
			if (EMPTY_KEY.equals(key)) {
				if (denyEmpty) {
					setResponseStatus(exchange, emptyKeyStatus);
					return exchange.getResponse().setComplete();
				}
				return chain.filter(exchange);
			}
			String routeId = config.getRouteId();
			if (routeId == null) {
				Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
				Assert.notNull(route, "Route is null");
				routeId = route.getId();
			}
			return limiter.isAllowed(routeId, key).flatMap(response -> {
				for (Map.Entry<String, String> header : response.getHeaders().entrySet()) {
					exchange.getResponse().getHeaders().add(header.getKey(), header.getValue());
				}
				if (response.isAllowed()) {
					return chain.filter(exchange);
				}
				return ReactiveResponseUtils.responseOk(exchange, Result.fail(TOO_MANY_REQUESTS));
			});
		});
	}

	private <T> T getOrDefault(T configValue, T defaultValue) {
		return (configValue != null) ? configValue : defaultValue;
	}

	public static class Config implements HasRouteId {

		@Getter
		private KeyResolver keyResolver;

		@Getter
		private RateLimiter<?> rateLimiter;

		@Getter
		private HttpStatus statusCode = HttpStatus.TOO_MANY_REQUESTS;

		@Getter
		private Boolean denyEmptyKey;

		@Getter
		private String emptyKeyStatus;

		private String routeId;

		public Config setKeyResolver(KeyResolver keyResolver) {
			this.keyResolver = keyResolver;
			return this;
		}

		public Config setRateLimiter(RateLimiter<?> rateLimiter) {
			this.rateLimiter = rateLimiter;
			return this;
		}

		public Config setStatusCode(HttpStatus statusCode) {
			this.statusCode = statusCode;
			return this;
		}

		public Config setDenyEmptyKey(Boolean denyEmptyKey) {
			this.denyEmptyKey = denyEmptyKey;
			return this;
		}

		public Config setEmptyKeyStatus(String emptyKeyStatus) {
			this.emptyKeyStatus = emptyKeyStatus;
			return this;
		}

		@Override
		public void setRouteId(String routeId) {
			this.routeId = routeId;
		}

		@Override
		public String getRouteId() {
			return this.routeId;
		}

	}

}

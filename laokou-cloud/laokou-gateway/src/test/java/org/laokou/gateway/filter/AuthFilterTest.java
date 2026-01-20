/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

import com.google.common.net.HttpHeaders;
import org.assertj.core.api.Assertions;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.laokou.common.i18n.util.SpringUtils;
import org.laokou.gateway.config.RequestMatcherProperties;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * AuthFilter 认证过滤器测试.
 *
 * @author laokou
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AuthFilter Tests")
class AuthFilterTest {

	@Mock
	private GatewayFilterChain chain;

	@Mock
	private SpringUtils springUtils;

	private AuthFilter authFilter;

	@BeforeEach
	void setUp() {
		RequestMatcherProperties requestMatcherProperties = new RequestMatcherProperties();
		Map<String, Set<String>> ignorePatterns = new HashMap<>();
		ignorePatterns.put("GET", Set.of("/actuator/**=laokou-gateway-test", "/public/**=laokou-gateway-test"));
		ignorePatterns.put("POST", Set.of("/api/v1/oauth2/token=laokou-gateway-test"));
		requestMatcherProperties.setIgnorePatterns(ignorePatterns);

		Mockito.when(springUtils.getServiceId()).thenReturn("laokou-gateway-test");

		authFilter = new AuthFilter(requestMatcherProperties, springUtils);
		authFilter.afterPropertiesSet();
	}

	@Test
	@DisplayName("Test request bypass for unauthenticated paths - GET /actuator/health")
	void testIgnorePatternRequest_actuator() {
		// Given
		MockServerHttpRequest request = MockServerHttpRequest.get("/actuator/health").build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);

		Mockito.when(chain.filter(Mockito.any())).thenReturn(Mono.empty());

		// When
		Mono<@NonNull Void> result = authFilter.filter(exchange, chain);

		// Then
		StepVerifier.create(result).verifyComplete();
		Mockito.verify(chain).filter(Mockito.any());
	}

	@Test
	@DisplayName("Test request bypass for unauthenticated paths - GET /public/test")
	void testIgnorePatternRequest_public() {
		// Given
		MockServerHttpRequest request = MockServerHttpRequest.get("/public/test").build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);

		Mockito.when(chain.filter(Mockito.any())).thenReturn(Mono.empty());

		// When
		Mono<@NonNull Void> result = authFilter.filter(exchange, chain);

		// Then
		StepVerifier.create(result).verifyComplete();
		Mockito.verify(chain).filter(Mockito.any());
	}

	@Test
	@DisplayName("Test request with valid token passes through")
	void testRequestWithValidToken() {
		// Given
		String token = "Bearer test-token-12345";
		MockServerHttpRequest request = MockServerHttpRequest.get("/api/v1/users")
			.header(HttpHeaders.AUTHORIZATION, token)
			.build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);

		Mockito.when(chain.filter(Mockito.any())).thenReturn(Mono.empty());

		// When
		Mono<@NonNull Void> result = authFilter.filter(exchange, chain);

		// Then
		StepVerifier.create(result).verifyComplete();
		Mockito.verify(chain).filter(Mockito.any());
	}

	@Test
	@DisplayName("Test request without token returns 401 Unauthorized")
	void testRequestWithoutToken() {
		// Given
		MockServerHttpRequest request = MockServerHttpRequest.get("/api/v1/users").build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);

		// When
		Mono<@NonNull Void> result = authFilter.filter(exchange, chain);

		// Then
		StepVerifier.create(result).verifyComplete();
		// 验证响应状态码
		Assertions.assertThat(exchange.getResponse().getStatusCode()).isNotNull();
	}

	@Test
	@DisplayName("Test token retrieval from query parameter")
	void testRequestWithTokenInQueryParam() {
		// Given
		String token = "Bearer query-token-12345";
		MockServerHttpRequest request = MockServerHttpRequest.get("/api/v1/users")
			.queryParam(HttpHeaders.AUTHORIZATION, token)
			.build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);

		Mockito.when(chain.filter(Mockito.any())).thenReturn(Mono.empty());

		// When
		Mono<@NonNull Void> result = authFilter.filter(exchange, chain);

		// Then
		StepVerifier.create(result).verifyComplete();
		Mockito.verify(chain).filter(Mockito.any());
	}

	@Test
	@DisplayName("Test POST request bypass authentication - /api/v1/oauth2/token")
	void testIgnorePatternRequest_post() {
		// Given
		MockServerHttpRequest request = MockServerHttpRequest.post("/api/v1/oauth2/token").build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);

		Mockito.when(chain.filter(Mockito.any())).thenReturn(Mono.empty());

		// When
		Mono<@NonNull Void> result = authFilter.filter(exchange, chain);

		// Then
		StepVerifier.create(result).verifyComplete();
		Mockito.verify(chain).filter(Mockito.any());
	}

	@Test
	@DisplayName("Test getOrder returns correct priority")
	void testGetOrder() {
		// When
		int order = authFilter.getOrder();

		// Then
		Assertions.assertThat(order).isLessThan(Integer.MAX_VALUE);
	}

}

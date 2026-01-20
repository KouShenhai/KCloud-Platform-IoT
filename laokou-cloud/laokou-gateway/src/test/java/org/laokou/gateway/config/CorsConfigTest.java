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

package org.laokou.gateway.config;

import org.assertj.core.api.Assertions;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * CorsConfig CORS configuration test.
 *
 * @author laokou
 */
@DisplayName("CorsConfig Tests")
class CorsConfigTest {

	private WebFilter corsFilter;

	@BeforeEach
	void setUp() {
		CorsConfig corsConfig = new CorsConfig();
		corsFilter = corsConfig.corsFilter();
	}

	@Test
	@DisplayName("Test non-CORS request forwards normally")
	void testNonCorsRequest() {
		// Given - non-CORS request without Origin header
		MockServerHttpRequest request = MockServerHttpRequest.get("http://localhost:8080/api/v1/users").build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);

		WebFilterChain chain = _ -> Mono.empty();

		// When
		Mono<@NonNull Void> result = corsFilter.filter(exchange, chain);

		// Then
		StepVerifier.create(result).verifyComplete();
		// Non-CORS request should not add CORS response headers
		Assertions.assertThat(exchange.getResponse().getHeaders().get(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN))
			.isNull();
	}

	@Test
	@DisplayName("Test CORS request adds correct Access-Control headers")
	void testCorsRequest() {
		// Given
		String origin = "http://localhost:3000";
		MockServerHttpRequest request = MockServerHttpRequest.get("http://localhost:8080/api/v1/users")
			.header(HttpHeaders.ORIGIN, origin)
			.build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);

		WebFilterChain chain = _ -> Mono.empty();

		// When
		Mono<@NonNull Void> result = corsFilter.filter(exchange, chain);

		// Then
		StepVerifier.create(result).verifyComplete();
		HttpHeaders responseHeaders = exchange.getResponse().getHeaders();
		Assertions.assertThat(responseHeaders.getAccessControlAllowOrigin()).isEqualTo(origin);
		Assertions.assertThat(responseHeaders.getAccessControlAllowCredentials()).isTrue();
		Assertions.assertThat(responseHeaders.getAccessControlMaxAge()).isEqualTo(3600L);
	}

	@Test
	@DisplayName("Test CORS preflight request returns 200 OK")
	void testCorsPreflightRequest() {
		// Given
		String origin = "http://localhost:3000";
		MockServerHttpRequest request = MockServerHttpRequest.options("http://localhost:8080/api/v1/users")
			.header(HttpHeaders.ORIGIN, origin)
			.header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "POST")
			.header(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, "Content-Type")
			.build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);

		WebFilterChain chain = _ -> Mono.empty();

		// When
		Mono<@NonNull Void> result = corsFilter.filter(exchange, chain);

		// Then
		StepVerifier.create(result).verifyComplete();
		Assertions.assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.OK);

		HttpHeaders responseHeaders = exchange.getResponse().getHeaders();
		Assertions.assertThat(responseHeaders.getAccessControlAllowOrigin()).isEqualTo(origin);
		Assertions.assertThat(responseHeaders.getAccessControlAllowMethods()).contains(HttpMethod.POST);
	}

	@Test
	@DisplayName("Test CORS request with multiple headers")
	void testCorsRequestWithMultipleHeaders() {
		// Given
		String origin = "http://example.com";
		MockServerHttpRequest request = MockServerHttpRequest.get("http://localhost:8080/api/v1/data")
			.header(HttpHeaders.ORIGIN, origin)
			.header(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, "Content-Type, Authorization")
			.build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);

		WebFilterChain chain = _ -> Mono.empty();

		// When
		Mono<@NonNull Void> result = corsFilter.filter(exchange, chain);

		// Then
		StepVerifier.create(result).verifyComplete();
		HttpHeaders responseHeaders = exchange.getResponse().getHeaders();
		// Headers may be split into individual values or kept as a single string
		Assertions.assertThat(responseHeaders.getAccessControlAllowHeaders())
			.containsAnyOf("Content-Type", "Content-Type, Authorization");
	}

	@Test
	@DisplayName("Test corsFilter Bean is not null")
	void testCorsFilterBeanNotNull() {
		// Then
		Assertions.assertThat(corsFilter).isNotNull();
	}

}

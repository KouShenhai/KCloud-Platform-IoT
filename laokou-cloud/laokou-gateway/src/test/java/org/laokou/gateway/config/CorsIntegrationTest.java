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
import org.junit.jupiter.api.Nested;
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
 * CORS 集成测试 - 完整的跨域请求场景测试.
 *
 * @author laokou
 */
@DisplayName("CORS Integration Tests")
class CorsIntegrationTest {

	private WebFilter corsFilter;

	@BeforeEach
	void setUp() {
		CorsConfig corsConfig = new CorsConfig();
		corsFilter = corsConfig.corsFilter();
	}

	@Nested
	@DisplayName("Simple CORS Requests")
	class SimpleCorsRequests {

		@Test
		@DisplayName("Test simple GET request with Origin header")
		void test_filter_simpleGetWithOrigin_shouldAddCorsHeaders() {
			// Given
			String origin = "https://example.com";
			MockServerHttpRequest request = MockServerHttpRequest.get("http://localhost:8080/api/data")
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
		}

		@Test
		@DisplayName("Test simple POST request with Origin header")
		void test_filter_simplePostWithOrigin_shouldAddCorsHeaders() {
			// Given
			String origin = "https://app.example.com";
			MockServerHttpRequest request = MockServerHttpRequest.post("http://localhost:8080/api/users")
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
		}

	}

	@Nested
	@DisplayName("Preflight CORS Requests")
	class PreflightCorsRequests {

		@Test
		@DisplayName("Test preflight OPTIONS request for POST")
		void test_filter_preflightPost_shouldReturnOk() {
			// Given
			String origin = "https://frontend.example.com";
			MockServerHttpRequest request = MockServerHttpRequest.options("http://localhost:8080/api/users")
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
			Assertions.assertThat(responseHeaders.getAccessControlMaxAge()).isEqualTo(3600L);
		}

		@Test
		@DisplayName("Test preflight OPTIONS request for PUT")
		void test_filter_preflightPut_shouldReturnOk() {
			// Given
			String origin = "https://admin.example.com";
			MockServerHttpRequest request = MockServerHttpRequest.options("http://localhost:8080/api/users/1")
				.header(HttpHeaders.ORIGIN, origin)
				.header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "PUT")
				.header(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, "Content-Type, Authorization")
				.build();
			MockServerWebExchange exchange = MockServerWebExchange.from(request);
			WebFilterChain chain = _ -> Mono.empty();

			// When
			Mono<@NonNull Void> result = corsFilter.filter(exchange, chain);

			// Then
			StepVerifier.create(result).verifyComplete();
			Assertions.assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.OK);

			HttpHeaders responseHeaders = exchange.getResponse().getHeaders();
			Assertions.assertThat(responseHeaders.getAccessControlAllowMethods()).contains(HttpMethod.PUT);
		}

		@Test
		@DisplayName("Test preflight OPTIONS request for DELETE")
		void test_filter_preflightDelete_shouldReturnOk() {
			// Given
			String origin = "https://console.example.com";
			MockServerHttpRequest request = MockServerHttpRequest.options("http://localhost:8080/api/users/1")
				.header(HttpHeaders.ORIGIN, origin)
				.header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "DELETE")
				.build();
			MockServerWebExchange exchange = MockServerWebExchange.from(request);
			WebFilterChain chain = _ -> Mono.empty();

			// When
			Mono<@NonNull Void> result = corsFilter.filter(exchange, chain);

			// Then
			StepVerifier.create(result).verifyComplete();
			Assertions.assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.OK);

			HttpHeaders responseHeaders = exchange.getResponse().getHeaders();
			Assertions.assertThat(responseHeaders.getAccessControlAllowMethods()).contains(HttpMethod.DELETE);
		}

		@Test
		@DisplayName("Test preflight OPTIONS request for PATCH")
		void test_filter_preflightPatch_shouldReturnOk() {
			// Given
			String origin = "https://dashboard.example.com";
			MockServerHttpRequest request = MockServerHttpRequest.options("http://localhost:8080/api/users/1")
				.header(HttpHeaders.ORIGIN, origin)
				.header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "PATCH")
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
			Assertions.assertThat(responseHeaders.getAccessControlAllowMethods()).contains(HttpMethod.PATCH);
		}

	}

	@Nested
	@DisplayName("Non-CORS Requests")
	class NonCorsRequests {

		@Test
		@DisplayName("Test same-origin request without Origin header")
		void test_filter_sameOrigin_shouldNotAddCorsHeaders() {
			// Given - no Origin header means not a CORS request
			MockServerHttpRequest request = MockServerHttpRequest.get("http://localhost:8080/api/data").build();
			MockServerWebExchange exchange = MockServerWebExchange.from(request);
			WebFilterChain chain = _ -> Mono.empty();

			// When
			Mono<@NonNull Void> result = corsFilter.filter(exchange, chain);

			// Then
			StepVerifier.create(result).verifyComplete();
			// No CORS headers should be added
			Assertions.assertThat(exchange.getResponse().getHeaders().getAccessControlAllowOrigin()).isNull();
		}

		@Test
		@DisplayName("Test request with null Origin header")
		void test_filter_nullOrigin_shouldNotAddCorsHeaders() {
			// Given
			MockServerHttpRequest request = MockServerHttpRequest.get("http://localhost:8080/api/data").build();
			MockServerWebExchange exchange = MockServerWebExchange.from(request);
			WebFilterChain chain = _ -> Mono.empty();

			// When
			Mono<@NonNull Void> result = corsFilter.filter(exchange, chain);

			// Then
			StepVerifier.create(result).verifyComplete();
			Assertions.assertThat(exchange.getResponse().getHeaders().getAccessControlAllowOrigin()).isNull();
		}

	}

	@Nested
	@DisplayName("Various Origin Patterns")
	class VariousOriginPatterns {

		@Test
		@DisplayName("Test localhost origin")
		void test_filter_localhostOrigin_shouldAllowCors() {
			// Given
			String origin = "http://localhost:3000";
			MockServerHttpRequest request = MockServerHttpRequest.get("http://localhost:8080/api/data")
				.header(HttpHeaders.ORIGIN, origin)
				.build();
			MockServerWebExchange exchange = MockServerWebExchange.from(request);
			WebFilterChain chain = _ -> Mono.empty();

			// When
			Mono<@NonNull Void> result = corsFilter.filter(exchange, chain);

			// Then
			StepVerifier.create(result).verifyComplete();
			Assertions.assertThat(exchange.getResponse().getHeaders().getAccessControlAllowOrigin()).isEqualTo(origin);
		}

		@Test
		@DisplayName("Test IP address origin")
		void test_filter_ipAddressOrigin_shouldAllowCors() {
			// Given
			String origin = "http://192.168.1.100:8080";
			MockServerHttpRequest request = MockServerHttpRequest.get("http://localhost:8080/api/data")
				.header(HttpHeaders.ORIGIN, origin)
				.build();
			MockServerWebExchange exchange = MockServerWebExchange.from(request);
			WebFilterChain chain = _ -> Mono.empty();

			// When
			Mono<@NonNull Void> result = corsFilter.filter(exchange, chain);

			// Then
			StepVerifier.create(result).verifyComplete();
			Assertions.assertThat(exchange.getResponse().getHeaders().getAccessControlAllowOrigin()).isEqualTo(origin);
		}

		@Test
		@DisplayName("Test HTTPS origin")
		void test_filter_httpsOrigin_shouldAllowCors() {
			// Given
			String origin = "https://secure.example.com";
			MockServerHttpRequest request = MockServerHttpRequest.get("http://localhost:8080/api/data")
				.header(HttpHeaders.ORIGIN, origin)
				.build();
			MockServerWebExchange exchange = MockServerWebExchange.from(request);
			WebFilterChain chain = _ -> Mono.empty();

			// When
			Mono<@NonNull Void> result = corsFilter.filter(exchange, chain);

			// Then
			StepVerifier.create(result).verifyComplete();
			Assertions.assertThat(exchange.getResponse().getHeaders().getAccessControlAllowOrigin()).isEqualTo(origin);
		}

		@Test
		@DisplayName("Test subdomain origin")
		void test_filter_subdomainOrigin_shouldAllowCors() {
			// Given
			String origin = "https://api.subdomain.example.com";
			MockServerHttpRequest request = MockServerHttpRequest.get("http://localhost:8080/api/data")
				.header(HttpHeaders.ORIGIN, origin)
				.build();
			MockServerWebExchange exchange = MockServerWebExchange.from(request);
			WebFilterChain chain = _ -> Mono.empty();

			// When
			Mono<@NonNull Void> result = corsFilter.filter(exchange, chain);

			// Then
			StepVerifier.create(result).verifyComplete();
			Assertions.assertThat(exchange.getResponse().getHeaders().getAccessControlAllowOrigin()).isEqualTo(origin);
		}

	}

	@Nested
	@DisplayName("CORS Headers Validation")
	class CorsHeadersValidation {

		@Test
		@DisplayName("Test Access-Control-Allow-Credentials is set to true")
		void test_filter_corsRequest_shouldSetAllowCredentials() {
			// Given
			String origin = "https://example.com";
			MockServerHttpRequest request = MockServerHttpRequest.get("http://localhost:8080/api/data")
				.header(HttpHeaders.ORIGIN, origin)
				.build();
			MockServerWebExchange exchange = MockServerWebExchange.from(request);
			WebFilterChain chain = _ -> Mono.empty();

			// When
			Mono<@NonNull Void> result = corsFilter.filter(exchange, chain);

			// Then
			StepVerifier.create(result).verifyComplete();
			Assertions.assertThat(exchange.getResponse().getHeaders().getAccessControlAllowCredentials()).isTrue();
		}

		@Test
		@DisplayName("Test Access-Control-Max-Age is set to 3600")
		void test_filter_corsRequest_shouldSetMaxAge() {
			// Given
			String origin = "https://example.com";
			MockServerHttpRequest request = MockServerHttpRequest.get("http://localhost:8080/api/data")
				.header(HttpHeaders.ORIGIN, origin)
				.build();
			MockServerWebExchange exchange = MockServerWebExchange.from(request);
			WebFilterChain chain = _ -> Mono.empty();

			// When
			Mono<@NonNull Void> result = corsFilter.filter(exchange, chain);

			// Then
			StepVerifier.create(result).verifyComplete();
			Assertions.assertThat(exchange.getResponse().getHeaders().getAccessControlMaxAge()).isEqualTo(3600L);
		}

		@Test
		@DisplayName("Test Access-Control-Expose-Headers is set to *")
		void test_filter_corsRequest_shouldSetExposeHeaders() {
			// Given
			String origin = "https://example.com";
			MockServerHttpRequest request = MockServerHttpRequest.get("http://localhost:8080/api/data")
				.header(HttpHeaders.ORIGIN, origin)
				.build();
			MockServerWebExchange exchange = MockServerWebExchange.from(request);
			WebFilterChain chain = _ -> Mono.empty();

			// When
			Mono<@NonNull Void> result = corsFilter.filter(exchange, chain);

			// Then
			StepVerifier.create(result).verifyComplete();
			Assertions.assertThat(exchange.getResponse().getHeaders().getAccessControlExposeHeaders()).contains("*");
		}

	}

}

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
import org.junit.jupiter.api.Nested;
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
 * AuthFilter 集成测试 - 完整的认证流程测试.
 *
 * @author laokou
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AuthFilter Integration Tests")
class AuthFilterIntegrationTest {

	@Mock
	private GatewayFilterChain chain;

	@Mock
	private SpringUtils springUtils;

	private AuthFilter authFilter;

	@BeforeEach
	void setUp() {
		RequestMatcherProperties requestMatcherProperties = new RequestMatcherProperties();
		Map<String, Set<String>> ignorePatterns = new HashMap<>();
		ignorePatterns.put("GET",
				Set.of("/actuator/**=laokou-gateway-test", "/public/**=laokou-gateway-test",
						"/v3/api-docs/**=laokou-gateway-test", "/swagger-ui/**=laokou-gateway-test",
						"/health/**=laokou-gateway-test"));
		ignorePatterns.put("POST", Set.of("/api/v1/oauth2/token=laokou-gateway-test"));
		ignorePatterns.put("DELETE", Set.of("/api/v1/logout=laokou-gateway-test"));
		requestMatcherProperties.setIgnorePatterns(ignorePatterns);

		Mockito.when(springUtils.getServiceId()).thenReturn("laokou-gateway-test");

		authFilter = new AuthFilter(requestMatcherProperties, springUtils);
		authFilter.afterPropertiesSet();
	}

	@Nested
	@DisplayName("Public Path Access Tests")
	class PublicPathAccessTests {

		@Test
		@DisplayName("Test actuator endpoints are publicly accessible")
		void test_filter_actuatorEndpoints_shouldPassThrough() {
			// Given
			String[] actuatorPaths = { "/actuator/health", "/actuator/health/live", "/actuator/health/ready",
					"/actuator/info", "/actuator/metrics" };

			for (String path : actuatorPaths) {
				MockServerHttpRequest request = MockServerHttpRequest.get(path).build();
				MockServerWebExchange exchange = MockServerWebExchange.from(request);

				Mockito.when(chain.filter(Mockito.any())).thenReturn(Mono.empty());

				// When
				Mono<@NonNull Void> result = authFilter.filter(exchange, chain);

				// Then
				StepVerifier.create(result).verifyComplete();
			}
		}

		@Test
		@DisplayName("Test Swagger/OpenAPI endpoints are publicly accessible")
		void test_filter_swaggerEndpoints_shouldPassThrough() {
			// Given
			String[] swaggerPaths = { "/v3/api-docs", "/v3/api-docs/swagger-config", "/swagger-ui/index.html",
					"/swagger-ui/swagger-ui.css" };

			for (String path : swaggerPaths) {
				MockServerHttpRequest request = MockServerHttpRequest.get(path).build();
				MockServerWebExchange exchange = MockServerWebExchange.from(request);

				Mockito.when(chain.filter(Mockito.any())).thenReturn(Mono.empty());

				// When
				Mono<@NonNull Void> result = authFilter.filter(exchange, chain);

				// Then
				StepVerifier.create(result).verifyComplete();
			}
		}

		@Test
		@DisplayName("Test public resources are accessible without token")
		void test_filter_publicResources_shouldPassThrough() {
			// Given
			String[] publicPaths = { "/public/css/style.css", "/public/js/app.js", "/public/images/logo.png",
					"/public/fonts/roboto.woff2" };

			for (String path : publicPaths) {
				MockServerHttpRequest request = MockServerHttpRequest.get(path).build();
				MockServerWebExchange exchange = MockServerWebExchange.from(request);

				Mockito.when(chain.filter(Mockito.any())).thenReturn(Mono.empty());

				// When
				Mono<@NonNull Void> result = authFilter.filter(exchange, chain);

				// Then
				StepVerifier.create(result).verifyComplete();
			}
		}

	}

	@Nested
	@DisplayName("Protected Path Access Tests")
	class ProtectedPathAccessTests {

		@Test
		@DisplayName("Test protected API endpoints require authentication")
		void test_filter_protectedEndpoints_shouldReturn401() {
			// Given
			String[] protectedPaths = { "/api/v1/users", "/api/v1/roles", "/api/v1/menus", "/api/v1/depts",
					"/api/v1/devices" };

			for (String path : protectedPaths) {
				MockServerHttpRequest request = MockServerHttpRequest.get(path).build();
				MockServerWebExchange exchange = MockServerWebExchange.from(request);

				// When
				Mono<@NonNull Void> result = authFilter.filter(exchange, chain);

				// Then
				StepVerifier.create(result).verifyComplete();
				Assertions.assertThat(exchange.getResponse().getStatusCode()).isNotNull();
			}
		}

		@Test
		@DisplayName("Test protected endpoints accessible with valid token")
		void test_filter_protectedWithToken_shouldPassThrough() {
			// Given
			String token = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.test-token";
			String[] protectedPaths = { "/api/v1/users", "/api/v1/users/1", "/api/v1/roles/list" };

			for (String path : protectedPaths) {
				MockServerHttpRequest request = MockServerHttpRequest.get(path)
					.header(HttpHeaders.AUTHORIZATION, token)
					.build();
				MockServerWebExchange exchange = MockServerWebExchange.from(request);

				Mockito.when(chain.filter(Mockito.any())).thenReturn(Mono.empty());

				// When
				Mono<@NonNull Void> result = authFilter.filter(exchange, chain);

				// Then
				StepVerifier.create(result).verifyComplete();
			}
		}

	}

	@Nested
	@DisplayName("HTTP Method Tests")
	class HttpMethodTests {

		@Test
		@DisplayName("Test all HTTP methods require token for protected paths")
		void test_filter_allMethods_shouldRequireToken() {
			// Given - protected path without token
			String path = "/api/v1/users";

			// GET
			MockServerHttpRequest getRequest = MockServerHttpRequest.get(path).build();
			MockServerWebExchange getExchange = MockServerWebExchange.from(getRequest);
			StepVerifier.create(authFilter.filter(getExchange, chain)).verifyComplete();
			Assertions.assertThat(getExchange.getResponse().getStatusCode()).isNotNull();

			// POST
			MockServerHttpRequest postRequest = MockServerHttpRequest.post(path).build();
			MockServerWebExchange postExchange = MockServerWebExchange.from(postRequest);
			StepVerifier.create(authFilter.filter(postExchange, chain)).verifyComplete();
			Assertions.assertThat(postExchange.getResponse().getStatusCode()).isNotNull();

			// PUT
			MockServerHttpRequest putRequest = MockServerHttpRequest.put(path + "/1").build();
			MockServerWebExchange putExchange = MockServerWebExchange.from(putRequest);
			StepVerifier.create(authFilter.filter(putExchange, chain)).verifyComplete();
			Assertions.assertThat(putExchange.getResponse().getStatusCode()).isNotNull();

			// DELETE
			MockServerHttpRequest deleteRequest = MockServerHttpRequest.delete(path + "/1").build();
			MockServerWebExchange deleteExchange = MockServerWebExchange.from(deleteRequest);
			StepVerifier.create(authFilter.filter(deleteExchange, chain)).verifyComplete();
			Assertions.assertThat(deleteExchange.getResponse().getStatusCode()).isNotNull();
		}

		@Test
		@DisplayName("Test token from header takes priority")
		void test_filter_headerToken_shouldTakePriority() {
			// Given
			String headerToken = "Bearer header-token";
			String queryToken = "Bearer query-token";
			MockServerHttpRequest request = MockServerHttpRequest.get("/api/v1/users")
				.header(HttpHeaders.AUTHORIZATION, headerToken)
				.queryParam(HttpHeaders.AUTHORIZATION, queryToken)
				.build();
			MockServerWebExchange exchange = MockServerWebExchange.from(request);

			Mockito.when(chain.filter(Mockito.any())).thenReturn(Mono.empty());

			// When
			Mono<@NonNull Void> result = authFilter.filter(exchange, chain);

			// Then
			StepVerifier.create(result).verifyComplete();
			Mockito.verify(chain).filter(Mockito.any());
		}

	}

	@Nested
	@DisplayName("OAuth2 Token Endpoint Tests")
	class OAuth2TokenEndpointTests {

		@Test
		@DisplayName("Test OAuth2 token endpoint is publicly accessible")
		void test_filter_oauth2TokenEndpoint_shouldPassThrough() {
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
		@DisplayName("Test logout endpoint is publicly accessible")
		void test_filter_logoutEndpoint_shouldPassThrough() {
			// Given
			MockServerHttpRequest request = MockServerHttpRequest.delete("/api/v1/logout").build();
			MockServerWebExchange exchange = MockServerWebExchange.from(request);

			Mockito.when(chain.filter(Mockito.any())).thenReturn(Mono.empty());

			// When
			Mono<@NonNull Void> result = authFilter.filter(exchange, chain);

			// Then
			StepVerifier.create(result).verifyComplete();
			Mockito.verify(chain).filter(Mockito.any());
		}

	}

	@Nested
	@DisplayName("Edge Cases")
	class EdgeCases {

		@Test
		@DisplayName("Test empty path")
		void test_filter_emptyPath_shouldHandle() {
			// Given
			MockServerHttpRequest request = MockServerHttpRequest.get("/").build();
			MockServerWebExchange exchange = MockServerWebExchange.from(request);

			// When
			Mono<@NonNull Void> result = authFilter.filter(exchange, chain);

			// Then
			StepVerifier.create(result).verifyComplete();
		}

		@Test
		@DisplayName("Test path with special characters")
		void test_filter_specialCharsPath_shouldHandle() {
			// Given
			String token = "Bearer test-token";
			MockServerHttpRequest request = MockServerHttpRequest.get("/api/v1/users/search?name=test%20user")
				.header(HttpHeaders.AUTHORIZATION, token)
				.build();
			MockServerWebExchange exchange = MockServerWebExchange.from(request);

			Mockito.when(chain.filter(Mockito.any())).thenReturn(Mono.empty());

			// When
			Mono<@NonNull Void> result = authFilter.filter(exchange, chain);

			// Then
			StepVerifier.create(result).verifyComplete();
		}

		@Test
		@DisplayName("Test token with Bearer prefix")
		void test_filter_bearerToken_shouldPassThrough() {
			// Given
			String token = "Bearer eyJhbGciOiJSUzI1NiJ9.test";
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
		@DisplayName("Test token without Bearer prefix")
		void test_filter_tokenWithoutBearer_shouldPassThrough() {
			// Given
			String token = "eyJhbGciOiJSUzI1NiJ9.test";
			MockServerHttpRequest request = MockServerHttpRequest.get("/api/v1/users")
				.header(HttpHeaders.AUTHORIZATION, token)
				.build();
			MockServerWebExchange exchange = MockServerWebExchange.from(request);

			Mockito.when(chain.filter(Mockito.any())).thenReturn(Mono.empty());

			// When
			Mono<@NonNull Void> result = authFilter.filter(exchange, chain);

			// Then
			StepVerifier.create(result).verifyComplete();
		}

	}

}

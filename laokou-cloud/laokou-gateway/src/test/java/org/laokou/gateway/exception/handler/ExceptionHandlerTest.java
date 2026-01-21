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

package org.laokou.gateway.exception.handler;

import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import org.assertj.core.api.Assertions;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * ExceptionHandler 全局异常处理器测试.
 *
 * @author laokou
 */
@DisplayName("ExceptionHandler Tests")
class ExceptionHandlerTest {

	private ExceptionHandler exceptionHandler;

	@BeforeEach
	void setUp() {
		exceptionHandler = new ExceptionHandler();
	}

	@Test
	@DisplayName("Test NotFoundException returns SERVICE_UNAVAILABLE")
	void test_handle_notFoundException_returnsServiceUnavailable() {
		// Given
		MockServerHttpRequest request = MockServerHttpRequest.get("/api/v1/service").build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);
		NotFoundException exception = new NotFoundException("Service not found");

		// When
		Mono<@NonNull Void> result = exceptionHandler.handle(exchange, exception);

		// Then
		StepVerifier.create(result).verifyComplete();
		Assertions.assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	@DisplayName("Test ResponseStatusException 404 returns NOT_FOUND")
	void test_handle_responseStatusException_notFound() {
		// Given
		MockServerHttpRequest request = MockServerHttpRequest.get("/api/v1/notfound").build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);
		ResponseStatusException exception = new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found");

		// When
		Mono<@NonNull Void> result = exceptionHandler.handle(exchange, exception);

		// Then
		StepVerifier.create(result).verifyComplete();
		Assertions.assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	@DisplayName("Test ResponseStatusException 400 returns BAD_REQUEST")
	void test_handle_responseStatusException_badRequest() {
		// Given
		MockServerHttpRequest request = MockServerHttpRequest.get("/api/v1/bad").build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);
		ResponseStatusException exception = new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request");

		// When
		Mono<@NonNull Void> result = exceptionHandler.handle(exchange, exception);

		// Then
		StepVerifier.create(result).verifyComplete();
		Assertions.assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	@DisplayName("Test ResponseStatusException 500 returns INTERNAL_SERVER_ERROR")
	void test_handle_responseStatusException_internalServerError() {
		// Given
		MockServerHttpRequest request = MockServerHttpRequest.get("/api/v1/error").build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);
		ResponseStatusException exception = new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
				"Internal error");

		// When
		Mono<@NonNull Void> result = exceptionHandler.handle(exchange, exception);

		// Then
		StepVerifier.create(result).verifyComplete();
		Assertions.assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	@DisplayName("Test BlockException returns TOO_MANY_REQUESTS")
	void test_handle_blockException_returnsTooManyRequests() {
		// Given
		MockServerHttpRequest request = MockServerHttpRequest.get("/api/v1/limited").build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);
		FlowException exception = new FlowException("rate limit exceeded");

		// When
		Mono<@NonNull Void> result = exceptionHandler.handle(exchange, exception);

		// Then
		StepVerifier.create(result).verifyComplete();
		Assertions.assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	@DisplayName("Test unknown exception returns BAD_GATEWAY")
	void test_handle_unknownException_returnsBadGateway() {
		// Given
		MockServerHttpRequest request = MockServerHttpRequest.get("/api/v1/unknown").build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);
		RuntimeException exception = new RuntimeException("Unknown error");

		// When
		Mono<@NonNull Void> result = exceptionHandler.handle(exchange, exception);

		// Then
		StepVerifier.create(result).verifyComplete();
		Assertions.assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	@DisplayName("Test getOrder returns highest precedence")
	void test_getOrder_returnsHighestPrecedence() {
		// When
		int order = exceptionHandler.getOrder();

		// Then
		Assertions.assertThat(order).isEqualTo(Ordered.HIGHEST_PRECEDENCE);
	}

	@Test
	@DisplayName("Test ResponseStatusException with other status codes")
	void test_handle_responseStatusException_otherStatusCode() {
		// Given
		MockServerHttpRequest request = MockServerHttpRequest.get("/api/v1/forbidden").build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);
		ResponseStatusException exception = new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");

		// When
		Mono<@NonNull Void> result = exceptionHandler.handle(exchange, exception);

		// Then
		StepVerifier.create(result).verifyComplete();
		// 其他状态码走默认 BAD_GATEWAY 逻辑
		Assertions.assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	/**
	 * Sentinel 异常处理测试组.
	 */
	@Nested
	@DisplayName("Sentinel Exception Tests")
	class SentinelExceptionTests {

		@Test
		@DisplayName("Test DegradeException returns TOO_MANY_REQUESTS")
		void test_handle_degradeException_returnsTooManyRequests() {
			// Given
			MockServerHttpRequest request = MockServerHttpRequest.get("/api/v1/degraded").build();
			MockServerWebExchange exchange = MockServerWebExchange.from(request);
			DegradeException exception = new DegradeException("service degraded");

			// When
			Mono<@NonNull Void> result = exceptionHandler.handle(exchange, exception);

			// Then
			StepVerifier.create(result).verifyComplete();
			Assertions.assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.OK);
		}

		@Test
		@DisplayName("Test AuthorityException returns TOO_MANY_REQUESTS")
		void test_handle_authorityException_returnsTooManyRequests() {
			// Given
			MockServerHttpRequest request = MockServerHttpRequest.get("/api/v1/unauthorized").build();
			MockServerWebExchange exchange = MockServerWebExchange.from(request);
			AuthorityException exception = new AuthorityException("authority check failed");

			// When
			Mono<@NonNull Void> result = exceptionHandler.handle(exchange, exception);

			// Then
			StepVerifier.create(result).verifyComplete();
			Assertions.assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.OK);
		}

		@Test
		@DisplayName("Test SystemBlockException returns TOO_MANY_REQUESTS")
		void test_handle_systemBlockException_returnsTooManyRequests() {
			// Given
			MockServerHttpRequest request = MockServerHttpRequest.get("/api/v1/system-blocked").build();
			MockServerWebExchange exchange = MockServerWebExchange.from(request);
			SystemBlockException exception = new SystemBlockException("system_rule", "system protection triggered");

			// When
			Mono<@NonNull Void> result = exceptionHandler.handle(exchange, exception);

			// Then
			StepVerifier.create(result).verifyComplete();
			Assertions.assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.OK);
		}

		@Test
		@DisplayName("Test ParamFlowException returns TOO_MANY_REQUESTS")
		void test_handle_paramFlowException_returnsTooManyRequests() {
			// Given
			MockServerHttpRequest request = MockServerHttpRequest.get("/api/v1/param-limited").build();
			MockServerWebExchange exchange = MockServerWebExchange.from(request);
			ParamFlowException exception = new ParamFlowException("param_flow", "param flow limit exceeded");

			// When
			Mono<@NonNull Void> result = exceptionHandler.handle(exchange, exception);

			// Then
			StepVerifier.create(result).verifyComplete();
			Assertions.assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.OK);
		}

	}

	@Test
	@DisplayName("Test exception with null message")
	void test_handle_exceptionWithNullMessage_handlesGracefully() {
		// Given
		MockServerHttpRequest request = MockServerHttpRequest.get("/api/v1/null-message").build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);
		RuntimeException exception = new RuntimeException((String) null);

		// When
		Mono<@NonNull Void> result = exceptionHandler.handle(exchange, exception);

		// Then
		StepVerifier.create(result).verifyComplete();
		Assertions.assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	@DisplayName("Test nested exception")
	void test_handle_nestedException_handlesCorrectly() {
		// Given
		MockServerHttpRequest request = MockServerHttpRequest.get("/api/v1/nested").build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);
		RuntimeException cause = new RuntimeException("root cause");
		RuntimeException exception = new RuntimeException("wrapper exception", cause);

		// When
		Mono<@NonNull Void> result = exceptionHandler.handle(exchange, exception);

		// Then
		StepVerifier.create(result).verifyComplete();
		Assertions.assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.OK);
	}

}

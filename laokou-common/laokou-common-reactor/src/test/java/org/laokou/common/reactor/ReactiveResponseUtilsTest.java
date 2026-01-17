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

package org.laokou.common.reactor;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.reactor.util.ReactiveResponseUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.http.server.reactive.MockServerHttpResponse;
import org.springframework.mock.web.server.MockServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * ReactiveResponseUtils测试类.
 *
 * @author laokou
 */
class ReactiveResponseUtilsTest {

	@Test
	void test_responseOk_with_object() {
		MockServerHttpRequest request = MockServerHttpRequest.get("/api/test").build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);

		Map<String, Object> data = new HashMap<>();
		data.put("code", 200);
		data.put("message", "success");

		Mono<Void> result = ReactiveResponseUtils.responseOk(exchange, data);

		StepVerifier.create(result).verifyComplete();

		MockServerHttpResponse response = exchange.getResponse();
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
	}

	@Test
	void test_responseOk_with_string_and_contentType() {
		MockServerHttpRequest request = MockServerHttpRequest.get("/api/test").build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);

		String str = "Hello, World!";

		Mono<Void> result = ReactiveResponseUtils.responseOk(exchange, str, MediaType.TEXT_PLAIN);

		StepVerifier.create(result).verifyComplete();

		MockServerHttpResponse response = exchange.getResponse();
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.TEXT_PLAIN);
		Assertions.assertThat(response.getHeaders().getContentLength())
			.isEqualTo(str.getBytes(StandardCharsets.UTF_8).length);
	}

	@Test
	void test_responseOk_with_json() {
		MockServerHttpRequest request = MockServerHttpRequest.get("/api/test").build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);

		String jsonStr = "{\"status\":\"ok\"}";

		Mono<Void> result = ReactiveResponseUtils.responseOk(exchange, jsonStr, MediaType.APPLICATION_JSON);

		StepVerifier.create(result).verifyComplete();

		MockServerHttpResponse response = exchange.getResponse();
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
	}

	@Test
	void test_responseOk_with_empty_string() {
		MockServerHttpRequest request = MockServerHttpRequest.get("/api/test").build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);

		String str = "";

		Mono<Void> result = ReactiveResponseUtils.responseOk(exchange, str, MediaType.TEXT_PLAIN);

		StepVerifier.create(result).verifyComplete();

		MockServerHttpResponse response = exchange.getResponse();
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getHeaders().getContentLength()).isZero();
	}

	@Test
	void test_responseOk_with_unicode() {
		MockServerHttpRequest request = MockServerHttpRequest.get("/api/test").build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);

		String str = "你好，世界！";

		Mono<Void> result = ReactiveResponseUtils.responseOk(exchange, str, MediaType.TEXT_PLAIN);

		StepVerifier.create(result).verifyComplete();

		MockServerHttpResponse response = exchange.getResponse();
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getHeaders().getContentLength())
			.isEqualTo(str.getBytes(StandardCharsets.UTF_8).length);
	}

}

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
import org.laokou.common.reactor.util.ReactiveRequestUtils;
import org.springframework.http.MediaType;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * ReactiveRequestUtils测试类.
 *
 * @author laokou
 */
class ReactiveRequestUtilsTest {

	@Test
	void test_getParamValue_from_header() {
		MockServerHttpRequest request = MockServerHttpRequest.get("/api/test")
			.header("Authorization", "Bearer token123")
			.build();
		String paramValue = ReactiveRequestUtils.getParamValue(request, "Authorization");
		Assertions.assertThat(paramValue).isEqualTo("Bearer token123");
	}

	@Test
	void test_getParamValue_from_queryParams() {
		MockServerHttpRequest request = MockServerHttpRequest.get("/api/test").queryParam("page", "1").build();
		String paramValue = ReactiveRequestUtils.getParamValue(request, "page");
		Assertions.assertThat(paramValue).isEqualTo("1");
	}

	@Test
	void test_getParamValue_header_priority() {
		MockServerHttpRequest request = MockServerHttpRequest.get("/api/test")
			.header("token", "headerValue")
			.queryParam("token", "queryValue")
			.build();
		// Header should have priority
		String paramValue = ReactiveRequestUtils.getParamValue(request, "token");
		Assertions.assertThat(paramValue).isEqualTo("headerValue");
	}

	@Test
	void test_getParamValue_empty() {
		MockServerHttpRequest request = MockServerHttpRequest.get("/api/test").build();
		String paramValue = ReactiveRequestUtils.getParamValue(request, "nonExistent");
		Assertions.assertThat(paramValue).isEmpty();
	}

	@Test
	void test_getParamValue_trim() {
		MockServerHttpRequest request = MockServerHttpRequest.get("/api/test").header("name", "  value  ").build();
		String paramValue = ReactiveRequestUtils.getParamValue(request, "name");
		Assertions.assertThat(paramValue).isEqualTo("value");
	}

	@Test
	void test_getRequestURL() {
		MockServerHttpRequest request = MockServerHttpRequest.get("/api/v1/users/123").build();
		String requestURL = ReactiveRequestUtils.getRequestURL(request);
		Assertions.assertThat(requestURL).isEqualTo("/api/v1/users/123");
	}

	@Test
	void test_getHost() {
		MockServerHttpRequest request = MockServerHttpRequest.get("http://localhost:8080/api/test").build();
		String host = ReactiveRequestUtils.getHost(request);
		Assertions.assertThat(host).isEqualTo("localhost");
	}

	@Test
	void test_getContentType() {
		MockServerHttpRequest request = MockServerHttpRequest.post("/api/test")
			.contentType(MediaType.APPLICATION_JSON)
			.build();
		MediaType contentType = ReactiveRequestUtils.getContentType(request);
		Assertions.assertThat(contentType).isEqualTo(MediaType.APPLICATION_JSON);
	}

	@Test
	void test_getContentType_null() {
		MockServerHttpRequest request = MockServerHttpRequest.get("/api/test").build();
		MediaType contentType = ReactiveRequestUtils.getContentType(request);
		Assertions.assertThat(contentType).isNull();
	}

	@Test
	void test_getMethodName() {
		MockServerHttpRequest getRequest = MockServerHttpRequest.get("/api/test").build();
		Assertions.assertThat(ReactiveRequestUtils.getMethodName(getRequest)).isEqualTo("GET");

		MockServerHttpRequest postRequest = MockServerHttpRequest.post("/api/test").build();
		Assertions.assertThat(ReactiveRequestUtils.getMethodName(postRequest)).isEqualTo("POST");

		MockServerHttpRequest putRequest = MockServerHttpRequest.put("/api/test").build();
		Assertions.assertThat(ReactiveRequestUtils.getMethodName(putRequest)).isEqualTo("PUT");

		MockServerHttpRequest deleteRequest = MockServerHttpRequest.delete("/api/test").build();
		Assertions.assertThat(ReactiveRequestUtils.getMethodName(deleteRequest)).isEqualTo("DELETE");
	}

	@Test
	void test_pathMatcher_match() {
		Map<String, Set<String>> uriMap = new HashMap<>();
		Set<String> getUrls = new HashSet<>();
		getUrls.add("/api/users/**");
		getUrls.add("/api/orders/*");
		uriMap.put("GET", getUrls);

		Assertions.assertThat(ReactiveRequestUtils.pathMatcher("GET", "/api/users/123", uriMap)).isTrue();
		Assertions.assertThat(ReactiveRequestUtils.pathMatcher("GET", "/api/users/123/profile", uriMap)).isTrue();
		Assertions.assertThat(ReactiveRequestUtils.pathMatcher("GET", "/api/orders/456", uriMap)).isTrue();
	}

	@Test
	void test_pathMatcher_noMatch() {
		Map<String, Set<String>> uriMap = new HashMap<>();
		Set<String> getUrls = new HashSet<>();
		getUrls.add("/api/users/**");
		uriMap.put("GET", getUrls);

		Assertions.assertThat(ReactiveRequestUtils.pathMatcher("GET", "/api/products/123", uriMap)).isFalse();
		Assertions.assertThat(ReactiveRequestUtils.pathMatcher("POST", "/api/users/123", uriMap)).isFalse();
	}

	@Test
	void test_pathMatcher_emptyUrls() {
		Map<String, Set<String>> uriMap = new HashMap<>();
		uriMap.put("GET", new HashSet<>());

		Assertions.assertThat(ReactiveRequestUtils.pathMatcher("GET", "/api/users/123", uriMap)).isFalse();
	}

	@Test
	void test_pathMatcher_nullUrls() {
		Map<String, Set<String>> uriMap = new HashMap<>();

		Assertions.assertThat(ReactiveRequestUtils.pathMatcher("GET", "/api/users/123", uriMap)).isFalse();
	}

	@Test
	void test_pathMatcher_exactMatch() {
		Map<String, Set<String>> uriMap = new HashMap<>();
		Set<String> postUrls = new HashSet<>();
		postUrls.add("/api/login");
		uriMap.put("POST", postUrls);

		Assertions.assertThat(ReactiveRequestUtils.pathMatcher("POST", "/api/login", uriMap)).isTrue();
		Assertions.assertThat(ReactiveRequestUtils.pathMatcher("POST", "/api/logout", uriMap)).isFalse();
	}

}

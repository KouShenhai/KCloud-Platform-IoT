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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * RequestMatcherProperties 配置类测试.
 *
 * @author laokou
 */
@DisplayName("RequestMatcherProperties Tests")
class RequestMatcherPropertiesTest {

	private RequestMatcherProperties properties;

	@BeforeEach
	void setUp() {
		properties = new RequestMatcherProperties();
	}

	@Test
	@DisplayName("Test default ignorePatterns is empty Map")
	void testDefaultIgnorePatternsIsEmpty() {
		// Then
		Assertions.assertThat(properties.getIgnorePatterns()).isNotNull();
		Assertions.assertThat(properties.getIgnorePatterns()).isEmpty();
	}

	@Test
	@DisplayName("Test set ignorePatterns")
	void testSetIgnorePatterns() {
		// Given
		Map<String, Set<String>> ignorePatterns = new HashMap<>();
		ignorePatterns.put("GET", Set.of("/actuator/**=laokou-gateway", "/public/**=laokou-gateway"));
		ignorePatterns.put("POST", Set.of("/api/v1/oauth2/token=laokou-gateway"));

		// When
		properties.setIgnorePatterns(ignorePatterns);

		// Then
		Assertions.assertThat(properties.getIgnorePatterns()).hasSize(2);
		Assertions.assertThat(properties.getIgnorePatterns().get("GET")).hasSize(2);
		Assertions.assertThat(properties.getIgnorePatterns().get("POST")).hasSize(1);
	}

	@Test
	@DisplayName("Test ignorePatterns contains correct GET paths")
	void testIgnorePatternsContainsGetPaths() {
		// Given
		Map<String, Set<String>> ignorePatterns = new HashMap<>();
		ignorePatterns.put("GET", Set.of("/actuator/**=laokou-gateway"));
		properties.setIgnorePatterns(ignorePatterns);

		// Then
		Assertions.assertThat(properties.getIgnorePatterns().get("GET")).contains("/actuator/**=laokou-gateway");
	}

	@Test
	@DisplayName("Test ignorePatterns contains correct POST paths")
	void testIgnorePatternsContainsPostPaths() {
		// Given
		Map<String, Set<String>> ignorePatterns = new HashMap<>();
		ignorePatterns.put("POST", Set.of("/api/v1/oauth2/token=laokou-gateway"));
		properties.setIgnorePatterns(ignorePatterns);

		// Then
		Assertions.assertThat(properties.getIgnorePatterns().get("POST"))
			.contains("/api/v1/oauth2/token=laokou-gateway");
	}

	@Test
	@DisplayName("Test ignorePatterns supports DELETE method")
	void testIgnorePatternsSupportsDeleteMethod() {
		// Given
		Map<String, Set<String>> ignorePatterns = new HashMap<>();
		ignorePatterns.put("DELETE", Set.of("/api/v1/tokens=laokou-gateway"));
		properties.setIgnorePatterns(ignorePatterns);

		// Then
		Assertions.assertThat(properties.getIgnorePatterns().get("DELETE")).contains("/api/v1/tokens=laokou-gateway");
	}

	@Test
	@DisplayName("Test empty ignorePatterns does not throw exception")
	void testEmptyIgnorePatternsNoException() {
		// When
		properties.setIgnorePatterns(new HashMap<>());

		// Then
		Assertions.assertThat(properties.getIgnorePatterns()).isEmpty();
	}

}

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

package org.laokou.common.security.config;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * OAuth2ResourceServerProperties测试类.
 *
 * @author laokou
 */
class OAuth2ResourceServerPropertiesTest {

	@Test
	void test_default_enabled_is_true() {
		// Given
		OAuth2ResourceServerProperties properties = new OAuth2ResourceServerProperties();

		// Then
		Assertions.assertThat(properties.isEnabled()).isTrue();
	}

	@Test
	void test_default_requestMatcher_is_not_null() {
		// Given
		OAuth2ResourceServerProperties properties = new OAuth2ResourceServerProperties();

		// Then
		Assertions.assertThat(properties.getRequestMatcher()).isNotNull();
		Assertions.assertThat(properties.getRequestMatcher().getIgnorePatterns()).isNotNull().isEmpty();
	}

	@Test
	void test_setEnabled() {
		// Given
		OAuth2ResourceServerProperties properties = new OAuth2ResourceServerProperties();

		// When
		properties.setEnabled(false);

		// Then
		Assertions.assertThat(properties.isEnabled()).isFalse();
	}

	@Test
	void test_setIgnorePatterns() {
		// Given
		OAuth2ResourceServerProperties properties = new OAuth2ResourceServerProperties();
		Map<String, Set<String>> ignorePatterns = new HashMap<>();
		Set<String> services = new HashSet<>();
		services.add("laokou-admin");
		ignorePatterns.put("/actuator/**", services);

		// When
		properties.getRequestMatcher().setIgnorePatterns(ignorePatterns);

		// Then
		Assertions.assertThat(properties.getRequestMatcher().getIgnorePatterns()).hasSize(1);
		Assertions.assertThat(properties.getRequestMatcher().getIgnorePatterns().get("/actuator/**"))
			.contains("laokou-admin");
	}

	@Test
	void test_requestMatcher_default_ignorePatterns_is_empty_map() {
		// Given
		OAuth2ResourceServerProperties.RequestMatcher requestMatcher = new OAuth2ResourceServerProperties.RequestMatcher();

		// Then
		Assertions.assertThat(requestMatcher.getIgnorePatterns()).isNotNull().isEmpty();
	}

}

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

package org.laokou.common.data.cache.constant;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

/**
 * NameConstants test class.
 *
 * @author laokou
 */
@DisplayName("NameConstants Unit Tests")
class NameConstantsTest {

	@Test
	@DisplayName("Test all constants are not null or empty")
	void test_constants_allDefined_notNullOrEmpty() throws IllegalAccessException {
		// Given
		Field[] fields = NameConstants.class.getDeclaredFields();

		// When & Then
		for (Field field : fields) {
			if (Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers())
					&& field.getType() == String.class) {
				field.setAccessible(true);
				String value = (String) field.get(null);
				Assertions.assertThat(value).as("Constant %s should not be null", field.getName()).isNotNull();
				Assertions.assertThat(value).as("Constant %s should not be empty", field.getName()).isNotEmpty();
			}
		}
	}

	@Test
	@DisplayName("Test all constants have unique values")
	void test_constants_unique_noDuplicates() throws IllegalAccessException {
		// Given
		Field[] fields = NameConstants.class.getDeclaredFields();
		Set<String> values = new HashSet<>();

		// When & Then
		for (Field field : fields) {
			if (Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers())
					&& field.getType() == String.class) {
				field.setAccessible(true);
				String value = (String) field.get(null);
				Assertions.assertThat(values.add(value))
					.as("Constant value '%s' from field '%s' should be unique", value, field.getName())
					.isTrue();
			}
		}
	}

	@Test
	@DisplayName("Test specific constant values are correct")
	void test_constants_specificValues_areCorrect() {
		// Then
		Assertions.assertThat(NameConstants.OSS).isEqualTo("oss");
		Assertions.assertThat(NameConstants.DEPTS).isEqualTo("depts");
		Assertions.assertThat(NameConstants.DICTS).isEqualTo("dicts");
		Assertions.assertThat(NameConstants.MENUS).isEqualTo("menus");
		Assertions.assertThat(NameConstants.MESSAGES).isEqualTo("messages");
		Assertions.assertThat(NameConstants.PACKAGES).isEqualTo("packages");
		Assertions.assertThat(NameConstants.TENANTS).isEqualTo("tenants");
		Assertions.assertThat(NameConstants.SOURCES).isEqualTo("sources");
		Assertions.assertThat(NameConstants.USERS).isEqualTo("users");
		Assertions.assertThat(NameConstants.USER_MENU).isEqualTo("user_menu");
		Assertions.assertThat(NameConstants.OSS_LOG).isEqualTo("oss_log");
		Assertions.assertThat(NameConstants.OSS_RESOURCE).isEqualTo("oss_resource");
	}

}

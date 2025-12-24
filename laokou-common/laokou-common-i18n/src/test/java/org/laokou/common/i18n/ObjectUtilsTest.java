/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.i18n;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.i18n.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author laokou
 */
class ObjectUtilsTest {

	@Test
	void test_isNull_withNull() {
		// Test with null object
		Object obj = null;
		Assertions.assertThat(ObjectUtils.isNull(obj)).isTrue();
	}

	@Test
	void test_isNull_withNonNull() {
		// Test with non-null object
		Object obj = new Object();
		Assertions.assertThat(ObjectUtils.isNull(obj)).isFalse();
	}

	@Test
	void test_isNull_withString() {
		// Test with non-null string
		String str = "test";
		Assertions.assertThat(ObjectUtils.isNull(str)).isFalse();
	}

	@Test
	void test_isNull_withEmptyString() {
		// Test with empty string (not null)
		String str = "";
		Assertions.assertThat(ObjectUtils.isNull(str)).isFalse();
	}

	@Test
	void test_isNull_withInteger() {
		// Test with integer
		Integer num = 123;
		Assertions.assertThat(ObjectUtils.isNull(num)).isFalse();
	}

	@Test
	void test_isNull_withZero() {
		// Test with zero (not null)
		Integer num = 0;
		Assertions.assertThat(ObjectUtils.isNull(num)).isFalse();
	}

	@Test
	void test_isNotNull_withNull() {
		// Test with null object
		Object obj = null;
		Assertions.assertThat(ObjectUtils.isNotNull(obj)).isFalse();
	}

	@Test
	void test_isNotNull_withNonNull() {
		// Test with non-null object
		Object obj = new Object();
		Assertions.assertThat(ObjectUtils.isNotNull(obj)).isTrue();
	}

	@Test
	void test_isNotNull_withString() {
		// Test with non-null string
		String str = "test";
		Assertions.assertThat(ObjectUtils.isNotNull(str)).isTrue();
	}

	@Test
	void test_isNotNull_withEmptyString() {
		// Test with empty string (not null)
		String str = "";
		Assertions.assertThat(ObjectUtils.isNotNull(str)).isTrue();
	}

	@Test
	void test_isNotNull_withList() {
		// Test with list
		Assertions.assertThat(ObjectUtils.isNotNull(new ArrayList<>(0))).isTrue();
	}

	@Test
	void test_equals_withBothNull() {
		// Test with both objects null
		Object obj1 = null;
		Object obj2 = null;
		Assertions.assertThat(ObjectUtils.equals(obj1, obj2)).isTrue();
	}

	@Test
	void test_equals_withFirstNull() {
		// Test with first object null
		Object obj1 = null;
		Object obj2 = "test";
		Assertions.assertThat(ObjectUtils.equals(obj1, obj2)).isFalse();
	}

	@Test
	void test_equals_withSecondNull() {
		// Test with second object null
		Object obj1 = "test";
		Object obj2 = null;
		Assertions.assertThat(ObjectUtils.equals(obj1, obj2)).isFalse();
	}

	@Test
	void test_equals_withSameObject() {
		// Test with same object reference
		Object obj = new Object();
		Assertions.assertThat(ObjectUtils.equals(obj, obj)).isTrue();
	}

	@Test
	void test_equals_withEqualStrings() {
		// Test with equal strings
		String str1 = "test";
		String str2 = "test";
		Assertions.assertThat(ObjectUtils.equals(str1, str2)).isTrue();
	}

	@Test
	void test_equals_withDifferentStrings() {
		// Test with different strings
		String str1 = "test1";
		String str2 = "test2";
		Assertions.assertThat(ObjectUtils.equals(str1, str2)).isFalse();
	}

	@Test
	void test_equals_withEqualIntegers() {
		// Test with equal integers
		Integer num1 = 123;
		Integer num2 = 123;
		Assertions.assertThat(ObjectUtils.equals(num1, num2)).isTrue();
	}

	@Test
	void test_equals_withDifferentIntegers() {
		// Test with different integers
		Integer num1 = 123;
		Integer num2 = 456;
		Assertions.assertThat(ObjectUtils.equals(num1, num2)).isFalse();
	}

	@Test
	void test_equals_withDifferentTypes() {
		// Test with different types
		String str = "123";
		Integer num = 123;
		Assertions.assertThat(ObjectUtils.equals(str, num)).isFalse();
	}

	@Test
	void test_requireNotNull_withNonNull() {
		// Test with non-null object
		String str = "test";
		String result = ObjectUtils.requireNotNull(str);
		Assertions.assertThat(result).isNotNull().isEqualTo("test");
	}

	@Test
	void test_requireNotNull_withNull() {
		// Test with null object (should throw NullPointerException)
		String str = null;
		Assertions.assertThatThrownBy(() -> ObjectUtils.requireNotNull(str)).isInstanceOf(NullPointerException.class);
	}

	@Test
	void test_requireNotNull_withInteger() {
		// Test with non-null integer
		Integer num = 123;
		Integer result = ObjectUtils.requireNotNull(num);
		Assertions.assertThat(result).isNotNull().isEqualTo(123);
	}

	@Test
	void test_requireNotNull_withList() {
		// Test with non-null list
		List<String> list = new ArrayList<>();
		list.add("item");
		List<String> result = ObjectUtils.requireNotNull(list);
		Assertions.assertThat(result).isNotNull().hasSize(1);
		Assertions.assertThat(result.getFirst()).isEqualTo("item");
	}

	@Test
	void test_requireNotNull_withEmptyString() {
		// Test with empty string (not null)
		String str = "";
		String result = ObjectUtils.requireNotNull(str);
		Assertions.assertThat(result).isNotNull().isEmpty();
	}

	@Test
	void test_requireNotNull_withZero() {
		// Test with zero (not null)
		Integer num = 0;
		Integer result = ObjectUtils.requireNotNull(num);
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result).isZero();
	}

	@Test
	void test_requireNotNull_withCustomObject() {
		// Test with custom object
		TestObject obj = new TestObject("test", 123);
		TestObject result = ObjectUtils.requireNotNull(obj);
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result.name()).isEqualTo("test");
		Assertions.assertThat(result.value()).isEqualTo(123);
	}

	@Test
	void test_isNull_and_isNotNull_opposite() {
		// Test that isNull and isNotNull are opposites
		Object obj = "test";
		Assertions.assertThat(ObjectUtils.isNull(obj)).isNotEqualTo(ObjectUtils.isNotNull(obj));

		Object nullObj = null;
		Assertions.assertThat(ObjectUtils.isNull(nullObj)).isNotEqualTo(ObjectUtils.isNotNull(nullObj));
	}

	@Test
	void test_equals_symmetry() {
		// Test that equals is symmetric
		String str1 = "test";
		String str2 = "test";
		Assertions.assertThat(ObjectUtils.equals(str1, str2)).isEqualTo(ObjectUtils.equals(str2, str1));
	}

	// Helper class for testing
	private record TestObject(String name, int value) {

	}

}

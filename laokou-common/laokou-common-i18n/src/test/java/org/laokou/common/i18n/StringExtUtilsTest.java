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
import org.laokou.common.i18n.util.StringExtUtils;

/**
 * @author laokou
 */
class StringExtUtilsTest {

	@Test
	void test_isEmpty_withNull() {
		// Test with null string
		String str = null;
		Assertions.assertThat(StringExtUtils.isEmpty(str)).isTrue();
	}

	@Test
	void test_isEmpty_withEmptyString() {
		// Test with empty string
		String str = "";
		Assertions.assertThat(StringExtUtils.isEmpty(str)).isTrue();
	}

	@Test
	void test_isEmpty_withWhitespace() {
		// Test with whitespace only
		String str = "   ";
		Assertions.assertThat(StringExtUtils.isEmpty(str)).isTrue();
	}

	@Test
	void test_isEmpty_withNonEmptyString() {
		// Test with non-empty string
		String str = "test";
		Assertions.assertThat(StringExtUtils.isEmpty(str)).isFalse();
	}

	@Test
	void test_isEmpty_withCharSequence() {
		// Test isEmpty with CharSequence
		CharSequence seq = "test";
		Assertions.assertThat(StringExtUtils.isEmpty(seq)).isFalse();
		CharSequence emptySeq = "";
		Assertions.assertThat(StringExtUtils.isEmpty(emptySeq)).isTrue();
	}

	@Test
	void test_isNotEmpty_withNull() {
		// Test with null string
		String str = null;
		Assertions.assertThat(StringExtUtils.isNotEmpty(str)).isFalse();
	}

	@Test
	void test_isNotEmpty_withEmptyString() {
		// Test with empty string
		String str = "";
		Assertions.assertThat(StringExtUtils.isNotEmpty(str)).isFalse();
	}

	@Test
	void test_isNotEmpty_withWhitespace() {
		// Test with whitespace only
		String str = "   ";
		Assertions.assertThat(StringExtUtils.isNotEmpty(str)).isFalse();
	}

	@Test
	void test_isNotEmpty_withNonEmptyString() {
		// Test with non-empty string
		String str = "test";
		Assertions.assertThat(StringExtUtils.isNotEmpty(str)).isTrue();
	}

	@Test
	void test_isNotEmpty_withCharSequence() {
		// Test isNotEmpty with CharSequence
		CharSequence seq = "test";
		Assertions.assertThat(StringExtUtils.isNotEmpty(seq)).isTrue();
		CharSequence emptySeq = "";
		Assertions.assertThat(StringExtUtils.isNotEmpty(emptySeq)).isFalse();
	}

	@Test
	void test_startWith_withMatchingPrefix() {
		// Test with matching prefix
		String str = "hello world";
		Assertions.assertThat(StringExtUtils.startWith(str, "hello")).isTrue();
	}

	@Test
	void test_startWith_withNonMatchingPrefix() {
		// Test with non-matching prefix
		String str = "hello world";
		Assertions.assertThat(StringExtUtils.startWith(str, "world")).isFalse();
	}

	@Test
	void test_startWith_withEmptyPrefix() {
		// Test with empty prefix
		String str = "hello";
		Assertions.assertThat(StringExtUtils.startWith(str, "")).isTrue();
	}

	@Test
	void test_parseLong_withValidNumber() {
		// Test with valid number string
		String str = "12345";
		Long result = StringExtUtils.parseLong(str);
		Assertions.assertThat(result).isEqualTo(12345L);
	}

	@Test
	void test_parseLong_withNull() {
		// Test with null string
		String str = null;
		Long result = StringExtUtils.parseLong(str);
		Assertions.assertThat(result).isNull();
	}

	@Test
	void test_parseLong_withEmptyString() {
		// Test with empty string
		String str = "";
		Long result = StringExtUtils.parseLong(str);
		Assertions.assertThat(result).isNull();
	}

	@Test
	void test_parseLong_withNegativeNumber() {
		// Test with negative number
		String str = "-12345";
		Long result = StringExtUtils.parseLong(str);
		Assertions.assertThat(result).isEqualTo(-12345L);
	}

	@Test
	void test_parseLong_withInvalidNumber() {
		// Test with invalid number string (should throw exception)
		String str = "abc";
		Assertions.assertThatThrownBy(() -> StringExtUtils.parseLong(str)).isInstanceOf(NumberFormatException.class);
	}

	@Test
	void test_empty_withNull() {
		// Test with null string
		String str = null;
		String result = StringExtUtils.empty(str);
		Assertions.assertThat(result).isEmpty();
	}

	@Test
	void test_empty_withEmptyString() {
		// Test with empty string
		String str = "";
		String result = StringExtUtils.empty(str);
		Assertions.assertThat(result).isEmpty();
	}

	@Test
	void test_empty_withNonEmptyString() {
		// Test with non-empty string
		String str = "test";
		String result = StringExtUtils.empty(str);
		Assertions.assertThat(result).isEqualTo("test");
	}

	@Test
	void test_like_withNonEmptyString() {
		// Test with non-empty string
		String str = "test";
		String result = StringExtUtils.like(str);
		Assertions.assertThat(result).isEqualTo("%test%");
	}

	@Test
	void test_like_withEmptyString() {
		// Test with empty string
		String str = "";
		String result = StringExtUtils.like(str);
		Assertions.assertThat(result).isEmpty();
	}

	@Test
	void test_like_withNull() {
		// Test with null string
		String str = null;
		String result = StringExtUtils.like(str);
		Assertions.assertThat(result).isNull();
	}

	@Test
	void test_trim_withWhitespace() {
		// Test with whitespace
		String str = "  test  ";
		String result = StringExtUtils.trim(str);
		Assertions.assertThat(result).isEqualTo("test");
	}

	@Test
	void test_trim_withNoWhitespace() {
		// Test with no whitespace
		String str = "test";
		String result = StringExtUtils.trim(str);
		Assertions.assertThat(result).isEqualTo("test");
	}

	@Test
	void test_trim_withNull() {
		// Test with null string
		String str = null;
		String result = StringExtUtils.trim(str);
		Assertions.assertThat(result).isNull();
	}

	@Test
	void test_trim_withEmptyString() {
		// Test with empty string
		String str = "";
		String result = StringExtUtils.trim(str);
		Assertions.assertThat(result).isEmpty();
	}

	@Test
	void test_removeStart_withMatchingPrefix() {
		// Test with matching prefix
		String str = "hello world";
		String result = StringExtUtils.removeStart(str, "hello ");
		Assertions.assertThat(result).isEqualTo("world");
	}

	@Test
	void test_removeStart_withNonMatchingPrefix() {
		// Test with non-matching prefix
		String str = "hello world";
		String result = StringExtUtils.removeStart(str, "world");
		Assertions.assertThat(result).isEqualTo("hello world");
	}

	@Test
	void test_removeStart_withEmptyPrefix() {
		// Test with empty prefix
		String str = "hello";
		String result = StringExtUtils.removeStart(str, "");
		Assertions.assertThat(result).isEqualTo("hello");
	}

	@Test
	void test_removeStart_withNull() {
		// Test with null string
		String str = null;
		String result = StringExtUtils.removeStart(str, "test");
		Assertions.assertThat(result).isNull();
	}

	@Test
	void test_allNotNull_withAllNonNull() {
		// Test with all non-null values
		boolean result = StringExtUtils.allNotNull("a", "b", "c");
		Assertions.assertThat(result).isTrue();
	}

	@Test
	void test_allNotNull_withOneNull() {
		// Test with one null value
		boolean result = StringExtUtils.allNotNull("a", null, "c");
		Assertions.assertThat(result).isFalse();
	}

	@Test
	void test_allNotNull_withAllNull() {
		// Test with all null values
		boolean result = StringExtUtils.allNotNull(null, null, null);
		Assertions.assertThat(result).isFalse();
	}

	@Test
	void test_allNotNull_withEmptyArray() {
		// Test with empty array
		boolean result = StringExtUtils.allNotNull();
		Assertions.assertThat(result).isTrue();
	}

	@Test
	void test_allNotNull_withNullArray() {
		// Test with null array
		Object[] array = null;
		boolean result = StringExtUtils.allNotNull(array);
		Assertions.assertThat(result).isFalse();
	}

	@Test
	void test_substringBetween_withValidDelimiters() {
		// Test with valid delimiters
		String str = "hello [world] test";
		String result = StringExtUtils.substringBetween(str, "[", "]");
		Assertions.assertThat(result).isEqualTo("world");
	}

	@Test
	void test_substringBetween_withNoClosingDelimiter() {
		// Test with no closing delimiter
		String str = "hello [world";
		String result = StringExtUtils.substringBetween(str, "[", "]");
		Assertions.assertThat(result).isNull();
	}

	@Test
	void test_substringBetween_withNoOpeningDelimiter() {
		// Test with no opening delimiter
		String str = "hello world]";
		String result = StringExtUtils.substringBetween(str, "[", "]");
		Assertions.assertThat(result).isNull();
	}

	@Test
	void test_substringBetween_withNull() {
		// Test with null string
		String str = null;
		String result = StringExtUtils.substringBetween(str, "[", "]");
		Assertions.assertThat(result).isNull();
	}

	@Test
	void test_substringBetween_withMultipleOccurrences() {
		// Test with multiple occurrences (should return first)
		String str = "hello [world] and [test]";
		String result = StringExtUtils.substringBetween(str, "[", "]");
		Assertions.assertThat(result).isEqualTo("world");
	}

	@Test
	void test_convertUnder_withUnderscores() {
		// Test converting underscore to camelCase
		String str = "hello_world_test";
		String result = StringExtUtils.convertUnder(str);
		Assertions.assertThat(result).isEqualTo("helloWorldTest");
	}

	@Test
	void test_convertUnder_withNoUnderscores() {
		// Test with no underscores
		String str = "helloworld";
		String result = StringExtUtils.convertUnder(str);
		Assertions.assertThat(result).isEqualTo("helloworld");
	}

	@Test
	void test_convertUnder_withSingleUnderscore() {
		// Test with single underscore
		String str = "hello_world";
		String result = StringExtUtils.convertUnder(str);
		Assertions.assertThat(result).isEqualTo("helloWorld");
	}

	@Test
	void test_convertUnder_withUpperCase() {
		// Test with uppercase letters
		String str = "HELLO_WORLD";
		String result = StringExtUtils.convertUnder(str);
		Assertions.assertThat(result).isEqualTo("helloWorld");
	}

	@Test
	void test_convertUnder_withMultipleUnderscores() {
		// Test with multiple consecutive underscores
		String str = "hello__world";
		String result = StringExtUtils.convertUnder(str);
		Assertions.assertThat(result).isNotNull();
	}

	@Test
	void test_truncate_withLongerString() {
		// Test with string longer than max length
		String str = "hello world test";
		String result = StringExtUtils.truncate(str, 10);
		Assertions.assertThat(result).isEqualTo("hello worl");
		Assertions.assertThat(result).hasSize(10);
	}

	@Test
	void test_truncate_withShorterString() {
		// Test with string shorter than max length
		String str = "hello";
		String result = StringExtUtils.truncate(str, 10);
		Assertions.assertThat(result).isEqualTo("hello");
	}

	@Test
	void test_truncate_withExactLength() {
		// Test with string exactly at max length
		String str = "hello";
		String result = StringExtUtils.truncate(str, 5);
		Assertions.assertThat(result).isEqualTo("hello");
	}

	@Test
	void test_truncate_withNull() {
		// Test with null string
		String str = null;
		String result = StringExtUtils.truncate(str, 10);
		Assertions.assertThat(result).isNull();
	}

	@Test
	void test_truncate_withEmptyString() {
		// Test with empty string
		String str = "";
		String result = StringExtUtils.truncate(str, 10);
		Assertions.assertThat(result).isNull();
	}

	@Test
	void test_truncate_withZeroLength() {
		// Test with zero max length
		String str = "hello";
		String result = StringExtUtils.truncate(str, 0);
		Assertions.assertThat(result).isEmpty();
	}

	@Test
	void test_isEmpty_and_isNotEmpty_opposite() {
		// Test that isEmpty and isNotEmpty are opposites
		String str = "test";
		Assertions.assertThat(StringExtUtils.isEmpty(str)).isNotEqualTo(StringExtUtils.isNotEmpty(str));

		String emptyStr = "";
		Assertions.assertThat(StringExtUtils.isEmpty(emptyStr)).isNotEqualTo(StringExtUtils.isNotEmpty(emptyStr));
	}

	@Test
	void test_like_withSpecialCharacters() {
		// Test like with special characters
		String str = "test@#$";
		String result = StringExtUtils.like(str);
		Assertions.assertThat(result).isEqualTo("%test@#$%");
	}

	@Test
	void test_substringBetween_withSameDelimiters() {
		// Test with same opening and closing delimiters
		String str = "hello 'world' test";
		String result = StringExtUtils.substringBetween(str, "'", "'");
		Assertions.assertThat(result).isEqualTo("world");
	}

	@Test
	void test_convertUnder_withLeadingUnderscore() {
		// Test with leading underscore
		String str = "_hello_world";
		String result = StringExtUtils.convertUnder(str);
		Assertions.assertThat(result).isNotNull();
	}

	@Test
	void test_convertUnder_withTrailingUnderscore() {
		// Test with trailing underscore
		String str = "hello_world_";
		String result = StringExtUtils.convertUnder(str);
		Assertions.assertThat(result).isNotNull();
	}

	@Test
	void test_parseLong_withMaxValue() {
		// Test with Long.MAX_VALUE
		String str = String.valueOf(Long.MAX_VALUE);
		Long result = StringExtUtils.parseLong(str);
		Assertions.assertThat(result).isEqualTo(Long.MAX_VALUE);
	}

	@Test
	void test_parseLong_withMinValue() {
		// Test with Long.MIN_VALUE
		String str = String.valueOf(Long.MIN_VALUE);
		Long result = StringExtUtils.parseLong(str);
		Assertions.assertThat(result).isEqualTo(Long.MIN_VALUE);
	}

}

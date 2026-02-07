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

package org.laokou.common.i18n;

import jakarta.servlet.http.HttpServletRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.laokou.common.i18n.util.I18nUtils;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Locale;

/**
 * I18nUtils unit test.
 *
 * @author laokou
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("I18nUtils test")
class I18nUtilsTest {

	@Mock
	private HttpServletRequest request;

	@Nested
	@DisplayName("getLocale() without parameters")
	class GetLocaleWithoutParamTests {

		@Test
		@DisplayName("Should return default locale when LOCALE ScopedValue is not bound")
		void test_getLocale_returnsDefaultLocale_whenScopedValueNotBound() {
			// Given
			Locale expectedLocale = Locale.getDefault();

			// When
			Locale result = I18nUtils.getLocale();

			// Then
			Assertions.assertThat(result).isEqualTo(expectedLocale);
		}

		@Test
		@DisplayName("Should return bound locale when LOCALE ScopedValue is bound")
		void test_getLocale_returnsBoundLocale_whenScopedValueBound() {
			// Given
			Locale expectedLocale = Locale.CHINA;

			// When & Then
			ScopedValue.where(I18nUtils.LOCALE, expectedLocale).run(() -> {
				Locale result = I18nUtils.getLocale();
				Assertions.assertThat(result).isEqualTo(expectedLocale);
			});
		}

	}

	@Nested
	@DisplayName("getLocale(HttpServletRequest) method")
	class GetLocaleWithRequestTests {

		@Test
		@DisplayName("Should return locale from Language header when it exists and is valid")
		void test_getLocale_returnsLocaleFromLanguageHeader_whenLanguageHeaderExists() {
			// Given
			Mockito.when(request.getHeader("Language")).thenReturn("zh-CN");

			// When
			Locale result = I18nUtils.getLocale(request);

			// Then
			Assertions.assertThat(result).isEqualTo(Locale.of("zh", "CN"));
		}

		@Test
		@DisplayName("Should return locale from Accept-Language header when Language header does not exist")
		void test_getLocale_returnsLocaleFromAcceptLanguageHeader_whenLanguageHeaderNotExists() {
			// Given
			Mockito.when(request.getHeader("Language")).thenReturn(null);
			Mockito.when(request.getHeader("Accept-Language")).thenReturn("en-US");

			// When
			Locale result = I18nUtils.getLocale(request);

			// Then
			Assertions.assertThat(result).isEqualTo(Locale.of("en", "US"));
		}

		@Test
		@DisplayName("Should return locale from Accept-Language header when Language header is empty string")
		void test_getLocale_returnsLocaleFromAcceptLanguageHeader_whenLanguageHeaderIsEmpty() {
			// Given
			Mockito.when(request.getHeader("Language")).thenReturn("");
			Mockito.when(request.getHeader("Accept-Language")).thenReturn("ja-JP");

			// When
			Locale result = I18nUtils.getLocale(request);

			// Then
			Assertions.assertThat(result).isEqualTo(Locale.of("ja", "JP"));
		}

		@Test
		@DisplayName("Should return locale from Accept-Language header when Language header contains only whitespace")
		void test_getLocale_returnsLocaleFromAcceptLanguageHeader_whenLanguageHeaderIsBlank() {
			// Given
			Mockito.when(request.getHeader("Language")).thenReturn("   ");
			Mockito.when(request.getHeader("Accept-Language")).thenReturn("ko-KR");

			// When
			Locale result = I18nUtils.getLocale(request);

			// Then
			Assertions.assertThat(result).isEqualTo(Locale.of("ko", "KR"));
		}

		@Test
		@DisplayName("Should return default locale when both headers do not exist")
		void test_getLocale_returnsDefaultLocale_whenBothHeadersNotExist() {
			// Given
			Mockito.when(request.getHeader("Language")).thenReturn(null);
			Mockito.when(request.getHeader("Accept-Language")).thenReturn(null);

			// When
			Locale result = I18nUtils.getLocale(request);

			// Then
			Assertions.assertThat(result).isEqualTo(Locale.getDefault());
		}

		@Test
		@DisplayName("Should return default locale when both headers are empty strings")
		void test_getLocale_returnsDefaultLocale_whenBothHeadersAreEmpty() {
			// Given
			Mockito.when(request.getHeader("Language")).thenReturn("");
			Mockito.when(request.getHeader("Accept-Language")).thenReturn("");

			// When
			Locale result = I18nUtils.getLocale(request);

			// Then
			Assertions.assertThat(result).isEqualTo(Locale.getDefault());
		}

		@Test
		@DisplayName("Should trim and parse Language header correctly when it has leading/trailing spaces")
		void test_getLocale_trimsLanguageHeader_whenLanguageHeaderHasSpaces() {
			// Given
			Mockito.when(request.getHeader("Language")).thenReturn("  zh-CN  ");

			// When
			Locale result = I18nUtils.getLocale(request);

			// Then
			Assertions.assertThat(result).isEqualTo(Locale.of("zh", "CN"));
		}

		@Test
		@DisplayName("Should parse locale correctly when language uses underscore format")
		void test_getLocale_parsesUnderscoreFormat_whenLanguageUsesUnderscore() {
			// Given
			Mockito.when(request.getHeader("Language")).thenReturn("zh_CN");

			// When
			Locale result = I18nUtils.getLocale(request);

			// Then
			Assertions.assertThat(result).isEqualTo(Locale.of("zh", "CN"));
		}

		@Test
		@DisplayName("Should prefer Language header over Accept-Language when both headers exist")
		void test_getLocale_prefersLanguageHeader_whenBothHeadersExist() {
			// Given
			Mockito.when(request.getHeader("Language")).thenReturn("zh-CN");

			// When
			Locale result = I18nUtils.getLocale(request);

			// Then
			Assertions.assertThat(result).isEqualTo(Locale.of("zh", "CN"));
			// Verify Accept-Language header was never accessed
			Mockito.verify(request, Mockito.never()).getHeader("Accept-Language");
		}

	}

}

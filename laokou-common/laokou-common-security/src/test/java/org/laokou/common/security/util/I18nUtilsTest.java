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

package org.laokou.common.security.util;

import jakarta.servlet.http.HttpServletRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.laokou.common.i18n.util.ObjectUtils;
import org.mockito.Mockito;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;

import java.util.Locale;

/**
 * @author laokou
 */
class I18nUtilsTest {

	@AfterEach
	void tearDown() {
		I18nUtils.reset();
	}

	@Test
	void test_set_withLanguageHeader_setsLocale() {
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getHeader("Language")).thenReturn("zh_CN");
		I18nUtils.set(request);
		Assertions.assertThat(LocaleContextHolder.getLocale().getLanguage()).isEqualTo("zh");
	}

	@Test
	void test_set_withAcceptLanguageHeader_fallbackToAcceptLanguage() {
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getHeader("Language")).thenReturn(null);
		Mockito.when(request.getHeader(HttpHeaders.ACCEPT_LANGUAGE)).thenReturn("en_US");
		I18nUtils.set(request);
		Assertions.assertThat(LocaleContextHolder.getLocale().getLanguage()).isEqualTo("en");
	}

	@Test
	void test_set_withEmptyLanguageHeader_fallbackToAcceptLanguage() {
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getHeader("Language")).thenReturn("");
		Mockito.when(request.getHeader(HttpHeaders.ACCEPT_LANGUAGE)).thenReturn("ja_JP");
		I18nUtils.set(request);
		Assertions.assertThat(LocaleContextHolder.getLocale().getLanguage()).isEqualTo("ja");
	}

	@Test
	void test_set_withBothHeadersNull_noExceptionThrown() {
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getHeader("Language")).thenReturn(null);
		Mockito.when(request.getHeader(HttpHeaders.ACCEPT_LANGUAGE)).thenReturn(null);
		Assertions.assertThatNoException().isThrownBy(() -> I18nUtils.set(request));
	}

	@Test
	void test_reset_afterSet_clearsLocaleContext() {
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getHeader("Language")).thenReturn("fr_FR");
		I18nUtils.set(request);
		Locale localeBeforeReset = LocaleContextHolder.getLocale();
		Assertions.assertThat(localeBeforeReset.getLanguage()).isEqualTo("fr");
		I18nUtils.reset();
		Locale locale = LocaleContextHolder.getLocale();
		if (ObjectUtils.equals("zh", locale.getLanguage())) {
			Assertions.assertThat(LocaleContextHolder.getLocale().getLanguage()).isEqualTo("zh");
		}
		else {
			Assertions.assertThat(LocaleContextHolder.getLocale().getLanguage()).isEqualTo("en");
		}
		// After reset, locale should be reset to default
	}

	@Test
	void test_reset_withoutPriorSet_noExceptionThrown() {
		Assertions.assertThatNoException().isThrownBy(I18nUtils::reset);
	}

}

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

package org.laokou.common.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.laokou.common.i18n.util.I18nUtils;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

/**
 * I18nFilter unit test.
 *
 * @author laokou
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("I18nFilter test")
class I18nFilterTest {

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Mock
	private FilterChain filterChain;

	private I18nFilter i18nFilter;

	@BeforeEach
	void setUp() {
		i18nFilter = new I18nFilter();
	}

	@Test
	@DisplayName("Should set locale from Language header and pass it to filter chain")
	void test_doFilterInternal_setsLocaleFromLanguageHeader() throws Exception {
		// Given
		Mockito.when(request.getHeader("Language")).thenReturn("zh-CN");
		AtomicReference<Locale> capturedLocale = new AtomicReference<>();

		Mockito.doAnswer(_ -> {
			capturedLocale.set(I18nUtils.getLocale());
			return null;
		}).when(filterChain).doFilter(request, response);

		// When
		i18nFilter.doFilterInternal(request, response, filterChain);

		// Then
		Assertions.assertThat(capturedLocale.get()).isEqualTo(Locale.of("zh", "CN"));
		Mockito.verify(filterChain).doFilter(request, response);
	}

	@Test
	@DisplayName("Should set locale from Accept-Language header when Language header is not present")
	void test_doFilterInternal_setsLocaleFromAcceptLanguageHeader() throws Exception {
		// Given
		Mockito.when(request.getHeader("Language")).thenReturn(null);
		Mockito.when(request.getHeader("Accept-Language")).thenReturn("en-US");
		AtomicReference<Locale> capturedLocale = new AtomicReference<>();

		Mockito.doAnswer(_ -> {
			capturedLocale.set(I18nUtils.getLocale());
			return null;
		}).when(filterChain).doFilter(request, response);

		// When
		i18nFilter.doFilterInternal(request, response, filterChain);

		// Then
		Assertions.assertThat(capturedLocale.get()).isEqualTo(Locale.of("en", "US"));
		Mockito.verify(filterChain).doFilter(request, response);
	}

	@Test
	@DisplayName("Should use default locale when no language headers are present")
	void test_doFilterInternal_usesDefaultLocale_whenNoLanguageHeaders() throws Exception {
		// Given
		Mockito.when(request.getHeader("Language")).thenReturn(null);
		Mockito.when(request.getHeader("Accept-Language")).thenReturn(null);
		AtomicReference<Locale> capturedLocale = new AtomicReference<>();

		Mockito.doAnswer(_ -> {
			capturedLocale.set(I18nUtils.getLocale());
			return null;
		}).when(filterChain).doFilter(request, response);

		// When
		i18nFilter.doFilterInternal(request, response, filterChain);

		// Then
		Assertions.assertThat(capturedLocale.get()).isEqualTo(Locale.getDefault());
		Mockito.verify(filterChain).doFilter(request, response);
	}

	@Test
	@DisplayName("Should wrap IOException in RuntimeException when filter chain throws IOException")
	void test_doFilterInternal_wrapsIOException() throws Exception {
		// Given
		Mockito.when(request.getHeader("Language")).thenReturn("zh-CN");
		IOException ioException = new IOException("Test IO error");
		Mockito.doThrow(ioException).when(filterChain).doFilter(request, response);

		// When & Then
		Assertions.assertThatThrownBy(() -> i18nFilter.doFilterInternal(request, response, filterChain))
			.isInstanceOf(RuntimeException.class)
			.hasCause(ioException);
	}

	@Test
	@DisplayName("Should wrap ServletException in RuntimeException when filter chain throws ServletException")
	void test_doFilterInternal_wrapsServletException() throws Exception {
		// Given
		Mockito.when(request.getHeader("Language")).thenReturn("zh-CN");
		ServletException servletException = new ServletException("Test servlet error");
		Mockito.doThrow(servletException).when(filterChain).doFilter(request, response);

		// When & Then
		Assertions.assertThatThrownBy(() -> i18nFilter.doFilterInternal(request, response, filterChain))
			.isInstanceOf(RuntimeException.class)
			.hasCause(servletException);
	}

	@Test
	@DisplayName("Should invoke filter chain exactly once")
	void test_doFilterInternal_invokesFilterChainOnce() throws Exception {
		// Given
		Mockito.when(request.getHeader("Language")).thenReturn("ja-JP");

		// When
		i18nFilter.doFilterInternal(request, response, filterChain);

		// Then
		Mockito.verify(filterChain, Mockito.times(1)).doFilter(request, response);
	}

	@Test
	@DisplayName("Locale should not be accessible outside of filter chain scope")
	void test_doFilterInternal_localeNotAccessibleOutsideScope() {
		// Given
		Mockito.when(request.getHeader("Language")).thenReturn("ko-KR");
		Locale localeBeforeFilter = I18nUtils.getLocale();

		// When
		i18nFilter.doFilterInternal(request, response, filterChain);

		// Then
		Locale localeAfterFilter = I18nUtils.getLocale();
		Assertions.assertThat(localeAfterFilter).isEqualTo(localeBeforeFilter);
	}

}

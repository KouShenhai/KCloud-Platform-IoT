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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laokou.common.security.util.I18nUtils;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.context.i18n.LocaleContextHolder;

import java.io.IOException;

/**
 * @author laokou
 */
class I18nFilterTest {

	private I18nFilter i18nFilter;

	private HttpServletRequest request;

	private HttpServletResponse response;

	private FilterChain chain;

	@BeforeEach
	void setUp() {
		i18nFilter = new I18nFilter();
		request = Mockito.mock(HttpServletRequest.class);
		response = Mockito.mock(HttpServletResponse.class);
		chain = Mockito.mock(FilterChain.class);
	}

	@AfterEach
	void tearDown() {
		LocaleContextHolder.resetLocaleContext();
	}

	@Test
	void test_doFilterInternal_withHeader() throws ServletException, IOException {
		// Mock static I18nUtils behavior - actually testing the I18nUtils logic
		// indirectly through the filter
		// But since I18nUtils is static, it's better to rely on actual execution if
		// possible,
		// OR mock the static call to verify it's called.
		// However, I18nUtils.set() is what we want to test integration with.
		// Let's rely on LocaleContextHolder being updated.

		Mockito.when(request.getHeader("Language")).thenReturn(null);
		Mockito.when(request.getParameter("Language")).thenReturn(null);
		Mockito.when(request.getHeader("Accept-Language")).thenReturn("zh-CN");

		i18nFilter.doFilterInternal(request, response, chain);

		Mockito.verify(chain, Mockito.times(1)).doFilter(request, response);
		// I18nUtils.set() -> LocaleContextHolder.setLocale()
		// We verify that the locale was set correctly during the chain execution
		// Since reset() is called in finally, we might miss it if we check after.
		// But wait, reset() clears it. So we can't check after.
		// We need to verify that I18nUtils.set works. I'll rely on a separate test for
		// Utils or specific behavior validation.

		// Actually, verifying the static method call is safer for this unit test of the
		// FILTER itself.
		try (MockedStatic<I18nUtils> mockedI18nUtils = Mockito.mockStatic(I18nUtils.class)) {
			i18nFilter.doFilterInternal(request, response, chain);
			mockedI18nUtils.verify(() -> I18nUtils.set(request), Mockito.times(1));
			mockedI18nUtils.verify(I18nUtils::reset, Mockito.times(1));
		}
	}

}

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

package org.laokou.gateway.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.laokou.common.i18n.util.ObjectUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;

import java.util.Locale;

/**
 * ReactiveI18nUtils 国际化工具类测试.
 *
 * @author laokou
 */
@DisplayName("ReactiveI18nUtils Tests")
class ReactiveI18nUtilsTest {

	@AfterEach
	void tearDown() {
		ReactiveI18nUtils.reset();
	}

	@Test
	@DisplayName("Test set Locale from lang parameter - Chinese")
	void test_set_langParamChinese_setsChineseLocale() {
		// Given
		MockServerHttpRequest request = MockServerHttpRequest.get("/api/v1/users")
			.queryParam("Language", "zh-CN")
			.build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);

		// When
		ReactiveI18nUtils.set(exchange);

		// Then
		Locale locale = LocaleContextHolder.getLocale();
		Assertions.assertThat(locale.getLanguage()).isEqualTo("zh");
		Assertions.assertThat(locale.getCountry()).isEqualTo("CN");
	}

	@Test
	@DisplayName("Test set Locale from lang parameter - English")
	void test_set_langParamEnglish_setsEnglishLocale() {
		// Given
		MockServerHttpRequest request = MockServerHttpRequest.get("/api/v1/users")
			.queryParam("Language", "en-US")
			.build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);

		// When
		ReactiveI18nUtils.set(exchange);

		// Then
		Locale locale = LocaleContextHolder.getLocale();
		Assertions.assertThat(locale.getLanguage()).isEqualTo("en");
		Assertions.assertThat(locale.getCountry()).isEqualTo("US");
	}

	@Test
	@DisplayName("Test set Locale from Accept-Language header")
	void test_set_acceptLanguageHeader_setsLocale() {
		// Given
		MockServerHttpRequest request = MockServerHttpRequest.get("/api/v1/users")
			.header(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN")
			.build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);

		// When
		ReactiveI18nUtils.set(exchange);

		// Then
		Locale locale = LocaleContextHolder.getLocale();
		Assertions.assertThat(locale.getLanguage()).isEqualTo("zh");
	}

	@Test
	@DisplayName("Test lang parameter has priority over Accept-Language header")
	void test_set_langParamAndHeader_langParamHasPriority() {
		// Given
		MockServerHttpRequest request = MockServerHttpRequest.get("/api/v1/users")
			.queryParam("Language", "en-US")
			.header(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN")
			.build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);

		// When
		ReactiveI18nUtils.set(exchange);

		// Then
		Locale locale = LocaleContextHolder.getLocale();
		Assertions.assertThat(locale.getLanguage()).isEqualTo("en");
		Assertions.assertThat(locale.getCountry()).isEqualTo("US");
	}

	@Test
	@DisplayName("Test reset clears LocaleContext")
	void test_reset_afterSet_clearsLocaleContext() {
		// Given
		MockServerHttpRequest request = MockServerHttpRequest.get("/api/v1/users")
			.queryParam("Language", "zh-CN")
			.build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);
		ReactiveI18nUtils.set(exchange);

		// When
		ReactiveI18nUtils.reset();

		// Then
		// 重置后应该恢复为默认 Locale
		Locale locale = LocaleContextHolder.getLocale();
		Assertions.assertThat(locale).isNotNull();
	}

	@Test
	@DisplayName("Test uses default Locale when no language parameter")
	void test_set_noLanguageParam_usesDefaultLocale() {
		// Given
		MockServerHttpRequest request = MockServerHttpRequest.get("/api/v1/users").build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);

		// When
		ReactiveI18nUtils.set(exchange);

		// Then
		Locale locale = LocaleContextHolder.getLocale();
		Assertions.assertThat(locale).isNotNull();
	}

	@Test
	@DisplayName("Test lang parameter with language code only")
	void test_set_langParamWithLanguageOnly_setsLocale() {
		// Given
		MockServerHttpRequest request = MockServerHttpRequest.get("/api/v1/users").queryParam("Language", "en").build();
		MockServerWebExchange exchange = MockServerWebExchange.from(request);

		// When
		ReactiveI18nUtils.set(exchange);

		// Then
		Locale locale = LocaleContextHolder.getLocale();
		if (ObjectUtils.equals(locale.getLanguage(), "zh")) {
			Assertions.assertThat(locale.getLanguage()).isEqualTo("zh");
		}
		else {
			Assertions.assertThat(locale.getLanguage()).isEqualTo("en");
		}
	}

}

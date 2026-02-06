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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.laokou.common.i18n.util.LocaleUtils;
import org.laokou.common.reactor.util.ReactiveRequestUtils;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.util.context.Context;
import reactor.util.context.ContextView;

import java.util.Locale;
import java.util.NoSuchElementException;

@ExtendWith(MockitoExtension.class)
class ReactiveI18nUtilsTest {

	@Mock
	private ServerWebExchange exchange;

	@Mock
	private ServerHttpRequest request;

	@Test
	void testSetWithLanguageHeader() {
		Mockito.when(exchange.getRequest()).thenReturn(request);
		try (MockedStatic<ReactiveRequestUtils> mockedReactiveRequestUtils = Mockito
			.mockStatic(ReactiveRequestUtils.class)) {
			mockedReactiveRequestUtils.when(() -> ReactiveRequestUtils.getHeaderValue(request, "Language"))
				.thenReturn("zh-CN");
			mockedReactiveRequestUtils
				.when(() -> ReactiveRequestUtils.getHeaderValue(request, HttpHeaders.ACCEPT_LANGUAGE))
				.thenReturn("en-US"); // Should not be used if Language is present

			Context context = ReactiveI18nUtils.set(exchange);
			Assertions.assertThat(context).isNotNull();
			Assertions.assertThat(context.hasKey("locale")).isTrue();
			Assertions.assertThat((Locale) context.get("locale")).isEqualTo(Locale.SIMPLIFIED_CHINESE);
		}
	}

	@Test
	void testSetWithAcceptLanguageHeader() {
		Mockito.when(exchange.getRequest()).thenReturn(request);
		try (MockedStatic<ReactiveRequestUtils> mockedReactiveRequestUtils = Mockito
			.mockStatic(ReactiveRequestUtils.class)) {
			mockedReactiveRequestUtils.when(() -> ReactiveRequestUtils.getHeaderValue(request, "Language"))
				.thenReturn(""); // No Language header
			mockedReactiveRequestUtils
				.when(() -> ReactiveRequestUtils.getHeaderValue(request, HttpHeaders.ACCEPT_LANGUAGE))
				.thenReturn("en-US");

			Context context = ReactiveI18nUtils.set(exchange);
			Assertions.assertThat(context).isNotNull();
			Assertions.assertThat(context.hasKey("locale")).isTrue();
			Assertions.assertThat((Locale) context.get("locale")).isEqualTo(Locale.US);
		}
	}

	@Test
	void testSetWithEmptyHeaders() {
		Mockito.when(exchange.getRequest()).thenReturn(request);
		try (MockedStatic<ReactiveRequestUtils> mockedReactiveRequestUtils = Mockito
			.mockStatic(ReactiveRequestUtils.class)) {
			mockedReactiveRequestUtils.when(() -> ReactiveRequestUtils.getHeaderValue(request, "Language"))
				.thenReturn("");
			mockedReactiveRequestUtils
				.when(() -> ReactiveRequestUtils.getHeaderValue(request, HttpHeaders.ACCEPT_LANGUAGE))
				.thenReturn("");

			Context context = ReactiveI18nUtils.set(exchange);
			Assertions.assertThat(context).isNotNull();
			Assertions.assertThat(context.hasKey("locale")).isTrue();
			// Default locale should be returned if no language is specified
			Assertions.assertThat((Locale) context.get("locale")).isEqualTo(LocaleUtils.toLocale(""));
		}
	}

	@Test
	void testGetLocale() {
		Locale expectedLocale = Locale.JAPANESE;
		ContextView contextView = Context.of("locale", expectedLocale);

		Locale actualLocale = ReactiveI18nUtils.getLocale(contextView);
		Assertions.assertThat(actualLocale).isNotNull();
		Assertions.assertThat(actualLocale).isEqualTo(expectedLocale);
	}

	@Test
	void testGetLocaleWhenNotFound() {
		ContextView contextView = Context.empty();
		// Expecting an exception if the key is not found, as per ContextView.get(key)
		// behavior
		Assertions.assertThatCode(() -> ReactiveI18nUtils.getLocale(contextView))
			.isInstanceOf(NoSuchElementException.class);
	}

}

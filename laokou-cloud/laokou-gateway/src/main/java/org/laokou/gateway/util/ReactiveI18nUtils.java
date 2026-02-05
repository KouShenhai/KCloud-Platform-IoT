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

import org.jspecify.annotations.NonNull;
import org.laokou.common.i18n.util.LocaleUtils;
import org.laokou.common.i18n.util.StringExtUtils;
import org.laokou.common.reactor.util.ReactiveRequestUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.util.context.Context;
import reactor.util.context.ContextView;

import java.util.Locale;

/**
 * Reactor Context I18n工具类.
 *
 * @author laokou
 */
public final class ReactiveI18nUtils {

	private static final String LOCALE_CONTEXT_KEY = "locale";

	private ReactiveI18nUtils() {
	}

	/**
	 * 将请求头中的语言信息写入Reactor Context.
	 * @param exchange 服务网络交换机
	 * @return 包含语言信息的Reactor Context
	 */
	public static Context set(@NonNull ServerWebExchange exchange) {
		ServerHttpRequest request = exchange.getRequest();
		String language = ReactiveRequestUtils.getParamValue(request, "Language");
		language = StringExtUtils.isNotEmpty(language) ? language
				: ReactiveRequestUtils.getParamValue(request, HttpHeaders.ACCEPT_LANGUAGE);
		return Context.of(LOCALE_CONTEXT_KEY, LocaleUtils.toLocale(language));
	}

	public static Locale getLocale(ContextView contextView) {
		return contextView.get(LOCALE_CONTEXT_KEY);
	}

}

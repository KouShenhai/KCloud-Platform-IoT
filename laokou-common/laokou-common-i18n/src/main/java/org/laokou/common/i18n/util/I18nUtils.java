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

package org.laokou.common.i18n.util;

import jakarta.servlet.http.HttpServletRequest;
import org.laokou.common.i18n.common.constant.StringConstants;

import java.util.Locale;

/**
 * @author laokou
 */
public final class I18nUtils {

	public static final ScopedValue<Locale> LOCALE = ScopedValue.newInstance();

	private I18nUtils() {
	}

	public static Locale getLocale() {
		return LOCALE.orElse(Locale.getDefault());
	}

	/**
	 * @param request 请求对象
	 */
	public static Locale getLocale(HttpServletRequest request) {
		String language = getHeaderValue(request, "Language");
		language = StringExtUtils.isNotEmpty(language) ? language : getHeaderValue(request, "Accept-Language");
		return LocaleUtils.toLocale(language);
	}

	/**
	 * 获取请求头值.
	 * @param request 请求对象
	 * @param headerName 请求头名称
	 */
	private static String getHeaderValue(HttpServletRequest request, String headerName) {
		String headerValue = request.getHeader(headerName);
		return StringExtUtils.isEmpty(headerValue) ? StringConstants.EMPTY : headerValue.trim();
	}

}

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

package org.laokou.common.core.util;

import jakarta.servlet.http.HttpServletRequest;
import org.laokou.common.core.filter.I18nRequestContextFilter;
import org.laokou.common.i18n.util.LocaleUtils;
import org.laokou.common.i18n.util.StringExtUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;

/**
 * @author laokou
 */
public final class I18nUtils {

	private I18nUtils() {
	}

	/**
	 * 请求头数据写入本地线程.
	 * @param request 请求对象
	 */
	public static void set(HttpServletRequest request) {
		String language = request.getHeader(I18nRequestContextFilter.LANG);
		language = StringExtUtils.isNotEmpty(language) ? language : request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
		LocaleContextHolder.setLocale(LocaleUtils.toLocale(language), true);
	}

	/**
	 * 清空本地线程，防止内存溢出.
	 */
	public static void reset() {
		LocaleContextHolder.resetLocaleContext();
	}

}

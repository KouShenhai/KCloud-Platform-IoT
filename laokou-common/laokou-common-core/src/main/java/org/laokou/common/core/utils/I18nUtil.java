/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.common.core.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.laokou.common.i18n.utils.LocaleUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;

import static org.laokou.common.core.i18n.I18nRequestContextFilter.LANG;

/**
 * @author laokou
 */
public class I18nUtil {

	/**
	 * 请求头数据写入本地线程.
	 * @param request 请求对象
	 */
	public static void set(HttpServletRequest request) {
		String language = request.getHeader(LANG);
		language = StringUtil.isNotEmpty(language) ? language : request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
		LocaleContextHolder.setLocale(LocaleUtil.toLocale(language), true);
	}

	/**
	 * 清空本地线程，防止内存溢出.
	 */
	public static void reset() {
		LocaleContextHolder.resetLocaleContext();
	}

}

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

import java.util.Locale;

/**
 * 国际化语言工具类.
 *
 * @author laokou
 */
public final class LocaleUtils {

	private LocaleUtils() {
	}

	public static Locale toLocale(String str) {
		try {
			if (StringExtUtils.isEmpty(str)) {
				return Locale.getDefault();
			}
			return getLocale(str);
		}
		catch (Exception e) {
			return Locale.getDefault();
		}
	}

	private static Locale getLocale(String str) {
		String[] arr;
		if ((arr = substring(str.indexOf('-'), str)).length > 0
				|| (arr = substring(str.indexOf('_'), str)).length > 0) {
			// 【语言】【国家】
			return Locale.of(arr[0], arr[1]);
		}
		throw new IllegalArgumentException("language not found: " + str);
	}

	private static String[] substring(int idx, String str) {
		if (idx > 0) {
			String[] arr = new String[2];
			arr[0] = str.substring(idx - 2, idx);
			arr[1] = str.substring(idx + 1, idx + 3);
			return arr;
		}
		return new String[0];
	}

}

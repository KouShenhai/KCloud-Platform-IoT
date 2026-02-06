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

	public static Locale toLocale(String language) {
		try {
			if (StringExtUtils.isEmpty(language)) {
				return Locale.getDefault();
			}
			String[] arr = filterLanguage(language);
			// 语言 国家
			return Locale.of(arr[0], arr[1]);
		}
		catch (Exception e) {
			return Locale.getDefault();
		}
	}

	private static String[] filterLanguage(String language) {
		String[] arr;
		if ((arr = filter(language.indexOf('-'), language)).length > 0
				|| (arr = filter(language.indexOf('_'), language)).length > 0) {
			return arr;
		}
		throw new IllegalArgumentException("language not found: " + language);
	}

	private static String[] filter(int idx, String language) {
		if (idx > 0) {
			String[] arr = new String[2];
			arr[0] = language.substring(idx - 2, idx);
			arr[1] = language.substring(idx + 1, idx + 3);
			return arr;
		}
		return new String[0];
	}

}

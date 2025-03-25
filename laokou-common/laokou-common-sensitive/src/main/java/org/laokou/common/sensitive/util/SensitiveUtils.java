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

package org.laokou.common.sensitive.util;

import org.laokou.common.i18n.util.StringUtils;

import static org.laokou.common.i18n.common.constant.StringConstants.*;

/**
 * @author laokou
 */
public final class SensitiveUtils {

	private SensitiveUtils() {
	}

	public static String formatMail(String mail) {
		if (StringUtils.isEmpty(mail)) {
			return EMPTY;
		}
		int index = mail.indexOf(AT);
		if (index == -1) {
			return mail;
		}
		String str = mail.substring(index);
		return getStar(mail.length() - str.length()).concat(str);
	}

	public static String formatMobile(String mobile) {
		return formatStr(mobile, 11, 3, 6);
	}

	public static String formatStr(String s, int length, int start, int end) {
		if (StringUtils.isEmpty(s)) {
			return EMPTY;
		}
		if (s.length() != length) {
			return s;
		}
		String str = s.substring(start, end + 1);
		return s.replace(str, getStar(end - start));
	}

	private static String getStar(int len) {
		return START.repeat(Math.max(0, len));
	}

}

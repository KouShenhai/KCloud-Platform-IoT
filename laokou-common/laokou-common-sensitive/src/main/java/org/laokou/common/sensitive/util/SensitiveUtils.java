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
		return mail.replaceAll("(^\\w)[^@]*(@.*$)", "$1****$2");
	}

	public static String formatMobile(String mobile) {
		return formatStr(mobile, "****", 3, 7);
	}

	public static String formatStr(String str, String overlay, int start, int end) {
		if (str == null) {
			return null;
		}
		if (overlay == null) {
			overlay = EMPTY;
		}
		int len = str.length();
		if (start < 0) {
			start = 0;
		}
		if (start > len) {
			start = len;
		}
		if (end < 0) {
			end = 0;
		}
		if (end > len) {
			end = len;
		}
		if (start > end) {
			int temp = start;
			start = end;
			end = temp;
		}
		return str.substring(0, start) + overlay + str.substring(end);
	}

}

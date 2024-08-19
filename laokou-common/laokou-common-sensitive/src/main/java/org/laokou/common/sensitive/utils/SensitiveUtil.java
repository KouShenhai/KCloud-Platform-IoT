/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.sensitive.utils;

import org.laokou.common.i18n.utils.StringUtil;

import static org.laokou.common.i18n.common.constant.StringConstant.*;

/**
 * @author laokou
 */
public class SensitiveUtil {

	public static String formatMail(String mail) {
		if (StringUtil.isEmpty(mail)) {
			return EMPTY;
		}
		int index = mail.indexOf(AT);
		if (index == -1) {
			return mail;
		}
		return START_START.concat(START_START).concat(mail.substring(index));
	}

	public static String formatMobile(String mobile, int start, int end) {
		if (StringUtil.isEmpty(mobile)) {
			return EMPTY;
		}
		int mobileLen = 11;
		if (mobile.length() != mobileLen) {
			return mobile;
		}
		String str = mobile.substring(start, end + 1);
		return mobile.replace(str, START_START.concat(START_START));
	}

}

/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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

import java.util.regex.Pattern;

/**
 * @author laokou
 */
public class RegexUtil {

	private static final String MAIL_REGEX = "^[A-Za-z0-9-_\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";

	private static final String MOBILE_REGEX = "^((13[0-9])|(14[5,7,9])|(15[0-3,5-9])|(166)|(17[0-9])|(18[0-9])|(19[1,8,9]))\\d{8}$";

	private static final String SOURCE_REGEX = "^[a-zA-Z]+_+([0-9]+)+$";

	private static final String TIME_REGEX = "([0-1]?[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])";

	/**
	 * 邮箱验证
	 * @param mail
	 * @return
	 */
	public static boolean mailRegex(String mail) {
		return Pattern.matches(MAIL_REGEX, mail);
	}

	/**
	 * 时间验证
	 * @param time
	 * @return
	 */
	public static boolean timeRegex(String time) {
		return Pattern.matches(TIME_REGEX, time);
	}

	/**
	 * 资源名验证
	 * @param sourceName
	 * @return
	 */
	public static boolean sourceRegex(String sourceName) {
		return Pattern.matches(SOURCE_REGEX, sourceName);
	}

	/**
	 * 手机号验证
	 * @param mobile
	 * @return
	 */
	public static boolean mobileRegex(String mobile) {
		return Pattern.matches(MOBILE_REGEX, mobile);
	}

}

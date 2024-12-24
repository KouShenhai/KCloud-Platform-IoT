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

package org.laokou.common.core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.laokou.common.i18n.common.constant.StringConstant.EMPTY;

/**
 * 正则表达式工具类.
 *
 * @author laokou
 */
public final class RegexUtil {

	/**
	 * IPV4正则表达式.
	 */
	private static final String IPV4_REGEX = "((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}";

	/**
	 * 邮箱正则表达式.
	 */
	private static final String MAIL_REGEX = "^[A-Za-z0-9-_\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";

	/**
	 * 手机号正则表达式.
	 */
	private static final String MOBILE_REGEX = "^((13[0-9])|(14[5,7])|(149)|(15[0-3,5-9])|(166)|(17[0-9])|(18[0-9])|(19[1,8])|(199))\\d{8}$";

	/**
	 * 数据源名称正则表达式.
	 */
	private static final String SOURCE_REGEX = "^[a-zA-Z]+_+([0-9]+)+$";

	/**
	 * 数字正则表达式.
	 */
	private static final String NUMBER_REGEX = "^[0-9]*$";

	/**
	 * 邮箱验证.
	 * @param mail 邮箱
	 * @return 邮箱匹配结果
	 */
	public static boolean mailRegex(String mail) {
		return Pattern.matches(MAIL_REGEX, mail);
	}

	/**
	 * IP验证.
	 * @param ip IP地址
	 * @return IP匹配结果
	 */
	public static boolean ipRegex(String ip) {
		return Pattern.matches(IPV4_REGEX, ip);
	}

	/**
	 * 数字验证.
	 * @param number 数字
	 * @return 数字匹配结果
	 */
	public static boolean numberRegex(String number) {
		return Pattern.matches(NUMBER_REGEX, number);
	}

	/**
	 * 数据源名称验证.
	 * @param sourceName 自由名称
	 * @return 数据源名称匹配结果
	 */
	public static boolean sourceNameRegex(String sourceName) {
		return Pattern.matches(SOURCE_REGEX, sourceName);
	}

	/**
	 * 手机号验证.
	 * @param mobile 手机号
	 * @return 手机号匹配结果
	 */
	public static boolean mobileRegex(String mobile) {
		return Pattern.matches(MOBILE_REGEX, mobile);
	}

	/**
	 * 根据正则表达式获取值.
	 * @param input 输入值
	 * @param regex 正则表达式
	 * @return 值
	 */
	public static String getRegexValue(String input, String regex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return EMPTY;
	}

}

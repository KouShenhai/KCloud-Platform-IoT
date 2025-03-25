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

import java.util.Base64;

public final class Base64Utils {

	private Base64Utils() {
	}

	/**
	 * base64解密.
	 * @param str 字符串
	 * @return 解密后的字符串
	 */
	public static byte[] decode(String str) {
		return Base64.getDecoder().decode(str);
	}

	/**
	 * base64加密.
	 * @param strBytes 字符串
	 * @return 加密后的字符串
	 */
	public static String encodeToString(byte[] strBytes) {
		return Base64.getEncoder().encodeToString(strBytes);
	}

	/**
	 * base64加密.
	 * @param strBytes 字符串
	 * @return 加密后的字符串
	 */
	public static String encodeToStringOfMime(byte[] strBytes) {
		return Base64.getMimeEncoder().encodeToString(strBytes);
	}

	/**
	 * base64解密.
	 * @param str 字符串
	 * @return 解密后的字符串
	 */
	public static byte[] decodeOfMime(String str) {
		return Base64.getMimeDecoder().decode(str);
	}

}

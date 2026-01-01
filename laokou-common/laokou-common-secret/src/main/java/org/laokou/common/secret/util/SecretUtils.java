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

package org.laokou.common.secret.util;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * @author laokou
 */
public final class SecretUtils {

	private SecretUtils() {
	}

	/**
	 * 应用Key.
	 */
	public static final String APP_KEY = "laokou2025";

	/**
	 * 应用密钥.
	 */
	public static final String APP_SECRET = "vb05f6c45d67340zaz95v7fa6d49v99zx";

	/**
	 * MD5(appKey+appSecret+nonce+timestamp+params).
	 * @param appKey aap标识
	 * @param appSecret app密钥
	 * @param nonce 随机字符
	 * @param params 参数
	 * @param timestamp 时间戳
	 */
	public static String sign(String appKey, String appSecret, String nonce, String timestamp, String params) {
		String str = appKey.concat(appSecret).concat(nonce).concat(timestamp).concat(params);
		return DigestUtils.md5DigestAsHex(str.getBytes(StandardCharsets.UTF_8));
	}

}

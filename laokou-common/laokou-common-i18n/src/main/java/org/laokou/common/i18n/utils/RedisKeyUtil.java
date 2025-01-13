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

package org.laokou.common.i18n.utils;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * @author laokou
 */
public final class RedisKeyUtil {

	private RedisKeyUtil() {
	}

	/**
	 * 验证码Key.
	 * @param uuid UUID
	 */
	public static String getUsernamePasswordAuthCaptchaKey(String uuid) {
		String key = "auth:username-password:captcha:" + uuid;
		return DigestUtils.md5DigestAsHex(key.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * 布隆过滤器Key.
	 */
	public static String getBloomFilterKey() {
		String key = "bloom:filter";
		return DigestUtils.md5DigestAsHex(key.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * 手机验证码Key.
	 * @param mobile 手机号
	 */
	public static String getMobileAuthCaptchaKey(String mobile) {
		String key = "auth:mobile:captcha:" + mobile;
		return DigestUtils.md5DigestAsHex(key.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * 邮箱验证码Key.
	 * @param mail 邮箱
	 */
	public static String getMailAuthCaptchaKey(String mail) {
		String key = "auth:mail:captcha:" + mail;
		return DigestUtils.md5DigestAsHex(key.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * 接口幂等性Key.
	 * @param token 令牌
	 */
	public static String getApiIdempotentKey(String token) {
		String key = "api:idempotent:" + token;
		return DigestUtils.md5DigestAsHex(key.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * 动态路由Key.
	 */
	public static String getRouteDefinitionHashKey() {
		String key = "route:definition";
		return DigestUtils.md5DigestAsHex(key.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * IP缓存Key.
	 * @param type 类型
	 */
	public static String getIpCacheHashKey(String type) {
		String key = "ip:cache:" + type;
		return DigestUtils.md5DigestAsHex(key.getBytes(StandardCharsets.UTF_8));
	}

}

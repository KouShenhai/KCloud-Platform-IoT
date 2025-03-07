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
		return "auth:username-password:captcha:" + uuid;
	}

	public static String getMenuKey(String token) {
		return "menu:user:" + token;
	}

	/**
	 * 布隆过滤器Key.
	 */
	public static String getBloomFilterKey() {
		return "bloom:filter";
	}

	/**
	 * 手机验证码Key.
	 * @param mobile 手机号
	 */
	public static String getMobileAuthCaptchaKey(String mobile) {
		return "auth:mobile:captcha:" + mobile;
	}

	/**
	 * 邮箱验证码Key.
	 * @param mail 邮箱
	 */
	public static String getMailAuthCaptchaKey(String mail) {
		return "auth:mail:captcha:" + mail;
	}

	/**
	 * 接口幂等性Key.
	 * @param token 令牌
	 */
	public static String getApiIdempotentKey(String token) {
		return "api:idempotent:" + token;
	}

	/**
	 * 动态路由Key.
	 */
	public static String getRouteDefinitionHashKey() {
		return "route:definition";
	}

	/**
	 * IP缓存Key.
	 * @param type 类型
	 */
	public static String getIpCacheHashKey(String type) {
		return "ip:cache:" + type;
	}

}

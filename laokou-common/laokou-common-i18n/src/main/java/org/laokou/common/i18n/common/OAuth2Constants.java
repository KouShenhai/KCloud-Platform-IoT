/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.common.i18n.common;

/**
 * @author laokou
 */
public final class OAuth2Constants {

	private OAuth2Constants() {
	}

	/**
	 * 认证令牌.
	 */
	public static final String ACCESS_TOKEN = "accessToken";

	/**
	 * 刷新令牌.
	 */
	public static final String REFRESH_TOKEN = "refreshToken";

	/**
	 * 路径.
	 */
	public static final String TOKEN_URL = "/oauth2/token";

	/**
	 * 用户名.
	 */
	public static final String USERNAME = "username";

	/**
	 * 密码.
	 */
	public static final String PASSWORD = "password";

	/**
	 * 邮箱.
	 */
	public static final String MAIL = "mail";

	/**
	 * 手机.
	 */
	public static final String MOBILE = "mobile";

	/**
	 * 唯一标识.
	 */
	public static final String UUID = "uuid";

	/**
	 * 验证码.
	 */
	public static final String CAPTCHA = "captcha";

	/**
	 * 错误.
	 */
	public static final String ERROR = "error";

	/**
	 * 错误信息.
	 */
	public static final String ERROR_DESCRIPTION = "error_description";

	/**
	 * 所有.
	 */
	public static final String FULL = "full";

	/**
	 * 存入redis的hash键.
	 */
	public static final String REDIS_OAUTH2_AUTHORIZATION_KEY = "oauth2:authorization";

	/**
	 * 错误地址.
	 */
	public static final String ERROR_URL = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

}

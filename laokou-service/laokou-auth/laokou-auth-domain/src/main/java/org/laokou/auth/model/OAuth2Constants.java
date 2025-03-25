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

package org.laokou.auth.model;

/**
 * OAuth2常量.
 *
 * @author laokou
 */
public final class OAuth2Constants {

	private OAuth2Constants() {
	}

	/**
	 * 租户不存在.
	 */
	public static final String TENANT_NOT_EXIST = "B_OAuth2_TenantNotExist";

	/**
	 * 表不存在.
	 */
	public static final String DATA_TABLE_NOT_EXIST = "B_OAuth2_DataTableNotExist";

	/**
	 * 数据源不存在.
	 */
	public static final String DATA_SOURCE_NOT_EXIST = "B_OAuth2_DataSourceNotExist";

	/**
	 * 令牌生成器无法生成标识令牌.
	 */
	public static final String GENERATE_ID_TOKEN_FAIL = "B_OAuth2_GenerateIdTokenFail";

	/**
	 * 令牌生成器无法生成刷新令牌.
	 */
	public static final String GENERATE_REFRESH_TOKEN_FAIL = "B_OAuth2_GenerateRefreshTokenFail";

	/**
	 * 令牌生成器无法生成访问令牌.
	 */
	public static final String GENERATE_ACCESS_TOKEN_FAIL = "B_OAuth2_GenerateAccessTokenFail";

	/**
	 * 注册客户端不存在.
	 */
	public static final String REGISTERED_CLIENT_NOT_EXIST = "B_OAuth2_RegisteredClientNotExist";

	/**
	 * 无效客户端.
	 */
	public static final String INVALID_CLIENT = "B_OAuth2_InvalidClient";

	/**
	 * 无效作用域.
	 */
	public static final String INVALID_SCOPE = "B_OAuth2_InvalidScope";

	/**
	 * 用户已禁用.
	 */
	public static final String USER_DISABLED = "B_OAuth2_UserDisabled";

	/**
	 * 验证码已过期.
	 */
	public static final String CAPTCHA_EXPIRED = "B_OAuth2_CaptchaExpired";

	/**
	 * 验证码错误.
	 */
	public static final String CAPTCHA_ERROR = "B_OAuth2_CaptchaError";

	/**
	 * 用户名或密码错误.
	 */
	public static final String USERNAME_PASSWORD_ERROR = "B_OAuth2_UsernamePasswordError";

	/**
	 * 手机号未注册.
	 */
	public static final String MOBILE_NOT_REGISTERED = "B_OAuth2_MobileNotRegistered";

	/**
	 * 邮箱未注册.
	 */
	public static final String MAIL_NOT_REGISTERED = "B_OAuth2_MailNotRegistered";

	/**
	 * 会话无效.
	 */
	public static final String SESSION_INVALID = "B_OAuth2_SessionInvalid";

	/**
	 * 会话过期.
	 */
	public static final String SESSION_EXPIRED = "B_OAuth2_SessionExpired";

	/**
	 * UUID不能为空.
	 */
	public static final String UUID_REQUIRE = "P_OAuth2_UuidIsNull";

	/**
	 * 验证码不能为空.
	 */
	public static final String CAPTCHA_REQUIRE = "P_OAuth2_CaptchaIsNull";

	/**
	 * 用户名不能为空.
	 */
	public static final String USERNAME_REQUIRE = "P_OAuth2_UsernameIsNull";

	/**
	 * 密码不能为空.
	 */
	public static final String PASSWORD_REQUIRE = "P_OAuth2_PasswordIsNull";

	/**
	 * 手机号不能为空.
	 */
	public static final String MOBILE_REQUIRE = "P_OAuth2_MobileIsNUll";

	/**
	 * 邮箱不能为空.
	 */
	public static final String MAIL_REQUIRE = "P_OAuth2_MailIsNull";

	/**
	 * 租户编码不能为空.
	 */
	public static final String TENANT_CODE_REQUIRE = "P_OAuth2_TenantCodeIsNull";

	/**
	 * 邮箱错误.
	 */
	public static final String MAIL_ERROR = "P_OAuth2_MailError";

	/**
	 * 手机号错误.
	 */
	public static final String MOBILE_ERROR = "P_OAuth2_MobileError";

}

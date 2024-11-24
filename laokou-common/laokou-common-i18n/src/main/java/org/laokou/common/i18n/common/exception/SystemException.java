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

package org.laokou.common.i18n.common.exception;

/**
 * 系统异常.
 *
 * @author laokou
 */
public final class SystemException extends GlobalException {

	/**
	 * IP已列入黑名单.
	 */
	public static final String IP_BLACKED = "S_Ip_Blacked";

	/**
	 * IP被限制.
	 */
	public static final String IP_RESTRICTED = "S_Ip_Restricted";

	/**
	 * 路由不存在.
	 */
	public static final String ROUTER_NOT_EXIST = "S_Gateway_RouterNotExist";

	/**
	 * 授权规则错误.
	 */
	public static final String AUTHORITY = "S_Sentinel_Authority";

	/**
	 * 系统规则错误.
	 */
	public static final String SYSTEM_BLOCKED = "S_Sentinel_SystemBlocked";

	/**
	 * 热点参数已限流.
	 */
	public static final String PARAM_FLOWED = "S_Sentinel_ParamFlowed";

	/**
	 * 已降级.
	 */
	public static final String DEGRADED = "S_Sentinel_Degraded";

	/**
	 * 已限流.
	 */
	public static final String FLOWED = "S_Sentinel_Flowed";

	/**
	 * 表不存在.
	 */
	public static final String OAUTH2_DATA_TABLE_NOT_EXIST = "S_OAuth2_DataTableNotExist";

	/**
	 * 用户名解密失败.
	 */
	public static final String USERNAME_AES_DECRYPT_FAIL = "S_User_UsernameAESDecryptFail";

	/**
	 * 手机号解密失败.
	 */
	public static final String MOBILE_AES_DECRYPT_FAIL = "S_User_MobileAESDecryptFail";

	/**
	 * 邮箱解密失败.
	 */
	public static final String MAIL_AES_DECRYPT_FAIL = "S_User_MailAESDecryptFail";

	/**
	 * 数据源不存在.
	 */
	public static final String OAUTH2_DATA_SOURCE_NOT_EXIST = "S_OAuth2_DataSourceNotExist";

	/**
	 * 令牌生成器无法生成标识令牌.
	 */
	public static final String OAUTH2_GENERATE_ID_TOKEN_FAIL = "S_OAuth2_GenerateIdTokenFail";

	/**
	 * 令牌生成器无法生成刷新令牌.
	 */
	public static final String OAUTH2_GENERATE_REFRESH_TOKEN_FAIL = "S_OAuth2_GenerateRefreshTokenFail";

	/**
	 * 令牌生成器无法生成访问令牌.
	 */
	public static final String OAUTH2_GENERATE_ACCESS_TOKEN_FAIL = "S_OAuth2_GenerateAccessTokenFail";

	/**
	 * 注册客户端不存在.
	 */
	public static final String OAUTH2_REGISTERED_CLIENT_NOT_EXIST = "S_OAuth2_RegisteredClientNotExist";

	/**
	 * 无效客户端.
	 */
	public static final String OAUTH2_INVALID_CLIENT = "S_OAuth2_InvalidClient";

	/**
	 * 无效作用域.
	 */
	public static final String OAUTH2_INVALID_SCOPE = "S_OAuth2_InvalidScope";

	/**
	 * 用户已禁用.
	 */
	public static final String OAUTH2_USER_DISABLED = "S_OAuth2_UserDisabled";

	/**
	 * 验证码已过期.
	 */
	public static final String OAUTH2_CAPTCHA_EXPIRED = "S_OAuth2_CaptchaExpired";

	/**
	 * 验证码错误.
	 */
	public static final String OAUTH2_CAPTCHA_ERROR = "S_OAuth2_CaptchaError";

	/**
	 * 用户名或密码错误.
	 */
	public static final String OAUTH2_USERNAME_PASSWORD_ERROR = "S_OAuth2_UsernamePasswordError";

	/**
	 * 手机号未注册.
	 */
	public static final String OAUTH2_MOBILE_NOT_REGISTERED = "S_OAuth2_MobileNotRegistered";

	/**
	 * 邮箱未注册.
	 */
	public static final String OAUTH2_MAIL_NOT_REGISTERED = "S_OAuth2_MailNotRegistered";

	public SystemException(String code) {
		super(code);
	}

	public SystemException(String code, String msg) {
		super(code, msg);
	}

}

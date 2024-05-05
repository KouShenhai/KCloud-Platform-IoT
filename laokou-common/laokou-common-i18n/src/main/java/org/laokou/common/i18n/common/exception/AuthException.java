/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author laokou
 */
@Schema(name = "AuthException", description = "认证异常")
public final class AuthException extends GlobalException {

	@Schema(name = "INVALID_REQUEST", description = "无效请求")
	public static final String INVALID_REQUEST = "A_OAuth2_InvalidRequest";

	@Schema(name = "GENERATE_ID_TOKEN_FAIL", description = "令牌生成器无法生成标识令牌")
	public static final String GENERATE_ID_TOKEN_FAIL = "A_OAuth2_GenerateIdTokenFail";

	@Schema(name = "GENERATE_REFRESH_TOKEN_FAIL", description = "令牌生成器无法生成刷新令牌")
	public static final String GENERATE_REFRESH_TOKEN_FAIL = "A_OAuth2_GenerateRefreshTokenFail";

	@Schema(name = "GENERATE_ACCESS_TOKEN_FAIL", description = "令牌生成器无法生成访问令牌")
	public static final String GENERATE_ACCESS_TOKEN_FAIL = "A_OAuth2_GenerateAccessTokenFail";

	@Schema(name = "REGISTERED_CLIENT_NOT_EXIST", description = "注册客户端不存在")
	public static final String REGISTERED_CLIENT_NOT_EXIST = "A_OAuth2_RegisteredClientNotExist";

	@Schema(name = "INVALID_CLIENT", description = "无效客户端")
	public static final String INVALID_CLIENT = "A_OAuth2_InvalidClient";

	@Schema(name = "INVALID_SCOPE", description = "无效作用域")
	public static final String INVALID_SCOPE = "A_OAuth2_InvalidScope";

	@Schema(name = "MAIL_ERROR", description = "邮箱错误")
	public static final String MAIL_ERROR = "A_OAuth2_MailError";

	@Schema(name = "MOBILE_ERROR", description = "手机号错误")
	public static final String MOBILE_ERROR = "A_OAuth2_MobileError";

	@Schema(name = "ACCOUNT_DISABLED", description = "账号已禁用")
	public static final String ACCOUNT_DISABLED = "A_OAuth2_AccountDisabled";

	@Schema(name = "CAPTCHA_EXPIRED", description = "验证码已过期")
	public static final String CAPTCHA_EXPIRED = "A_OAuth2_CaptchaExpired";

	@Schema(name = "CAPTCHA_ERROR", description = "验证码错误")
	public static final String CAPTCHA_ERROR = "A_OAuth2_CaptchaError";

	@Schema(name = "USERNAME_PASSWORD_ERROR", description = "用户名或密码错误")
	public static final String USERNAME_PASSWORD_ERROR = "A_OAuth2_UsernamePasswordError";

	@Schema(name = "SOURCE_NOT_EXIST", description = "数据源不存在")
	public static final String SOURCE_NOT_EXIST = "A_OAuth2_SourceNotExist";

	public AuthException(String code) {
		super(code);
	}

	public AuthException(String code, String msg) {
		super(code, msg);
	}

}

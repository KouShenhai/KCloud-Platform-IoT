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

	@Schema(name = "OAUTH2_INVALID_REQUEST", description = "无效请求")
	public static final String OAUTH2_INVALID_REQUEST = "A_OAuth2_InvalidRequest";

	@Schema(name = "OAUTH2_GENERATE_ID_TOKEN_FAIL", description = "令牌生成器无法生成标识令牌")
	public static final String OAUTH2_GENERATE_ID_TOKEN_FAIL = "A_OAuth2_GenerateIdTokenFail";

	@Schema(name = "OAUTH2_GENERATE_REFRESH_TOKEN_FAIL", description = "令牌生成器无法生成刷新令牌")
	public static final String OAUTH2_GENERATE_REFRESH_TOKEN_FAIL = "A_OAuth2_GenerateRefreshTokenFail";

	@Schema(name = "OAUTH2_GENERATE_ACCESS_TOKEN_FAIL", description = "令牌生成器无法生成访问令牌")
	public static final String OAUTH2_GENERATE_ACCESS_TOKEN_FAIL = "A_OAuth2_GenerateAccessTokenFail";

	@Schema(name = "OAUTH2_REGISTERED_CLIENT_NOT_EXIST", description = "注册客户端不存在")
	public static final String OAUTH2_REGISTERED_CLIENT_NOT_EXIST = "A_OAuth2_RegisteredClientNotExist";

	@Schema(name = "OAUTH2_INVALID_CLIENT", description = "无效客户端")
	public static final String OAUTH2_INVALID_CLIENT = "A_OAuth2_InvalidClient";

	@Schema(name = "OAUTH2_INVALID_SCOPE", description = "无效作用域")
	public static final String OAUTH2_INVALID_SCOPE = "A_OAuth2_InvalidScope";

	@Schema(name = "OAUTH2_USER_DISABLED", description = "用户已禁用")
	public static final String OAUTH2_USER_DISABLED = "A_OAuth2_UserDisabled";

	@Schema(name = "OAUTH2_CAPTCHA_EXPIRED", description = "验证码已过期")
	public static final String OAUTH2_CAPTCHA_EXPIRED = "A_OAuth2_CaptchaExpired";

	@Schema(name = "OAUTH2_CAPTCHA_ERROR", description = "验证码错误")
	public static final String OAUTH2_CAPTCHA_ERROR = "A_OAuth2_CaptchaError";

	@Schema(name = "OAUTH2_USERNAME_PASSWORD_ERROR", description = "用户名或密码错误")
	public static final String OAUTH2_USERNAME_PASSWORD_ERROR = "A_OAuth2_UsernamePasswordError";

	@Schema(name = "OAUTH2_SOURCE_NOT_EXIST", description = "数据源不存在")
	public static final String OAUTH2_SOURCE_NOT_EXIST = "A_OAuth2_SourceNotExist";

	public AuthException(String code) {
		super(code);
	}

	public AuthException(String code, String msg) {
		super(code, msg);
	}

}

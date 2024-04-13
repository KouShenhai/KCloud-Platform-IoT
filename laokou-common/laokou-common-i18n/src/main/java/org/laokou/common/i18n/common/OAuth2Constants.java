/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author laokou
 */
@Schema(name = "OAuth2Constants", description = "OAuth2常量")
public final class OAuth2Constants {

	private OAuth2Constants() {
	}

	@Schema(name = "ACCESS_TOKEN", description = "认证令牌")
	public static final String ACCESS_TOKEN = "accessToken";

	@Schema(name = "REFRESH_TOKEN", description = "刷新令牌")
	public static final String REFRESH_TOKEN = "refreshToken";

	@Schema(name = "TOKEN_URL", description = "令牌路径")
	public static final String TOKEN_URL = "/oauth2/token";

	@Schema(name = "USERNAME", description = "用户名")
	public static final String USERNAME = "username";

	@Schema(name = "PASSWORD", description = "密码")
	public static final String PASSWORD = "password";

	@Schema(name = "MAIL", description = "邮箱")
	public static final String MAIL = "mail";

	@Schema(name = "MOBILE", description = "手机")
	public static final String MOBILE = "mobile";

	@Schema(name = "UUID", description = "唯一标识")
	public static final String UUID = "uuid";

	@Schema(name = "CAPTCHA", description = "验证码")
	public static final String CAPTCHA = "captcha";

	@Schema(name = "ERROR", description = "错误")
	public static final String ERROR = "error";

	@Schema(name = "ERROR_DESCRIPTION", description = "错误信息")
	public static final String ERROR_DESCRIPTION = "error_description";

	@Schema(name = "REDIS_OAUTH2_AUTHORIZATION_KEY", description = "存入Redis的Hash键")
	public static final String REDIS_OAUTH2_AUTHORIZATION_KEY = "oauth2:authorization";

	@Schema(name = "AUTHORIZATION_CODE", description = "授权码")
	public static final String AUTHORIZATION_CODE = "authorization_code";

}

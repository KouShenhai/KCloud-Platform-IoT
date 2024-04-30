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
@Schema(name = "ParamException", description = "参数异常")
public final class ParamException extends GlobalException {

	@Schema(name = "OAUTH2_UUID_REQUIRE", description = "UUID不能为空")
	public static final String OAUTH2_UUID_REQUIRE = "P_OAuth2_UuidIsNull";

	@Schema(name = "OAUTH2_CAPTCHA_REQUIRE", description = "验证码不能为空")
	public static final String OAUTH2_CAPTCHA_REQUIRE = "P_OAuth2_CaptchaIsNull";

	@Schema(name = "OAUTH2_USERNAME_REQUIRE", description = "账号不能为空")
	public static final String OAUTH2_USERNAME_REQUIRE = "P_OAuth2_UsernameIsNull";

	@Schema(name = "OAUTH2_PASSWORD_REQUIRE", description = "密码不能为空")
	public static final String OAUTH2_PASSWORD_REQUIRE = "P_OAuth2_PasswordIsNull";

	@Schema(name = "OAUTH2_MOBILE_REQUIRE", description = "手机号不能为空")
	public static final String OAUTH2_MOBILE_REQUIRE = "P_OAuth2_MobileIsNUll";

	@Schema(name = "OAUTH2_MAIL_REQUIRE", description = "邮箱不能为空")
	public static final String OAUTH2_MAIL_REQUIRE = "P_OAuth2_MailIsNull";

	@Schema(name = "OAUTH2_TENANT_ID_REQUIRE", description = "租户ID不能为空")
	public static final String OAUTH2_TENANT_ID_REQUIRE = "P_OAuth2_TenantIdIsNull";

	@Schema(name = "SYSTEM_ID_REQUIRE", description = "ID不能为空")
	public static final String SYSTEM_ID_REQUIRE = "P_System_IdIsNull";

	public ParamException(String code) {
		super(code);
	}

	public ParamException(String code, String msg) {
		super(code, msg);
	}

}

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

package org.laokou.auth.domain.model.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.laokou.common.core.utils.RegexUtil;
import org.laokou.common.i18n.common.exception.AuthException;
import org.laokou.common.i18n.utils.MessageUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.i18n.utils.ValidatorUtil;

import static org.laokou.auth.domain.model.auth.AuthA.*;
import static org.laokou.common.i18n.common.exception.AuthException.MAIL_ERROR;
import static org.laokou.common.i18n.common.exception.AuthException.MOBILE_ERROR;
import static org.laokou.common.i18n.common.exception.ParamException.*;

/**
 * @author laokou
 */
@Getter
@Schema(name = "CaptchaV", description = "验证码值对象")
public record CaptchaV(@Schema(name = "uuid", description = "UUID") String uuid,
					   @Schema(name = "type", description = "类型 mail邮箱 mobile手机号 password密码 authorization_code授权码") String type,
					   @Schema(name = "captcha", description = "验证码") String captcha) {

	public CaptchaV(String uuid, String type, String captcha) {
		this.uuid = uuid;
		this.type = type;
		this.captcha = captcha;
	}

	void checkNullCaptcha() {
		if (StringUtil.isEmpty(this.captcha)) {
			throw new AuthException(OAUTH2_CAPTCHA_REQUIRE, ValidatorUtil.getMessage(OAUTH2_CAPTCHA_REQUIRE));
		}
	}

	void checkMobile() {
		if (!RegexUtil.mobileRegex(this.uuid)) {
			throw new AuthException(MOBILE_ERROR, MessageUtil.getMessage(MOBILE_ERROR));
		}
	}

	void checkMail() {
		if (!RegexUtil.mailRegex(this.uuid)) {
			throw new AuthException(MAIL_ERROR, MessageUtil.getMessage(MAIL_ERROR));
		}
	}

	void checkNullUuid() {
		if (StringUtil.isEmpty(this.uuid)) {
			switch (type) {
				case PASSWORD: throw new AuthException(OAUTH2_UUID_REQUIRE, ValidatorUtil.getMessage(OAUTH2_UUID_REQUIRE));
				case MAIL: throw new AuthException(OAUTH2_MAIL_REQUIRE, ValidatorUtil.getMessage(OAUTH2_MAIL_REQUIRE));
				case MOBILE: throw new AuthException(OAUTH2_MOBILE_REQUIRE, ValidatorUtil.getMessage(OAUTH2_MOBILE_REQUIRE));
			}
		}
	}

}

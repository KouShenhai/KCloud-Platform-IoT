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
import org.laokou.common.i18n.common.exception.AuthException;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.i18n.utils.ValidatorUtils;

import static org.laokou.common.i18n.common.exception.ParamException.OAUTH2_CAPTCHA_REQUIRE;
import static org.laokou.common.i18n.common.exception.ParamException.OAUTH2_UUID_REQUIRE;

/**
 * @author laokou
 */
@Getter
@Schema(name = "CaptchaV", description = "验证码值对象")
public class CaptchaV {

	@Schema(name = "uuid", description = "唯一标识")
	private final String uuid;

	@Schema(name = "captcha", description = "验证码")
	private final String captcha;

	public CaptchaV(String uuid, String captcha) {
		this.uuid = uuid;
		this.captcha = captcha;
	}

	private void checkNullCaptcha() {
		if (StringUtil.isEmpty(this.captcha)) {
			throw new AuthException(OAUTH2_CAPTCHA_REQUIRE, ValidatorUtils.getMessage(OAUTH2_CAPTCHA_REQUIRE));
		}
	}

	private void checkNullUuid() {
		if (StringUtil.isEmpty(this.uuid)) {
			throw new AuthException(OAUTH2_UUID_REQUIRE, ValidatorUtils.getMessage(OAUTH2_UUID_REQUIRE));
		}
	}

}

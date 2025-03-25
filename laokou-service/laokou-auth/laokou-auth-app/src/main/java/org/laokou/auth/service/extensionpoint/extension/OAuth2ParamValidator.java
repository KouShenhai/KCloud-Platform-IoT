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

package org.laokou.auth.service.extensionpoint.extension;

import org.laokou.common.core.util.RegexUtils;
import org.laokou.common.i18n.util.ParamValidator;
import org.laokou.common.i18n.util.StringUtils;
import org.laokou.common.i18n.util.ValidatorUtils;

import static org.laokou.auth.model.OAuth2Constants.*;
import static org.laokou.common.i18n.util.ParamValidator.*;

/**
 * @author laokou
 */
final class OAuth2ParamValidator {

	private OAuth2ParamValidator() {
	}

	public static ParamValidator.Validate validateUsername(String username) {
		if (StringUtils.isEmpty(username)) {
			return invalidate(ValidatorUtils.getMessage(USERNAME_REQUIRE));
		}
		return validate();
	}

	public static ParamValidator.Validate validatePassword(String password) {
		if (StringUtils.isEmpty(password)) {
			return invalidate(ValidatorUtils.getMessage(PASSWORD_REQUIRE));
		}
		return validate();
	}

	public static ParamValidator.Validate validateMail(String mail) {
		if (StringUtils.isEmpty(mail)) {
			return invalidate(ValidatorUtils.getMessage(MAIL_REQUIRE));
		}
		else if (RegexUtils.mailRegex(mail)) {
			return validate();
		}
		return invalidate(ValidatorUtils.getMessage(MAIL_ERROR));
	}

	public static ParamValidator.Validate validateMobile(String mobile) {
		if (StringUtils.isEmpty(mobile)) {
			return invalidate(ValidatorUtils.getMessage(MOBILE_REQUIRE));
		}
		else if (RegexUtils.mobileRegex(mobile)) {
			return validate();
		}
		return invalidate(ValidatorUtils.getMessage(MOBILE_ERROR));
	}

	public static ParamValidator.Validate validateUuid(String uuid) {
		if (StringUtils.isEmpty(uuid)) {
			return invalidate(ValidatorUtils.getMessage(UUID_REQUIRE));
		}
		return validate();
	}

	public static ParamValidator.Validate validateTenantCode(String tenantCode) {
		if (StringUtils.isEmpty(tenantCode)) {
			return invalidate(ValidatorUtils.getMessage(TENANT_CODE_REQUIRE));
		}
		return validate();
	}

	public static ParamValidator.Validate validateCaptcha(String captcha) {
		if (StringUtils.isEmpty(captcha)) {
			return invalidate(ValidatorUtils.getMessage(CAPTCHA_REQUIRE));
		}
		return validate();
	}

}

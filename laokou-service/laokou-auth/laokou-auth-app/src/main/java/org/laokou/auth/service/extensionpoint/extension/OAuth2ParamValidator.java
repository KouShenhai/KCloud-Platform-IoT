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

import org.laokou.common.core.utils.RegexUtil;
import org.laokou.common.i18n.utils.ParamValidator;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.i18n.utils.ValidatorUtil;

import static org.laokou.common.i18n.common.exception.ParamException.OAuth2.*;
import static org.laokou.common.i18n.utils.ParamValidator.*;

/**
 * @author laokou
 */
final class OAuth2ParamValidator {

	private OAuth2ParamValidator() {
	}

	public static ParamValidator.Validate validateUsername(String username) {
		if (StringUtil.isEmpty(username)) {
			return invalidate(ValidatorUtil.getMessage(USERNAME_REQUIRE));
		}
		return validate();
	}

	public static ParamValidator.Validate validatePassword(String password) {
		if (StringUtil.isEmpty(password)) {
			return invalidate(ValidatorUtil.getMessage(PASSWORD_REQUIRE));
		}
		return validate();
	}

	public static ParamValidator.Validate validateMail(String mail) {
		if (StringUtil.isEmpty(mail)) {
			return invalidate(ValidatorUtil.getMessage(MAIL_REQUIRE));
		}
		else if (RegexUtil.mailRegex(mail)) {
			return validate();
		}
		return invalidate(ValidatorUtil.getMessage(MAIL_ERROR));
	}

	public static ParamValidator.Validate validateMobile(String mobile) {
		if (StringUtil.isEmpty(mobile)) {
			return invalidate(ValidatorUtil.getMessage(MOBILE_REQUIRE));
		}
		else if (RegexUtil.mobileRegex(mobile)) {
			return validate();
		}
		return invalidate(ValidatorUtil.getMessage(MOBILE_ERROR));
	}

	public static ParamValidator.Validate validateUuid(String uuid) {
		if (StringUtil.isEmpty(uuid)) {
			return invalidate(ValidatorUtil.getMessage(UUID_REQUIRE));
		}
		return validate();
	}

	public static ParamValidator.Validate validateTenantCode(String tenantCode) {
		if (StringUtil.isEmpty(tenantCode)) {
			return invalidate(ValidatorUtil.getMessage(TENANT_CODE_REQUIRE));
		}
		return validate();
	}

	public static ParamValidator.Validate validateCaptcha(String captcha) {
		if (StringUtil.isEmpty(captcha)) {
			return invalidate(ValidatorUtil.getMessage(CAPTCHA_REQUIRE));
		}
		return validate();
	}

}

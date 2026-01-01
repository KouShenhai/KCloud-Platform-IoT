/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.auth.service.validator;

import org.laokou.auth.model.OAuth2Constants;
import org.laokou.common.core.util.RegexUtils;
import org.laokou.common.i18n.util.ParamValidator;
import org.laokou.common.i18n.util.StringExtUtils;
import org.laokou.common.i18n.util.ValidatorUtils;

/**
 * @author laokou
 */
final class OAuth2ParamValidator {

	private OAuth2ParamValidator() {
	}

	public static ParamValidator.Validate validateUsername(String username) {
		if (StringExtUtils.isEmpty(username)) {
			return ParamValidator.invalidate(ValidatorUtils.getMessage(OAuth2Constants.USERNAME_REQUIRE));
		}
		return ParamValidator.validate();
	}

	public static ParamValidator.Validate validatePassword(String password) {
		if (StringExtUtils.isEmpty(password)) {
			return ParamValidator.invalidate(ValidatorUtils.getMessage(OAuth2Constants.PASSWORD_REQUIRE));
		}
		return ParamValidator.validate();
	}

	public static ParamValidator.Validate validateMail(String mail) {
		if (StringExtUtils.isEmpty(mail)) {
			return ParamValidator.invalidate(ValidatorUtils.getMessage(OAuth2Constants.MAIL_REQUIRE));
		}
		else if (RegexUtils.mailRegex(mail)) {
			return ParamValidator.validate();
		}
		return ParamValidator.invalidate(ValidatorUtils.getMessage(OAuth2Constants.MAIL_ERROR));
	}

	public static ParamValidator.Validate validateMobile(String mobile) {
		if (StringExtUtils.isEmpty(mobile)) {
			return ParamValidator.invalidate(ValidatorUtils.getMessage(OAuth2Constants.MOBILE_REQUIRE));
		}
		else if (RegexUtils.mobileRegex(mobile)) {
			return ParamValidator.validate();
		}
		return ParamValidator.invalidate(ValidatorUtils.getMessage(OAuth2Constants.MOBILE_ERROR));
	}

	public static ParamValidator.Validate validateUuid(String uuid) {
		if (StringExtUtils.isEmpty(uuid)) {
			return ParamValidator.invalidate(ValidatorUtils.getMessage(OAuth2Constants.UUID_REQUIRE));
		}
		return ParamValidator.validate();
	}

	public static ParamValidator.Validate validateTenantCode(String tenantCode) {
		if (StringExtUtils.isEmpty(tenantCode)) {
			return ParamValidator.invalidate(ValidatorUtils.getMessage(OAuth2Constants.TENANT_CODE_REQUIRE));
		}
		return ParamValidator.validate();
	}

	public static ParamValidator.Validate validateCaptcha(String captcha) {
		if (StringExtUtils.isEmpty(captcha)) {
			return ParamValidator.invalidate(ValidatorUtils.getMessage(OAuth2Constants.CAPTCHA_REQUIRE));
		}
		return ParamValidator.validate();
	}

}

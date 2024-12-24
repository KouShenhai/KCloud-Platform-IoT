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

package org.laokou.auth.service.extensionpoint.extension;

import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.i18n.utils.ValidatorUtil;

import static org.laokou.common.i18n.common.constant.StringConstant.EMPTY;
import static org.laokou.common.i18n.common.exception.ParamException.OAuth2.*;

/**
 * @author laokou
 */
public class OAuth2ParamValidator {

	public static String validateUsername(String username) {
		if (StringUtil.isEmpty(username)) {
			return ValidatorUtil.getMessage(USERNAME_REQUIRE);
		}
		return EMPTY;
	}

	public static String validatePassword(String password) {
		if (StringUtil.isEmpty(password)) {
			return ValidatorUtil.getMessage(PASSWORD_REQUIRE);
		}
		return EMPTY;
	}

//	public static String validateMail(String mail) {
//		if (StringUtil.isEmpty(mail)) {
//			return ValidatorUtil.getMessage();
//		}
//	}
//
//	public static String validateMobile(String mobile) {
//
//	}

	public static String validateUuid(String uuid) {
		if (StringUtil.isEmpty(uuid)) {
			return ValidatorUtil.getMessage(UUID_REQUIRE);
		}
		return EMPTY;
	}

	public static String validateTenantCode(String tenantCode) {
		if (StringUtil.isEmpty(tenantCode)) {
			return ValidatorUtil.getMessage(TENANT_CODE_REQUIRE);
		}
		return EMPTY;
	}

	public static String validateCaptcha(String captcha) {
		if (StringUtil.isEmpty(captcha)) {
			return ValidatorUtil.getMessage(CAPTCHA_REQUIRE);
		}
		return EMPTY;
	}

}

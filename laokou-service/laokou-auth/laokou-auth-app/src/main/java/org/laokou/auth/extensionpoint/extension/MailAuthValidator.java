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

package org.laokou.auth.extensionpoint.extension;

import org.laokou.auth.extensionpoint.AuthValidatorExtPt;
import org.laokou.auth.domain.model.auth.AuthA;
import org.laokou.common.core.utils.RegexUtil;
import org.laokou.common.extension.Extension;
import org.laokou.common.i18n.common.exception.AuthException;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.i18n.utils.ValidatorUtil;

import static org.laokou.auth.domain.model.auth.AuthA.*;
import static org.laokou.common.i18n.common.exception.ParamException.*;
import static org.laokou.common.i18n.common.exception.ParamException.OAUTH2_CAPTCHA_REQUIRE;

/**
 * @author laokou
 */
@Extension(bizId = BIZ_ID, useCase = USE_CASE, scenario = MAIL)
public class MailAuthValidator implements AuthValidatorExtPt {

	@Override
	public void validate(AuthA auth) {
		// 租户ID判空
		if (ObjectUtil.isNull(auth.getTenantId())) {
			throw new AuthException(OAUTH2_TENANT_ID_REQUIRE, ValidatorUtil.getMessage(OAUTH2_TENANT_ID_REQUIRE));
		}
		// 邮箱判空
		if (StringUtil.isEmpty(auth.getCaptcha().uuid())) {
			throw new AuthException(OAUTH2_MAIL_REQUIRE, ValidatorUtil.getMessage(OAUTH2_MAIL_REQUIRE));
		}
		// 验证码判空
		if (StringUtil.isEmpty(auth.getCaptcha().captcha())) {
			throw new AuthException(OAUTH2_CAPTCHA_REQUIRE, ValidatorUtil.getMessage(OAUTH2_CAPTCHA_REQUIRE));
		}
		// 邮箱格式判断
		if (!RegexUtil.mailRegex(auth.getCaptcha().uuid())) {
			throw new AuthException(OAUTH2_MAIL_ERROR, ValidatorUtil.getMessage(OAUTH2_MAIL_ERROR));
		}
	}

}

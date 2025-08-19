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

package org.laokou.auth.service.validator;

import org.laokou.auth.model.CaptchaE;
import org.laokou.auth.model.CaptchaParamValidator;
import org.laokou.common.i18n.util.ParamValidator;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Component("mailCaptchaParamValidator")
public class MailCaptchaParamValidator implements CaptchaParamValidator {

	@Override
	public void validateCaptcha(CaptchaE captcha) {
		ParamValidator.validate(
				// 校验租户编码
				OAuth2ParamValidator.validateTenantCode(captcha.getTenantCode()),
				// 校验邮箱
				OAuth2ParamValidator.validateMail(captcha.getUuid()));
	}

}

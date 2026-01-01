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

import org.laokou.auth.model.AuthA;
import org.laokou.auth.model.AuthParamValidator;
import org.laokou.auth.model.CaptchaV;
import org.laokou.auth.model.UserV;
import org.laokou.common.i18n.util.ParamValidator;
import org.springframework.stereotype.Component;

import java.io.Serial;

/**
 * @author laokou
 */
@Component("mailAuthParamValidator")
public class MailAuthParamValidator implements AuthParamValidator {

	@Serial
	private static final long serialVersionUID = 3319752558160144614L;

	@Override
	public void validateAuth(AuthA authA) {
		UserV userV = authA.getUserV();
		CaptchaV captchaV = authA.getCaptchaV();
		ParamValidator.validate(authA.getValidateName(),
				// 校验租户编码
				OAuth2ParamValidator.validateTenantCode(userV.tenantCode()),
				// 校验验证码
				OAuth2ParamValidator.validateCaptcha(captchaV.captcha()),
				// 校验邮箱
				OAuth2ParamValidator.validateMail(captchaV.uuid()));
	}

}

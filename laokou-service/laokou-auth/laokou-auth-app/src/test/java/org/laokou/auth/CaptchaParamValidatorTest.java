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

package org.laokou.auth;

import org.junit.jupiter.api.Test;
import org.laokou.auth.factory.DomainFactory;
import org.laokou.auth.model.CaptchaE;
import org.laokou.auth.service.extensionpoint.CaptchaParamValidatorExtPt;
import org.laokou.auth.service.extensionpoint.extension.MailCaptchaParamValidator;
import org.laokou.auth.service.extensionpoint.extension.MobileCaptchaParamValidator;

/**
 * 验证码参数校验器测试.
 *
 * @author laokou
 */
class CaptchaParamValidatorTest {

	@Test
	void testMailCaptchaParamValidator() {
		CaptchaParamValidatorExtPt captchaParamValidator = new MailCaptchaParamValidator();
		CaptchaE captcha = DomainFactory.getCaptcha();
		// 校验邮箱验证码
		captcha.setUuid("2413176044@qq.com");
		captcha.setTenantCode("laokou");
		captchaParamValidator.validate(captcha);
	}

	@Test
	void testMobileCaptchaParamValidator() {
		CaptchaParamValidatorExtPt captchaParamValidator = new MobileCaptchaParamValidator();
		CaptchaE captcha = DomainFactory.getCaptcha();
		// 校验手机号验证码
		captcha.setUuid("18888888888");
		captcha.setTenantCode("laokou");
		captchaParamValidator.validate(captcha);
	}

}

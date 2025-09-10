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

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.auth.factory.DomainFactory;
import org.laokou.auth.model.CaptchaE;
import org.laokou.auth.model.CaptchaParamValidator;
import org.laokou.auth.model.CaptchaValidator;
import org.laokou.auth.model.PasswordValidator;
import org.laokou.auth.model.SendCaptchaTypeEnum;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

/**
 * 验证码参数校验器测试.
 *
 * @author laokou
 */
@SpringBootTest
class CaptchaParamValidatorTest {

	private final CaptchaParamValidator mailCaptchaParamValidator;

	private final CaptchaParamValidator mobileCaptchaParamValidator;

	@MockitoBean
	private PasswordValidator passwordValidator;

	@MockitoBean
	private CaptchaValidator captchaValidator;

	CaptchaParamValidatorTest(@Qualifier("mailCaptchaParamValidator") CaptchaParamValidator mailCaptchaParamValidator,
			@Qualifier("mobileCaptchaParamValidator") CaptchaParamValidator mobileCaptchaParamValidator) {
		this.mailCaptchaParamValidator = mailCaptchaParamValidator;
		this.mobileCaptchaParamValidator = mobileCaptchaParamValidator;
	}

	@Test
	void test_mailCaptchaParamValidator() {
		CaptchaE captcha = getCaptcha("2413176044@qq.com", SendCaptchaTypeEnum.SEND_MAIL_CAPTCHA.getCode());
		// 校验邮箱验证码
		Assertions.assertThatNoException().isThrownBy(() -> mailCaptchaParamValidator.validateCaptcha(captcha));
	}

	@Test
	void test_mobileCaptchaParamValidator() {
		CaptchaE captcha = getCaptcha("18888888888", SendCaptchaTypeEnum.SEND_MOBILE_CAPTCHA.getCode());
		// 校验手机号验证码
		Assertions.assertThatNoException().isThrownBy(() -> mobileCaptchaParamValidator.validateCaptcha(captcha));
	}

	private CaptchaE getCaptcha(String uuid, String tag) {
		CaptchaE captchaE = DomainFactory.getCaptcha();
		captchaE.setId(1L);
		captchaE.setUuid(uuid);
		captchaE.setSendCaptchaTypeEnum(SendCaptchaTypeEnum.getByCode(tag));
		captchaE.setTenantCode("laokou");
		return captchaE;
	}

}

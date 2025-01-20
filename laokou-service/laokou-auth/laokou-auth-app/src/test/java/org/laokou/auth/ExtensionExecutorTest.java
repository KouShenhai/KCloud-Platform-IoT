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

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.auth.factory.DomainFactory;
import org.laokou.auth.model.AuthA;
import org.laokou.auth.model.CaptchaE;
import org.laokou.auth.service.extensionpoint.AuthParamValidatorExtPt;
import org.laokou.auth.service.extensionpoint.CaptchaParamValidatorExtPt;
import org.laokou.auth.service.extensionpoint.extension.*;
import org.laokou.common.extension.BizScenario;
import org.laokou.common.extension.ExtensionExecutor;
import org.laokou.common.extension.ExtensionPointI;
import org.laokou.common.extension.ExtensionRepository;
import org.laokou.common.extension.register.ExtensionRegister;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestConstructor;

import static org.laokou.auth.dto.clientobject.CaptchaCO.USE_CASE_CAPTCHA;
import static org.laokou.auth.model.AuthA.USE_CASE_AUTH;
import static org.laokou.auth.model.Constant.MAIL_TAG;
import static org.laokou.auth.model.Constant.MOBILE_TAG;
import static org.laokou.common.i18n.common.constant.Constant.SCENARIO;

/**
 * 测试扩展点执行器.
 *
 * @author laokou
 */
@SpringBootTest
@RequiredArgsConstructor
@ContextConfiguration(classes = { ExtensionExecutor.class, ExtensionRepository.class, ExtensionRegister.class })
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class ExtensionExecutorTest {

	private final ExtensionExecutor extensionExecutor;

	private final ExtensionRegister extensionRegister;

	@Test
	void testUsernamePasswordAuthParamValidateExecutor() {
		// 校验参数
		validate();

		AuthA auth = DomainFactory.getUsernamePasswordAuth(1L, "admin", "123", "laokou", "1", "1234");
		Assertions.assertNotNull(auth);

		// 注册【用户名密码登录】校验器
		doRegistration(new UsernamePasswordAuthParamValidator());

		// 执行参数校验【用户名密码登录】
		execute(auth);
	}

	@Test
	void testMailAuthParamValidateExecutor() {
		// 校验参数
		validate();

		AuthA auth = DomainFactory.getMailAuth(1L, "2413176044@qq.com", "123456", "laokou");
		Assertions.assertNotNull(auth);

		// 注册【邮箱登录】校验器
		doRegistration(new MailAuthParamValidator());

		// 执行参数校验【邮箱登录】
		execute(auth);
	}

	@Test
	void testMobileAuthParamValidateExecutor() {
		// 校验参数
		validate();

		AuthA auth = DomainFactory.getMobileAuth(1L, "18888888888", "123456", "laokou");
		Assertions.assertNotNull(auth);

		// 注册【手机号登录】校验器
		doRegistration(new MobileAuthParamValidator());

		// 执行参数校验【手机号登录】
		execute(auth);
	}

	@Test
	void testAuthorizationCodeAuthParamValidateExecutor() {
		// 校验参数
		validate();

		AuthA auth = DomainFactory.getAuthorizationCodeAuth(1L, "admin", "123", "laokou");
		Assertions.assertNotNull(auth);

		// 注册【授权码登录】校验器
		doRegistration(new AuthorizationCodeAuthParamValidator());

		// 执行参数校验【授权码登录】
		execute(auth);
	}

	@Test
	void testMailCaptchaParamValidateExecutor() {
		// 校验参数
		validate();

		CaptchaE captcha = DomainFactory.getCaptcha();
		Assertions.assertNotNull(captcha);

		// 注册【邮箱验证码】校验器
		doRegistration(new MailCaptchaParamValidator());

		// 执行参数校验【邮箱验证码】
		captcha.setTag(MAIL_TAG);
		captcha.setUuid("2413176044@qq.com");
		captcha.setTenantCode("laokou");
		execute(captcha);
	}

	@Test
	void testCaptchaParamValidateExecutor() {
		// 校验参数
		validate();

		CaptchaE captcha = DomainFactory.getCaptcha();
		Assertions.assertNotNull(captcha);

		// 注册【手机号验证码】校验器
		doRegistration(new MobileCaptchaParamValidator());

		// 执行参数校验【手机号验证码】
		captcha.setTag(MOBILE_TAG);
		captcha.setUuid("18888888888");
		captcha.setTenantCode("laokou");
		execute(captcha);
	}

	private void doRegistration(ExtensionPointI extensionPointI) {
		Assertions.assertNotNull(extensionPointI);
		extensionRegister.doRegistration(extensionPointI);
	}

	private void validate() {
		Assertions.assertNotNull(extensionExecutor);
		Assertions.assertNotNull(extensionRegister);
	}

	private void execute(AuthA auth) {
		extensionExecutor.executeVoid(AuthParamValidatorExtPt.class,
				BizScenario.valueOf(auth.getGrantType().getCode(), USE_CASE_AUTH, SCENARIO),
				extension -> extension.validate(auth));
	}

	private void execute(CaptchaE captcha) {
		extensionExecutor.executeVoid(CaptchaParamValidatorExtPt.class,
				BizScenario.valueOf(captcha.getTag(), USE_CASE_CAPTCHA, SCENARIO),
				extension -> extension.validate(captcha));
	}

}

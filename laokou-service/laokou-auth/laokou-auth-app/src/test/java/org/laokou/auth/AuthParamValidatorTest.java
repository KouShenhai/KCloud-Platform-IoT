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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.auth.factory.DomainFactory;
import org.laokou.auth.model.AuthA;
import org.laokou.auth.service.extensionpoint.AuthParamValidatorExtPt;
import org.laokou.auth.service.extensionpoint.extension.AuthorizationCodeAuthParamValidator;
import org.laokou.auth.service.extensionpoint.extension.MailAuthParamValidator;
import org.laokou.auth.service.extensionpoint.extension.MobileAuthParamValidator;
import org.laokou.auth.service.extensionpoint.extension.UsernamePasswordAuthParamValidator;

/**
 * 认证参数校验器测试.
 *
 * @author laokou
 */
class AuthParamValidatorTest {

	@Test
	void testUsernamePasswordAuthParamValidator() {
		AuthParamValidatorExtPt authParamValidator = new UsernamePasswordAuthParamValidator();
		AuthA auth = DomainFactory.getUsernamePasswordAuth(1L, "admin", "123", "laokou", "1", "1234");
		// 校验用户名密码登录
		authParamValidator.validate(auth);
	}

	@Test
	void testAuthorizationCodeAuthParamValidator() {
		AuthParamValidatorExtPt authParamValidator = new AuthorizationCodeAuthParamValidator();
		AuthA auth = DomainFactory.getAuthorizationCodeAuth(1L, "admin", "123", "laokou");
		// 校验授权码登录
		authParamValidator.validate(auth);
	}

	@Test
	void testMailAuthParamValidator() {
		AuthParamValidatorExtPt authParamValidator = new MailAuthParamValidator();
		AuthA auth = DomainFactory.getMailAuth(1L, "2413176044@qq.com", "123456", "laokou");
		// 校验邮箱登录
		authParamValidator.validate(auth);
	}

	@Test
	void testMobileAuthParamValidator() {
		AuthParamValidatorExtPt authParamValidator = new MobileAuthParamValidator();
		AuthA auth = DomainFactory.getMobileAuth(1L, "18888888888", "123456", "laokou");
		Assertions.assertNotNull(auth);
		// 校验手机号登录
		authParamValidator.validate(auth);
	}

}

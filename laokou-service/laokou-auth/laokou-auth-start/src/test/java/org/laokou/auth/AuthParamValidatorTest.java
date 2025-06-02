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
import org.laokou.auth.model.AuthParamValidator;
import org.laokou.auth.model.CaptchaV;
import org.laokou.auth.model.GrantTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import static org.laokou.common.i18n.common.constant.StringConstants.EMPTY;

/**
 * 认证参数校验器测试.
 *
 * @author laokou
 */
@SpringBootTest
class AuthParamValidatorTest {

	@Autowired
	@Qualifier("authorizationCodeAuthParamValidator")
	private AuthParamValidator authorizationCodeAuthParamValidator;

	@Autowired
	@Qualifier("mailAuthParamValidator")
	private AuthParamValidator mailAuthParamValidator;

	@Autowired
	@Qualifier("mobileAuthParamValidator")
	private AuthParamValidator mobileAuthParamValidator;

	@Autowired
	@Qualifier("testAuthParamValidator")
	private AuthParamValidator testAuthParamValidator;

	@Autowired
	@Qualifier("usernamePasswordAuthParamValidator")
	private AuthParamValidator usernamePasswordAuthParamValidator;

	@Test
	void testTestAuthParamValidator() {
		AuthA auth = getAuth("admin", "123", GrantTypeEnum.TEST, EMPTY, EMPTY);
		// 校验测试登录
		Assertions.assertDoesNotThrow(() -> testAuthParamValidator.validateAuth(auth));
	}

	@Test
	void testUsernamePasswordAuthParamValidator() {
		AuthA auth = getAuth("admin", "123", GrantTypeEnum.USERNAME_PASSWORD, "1", "1234");
		// 校验用户名密码登录
		Assertions.assertDoesNotThrow(() -> usernamePasswordAuthParamValidator.validateAuth(auth));
	}

	@Test
	void testAuthorizationCodeAuthParamValidator() {
		AuthA auth = getAuth("admin", "123", GrantTypeEnum.AUTHORIZATION_CODE, EMPTY, EMPTY);
		// 校验授权码登录
		Assertions.assertDoesNotThrow(() -> authorizationCodeAuthParamValidator.validateAuth(auth));
	}

	@Test
	void testMailAuthParamValidator() {
		AuthA auth = getAuth(EMPTY, EMPTY, GrantTypeEnum.MAIL, "2413176044@qq.com", "123456");
		// 校验邮箱登录
		Assertions.assertDoesNotThrow(() -> mailAuthParamValidator.validateAuth(auth));
	}

	@Test
	void testMobileAuthParamValidator() {
		AuthA auth = getAuth(EMPTY, EMPTY, GrantTypeEnum.MOBILE, "18888888888", "123456");
		Assertions.assertNotNull(auth);
		// 校验手机号登录
		Assertions.assertDoesNotThrow(() -> mobileAuthParamValidator.validateAuth(auth));
	}

	private AuthA getAuth(String username, String password, GrantTypeEnum grantTypeEnum, String uuid, String captcha) {
		AuthA authA = DomainFactory.getAuth();
		authA.setId(1L);
		authA.setUsername(username);
		authA.setPassword(password);
		authA.setTenantCode("laokou");
		authA.setGrantTypeEnum(grantTypeEnum);
		authA.setCaptcha(new CaptchaV(uuid, captcha));
		return authA;
	}

}

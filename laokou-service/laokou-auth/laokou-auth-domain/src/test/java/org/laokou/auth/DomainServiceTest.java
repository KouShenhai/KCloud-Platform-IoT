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

package org.laokou.auth;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laokou.auth.ability.DomainService;
import org.laokou.auth.ability.validator.PasswordValidator;
import org.laokou.auth.factory.DomainFactory;
import org.laokou.auth.gateway.*;
import org.laokou.auth.model.*;

import static org.laokou.auth.PasswordValidatorTest.getEncodePassword;

/**
 * 领域服务测试.
 *
 * @author laokou
 */
class DomainServiceTest {

	private DomainService domainService;

	private InfoV info;

	@BeforeEach
	void testDomainService() {
		UserGateway userGateway = new UserGatewayImplTest();
		MenuGateway menuGateway = new MenuGatewayImplTest();
		DeptGateway deptGateway = new DeptGatewayImplTest();
		TenantGateway tenantGateway = new TenantGatewayImplTest();
		SourceGateway sourceGateway = new SourceGatewayImplTest();
		CaptchaGateway captchaGateway = new CaptchaGatewayImplTest();
		LoginLogGateway loginLogGateway = new LoginLogGatewayImplTest();
		PasswordValidator passwordValidator = new PasswordValidatorTest();
		Assertions.assertNotNull(userGateway);
		Assertions.assertNotNull(menuGateway);
		Assertions.assertNotNull(deptGateway);
		Assertions.assertNotNull(tenantGateway);
		Assertions.assertNotNull(sourceGateway);
		Assertions.assertNotNull(captchaGateway);
		Assertions.assertNotNull(loginLogGateway);
		Assertions.assertNotNull(passwordValidator);
		domainService = new DomainService(userGateway, menuGateway, deptGateway, tenantGateway, sourceGateway,
				captchaGateway, loginLogGateway, passwordValidator);
		info = new InfoV("Windows", "127.0.0.1", "中国 广东 深圳", "Chrome");
		Assertions.assertNotNull(domainService);
		Assertions.assertNotNull(info);
	}

	@Test
	void testUsernamePasswordAuth() {
		Assertions.assertNotNull(domainService);
		Assertions.assertNotNull(info);

		AuthA auth = DomainFactory.getUsernamePasswordAuth(1L, "admin", "123", "laokou", "1", "1234");

		// 创建用户【用户名密码】
		auth.createUserByUsernamePassword();

		Assertions.assertNotNull(auth);
		Assertions.assertNotNull(auth.getUser());

		// 构建密码
		auth.getUser().setPassword(getEncodePassword(auth.getPassword()));
		Assertions.assertNotNull(auth.getUser().getPassword());

		// 用户名密码登录
		domainService.auth(auth, info);
	}

	@Test
	void testMailAuth() {
		Assertions.assertNotNull(domainService);
		Assertions.assertNotNull(info);

		AuthA auth = DomainFactory.getMailAuth(1L, "2413176044@qq.com", "123456", "laokou");

		// 创建用户【邮箱】
		auth.createUserByMail();

		Assertions.assertNotNull(auth);
		Assertions.assertNotNull(auth.getUser());

		// 邮箱登录
		domainService.auth(auth, info);
	}

	@Test
	void testMobileAuth() {
		Assertions.assertNotNull(domainService);
		Assertions.assertNotNull(info);

		AuthA auth = DomainFactory.getMobileAuth(1L, "18888888888", "123456", "laokou");

		// 创建用户【手机号】
		auth.createUserByMobile();

		Assertions.assertNotNull(auth);
		Assertions.assertNotNull(auth.getUser());

		// 手机号登录
		domainService.auth(auth, info);
	}

	@Test
	void testAuthorizationCodeAuth() {
		Assertions.assertNotNull(domainService);
		Assertions.assertNotNull(info);

		AuthA auth = DomainFactory.getAuthorizationCodeAuth(1L, "admin", "123", "laokou");

		// 创建用户【授权码】
		auth.createUserByAuthorizationCode();

		Assertions.assertNotNull(auth);
		Assertions.assertNotNull(auth.getUser());

		// 授权码登录
		domainService.auth(auth, info);
	}

	@Test
	void testCreateCaptcha() {
		Assertions.assertNotNull(domainService);

		CaptchaE captcha = DomainFactory.getCaptcha();
		Assertions.assertNotNull(captcha);

		AuthA auth = DomainFactory.getAuth(1L);
		Assertions.assertNotNull(auth);

		// 创建验证码
		domainService.createCaptcha(1L, auth, captcha);
	}

	@Test
	void testCreateLoginLog() {
		Assertions.assertNotNull(domainService);

		LoginLogE loginLog = DomainFactory.getLoginLog();
		Assertions.assertNotNull(loginLog);

		// 创建登录日志
		domainService.createLoginLog(loginLog);
	}

}

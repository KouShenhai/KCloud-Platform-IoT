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
import org.laokou.auth.ability.validator.PasswordValidator;
import org.laokou.auth.factory.DomainFactory;
import org.laokou.auth.gateway.*;
import org.laokou.auth.model.AuthA;
import org.laokou.auth.model.CaptchaE;
import org.laokou.auth.model.InfoV;
import org.laokou.auth.model.UserE;
import org.laokou.common.crypto.utils.AESUtil;
import org.laokou.common.i18n.common.exception.SystemException;

import static org.laokou.common.i18n.common.exception.SystemException.OAuth2.USERNAME_PASSWORD_ERROR;

/**
 * 认证聚合根测试.
 *
 * @author laokou
 */
class AuthATest {

	private AuthA auth;

	private InfoV info;

	@BeforeEach
	void testAuth() {
		auth = DomainFactory.getAuth(1L);
		info = new InfoV("Windows", "127.0.0.1", "中国 广东 深圳", "Chrome");
		Assertions.assertNotNull(auth);
	}

	@Test
	void testCreateUserByUsernamePassword() {
		AuthA auth = DomainFactory.getUsernamePasswordAuth(1L, "admin", "123", "laokou", "1", "1234");
		Assertions.assertNotNull(auth);

		// 创建用户【用户名密码】
		auth.createUserByUsernamePassword();
		Assertions.assertNotNull(auth.getUser());
		String username = auth.getUser().getUsername();
		Assertions.assertNotNull(username);
		Assertions.assertEquals(AESUtil.decrypt(username), auth.getUsername());
	}

	@Test
	void testCreateUserByMobile() {
		AuthA auth = DomainFactory.getMobileAuth(1L, "18888888888", "123456", "laokou");
		Assertions.assertNotNull(auth);

		// 创建用户【手机号】
		auth.createUserByMobile();
		Assertions.assertNotNull(auth.getUser());
		String mobile = auth.getUser().getMobile();
		Assertions.assertNotNull(mobile);
		Assertions.assertEquals(AESUtil.decrypt(mobile), auth.getCaptcha().uuid());
	}

	@Test
	void testCreateUserByMail() {
		AuthA auth = DomainFactory.getMailAuth(1L, "2413176044@qq.com", "123456", "laokou");
		Assertions.assertNotNull(auth);

		// 创建用户【邮箱】
		auth.createUserByMail();
		Assertions.assertNotNull(auth.getUser());
		String mail = auth.getUser().getMail();
		Assertions.assertNotNull(mail);
		Assertions.assertEquals(AESUtil.decrypt(mail), auth.getCaptcha().uuid());
	}

	@Test
	void testCreateUserByAuthorizationCode() {
		AuthA auth = DomainFactory.getAuthorizationCodeAuth(1L, "admin", "123", "laokou");
		Assertions.assertNotNull(auth);

		// 创建用户【授权码】
		auth.createUserByAuthorizationCode();
		Assertions.assertNotNull(auth.getUser());
		String username = auth.getUser().getUsername();
		Assertions.assertNotNull(username);
		Assertions.assertEquals(AESUtil.decrypt(username), auth.getUsername());
	}

	@Test
	void testCreateCaptcha() {
		Assertions.assertNotNull(auth);

		CaptchaE captcha = DomainFactory.getCaptcha();

		// 创建验证码
		auth.getCaptcha(captcha);
		auth.createCaptcha(1L);
		Assertions.assertNotNull(auth.getCaptchaE());
		Assertions.assertFalse(auth.getEVENTS().isEmpty());

		// 释放事件
		auth.releaseEvents();
		Assertions.assertTrue(auth.getEVENTS().isEmpty());
	}

	@Test
	void testCheckExtInfo() {
		Assertions.assertNotNull(auth);

		auth.getExtInfo(info);
		Assertions.assertNotNull(auth.getInfo());
	}

	@Test
	void testCheckTenantId() {
		Assertions.assertNotNull(auth);

		TenantGateway tenantGateway = new TenantGatewayImplTest();
		Assertions.assertNotNull(tenantGateway);

		auth.getTenantId(tenantGateway.getId("laokou"));

		// 校验租户ID
		auth.checkTenantId();
	}

	@Test
	void testCheckCaptcha() {
		Assertions.assertNotNull(auth);

		CaptchaGateway captchaGateway = new CaptchaGatewayImplTest();
		Assertions.assertNotNull(captchaGateway);

		// 校验验证码
		auth.checkCaptcha(captchaGateway::validate);
	}

	@Test
	void testCheckSourcePrefix() {
		Assertions.assertNotNull(auth);

		SourceGateway sourceGateway = new SourceGatewayImplTest();
		Assertions.assertNotNull(sourceGateway);

		auth.getSourcePrefix(sourceGateway.getPrefix("laokou"));

		// 校验数据源前缀
		auth.checkSourcePrefix();
	}

	@Test
	void testCheckUsername() {
		Assertions.assertNotNull(auth);

		UserE user = DomainFactory.getUser();
		Assertions.assertNotNull(user);

		UserGateway userGateway = new UserGatewayImplTest();
		Assertions.assertNotNull(userGateway);

		auth.getUserInfo(userGateway.getProfile(user, "laokou"));

		// 校验用户名
		auth.checkUsername();
	}

	@Test
	void testCheckPassword() {
		AuthA auth = DomainFactory.getUsernamePasswordAuth(1L, "admin", "123", "laokou", "1", "1234");
		Assertions.assertNotNull(auth);

		PasswordValidator passwordValidator = new PasswordValidatorTest();
		Assertions.assertNotNull(passwordValidator);

		// 创建用户【用户名密码】
		auth.createUserByUsernamePassword();

		// 构建密码
		auth.getUser().setPassword(PasswordValidatorTest.getEncodePassword(auth.getPassword()));
		Assertions.assertNotNull(auth.getUser().getPassword());

		// 校验密码
		auth.checkPassword(passwordValidator);
	}

	@Test
	void testCheckUserStatus() {
		Assertions.assertNotNull(auth);

		// 创建用户【用户名密码】
		auth.createUserByUsernamePassword();
		Assertions.assertNotNull(auth.getUser());

		// 校验用户状态
		auth.checkUserStatus();
	}

	@Test
	void testCheckMenuPermissions() {
		Assertions.assertNotNull(auth);

		MenuGateway menuGateway = new MenuGatewayImplTest();
		Assertions.assertNotNull(menuGateway);

		// 创建用户【用户名密码】
		auth.createUserByUsernamePassword();
		Assertions.assertNotNull(auth.getUser());
		auth.getMenuPermissions(menuGateway.getPermissions(auth.getUser()));

		// 校验菜单权限集合
		auth.checkMenuPermissions();
	}

	@Test
	void testCheckDeptPaths() {
		Assertions.assertNotNull(auth);

		DeptGateway deptGateway = new DeptGatewayImplTest();
		Assertions.assertNotNull(deptGateway);

		// 创建用户【用户名密码】
		auth.createUserByUsernamePassword();
		Assertions.assertNotNull(auth.getUser());
		auth.getDeptPaths(deptGateway.getPaths(auth.getUser()));

		// 校验部门路径集合
		auth.checkDeptPaths();
	}

	@Test
	void testRecordLog() {
		Assertions.assertNotNull(auth);
		Assertions.assertNotNull(info);

		auth.getExtInfo(info);
		Assertions.assertNotNull(auth.getInfo());

		// 记录日志【登录成功】
		auth.recordLog(1L, null);
		Assertions.assertFalse(auth.getEVENTS().isEmpty());

		// 释放事件
		auth.releaseEvents();
		Assertions.assertTrue(auth.getEVENTS().isEmpty());

		// 记录日志【登录失败】
		auth.recordLog(1L, new SystemException(USERNAME_PASSWORD_ERROR));
		Assertions.assertFalse(auth.getEVENTS().isEmpty());

		// 释放事件
		auth.releaseEvents();
		Assertions.assertTrue(auth.getEVENTS().isEmpty());
	}

}

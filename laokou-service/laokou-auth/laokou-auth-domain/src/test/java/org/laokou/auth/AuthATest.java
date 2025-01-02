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
		this.auth = DomainFactory.getAuth(1L, "laokou");
		this.info = new InfoV("Windows", "127.0.0.1", "中国 广东 深圳", "Chrome");
		Assertions.assertNotNull(this.auth);
	}

	@Test
	void testCreateUserByUsernamePassword() {
		AuthA authA = DomainFactory.getUsernamePasswordAuth(1L, "admin", "123", "laokou", "1", "1234");
		Assertions.assertNotNull(authA);

		// 创建用户【用户名密码】
		authA.createUserByUsernamePassword();
		Assertions.assertNotNull(authA.getUser());
		String username = authA.getUser().getUsername();
		Assertions.assertNotNull(username);
		Assertions.assertEquals(AESUtil.decrypt(username), authA.getUsername());
	}

	@Test
	void testCreateUserByMobile() {
		AuthA authA = DomainFactory.getMobileAuth(1L, "18888888888", "123456", "laokou");
		Assertions.assertNotNull(authA);

		// 创建用户【手机号】
		authA.createUserByMobile();
		Assertions.assertNotNull(authA.getUser());
		String mobile = authA.getUser().getMobile();
		Assertions.assertNotNull(mobile);
		Assertions.assertEquals(AESUtil.decrypt(mobile), authA.getCaptcha().uuid());
	}

	@Test
	void testCreateUserByMail() {
		AuthA authA = DomainFactory.getMailAuth(1L, "2413176044@qq.com", "123456", "laokou");
		Assertions.assertNotNull(authA);

		// 创建用户【邮箱】
		authA.createUserByMail();
		Assertions.assertNotNull(authA.getUser());
		String mail = authA.getUser().getMail();
		Assertions.assertNotNull(mail);
		Assertions.assertEquals(AESUtil.decrypt(mail), authA.getCaptcha().uuid());
	}

	@Test
	void testCreateUserByAuthorizationCode() {
		AuthA authA = DomainFactory.getAuthorizationCodeAuth(1L, "admin", "123", "laokou");
		Assertions.assertNotNull(authA);

		// 创建用户【授权码】
		authA.createUserByAuthorizationCode();
		Assertions.assertNotNull(authA.getUser());
		String username = authA.getUser().getUsername();
		Assertions.assertNotNull(username);
		Assertions.assertEquals(AESUtil.decrypt(username), authA.getUsername());
	}

	@Test
	void testCreateCaptcha() {
		Assertions.assertNotNull(this.auth);

		CaptchaE captcha = DomainFactory.getCaptcha();

		// 创建验证码
		this.auth.getCaptcha(captcha);
		this.auth.createCaptcha(1L);
		Assertions.assertNotNull(this.auth.getCaptchaE());
		Assertions.assertFalse(this.auth.getEVENTS().isEmpty());

		// 释放事件
		this.auth.releaseEvents();
		Assertions.assertTrue(this.auth.getEVENTS().isEmpty());
	}

	@Test
	void testCheckExtInfo() {
		Assertions.assertNotNull(this.auth);

		this.auth.getExtInfo(this.info);
		Assertions.assertNotNull(this.auth.getInfo());
	}

	@Test
	void testCheckTenantId() {
		Assertions.assertNotNull(this.auth);

		TenantGateway tenantGateway = new TenantGatewayImplTest();
		Assertions.assertNotNull(tenantGateway);

		this.auth.getTenantId(tenantGateway.getId("laokou"));

		// 校验租户ID
		this.auth.checkTenantId();
	}

	@Test
	void testCheckCaptcha() {
		Assertions.assertNotNull(this.auth);

		CaptchaGateway captchaGateway = new CaptchaGatewayImplTest();
		Assertions.assertNotNull(captchaGateway);

		// 校验验证码
		this.auth.checkCaptcha(captchaGateway::validate);
	}

	@Test
	void testCheckSourcePrefix() {
		Assertions.assertNotNull(this.auth);

		SourceGateway sourceGateway = new SourceGatewayImplTest();
		Assertions.assertNotNull(sourceGateway);

		this.auth.getSourcePrefix(sourceGateway.getPrefix("laokou"));

		// 校验数据源前缀
		this.auth.checkSourcePrefix();
	}

	@Test
	void testCheckUsername() {
		Assertions.assertNotNull(this.auth);

		UserE user = DomainFactory.getUser();
		Assertions.assertNotNull(user);

		UserGateway userGateway = new UserGatewayImplTest();
		Assertions.assertNotNull(userGateway);

		this.auth.getUserInfo(userGateway.getProfile(user, "laokou"));

		// 校验用户名
		this.auth.checkUsername();
	}

	@Test
	void testCheckPassword() {
		AuthA authA = DomainFactory.getUsernamePasswordAuth(1L, "admin", "123", "laokou", "1", "1234");
		Assertions.assertNotNull(authA);

		PasswordValidator passwordValidator = new PasswordValidatorTest();
		Assertions.assertNotNull(passwordValidator);

		// 创建用户【用户名密码】
		authA.createUserByUsernamePassword();

		// 构建密码
		authA.getUser().setPassword(PasswordValidatorTest.getEncodePassword(authA.getPassword()));
		Assertions.assertNotNull(authA.getUser().getPassword());

		// 校验密码
		authA.checkPassword(passwordValidator);
	}

	@Test
	void testCheckUserStatus() {
		Assertions.assertNotNull(this.auth);

		// 创建用户【用户名密码】
		this.auth.createUserByUsernamePassword();
		Assertions.assertNotNull(this.auth.getUser());

		// 校验用户状态
		this.auth.checkUserStatus();
	}

	@Test
	void testCheckMenuPermissions() {
		Assertions.assertNotNull(this.auth);

		MenuGateway menuGateway = new MenuGatewayImplTest();
		Assertions.assertNotNull(menuGateway);

		// 创建用户【用户名密码】
		this.auth.createUserByUsernamePassword();
		Assertions.assertNotNull(this.auth.getUser());
		this.auth.getMenuPermissions(menuGateway.getPermissions(this.auth.getUser()));

		// 校验菜单权限集合
		this.auth.checkMenuPermissions();
	}

	@Test
	void testCheckDeptPaths() {
		Assertions.assertNotNull(this.auth);

		DeptGateway deptGateway = new DeptGatewayImplTest();
		Assertions.assertNotNull(deptGateway);

		// 创建用户【用户名密码】
		this.auth.createUserByUsernamePassword();
		Assertions.assertNotNull(this.auth.getUser());
		this.auth.getDeptPaths(deptGateway.getPaths(this.auth.getUser()));

		// 校验部门路径集合
		this.auth.checkDeptPaths();
	}

	@Test
	void testRecordLog() {
		Assertions.assertNotNull(this.auth);
		Assertions.assertNotNull(this.info);

		this.auth.getExtInfo(this.info);
		Assertions.assertNotNull(this.auth.getInfo());

		// 记录日志【登录成功】
		this.auth.recordLog(1L, null);
		Assertions.assertFalse(this.auth.getEVENTS().isEmpty());

		// 释放事件
		this.auth.releaseEvents();
		Assertions.assertTrue(this.auth.getEVENTS().isEmpty());

		// 记录日志【登录失败】
		this.auth.recordLog(1L, new SystemException(USERNAME_PASSWORD_ERROR));
		Assertions.assertFalse(this.auth.getEVENTS().isEmpty());

		// 释放事件
		this.auth.releaseEvents();
		Assertions.assertTrue(this.auth.getEVENTS().isEmpty());
	}

}

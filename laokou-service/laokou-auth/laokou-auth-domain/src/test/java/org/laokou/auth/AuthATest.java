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
import org.laokou.auth.ability.validator.PasswordValidator;
import org.laokou.auth.factory.DomainFactory;
import org.laokou.auth.gateway.*;
import org.laokou.auth.model.AuthA;
import org.laokou.auth.model.InfoV;
import org.laokou.auth.model.UserE;
import org.laokou.common.i18n.common.exception.BizException;
import org.laokou.common.i18n.utils.RedisKeyUtil;
import org.mockito.Mockito;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.laokou.common.i18n.common.exception.BizException.OAuth2.USERNAME_PASSWORD_ERROR;
import static org.mockito.Mockito.*;

/**
 * 认证聚合根测试.
 *
 * @author laokou
 */
class AuthATest {

	@Test
	void testCreateUserByUsernamePassword() throws Exception {
		AuthA authA = DomainFactory.getUsernamePasswordAuth(1L, "admin", "123", "laokou", "1", "1234");
		// 创建用户【用户名密码】
		authA.createUserByUsernamePassword();
		Assertions.assertNotNull(authA.getUser());
	}

	@Test
	void testCreateUserByMobile() throws Exception {
		AuthA authA = DomainFactory.getMobileAuth(1L, "18888888888", "123456", "laokou");
		// 创建用户【手机号】
		authA.createUserByMobile();
		Assertions.assertNotNull(authA.getUser());
	}

	@Test
	void testCreateUserByMail() throws Exception {
		AuthA authA = DomainFactory.getMailAuth(1L, "2413176044@qq.com", "123456", "laokou");
		// 创建用户【邮箱】
		authA.createUserByMail();
		Assertions.assertNotNull(authA.getUser());
	}

	@Test
	void testCreateUserByAuthorizationCode() throws Exception {
		AuthA authA = DomainFactory.getAuthorizationCodeAuth(1L, "admin", "123", "laokou");
		// 创建用户【授权码】
		authA.createUserByAuthorizationCode();
		Assertions.assertNotNull(authA.getUser());
	}

	@Test
	void testCreateCaptcha() {
		AuthA auth = DomainFactory.getAuth(1L, "laokou");
		// 创建验证码
		auth.getCaptcha(DomainFactory.getCaptcha());
		auth.createCaptcha(1L);
		Assertions.assertNotNull(auth.getCaptchaE());
		Assertions.assertFalse(auth.getEVENTS().isEmpty());
		// 释放事件
		auth.releaseEvents();
		Assertions.assertTrue(auth.getEVENTS().isEmpty());
	}

	@Test
	void testCheckTenantId() {
		TenantGateway tenantGateway = Mockito.mock(TenantGateway.class);
		// 构造租户
		when(tenantGateway.getId("laokou")).thenReturn(0L);
		// 校验租户ID
		AuthA auth = DomainFactory.getAuth(1L, "laokou");
		// 获取租户ID
		auth.getTenantId(tenantGateway.getId(auth.getTenantCode()));
		auth.checkTenantId();
		// 校验调用次数
		verify(tenantGateway, times(1)).getId("laokou");
	}

	@Test
	void testCheckCaptcha() {
		CaptchaGateway captchaGateway = mock(CaptchaGateway.class);
		// 构造验证码校验
		doReturn(true).when(captchaGateway).validate(RedisKeyUtil.getUsernamePasswordAuthCaptchaKey("1"), "1234");
		doReturn(true).when(captchaGateway).validate(RedisKeyUtil.getMailAuthCaptchaKey("2413176044@qq.com"), "123456");
		doReturn(true).when(captchaGateway).validate(RedisKeyUtil.getMobileAuthCaptchaKey("18888888888"), "123456");
		// 校验验证码【用户名密码登录】
		AuthA auth = DomainFactory.getUsernamePasswordAuth(1L, "admin", "123", "laokou", "1", "1234");
		auth.checkCaptcha(captchaGateway::validate);
		// 校验验证码【邮箱登录】
		auth = DomainFactory.getMailAuth(1L, "2413176044@qq.com", "123456", "laokou");
		auth.checkCaptcha(captchaGateway::validate);
		// 校验验证码【手机号登录】
		auth = DomainFactory.getMobileAuth(1L, "18888888888", "123456", "laokou");
		auth.checkCaptcha(captchaGateway::validate);
		// 校验调用次数
		verify(captchaGateway, times(1)).validate(RedisKeyUtil.getUsernamePasswordAuthCaptchaKey("1"), "1234");
		verify(captchaGateway, times(1)).validate(RedisKeyUtil.getMailAuthCaptchaKey("2413176044@qq.com"), "123456");
		verify(captchaGateway, times(1)).validate(RedisKeyUtil.getMobileAuthCaptchaKey("18888888888"), "123456");
	}

	@Test
	void testCheckSourcePrefix() {
		SourceGateway sourceGateway = mock(SourceGateway.class);
		// 构造数据源
		when(sourceGateway.getPrefix("laokou")).thenReturn("master");
		// 校验数据源前缀
		AuthA auth = DomainFactory.getAuth(1L, "laokou");
		auth.getSourcePrefix(auth.getTenantCode());
		auth.checkSourcePrefix();
	}

	@Test
	void testCheckUsername() {
		UserGateway userGateway = mock(UserGateway.class);
		// 构造用户信息
		UserE user = DomainFactory.getUser();
		when(userGateway.getProfile(user, "laokou")).thenReturn(user);
		// 校验用户名
		AuthA auth = DomainFactory.getAuth(1L, "laokou");
		auth.getUserInfo(userGateway.getProfile(user, auth.getTenantCode()));
		auth.checkUsername();
	}

	@Test
	void testCheckPassword() throws Exception {
		// 构造密码校验
		PasswordValidator passwordValidator = mock(PasswordValidator.class);
		doReturn(true).when(passwordValidator).validate("123", "202cb962ac59075b964b07152d234b70");
		// 创建用户【用户名密码】
		AuthA auth = DomainFactory.getUsernamePasswordAuth(1L, "admin", "123", "laokou", "1", "1234");
		auth.createUserByUsernamePassword();
		Assertions.assertNotNull(auth.getUser());
		// 构建密码
		UserE user = auth.getUser();
		user.setPassword(DigestUtils.md5DigestAsHex(auth.getPassword().getBytes(StandardCharsets.UTF_8)));
		// 校验密码
		auth.checkPassword(passwordValidator);
	}

	@Test
	void testCheckUserStatus() throws Exception {
		// 创建用户【用户名密码】
		AuthA auth = DomainFactory.getUsernamePasswordAuth(1L, "admin", "123", "laokou", "1", "1234");
		auth.createUserByUsernamePassword();
		Assertions.assertNotNull(auth.getUser());
		// 校验用户状态
		auth.checkUserStatus();
	}

	@Test
	void testCheckMenuPermissions() throws Exception {
		MenuGateway menuGateway = mock(MenuGateway.class);
		// 创建用户【用户名密码】
		AuthA auth = DomainFactory.getUsernamePasswordAuth(1L, "admin", "123", "laokou", "1", "1234");
		auth.createUserByUsernamePassword();
		UserE user = auth.getUser();
		Assertions.assertNotNull(user);
		// 构造菜单
		when(menuGateway.getPermissions(user)).thenReturn(Set.of("sys:user:page"));
		// 校验菜单权限集合
		auth.getMenuPermissions(menuGateway.getPermissions(user));
		auth.checkMenuPermissions();
	}

	@Test
	void testCheckDeptPaths() throws Exception {
		DeptGateway deptGateway = mock(DeptGateway.class);
		// 创建用户【用户名密码】
		AuthA auth = DomainFactory.getUsernamePasswordAuth(1L, "admin", "123", "laokou", "1", "1234");
		auth.createUserByUsernamePassword();
		UserE user = auth.getUser();
		Assertions.assertNotNull(user);
		// 构造部门
		when(deptGateway.getPaths(user)).thenReturn(new ArrayList<>(List.of("0", "0,1")));
		// 校验部门路径集合
		auth.getDeptPaths(deptGateway.getPaths(user));
		auth.checkDeptPaths();
	}

	@Test
	void testRecordLog() {
		InfoV info = new InfoV("Windows", "127.0.0.1", "中国 广东 深圳", "Chrome");
		// 记录日志【登录成功】
		AuthA auth = DomainFactory.getAuth(1L, "laokou");
		auth.getExtInfo(info);
		auth.recordLog(1L, null);
		Assertions.assertFalse(auth.getEVENTS().isEmpty());
		// 释放事件
		auth.releaseEvents();
		Assertions.assertTrue(auth.getEVENTS().isEmpty());
		// 记录日志【登录失败】
		auth.recordLog(1L, new BizException(USERNAME_PASSWORD_ERROR));
		Assertions.assertFalse(auth.getEVENTS().isEmpty());
		// 释放事件
		auth.releaseEvents();
		Assertions.assertTrue(auth.getEVENTS().isEmpty());
	}

}

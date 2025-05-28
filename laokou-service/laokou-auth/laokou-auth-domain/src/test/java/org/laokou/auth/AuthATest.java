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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.laokou.auth.model.CaptchaValidator;
import org.laokou.auth.model.PasswordValidator;
import org.laokou.auth.factory.DomainFactory;
import org.laokou.auth.gateway.*;
import org.laokou.auth.model.AuthA;
import org.laokou.auth.model.UserE;
import org.laokou.common.i18n.util.RedisKeyUtils;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.util.DigestUtils;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import static org.mockito.Mockito.*;

/**
 * 认证聚合根测试.
 *
 * @author laokou
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthATest {

	@Mock
	private UserGateway userGateway;

	@Mock
	private MenuGateway menuGateway;

	@Mock
	private DeptGateway deptGateway;

	@Mock
	private TenantGateway tenantGateway;

	@Mock
	private PasswordValidator passwordValidator;

	@Mock
	private CaptchaValidator captchaValidator;

	@BeforeEach
	void testDomainService() {
		Assertions.assertNotNull(userGateway);
		Assertions.assertNotNull(menuGateway);
		Assertions.assertNotNull(deptGateway);
		Assertions.assertNotNull(tenantGateway);
		Assertions.assertNotNull(passwordValidator);
		Assertions.assertNotNull(captchaValidator);
	}

	@Test
	void testCreateUserByUsernamePassword() {
		AuthA authA = DomainFactory.getUsernamePasswordAuth(1L, "admin", "123", "laokou", "1", "1234");
		// 创建用户【用户名密码】
		Assertions.assertDoesNotThrow(authA::createUserByUsernamePassword);
		Assertions.assertNotNull(authA.getUser());
	}

	@Test
	void testCreateUserByMobile() {
		AuthA authA = DomainFactory.getMobileAuth(1L, "18888888888", "123456", "laokou");
		// 创建用户【手机号】
		Assertions.assertDoesNotThrow(authA::createUserByMobile);
		Assertions.assertNotNull(authA.getUser());
	}

	@Test
	void testCreateUserByMail() {
		AuthA authA = DomainFactory.getMailAuth(1L, "2413176044@qq.com", "123456", "laokou");
		// 创建用户【邮箱】
		Assertions.assertDoesNotThrow(authA::createUserByMail);
		Assertions.assertNotNull(authA.getUser());
	}

	@Test
	void testCreateUserByAuthorizationCode() {
		AuthA authA = DomainFactory.getAuthorizationCodeAuth(1L, "admin", "123", "laokou");
		// 创建用户【授权码】
		Assertions.assertDoesNotThrow(authA::createUserByAuthorizationCode);
		Assertions.assertNotNull(authA.getUser());
	}

	@Test
	void testCheckTenantId() {
		// 构造租户
		when(tenantGateway.getIdTenant("laokou")).thenReturn(0L);
		// 校验租户ID
		AuthA auth = DomainFactory.getUsernamePasswordAuth(1L, "admin", "123", "laokou", "1", "1234");
		// 获取租户ID
		Assertions.assertDoesNotThrow(() -> auth.getTenantId(tenantGateway.getIdTenant(auth.getTenantCode())));
		Assertions.assertDoesNotThrow(auth::checkTenantId);
		// 校验调用次数
		verify(tenantGateway, times(1)).getIdTenant("laokou");
	}

	@Test
	void testCheckCaptcha() {
		// 构造验证码校验
		doReturn(true).when(captchaValidator)
			.validateCaptcha(RedisKeyUtils.getUsernamePasswordAuthCaptchaKey("1"), "1234");
		doReturn(true).when(captchaValidator)
			.validateCaptcha(RedisKeyUtils.getMailAuthCaptchaKey("2413176044@qq.com"), "123456");
		doReturn(true).when(captchaValidator)
			.validateCaptcha(RedisKeyUtils.getMobileAuthCaptchaKey("18888888888"), "123456");
		// 校验验证码【用户名密码登录】
		AuthA auth = DomainFactory.getUsernamePasswordAuth(1L, "admin", "123", "laokou", "1", "1234");
		Assertions.assertDoesNotThrow(() -> auth.setCaptchaValidator(captchaValidator));
		Assertions.assertDoesNotThrow(auth::checkCaptcha);
		// 校验验证码【邮箱登录】
		AuthA auth1 = DomainFactory.getMailAuth(1L, "2413176044@qq.com", "123456", "laokou");
		Assertions.assertDoesNotThrow(() -> auth1.setCaptchaValidator(captchaValidator));
		Assertions.assertDoesNotThrow(auth1::checkCaptcha);
		// 校验验证码【手机号登录】
		AuthA auth2 = DomainFactory.getMobileAuth(1L, "18888888888", "123456", "laokou");
		Assertions.assertDoesNotThrow(() -> auth2.setCaptchaValidator(captchaValidator));
		Assertions.assertDoesNotThrow(auth2::checkCaptcha);
		// 校验调用次数
		verify(captchaValidator, times(1)).validateCaptcha(RedisKeyUtils.getUsernamePasswordAuthCaptchaKey("1"),
				"1234");
		verify(captchaValidator, times(1)).validateCaptcha(RedisKeyUtils.getMailAuthCaptchaKey("2413176044@qq.com"),
				"123456");
		verify(captchaValidator, times(1)).validateCaptcha(RedisKeyUtils.getMobileAuthCaptchaKey("18888888888"),
				"123456");
	}

	@Test
	void testCheckUsername() {
		// 构造用户信息
		UserE user = DomainFactory.getUser();
		when(userGateway.getProfileUser(user)).thenReturn(user);
		// 校验用户名
		AuthA auth = DomainFactory.getUsernamePasswordAuth(1L, "admin", "123", "laokou", "1", "1234");
		Assertions.assertDoesNotThrow(() -> auth.getUserInfo(userGateway.getProfileUser(user)));
		Assertions.assertDoesNotThrow(auth::checkUsername);
	}

	@Test
	void testCheckPassword() {
		// 构造密码校验
		doReturn(true).when(passwordValidator).validatePassword("123", "202cb962ac59075b964b07152d234b70");
		// 创建用户【用户名密码】
		AuthA auth = DomainFactory.getUsernamePasswordAuth(1L, "admin", "123", "laokou", "1", "1234");
		Assertions.assertDoesNotThrow(() -> auth.setPasswordValidator(passwordValidator));
		Assertions.assertDoesNotThrow(auth::createUserByUsernamePassword);
		Assertions.assertNotNull(auth.getUser());
		// 构建密码
		UserE user = auth.getUser();
		Assertions.assertDoesNotThrow(() -> user
			.setPassword(DigestUtils.md5DigestAsHex(auth.getPassword().getBytes(StandardCharsets.UTF_8))));
		// 校验密码
		Assertions.assertDoesNotThrow(auth::checkPassword);
	}

	@Test
	void testCheckUserStatus() {
		// 创建用户【用户名密码】
		AuthA auth = DomainFactory.getUsernamePasswordAuth(1L, "admin", "123", "laokou", "1", "1234");
		Assertions.assertDoesNotThrow(auth::createUserByUsernamePassword);
		Assertions.assertNotNull(auth.getUser());
		// 校验用户状态
		Assertions.assertDoesNotThrow(auth::checkUserStatus);
	}

	@Test
	void testCheckMenuPermissions() {
		// 创建用户【用户名密码】
		AuthA auth = DomainFactory.getUsernamePasswordAuth(1L, "admin", "123", "laokou", "1", "1234");
		Assertions.assertDoesNotThrow(auth::createUserByUsernamePassword);
		UserE user = auth.getUser();
		Assertions.assertNotNull(user);
		// 构造菜单
		when(menuGateway.getPermissionsMenu(user)).thenReturn(Set.of("sys:user:page"));
		// 校验菜单权限集合
		Assertions.assertDoesNotThrow(() -> auth.getMenuPermissions(menuGateway.getPermissionsMenu(user)));
		Assertions.assertDoesNotThrow(auth::checkMenuPermissions);
	}

	@Test
	void testCheckDeptPaths() {
		// 创建用户【用户名密码】
		AuthA auth = DomainFactory.getUsernamePasswordAuth(1L, "admin", "123", "laokou", "1", "1234");
		Assertions.assertDoesNotThrow(auth::createUserByUsernamePassword);
		UserE user = auth.getUser();
		Assertions.assertNotNull(user);
		// 构造部门
		when(deptGateway.getPathsDept(user)).thenReturn(new ArrayList<>(List.of("0", "0,1")));
		// 校验部门路径集合
		Assertions.assertDoesNotThrow(() -> auth.getDeptPaths(deptGateway.getPathsDept(user)));
		Assertions.assertDoesNotThrow(auth::checkDeptPaths);
	}

}

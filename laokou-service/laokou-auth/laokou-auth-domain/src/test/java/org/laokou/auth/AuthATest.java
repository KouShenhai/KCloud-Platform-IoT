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
import org.laokou.auth.model.*;
import org.laokou.auth.factory.DomainFactory;
import org.laokou.auth.gateway.*;
import org.laokou.common.crypto.util.AESUtils;
import org.laokou.common.i18n.util.RedisKeyUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.util.DigestUtils;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.laokou.common.i18n.common.constant.StringConstants.EMPTY;
import static org.mockito.Mockito.*;

/**
 * 认证聚合根测试.
 *
 * @author laokou
 */
@SpringBootTest
class AuthATest {

	@MockitoBean
	private UserGateway userGateway;

	@MockitoBean
	private MenuGateway menuGateway;

	@MockitoBean
	private DeptGateway deptGateway;

	@MockitoBean
	private TenantGateway tenantGateway;

	@MockitoBean
	private PasswordValidator passwordValidator;

	@MockitoBean
	private CaptchaValidator captchaValidator;

	@MockitoBean
	private LoginLogGateway loginLogGateway;

	@MockitoBean
	private NoticeLogGateway noticeLogGateway;

	@MockitoBean("authorizationCodeAuthParamValidator")
	private AuthParamValidator authorizationCodeAuthParamValidator;

	@MockitoBean("mailAuthParamValidator")
	private AuthParamValidator mailAuthParamValidator;

	@MockitoBean("mobileAuthParamValidator")
	private AuthParamValidator mobileAuthParamValidator;

	@MockitoBean("testAuthParamValidator")
	private AuthParamValidator testAuthParamValidator;

	@MockitoBean("usernamePasswordAuthParamValidator")
	private AuthParamValidator usernamePasswordAuthParamValidator;

	@Test
	void testCreateUserByTest() throws Exception {
		AuthA authA = getAuth("admin", "123", GrantTypeEnum.TEST, EMPTY, EMPTY);
		// 创建用户【测试】
		Assertions.assertDoesNotThrow(authA::createUserByTest);
		UserE user = authA.getUser();
		Assertions.assertNotNull(user);
		Assertions.assertEquals(AESUtils.encrypt("admin"), user.getUsername());
	}

	@Test
	void testCreateUserByUsernamePassword() throws Exception {
		AuthA authA = getAuth("admin", "123", GrantTypeEnum.USERNAME_PASSWORD, "1", "1234");
		// 创建用户【用户名密码】
		Assertions.assertDoesNotThrow(authA::createUserByUsernamePassword);
		UserE user = authA.getUser();
		Assertions.assertNotNull(user);
		Assertions.assertEquals(AESUtils.encrypt("admin"), user.getUsername());
	}

	@Test
	void testCreateUserByMobile() throws Exception {
		AuthA authA = getAuth(EMPTY, EMPTY, GrantTypeEnum.MOBILE, "18888888888", "123456");
		;
		// 创建用户【手机号】
		Assertions.assertDoesNotThrow(authA::createUserByMobile);
		UserE user = authA.getUser();
		Assertions.assertNotNull(user);
		Assertions.assertEquals(AESUtils.encrypt("18888888888"), user.getMobile());
	}

	@Test
	void testCreateUserByMail() throws Exception {
		AuthA authA = getAuth(EMPTY, EMPTY, GrantTypeEnum.MAIL, "2413176044@qq.com", "123456");
		// 创建用户【邮箱】
		Assertions.assertDoesNotThrow(authA::createUserByMail);
		UserE user = authA.getUser();
		Assertions.assertNotNull(user);
		Assertions.assertEquals(AESUtils.encrypt("2413176044@qq.com"), user.getMail());
	}

	@Test
	void testCreateUserByAuthorizationCode() throws Exception {
		AuthA authA = getAuth("admin", "123", GrantTypeEnum.AUTHORIZATION_CODE, EMPTY, EMPTY);
		// 创建用户【授权码】
		Assertions.assertDoesNotThrow(authA::createUserByAuthorizationCode);
		UserE user = authA.getUser();
		Assertions.assertNotNull(user);
		Assertions.assertEquals(AESUtils.encrypt("admin"), user.getUsername());
	}

	@Test
	void testCheckTenantId() {
		// 构造租户
		when(tenantGateway.getTenantId("laokou")).thenReturn(0L);
		// 校验租户ID
		AuthA auth = getAuth("admin", "123", GrantTypeEnum.USERNAME_PASSWORD, "1", "1234");
		// 获取租户ID
		Assertions.assertDoesNotThrow(auth::createUserByUsernamePassword);
		Assertions.assertDoesNotThrow(() -> auth.getTenantId(tenantGateway.getTenantId(auth.getTenantCode())));
		Assertions.assertDoesNotThrow(auth::checkTenantId);
		// 校验调用次数
		verify(tenantGateway, times(1)).getTenantId("laokou");
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
		AuthA auth = getAuth("admin", "123", GrantTypeEnum.USERNAME_PASSWORD, "1", "1234");
		Assertions.assertDoesNotThrow(auth::checkCaptcha);
		// 校验验证码【邮箱登录】
		AuthA auth1 = getAuth(EMPTY, EMPTY, GrantTypeEnum.MAIL, "2413176044@qq.com", "123456");
		Assertions.assertDoesNotThrow(auth1::checkCaptcha);
		// 校验验证码【手机号登录】
		AuthA auth2 = getAuth(EMPTY, EMPTY, GrantTypeEnum.MOBILE, "18888888888", "123456");
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
		when(userGateway.getUserProfile(user)).thenReturn(user);
		// 校验用户名
		AuthA auth = getAuth("admin", "123", GrantTypeEnum.USERNAME_PASSWORD, "1", "1234");
		Assertions.assertDoesNotThrow(() -> auth.getUserInfo(userGateway.getUserProfile(user)));
		Assertions.assertDoesNotThrow(auth::checkUsername);
	}

	@Test
	void testCheckPassword() {
		// 构造密码校验
		doReturn(true).when(passwordValidator).validatePassword("123", "202cb962ac59075b964b07152d234b70");
		// 创建用户【用户名密码】
		AuthA auth = getAuth("admin", "123", GrantTypeEnum.USERNAME_PASSWORD, "1", "1234");
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
		AuthA auth = getAuth("admin", "123", GrantTypeEnum.USERNAME_PASSWORD, "1", "1234");
		Assertions.assertDoesNotThrow(auth::createUserByUsernamePassword);
		Assertions.assertNotNull(auth.getUser());
		// 校验用户状态
		Assertions.assertDoesNotThrow(auth::checkUserStatus);
	}

	@Test
	void testCheckMenuPermissions() {
		// 创建用户【用户名密码】
		AuthA auth = getAuth("admin", "123", GrantTypeEnum.USERNAME_PASSWORD, "1", "1234");
		Assertions.assertDoesNotThrow(auth::createUserByUsernamePassword);
		UserE user = auth.getUser();
		Assertions.assertNotNull(user);
		// 构造菜单
		when(menuGateway.getMenuPermissions(user)).thenReturn(Set.of("sys:user:page"));
		// 校验菜单权限集合
		Assertions.assertDoesNotThrow(() -> auth.getMenuPermissions(menuGateway.getMenuPermissions(user)));
		Assertions.assertDoesNotThrow(auth::checkMenuPermissions);
	}

	@Test
	void testCheckDeptPaths() {
		// 创建用户【用户名密码】
		AuthA auth = getAuth("admin", "123", GrantTypeEnum.USERNAME_PASSWORD, "1", "1234");
		Assertions.assertDoesNotThrow(auth::createUserByUsernamePassword);
		UserE user = auth.getUser();
		Assertions.assertNotNull(user);
		// 构造部门
		when(deptGateway.getDeptPaths(user)).thenReturn(new ArrayList<>(List.of("0", "0,1")));
		// 校验部门路径集合
		Assertions.assertDoesNotThrow(() -> auth.getDeptPaths(deptGateway.getDeptPaths(user)));
		Assertions.assertDoesNotThrow(auth::checkDeptPaths);
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

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

import org.junit.jupiter.api.Test;
import org.laokou.auth.model.*;
import org.laokou.auth.factory.DomainFactory;
import org.laokou.auth.gateway.*;
import org.laokou.common.crypto.util.AESUtils;
import org.laokou.common.crypto.util.RSAUtils;
import org.laokou.common.i18n.dto.IdGenerator;
import org.laokou.common.i18n.util.RedisKeyUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.util.DigestUtils;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
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

	@MockitoBean
	private IdGenerator idGenerator;

	@MockitoBean
	private OssLogGateway ossLogGateway;

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
	void test_getUserAvatar() {
		AuthA authA = getAuth("admin", "123", GrantTypeEnum.USERNAME_PASSWORD, "1", "1234");
		UserE user = authA.getUser();
		assertThatNoException().isThrownBy(() -> user.setAvatar(1L));
		// 构造租户
		when(ossLogGateway.getOssUrl(1L)).thenReturn("https://laokou.png");
		authA.getUserAvatar(ossLogGateway.getOssUrl(user.getAvatar()));
		assertThat(authA.getAvatar()).isEqualTo("https://laokou.png");
	}

	@Test
	void test_decryptUsernamePassword() {
		String username = "admin";
		String password = "123";
		String encryptUsername = RSAUtils.encryptByPublicKey(username);
		String decryptUsername = RSAUtils.decryptByPrivateKey(encryptUsername);
		String encryptPassword = RSAUtils.encryptByPublicKey(password);
		String decryptPassword = RSAUtils.decryptByPrivateKey(encryptPassword);
		assertThat(username).isEqualTo(decryptUsername);
		assertThat(password).isEqualTo(decryptPassword);
		AuthA authA = getAuth(encryptUsername, encryptPassword, GrantTypeEnum.USERNAME_PASSWORD, EMPTY, EMPTY);
		assertThatNoException().isThrownBy(authA::decryptUsernamePassword);
		assertThat(authA.getUsername()).isEqualTo(username);
		assertThat(authA.getPassword()).isEqualTo(password);
	}

	@Test
	void test_createUserByTest() throws Exception {
		AuthA authA = getAuth("admin", "123", GrantTypeEnum.TEST, EMPTY, EMPTY);
		// 创建用户【测试】
		assertThatNoException().isThrownBy(authA::createUserByTest);
		UserE user = authA.getUser();
		assertThat(user).isNotNull();
		assertThat(user.getUsername()).isEqualTo(AESUtils.encrypt("admin"));
	}

	@Test
	void test_createUserByUsernamePassword() throws Exception {
		AuthA authA = getAuth("admin", "123", GrantTypeEnum.USERNAME_PASSWORD, "1", "1234");
		// 创建用户【用户名密码】
		assertThatNoException().isThrownBy(authA::createUserByUsernamePassword);
		UserE user = authA.getUser();
		assertThat(user).isNotNull();
		assertThat(user.getUsername()).isEqualTo(AESUtils.encrypt("admin"));
	}

	@Test
	void test_createUserByMobile() throws Exception {
		AuthA authA = getAuth(EMPTY, EMPTY, GrantTypeEnum.MOBILE, "18888888888", "123456");
		// 创建用户【手机号】
		assertThatNoException().isThrownBy(authA::createUserByMobile);
		UserE user = authA.getUser();
		assertThat(user).isNotNull();
		assertThat(user.getMobile()).isEqualTo(AESUtils.encrypt("18888888888"));
	}

	@Test
	void test_createUserByMail() throws Exception {
		AuthA authA = getAuth(EMPTY, EMPTY, GrantTypeEnum.MAIL, "2413176044@qq.com", "123456");
		// 创建用户【邮箱】
		assertThatNoException().isThrownBy(authA::createUserByMail);
		UserE user = authA.getUser();
		assertThat(user).isNotNull();
		assertThat(user.getMail()).isEqualTo(AESUtils.encrypt("2413176044@qq.com"));
	}

	@Test
	void test_createUserByAuthorizationCode() throws Exception {
		AuthA authA = getAuth("admin", "123", GrantTypeEnum.AUTHORIZATION_CODE, EMPTY, EMPTY);
		// 创建用户【授权码】
		assertThatNoException().isThrownBy(authA::createUserByAuthorizationCode);
		UserE user = authA.getUser();
		assertThat(user).isNotNull();
		assertThat(user.getUsername()).isEqualTo(AESUtils.encrypt("admin"));
	}

	@Test
	void test_checkTenantId() {
		// 构造租户
		when(tenantGateway.getTenantId("laokou")).thenReturn(0L);
		// 校验租户ID
		AuthA auth = getAuth("admin", "123", GrantTypeEnum.USERNAME_PASSWORD, "1", "1234");
		// 获取租户ID
		assertThatNoException().isThrownBy(auth::createUserByUsernamePassword);
		assertThatNoException().isThrownBy(() -> auth.getTenantId(() -> tenantGateway.getTenantId(auth.getTenantCode())));
		assertThatNoException().isThrownBy(auth::checkTenantId);
		// 校验调用次数
		verify(tenantGateway, times(1)).getTenantId("laokou");
	}

	@Test
	void test_checkCaptcha() {
		// 构造验证码校验
		doReturn(true).when(captchaValidator)
			.validateCaptcha(RedisKeyUtils.getUsernamePasswordAuthCaptchaKey("1"), "1234");
		doReturn(true).when(captchaValidator)
			.validateCaptcha(RedisKeyUtils.getMailAuthCaptchaKey("2413176044@qq.com"), "123456");
		doReturn(true).when(captchaValidator)
			.validateCaptcha(RedisKeyUtils.getMobileAuthCaptchaKey("18888888888"), "123456");
		// 校验验证码【用户名密码登录】
		AuthA auth = getAuth("admin", "123", GrantTypeEnum.USERNAME_PASSWORD, "1", "1234");
		assertThatNoException().isThrownBy(auth::checkCaptcha);
		// 校验验证码【邮箱登录】
		AuthA auth1 = getAuth(EMPTY, EMPTY, GrantTypeEnum.MAIL, "2413176044@qq.com", "123456");
		assertThatNoException().isThrownBy(auth1::checkCaptcha);
		// 校验验证码【手机号登录】
		AuthA auth2 = getAuth(EMPTY, EMPTY, GrantTypeEnum.MOBILE, "18888888888", "123456");
		assertThatNoException().isThrownBy(auth2::checkCaptcha);
		// 校验调用次数
		verify(captchaValidator, times(1)).validateCaptcha(RedisKeyUtils.getUsernamePasswordAuthCaptchaKey("1"),
				"1234");
		verify(captchaValidator, times(1)).validateCaptcha(RedisKeyUtils.getMailAuthCaptchaKey("2413176044@qq.com"),
				"123456");
		verify(captchaValidator, times(1)).validateCaptcha(RedisKeyUtils.getMobileAuthCaptchaKey("18888888888"),
				"123456");
	}

	@Test
	void test_checkUsername() {
		// 构造用户信息
		UserE user = DomainFactory.getUser();
		when(userGateway.getUserProfile(user)).thenReturn(user);
		// 校验用户名
		AuthA auth = getAuth("admin", "123", GrantTypeEnum.USERNAME_PASSWORD, "1", "1234");
		assertThatNoException().isThrownBy(() -> auth.getUserInfo(userGateway.getUserProfile(user)));
		assertThatNoException().isThrownBy(auth::checkUsername);
	}

	@Test
	void test_checkPassword() {
		// 构造密码校验
		doReturn(true).when(passwordValidator).validatePassword("123", "202cb962ac59075b964b07152d234b70");
		// 创建用户【用户名密码】
		AuthA auth = getAuth("admin", "123", GrantTypeEnum.USERNAME_PASSWORD, "1", "1234");
		assertThatNoException().isThrownBy(auth::createUserByUsernamePassword);
		assertThat(auth.getUser()).isNotNull();
		// 构建密码
		UserE user = auth.getUser();
		assertThatNoException().isThrownBy(() -> user
			.setPassword(DigestUtils.md5DigestAsHex(auth.getPassword().getBytes(StandardCharsets.UTF_8))));
		// 校验密码
		assertThatNoException().isThrownBy(auth::checkPassword);
	}

	@Test
	void test_checkUserStatus() {
		// 创建用户【用户名密码】
		AuthA auth = getAuth("admin", "123", GrantTypeEnum.USERNAME_PASSWORD, "1", "1234");
		assertThatNoException().isThrownBy(auth::createUserByUsernamePassword);
		assertThat(auth.getUser()).isNotNull();
		// 校验用户状态
		assertThatNoException().isThrownBy(auth::checkUserStatus);
	}

	@Test
	void test_checkMenuPermissions() {
		// 创建用户【用户名密码】
		AuthA auth = getAuth("admin", "123", GrantTypeEnum.USERNAME_PASSWORD, "1", "1234");
		assertThatNoException().isThrownBy(auth::createUserByUsernamePassword);
		UserE user = auth.getUser();
		assertThat(user).isNotNull();
		// 构造菜单
		when(menuGateway.getMenuPermissions(user)).thenReturn(Set.of("sys:user:page"));
		// 校验菜单权限集合
		assertThatNoException().isThrownBy(() -> auth.getMenuPermissions(menuGateway.getMenuPermissions(user)));
		assertThatNoException().isThrownBy(auth::checkMenuPermissions);
	}

	@Test
	void test_checkDeptPaths() {
		// 创建用户【用户名密码】
		AuthA auth = getAuth("admin", "123", GrantTypeEnum.USERNAME_PASSWORD, "1", "1234");
		assertThatNoException().isThrownBy(auth::createUserByUsernamePassword);
		UserE user = auth.getUser();
		assertThat(user).isNotNull();
		// 构造部门
		when(deptGateway.getDeptPaths(user)).thenReturn(new ArrayList<>(List.of("0", "0,1")));
		// 校验部门路径集合
		assertThatNoException().isThrownBy(() -> auth.getDeptPaths(deptGateway.getDeptPaths(user)));
		assertThatNoException().isThrownBy(auth::checkDeptPaths);
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

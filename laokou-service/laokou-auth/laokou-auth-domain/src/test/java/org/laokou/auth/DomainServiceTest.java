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
import org.laokou.auth.ability.DomainService;
import org.laokou.auth.model.CaptchaValidator;
import org.laokou.auth.model.PasswordValidator;
import org.laokou.auth.factory.DomainFactory;
import org.laokou.auth.gateway.*;
import org.laokou.auth.model.*;
import org.laokou.common.i18n.util.RedisKeyUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.util.DigestUtils;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.laokou.auth.model.SendCaptchaTypeEnum.SEND_MAIL_CAPTCHA;
import static org.laokou.auth.model.SendCaptchaTypeEnum.SEND_MOBILE_CAPTCHA;
import static org.laokou.common.i18n.common.constant.StringConstants.EMPTY;
import static org.mockito.Mockito.*;

/**
 * 领域服务测试.
 *
 * @author laokou
 */
@SpringBootTest
class DomainServiceTest {

	@MockitoBean
	private UserGateway userGateway;

	@MockitoBean
	private MenuGateway menuGateway;

	@MockitoBean
	private DeptGateway deptGateway;

	@MockitoBean
	private TenantGateway tenantGateway;

	@MockitoBean
	private LoginLogGateway loginLogGateway;

	@MockitoBean
	private NoticeLogGateway noticeLogGateway;

	@MockitoBean
	private PasswordValidator passwordValidator;

	@MockitoBean
	private CaptchaValidator captchaValidator;

	@MockitoSpyBean
	private DomainService domainService;

	@MockitoBean
	private IdGenerator idGenerator;

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

	@MockitoBean("mailCaptchaParamValidator")
	private CaptchaParamValidator mailCaptchaParamValidator;

	@MockitoBean("mobileCaptchaParamValidator")
	private CaptchaParamValidator mobileCaptchaParamValidator;

	@Test
	void testUsernamePasswordAuth() {
		AuthA auth = getAuth("admin", "123", GrantTypeEnum.USERNAME_PASSWORD, "1", "1234");
		// 创建用户【用户名密码】
		Assertions.assertDoesNotThrow(auth::createUserByUsernamePassword);
		// 构造租户
		when(tenantGateway.getTenantId("laokou")).thenReturn(0L);
		// 构造验证码校验
		doReturn(true).when(captchaValidator)
			.validateCaptcha(RedisKeyUtils.getUsernamePasswordAuthCaptchaKey("1"), "1234");
		// 构造用户信息
		UserE user = auth.getUser();
		Assertions.assertDoesNotThrow(() -> user
			.setPassword(DigestUtils.md5DigestAsHex(auth.getPassword().getBytes(StandardCharsets.UTF_8))));
		when(userGateway.getUserProfile(user)).thenReturn(user);
		// 构造密码校验
		doReturn(true).when(passwordValidator).validatePassword("123", "202cb962ac59075b964b07152d234b70");
		// 构造菜单
		when(menuGateway.getMenuPermissions(user)).thenReturn(Set.of("sys:user:page"));
		// 构造部门
		when(deptGateway.getDeptPaths(user)).thenReturn(new ArrayList<>(List.of("0", "0,1")));
		// 用户名密码登录
		Assertions.assertDoesNotThrow(() -> domainService.auth(auth));
		// 校验调用次数
		verify(deptGateway, times(1)).getDeptPaths(user);
		verify(menuGateway, times(1)).getMenuPermissions(user);
		verify(passwordValidator, times(1)).validatePassword("123", "202cb962ac59075b964b07152d234b70");
		verify(captchaValidator, times(1)).validateCaptcha(RedisKeyUtils.getUsernamePasswordAuthCaptchaKey("1"),
				"1234");
		verify(userGateway, times(1)).getUserProfile(user);
		verify(tenantGateway, times(1)).getTenantId("laokou");
	}

	@Test
	void testMailAuth() {
		AuthA auth = getAuth(EMPTY, EMPTY, GrantTypeEnum.MAIL, "2413176044@qq.com", "123456");
		// 创建用户【邮箱】
		Assertions.assertDoesNotThrow(auth::createUserByMail);
		// 构造租户
		when(tenantGateway.getTenantId("laokou")).thenReturn(0L);
		// 构造验证码校验
		doReturn(true).when(captchaValidator)
			.validateCaptcha(RedisKeyUtils.getMailAuthCaptchaKey("2413176044@qq.com"), "123456");
		// 构造用户信息
		UserE user = auth.getUser();
		when(userGateway.getUserProfile(user)).thenReturn(user);
		// 构造菜单
		when(menuGateway.getMenuPermissions(user)).thenReturn(Set.of("sys:user:page"));
		// 构造部门
		when(deptGateway.getDeptPaths(user)).thenReturn(new ArrayList<>(List.of("0", "0,1")));
		// 邮箱登录
		Assertions.assertDoesNotThrow(() -> domainService.auth(auth));
		// 校验调用次数
		verify(deptGateway, times(1)).getDeptPaths(user);
		verify(menuGateway, times(1)).getMenuPermissions(user);
		verify(userGateway, times(1)).getUserProfile(user);
		verify(captchaValidator, times(1)).validateCaptcha(RedisKeyUtils.getMailAuthCaptchaKey("2413176044@qq.com"),
				"123456");
		verify(tenantGateway, times(1)).getTenantId("laokou");
	}

	@Test
	void testMobileAuth() {
		AuthA auth = getAuth(EMPTY, EMPTY, GrantTypeEnum.MOBILE, "18888888888", "123456");
		// 创建用户【手机号】
		Assertions.assertDoesNotThrow(auth::createUserByMobile);
		// 构造租户
		when(tenantGateway.getTenantId("laokou")).thenReturn(0L);
		// 构造验证码校验
		doReturn(true).when(captchaValidator)
			.validateCaptcha(RedisKeyUtils.getMobileAuthCaptchaKey("18888888888"), "123456");
		// 构造用户信息
		UserE user = auth.getUser();
		when(userGateway.getUserProfile(user)).thenReturn(user);
		// 构造菜单
		when(menuGateway.getMenuPermissions(user)).thenReturn(Set.of("sys:user:page"));
		// 构造部门
		when(deptGateway.getDeptPaths(user)).thenReturn(new ArrayList<>(List.of("0", "0,1")));
		// 手机号登录
		Assertions.assertDoesNotThrow(() -> domainService.auth(auth));
		// 校验调用次数
		verify(deptGateway, times(1)).getDeptPaths(user);
		verify(menuGateway, times(1)).getMenuPermissions(user);
		verify(userGateway, times(1)).getUserProfile(user);
		verify(tenantGateway, times(1)).getTenantId("laokou");
	}

	@Test
	void testAuthorizationCodeAuth() {
		AuthA auth = getAuth("admin", "123", GrantTypeEnum.AUTHORIZATION_CODE, EMPTY, EMPTY);
		// 创建用户【授权码】
		Assertions.assertDoesNotThrow(auth::createUserByAuthorizationCode);
		// 构造租户
		when(tenantGateway.getTenantId("laokou")).thenReturn(0L);
		// 构造用户信息
		UserE user = auth.getUser();
		Assertions.assertDoesNotThrow(() -> user
			.setPassword(DigestUtils.md5DigestAsHex(auth.getPassword().getBytes(StandardCharsets.UTF_8))));
		when(userGateway.getUserProfile(user)).thenReturn(user);
		// 构造密码校验
		doReturn(true).when(passwordValidator).validatePassword("123", "202cb962ac59075b964b07152d234b70");
		// 构造菜单
		when(menuGateway.getMenuPermissions(user)).thenReturn(Set.of("sys:user:page"));
		// 构造部门
		when(deptGateway.getDeptPaths(user)).thenReturn(new ArrayList<>(List.of("0", "0,1")));
		// 授权码登录
		Assertions.assertDoesNotThrow(() -> domainService.auth(auth));
		// 校验调用次数
		verify(deptGateway, times(1)).getDeptPaths(user);
		verify(menuGateway, times(1)).getMenuPermissions(user);
		verify(passwordValidator, times(1)).validatePassword("123", "202cb962ac59075b964b07152d234b70");
		verify(userGateway, times(1)).getUserProfile(user);
		verify(tenantGateway, times(1)).getTenantId("laokou");
	}

	@Test
	void testTestAuth() {
		AuthA auth = getAuth("admin", "123", GrantTypeEnum.TEST, EMPTY, EMPTY);
		// 创建用户【测试】
		Assertions.assertDoesNotThrow(auth::createUserByTest);
		// 构造租户
		when(tenantGateway.getTenantId("laokou")).thenReturn(0L);
		// 构造用户信息
		UserE user = auth.getUser();
		Assertions.assertDoesNotThrow(() -> user
			.setPassword(DigestUtils.md5DigestAsHex(auth.getPassword().getBytes(StandardCharsets.UTF_8))));
		when(userGateway.getUserProfile(user)).thenReturn(user);
		// 构造密码校验
		doReturn(true).when(passwordValidator).validatePassword("123", "202cb962ac59075b964b07152d234b70");
		// 构造菜单
		when(menuGateway.getMenuPermissions(user)).thenReturn(Set.of("sys:user:page"));
		// 构造部门
		when(deptGateway.getDeptPaths(user)).thenReturn(new ArrayList<>(List.of("0", "0,1")));
		// 测试登录
		Assertions.assertDoesNotThrow(() -> domainService.auth(auth));
		// 校验调用次数
		verify(deptGateway, times(1)).getDeptPaths(user);
		verify(menuGateway, times(1)).getMenuPermissions(user);
		verify(passwordValidator, times(1)).validatePassword("123", "202cb962ac59075b964b07152d234b70");
		verify(userGateway, times(1)).getUserProfile(user);
		verify(tenantGateway, times(1)).getTenantId("laokou");
	}

	@Test
	void testCreateLoginLog() {
		LoginLogE loginLog = DomainFactory.getLoginLog();
		// 创建登录日志
		Assertions.assertDoesNotThrow(() -> domainService.createLoginLog(loginLog));
	}

	@Test
	void testCreateSendCaptchaInfoByMail() {
		// 创建发送验证码信息
		CaptchaE captcha = getCaptcha("2413176044@qq.com", SEND_MAIL_CAPTCHA.getCode());
		Assertions.assertDoesNotThrow(() -> domainService.createSendCaptchaInfo(captcha));
	}

	@Test
	void testCreateSendCaptchaInfoByMobile() {
		// 创建发送验证码信息
		CaptchaE captcha = getCaptcha("18888888888", SEND_MOBILE_CAPTCHA.getCode());
		Assertions.assertDoesNotThrow(() -> domainService.createSendCaptchaInfo(captcha));
	}

	@Test
	void testCreateNoticeLogByMailCaptcha() {
		// 创建通知日志
		NoticeLogE noticeLog = DomainFactory.getNoticeLog();
		Assertions.assertDoesNotThrow(() -> noticeLog.setCode(SendCaptchaTypeEnum.SEND_MAIL_CAPTCHA.getCode()));
		Assertions.assertDoesNotThrow(() -> noticeLog.setStatus(SendCaptchaStatusEnum.OK.getCode()));
		Assertions.assertDoesNotThrow(() -> domainService.createNoticeLog(noticeLog));
	}

	@Test
	void testCreateNoticeLogByMobileCaptcha() {
		// 创建通知日志
		NoticeLogE noticeLog = DomainFactory.getNoticeLog();
		Assertions.assertDoesNotThrow(() -> noticeLog.setCode(SendCaptchaTypeEnum.SEND_MAIL_CAPTCHA.getCode()));
		Assertions.assertDoesNotThrow(() -> noticeLog.setStatus(SendCaptchaStatusEnum.OK.getCode()));
		Assertions.assertDoesNotThrow(() -> domainService.createNoticeLog(noticeLog));
	}

	private CaptchaE getCaptcha(String uuid, String tag) {
		CaptchaE captchaE = DomainFactory.getCaptcha();
		captchaE.setId(1L);
		captchaE.setUuid(uuid);
		captchaE.setSendCaptchaTypeEnum(SendCaptchaTypeEnum.getByCode(tag));
		captchaE.setTenantCode("laokou");
		return captchaE;
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

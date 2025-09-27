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

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.auth.ability.DomainService;
import org.laokou.auth.factory.DomainFactory;
import org.laokou.auth.gateway.DeptGateway;
import org.laokou.auth.gateway.LoginLogGateway;
import org.laokou.auth.gateway.MenuGateway;
import org.laokou.auth.gateway.NoticeLogGateway;
import org.laokou.auth.gateway.OssLogGateway;
import org.laokou.auth.gateway.TenantGateway;
import org.laokou.auth.gateway.UserGateway;
import org.laokou.auth.model.AuthA;
import org.laokou.auth.model.AuthParamValidator;
import org.laokou.auth.model.CaptchaE;
import org.laokou.auth.model.CaptchaParamValidator;
import org.laokou.auth.model.CaptchaV;
import org.laokou.auth.model.CaptchaValidator;
import org.laokou.auth.model.GrantTypeEnum;
import org.laokou.auth.model.LoginLogE;
import org.laokou.auth.model.NoticeLogE;
import org.laokou.auth.model.PasswordValidator;
import org.laokou.auth.model.SendCaptchaStatusEnum;
import org.laokou.auth.model.SendCaptchaTypeEnum;
import org.laokou.auth.model.UserE;
import org.laokou.common.i18n.common.constant.StringConstants;
import org.laokou.common.i18n.dto.IdGenerator;
import org.laokou.common.i18n.util.RedisKeyUtils;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
	private OssLogGateway ossLogGateway;

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
	void test_usernamePasswordAuth() {
		AuthA auth = getAuth("admin", "123", GrantTypeEnum.USERNAME_PASSWORD, "1", "1234");
		// 创建用户【用户名密码】
		Assertions.assertThatNoException().isThrownBy(auth::createUserByUsernamePassword);
		// 构造租户
		Mockito.when(tenantGateway.getTenantId("laokou")).thenReturn(0L);
		// 构造验证码校验
		Mockito.doReturn(true)
			.when(captchaValidator)
			.validateCaptcha(RedisKeyUtils.getUsernamePasswordAuthCaptchaKey("1"), "1234");
		// 构造用户信息
		UserE user = auth.getUser();
		Assertions.assertThatNoException()
			.isThrownBy(() -> user
				.setPassword(DigestUtils.md5DigestAsHex(auth.getPassword().getBytes(StandardCharsets.UTF_8))));
		Mockito.when(userGateway.getUserProfile(user)).thenReturn(user);
		// 构造密码校验
		Mockito.doReturn(true).when(passwordValidator).validatePassword("123", "202cb962ac59075b964b07152d234b70");
		// 构造菜单
		Mockito.when(menuGateway.getMenuPermissions(user)).thenReturn(Set.of("sys:user:page"));
		// 构造部门
		Mockito.when(deptGateway.getDeptPaths(user)).thenReturn(new ArrayList<>(List.of("0", "0,1")));
		// 用户名密码登录
		Assertions.assertThatNoException().isThrownBy(() -> domainService.auth(auth));
		// 校验调用次数
		Mockito.verify(deptGateway, Mockito.times(1)).getDeptPaths(user);
		Mockito.verify(menuGateway, Mockito.times(1)).getMenuPermissions(user);
		Mockito.verify(passwordValidator, Mockito.times(1)).validatePassword("123", "202cb962ac59075b964b07152d234b70");
		Mockito.verify(captchaValidator, Mockito.times(1))
			.validateCaptcha(RedisKeyUtils.getUsernamePasswordAuthCaptchaKey("1"), "1234");
		Mockito.verify(userGateway, Mockito.times(1)).getUserProfile(user);
		Mockito.verify(tenantGateway, Mockito.times(1)).getTenantId("laokou");
	}

	@Test
	void test_mailAuth() {
		AuthA auth = getAuth(StringConstants.EMPTY, StringConstants.EMPTY, GrantTypeEnum.MAIL, "2413176044@qq.com",
				"123456");
		// 创建用户【邮箱】
		Assertions.assertThatNoException().isThrownBy(auth::createUserByMail);
		// 构造租户
		Mockito.when(tenantGateway.getTenantId("laokou")).thenReturn(0L);
		// 构造验证码校验
		Mockito.doReturn(true)
			.when(captchaValidator)
			.validateCaptcha(RedisKeyUtils.getMailAuthCaptchaKey("2413176044@qq.com"), "123456");
		// 构造用户信息
		UserE user = auth.getUser();
		Mockito.when(userGateway.getUserProfile(user)).thenReturn(user);
		// 构造菜单
		Mockito.when(menuGateway.getMenuPermissions(user)).thenReturn(Set.of("sys:user:page"));
		// 构造部门
		Mockito.when(deptGateway.getDeptPaths(user)).thenReturn(new ArrayList<>(List.of("0", "0,1")));
		// 邮箱登录
		Assertions.assertThatNoException().isThrownBy(() -> domainService.auth(auth));
		// 校验调用次数
		Mockito.verify(deptGateway, Mockito.times(1)).getDeptPaths(user);
		Mockito.verify(menuGateway, Mockito.times(1)).getMenuPermissions(user);
		Mockito.verify(userGateway, Mockito.times(1)).getUserProfile(user);
		Mockito.verify(captchaValidator, Mockito.times(1))
			.validateCaptcha(RedisKeyUtils.getMailAuthCaptchaKey("2413176044@qq.com"), "123456");
		Mockito.verify(tenantGateway, Mockito.times(1)).getTenantId("laokou");
	}

	@Test
	void test_mobileAuth() {
		AuthA auth = getAuth(StringConstants.EMPTY, StringConstants.EMPTY, GrantTypeEnum.MOBILE, "18888888888",
				"123456");
		// 创建用户【手机号】
		Assertions.assertThatNoException().isThrownBy(auth::createUserByMobile);
		// 构造租户
		Mockito.when(tenantGateway.getTenantId("laokou")).thenReturn(0L);
		// 构造验证码校验
		Mockito.doReturn(true)
			.when(captchaValidator)
			.validateCaptcha(RedisKeyUtils.getMobileAuthCaptchaKey("18888888888"), "123456");
		// 构造用户信息
		UserE user = auth.getUser();
		Mockito.when(userGateway.getUserProfile(user)).thenReturn(user);
		// 构造菜单
		Mockito.when(menuGateway.getMenuPermissions(user)).thenReturn(Set.of("sys:user:page"));
		// 构造部门
		Mockito.when(deptGateway.getDeptPaths(user)).thenReturn(new ArrayList<>(List.of("0", "0,1")));
		// 手机号登录
		Assertions.assertThatNoException().isThrownBy(() -> domainService.auth(auth));
		// 校验调用次数
		Mockito.verify(deptGateway, Mockito.times(1)).getDeptPaths(user);
		Mockito.verify(menuGateway, Mockito.times(1)).getMenuPermissions(user);
		Mockito.verify(userGateway, Mockito.times(1)).getUserProfile(user);
		Mockito.verify(tenantGateway, Mockito.times(1)).getTenantId("laokou");
	}

	@Test
	void test_authorizationCodeAuth() {
		AuthA auth = getAuth("admin", "123", GrantTypeEnum.AUTHORIZATION_CODE, StringConstants.EMPTY,
				StringConstants.EMPTY);
		// 创建用户【授权码】
		Assertions.assertThatNoException().isThrownBy(auth::createUserByAuthorizationCode);
		// 构造租户
		Mockito.when(tenantGateway.getTenantId("laokou")).thenReturn(0L);
		// 构造用户信息
		UserE user = auth.getUser();
		Assertions.assertThatNoException()
			.isThrownBy(() -> user
				.setPassword(DigestUtils.md5DigestAsHex(auth.getPassword().getBytes(StandardCharsets.UTF_8))));
		Mockito.when(userGateway.getUserProfile(user)).thenReturn(user);
		// 构造密码校验
		Mockito.doReturn(true).when(passwordValidator).validatePassword("123", "202cb962ac59075b964b07152d234b70");
		// 构造菜单
		Mockito.when(menuGateway.getMenuPermissions(user)).thenReturn(Set.of("sys:user:page"));
		// 构造部门
		Mockito.when(deptGateway.getDeptPaths(user)).thenReturn(new ArrayList<>(List.of("0", "0,1")));
		// 授权码登录
		Assertions.assertThatNoException().isThrownBy(() -> domainService.auth(auth));
		// 校验调用次数
		Mockito.verify(deptGateway, Mockito.times(1)).getDeptPaths(user);
		Mockito.verify(menuGateway, Mockito.times(1)).getMenuPermissions(user);
		Mockito.verify(passwordValidator, Mockito.times(1)).validatePassword("123", "202cb962ac59075b964b07152d234b70");
		Mockito.verify(userGateway, Mockito.times(1)).getUserProfile(user);
		Mockito.verify(tenantGateway, Mockito.times(1)).getTenantId("laokou");
	}

	@Test
	void test_testAuth() {
		AuthA auth = getAuth("admin", "123", GrantTypeEnum.TEST, StringConstants.EMPTY, StringConstants.EMPTY);
		// 创建用户【测试】
		Assertions.assertThatNoException().isThrownBy(auth::createUserByTest);
		// 构造租户
		Mockito.when(tenantGateway.getTenantId("laokou")).thenReturn(0L);
		// 构造用户信息
		UserE user = auth.getUser();
		Assertions.assertThatNoException()
			.isThrownBy(() -> user
				.setPassword(DigestUtils.md5DigestAsHex(auth.getPassword().getBytes(StandardCharsets.UTF_8))));
		Mockito.when(userGateway.getUserProfile(user)).thenReturn(user);
		// 构造密码校验
		Mockito.doReturn(true).when(passwordValidator).validatePassword("123", "202cb962ac59075b964b07152d234b70");
		// 构造菜单
		Mockito.when(menuGateway.getMenuPermissions(user)).thenReturn(Set.of("sys:user:page"));
		// 构造部门
		Mockito.when(deptGateway.getDeptPaths(user)).thenReturn(new ArrayList<>(List.of("0", "0,1")));
		// 测试登录
		Assertions.assertThatNoException().isThrownBy(() -> domainService.auth(auth));
		// 校验调用次数
		Mockito.verify(deptGateway, Mockito.times(1)).getDeptPaths(user);
		Mockito.verify(menuGateway, Mockito.times(1)).getMenuPermissions(user);
		Mockito.verify(passwordValidator, Mockito.times(1)).validatePassword("123", "202cb962ac59075b964b07152d234b70");
		Mockito.verify(userGateway, Mockito.times(1)).getUserProfile(user);
		Mockito.verify(tenantGateway, Mockito.times(1)).getTenantId("laokou");
	}

	@Test
	void test_createLoginLog() {
		LoginLogE loginLog = DomainFactory.getLoginLog();
		// 创建登录日志
		Assertions.assertThatNoException().isThrownBy(() -> domainService.createLoginLog(loginLog));
	}

	@Test
	void test_createSendCaptchaInfoByMail() {
		// 创建发送验证码信息
		CaptchaE captcha = getCaptcha("2413176044@qq.com", SendCaptchaTypeEnum.SEND_MAIL_CAPTCHA.getCode());
		Assertions.assertThatNoException().isThrownBy(() -> domainService.createSendCaptchaInfo(captcha));
	}

	@Test
	void test_createSendCaptchaInfoByMobile() {
		// 创建发送验证码信息
		CaptchaE captcha = getCaptcha("18888888888", SendCaptchaTypeEnum.SEND_MOBILE_CAPTCHA.getCode());
		Assertions.assertThatNoException().isThrownBy(() -> domainService.createSendCaptchaInfo(captcha));
	}

	@Test
	void test_createNoticeLogByMailCaptcha() {
		// 创建通知日志
		NoticeLogE noticeLog = DomainFactory.getNoticeLog();
		Assertions.assertThatNoException()
			.isThrownBy(() -> noticeLog.setCode(SendCaptchaTypeEnum.SEND_MAIL_CAPTCHA.getCode()));
		Assertions.assertThatNoException().isThrownBy(() -> noticeLog.setStatus(SendCaptchaStatusEnum.OK.getCode()));
		Assertions.assertThatNoException().isThrownBy(() -> domainService.createNoticeLog(noticeLog));
	}

	@Test
	void test_createNoticeLogByMobileCaptcha() {
		// 创建通知日志
		NoticeLogE noticeLog = DomainFactory.getNoticeLog();
		Assertions.assertThatNoException()
			.isThrownBy(() -> noticeLog.setCode(SendCaptchaTypeEnum.SEND_MAIL_CAPTCHA.getCode()));
		Assertions.assertThatNoException().isThrownBy(() -> noticeLog.setStatus(SendCaptchaStatusEnum.OK.getCode()));
		Assertions.assertThatNoException().isThrownBy(() -> domainService.createNoticeLog(noticeLog));
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

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
import org.laokou.auth.ability.DomainService;
import org.laokou.auth.ability.validator.PasswordValidator;
import org.laokou.auth.factory.DomainFactory;
import org.laokou.auth.gateway.*;
import org.laokou.auth.model.*;
import org.laokou.common.i18n.utils.RedisKeyUtil;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;

/**
 * 领域服务测试.
 *
 * @author laokou
 */
@ExtendWith(MockitoExtension.class)
class DomainServiceTest {

	@Mock
	private UserGateway userGateway;

	@Mock
	private MenuGateway menuGateway;

	@Mock
	private DeptGateway deptGateway;

	@Mock
	private TenantGateway tenantGateway;

	@Mock
	private SourceGateway sourceGateway;

	@Mock
	private CaptchaGateway captchaGateway;

	@Mock
	private LoginLogGateway loginLogGateway;

	@Mock
	private NoticeLogGateway noticeLogGateway;

	@Mock
	private PasswordValidator passwordValidator;

	@InjectMocks
	private DomainService domainService;

	private InfoV info;

	@BeforeEach
	void testDomainService() {
		Assertions.assertNotNull(userGateway);
		Assertions.assertNotNull(menuGateway);
		Assertions.assertNotNull(deptGateway);
		Assertions.assertNotNull(tenantGateway);
		Assertions.assertNotNull(sourceGateway);
		Assertions.assertNotNull(captchaGateway);
		Assertions.assertNotNull(loginLogGateway);
		Assertions.assertNotNull(noticeLogGateway);
		Assertions.assertNotNull(passwordValidator);
		Assertions.assertNotNull(domainService);
		this.info = new InfoV("Windows", "127.0.0.1", "中国 广东 深圳", "Chrome");
	}

	@Test
	void testUsernamePasswordAuth() throws Exception {
		AuthA auth = DomainFactory.getUsernamePasswordAuth(1L, "admin", "123", "laokou", "1", "1234");
		// 创建用户【用户名密码】
		auth.createUserByUsernamePassword();
		// 构造租户
		when(tenantGateway.getId("laokou")).thenReturn(0L);
		// 构造数据源
		when(sourceGateway.getPrefix("laokou")).thenReturn("master");
		// 构造验证码校验
		doReturn(true).when(captchaGateway).validate(RedisKeyUtil.getUsernamePasswordAuthCaptchaKey("1"), "1234");
		// 构造用户信息
		UserE user = auth.getUser();
		user.setPassword(DigestUtils.md5DigestAsHex(auth.getPassword().getBytes(StandardCharsets.UTF_8)));
		when(userGateway.getProfile(user, "laokou")).thenReturn(user);
		// 构造密码校验
		doReturn(true).when(passwordValidator).validate("123", "202cb962ac59075b964b07152d234b70");
		// 构造菜单
		when(menuGateway.getPermissions(user)).thenReturn(Set.of("sys:user:page"));
		// 构造部门
		when(deptGateway.getPaths(user)).thenReturn(new ArrayList<>(List.of("0", "0,1")));
		// 用户名密码登录
		domainService.auth(auth, this.info);
		// 校验调用次数
		verify(deptGateway, times(1)).getPaths(user);
		verify(menuGateway, times(1)).getPermissions(user);
		verify(passwordValidator, times(1)).validate("123", "202cb962ac59075b964b07152d234b70");
		verify(userGateway, times(1)).getProfile(user, "laokou");
		verify(captchaGateway, times(1)).validate(RedisKeyUtil.getUsernamePasswordAuthCaptchaKey("1"), "1234");
		verify(sourceGateway, times(1)).getPrefix("laokou");
		verify(tenantGateway, times(1)).getId("laokou");
	}

	@Test
	void testMailAuth() throws Exception {
		AuthA auth = DomainFactory.getMailAuth(1L, "2413176044@qq.com", "123456", "laokou");
		// 创建用户【邮箱】
		auth.createUserByMail();
		// 构造租户
		when(tenantGateway.getId("laokou")).thenReturn(0L);
		// 构造数据源
		when(sourceGateway.getPrefix("laokou")).thenReturn("master");
		// 构造验证码校验
		doReturn(true).when(captchaGateway).validate(RedisKeyUtil.getMailAuthCaptchaKey("2413176044@qq.com"), "123456");
		// 构造用户信息
		UserE user = auth.getUser();
		when(userGateway.getProfile(user, "laokou")).thenReturn(user);
		// 构造菜单
		when(menuGateway.getPermissions(user)).thenReturn(Set.of("sys:user:page"));
		// 构造部门
		when(deptGateway.getPaths(user)).thenReturn(new ArrayList<>(List.of("0", "0,1")));
		// 邮箱登录
		domainService.auth(auth, this.info);
		// 校验调用次数
		verify(deptGateway, times(1)).getPaths(user);
		verify(menuGateway, times(1)).getPermissions(user);
		verify(userGateway, times(1)).getProfile(user, "laokou");
		verify(captchaGateway, times(1)).validate(RedisKeyUtil.getMailAuthCaptchaKey("2413176044@qq.com"), "123456");
		verify(sourceGateway, times(1)).getPrefix("laokou");
		verify(tenantGateway, times(1)).getId("laokou");
	}

	@Test
	void testMobileAuth() throws Exception {
		AuthA auth = DomainFactory.getMobileAuth(1L, "18888888888", "123456", "laokou");
		// 创建用户【手机号】
		auth.createUserByMobile();
		// 构造租户
		when(tenantGateway.getId("laokou")).thenReturn(0L);
		// 构造数据源
		when(sourceGateway.getPrefix("laokou")).thenReturn("master");
		// 构造验证码校验
		doReturn(true).when(captchaGateway).validate(RedisKeyUtil.getMobileAuthCaptchaKey("18888888888"), "123456");
		// 构造用户信息
		UserE user = auth.getUser();
		when(userGateway.getProfile(user, "laokou")).thenReturn(user);
		// 构造菜单
		when(menuGateway.getPermissions(user)).thenReturn(Set.of("sys:user:page"));
		// 构造部门
		when(deptGateway.getPaths(user)).thenReturn(new ArrayList<>(List.of("0", "0,1")));
		// 手机号登录
		domainService.auth(auth, this.info);
		// 校验调用次数
		verify(deptGateway, times(1)).getPaths(user);
		verify(menuGateway, times(1)).getPermissions(user);
		verify(userGateway, times(1)).getProfile(user, "laokou");
		verify(captchaGateway, times(1)).validate(RedisKeyUtil.getMobileAuthCaptchaKey("18888888888"), "123456");
		verify(sourceGateway, times(1)).getPrefix("laokou");
		verify(tenantGateway, times(1)).getId("laokou");
	}

	@Test
	void testAuthorizationCodeAuth() throws Exception {
		AuthA auth = DomainFactory.getAuthorizationCodeAuth(1L, "admin", "123", "laokou");
		// 创建用户【授权码】
		auth.createUserByAuthorizationCode();
		// 构造租户
		when(tenantGateway.getId("laokou")).thenReturn(0L);
		// 构造数据源
		when(sourceGateway.getPrefix("laokou")).thenReturn("master");
		// 构造用户信息
		UserE user = auth.getUser();
		user.setPassword(DigestUtils.md5DigestAsHex(auth.getPassword().getBytes(StandardCharsets.UTF_8)));
		when(userGateway.getProfile(user, "laokou")).thenReturn(user);
		// 构造密码校验
		doReturn(true).when(passwordValidator).validate("123", "202cb962ac59075b964b07152d234b70");
		// 构造菜单
		when(menuGateway.getPermissions(user)).thenReturn(Set.of("sys:user:page"));
		// 构造部门
		when(deptGateway.getPaths(user)).thenReturn(new ArrayList<>(List.of("0", "0,1")));
		// 授权码登录
		domainService.auth(auth, this.info);
		// 校验调用次数
		verify(deptGateway, times(1)).getPaths(user);
		verify(menuGateway, times(1)).getPermissions(user);
		verify(passwordValidator, times(1)).validate("123", "202cb962ac59075b964b07152d234b70");
		verify(userGateway, times(1)).getProfile(user, "laokou");
		verify(sourceGateway, times(1)).getPrefix("laokou");
		verify(tenantGateway, times(1)).getId("laokou");
	}

	@Test
	void testCreateCaptcha() {
		CaptchaE captcha = DomainFactory.getCaptcha();
		AuthA auth = DomainFactory.getAuth(1L, "laokou");
		// 构造租户
		when(tenantGateway.getId("laokou")).thenReturn(0L);
		// 构造数据源
		when(sourceGateway.getPrefix("laokou")).thenReturn("master");
		// 创建验证码
		domainService.createCaptcha(1L, auth, captcha);
		// 校验调用次数
		verify(sourceGateway, times(1)).getPrefix("laokou");
		verify(tenantGateway, times(1)).getId("laokou");
	}

	@Test
	void testCreateLoginLog() {
		LoginLogE loginLog = DomainFactory.getLoginLog();
		// 创建登录日志
		domainService.createLoginLog(loginLog);
	}

	@Test
	void testCreateMailCaptchaNoticeLog() {
		// 创建通知日志
		NoticeLogE noticeLog = DomainFactory.getNoticeLog();
		noticeLog.setCode("sendMailCaptcha");
		noticeLog.setStatus(SendCaptchaStatus.OK.getCode());
		domainService.createNoticeLog(noticeLog);
	}

	@Test
	void testCreateMobileCaptchaNoticeLog() {
		// 创建通知日志
		NoticeLogE noticeLog = DomainFactory.getNoticeLog();
		noticeLog.setCode("sendMobileCaptcha");
		noticeLog.setStatus(SendCaptchaStatus.OK.getCode());
		domainService.createNoticeLog(noticeLog);
	}

}

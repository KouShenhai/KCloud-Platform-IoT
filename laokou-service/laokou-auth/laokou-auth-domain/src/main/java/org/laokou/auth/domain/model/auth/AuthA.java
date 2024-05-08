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

package org.laokou.auth.domain.model.auth;

import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.laokou.auth.domain.event.LoginEvent;
import org.laokou.common.core.utils.*;
import org.laokou.common.i18n.common.constants.EventType;
import org.laokou.common.i18n.common.exception.AuthException;
import org.laokou.common.i18n.dto.AggregateRoot;
import org.laokou.common.i18n.utils.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.laokou.common.i18n.common.constants.EventType.LOGIN_FAILED;
import static org.laokou.common.i18n.common.exception.AuthException.*;
import static org.laokou.common.i18n.common.exception.StatusCode.FORBIDDEN;
import static org.laokou.common.i18n.common.constants.StringConstant.EMPTY;
import static org.laokou.auth.domain.model.auth.UserStatus.DISABLE;

/**
 * 认证聚合.
 *
 * @author laokou
 */
@Getter
public class AuthA extends AggregateRoot<Long> {

	/**
	 * 用户名.
	 */
	private String username;

	/**
	 * 密码.
	 */
	private String password;

	/**
	 * 认证类型 mail邮箱 mobile手机号 password密码 authorization_code授权码.
	 */
	private String grantType;

	/**
	 * 验证码值对象.
	 */
	private CaptchaV captcha;

	/**
	 * 用户实体.
	 */
	private UserE user;

	/**
	 * 菜单实体.
	 */
	private MenuE menu;

	/**
	 * 部门实体.
	 */
	private DeptE dept;

	/**
	 * 日志值对象.
	 */
	private LogV log;

	/**
	 * 登录成功.
	 */
	private final String LOGIN_SUCCEEDED = "OAuth2_LoginSucceeded";

	/**
	 * 邮箱.
	 */
	public static final String MAIL = "mail";

	/**
	 * 手机号.
	 */
	public static final String MOBILE = "mobile";

	/**
	 * 密码.
	 */
	public static final String PASSWORD = "password";

	/**
	 * 授权码.
	 */
	public static final String AUTHORIZATION_CODE = "authorization_code";

	/**
	 * 用户名.
	 */
	public static final String USERNAME = "username";

	/**
	 * 验证码.
	 */
	public static final String CAPTCHA = "captcha";

	/**
	 * UUID.
	 */
	public static final String UUID = "uuid";

	/**
	 * 认证类型.
	 */
	public static final String GRANT_TYPE = "grant_type";

	/**
	 * 验证码.
	 */
	public static final String CODE = "code";

	/**
	 * 租户ID.
	 */
	public static final String TENANT_ID = "tenant_id";

	/**
	 * 成功.
	 */
	private static final int OK = 0;

	/**
	 * 失败.
	 */
	private static final int FAIL = 1;

	/**
	 * 业务标识.
	 */
	public static final String BIZ_ID = "Auth";

	public AuthA() {
	}

	public AuthA(String username, String password, String tenantId, String grantType, String uuid, String captcha,
			HttpServletRequest request) {
		this.id = IdGenerator.defaultSnowflakeId();
		this.username = username;
		this.password = password;
		this.grantType = grantType;
		this.tenantId = StringUtil.isNotEmpty(tenantId) ? Long.parseLong(tenantId) : 0L;
		this.captcha = new CaptchaV(uuid, captcha);
		this.log = createLog(request);
	}

	public void updateAppName(String appName) {
		this.appName = appName;
	}

	public void createUserByPassword() {
		this.user = new UserE(this.username, EMPTY, EMPTY, this.tenantId);
	}

	public void createUserByMobile() {
		this.user = new UserE(EMPTY, EMPTY, this.captcha.uuid(), this.tenantId);
	}

	public void createUserByMail() {
		this.user = new UserE(EMPTY, this.captcha.uuid(), EMPTY, this.tenantId);
	}

	public void createUserByAuthorizationCode() {
		this.user = new UserE(this.username, EMPTY, EMPTY, this.tenantId);
	}

	public void updateUser(UserE user) {
		if (ObjectUtil.isNull(user)) {
			fail(OAUTH2_USERNAME_PASSWORD_ERROR);
		}
		this.user = user;
		this.creator = user.getId();
		this.editor = user.getId();
		this.deptId = user.getDeptId();
		this.deptPath = user.getDeptPath();
		this.tenantId = user.getTenantId();
	}

	public void updateSource(SourceE source) {
		if (ObjectUtil.isNull(source)) {
			fail(OAUTH2_SOURCE_NOT_EXIST);
		}
		this.sourceName = source.getName();
	}

	public void updateMenu(MenuE menu) {
		this.menu = menu;
	}

	public void updateDept(DeptE dept) {
		this.dept = dept;
	}

	public boolean isUseCaptcha() {
		return List.of(PASSWORD, MOBILE, MAIL).contains(grantType);
	}

	public void checkCaptcha(Boolean result) {
		if (ObjectUtil.isNull(result)) {
			fail(OAUTH2_CAPTCHA_EXPIRED);
		}
		if (!result) {
			fail(OAUTH2_CAPTCHA_ERROR);
		}
	}

	public void checkUserPassword(PasswordEncoder passwordEncoder) {
		if (StringUtil.isNotEmpty(this.password) && !passwordEncoder.matches(this.password, user.getPassword())) {
			fail(OAUTH2_USERNAME_PASSWORD_ERROR);
		}
	}

	public void checkUserStatus() {
		if (ObjectUtil.equals(DISABLE.ordinal(), this.user.getStatus())) {
			fail(OAUTH2_USER_DISABLED);
		}
	}

	public void checkMenuPermissions() {
		if (CollectionUtil.isEmpty(this.menu.getPermissions())) {
			fail(FORBIDDEN);
		}
	}

	public void ok() {
		addEvent(new LoginEvent(this, EventType.LOGIN_SUCCEEDED, MessageUtil.getMessage(LOGIN_SUCCEEDED), OK));
	}

	private void fail(String code) {
		String message = MessageUtil.getMessage(code);
		addEvent(new LoginEvent(this, LOGIN_FAILED, message, FAIL));
		throw new AuthException(code, message);
	}

	private LogV createLog(HttpServletRequest request) {
		String ip = IpUtil.getIpAddr(request);
		String address = AddressUtil.getRealAddress(ip);
		UserAgent userAgent = RequestUtil.getUserAgent(request);
		String os = userAgent.getOperatingSystem().getName();
		String browser = userAgent.getBrowser().getName();
		return new LogV(ip, address, browser, os, DateUtil.now());
	}

}

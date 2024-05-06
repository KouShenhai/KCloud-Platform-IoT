/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.laokou.auth.domain.event.LoginEvent;
import org.laokou.common.core.utils.*;
import org.laokou.common.i18n.common.EventTypeEnum;
import org.laokou.common.i18n.common.exception.AuthException;
import org.laokou.common.i18n.dto.AggregateRoot;
import org.laokou.common.i18n.utils.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.laokou.common.i18n.common.EventTypeEnum.LOGIN_FAILED;
import static org.laokou.common.i18n.common.exception.AuthException.*;
import static org.laokou.common.i18n.common.exception.StatusCode.FORBIDDEN;
import static org.laokou.common.i18n.common.StringConstant.EMPTY;
import static org.laokou.auth.domain.model.auth.UserStatus.DISABLE;

/**
 * @author laokou
 */
@Getter
@Schema(name = "AuthA", description = "认证聚合")
public class AuthA extends AggregateRoot<Long> {

	@Schema(name = "username", description = "用户名", example = "admin")
	private String username;

	@Schema(name = "password", description = "密码", example = "admin123")
	private String password;

	@Schema(name = "grantType", description = "类型 mail邮箱 mobile手机号 password密码 authorization_code授权码")
	private String grantType;

	@Schema(name = "captcha", description = "验证码值对象")
	private CaptchaV captcha;

	@Schema(name = "user", description = "用户实体")
	private UserE user;

	@Schema(name = "menu", description = "菜单实体")
	private MenuE menu;

	@Schema(name = "dept", description = "部门实体")
	private DeptE dept;

	@Schema(name = "log", description = "日志")
	private LogV log;

	@Schema(name = "LOGIN_SUCCEEDED", description = "登录成功")
	private final String LOGIN_SUCCEEDED = "OAuth2_LoginSucceeded";

	@Schema(name = "MAIL", description = "邮箱")
	public static final String MAIL = "mail";

	@Schema(name = "MOBILE", description = "手机")
	public static final String MOBILE = "mobile";

	@Schema(name = "PASSWORD", description = "密码")
	public static final String PASSWORD = "password";

	@Schema(name = "AUTHORIZATION_CODE", description = "授权码")
	public static final String AUTHORIZATION_CODE = "authorization_code";

	@Schema(name = "USERNAME", description = "用户名")
	public static final String USERNAME = "username";

	@Schema(name = "CAPTCHA", description = "验证码")
	public static final String CAPTCHA = "captcha";

	@Schema(name = "UUID", description = "UUID")
	public static final String UUID = "uuid";

	@Schema(name = "GRANT_TYPE", description = "认证类型")
	public static final String GRANT_TYPE = "grant_type";

	@Schema(name = "CODE", description = "验证码")
	public static final String CODE = "code";

	@Schema(name = "TENANT_ID", description = "租户ID")
	public static final String TENANT_ID = "tenant_id";

	@Schema(name = "DEFAULT_TENANT", description = "默认租户")
	static final long DEFAULT_TENANT = 0;

	@Schema(name = "OK", description = "成功")
	private static final int OK = 0;

	@Schema(name = "FAIL", description = "失败")
	private static final int FAIL = 1;

	public AuthA() {
	}

	public AuthA(String username, String password, String tenantId, String grantType, String uuid, String captcha,
			HttpServletRequest request) {
		this.id = IdGenerator.defaultSnowflakeId();
		this.username = username;
		this.password = password;
		this.grantType = grantType;
		this.tenantId = StringUtil.isNotEmpty(tenantId) ? Long.parseLong(tenantId) : DEFAULT_TENANT;
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
		addEvent(new LoginEvent(this, EventTypeEnum.LOGIN_SUCCEEDED, MessageUtil.getMessage(LOGIN_SUCCEEDED), OK));
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

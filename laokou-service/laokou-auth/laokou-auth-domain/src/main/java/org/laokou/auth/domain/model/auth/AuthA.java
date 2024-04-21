/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.i18n.common.exception.AuthException;
import org.laokou.common.i18n.dto.AggregateRoot;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.i18n.utils.ValidatorUtil;

import static org.laokou.common.i18n.common.exception.ParamException.OAUTH2_PASSWORD_REQUIRE;
import static org.laokou.common.i18n.common.exception.ParamException.OAUTH2_USERNAME_REQUIRE;

/**
 * @author laokou
 */
@Getter
@Schema(name = "AuthA", description = "认证聚合")
public class AuthA extends AggregateRoot<Long> {

	@Schema(name = "username", description = "用户名")
	private String username;

	@Schema(name = "password", description = "密码", example = "123456")
	private String password;

	@Schema(name = "captcha", description = "验证码值对象")
	private CaptchaV captcha;

	@Schema(name = "user", description = "用户实体")
	private UserE user;

	@Schema(name = "menu", description = "菜单实体")
	private MenuE menu;

	@Schema(name = "source", description = "数据源实体")
	private SourceE source;

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

	@Schema(name = "USERNAME", description = "账号")
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
	private static final Long DEFAULT_TENANT = 0L;

	public AuthA() {
	}

	public AuthA(String username, String password, String tenantId, String type, String uuid, String captcha) {
		this.id = IdGenerator.defaultSnowflakeId();
		this.username = username;
		this.password = password;
		this.tenantId = StringUtil.isNotEmpty(tenantId) ? Long.parseLong(tenantId) : DEFAULT_TENANT;
		this.captcha = new CaptchaV(uuid, type, captcha);
	}

	public UserE createUser() {
		String uuid = this.captcha.uuid();
		return new UserE(this.username, uuid, uuid);
	}

	public String getGrantType() {
		return captcha.type();
	}

	public void checkNullByMail() {
		// 检查租户ID
		checkNullTenantId();
		// 检查验证码
		checkNullCaptcha();
		// 检查邮箱
		captcha.checkMail();
	}

	public void checkNullByMobile() {
		// 检查租户ID
		checkNullTenantId();
		// 检查验证码
		checkNullCaptcha();
		// 检查手机号
		captcha.checkMobile();
	}

	public void checkNullByPassword() {
		// 检查租户ID
		checkNullTenantId();
		// 检查验证码
		checkNullCaptcha();
		// 检查账号
		checkNullUsername();
		// 检查密码
		checkNullPassword();
	}

	private void checkNullCaptcha() {
		// 检查UUID
		captcha.checkNullUuid();
		// 检查验证码
		captcha.checkNullCaptcha();
	}

	private void checkNullPassword() {
		if (StringUtil.isEmpty(this.password)) {
			throw new AuthException(OAUTH2_PASSWORD_REQUIRE, ValidatorUtil.getMessage(OAUTH2_PASSWORD_REQUIRE));
		}
	}

	private void checkNullUsername() {
		if (StringUtil.isEmpty(this.username)) {
			throw new AuthException(OAUTH2_USERNAME_REQUIRE, ValidatorUtil.getMessage(OAUTH2_USERNAME_REQUIRE));
		}
	}

/*

	public AuthA create(AuthA authA, HttpServletRequest request, String sourceName, String appName, String authType) {
		if (ObjectUtil.isNull(authA)) {
			loginFail(ACCOUNT_PASSWORD_ERROR, MessageUtil.getMessage(ACCOUNT_PASSWORD_ERROR), request, sourceName,
					appName, authType);
		}
		return authA;
	}

	public void checkPassword(String clientPassword, PasswordEncoder passwordEncoder, HttpServletRequest request,
			String sourceName, String appName, String authType) {
		if (StringUtil.isNotEmpty(clientPassword) && !passwordEncoder.matches(clientPassword, this.password)) {
			loginFail(ACCOUNT_PASSWORD_ERROR, MessageUtil.getMessage(ACCOUNT_PASSWORD_ERROR), request, sourceName,
					appName, authType);
		}
	}

	public void checkStatus(HttpServletRequest request, String sourceName, String appName, String authType) {
		if (ObjectUtil.equals(DISABLE.ordinal(), this.status)) {
			loginFail(ACCOUNT_DISABLED, MessageUtil.getMessage(ACCOUNT_DISABLED), request, sourceName, appName,
					authType);
		}
	}

	public void checkNullPermissions(Set<String> permissions, HttpServletRequest request, String sourceName,
			String appName, String authType) {
		if (CollectionUtil.isEmpty(permissions)) {
			loginFail(FORBIDDEN, MessageUtil.getMessage(FORBIDDEN), request, sourceName, appName, authType);
		}
	}

	public void checkCaptcha(Boolean checkResult, HttpServletRequest request, String sourceName, String appName,
			String authType) {
		if (ObjectUtil.isNull(checkResult)) {
			loginFail(CAPTCHA_EXPIRED, MessageUtil.getMessage(CAPTCHA_EXPIRED), request, sourceName, appName,
					authType);
		}
		if (!checkResult) {
			loginFail(CAPTCHA_ERROR, MessageUtil.getMessage(CAPTCHA_ERROR), request, sourceName, appName, authType);
		}
	}

	public void loginSuccess(HttpServletRequest request, String sourceName, String appName, String authType) {
		addEvent(new LoginSucceededEvent(this, request, MessageUtil.getMessage(LOGIN_SUCCEEDED), sourceName, appName,
				authType));
	}

	private void loginFail(String code, String message, HttpServletRequest request, String sourceName, String appName,
			String authType) {
		addEvent(new LoginFailedEvent(this, request, message, sourceName, appName, authType));
		throw new AuthException(code, message);
	}*/

}

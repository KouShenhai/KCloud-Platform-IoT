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
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.laokou.auth.domain.event.LoginFailedEvent;
import org.laokou.auth.domain.event.LoginSucceededEvent;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.core.utils.RegexUtil;
import org.laokou.common.i18n.common.exception.AuthException;
import org.laokou.common.i18n.dto.AggregateRoot;
import org.laokou.common.i18n.utils.MessageUtil;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.i18n.utils.ValidatorUtil;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;

import static org.laokou.common.i18n.common.StatusCode.FORBIDDEN;
import static org.laokou.common.i18n.common.SuperAdminEnum.YES;
import static org.laokou.common.i18n.common.UserStatusEnum.DISABLE;
import static org.laokou.common.i18n.common.exception.AuthException.*;
import static org.laokou.common.i18n.common.exception.ParamException.*;

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

	@Schema(name = "type", description = "类型 mail邮箱 mobile手机号 password密码 authorization_code授权码")
	private String type;

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

	public AuthA(String username, String password, Long tenantId, String type, CaptchaV captcha) {
		this.id = IdGenerator.defaultSnowflakeId();
		this.username = username;
		this.password = password;
		this.tenantId = tenantId;
		this.type = type;
		this.captcha = captcha;
	}

	public void checkPasswordAuth() {

	}


	public void checkUsernamePasswordAuth() {
		// 检查租户ID
		checkNullTenantId();
		// 检查UUID
		captchaV.checkNullUuid();
		// 检查验证码
		captchaV.checkNullCaptcha();
		// 检查账号
		checkNullUsername();
		// 检查密码
		checkNullPassword();
	}

	public void checkMailAuth() {
		// 检查租户ID
		checkNullTenantId();
		// 检查验证码
		captchaV.checkNullCaptcha();
		// 检查邮箱
		checkMail();
	}

	public void checkMobileAuth() {
		// 检查租户ID
		checkNullTenantId();
		// 检查验证码
		captchaV.checkNullCaptcha();
		// 检查手机号
		checkMobile();
	}

	public void checkScopes(List<String> scopes) {
		if (CollectionUtil.isNotEmpty(scopes) && scopes.size() != 1) {
			throw new AuthException(INVALID_SCOPE);
		}
	}

	public void checkMobile() {
		if (StringUtil.isEmpty(this.mobile)) {
			throw new AuthException(OAUTH2_MOBILE_REQUIRE, ValidatorUtil.getMessage(OAUTH2_MOBILE_REQUIRE));
		}
		if (!RegexUtil.mobileRegex(this.mobile)) {
			throw new AuthException(MOBILE_ERROR);
		}
	}

	public void checkMail() {
		if (StringUtil.isEmpty(this.mail)) {
			throw new AuthException(OAUTH2_MAIL_REQUIRE, ValidatorUtil.getMessage(OAUTH2_MAIL_REQUIRE));
		}
		if (!RegexUtil.mailRegex(this.mail)) {
			throw new AuthException(MAIL_ERROR, MessageUtil.getMessage(MAIL_ERROR));
		}
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
	}

}

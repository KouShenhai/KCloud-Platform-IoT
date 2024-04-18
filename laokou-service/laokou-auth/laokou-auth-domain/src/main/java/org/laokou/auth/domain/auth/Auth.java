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

package org.laokou.auth.domain.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.laokou.auth.domain.event.LoginFailedEvent;
import org.laokou.auth.domain.event.LoginSucceededEvent;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.core.utils.RegexUtil;
import org.laokou.common.i18n.common.exception.AuthException;
import org.laokou.common.i18n.dto.AggregateRoot;
import org.laokou.common.i18n.utils.MessageUtils;
import org.laokou.common.i18n.utils.ObjectUtils;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.i18n.utils.ValidatorUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static lombok.AccessLevel.PRIVATE;
import static org.laokou.common.i18n.common.StatusCode.FORBIDDEN;
import static org.laokou.common.i18n.common.SuperAdminEnum.YES;
import static org.laokou.common.i18n.common.UserStatusEnum.DISABLE;
import static org.laokou.common.i18n.common.exception.AuthException.*;
import static org.laokou.common.i18n.common.exception.ParamException.*;

/**
 * @author laokou
 */
@Data
@SuperBuilder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PRIVATE)
@Schema(name = "Auth", description = "认证")
public class Auth extends AggregateRoot<Long> {

	@Schema(name = "LOGIN_SUCCEEDED", description = "登录成功")
	private static final String LOGIN_SUCCEEDED = "OAuth2_LoginSucceeded";

	@Schema(name = "username", description = "用户名")
	private String username;

	@Schema(name = "password", description = "密码", example = "123456")
	private String password;

	@Schema(name = "superAdmin", description = "超级管理员标识 0否 1是", example = "1")
	private Integer superAdmin;

	@Schema(name = "avatar", description = "头像", example = "https://pic.cnblogs.com/avatar/simple_avatar.gif")
	private String avatar;

	@Schema(name = "mail", description = "邮箱", example = "2413176044@qq.com")
	private String mail;

	@Schema(name = "status", description = "用户状态 0正常 1锁定", example = "0")
	private Integer status;

	@Schema(name = "mobile", description = "手机号", example = "18974432500")
	private String mobile;

	@Schema(name = "captcha", description = "验证码")
	private Captcha captcha;

	@Schema(name = "secretKey", description = "认证密钥")
	private SecretKey secretKey;

	public void checkUsernamePasswordAuth() {
		// 检查租户ID
		checkNullTenantId();
		// 检查UUID
		captcha.checkNullUuid();
		// 检查验证码
		captcha.checkNullCaptcha();
		// 检查账号
		checkNullUsername();
		// 检查密码
		checkNullPassword();
	}

	public void checkMailAuth() {
		// 检查租户ID
		checkNullTenantId();
		// 检查验证码
		captcha.checkNullCaptcha();
		// 检查邮箱
		checkMail();
	}

	public void checkMobileAuth() {
		// 检查租户ID
		checkNullTenantId();
		// 检查验证码
		captcha.checkNullCaptcha();
		// 检查手机号
		checkMobile();
	}

	public boolean isSuperAdministrator() {
		return ObjectUtils.equals(YES.ordinal(), this.superAdmin);
	}

	public void checkMobile() {
		if (StringUtil.isEmpty(this.mobile)) {
			throw new AuthException(OAUTH2_MOBILE_REQUIRE, ValidatorUtils.getMessage(OAUTH2_MOBILE_REQUIRE));
		}
		if (!RegexUtil.mobileRegex(this.mobile)) {
			throw new AuthException(MOBILE_ERROR);
		}
	}

	public void checkMail() {
		if (StringUtil.isEmpty(this.mail)) {
			throw new AuthException(OAUTH2_MAIL_REQUIRE, ValidatorUtils.getMessage(OAUTH2_MAIL_REQUIRE));
		}
		if (!RegexUtil.mailRegex(this.mail)) {
			throw new AuthException(MAIL_ERROR, MessageUtils.getMessage(MAIL_ERROR));
		}
	}

	private void checkNullPassword() {
		if (StringUtil.isEmpty(this.password)) {
			throw new AuthException(OAUTH2_PASSWORD_REQUIRE, ValidatorUtils.getMessage(OAUTH2_PASSWORD_REQUIRE));
		}
	}

	private void checkNullUsername() {
		if (StringUtil.isEmpty(this.username)) {
			throw new AuthException(OAUTH2_USERNAME_REQUIRE, ValidatorUtils.getMessage(OAUTH2_USERNAME_REQUIRE));
		}
	}

	public Auth create(Auth auth, HttpServletRequest request, String sourceName, String appName, String authType) {
		if (ObjectUtils.isNull(auth)) {
			loginFail(ACCOUNT_PASSWORD_ERROR, MessageUtils.getMessage(ACCOUNT_PASSWORD_ERROR), request, sourceName,
					appName, authType);
		}
		return auth;
	}

	public void checkPassword(String clientPassword, PasswordEncoder passwordEncoder, HttpServletRequest request,
			String sourceName, String appName, String authType) {
		if (StringUtil.isNotEmpty(clientPassword) && !passwordEncoder.matches(clientPassword, this.password)) {
			loginFail(ACCOUNT_PASSWORD_ERROR, MessageUtils.getMessage(ACCOUNT_PASSWORD_ERROR), request, sourceName,
					appName, authType);
		}
	}

	public void checkStatus(HttpServletRequest request, String sourceName, String appName, String authType) {
		if (ObjectUtils.equals(DISABLE.ordinal(), this.status)) {
			loginFail(ACCOUNT_DISABLED, MessageUtils.getMessage(ACCOUNT_DISABLED), request, sourceName, appName,
					authType);
		}
	}

	public void checkNullPermissions(Set<String> permissions, HttpServletRequest request, String sourceName,
			String appName, String authType) {
		if (CollectionUtil.isEmpty(permissions)) {
			loginFail(FORBIDDEN, MessageUtils.getMessage(FORBIDDEN), request, sourceName, appName, authType);
		}
	}

	public void checkCaptcha(Boolean checkResult, HttpServletRequest request, String sourceName, String appName,
			String authType) {
		if (ObjectUtils.isNull(checkResult)) {
			loginFail(CAPTCHA_EXPIRED, MessageUtils.getMessage(CAPTCHA_EXPIRED), request, sourceName, appName,
					authType);
		}
		if (!checkResult) {
			loginFail(CAPTCHA_ERROR, MessageUtils.getMessage(CAPTCHA_ERROR), request, sourceName, appName, authType);
		}
	}

	public void loginSuccess(HttpServletRequest request, String sourceName, String appName, String authType) {
		addEvent(new LoginSucceededEvent(this, request, MessageUtils.getMessage(LOGIN_SUCCEEDED), sourceName, appName,
				authType));
	}

	private void loginFail(String code, String message, HttpServletRequest request, String sourceName, String appName,
			String authType) {
		addEvent(new LoginFailedEvent(this, request, message, sourceName, appName, authType));
		throw new AuthException(code, message);
	}

}

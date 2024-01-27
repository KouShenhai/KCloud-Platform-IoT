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

package org.laokou.auth.domain.user;

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
import org.laokou.common.i18n.utils.MessageUtil;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.i18n.utils.ValidatorUtil;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static lombok.AccessLevel.PROTECTED;
import static org.laokou.common.i18n.common.BizCodes.LOGIN_SUCCEEDED;
import static org.laokou.common.i18n.common.ErrorCodes.*;
import static org.laokou.common.i18n.common.OAuth2Constants.PASSWORD;
import static org.laokou.common.i18n.common.OAuth2Constants.USERNAME;
import static org.laokou.common.i18n.common.StatusCodes.CUSTOM_SERVER_ERROR;
import static org.laokou.common.i18n.common.StatusCodes.FORBIDDEN;
import static org.laokou.common.i18n.common.SuperAdminEnums.YES;
import static org.laokou.common.i18n.common.UserStatusEnums.DISABLE;
import static org.laokou.common.i18n.common.ValCodes.*;

/**
 * @author laokou
 */
@Data
@SuperBuilder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@Schema(name = "User", description = "用户信息")
public class User extends AggregateRoot<Long> {

	@Schema(name = USERNAME, description = "用户名")
	private String username;

	@Schema(name = PASSWORD, description = "密码", example = "123456")
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

	@Schema(name = "auth", description = "认证")
	private Auth auth;

	public boolean isSuperAdministrator() {
		return ObjectUtil.equals(YES.ordinal(), this.superAdmin);
	}

	public void checkMobile() {
		if (StringUtil.isEmpty(this.mobile)) {
			throw new AuthException(CUSTOM_SERVER_ERROR, ValidatorUtil.getMessage(OAUTH2_MOBILE_REQUIRE));
		}
		if (!RegexUtil.mobileRegex(this.mobile)) {
			throw new AuthException(MOBILE_ERROR);
		}
	}

	public void checkMail() {
		if (StringUtil.isEmpty(this.mail)) {
			throw new AuthException(CUSTOM_SERVER_ERROR, ValidatorUtil.getMessage(OAUTH2_MAIL_REQUIRE));
		}
		if (!RegexUtil.mailRegex(this.mail)) {
			throw new AuthException(MAIL_ERROR, MessageUtil.getMessage(MAIL_ERROR));
		}
	}

	public void checkNullPassword() {
		if (StringUtil.isEmpty(this.password)) {
			throw new AuthException(CUSTOM_SERVER_ERROR, ValidatorUtil.getMessage(OAUTH2_PASSWORD_REQUIRE));
		}
	}

	public void checkNullUsername() {
		if (StringUtil.isEmpty(this.username)) {
			throw new AuthException(CUSTOM_SERVER_ERROR, ValidatorUtil.getMessage(OAUTH2_USERNAME_REQUIRE));
		}
	}

	public void checkNull(User user, HttpServletRequest request) {
		if (ObjectUtil.isNull(user)) {
			loginFail(ACCOUNT_PASSWORD_ERROR, MessageUtil.getMessage(ACCOUNT_PASSWORD_ERROR), request);
		}
	}

	public void checkPassword(String clientPassword, PasswordEncoder passwordEncoder, HttpServletRequest request) {
		if (StringUtil.isNotEmpty(clientPassword) && !passwordEncoder.matches(clientPassword, this.password)) {
			loginFail(ACCOUNT_PASSWORD_ERROR, MessageUtil.getMessage(ACCOUNT_PASSWORD_ERROR), request);
		}
	}

	public void checkStatus(HttpServletRequest request) {
		if (ObjectUtil.equals(DISABLE.ordinal(), this.status)) {
			loginFail(ACCOUNT_DISABLE, MessageUtil.getMessage(ACCOUNT_DISABLE), request);
		}
	}

	public void checkNullPermissions(Set<String> permissions, HttpServletRequest request) {
		if (CollectionUtil.isEmpty(permissions)) {
			loginFail(FORBIDDEN, MessageUtil.getMessage(FORBIDDEN), request);
		}
	}

	public void checkCaptcha(Boolean checkResult, HttpServletRequest request) {
		if (ObjectUtil.isNull(checkResult)) {
			loginFail(CAPTCHA_EXPIRED, MessageUtil.getMessage(CAPTCHA_EXPIRED), request);
		}
		if (!checkResult) {
			loginFail(CAPTCHA_ERROR, MessageUtil.getMessage(CAPTCHA_ERROR), request);
		}
	}

	public void loginSuccess(HttpServletRequest request) {
		addEvent(new LoginSucceededEvent(this, request, MessageUtil.getMessage(LOGIN_SUCCEEDED)));
	}

	private void loginFail(int code, String message, HttpServletRequest request) {
		addEvent(new LoginFailedEvent(this, request, message));
		throw new AuthException(code, message);
	}

}

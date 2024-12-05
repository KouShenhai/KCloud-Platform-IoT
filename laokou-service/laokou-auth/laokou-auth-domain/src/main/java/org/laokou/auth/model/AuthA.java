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

package org.laokou.auth.model;

import com.blueconic.browscap.Capabilities;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.laokou.auth.ability.CaptchaValidator;
import org.laokou.auth.ability.PasswordValidator;
import org.laokou.common.core.utils.AddressUtil;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.core.utils.IpUtil;
import org.laokou.common.core.utils.RequestUtil;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.dto.AggregateRoot;
import org.laokou.common.i18n.utils.MessageUtil;
import org.laokou.common.i18n.utils.ObjectUtil;

import java.util.List;

import static org.laokou.auth.model.GrantType.*;
import static org.laokou.common.i18n.common.constant.Constant.FAIL;
import static org.laokou.common.i18n.common.constant.Constant.OK;
import static org.laokou.common.i18n.common.constant.StringConstant.EMPTY;
import static org.laokou.common.i18n.common.exception.StatusCode.FORBIDDEN;
import static org.laokou.common.i18n.common.exception.SystemException.OAuth2.*;

/**
 * 认证聚合.
 *
 * @author laokou
 */
@Getter
public class AuthA extends AggregateRoot {

	/**
	 * 业务用例.
	 */
	public static final String USE_CASE_AUTH = "auth";

	/**
	 * 用户名.
	 */
	private final String username;

	/**
	 * 密码.
	 */
	private final String password;

	/**
	 * 租户编号.
	 */
	private final String tenantCode;

	/**
	 * 认证类型 mail邮箱 mobile手机号 password密码 authorization_code授权码.
	 */
	private final GrantType grantType;

	/**
	 * 验证码值对象.
	 */
	private final CaptchaV captcha;

	/**
	 * 请求对象.
	 */
	private final HttpServletRequest request;

	/**
	 * 用户实体.
	 */
	private UserE user;

	/**
	 * 菜单值对象.
	 */
	private MenuV menu;

	/**
	 * 部门值对象.
	 */
	private DeptV dept;

	/**
	 * 日志值对象.
	 */
	private LogV log;

	/**
	 * 当前用户.
	 */
	private String currentUser;

	public AuthA(String username, String password, String tenantCode, GrantType grantType, String uuid, String captcha,
			HttpServletRequest request) {
		this.username = username;
		this.password = password;
		this.tenantCode = tenantCode;
		this.grantType = grantType;
		this.captcha = new CaptchaV(uuid, captcha);
		this.request = request;
	}

	public void createUserByPassword() {
		currentUser = this.username;
		this.user = new UserE(currentUser, EMPTY, EMPTY);
	}

	public void createUserByMobile() {
		currentUser = this.captcha.uuid();
		this.user = new UserE(EMPTY, EMPTY, currentUser);
	}

	public void createUserByMail() {
		currentUser = this.captcha.uuid();
		this.user = new UserE(EMPTY, currentUser, EMPTY);
	}

	public void createUserByAuthorizationCode() {
		currentUser = this.username;
		this.user = new UserE(currentUser, EMPTY, EMPTY);
	}

	public void checkUserInfo(UserE user) {
		if (ObjectUtil.isNotNull(user)) {
			this.user = user;
			super.tenantId = user.getTenantId();
			super.userId = user.getId();
		}
		else {
			recordFail(grantType.getErrorCode());
		}
	}

	public boolean checkNotEmptyLog() {
		return ObjectUtil.isNotNull(this.log);
	}

	public void checkTenantExist(long count) {
		if (count == 0) {
			recordFail(TENANT_NOT_EXIST);
		}
	}

	public void checkSourcePrefix(SourceV source) {
		if (ObjectUtil.isNull(source)) {
			recordFail(DATA_SOURCE_NOT_EXIST);
		}
	}

	public void checkMenuPermissions(MenuV menu) {
		if (CollectionUtil.isEmpty(menu.permissions())) {
			recordFail(FORBIDDEN);
		}
		this.menu = menu;
	}

	public void checkDeptPaths(DeptV dept) {
		if (CollectionUtil.isEmpty(dept.deptPaths())) {
			recordFail(FORBIDDEN);
		}
		this.dept = dept;
	}

	public void checkCaptcha(CaptchaValidator captchaValidator) {
		if (isUseCaptcha()) {
			Boolean result = captchaValidator.validate(captcha.uuid(), captcha.captcha());
			if (ObjectUtil.isNull(result)) {
				recordFail(CAPTCHA_EXPIRED);
			}
			if (!result) {
				recordFail(CAPTCHA_ERROR);
			}
		}
	}

	public void checkUserPassword(PasswordValidator passwordValidator) {
		if (PASSWORD.equals(this.grantType) && !passwordValidator.validate(this.password, user.getPassword())) {
			recordFail(USERNAME_PASSWORD_ERROR);
		}
	}

	public void checkUserStatus() {
		if (ObjectUtil.equals(UserStatus.DISABLE.ordinal(), this.user.getStatus())) {
			recordFail(USER_DISABLED);
		}
	}

	public void recordSuccess() {
		createLog(OK, EMPTY);
	}

	public void recordFail(String code) {
		String errorMessage = MessageUtil.getMessage(code);
		createLog(FAIL, errorMessage);
		throw new SystemException(code, errorMessage);
	}

	private void createLog(Integer status, String errorMessage) {
		String ip = IpUtil.getIpAddr(request);
		String address = AddressUtil.getRealAddress(ip);
		Capabilities capabilities = RequestUtil.getCapabilities(request);
		String os = capabilities.getPlatform();
		String browser = capabilities.getBrowser();
		this.log = new LogV(currentUser, os, ip, address, browser, status, errorMessage, grantType.getCode());
	}

	private boolean isUseCaptcha() {
		return List.of(PASSWORD, MOBILE, MAIL).contains(grantType);
	}

}

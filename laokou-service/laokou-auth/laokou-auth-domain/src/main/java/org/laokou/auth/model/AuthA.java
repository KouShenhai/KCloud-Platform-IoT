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
import org.laokou.common.core.utils.*;
import org.laokou.common.i18n.common.exception.AuthException;
import org.laokou.common.i18n.dto.AggregateRoot;
import org.laokou.common.i18n.utils.DateUtil;
import org.laokou.common.i18n.utils.MessageUtil;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.laokou.auth.model.GrantType.*;
import static org.laokou.common.i18n.common.constant.Constant.FAIL;
import static org.laokou.common.i18n.common.constant.Constant.OK;
import static org.laokou.common.i18n.common.constant.StringConstant.EMPTY;
import static org.laokou.common.i18n.common.exception.AuthException.*;
import static org.laokou.common.i18n.common.exception.StatusCode.FORBIDDEN;

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
	private GrantType grantType;

	/**
	 * 验证码值对象.
	 */
	private CaptchaV captcha;

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
	 * 请求对象.
	 */
	private HttpServletRequest request;

	/**
	 * 当前用户.
	 */
	private String currentUser;

	public AuthA() {
		super(IdGenerator.defaultSnowflakeId());
	}

	public AuthA(String username, String password, String tenantId, GrantType grantType, String uuid, String captcha,
			HttpServletRequest request) {
		super(IdGenerator.defaultSnowflakeId());
		this.username = username;
		this.password = password;
		this.grantType = grantType;
		// 用于tenantId判空
		this.tenantId = StringUtil.isNotEmpty(tenantId) ? Long.parseLong(tenantId) : null;
		this.captcha = new CaptchaV(uuid, captcha);
		this.request = request;
	}

	public void updateAppName(String appName) {
		this.appName = appName;
	}

	public void createUserByPassword() {
		currentUser = this.username;
		this.user = new UserE(currentUser, EMPTY, EMPTY, this.tenantId);
	}

	public void createUserByMobile() {
		currentUser = this.captcha.uuid();
		this.user = new UserE(EMPTY, EMPTY, currentUser, this.tenantId);
	}

	public void createUserByMail() {
		currentUser = this.captcha.uuid();
		this.user = new UserE(EMPTY, currentUser, EMPTY, this.tenantId);
	}

	public void createUserByAuthorizationCode() {
		currentUser = this.username;
		this.user = new UserE(currentUser, EMPTY, EMPTY, 0L);
	}

	public void updateUser(UserE user) {
		if (ObjectUtil.isNotNull(user)) {
			this.user = user;
			this.creator = user.getId();
			this.editor = user.getId();
			this.deptId = user.getDeptId();
			this.deptPath = user.getDeptPath();
		}
		else {
			fail(grantType.getErrorCode());
		}
	}

	public boolean isHasLog() {
		return ObjectUtil.isNotNull(this.log);
	}

	public void updateSource(SourceV source) {
		if (ObjectUtil.isNull(source)) {
			fail(OAUTH2_SOURCE_NOT_EXIST);
		}
		this.sourceName = source.name();
	}

	public void updateMenu(MenuV menu) {
		this.menu = menu;
	}

	public void updateDept(DeptV dept) {
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
		if (PASSWORD.equals(this.grantType) && !passwordEncoder.matches(this.password, user.getPassword())) {
			fail(OAUTH2_USERNAME_PASSWORD_ERROR);
		}
	}

	public void checkUserStatus() {
		if (ObjectUtil.equals(UserStatus.DISABLE.ordinal(), this.user.getStatus())) {
			fail(OAUTH2_USER_DISABLED);
		}
	}

	public void checkMenuPermissions() {
		if (CollectionUtil.isEmpty(this.menu.permissions())) {
			fail(FORBIDDEN);
		}
	}

	public void ok() {
		createLog(OK, EMPTY);
	}

	private void fail(String code) {
		String errorMessage = MessageUtil.getMessage(code);
		createLog(FAIL, errorMessage);
		throw new AuthException(code, errorMessage);
	}

	private void createLog(Integer status, String errorMessage) {
		String ip = IpUtil.getIpAddr(request);
		String address = AddressUtil.getRealAddress(ip);
		Capabilities capabilities = RequestUtil.getCapabilities(request);
		String os = capabilities.getPlatform();
		String browser = capabilities.getBrowser();
		this.log = new LogV(currentUser, os, ip, address, browser, status, errorMessage, grantType.getCode(),
				DateUtil.nowInstant());
	}

}

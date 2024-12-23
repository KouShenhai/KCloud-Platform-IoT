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

package org.laokou.auth.factory;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.laokou.auth.model.*;

import static org.laokou.common.i18n.common.constant.StringConstant.EMPTY;

/**
 * @author laokou
 */
@Slf4j
public class DomainFactory {

	/**
	 * 邮箱.
	 */
	public static final String MAIL = "mail";

	/**
	 * 手机号.
	 */
	public static final String MOBILE = "mobile";

	/**
	 * 用户名密码.
	 */
	public static final String USERNAME_PASSWORD = "username_password";

	/**
	 * 授权码.
	 */
	public static final String AUTHORIZATION_CODE = "authorization_code";

	/**
	 * 用户名.
	 */
	private static final String USERNAME = "username";

	/**
	 * 密码.
	 */
	private static final String PASSWORD = "password";

	/**
	 * 验证码.
	 */
	private static final String CAPTCHA = "captcha";

	/**
	 * UUID.
	 */
	private static final String UUID = "uuid";

	/**
	 * 验证码.
	 */
	private static final String CODE = "code";

	/**
	 * 租户编号.
	 */
	private static final String TENANT_CODE = "tenant_code";

	public static AuthA getAuth() {
		return new AuthA();
	}

	public static AuthA getMailAuth(Long aggregateId, HttpServletRequest request) {
		String code = request.getParameter(CODE);
		String mail = request.getParameter(MAIL);
		String tenantCode = request.getParameter(TENANT_CODE);
		return new AuthA(aggregateId, EMPTY, EMPTY, tenantCode, GrantType.MAIL, mail, code);
	}

	public static AuthA getMobileAuth(Long aggregateId, HttpServletRequest request) {
		String code = request.getParameter(CODE);
		String mobile = request.getParameter(MOBILE);
		String tenantCode = request.getParameter(TENANT_CODE);
		return new AuthA(aggregateId, EMPTY, EMPTY, tenantCode, GrantType.MOBILE, mobile, code);
	}

	public static AuthA getUsernamePasswordAuth(Long aggregateId, HttpServletRequest request) {
		String uuid = request.getParameter(UUID);
		String captcha = request.getParameter(CAPTCHA);
		String username = request.getParameter(USERNAME);
		String password = request.getParameter(PASSWORD);
		String tenantCode = request.getParameter(TENANT_CODE);
		return new AuthA(aggregateId, username, password, tenantCode, GrantType.USERNAME_PASSWORD, uuid, captcha);
	}

	public static AuthA getAuthorizationCodeAuth(Long aggregateId, HttpServletRequest request) {
		String username = request.getParameter(USERNAME);
		String password = request.getParameter(PASSWORD);
		String tenantCode = request.getParameter(TENANT_CODE);
		return new AuthA(aggregateId, username, password, tenantCode, GrantType.AUTHORIZATION_CODE, EMPTY, EMPTY);
	}

	public static UserE getUser() {
		return new UserE();
	}

	public static LoginLogE getLoginLog() {
		return new LoginLogE();
	}

	public static CaptchaE getCaptcha() {
		return new CaptchaE();
	}

}

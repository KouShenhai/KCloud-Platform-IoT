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
import org.laokou.auth.model.AuthA;
import org.laokou.auth.model.GrantType;

import static org.laokou.common.i18n.common.constant.StringConstant.EMPTY;

/**
 * @author laokou
 */
@Slf4j
public class AuthFactory {

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
	 * 验证码.
	 */
	public static final String CODE = "code";

	/**
	 * 租户ID.
	 */
	public static final String TENANT_ID = "tenant_id";

	public static AuthA mail(HttpServletRequest request) {
		String tenantId = request.getParameter(TENANT_ID);
		String code = request.getParameter(CODE);
		String mail = request.getParameter(MAIL);
		return new AuthA(EMPTY, EMPTY, tenantId, GrantType.MAIL, mail, code, request);
	}

	public static AuthA mobile(HttpServletRequest request) {
		String tenantId = request.getParameter(TENANT_ID);
		String code = request.getParameter(CODE);
		String mobile = request.getParameter(MOBILE);
		return new AuthA(EMPTY, EMPTY, tenantId, GrantType.MOBILE, mobile, code, request);
	}

	public static AuthA password(HttpServletRequest request) {
		String tenantId = request.getParameter(TENANT_ID);
		String uuid = request.getParameter(UUID);
		String captcha = request.getParameter(CAPTCHA);
		String username = request.getParameter(USERNAME);
		String password = request.getParameter(PASSWORD);
		return new AuthA(username, password, tenantId, GrantType.PASSWORD, uuid, captcha, request);
	}

	public static AuthA authorizationCode(HttpServletRequest request) {
		String username = request.getParameter(USERNAME);
		String password = request.getParameter(PASSWORD);
		String tenantId = request.getParameter(TENANT_ID);
		return new AuthA(username, password, tenantId, GrantType.AUTHORIZATION_CODE, EMPTY, EMPTY, request);
	}

}

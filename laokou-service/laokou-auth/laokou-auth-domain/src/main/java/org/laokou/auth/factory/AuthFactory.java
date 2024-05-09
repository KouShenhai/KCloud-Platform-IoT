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

import static org.laokou.auth.model.AuthA.*;
import static org.laokou.common.i18n.common.constants.StringConstant.EMPTY;

/**
 * @author laokou
 */
@Slf4j
public class AuthFactory {

	public static AuthA mail(HttpServletRequest request) {
		String tenantId = request.getParameter(TENANT_ID);
		String code = request.getParameter(CODE);
		String mail = request.getParameter(MAIL);
		// log.info("租户ID：{}", tenantId);
		// log.info("验证码：{}", code);
		// log.info("邮箱：{}", mail);
		return new AuthA(EMPTY, EMPTY, tenantId, MAIL, mail, code, request);
	}

	public static AuthA mobile(HttpServletRequest request) {
		String tenantId = request.getParameter(TENANT_ID);
		String code = request.getParameter(CODE);
		String mobile = request.getParameter(MOBILE);
		// log.info("租户ID：{}", tenantId);
		// log.info("验证码：{}", code);
		// log.info("手机：{}", mobile);
		return new AuthA(EMPTY, EMPTY, tenantId, MOBILE, mobile, code, request);
	}

	public static AuthA password(HttpServletRequest request) {
		String tenantId = request.getParameter(TENANT_ID);
		String uuid = request.getParameter(UUID);
		String captcha = request.getParameter(CAPTCHA);
		String username = request.getParameter(USERNAME);
		String password = request.getParameter(PASSWORD);
		// log.info("UUID：{}", uuid);
		// log.info("验证码：{}", captcha);
		// log.info("用户名：{}", username);
		// log.info("密码：{}", password);
		// log.info("租户ID：{}", tenantId);
		return new AuthA(username, password, tenantId, PASSWORD, uuid, captcha, request);
	}

	public static AuthA authorizationCode(HttpServletRequest request) {
		String username = request.getParameter(USERNAME);
		String password = request.getParameter(PASSWORD);
		// log.info("用户名：{}", username);
		// log.info("密码：{}", password);
		return new AuthA(username, password, EMPTY, AUTHORIZATION_CODE, EMPTY, EMPTY, request);
	}

}

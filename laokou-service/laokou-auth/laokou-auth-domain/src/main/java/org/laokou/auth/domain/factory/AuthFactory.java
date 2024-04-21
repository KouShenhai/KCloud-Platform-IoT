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

package org.laokou.auth.domain.factory;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.laokou.auth.domain.model.auth.AuthA;

import static org.laokou.auth.domain.model.auth.AuthA.*;
import static org.laokou.common.i18n.common.StringConstant.EMPTY;

/**
 * @author laokou
 */
@Slf4j
public class AuthFactory {

	public static AuthA build(HttpServletRequest request) {
		String grantType = request.getParameter(GRANT_TYPE);
		return switch (grantType) {
            case MAIL -> mail(request, grantType);
			case MOBILE -> mobile(request, grantType);
			case PASSWORD -> password(request, grantType);
			case AUTHORIZATION_CODE -> authorizationCode(request, grantType);
			default -> new AuthA();
		};
	}

	private static AuthA mail(HttpServletRequest request, String grantType) {
		String tenantId = request.getParameter(TENANT_ID);
		String code = request.getParameter(CODE);
		String mail = request.getParameter(MAIL);
		// log.info("租户ID：{}", tenantId);
		// log.info("验证码：{}", code);
		// log.info("邮箱：{}", mail);
		return new AuthA(EMPTY, EMPTY, tenantId, grantType, mail, code);
	}

	private static AuthA mobile(HttpServletRequest request, String grantType) {
		String tenantId = request.getParameter(TENANT_ID);
		String code = request.getParameter(CODE);
		String mobile = request.getParameter(MOBILE);
		// log.info("租户ID：{}", tenantId);
		// log.info("验证码：{}", code);
		// log.info("手机：{}", mobile);
		return new AuthA(EMPTY, EMPTY, tenantId, grantType, mobile, code);
	}

	private static AuthA password(HttpServletRequest request, String grantType) {
		String tenantId = request.getParameter(TENANT_ID);
		String uuid = request.getParameter(UUID);
		String captcha = request.getParameter(CAPTCHA);
		String username = request.getParameter(USERNAME);
		String password = request.getParameter(PASSWORD);
		// log.info("UUID：{}", uuid);
		// log.info("验证码：{}", captcha);
		// log.info("账号：{}", username);
		// log.info("密码：{}", password);
		// log.info("租户ID：{}", tenantId);
		return new AuthA(username, password, tenantId, grantType, uuid, captcha);
	}

	private static AuthA authorizationCode(HttpServletRequest request, String grantType) {
		String username = request.getParameter(USERNAME);
		String password = request.getParameter(PASSWORD);
		// log.info("账号：{}", username);
		// log.info("密码：{}", password);
		return new AuthA(username, password, EMPTY, grantType, EMPTY, EMPTY);
	}

}

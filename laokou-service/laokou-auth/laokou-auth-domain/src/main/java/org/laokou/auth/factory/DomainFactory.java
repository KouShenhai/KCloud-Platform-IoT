/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

import lombok.extern.slf4j.Slf4j;
import org.laokou.auth.model.*;

import static org.laokou.common.i18n.common.constant.StringConstants.EMPTY;

/**
 * @author laokou
 */
@Slf4j
public final class DomainFactory {

	/**
	 * 邮箱.
	 */
	public static final String MAIL = "mail";

	/**
	 * 手机号.
	 */
	public static final String MOBILE = "mobile";

	/**
	 * 测试.
	 */
	public static final String TEST = "test";

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
	public static final String USERNAME = "username";

	/**
	 * 密码.
	 */
	public static final String PASSWORD = "password";

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
	 * 租户编码.
	 */
	public static final String TENANT_CODE = "tenant_code";

	private DomainFactory() {
	}

	public static AuthA getAuth(Long aggregateId, String tenantCode) {
		return new AuthA(aggregateId, tenantCode);
	}

	public static AuthA getMailAuth(Long aggregateId, String mail, String code, String tenantCode) {
		return new AuthA(aggregateId, EMPTY, EMPTY, tenantCode, GrantTypeEnum.MAIL, mail, code);
	}

	public static AuthA getMobileAuth(Long aggregateId, String mobile, String code, String tenantCode) {
		return new AuthA(aggregateId, EMPTY, EMPTY, tenantCode, GrantTypeEnum.MOBILE, mobile, code);
	}

	public static AuthA getUsernamePasswordAuth(Long aggregateId, String username, String password, String tenantCode,
			String uuid, String captcha) {
		return new AuthA(aggregateId, username, password, tenantCode, GrantTypeEnum.USERNAME_PASSWORD, uuid, captcha);
	}

	public static AuthA getAuthorizationCodeAuth(Long aggregateId, String username, String password,
			String tenantCode) {
		return new AuthA(aggregateId, username, password, tenantCode, GrantTypeEnum.AUTHORIZATION_CODE, EMPTY, EMPTY);
	}

	public static AuthA getTestAuth(Long aggregateId, String username, String password, String tenantCode) {
		return new AuthA(aggregateId, username, password, tenantCode, GrantTypeEnum.TEST, EMPTY, EMPTY);
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

	public static NoticeLogE getNoticeLog() {
		return new NoticeLogE();
	}

}

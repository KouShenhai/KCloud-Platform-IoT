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

import lombok.Getter;
import org.laokou.auth.factory.AuthFactory;

import static org.laokou.common.i18n.common.exception.SystemException.*;
import static org.laokou.common.i18n.common.exception.SystemException.OAuth2.*;

/**
 * @author laokou
 */
@Getter
public enum GrantType {

	/**
	 * 用户名密码.
	 */
	PASSWORD(AuthFactory.PASSWORD) {
		@Override
		public String getErrorCode() {
			return USERNAME_PASSWORD_ERROR;
		}
	},

	/**
	 * 授权码.
	 */
	AUTHORIZATION_CODE(AuthFactory.AUTHORIZATION_CODE) {
		@Override
		public String getErrorCode() {
			return USERNAME_PASSWORD_ERROR;
		}
	},

	/**
	 * 手机号.
	 */
	MOBILE(AuthFactory.MOBILE) {
		@Override
		public String getErrorCode() {
			return MOBILE_NOT_REGISTERED;
		}
	},

	/**
	 * 邮箱.
	 */
	MAIL(AuthFactory.MAIL) {
		@Override
		public String getErrorCode() {
			return MAIL_NOT_REGISTERED;
		}
	};

	private final String code;

	GrantType(String code) {
		this.code = code;
	}

	public abstract String getErrorCode();

}

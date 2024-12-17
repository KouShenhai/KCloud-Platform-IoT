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
import org.laokou.auth.factory.DomainFactory;
import org.laokou.common.i18n.common.exception.SystemException;

import static org.laokou.common.i18n.common.exception.SystemException.OAuth2.*;

/**
 * @author laokou
 */
@Getter
public enum GrantType {

	/**
	 * 用户名密码.
	 */
	USERNAME_PASSWORD(DomainFactory.USERNAME_PASSWORD) {
		@Override
		public void checkUsernameNotExist() {
			throw new SystemException(USERNAME_PASSWORD_ERROR);
		}
	},

	/**
	 * 授权码.
	 */
	AUTHORIZATION_CODE(DomainFactory.AUTHORIZATION_CODE) {
		@Override
		public void checkUsernameNotExist() {
			throw new SystemException(USERNAME_PASSWORD_ERROR);
		}
	},

	/**
	 * 手机号.
	 */
	MOBILE(DomainFactory.MOBILE) {
		@Override
		public void checkUsernameNotExist() {
			throw new SystemException(MOBILE_NOT_REGISTERED);
		}
	},

	/**
	 * 邮箱.
	 */
	MAIL(DomainFactory.MAIL) {
		@Override
		public void checkUsernameNotExist() {
			throw new SystemException(MAIL_NOT_REGISTERED);
		}
	};

	private final String code;

	GrantType(String code) {
		this.code = code;
	}

	public abstract void checkUsernameNotExist();

}

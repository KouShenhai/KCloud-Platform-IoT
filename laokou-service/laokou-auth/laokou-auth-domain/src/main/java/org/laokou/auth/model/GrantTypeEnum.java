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

package org.laokou.auth.model;

import lombok.Getter;
import org.laokou.common.i18n.common.exception.BizException;
/**
 * 登录类型枚举.
 *
 * @author laokou
 */
@Getter
public enum GrantTypeEnum {

	// @formatter:off
	USERNAME_PASSWORD(Constants.USERNAME_PASSWORD, "用户名密码登录") {
		@Override
		public void checkUsernameNotExist() {
			throw new BizException(OAuth2Constants.USERNAME_PASSWORD_ERROR);
		}
	},

	AUTHORIZATION_CODE(Constants.AUTHORIZATION_CODE, "授权码登录") {
		@Override
		public void checkUsernameNotExist() {
			throw new BizException(OAuth2Constants.USERNAME_PASSWORD_ERROR);
		}
	},

	MOBILE(Constants.MOBILE, "手机号登录") {
		@Override
		public void checkUsernameNotExist() {
			throw new BizException(OAuth2Constants.MOBILE_NOT_REGISTERED);
		}
	},

	MAIL(Constants.MAIL, "邮箱登录") {
		@Override
		public void checkUsernameNotExist() {
			throw new BizException(OAuth2Constants.MAIL_NOT_REGISTERED);
		}
	},

	TEST(Constants.TEST, "测试登录") {
		@Override
		public void checkUsernameNotExist() {
			throw new BizException(OAuth2Constants.USERNAME_PASSWORD_ERROR);
		}
	};

	private final String code;

	private final String desc;

	GrantTypeEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public abstract void checkUsernameNotExist();
	// @formatter:on

}

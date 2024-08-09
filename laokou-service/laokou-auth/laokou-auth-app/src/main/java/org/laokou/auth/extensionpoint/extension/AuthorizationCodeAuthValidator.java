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

package org.laokou.auth.extensionpoint.extension;

import org.laokou.auth.extensionpoint.AuthValidatorExtPt;
import org.laokou.auth.model.AuthA;
import org.laokou.common.extension.Extension;
import org.laokou.common.i18n.common.exception.AuthException;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.i18n.utils.ValidatorUtil;

import static org.laokou.auth.common.constant.Constant.SCENARIO;
import static org.laokou.auth.common.constant.Constant.USE_CASE_AUTH;
import static org.laokou.auth.factory.AuthFactory.AUTHORIZATION_CODE;
import static org.laokou.common.i18n.common.exception.ParamException.OAUTH2_PASSWORD_REQUIRE;
import static org.laokou.common.i18n.common.exception.ParamException.OAUTH2_USERNAME_REQUIRE;

/**
 * @author laokou
 */
@Extension(bizId = AUTHORIZATION_CODE, useCase = USE_CASE_AUTH, scenario = SCENARIO)
public class AuthorizationCodeAuthValidator implements AuthValidatorExtPt {

	@Override
	public void validate(AuthA auth) {
		// 用户名判空
		if (StringUtil.isEmpty(auth.getUsername())) {
			throw new AuthException(OAUTH2_USERNAME_REQUIRE, ValidatorUtil.getMessage(OAUTH2_USERNAME_REQUIRE));
		}
		// 密码判空
		if (StringUtil.isEmpty(auth.getPassword())) {
			throw new AuthException(OAUTH2_PASSWORD_REQUIRE, ValidatorUtil.getMessage(OAUTH2_PASSWORD_REQUIRE));
		}
	}

}

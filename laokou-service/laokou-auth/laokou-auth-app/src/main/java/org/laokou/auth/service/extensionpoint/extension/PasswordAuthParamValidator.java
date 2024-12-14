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

package org.laokou.auth.service.extensionpoint.extension;

import org.laokou.auth.service.extensionpoint.AuthParamValidatorExtPt;
import org.laokou.auth.model.AuthA;
import org.laokou.common.extension.Extension;

import static org.laokou.auth.common.utils.ParamValidatorUtil.validateNotEmpty;
import static org.laokou.auth.factory.DomainFactory.PASSWORD;
import static org.laokou.auth.model.AuthA.USE_CASE_AUTH;
import static org.laokou.common.i18n.common.constant.Constant.SCENARIO;
import static org.laokou.common.i18n.common.exception.ParamException.OAuth2.*;

/**
 * @author laokou
 */
@Extension(bizId = PASSWORD, useCase = USE_CASE_AUTH, scenario = SCENARIO)
public class PasswordAuthParamValidator implements AuthParamValidatorExtPt {

	@Override
	public void validate(AuthA auth) {
		// 租户编号判空
		validateNotEmpty(auth.getTenantCode(), TENANT_CODE_REQUIRE);
		// UUID判空
		validateNotEmpty(auth.getCaptcha().uuid(), UUID_REQUIRE);
		// 验证码判空
		validateNotEmpty(auth.getCaptcha().captcha(), CAPTCHA_REQUIRE);
		// 用户名判空
		validateNotEmpty(auth.getUsername(), USERNAME_REQUIRE);
		// 密码判空
		validateNotEmpty(auth.getPassword(), PASSWORD_REQUIRE);
	}

}

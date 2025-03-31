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

package org.laokou.auth.service.extensionpoint.extension;

import org.laokou.auth.model.AuthA;
import org.laokou.auth.service.extensionpoint.AuthParamValidatorExtPt;
import org.laokou.common.i18n.util.ParamValidator;
import org.laokou.common.extension.Extension;

import static org.laokou.auth.common.constant.BizConstants.SCENARIO;
import static org.laokou.auth.common.constant.BizConstants.USE_CASE_AUTH;
import static org.laokou.auth.factory.DomainFactory.USERNAME_PASSWORD;

/**
 * @author laokou
 */
@Extension(bizId = USERNAME_PASSWORD, useCase = USE_CASE_AUTH, scenario = SCENARIO)
public class UsernamePasswordAuthParamValidator implements AuthParamValidatorExtPt {

	@Override
	public void validate(AuthA auth) {
		ParamValidator.validate(
				// 校验租户编码
				OAuth2ParamValidator.validateTenantCode(auth.getTenantCode()),
				// 校验UUID
				OAuth2ParamValidator.validateUuid(auth.getCaptcha().uuid()),
				// 校验验证码
				OAuth2ParamValidator.validateCaptcha(auth.getCaptcha().captcha()),
				// 校验用户名
				OAuth2ParamValidator.validateUsername(auth.getUsername()),
				// 校验密码
				OAuth2ParamValidator.validatePassword(auth.getPassword()));
	}

}

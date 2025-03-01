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

package org.laokou.admin.user.service.extensionpoint.extension;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.user.gatewayimpl.database.UserMapper;
import org.laokou.admin.user.model.UserE;
import org.laokou.admin.user.service.extensionpoint.UserParamValidatorExtPt;
import org.laokou.common.i18n.utils.ParamValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Component("resetPwdParamValidator")
@RequiredArgsConstructor
public class ResetPwdParamValidator implements UserParamValidatorExtPt {

	private final PasswordEncoder passwordEncoder;

	private final UserMapper userMapper;

	@Override
	public void validate(UserE userE) throws Exception {
		ParamValidator.validate(
				// 校验ID
				UserParamValidator.validateId(userE),
				// 校验密码
				UserParamValidator.validatePassword(userE, passwordEncoder, userMapper));
	}

}

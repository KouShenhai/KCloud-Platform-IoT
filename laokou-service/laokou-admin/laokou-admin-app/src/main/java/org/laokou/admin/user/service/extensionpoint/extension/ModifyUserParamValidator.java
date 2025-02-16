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

import org.laokou.admin.user.gatewayimpl.database.UserMapper;
import org.laokou.admin.user.model.UserE;
import org.laokou.admin.user.service.extensionpoint.UserParamValidatorExtPt;
import org.laokou.common.extension.Extension;
import org.laokou.common.i18n.utils.ParamValidator;

import static org.laokou.admin.common.constant.Constant.*;
import static org.laokou.common.i18n.common.constant.Constant.SCENARIO;

/**
 * @author laokou
 */
@Extension(bizId = MODIFY, useCase = USER, scenario = SCENARIO)
public class ModifyUserParamValidator implements UserParamValidatorExtPt {

	@Override
	public void validate(UserE userE, UserMapper userMapper) {
		ParamValidator.validate(
				// 校验ID
				UserParamValidator.validateId(userE),
				// 校验用户名
				UserParamValidator.validateUserName(userE, userMapper, false),
				// 校验邮箱
				UserParamValidator.validateMail(userE, userMapper, false),
				// 校验手机号
				UserParamValidator.validateMobile(userE, userMapper, false),
				// 校验角色IDS
				UserParamValidator.validateRoleIds(userE),
				// 校验部门IDS
				UserParamValidator.validateDeptIds(userE));
	}

}

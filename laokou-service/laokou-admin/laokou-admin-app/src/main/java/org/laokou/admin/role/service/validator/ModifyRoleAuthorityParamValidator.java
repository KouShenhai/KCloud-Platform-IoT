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

package org.laokou.admin.role.service.validator;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.role.model.RoleE;
import org.laokou.admin.role.model.RoleParamValidator;
import org.laokou.common.i18n.util.ParamValidator;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Component("modifyRoleAuthorityParamValidator")
@RequiredArgsConstructor
public class ModifyRoleAuthorityParamValidator implements RoleParamValidator {

	@Override
	public void validateRole(RoleE roleE) {
		ParamValidator.validate(
				// 校验ID
				org.laokou.admin.role.service.validator.RoleParamValidator.validateId(roleE),
				// 校验数据范围
				org.laokou.admin.role.service.validator.RoleParamValidator.validateDataScope(roleE),
				// 校验菜单IDS
				org.laokou.admin.role.service.validator.RoleParamValidator.validateMenuIds(roleE),
				// 校验部门IDS
				org.laokou.admin.role.service.validator.RoleParamValidator.validateDeptIds(roleE));
	}

}

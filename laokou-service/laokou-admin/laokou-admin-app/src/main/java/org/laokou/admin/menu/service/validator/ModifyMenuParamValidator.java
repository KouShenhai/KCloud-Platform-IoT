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

package org.laokou.admin.menu.service.validator;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.menu.gatewayimpl.database.MenuMapper;
import org.laokou.admin.menu.model.MenuE;
import org.laokou.admin.menu.model.MenuParamValidator;
import org.laokou.common.i18n.util.ParamValidator;

/**
 * @author laokou
 */
// @Component("modifyMenuParamValidator")
@RequiredArgsConstructor
public class ModifyMenuParamValidator implements MenuParamValidator {

	private final MenuMapper menuMapper;

	@Override
	public void validateMenu(MenuE menuE) {
		ParamValidator.validate(
				// 校验ID
				org.laokou.admin.menu.service.validator.MenuParamValidator.validateId(menuE),
				// 校验父级ID
				org.laokou.admin.menu.service.validator.MenuParamValidator.validateParentId(menuE),
				// 校验类型
				org.laokou.admin.menu.service.validator.MenuParamValidator.validateType(menuE),
				// 校验名称
				org.laokou.admin.menu.service.validator.MenuParamValidator.validateName(menuE, menuMapper, false),
				// 校验路径
				org.laokou.admin.menu.service.validator.MenuParamValidator.validatePath(menuE, menuMapper, false),
				// 校验权限标识
				org.laokou.admin.menu.service.validator.MenuParamValidator.validatePermission(menuE, menuMapper, false),
				// 校验状态
				org.laokou.admin.menu.service.validator.MenuParamValidator.validateStatus(menuE),
				// 校验排序
				org.laokou.admin.menu.service.validator.MenuParamValidator.validateSort(menuE));
	}

}

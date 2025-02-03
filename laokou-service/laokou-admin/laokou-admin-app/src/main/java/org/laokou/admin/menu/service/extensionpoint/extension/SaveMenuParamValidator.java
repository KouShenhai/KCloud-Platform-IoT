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

package org.laokou.admin.menu.service.extensionpoint.extension;

import org.laokou.admin.menu.model.MenuE;
import org.laokou.admin.menu.service.extensionpoint.MenuParamValidatorExtPt;
import org.laokou.common.extension.Extension;
import org.laokou.common.i18n.utils.ParamValidator;

import static org.laokou.admin.common.constant.Constant.MENU;
import static org.laokou.admin.common.constant.Constant.SAVE;
import static org.laokou.common.i18n.common.constant.Constant.SCENARIO;

/**
 * @author laokou
 */
@Extension(bizId = SAVE, useCase = MENU, scenario = SCENARIO)
public class SaveMenuParamValidator implements MenuParamValidatorExtPt {

	@Override
	public void validate(MenuE menuE) {
		ParamValidator.validate(
				// 校验父级ID
				MenuParamValidator.validateParentId(menuE),
				// 校验类型
				MenuParamValidator.validateType(menuE),
				// 校验名称
				MenuParamValidator.validateName(menuE, true),
				// 校验路径
				MenuParamValidator.validatePath(menuE),
				// 校验权限标识
				MenuParamValidator.validatePermission(menuE, true),
				// 校验状态
				MenuParamValidator.validateStatus(menuE),
				// 校验排序
				MenuParamValidator.validateSort(menuE));
	}

}

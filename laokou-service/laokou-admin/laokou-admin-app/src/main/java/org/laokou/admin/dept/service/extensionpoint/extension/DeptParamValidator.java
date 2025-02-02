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

package org.laokou.admin.dept.service.extensionpoint.extension;

import org.laokou.admin.dept.model.DeptE;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.i18n.utils.ParamValidator;
import org.laokou.common.i18n.utils.StringUtil;

import static org.laokou.common.i18n.utils.ParamValidator.invalidate;
import static org.laokou.common.i18n.utils.ParamValidator.validate;

/**
 * @author laokou
 */
public final class DeptParamValidator {

	private DeptParamValidator() {
	}

	public static ParamValidator.Validate validateParentId(DeptE deptE) {
		Long pid = deptE.getPid();
		if (ObjectUtil.isNull(pid)) {
			return invalidate("父级ID不能为空");
		}
		return validate();
	}

	public static ParamValidator.Validate validateSort(DeptE deptE) {
		Integer sort = deptE.getSort();
		if (ObjectUtil.isNull(sort)) {
			return invalidate("排序不能为空");
		}
		if (sort < 1 || sort > 99999) {
			return invalidate("排序范围1-99999");
		}
		return validate();
	}

	public static ParamValidator.Validate validateId(DeptE deptE) {
		Long id = deptE.getId();
		if (ObjectUtil.isNull(id)) {
			return invalidate("ID不能为空");
		}
		return validate();
	}

	public static ParamValidator.Validate validateName(DeptE deptE) {
		String name = deptE.getName();
		if (StringUtil.isEmpty(name)) {
			return invalidate("名称不能为空");
		}
		return validate();
	}

}

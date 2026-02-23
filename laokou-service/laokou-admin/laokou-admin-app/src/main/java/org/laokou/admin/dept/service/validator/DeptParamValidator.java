/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.admin.dept.service.validator;

import org.laokou.admin.dept.model.DeptA;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.ParamValidator;
import org.laokou.common.i18n.util.StringExtUtils;

/**
 * @author laokou
 */
final class DeptParamValidator {

	private DeptParamValidator() {
	}

	public static ParamValidator.Validate validateParentId(DeptA deptA) {
		Long pid = deptA.getDeptE().getPid();
		if (ObjectUtils.isNull(pid)) {
			return ParamValidator.invalidate("部门父级ID不能为空");
		}
		return ParamValidator.validate();
	}

	public static ParamValidator.Validate validateSort(DeptA deptA) {
		Integer sort = deptA.getDeptE().getSort();
		if (ObjectUtils.isNull(sort)) {
			return ParamValidator.invalidate("部门排序不能为空");
		}
		if (sort < 1 || sort > 99999) {
			return ParamValidator.invalidate("部门排序范围1-99999");
		}
		return ParamValidator.validate();
	}

	public static ParamValidator.Validate validateId(DeptA deptA) {
		Long id = deptA.getDeptE().getId();
		if (deptA.isModify() && ObjectUtils.isNull(id)) {
			return ParamValidator.invalidate("部门ID不能为空");
		}
		return ParamValidator.validate();
	}

	public static ParamValidator.Validate validateName(DeptA deptA) {
		String name = deptA.getDeptE().getName();
		if (StringExtUtils.isEmpty(name)) {
			return ParamValidator.invalidate("部门名称不能为空");
		}
		return ParamValidator.validate();
	}

}

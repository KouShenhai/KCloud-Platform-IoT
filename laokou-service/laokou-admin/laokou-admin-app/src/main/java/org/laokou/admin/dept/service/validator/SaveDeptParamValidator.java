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

package org.laokou.admin.dept.service.validator;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.dept.model.DeptE;
import org.laokou.admin.dept.model.DeptParamValidator;
import org.laokou.common.i18n.util.ParamValidator;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Component("saveDeptParamValidator")
@RequiredArgsConstructor
public class SaveDeptParamValidator implements DeptParamValidator {

	@Override
	public void validateDept(DeptE deptE) {
		ParamValidator.validate(
				// 校验父级ID
				org.laokou.admin.dept.service.validator.DeptParamValidator.validateParentId(deptE),
				// 校验名称
				org.laokou.admin.dept.service.validator.DeptParamValidator.validateName(deptE),
				// 校验排序
				org.laokou.admin.dept.service.validator.DeptParamValidator.validateSort(deptE));
	}

}

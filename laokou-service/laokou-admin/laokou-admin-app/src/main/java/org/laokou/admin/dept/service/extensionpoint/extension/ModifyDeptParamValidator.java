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
import org.laokou.admin.dept.service.extensionpoint.DeptParamValidatorExtPt;
import org.laokou.common.extension.Extension;
import org.laokou.common.i18n.utils.ParamValidator;

import static org.laokou.admin.common.constant.Constant.DEPT;
import static org.laokou.admin.common.constant.Constant.MODIFY;
import static org.laokou.common.i18n.common.constant.Constant.SCENARIO;

/**
 * @author laokou
 */
@Extension(bizId = MODIFY, useCase = DEPT, scenario = SCENARIO)
public class ModifyDeptParamValidator implements DeptParamValidatorExtPt {

	@Override
	public void validate(DeptE deptE) {
		ParamValidator.validate(
				// 校验ID
				DeptParamValidator.validateId(deptE),
				// 校验父级ID
				DeptParamValidator.validateParentId(deptE),
				// 校验名称
				DeptParamValidator.validateName(deptE),
				// 校验排序
				DeptParamValidator.validateSort(deptE));
	}

}

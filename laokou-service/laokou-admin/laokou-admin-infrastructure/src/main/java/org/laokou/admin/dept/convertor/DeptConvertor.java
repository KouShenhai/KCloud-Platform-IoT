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

package org.laokou.admin.dept.convertor;

import org.laokou.admin.dept.dto.clientobject.DeptCO;
import org.laokou.admin.dept.gatewayimpl.database.dataobject.DeptDO;
import org.laokou.admin.dept.model.DeptE;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.utils.ObjectUtil;

/**
 * 部门转换器.
 *
 * @author laokou
 */
public class DeptConvertor {

	public static DeptDO toDataObject(DeptE deptE) {
		DeptDO deptDO = ConvertUtil.sourceToTarget(deptE, DeptDO.class);
		if (ObjectUtil.isNull(deptDO.getId())) {
			deptDO.generatorId();
		}
		return deptDO;
	}

	public static DeptCO toClientObject(DeptDO deptDO) {
		return ConvertUtil.sourceToTarget(deptDO, DeptCO.class);
	}

	public static DeptE toEntity(DeptCO deptCO) {
		return ConvertUtil.sourceToTarget(deptCO, DeptE.class);
	}

}

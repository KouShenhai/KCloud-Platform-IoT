/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.admin.convertor;

import org.laokou.admin.domain.dept.Dept;
import org.laokou.admin.dto.dept.clientobject.DeptCO;
import org.laokou.admin.gatewayimpl.database.dataobject.DeptDO;

/**
 * @author laokou
 */
public class DeptConvertor {

	public static Dept toEntity(DeptCO co) {
		Dept dept = new Dept();
		dept.setId(co.getId());
		dept.setName(co.getName());
		dept.setPid(co.getPid());
		dept.setSort(co.getSort());
		return dept;
	}

	public static DeptCO toClientObject(DeptDO deptDO) {
		DeptCO deptCO = new DeptCO();
		deptCO.setId(deptDO.getId());
		deptCO.setName(deptDO.getName());
		deptCO.setPid(deptDO.getPid());
		deptCO.setSort(deptDO.getSort());
		return deptCO;
	}

	public static DeptDO toDataObject(Dept dept) {
		DeptDO deptDO = new DeptDO();
		deptDO.setId(dept.getId());
		deptDO.setCreator(dept.getCreator());
		deptDO.setEditor(dept.getEditor());
		deptDO.setCreateTime(dept.getCreateTime());
		deptDO.setUpdateTime(dept.getUpdateTime());
		deptDO.setDeptId(dept.getDeptId());
		deptDO.setDeptPath(dept.getDeptPath());
		deptDO.setTenantId(dept.getTenantId());
		deptDO.setPid(dept.getPid());
		deptDO.setName(dept.getName());
		deptDO.setPath(dept.getPath());
		deptDO.setSort(dept.getSort());
		return deptDO;
	}

}

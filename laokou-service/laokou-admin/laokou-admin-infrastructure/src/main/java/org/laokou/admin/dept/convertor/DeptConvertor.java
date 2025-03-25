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
import org.laokou.admin.dept.dto.clientobject.DeptTreeCO;
import org.laokou.admin.dept.gatewayimpl.database.dataobject.DeptDO;
import org.laokou.admin.dept.model.DeptE;
import org.laokou.common.core.util.IdGenerator;

import java.util.List;

/**
 * 部门转换器.
 *
 * @author laokou
 */
public class DeptConvertor {

	public static DeptDO toDataObject(DeptE deptE) {
		DeptDO deptDO = new DeptDO();
		deptDO.setId(IdGenerator.defaultSnowflakeId());
		deptDO.setPid(deptE.getPid());
		deptDO.setName(deptE.getName());
		deptDO.setPath(deptE.getPath());
		deptDO.setSort(deptE.getSort());
		return deptDO;
	}

	public static DeptCO toClientObject(DeptDO deptDO) {
		DeptCO deptCO = new DeptCO();
		deptCO.setId(deptDO.getId());
		deptCO.setPid(deptDO.getPid());
		deptCO.setName(deptDO.getName());
		deptCO.setPath(deptDO.getPath());
		deptCO.setSort(deptDO.getSort());
		deptCO.setCreateTime(deptDO.getCreateTime());
		return deptCO;
	}

	public static List<DeptCO> toClientObjects(List<DeptDO> list) {
		return list.stream().map(DeptConvertor::toClientObject).toList();
	}

	public static DeptE toEntity(DeptCO deptCO) {
		DeptE deptE = new DeptE();
		deptE.setId(deptCO.getId());
		deptE.setPid(deptCO.getPid());
		deptE.setName(deptCO.getName());
		deptE.setPath(deptCO.getPath());
		deptE.setSort(deptCO.getSort());
		return deptE;
	}

	public static DeptTreeCO toClientObj(DeptDO deptDO) {
		DeptTreeCO co = new DeptTreeCO();
		co.setId(deptDO.getId());
		co.setName(deptDO.getName());
		co.setPid(deptDO.getPid());
		co.setPath(deptDO.getPath());
		co.setSort(deptDO.getSort());
		co.setCreateTime(deptDO.getCreateTime());
		return co;

	}

	public static List<DeptTreeCO> toClientObjs(List<DeptDO> list) {
		return list.stream().map(DeptConvertor::toClientObj).toList();
	}

}

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

package org.laokou.admin.dept.gatewayimpl;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.dept.convertor.DeptConvertor;
import org.laokou.admin.dept.gateway.DeptGateway;
import org.laokou.admin.dept.gatewayimpl.database.DeptMapper;
import org.laokou.admin.dept.gatewayimpl.database.dataobject.DeptDO;
import org.laokou.admin.dept.model.DeptE;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 部门网关实现.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class DeptGatewayImpl implements DeptGateway {

	private final DeptMapper deptMapper;

	@Override
	public void create(DeptE deptE) {
		DeptDO deptDO = DeptConvertor.toDataObject(deptE);
		// 校验父级路径
		checkParentPath(deptE, deptDO.getId());
		deptDO.setPath(deptE.getPath());
		deptMapper.insert(deptDO);
	}

	@Override
	public void update(DeptE deptE) {
		Long id = deptE.getId();
		DeptDO deptDO = getDeptDO(deptE, id);
		// 获取旧路径
		deptE.getOldPath(deptDO.getPath());
		// 校验旧路径
		deptE.checkOldPath();
		// 校验父级路径
		checkParentPath(deptE, id);
		deptDO.setPath(deptE.getPath());
		deptMapper.updateById(deptDO);
		deptMapper.updateChildrenPath(deptE.getOldPath(), deptE.getOldPrefix(), deptE.getNewPrefix());
	}

	@Override
	public void delete(Long[] ids) {
		deptMapper.deleteByIds(Arrays.asList(ids));
	}

	private DeptDO getDeptDO(DeptE deptE, Long id) {
		DeptDO deptDO = deptMapper.selectById(id);
		deptDO.setPid(deptE.getPid());
		deptDO.setName(deptE.getName());
		deptDO.setSort(deptE.getSort());
		return deptDO;
	}

	/**
	 * 校验父级路径.
	 */
	private void checkParentPath(DeptE deptE, Long id) {
		// 获取父级路径
		deptE.getParentPath(deptMapper.selectParentPathById(deptE.getPid()));
		// 校验父级路径
		deptE.checkParentPath(id);
	}

}

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
		deptMapper.insert(DeptConvertor.toDataObject(deptE));
	}

	@Override
	public void update(DeptE deptE) {
		DeptDO deptDO = DeptConvertor.toDataObject(deptE);
		deptDO.setVersion(deptMapper.selectVersion(deptE.getId()));
		deptMapper.updateById(deptDO);
	}

	@Override
	public void delete(Long[] ids) {
		deptMapper.deleteByIds(Arrays.asList(ids));
	}

}

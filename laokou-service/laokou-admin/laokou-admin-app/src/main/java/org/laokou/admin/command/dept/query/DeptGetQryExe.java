/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.admin.command.dept.query;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.dto.dept.DeptGetQry;
import org.laokou.admin.dto.dept.clientobject.DeptCO;
import org.laokou.admin.gatewayimpl.database.DeptRepository;
import org.laokou.admin.gatewayimpl.database.dataobject.DeptDO;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import static org.laokou.common.i18n.common.DatasourceConstant.TENANT;

/**
 * 查看部门执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class DeptGetQryExe {

	private final DeptRepository deptMapper;

	/**
	 * 执行查看部门.
	 * @param qry 查看部门参数
	 * @return 部门
	 */
	@DS(TENANT)
	public Result<DeptCO> execute(DeptGetQry qry) {
		return Result.of(convert(deptMapper.selectById(qry.getId())));
	}

	private DeptCO convert(DeptDO deptDO) {
		return DeptCO.builder()
			.path(deptDO.getPath())
			.sort(deptDO.getSort())
			.id(deptDO.getId())
			.pid(deptDO.getPid())
			.name(deptDO.getName())
			.build();
	}

}

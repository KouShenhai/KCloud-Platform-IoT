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
import org.laokou.admin.dto.dept.DeptIdsGetQry;
import org.laokou.admin.gatewayimpl.database.DeptRepository;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.laokou.common.i18n.common.DatasourceConstant.TENANT;

/**
 * 查看部门IDS执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class DeptIdsGetQryExe {

	private final DeptRepository deptMapper;

	/**
	 * 执行查看部门IDS.
	 * @param qry 查看部门IDS参数
	 * @return 部门IDS
	 */
	@DS(TENANT)
	public Result<List<Long>> execute(DeptIdsGetQry qry) {
		return Result.of(deptMapper.selectIdsByRoleId(qry.getRoleId()));
	}

}

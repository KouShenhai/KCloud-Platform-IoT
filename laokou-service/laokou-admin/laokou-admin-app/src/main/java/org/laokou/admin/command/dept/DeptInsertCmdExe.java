/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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

package org.laokou.admin.command.dept;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.dto.dept.DeptInsertCmd;
import org.laokou.admin.convertor.DeptConvertor;
import org.laokou.admin.domain.gateway.DeptGateway;
import org.laokou.admin.dto.dept.clientobject.DeptCO;
import org.laokou.admin.gatewayimpl.database.DeptMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.DeptDO;
import org.laokou.common.i18n.common.GlobalException;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.security.utils.UserUtil;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class DeptInsertCmdExe {

	private final DeptGateway deptGateway;

	private final DeptMapper deptMapper;

	public Result<Boolean> execute(DeptInsertCmd cmd) {
		DeptCO deptCO = cmd.getDeptCO();
		long count = deptMapper.selectCount(Wrappers.lambdaQuery(DeptDO.class)
			.eq(DeptDO::getTenantId, UserUtil.getTenantId())
			.eq(DeptDO::getName, deptCO.getName()));
		if (count > 0) {
			throw new GlobalException("部门已存在，请重新填写");
		}
		return Result.of(deptGateway.insert(DeptConvertor.toEntity(deptCO)));
	}

}

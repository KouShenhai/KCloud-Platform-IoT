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

package org.laokou.admin.command.role.query;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.dto.role.RoleGetQry;
import org.laokou.admin.dto.role.clientobject.RoleCO;
import org.laokou.admin.gatewayimpl.database.RoleMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.RoleDO;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import static org.laokou.common.i18n.common.DatasourceConstant.TENANT;

/**
 * 查看角色执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class RoleGetQryExe {

	private final RoleMapper roleMapper;

	/**
	 * 执行查看角色.
	 * @param qry 查看角色参数
	 * @return 角色
	 */
	@DS(TENANT)
	public Result<RoleCO> execute(RoleGetQry qry) {
		return Result.ok(convert(roleMapper.selectById(qry.getId())));
	}

	private RoleCO convert(RoleDO roleDO) {
		return RoleCO.builder().id(roleDO.getId()).sort(roleDO.getSort()).name(roleDO.getName()).build();
	}

}

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
import org.laokou.admin.convertor.RoleConvertor;
import org.laokou.admin.domain.gateway.RoleGateway;
import org.laokou.admin.domain.role.Role;
import org.laokou.admin.dto.role.RoleListQry;
import org.laokou.admin.dto.role.clientobject.RoleCO;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import static org.laokou.common.i18n.common.DatasourceConstants.TENANT;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class RoleListQryExe {

	private final RoleGateway roleGateway;

	private final RoleConvertor roleConvertor;

	@DS(TENANT)
	public Result<Datas<RoleCO>> execute(RoleListQry qry) {
		Role role = ConvertUtil.sourceToTarget(qry, Role.class);
		Datas<Role> datas = roleGateway.list(role, qry);
		Datas<RoleCO> newDatas = new Datas<>();
		newDatas.setTotal(datas.getTotal());
		newDatas.setRecords(roleConvertor.convertClientObjectList(datas.getRecords()));
		return Result.of(newDatas);
	}

}

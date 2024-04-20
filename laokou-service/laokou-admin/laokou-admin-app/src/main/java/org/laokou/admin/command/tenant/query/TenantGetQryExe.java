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

package org.laokou.admin.command.tenant.query;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.dto.tenant.TenantGetQry;
import org.laokou.admin.dto.tenant.clientobject.TenantCO;
import org.laokou.admin.gatewayimpl.database.TenantMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.TenantDO;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

/**
 * 查看租户执行器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class TenantGetQryExe {

	private final TenantMapper tenantMapper;

	/**
	 * 执行查看租户.
	 * @param qry 查看租户参数
	 * @return 租户
	 */
	public Result<TenantCO> execute(TenantGetQry qry) {
		return Result.of(convert(tenantMapper.selectById(qry.getId())));
	}

	private TenantCO convert(TenantDO tenantDO) {
		return TenantCO.builder()
			.id(tenantDO.getId())
			.label(tenantDO.getLabel())
			.name(tenantDO.getName())
			.packageId(tenantDO.getPackageId())
			.sourceId(tenantDO.getSourceId())
			.build();
	}

}

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

package org.laokou.admin.command.tenant.query;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.domain.gateway.TenantGateway;
import org.laokou.admin.domain.tenant.Tenant;
import org.laokou.admin.dto.tenant.TenantListQry;
import org.laokou.admin.dto.tenant.clientobject.TenantCO;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class TenantListQryExe {

	private final TenantGateway tenantGateway;

	public Result<Datas<TenantCO>> execute(TenantListQry qry) {
		Tenant tenant = ConvertUtil.sourceToTarget(qry, Tenant.class);
		Datas<Tenant> newPage = tenantGateway.list(tenant, qry);
		Datas<TenantCO> datas = new Datas<>();
		datas.setTotal(newPage.getTotal());
		datas.setRecords(ConvertUtil.sourceToTarget(newPage.getRecords(), TenantCO.class));
		return Result.of(datas);
	}

}

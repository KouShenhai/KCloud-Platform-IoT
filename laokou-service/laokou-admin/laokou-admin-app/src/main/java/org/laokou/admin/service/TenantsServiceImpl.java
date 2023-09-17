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

package org.laokou.admin.service;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.api.TenantsServiceI;
import org.laokou.admin.dto.common.clientobject.OptionCO;
import org.laokou.admin.dto.tenant.*;
import org.laokou.admin.dto.tenant.clientobject.TenantCO;
import org.laokou.admin.command.tenant.TenantDeleteCmdExe;
import org.laokou.admin.command.tenant.TenantInsertCmdExe;
import org.laokou.admin.command.tenant.TenantUpdateCmdExe;
import org.laokou.admin.command.tenant.query.TenantGetQryExe;
import org.laokou.admin.command.tenant.query.TenantListQryExe;
import org.laokou.admin.command.tenant.query.TenantOptionListQryExe;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class TenantsServiceImpl implements TenantsServiceI {

	private final TenantInsertCmdExe tenantInsertCmdExe;

	private final TenantOptionListQryExe tenantOptionListQryExe;

	private final TenantUpdateCmdExe tenantUpdateCmdExe;

	private final TenantDeleteCmdExe tenantDeleteCmdExe;

	private final TenantListQryExe tenantListQryExe;

	private final TenantGetQryExe tenantGetQryExe;

	@Override
	public Result<List<OptionCO>> optionList(TenantOptionListQry qry) {
		return tenantOptionListQryExe.execute(qry);
	}

	@Override
	public Result<Boolean> insert(TenantInsertCmd cmd) {
		return tenantInsertCmdExe.execute(cmd);
	}

	@Override
	public Result<Boolean> update(TenantUpdateCmd cmd) {
		return tenantUpdateCmdExe.execute(cmd);
	}

	@Override
	public Result<Boolean> deleteById(TenantDeleteCmd cmd) {
		return tenantDeleteCmdExe.execute(cmd);
	}

	@Override
	public Result<Datas<TenantCO>> list(TenantListQry qry) {
		return tenantListQryExe.execute(qry);
	}

	@Override
	public Result<TenantCO> getById(TenantGetQry qry) {
		return tenantGetQryExe.execute(qry);
	}

}

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

package org.laokou.admin.tenant.service;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.tenant.api.TenantsServiceI;
import org.laokou.admin.tenant.command.*;
import org.laokou.admin.tenant.command.query.TenantGetQryExe;
import org.laokou.admin.tenant.command.query.TenantPageQryExe;
import org.laokou.admin.tenant.dto.*;
import org.laokou.admin.tenant.dto.clientobject.TenantCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

/**
 * 租户接口实现类.
 *
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class TenantsServiceImpl implements TenantsServiceI {

	private final TenantSaveCmdExe tenantSaveCmdExe;

	private final TenantModifyCmdExe tenantModifyCmdExe;

	private final TenantRemoveCmdExe tenantRemoveCmdExe;

	private final TenantImportCmdExe tenantImportCmdExe;

	private final TenantExportCmdExe tenantExportCmdExe;

	private final TenantPageQryExe tenantPageQryExe;

	private final TenantGetQryExe tenantGetQryExe;

	@Override
	public void save(TenantSaveCmd cmd) {
		tenantSaveCmdExe.executeVoid(cmd);
	}

	@Override
	public void modify(TenantModifyCmd cmd) {
		tenantModifyCmdExe.executeVoid(cmd);
	}

	@Override
	public void remove(TenantRemoveCmd cmd) {
		tenantRemoveCmdExe.executeVoid(cmd);
	}

	@Override
	public void importI(TenantImportCmd cmd) {
		tenantImportCmdExe.executeVoid(cmd);
	}

	@Override
	public void export(TenantExportCmd cmd) {
		tenantExportCmdExe.executeVoid(cmd);
	}

	@Override
	public Result<Page<TenantCO>> page(TenantPageQry qry) {
		return tenantPageQryExe.execute(qry);
	}

	@Override
	public Result<TenantCO> getById(TenantGetQry qry) {
		return tenantGetQryExe.execute(qry);
	}

}

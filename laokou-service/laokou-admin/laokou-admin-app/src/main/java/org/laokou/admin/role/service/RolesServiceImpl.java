/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.admin.role.service;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.role.api.RolesServiceI;
import org.laokou.admin.role.command.*;
import org.laokou.admin.role.command.query.RoleGetQryExe;
import org.laokou.admin.role.command.query.RolePageQryExe;
import org.laokou.admin.role.dto.*;
import org.laokou.admin.role.dto.clientobject.RoleCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

/**
 * 角色接口实现类.
 *
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class RolesServiceImpl implements RolesServiceI {

	private final RoleSaveCmdExe roleSaveCmdExe;

	private final RoleModifyCmdExe roleModifyCmdExe;

	private final RoleRemoveCmdExe roleRemoveCmdExe;

	private final RoleImportCmdExe roleImportCmdExe;

	private final RoleExportCmdExe roleExportCmdExe;

	private final RolePageQryExe rolePageQryExe;

	private final RoleGetQryExe roleGetQryExe;

	@Override
	public void save(RoleSaveCmd cmd) {
		roleSaveCmdExe.executeVoid(cmd);
	}

	@Override
	public void modify(RoleModifyCmd cmd) {
		roleModifyCmdExe.executeVoid(cmd);
	}

	@Override
	public void remove(RoleRemoveCmd cmd) {
		roleRemoveCmdExe.executeVoid(cmd);
	}

	@Override
	public void importI(RoleImportCmd cmd) {
		roleImportCmdExe.executeVoid(cmd);
	}

	@Override
	public void export(RoleExportCmd cmd) {
		roleExportCmdExe.executeVoid(cmd);
	}

	@Override
	public Result<Page<RoleCO>> page(RolePageQry qry) {
		return rolePageQryExe.execute(qry);
	}

	@Override
	public Result<RoleCO> getById(RoleGetQry qry) {
		return roleGetQryExe.execute(qry);
	}

}

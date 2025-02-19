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

package org.laokou.admin.ip.service;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.ip.api.IpsServiceI;
import org.laokou.admin.ip.command.*;
import org.laokou.admin.ip.command.query.IpGetQryExe;
import org.laokou.admin.ip.command.query.IpPageQryExe;
import org.laokou.admin.ip.dto.*;
import org.laokou.admin.ip.dto.clientobject.IpCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

/**
 * IP接口实现类.
 *
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class IpsServiceImpl implements IpsServiceI {

	private final IpSaveCmdExe ipSaveCmdExe;

	private final IpModifyCmdExe ipModifyCmdExe;

	private final IpRemoveCmdExe ipRemoveCmdExe;

	private final IpImportCmdExe ipImportCmdExe;

	private final IpExportCmdExe ipExportCmdExe;

	private final IpPageQryExe ipPageQryExe;

	private final IpGetQryExe ipGetQryExe;

	@Override
	public void save(IpSaveCmd cmd) {
		ipSaveCmdExe.executeVoid(cmd);
	}

	@Override
	public void modify(IpModifyCmd cmd) {
		ipModifyCmdExe.executeVoid(cmd);
	}

	@Override
	public void remove(IpRemoveCmd cmd) {
		ipRemoveCmdExe.executeVoid(cmd);
	}

	@Override
	public void importI(IpImportCmd cmd) {
		ipImportCmdExe.executeVoid(cmd);
	}

	@Override
	public void export(IpExportCmd cmd) {
		ipExportCmdExe.executeVoid(cmd);
	}

	@Override
	public Result<Page<IpCO>> page(IpPageQry qry) {
		return ipPageQryExe.execute(qry);
	}

	@Override
	public Result<IpCO> getById(IpGetQry qry) {
		return ipGetQryExe.execute(qry);
	}

}

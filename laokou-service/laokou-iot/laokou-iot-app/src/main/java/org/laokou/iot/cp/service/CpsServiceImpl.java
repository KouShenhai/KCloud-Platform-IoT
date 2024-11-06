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

package org.laokou.iot.cp.service;

import lombok.RequiredArgsConstructor;
import org.laokou.iot.cp.api.CpsServiceI;
import org.laokou.iot.cp.command.*;
import org.laokou.iot.cp.command.query.CpGetQryExe;
import org.laokou.iot.cp.command.query.CpPageQryExe;
import org.laokou.iot.cp.dto.*;
import org.laokou.iot.cp.dto.clientobject.CpCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

/**
 *
 * 通讯协议接口实现类.
 *
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class CpsServiceImpl implements CpsServiceI {

	private final CpSaveCmdExe cpSaveCmdExe;

	private final CpModifyCmdExe cpModifyCmdExe;

	private final CpRemoveCmdExe cpRemoveCmdExe;

	private final CpImportCmdExe cpImportCmdExe;

	private final CpExportCmdExe cpExportCmdExe;

	private final CpPageQryExe cpPageQryExe;

	private final CpGetQryExe cpGetQryExe;

	@Override
	public void save(CpSaveCmd cmd) {
		cpSaveCmdExe.executeVoid(cmd);
	}

	@Override
	public void modify(CpModifyCmd cmd) {
		cpModifyCmdExe.executeVoid(cmd);
	}

	@Override
	public void remove(CpRemoveCmd cmd) {
		cpRemoveCmdExe.executeVoid(cmd);
	}

	@Override
	public void importI(CpImportCmd cmd) {
		cpImportCmdExe.executeVoid(cmd);
	}

	@Override
	public void export(CpExportCmd cmd) {
		cpExportCmdExe.executeVoid(cmd);
	}

	@Override
	public Result<Page<CpCO>> page(CpPageQry qry) {
		return cpPageQryExe.execute(qry);
	}

	@Override
	public Result<CpCO> getById(CpGetQry qry) {
		return cpGetQryExe.execute(qry);
	}

}

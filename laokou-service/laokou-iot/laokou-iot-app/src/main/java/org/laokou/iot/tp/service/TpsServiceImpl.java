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

package org.laokou.iot.tp.service;

import lombok.RequiredArgsConstructor;
import org.laokou.iot.tp.api.TpsServiceI;
import org.laokou.iot.tp.command.*;
import org.laokou.iot.tp.command.query.TpGetQryExe;
import org.laokou.iot.tp.command.query.TpPageQryExe;
import org.laokou.iot.tp.dto.*;
import org.laokou.iot.tp.dto.clientobject.TpCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

/**
 *
 * 传输协议接口实现类.
 *
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class TpsServiceImpl implements TpsServiceI {

	private final TpSaveCmdExe tpSaveCmdExe;

	private final TpModifyCmdExe tpModifyCmdExe;

	private final TpRemoveCmdExe tpRemoveCmdExe;

	private final TpImportCmdExe tpImportCmdExe;

	private final TpExportCmdExe tpExportCmdExe;

	private final TpPageQryExe tpPageQryExe;

	private final TpGetQryExe tpGetQryExe;

	@Override
	public void save(TpSaveCmd cmd) {
		tpSaveCmdExe.executeVoid(cmd);
	}

	@Override
	public void modify(TpModifyCmd cmd) {
		tpModifyCmdExe.executeVoid(cmd);
	}

	@Override
	public void remove(TpRemoveCmd cmd) {
		tpRemoveCmdExe.executeVoid(cmd);
	}

	@Override
	public void importI(TpImportCmd cmd) {
		tpImportCmdExe.executeVoid(cmd);
	}

	@Override
	public void export(TpExportCmd cmd) {
		tpExportCmdExe.executeVoid(cmd);
	}

	@Override
	public Result<Page<TpCO>> page(TpPageQry qry) {
		return tpPageQryExe.execute(qry);
	}

	@Override
	public Result<TpCO> getById(TpGetQry qry) {
		return tpGetQryExe.execute(qry);
	}

}

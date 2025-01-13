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

package org.laokou.admin.operateLog.service;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.operateLog.api.OperateLogsServiceI;
import org.laokou.admin.operateLog.command.*;
import org.laokou.admin.operateLog.command.query.OperateLogGetQryExe;
import org.laokou.admin.operateLog.command.query.OperateLogPageQryExe;
import org.laokou.admin.operateLog.dto.*;
import org.laokou.admin.operateLog.dto.clientobject.OperateLogCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

/**
 * 操作日志接口实现类.
 *
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class OperateLogsServiceImpl implements OperateLogsServiceI {

	private final OperateLogSaveCmdExe operateLogSaveCmdExe;

	private final OperateLogModifyCmdExe operateLogModifyCmdExe;

	private final OperateLogRemoveCmdExe operateLogRemoveCmdExe;

	private final OperateLogImportCmdExe operateLogImportCmdExe;

	private final OperateLogExportCmdExe operateLogExportCmdExe;

	private final OperateLogPageQryExe operateLogPageQryExe;

	private final OperateLogGetQryExe operateLogGetQryExe;

	@Override
	public void save(OperateLogSaveCmd cmd) {
		operateLogSaveCmdExe.executeVoid(cmd);
	}

	@Override
	public void modify(OperateLogModifyCmd cmd) {
		operateLogModifyCmdExe.executeVoid(cmd);
	}

	@Override
	public void remove(OperateLogRemoveCmd cmd) {
		operateLogRemoveCmdExe.executeVoid(cmd);
	}

	@Override
	public void importI(OperateLogImportCmd cmd) {
		operateLogImportCmdExe.executeVoid(cmd);
	}

	@Override
	public void export(OperateLogExportCmd cmd) {
		operateLogExportCmdExe.executeVoid(cmd);
	}

	@Override
	public Result<Page<OperateLogCO>> page(OperateLogPageQry qry) {
		return operateLogPageQryExe.execute(qry);
	}

	@Override
	public Result<OperateLogCO> getById(OperateLogGetQry qry) {
		return operateLogGetQryExe.execute(qry);
	}

}

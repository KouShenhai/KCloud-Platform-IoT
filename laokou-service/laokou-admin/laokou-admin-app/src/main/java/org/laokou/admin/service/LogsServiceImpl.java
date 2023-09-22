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
import org.laokou.admin.api.LogsServiceI;
import org.laokou.admin.dto.log.LoginLogExportCmd;
import org.laokou.admin.dto.log.LoginLogListQry;
import org.laokou.admin.dto.log.OperateLogExportCmd;
import org.laokou.admin.dto.log.OperateLogListQry;
import org.laokou.admin.dto.log.clientobject.LoginLogCO;
import org.laokou.admin.dto.log.clientobject.OperateLogCO;
import org.laokou.admin.command.log.LoginLogExportCmdExe;
import org.laokou.admin.command.log.OperateLogExportCmdExe;
import org.laokou.admin.command.log.query.LoginLogListQryExe;
import org.laokou.admin.command.log.query.OperateLogListQryExe;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

/**
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class LogsServiceImpl implements LogsServiceI {

	private final OperateLogListQryExe operateLogListQryExe;

	private final OperateLogExportCmdExe operateLogExportCmdExe;

	private final LoginLogListQryExe loginLogListQryExe;

	private final LoginLogExportCmdExe loginLogExportCmdExe;

	@Override
	public Result<Datas<OperateLogCO>> operateList(OperateLogListQry qry) {
		return operateLogListQryExe.execute(qry);
	}

	@Override
	public void operateExport(OperateLogExportCmd cmd) {
		operateLogExportCmdExe.executeVoid(cmd);
	}

	@Override
	public Result<Datas<LoginLogCO>> loginList(LoginLogListQry qry) {
		return loginLogListQryExe.execute(qry);
	}

	@Override
	public void loginExport(LoginLogExportCmd cmd) {
		loginLogExportCmdExe.executeVoid(cmd);
	}

}

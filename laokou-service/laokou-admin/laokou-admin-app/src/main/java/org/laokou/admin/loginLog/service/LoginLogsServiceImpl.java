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

package org.laokou.admin.loginLog.service;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.loginLog.api.LoginLogsServiceI;
import org.laokou.admin.loginLog.command.*;
import org.laokou.admin.loginLog.command.query.LoginLogGetQryExe;
import org.laokou.admin.loginLog.command.query.LoginLogPageQryExe;
import org.laokou.admin.loginLog.dto.*;
import org.laokou.admin.loginLog.dto.clientobject.LoginLogCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

/**
 * 登录日志接口实现类.
 *
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class LoginLogsServiceImpl implements LoginLogsServiceI {

	private final LoginLogSaveCmdExe loginLogSaveCmdExe;

	private final LoginLogModifyCmdExe loginLogModifyCmdExe;

	private final LoginLogRemoveCmdExe loginLogRemoveCmdExe;

	private final LoginLogImportCmdExe loginLogImportCmdExe;

	private final LoginLogExportCmdExe loginLogExportCmdExe;

	private final LoginLogPageQryExe loginLogPageQryExe;

	private final LoginLogGetQryExe loginLogGetQryExe;

	private final LoginLogTruncateCmdExe loginLogTruncateCmdExe;

	@Override
	public void save(LoginLogSaveCmd cmd) {
		loginLogSaveCmdExe.executeVoid(cmd);
	}

	@Override
	public void modify(LoginLogModifyCmd cmd) {
		loginLogModifyCmdExe.executeVoid(cmd);
	}

	@Override
	public void remove(LoginLogRemoveCmd cmd) {
		loginLogRemoveCmdExe.executeVoid(cmd);
	}

	@Override
	public void importI(LoginLogImportCmd cmd) {
		loginLogImportCmdExe.executeVoid(cmd);
	}

	@Override
	public void export(LoginLogExportCmd cmd) {
		loginLogExportCmdExe.executeVoid(cmd);
	}

	@Override
	public Result<Page<LoginLogCO>> page(LoginLogPageQry qry) {
		return loginLogPageQryExe.execute(qry);
	}

	@Override
	public Result<LoginLogCO> getById(LoginLogGetQry qry) {
		return loginLogGetQryExe.execute(qry);
	}

	@Override
	public void truncate() {
		loginLogTruncateCmdExe.executeVoid();
	}

}

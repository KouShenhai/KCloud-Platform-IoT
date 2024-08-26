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

package org.laokou.admin.apiLog.service;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.apiLog.api.ApiLogsServiceI;
import org.laokou.admin.apiLog.command.*;
import org.laokou.admin.apiLog.command.query.ApiLogGetQryExe;
import org.laokou.admin.apiLog.command.query.ApiLogPageQryExe;
import org.laokou.admin.apiLog.dto.*;
import org.laokou.admin.apiLog.dto.clientobject.ApiLogCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

/**
 * Api日志接口实现类.
 *
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class ApiLogsServiceImpl implements ApiLogsServiceI {

	private final ApiLogSaveCmdExe apiLogSaveCmdExe;

	private final ApiLogModifyCmdExe apiLogModifyCmdExe;

	private final ApiLogRemoveCmdExe apiLogRemoveCmdExe;

	private final ApiLogImportCmdExe apiLogImportCmdExe;

	private final ApiLogExportCmdExe apiLogExportCmdExe;

	private final ApiLogPageQryExe apiLogPageQryExe;

	private final ApiLogGetQryExe apiLogGetQryExe;

	@Override
	public void save(ApiLogSaveCmd cmd) {
		apiLogSaveCmdExe.executeVoid(cmd);
	}

	@Override
	public void modify(ApiLogModifyCmd cmd) {
		apiLogModifyCmdExe.executeVoid(cmd);
	}

	@Override
	public void remove(ApiLogRemoveCmd cmd) {
		apiLogRemoveCmdExe.executeVoid(cmd);
	}

	@Override
	public void importI(ApiLogImportCmd cmd) {
		apiLogImportCmdExe.executeVoid(cmd);
	}

	@Override
	public void export(ApiLogExportCmd cmd) {
		apiLogExportCmdExe.executeVoid(cmd);
	}

	@Override
	public Result<Page<ApiLogCO>> page(ApiLogPageQry qry) {
		return apiLogPageQryExe.execute(qry);
	}

	@Override
	public Result<ApiLogCO> getById(ApiLogGetQry qry) {
		return apiLogGetQryExe.execute(qry);
	}

}

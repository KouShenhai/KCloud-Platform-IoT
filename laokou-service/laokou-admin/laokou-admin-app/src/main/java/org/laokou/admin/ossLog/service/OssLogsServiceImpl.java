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

package org.laokou.admin.ossLog.service;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.ossLog.api.OssLogsServiceI;
import org.laokou.admin.ossLog.command.*;
import org.laokou.admin.ossLog.command.query.OssLogGetQryExe;
import org.laokou.admin.ossLog.command.query.OssLogPageQryExe;
import org.laokou.admin.ossLog.dto.*;
import org.laokou.admin.ossLog.dto.clientobject.OssLogCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

/**
 * OSS日志接口实现类.
 *
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class OssLogsServiceImpl implements OssLogsServiceI {

	private final OssLogSaveCmdExe ossLogSaveCmdExe;

	private final OssLogModifyCmdExe ossLogModifyCmdExe;

	private final OssLogRemoveCmdExe ossLogRemoveCmdExe;

	private final OssLogImportCmdExe ossLogImportCmdExe;

	private final OssLogExportCmdExe ossLogExportCmdExe;

	private final OssLogPageQryExe ossLogPageQryExe;

	private final OssLogGetQryExe ossLogGetQryExe;

	@Override
	public void save(OssLogSaveCmd cmd) {
		ossLogSaveCmdExe.executeVoid(cmd);
	}

	@Override
	public void modify(OssLogModifyCmd cmd) {
		ossLogModifyCmdExe.executeVoid(cmd);
	}

	@Override
	public void remove(OssLogRemoveCmd cmd) {
		ossLogRemoveCmdExe.executeVoid(cmd);
	}

	@Override
	public void importI(OssLogImportCmd cmd) {
		ossLogImportCmdExe.executeVoid(cmd);
	}

	@Override
	public void export(OssLogExportCmd cmd) {
		ossLogExportCmdExe.executeVoid(cmd);
	}

	@Override
	public Result<Page<OssLogCO>> page(OssLogPageQry qry) {
		return ossLogPageQryExe.execute(qry);
	}

	@Override
	public Result<OssLogCO> getById(OssLogGetQry qry) {
		return ossLogGetQryExe.execute(qry);
	}

}

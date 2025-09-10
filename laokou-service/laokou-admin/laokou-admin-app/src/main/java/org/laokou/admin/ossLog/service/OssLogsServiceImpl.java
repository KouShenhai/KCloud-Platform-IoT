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

package org.laokou.admin.ossLog.service;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.ossLog.api.OssLogsServiceI;
import org.laokou.admin.ossLog.command.OssLogExportCmdExe;
import org.laokou.admin.ossLog.command.OssLogImportCmdExe;
import org.laokou.admin.ossLog.command.OssLogModifyCmdExe;
import org.laokou.admin.ossLog.command.OssLogRemoveCmdExe;
import org.laokou.admin.ossLog.command.OssLogSaveCmdExe;
import org.laokou.admin.ossLog.command.query.OssLogGetQryExe;
import org.laokou.admin.ossLog.command.query.OssLogPageQryExe;
import org.laokou.admin.ossLog.dto.OssLogExportCmd;
import org.laokou.admin.ossLog.dto.OssLogGetQry;
import org.laokou.admin.ossLog.dto.OssLogImportCmd;
import org.laokou.admin.ossLog.dto.OssLogModifyCmd;
import org.laokou.admin.ossLog.dto.OssLogPageQry;
import org.laokou.admin.ossLog.dto.OssLogRemoveCmd;
import org.laokou.admin.ossLog.dto.OssLogSaveCmd;
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
	public void saveOssLog(OssLogSaveCmd cmd) {
		ossLogSaveCmdExe.executeVoid(cmd);
	}

	@Override
	public void modifyOssLog(OssLogModifyCmd cmd) {
		ossLogModifyCmdExe.executeVoid(cmd);
	}

	@Override
	public void removeOssLog(OssLogRemoveCmd cmd) {
		ossLogRemoveCmdExe.executeVoid(cmd);
	}

	@Override
	public void importOssLog(OssLogImportCmd cmd) {
		ossLogImportCmdExe.executeVoid(cmd);
	}

	@Override
	public void exportOssLog(OssLogExportCmd cmd) {
		ossLogExportCmdExe.executeVoid(cmd);
	}

	@Override
	public Result<Page<OssLogCO>> pageOssLog(OssLogPageQry qry) {
		return ossLogPageQryExe.execute(qry);
	}

	@Override
	public Result<OssLogCO> getByIdOssLog(OssLogGetQry qry) {
		return ossLogGetQryExe.execute(qry);
	}

}

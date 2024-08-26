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

package org.laokou.admin.oss.service;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.oss.api.OssServiceI;
import org.laokou.admin.oss.command.*;
import org.laokou.admin.oss.command.query.OssGetQryExe;
import org.laokou.admin.oss.command.query.OssPageQryExe;
import org.laokou.admin.oss.dto.*;
import org.laokou.admin.oss.dto.clientobject.OssCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

/**
 * OSS接口实现类.
 *
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class OssServiceImpl implements OssServiceI {

	private final OssSaveCmdExe ossSaveCmdExe;

	private final OssModifyCmdExe ossModifyCmdExe;

	private final OssRemoveCmdExe ossRemoveCmdExe;

	private final OssImportCmdExe ossImportCmdExe;

	private final OssExportCmdExe ossExportCmdExe;

	private final OssPageQryExe ossPageQryExe;

	private final OssGetQryExe ossGetQryExe;

	@Override
	public void save(OssSaveCmd cmd) {
		ossSaveCmdExe.executeVoid(cmd);
	}

	@Override
	public void modify(OssModifyCmd cmd) {
		ossModifyCmdExe.executeVoid(cmd);
	}

	@Override
	public void remove(OssRemoveCmd cmd) {
		ossRemoveCmdExe.executeVoid(cmd);
	}

	@Override
	public void importI(OssImportCmd cmd) {
		ossImportCmdExe.executeVoid(cmd);
	}

	@Override
	public void export(OssExportCmd cmd) {
		ossExportCmdExe.executeVoid(cmd);
	}

	@Override
	public Result<Page<OssCO>> page(OssPageQry qry) {
		return ossPageQryExe.execute(qry);
	}

	@Override
	public Result<OssCO> getById(OssGetQry qry) {
		return ossGetQryExe.execute(qry);
	}

}

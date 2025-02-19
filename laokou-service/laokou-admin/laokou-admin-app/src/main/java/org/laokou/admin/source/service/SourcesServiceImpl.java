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

package org.laokou.admin.source.service;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.source.api.SourcesServiceI;
import org.laokou.admin.source.command.*;
import org.laokou.admin.source.command.query.SourceGetQryExe;
import org.laokou.admin.source.command.query.SourcePageQryExe;
import org.laokou.admin.source.dto.*;
import org.laokou.admin.source.dto.clientobject.SourceCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

/**
 * 数据源接口实现类.
 *
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class SourcesServiceImpl implements SourcesServiceI {

	private final SourceSaveCmdExe sourceSaveCmdExe;

	private final SourceModifyCmdExe sourceModifyCmdExe;

	private final SourceRemoveCmdExe sourceRemoveCmdExe;

	private final SourceImportCmdExe sourceImportCmdExe;

	private final SourceExportCmdExe sourceExportCmdExe;

	private final SourcePageQryExe sourcePageQryExe;

	private final SourceGetQryExe sourceGetQryExe;

	@Override
	public void save(SourceSaveCmd cmd) {
		sourceSaveCmdExe.executeVoid(cmd);
	}

	@Override
	public void modify(SourceModifyCmd cmd) {
		sourceModifyCmdExe.executeVoid(cmd);
	}

	@Override
	public void remove(SourceRemoveCmd cmd) {
		sourceRemoveCmdExe.executeVoid(cmd);
	}

	@Override
	public void importI(SourceImportCmd cmd) {
		sourceImportCmdExe.executeVoid(cmd);
	}

	@Override
	public void export(SourceExportCmd cmd) {
		sourceExportCmdExe.executeVoid(cmd);
	}

	@Override
	public Result<Page<SourceCO>> page(SourcePageQry qry) {
		return sourcePageQryExe.execute(qry);
	}

	@Override
	public Result<SourceCO> getById(SourceGetQry qry) {
		return sourceGetQryExe.execute(qry);
	}

}

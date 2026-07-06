/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.iot.source.service;

import lombok.RequiredArgsConstructor;
import org.laokou.iot.source.api.SourcesServiceI;
import org.laokou.iot.source.command.SourceExportCmdExe;
import org.laokou.iot.source.command.SourceImportCmdExe;
import org.laokou.iot.source.command.SourceModifyCmdExe;
import org.laokou.iot.source.command.SourceRemoveCmdExe;
import org.laokou.iot.source.command.SourceSaveCmdExe;
import org.laokou.iot.source.command.SourceTestCmdExe;
import org.laokou.iot.source.command.query.SourceGetQryExe;
import org.laokou.iot.source.command.query.SourcePageQryExe;
import org.laokou.iot.source.dto.SourceExportCmd;
import org.laokou.iot.source.dto.SourceGetQry;
import org.laokou.iot.source.dto.SourceImportCmd;
import org.laokou.iot.source.dto.SourceModifyCmd;
import org.laokou.iot.source.dto.SourcePageQry;
import org.laokou.iot.source.dto.SourceRemoveCmd;
import org.laokou.iot.source.dto.SourceSaveCmd;
import org.laokou.iot.source.dto.SourceTestCmd;
import org.laokou.iot.source.dto.clientobject.SourceCO;
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

	private final SourceTestCmdExe sourceTestCmdExe;

	@Override
	public void saveSource(SourceSaveCmd cmd) {
		sourceSaveCmdExe.executeVoid(cmd);
	}

	@Override
	public void modifySource(SourceModifyCmd cmd) {
		sourceModifyCmdExe.executeVoid(cmd);
	}

	@Override
	public void removeSource(SourceRemoveCmd cmd) {
		sourceRemoveCmdExe.executeVoid(cmd);
	}

	@Override
	public void importSource(SourceImportCmd cmd) {
		sourceImportCmdExe.executeVoid(cmd);
	}

	@Override
	public void exportSource(SourceExportCmd cmd) {
		sourceExportCmdExe.executeVoid(cmd);
	}

	@Override
	public Result<Page<SourceCO>> pageSource(SourcePageQry qry) {
		return sourcePageQryExe.execute(qry);
	}

	@Override
	public Result<SourceCO> getSourceById(SourceGetQry qry) {
		return sourceGetQryExe.execute(qry);
	}

	@Override
	public void testSource(SourceTestCmd cmd) {
		sourceTestCmdExe.executeVoid(cmd);
	}

}

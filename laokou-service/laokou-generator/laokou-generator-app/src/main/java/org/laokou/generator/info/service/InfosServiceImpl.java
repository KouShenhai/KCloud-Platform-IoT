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

package org.laokou.generator.info.service;

import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.generator.info.api.InfosServiceI;
import org.laokou.generator.info.command.InfoExportCmdExe;
import org.laokou.generator.info.command.InfoImportCmdExe;
import org.laokou.generator.info.command.InfoModifyCmdExe;
import org.laokou.generator.info.command.InfoRemoveCmdExe;
import org.laokou.generator.info.command.InfoSaveCmdExe;
import org.laokou.generator.info.command.query.InfoGetQryExe;
import org.laokou.generator.info.command.query.InfoPageQryExe;
import org.laokou.generator.info.dto.InfoExportCmd;
import org.laokou.generator.info.dto.InfoGetQry;
import org.laokou.generator.info.dto.InfoImportCmd;
import org.laokou.generator.info.dto.InfoModifyCmd;
import org.laokou.generator.info.dto.InfoPageQry;
import org.laokou.generator.info.dto.InfoRemoveCmd;
import org.laokou.generator.info.dto.InfoSaveCmd;
import org.laokou.generator.info.dto.clientobject.InfoCO;
import org.springframework.stereotype.Service;

/**
 *
 * 代码生成器信息接口实现类.
 *
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class InfosServiceImpl implements InfosServiceI {

	private final InfoSaveCmdExe infoSaveCmdExe;

	private final InfoModifyCmdExe infoModifyCmdExe;

	private final InfoRemoveCmdExe infoRemoveCmdExe;

	private final InfoImportCmdExe infoImportCmdExe;

	private final InfoExportCmdExe infoExportCmdExe;

	private final InfoPageQryExe infoPageQryExe;

	private final InfoGetQryExe infoGetQryExe;

	@Override
	public void saveInfo(InfoSaveCmd cmd) {
		infoSaveCmdExe.executeVoid(cmd);
	}

	@Override
	public void modifyInfo(InfoModifyCmd cmd) {
		infoModifyCmdExe.executeVoid(cmd);
	}

	@Override
	public void removeInfo(InfoRemoveCmd cmd) {
		infoRemoveCmdExe.executeVoid(cmd);
	}

	@Override
	public void importInfo(InfoImportCmd cmd) {
		infoImportCmdExe.executeVoid(cmd);
	}

	@Override
	public void exportInfo(InfoExportCmd cmd) {
		infoExportCmdExe.executeVoid(cmd);
	}

	@Override
	public Result<Page<InfoCO>> pageInfo(InfoPageQry qry) {
		return infoPageQryExe.execute(qry);
	}

	@Override
	public Result<InfoCO> getInfoById(InfoGetQry qry) {
		return infoGetQryExe.execute(qry);
	}

}

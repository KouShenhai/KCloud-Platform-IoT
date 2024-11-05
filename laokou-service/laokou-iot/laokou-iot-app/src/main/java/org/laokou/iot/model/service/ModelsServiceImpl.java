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

package org.laokou.iot.model.service;

import lombok.RequiredArgsConstructor;
import org.laokou.iot.model.api.ModelsServiceI;
import org.laokou.iot.model.command.*;
import org.laokou.iot.model.command.query.ModelGetQryExe;
import org.laokou.iot.model.command.query.ModelPageQryExe;
import org.laokou.iot.model.dto.*;
import org.laokou.iot.model.dto.clientobject.ModelCO;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

/**
 *
 * 模型接口实现类.
 *
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class ModelsServiceImpl implements ModelsServiceI {

	private final ModelSaveCmdExe modelSaveCmdExe;

	private final ModelModifyCmdExe modelModifyCmdExe;

	private final ModelRemoveCmdExe modelRemoveCmdExe;

	private final ModelImportCmdExe modelImportCmdExe;

	private final ModelExportCmdExe modelExportCmdExe;

	private final ModelPageQryExe modelPageQryExe;

	private final ModelGetQryExe modelGetQryExe;

	@Override
	public void save(ModelSaveCmd cmd) {
		modelSaveCmdExe.executeVoid(cmd);
	}

	@Override
	public void modify(ModelModifyCmd cmd) {
		modelModifyCmdExe.executeVoid(cmd);
	}

	@Override
	public void remove(ModelRemoveCmd cmd) {
		modelRemoveCmdExe.executeVoid(cmd);
	}

	@Override
	public void importI(ModelImportCmd cmd) {
		modelImportCmdExe.executeVoid(cmd);
	}

	@Override
	public void export(ModelExportCmd cmd) {
		modelExportCmdExe.executeVoid(cmd);
	}

	@Override
	public Result<Page<ModelCO>> page(ModelPageQry qry) {
		return modelPageQryExe.execute(qry);
	}

	@Override
	public Result<ModelCO> getById(ModelGetQry qry) {
		return modelGetQryExe.execute(qry);
	}

}

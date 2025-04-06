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

package org.laokou.iot.thingModel.service;

import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.iot.thingModel.api.ThingModelsServiceI;
import org.laokou.iot.thingModel.command.*;
import org.laokou.iot.thingModel.command.query.ThingModelGetQryExe;
import org.laokou.iot.thingModel.command.query.ThingModelPageQryExe;
import org.laokou.iot.thingModel.dto.*;
import org.laokou.iot.thingModel.dto.clientobject.ThingModelCO;
import org.springframework.stereotype.Service;

/**
 *
 * 物模型接口实现类.
 *
 * @author laokou
 */
@Service
@RequiredArgsConstructor
public class ThingThingModelsServiceImpl implements ThingModelsServiceI {

	private final ThingModelSaveCmdExe thingModelSaveCmdExe;

	private final ThingModelModifyCmdExe thingModelModifyCmdExe;

	private final ThingModelRemoveCmdExe thingModelRemoveCmdExe;

	private final ThingModelImportCmdExe thingModelImportCmdExe;

	private final ThingModelExportCmdExe thingModelExportCmdExe;

	private final ThingModelPageQryExe thingModelPageQryExe;

	private final ThingModelGetQryExe thingModelGetQryExe;

	@Override
	public void save(ThingModelSaveCmd cmd) throws Exception {
		thingModelSaveCmdExe.executeVoid(cmd);
	}

	@Override
	public void modify(ThingModelModifyCmd cmd) {
		thingModelModifyCmdExe.executeVoid(cmd);
	}

	@Override
	public void remove(ThingModelRemoveCmd cmd) {
		thingModelRemoveCmdExe.executeVoid(cmd);
	}

	@Override
	public void importI(ThingModelImportCmd cmd) {
		thingModelImportCmdExe.executeVoid(cmd);
	}

	@Override
	public void export(ThingThingModelExportCmd cmd) {
		thingModelExportCmdExe.executeVoid(cmd);
	}

	@Override
	public Result<Page<ThingModelCO>> page(ThingModelPageQry qry) {
		return thingModelPageQryExe.execute(qry);
	}

	@Override
	public Result<ThingModelCO> getById(ThingModelGetQry qry) {
		return thingModelGetQryExe.execute(qry);
	}

}

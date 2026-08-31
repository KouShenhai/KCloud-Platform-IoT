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

package org.laokou.iot.thing.model.service;

import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.iot.thing.model.api.ThingModelsServiceI;
import org.laokou.iot.thing.model.command.ThingModelExportCmdExe;
import org.laokou.iot.thing.model.command.ThingModelImportCmdExe;
import org.laokou.iot.thing.model.command.ThingModelModifyCmdExe;
import org.laokou.iot.thing.model.command.ThingModelRemoveCmdExe;
import org.laokou.iot.thing.model.command.ThingModelSaveCmdExe;
import org.laokou.iot.thing.model.command.query.ThingModelGetQryExe;
import org.laokou.iot.thing.model.command.query.ThingModelPageQryExe;
import org.laokou.iot.thing.model.dto.ThingModelGetQry;
import org.laokou.iot.thing.model.dto.ThingModelImportCmd;
import org.laokou.iot.thing.model.dto.ThingModelModifyCmd;
import org.laokou.iot.thing.model.dto.ThingModelPageQry;
import org.laokou.iot.thing.model.dto.ThingModelRemoveCmd;
import org.laokou.iot.thing.model.dto.ThingModelSaveCmd;
import org.laokou.iot.thing.model.dto.ThingThingModelExportCmd;
import org.laokou.iot.thing.model.dto.clientobject.ThingModelCO;
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
	public void saveThingModel(ThingModelSaveCmd cmd) throws Exception {
		thingModelSaveCmdExe.executeVoid(cmd);
	}

	@Override
	public void modifyThingModel(ThingModelModifyCmd cmd) throws Exception {
		thingModelModifyCmdExe.executeVoid(cmd);
	}

	@Override
	public void removeThingModel(ThingModelRemoveCmd cmd) {
		thingModelRemoveCmdExe.executeVoid(cmd);
	}

	@Override
	public void importThingModel(ThingModelImportCmd cmd) {
		thingModelImportCmdExe.executeVoid(cmd);
	}

	@Override
	public void exportThingModel(ThingThingModelExportCmd cmd) {
		thingModelExportCmdExe.executeVoid(cmd);
	}

	@Override
	public Result<Page<ThingModelCO>> pageThingModel(ThingModelPageQry qry) {
		return thingModelPageQryExe.execute(qry);
	}

	@Override
	public Result<ThingModelCO> getThingModelById(ThingModelGetQry qry) {
		return thingModelGetQryExe.execute(qry);
	}

}

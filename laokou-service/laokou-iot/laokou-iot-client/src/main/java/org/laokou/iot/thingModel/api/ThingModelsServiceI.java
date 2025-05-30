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

package org.laokou.iot.thingModel.api;

import org.laokou.common.i18n.dto.Page;
import org.laokou.common.i18n.dto.Result;
import org.laokou.iot.thingModel.dto.*;
import org.laokou.iot.thingModel.dto.clientobject.ThingModelCO;

/**
 *
 * 物模型接口.
 *
 * @author laokou
 */
public interface ThingModelsServiceI {

	/**
	 * 保存物模型.
	 * @param cmd 保存命令
	 */
	void saveThingModel(ThingModelSaveCmd cmd) throws Exception;

	/**
	 * 修改物模型.
	 * @param cmd 修改命令
	 */
	void modifyThingModel(ThingModelModifyCmd cmd);

	/**
	 * 删除物模型.
	 * @param cmd 删除命令
	 */
	void removeThingModel(ThingModelRemoveCmd cmd);

	/**
	 * 导入物模型.
	 * @param cmd 导入命令
	 */
	void importThingModel(ThingModelImportCmd cmd);

	/**
	 * 导出物模型.
	 * @param cmd 导出命令
	 */
	void exportThingModel(ThingThingModelExportCmd cmd);

	/**
	 * 分页查询物模型.
	 * @param qry 分页查询请求
	 */
	Result<Page<ThingModelCO>> pageThingModel(ThingModelPageQry qry);

	/**
	 * 查看物模型.
	 * @param qry 查看请求
	 */
	Result<ThingModelCO> getThingModelById(ThingModelGetQry qry);

}

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

package org.laokou.iot.thingModel.convertor;

import org.laokou.iot.thingModel.gatewayimpl.database.dataobject.ThingModelDO;
import org.laokou.iot.thingModel.dto.clientobject.ThingModelCO;
import org.laokou.iot.thingModel.model.ThingModelE;

/**
 *
 * 物模型转换器.
 *
 * @author laokou
 */
public class ModelConvertor {

	public static ThingModelDO toDataObject(ThingModelE thingModelE, boolean isInsert) {
		ThingModelDO thingModelDO = new ThingModelDO();
		if (isInsert) {
			thingModelDO.generatorId();
		}
		else {
			thingModelDO.setId(thingModelE.getId());
		}
		thingModelDO.setName(thingModelE.getName());
		thingModelDO.setCode(thingModelE.getCode());
		thingModelDO.setDataType(thingModelE.getDataType());
		thingModelDO.setCategory(thingModelE.getCategory());
		thingModelDO.setRwType(thingModelE.getRwType());
		thingModelDO.setExpression(thingModelE.getExpression());
		thingModelDO.setSort(thingModelE.getSort());
		thingModelDO.setSpecs(thingModelE.getSpecs());
		thingModelDO.setRemark(thingModelE.getRemark());
		return thingModelDO;
	}

	public static ThingModelCO toClientObject(ThingModelDO thingModelDO) {
		ThingModelCO thingModelCO = new ThingModelCO();
		thingModelCO.setName(thingModelDO.getName());
		thingModelCO.setCode(thingModelDO.getCode());
		thingModelCO.setDataType(thingModelDO.getDataType());
		thingModelCO.setCategory(thingModelDO.getCategory());
		thingModelCO.setRwType(thingModelDO.getRwType());
		thingModelCO.setExpression(thingModelDO.getExpression());
		thingModelCO.setSort(thingModelDO.getSort());
		thingModelCO.setSpecs(thingModelDO.getSpecs());
		thingModelCO.setRemark(thingModelDO.getRemark());
		return thingModelCO;
	}

	public static ThingModelE toEntity(ThingModelCO thingModelCO) {
		ThingModelE thingModelE = new ThingModelE();
		thingModelE.setName(thingModelCO.getName());
		thingModelE.setCode(thingModelCO.getCode());
		thingModelE.setDataType(thingModelCO.getDataType());
		thingModelE.setCategory(thingModelCO.getCategory());
		thingModelE.setRwType(thingModelCO.getRwType());
		thingModelE.setExpression(thingModelCO.getExpression());
		thingModelE.setSort(thingModelCO.getSort());
		thingModelE.setSpecs(thingModelCO.getSpecs());
		thingModelE.setRemark(thingModelCO.getRemark());
		return thingModelE;
	}

}

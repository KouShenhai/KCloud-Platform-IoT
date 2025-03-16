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

import org.laokou.common.core.utils.IdGenerator;
import org.laokou.iot.thingModel.dto.clientobject.ThingModelCO;
import org.laokou.iot.thingModel.gatewayimpl.database.dataobject.ThingModelDO;
import org.laokou.iot.thingModel.model.ThingModelE;

import java.util.List;

/**
 *
 * 物模型转换器.
 *
 * @author laokou
 */
public class ThingModelConvertor {

	public static ThingModelDO toDataObject(ThingModelE thingModelE, boolean isInsert) {
		ThingModelDO thingModelDO = new ThingModelDO();
		if (isInsert) {
			thingModelDO.setId(IdGenerator.defaultSnowflakeId());
		}
		else {
			thingModelDO.setId(thingModelE.getId());
		}
		thingModelDO.setName(thingModelE.getName());
		thingModelDO.setCode(thingModelE.getCode());
		thingModelDO.setDataType(thingModelE.getDataType());
		thingModelDO.setCategory(thingModelE.getCategory());
		thingModelDO.setType(thingModelE.getType());
		thingModelDO.setExpression(thingModelE.getExpression());
		thingModelDO.setSort(thingModelE.getSort());
		thingModelDO.setSpecs(thingModelE.getSpecs());
		thingModelDO.setRemark(thingModelE.getRemark());
		thingModelDO.setExpressionFlag(thingModelE.getExpressionFlag());
		return thingModelDO;
	}

	public static List<ThingModelCO> toClientObjects(List<ThingModelDO> list) {
		return list.stream().map(ThingModelConvertor::toClientObject).toList();
	}

	public static ThingModelCO toClientObject(ThingModelDO thingModelDO) {
		ThingModelCO thingModelCO = new ThingModelCO();
		thingModelCO.setId(thingModelDO.getId());
		thingModelCO.setName(thingModelDO.getName());
		thingModelCO.setCode(thingModelDO.getCode());
		thingModelCO.setDataType(thingModelDO.getDataType());
		thingModelCO.setCategory(thingModelDO.getCategory());
		thingModelCO.setType(thingModelDO.getType());
		thingModelCO.setExpression(thingModelDO.getExpression());
		thingModelCO.setSort(thingModelDO.getSort());
		thingModelCO.setSpecs(thingModelDO.getSpecs());
		thingModelCO.setRemark(thingModelDO.getRemark());
		thingModelCO.setCreateTime(thingModelDO.getCreateTime());
		thingModelCO.setExpressionFlag(thingModelDO.getExpressionFlag());
		return thingModelCO;
	}

	public static ThingModelE toEntity(ThingModelCO thingModelCO) {
		ThingModelE thingModelE = new ThingModelE();
		thingModelE.setId(thingModelCO.getId());
		thingModelE.setName(thingModelCO.getName());
		thingModelE.setCode(thingModelCO.getCode());
		thingModelE.setDataType(thingModelCO.getDataType());
		thingModelE.setCategory(thingModelCO.getCategory());
		thingModelE.setType(thingModelCO.getType());
		thingModelE.setExpression(thingModelCO.getExpression());
		thingModelE.setSort(thingModelCO.getSort());
		thingModelE.setSpecs(thingModelCO.getSpecs());
		thingModelE.setRemark(thingModelCO.getRemark());
		thingModelE.setExpressionFlag(thingModelCO.getExpressionFlag());
		return thingModelE;
	}

}

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

package org.laokou.iot.thingModel.convertor;

import org.laokou.iot.thingModel.dto.clientobject.ThingModelCO;
import org.laokou.iot.thingModel.factory.ThingModelDomainFactory;
import org.laokou.iot.thingModel.gatewayimpl.database.dataobject.ThingModelDO;
import org.laokou.iot.thingModel.model.ThingModelA;
import org.laokou.iot.thingModel.model.entity.ThingModelE;

import java.util.List;

/**
 *
 * 物模型转换器.
 *
 * @author laokou
 */
public final class ThingModelConvertor {

	private ThingModelConvertor() {
	}

	public static ThingModelDO toDataObject(ThingModelA thingModelA) {
		ThingModelDO thingModelDO = new ThingModelDO();
		ThingModelE thingModelE = thingModelA.getThingModelE();
		thingModelDO.setId(thingModelA.getId());
		thingModelDO.setName(thingModelE.getName());
		thingModelDO.setCode(thingModelE.getCode());
		thingModelDO.setDataType(thingModelE.getDataType());
		thingModelDO.setType(thingModelE.getType());
		thingModelDO.setSort(thingModelE.getSort());
		thingModelDO.setSpec(thingModelE.getSpec());
		thingModelDO.setRemark(thingModelE.getRemark());
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
		thingModelCO.setType(thingModelDO.getType());
		thingModelCO.setSort(thingModelDO.getSort());
		thingModelCO.setSpec(thingModelDO.getSpec());
		thingModelCO.setRemark(thingModelDO.getRemark());
		thingModelCO.setCreateTime(thingModelDO.getCreateTime());
		return thingModelCO;
	}

	public static ThingModelE toEntity(ThingModelCO thingModelCO) {
		return ThingModelDomainFactory.createThingModelE()
			.toBuilder()
			.id(thingModelCO.getId())
			.name(thingModelCO.getName())
			.code(thingModelCO.getCode())
			.dataType(thingModelCO.getDataType())
			.type(thingModelCO.getType())
			.sort(thingModelCO.getSort())
			.spec(thingModelCO.getSpec())
			.remark(thingModelCO.getRemark())
			.build();
	}

}

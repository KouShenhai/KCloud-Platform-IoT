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

package org.laokou.iot.model.convertor;

import org.laokou.iot.model.gatewayimpl.database.dataobject.ModelDO;
import org.laokou.iot.model.dto.clientobject.ModelCO;
import org.laokou.iot.model.model.ModelE;

/**
 *
 * 模型转换器.
 *
 * @author laokou
 */
public class ModelConvertor {

	public static ModelDO toDataObject(ModelE modelE, boolean isInsert) {
		ModelDO modelDO = new ModelDO();
		if (isInsert) {
			modelDO.generatorId();
		}
		else {
			modelDO.setId(modelE.getId());
		}
		modelDO.setName(modelE.getName());
		modelDO.setCode(modelE.getCode());
		modelDO.setDataType(modelE.getDataType());
		modelDO.setCategory(modelE.getCategory());
		modelDO.setRwType(modelE.getRwType());
		modelDO.setExpression(modelE.getExpression());
		modelDO.setSort(modelE.getSort());
		modelDO.setSpecs(modelE.getSpecs());
		modelDO.setRemark(modelE.getRemark());
		return modelDO;
	}

	public static ModelCO toClientObject(ModelDO modelDO) {
		ModelCO modelCO = new ModelCO();
		modelCO.setName(modelDO.getName());
		modelCO.setCode(modelDO.getCode());
		modelCO.setDataType(modelDO.getDataType());
		modelCO.setCategory(modelDO.getCategory());
		modelCO.setRwType(modelDO.getRwType());
		modelCO.setExpression(modelDO.getExpression());
		modelCO.setSort(modelDO.getSort());
		modelCO.setSpecs(modelDO.getSpecs());
		modelCO.setRemark(modelDO.getRemark());
		return modelCO;
	}

	public static ModelE toEntity(ModelCO modelCO) {
		ModelE modelE = new ModelE();
		modelE.setName(modelCO.getName());
		modelE.setCode(modelCO.getCode());
		modelE.setDataType(modelCO.getDataType());
		modelE.setCategory(modelCO.getCategory());
		modelE.setRwType(modelCO.getRwType());
		modelE.setExpression(modelCO.getExpression());
		modelE.setSort(modelCO.getSort());
		modelE.setSpecs(modelCO.getSpecs());
		modelE.setRemark(modelCO.getRemark());
		return modelE;
	}

}

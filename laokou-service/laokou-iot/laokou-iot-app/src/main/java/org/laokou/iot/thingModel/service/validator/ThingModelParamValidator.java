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

package org.laokou.iot.thingModel.service.validator;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.laokou.common.core.util.CollectionUtils;
import org.laokou.common.i18n.common.constant.StringConstants;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.ParamValidator;
import org.laokou.iot.thingModel.gatewayimpl.database.ThingModelMapper;
import org.laokou.iot.thingModel.gatewayimpl.database.dataobject.ThingModelDO;
import org.laokou.iot.thingModel.model.CategoryEnum;
import org.laokou.iot.thingModel.model.DataTypeEnum;
import org.laokou.iot.thingModel.model.ThingModelE;
import org.laokou.iot.thingModel.model.TypeEnum;

import java.util.Arrays;

/**
 * @author laokou
 */
public final class ThingModelParamValidator {

	private ThingModelParamValidator() {

	}

	public static ParamValidator.Validate validateId(ThingModelE thingModelE) {
		Long id = thingModelE.getId();
		if (ObjectUtils.isNull(id)) {
			return ParamValidator.invalidate("物模型ID不能为空");
		}
		return ParamValidator.validate();
	}

	public static ParamValidator.Validate validateCodeAndName(ThingModelE thingModelE, boolean isSave,
			ThingModelMapper thingModelMapper) {
		String code = thingModelE.getCode();
		String name = thingModelE.getName();
		if (ObjectUtils.isNull(name) || ObjectUtils.isNull(code)) {
			return ParamValidator.invalidate("物模型编码和名称不能为空");
		}
		if (isSave && thingModelMapper.selectCount(Wrappers.lambdaQuery(ThingModelDO.class)
			.eq(ThingModelDO::getCode, code)
			.eq(ThingModelDO::getName, name)) > 0) {
			return ParamValidator.invalidate("物模型编码和名称已存在");
		}
		if (!isSave && thingModelMapper.selectCount(Wrappers.lambdaQuery(ThingModelDO.class)
			.eq(ThingModelDO::getCode, code)
			.eq(ThingModelDO::getName, name)
			.ne(ThingModelDO::getId, thingModelE.getId())) > 0) {
			return ParamValidator.invalidate("物模型编码和名称已存在");
		}
		return ParamValidator.validate();
	}

	public static ParamValidator.Validate validateSpecs(ThingModelE thingModelE) throws JsonProcessingException {
		String specs = thingModelE.getSpecs();
		if (ObjectUtils.isNull(specs)) {
			return ParamValidator.invalidate("物模型规则说明不能为空");
		}
		DataTypeEnum dataTypeEnum = DataTypeEnum.getByCode(thingModelE.getDataType());
		return dataTypeEnum.validate(specs);
	}

	public static ParamValidator.Validate validateSort(ThingModelE thingModelE) {
		Integer sort = thingModelE.getSort();
		if (ObjectUtils.isNull(sort)) {
			return ParamValidator.invalidate("物模型排序不能为空");
		}
		if (sort < 1 || sort > 99999) {
			return ParamValidator.invalidate("物模型排序范围1-99999");
		}
		return ParamValidator.validate();
	}

	public static ParamValidator.Validate validateType(ThingModelE thingModelE) {
		String type = thingModelE.getType();
		if (ObjectUtils.isNull(type)) {
			return ParamValidator.invalidate("物模型类型不能为空");
		}
		boolean isExist = CollectionUtils.containsAll(Arrays.stream(type.split(StringConstants.COMMA)).toList(),
				Arrays.stream(TypeEnum.values()).map(TypeEnum::getCode).toList());
		if (!isExist) {
			return ParamValidator.invalidate("物模型类型不存在");
		}
		return ParamValidator.validate();
	}

	public static ParamValidator.Validate validateCategory(ThingModelE thingModelE) {
		Integer category = thingModelE.getCategory();
		if (ObjectUtils.isNull(category)) {
			return ParamValidator.invalidate("物模型类别不能为空");
		}
		if (!CollectionUtils.contains(Arrays.stream(CategoryEnum.values()).map(CategoryEnum::getCode).toList(),
				category)) {
			return ParamValidator.invalidate("物模型类别不存在");
		}
		return ParamValidator.validate();
	}

	public static ParamValidator.Validate validateDataType(ThingModelE thingModelE) {
		String dataType = thingModelE.getDataType();
		if (ObjectUtils.isNull(dataType)) {
			return ParamValidator.invalidate("物模型数据类型不能为空");
		}
		if (!Arrays.stream(DataTypeEnum.values()).map(DataTypeEnum::getCode).toList().contains(dataType)) {
			return ParamValidator.invalidate("物模型数据类型不存在");
		}
		return ParamValidator.validate();
	}

}

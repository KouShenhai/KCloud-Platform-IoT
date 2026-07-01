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

package org.laokou.iot.thingModel.service.validator;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.laokou.common.core.util.CollectionExtUtils;
import org.laokou.common.i18n.common.constant.StringConstants;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.ParamValidator;
import org.laokou.iot.thingModel.gatewayimpl.database.ThingModelMapper;
import org.laokou.iot.thingModel.gatewayimpl.database.dataobject.ThingModelDO;
import org.laokou.iot.thingModel.model.ThingModelA;
import org.laokou.iot.thingModel.model.entity.ThingModelE;
import org.laokou.iot.thingModel.model.enums.DataType;
import org.laokou.iot.thingModel.model.enums.Type;
import org.springframework.util.StringUtils;

import java.util.Arrays;

/**
 * @author laokou
 */
final class ThingModelParamValidator {

	private ThingModelParamValidator() {

	}

	static ParamValidator.Validate validateId(ThingModelA thingModelA) {
		Long id = thingModelA.getId();
		if (id == null) {
			return ParamValidator.invalidate("物模型ID不能为空");
		}
		return ParamValidator.validate();
	}

	static ParamValidator.Validate validateCodeAndName(ThingModelA thingModelA, ThingModelMapper thingModelMapper) {
		ThingModelE thingModelE = thingModelA.getThingModelE();
		String code = thingModelE.getCode();
		String name = thingModelE.getName();
		if (!StringUtils.hasText(name)) {
			return ParamValidator.invalidate("物模型名称不能为空");
		}
		if (!StringUtils.hasText(code)) {
			return ParamValidator.invalidate("物模型编码不能为空");
		}
		if (thingModelA.isSave() && thingModelMapper.selectCount(Wrappers.lambdaQuery(ThingModelDO.class)
			.eq(ThingModelDO::getCode, code)
			.eq(ThingModelDO::getName, name)) > 0) {
			return ParamValidator.invalidate("物模型编码和物模型名称已存在");
		}
		if (thingModelA.isModify() && thingModelMapper.selectCount(Wrappers.lambdaQuery(ThingModelDO.class)
			.eq(ThingModelDO::getCode, code)
			.eq(ThingModelDO::getName, name)
			.ne(ThingModelDO::getId, thingModelE.getId())) > 0) {
			return ParamValidator.invalidate("物模型编码和物模型名称已存在");
		}
		return ParamValidator.validate();
	}

	static ParamValidator.Validate validateSpec(ThingModelA thingModelA) {
		String spec = thingModelA.getThingModelE().getSpec();
		if (!StringUtils.hasText(spec)) {
			return ParamValidator.invalidate("物模型规格不能为空");
		}
		ParamValidator.Validate validate = validateDataType(thingModelA);
		if (validate.isValidate()) {
			return DataType.getByCode(thingModelA.getThingModelE().getDataType()).validate(spec);
		}
		return validate;
	}

	static ParamValidator.Validate validateSort(ThingModelA thingModelA) {
		Integer sort = thingModelA.getThingModelE().getSort();
		if (sort == null) {
			return ParamValidator.invalidate("物模型排序不能为空");
		}
		if (sort < 1 || sort > 99999) {
			return ParamValidator.invalidate("物模型排序范围1-99999");
		}
		return ParamValidator.validate();
	}

	static ParamValidator.Validate validateType(ThingModelA thingModelA) {
		String type = thingModelA.getThingModelE().getType();
		if (ObjectUtils.isNull(type)) {
			return ParamValidator.invalidate("物模型类型不能为空");
		}
		boolean isExist = CollectionExtUtils.containsAll(Arrays.stream(type.split(StringConstants.COMMA)).toList(),
				Arrays.stream(Type.values()).map(Type::getCode).toList());
		if (!isExist) {
			return ParamValidator.invalidate("物模型类型不存在");
		}
		return ParamValidator.validate();
	}

	static ParamValidator.Validate validateDataType(ThingModelA thingModelA) {
		String dataType = thingModelA.getThingModelE().getDataType();
		if (ObjectUtils.isNull(dataType)) {
			return ParamValidator.invalidate("物模型数据类型不能为空");
		}
		if (!Arrays.stream(DataType.values()).map(DataType::getCode).toList().contains(dataType)) {
			return ParamValidator.invalidate("物模型数据类型不存在");
		}
		return ParamValidator.validate();
	}

}

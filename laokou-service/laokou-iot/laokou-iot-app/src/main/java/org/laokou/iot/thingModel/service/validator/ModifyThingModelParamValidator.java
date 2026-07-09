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

import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.util.ParamValidator;
import org.laokou.iot.thingModel.gatewayimpl.database.ThingModelMapper;
import org.laokou.iot.thingModel.model.ThingModelA;
import org.laokou.iot.thingModel.model.validator.ThingModelParamValidator;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Component("modifyThingModelParamValidator")
@RequiredArgsConstructor
public class ModifyThingModelParamValidator implements ThingModelParamValidator {

	private final ThingModelMapper thingModelMapper;

	@Override
	public void validateThingModel(ThingModelA thingModelA) {
		ParamValidator.validate("System",
				// 校验物模型ID
				org.laokou.iot.thingModel.service.validator.ThingModelParamValidator.validateId(thingModelA),
				// 校验物模型编码和物模型名称
				org.laokou.iot.thingModel.service.validator.ThingModelParamValidator.validateCodeAndName(thingModelA,
						thingModelMapper),
				// 校验物模型数据类型
				org.laokou.iot.thingModel.service.validator.ThingModelParamValidator.validateDataType(thingModelA),
				// 校验物模型规格
				org.laokou.iot.thingModel.service.validator.ThingModelParamValidator.validateSpec(thingModelA),
				// 校验物模型排序
				org.laokou.iot.thingModel.service.validator.ThingModelParamValidator.validateSort(thingModelA));
	}

}

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

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.util.ParamValidator;
import org.laokou.iot.thingModel.gatewayimpl.database.ThingModelMapper;
import org.laokou.iot.thingModel.model.ThingModelE;
import org.laokou.iot.thingModel.model.ThingModelParamValidator;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Component("saveThingModelParamValidator")
@RequiredArgsConstructor
public class SaveThingModelParamValidator implements ThingModelParamValidator {

	private final ThingModelMapper thingModelMapper;

	@Override
	public void validateThingModel(ThingModelE thingModelE) throws JsonProcessingException {
		ParamValidator.validate(
				org.laokou.iot.thingModel.service.validator.ThingModelParamValidator.validateCodeAndName(thingModelE,
						true, thingModelMapper),
				org.laokou.iot.thingModel.service.validator.ThingModelParamValidator.validateCategory(thingModelE),
				org.laokou.iot.thingModel.service.validator.ThingModelParamValidator.validateDataType(thingModelE),
				org.laokou.iot.thingModel.service.validator.ThingModelParamValidator.validateSpecs(thingModelE),
				org.laokou.iot.thingModel.service.validator.ThingModelParamValidator.validateType(thingModelE),
				org.laokou.iot.thingModel.service.validator.ThingModelParamValidator.validateSort(thingModelE));
	}

}

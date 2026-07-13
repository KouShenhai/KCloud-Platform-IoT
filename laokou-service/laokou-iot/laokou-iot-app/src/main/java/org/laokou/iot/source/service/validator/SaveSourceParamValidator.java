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

package org.laokou.iot.source.service.validator;

import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.util.ParamValidator;
import org.laokou.iot.source.model.SourceA;
import org.laokou.iot.source.model.validator.SourceParamValidator;
import org.laokou.iot.thingModel.gatewayimpl.database.ThingModelMapper;
import org.laokou.iot.thingModel.model.ThingModelA;
import org.laokou.iot.thingModel.model.validator.ThingModelParamValidator;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Component("saveSourceParamValidator")
@RequiredArgsConstructor
public class SaveSourceParamValidator implements SourceParamValidator {

	@Override
	public void validateSource(SourceA sourceA) {
		ParamValidator.validate(sourceA.getValidateName(),
				// 校验数据源名称
				org.laokou.iot.source.service.validator.SourceParamValidator.validateName(sourceA),
				// 校验数据源用户名
				org.laokou.iot.source.service.validator.SourceParamValidator.validateUsername(sourceA),
				// 校验数据源密码
				org.laokou.iot.source.service.validator.SourceParamValidator.validatePassword(sourceA),
				// 校验数据源地址
				org.laokou.iot.source.service.validator.SourceParamValidator.validateEndpoint(sourceA),
				// 校验数据源类型
				org.laokou.iot.source.service.validator.SourceParamValidator.validateType(sourceA));
	}

}

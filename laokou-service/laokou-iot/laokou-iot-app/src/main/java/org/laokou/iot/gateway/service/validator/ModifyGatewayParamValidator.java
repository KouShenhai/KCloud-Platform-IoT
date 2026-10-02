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

package org.laokou.iot.gateway.service.validator;

import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.util.ParamValidator;
import org.laokou.iot.gateway.model.GatewayA;
import org.springframework.stereotype.Component;
import org.laokou.iot.gateway.model.validator.GatewayParamValidator;

/**
 * @author laokou
 */
@Component("modifyGatewayParamValidator")
@RequiredArgsConstructor
public class ModifyGatewayParamValidator implements GatewayParamValidator {

	@Override
	public void validateGateway(GatewayA gatewayA) {
		ParamValidator.validate(gatewayA.getValidateName(),
				// 校验网关ID
				org.laokou.iot.gateway.service.validator.GatewayParamValidator.validateId(gatewayA),
				// 校验网关名称
				org.laokou.iot.gateway.service.validator.GatewayParamValidator.validateName(gatewayA));
	}

}

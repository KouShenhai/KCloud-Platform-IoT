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

package org.laokou.iot.session.service.validator;

import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.util.ParamValidator;
import org.laokou.iot.session.gatewayimpl.database.SessionMapper;
import org.laokou.iot.session.model.SessionA;
import org.laokou.iot.session.model.validator.SessionParamValidator;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Component("modifySessionParamValidator")
@RequiredArgsConstructor
public class ModifySessionParamValidator implements SessionParamValidator {

	private final SessionMapper sessionMapper;

	@Override
	public void validateSession(SessionA sessionA) {
		ParamValidator.validate(sessionA.getValidateName(),
				// 校验会话ID
				org.laokou.iot.session.service.validator.SessionParamValidator.validateId(sessionA),
				// 校验会话主机和会话端口
				org.laokou.iot.session.service.validator.SessionParamValidator.validateHostAndPort(sessionA,
						sessionMapper),
				// 校验会话名称
				org.laokou.iot.session.service.validator.SessionParamValidator.validateName(sessionA),
				// 校验会话用户名
				org.laokou.iot.session.service.validator.SessionParamValidator.validateUsername(sessionA),
				// 校验会话密码
				org.laokou.iot.session.service.validator.SessionParamValidator.validatePassword(sessionA));
	}

}

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

package org.laokou.auth.gatewayimpl;

import lombok.RequiredArgsConstructor;
import org.laokou.auth.convertor.LoginLogConvertor;
import org.laokou.auth.dto.domainevent.LoginEvent;
import org.laokou.auth.gateway.LoginLogGateway;
import org.laokou.auth.gatewayimpl.database.LoginLogMapper;
import org.laokou.auth.gatewayimpl.database.dataobject.LoginLogDO;
import org.laokou.common.i18n.dto.DefaultDomainEvent;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class LoginLogGatewayImpl implements LoginLogGateway {

	private final LoginLogConvertor loginLogConvertor;

	private final LoginLogMapper loginLogMapper;

	@Override
	public void createLoginLog(DefaultDomainEvent domainEvent) {
		LoginLogDO loginLogDO = loginLogConvertor.toDataObj((LoginEvent) domainEvent);
		loginLogMapper.insert(loginLogDO);
	}

}

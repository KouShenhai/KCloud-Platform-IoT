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

package org.laokou.iot.session.gatewayimpl;

import lombok.RequiredArgsConstructor;
import org.laokou.iot.session.convertor.SessionConvertor;
import org.laokou.iot.session.gateway.SessionGateway;
import org.laokou.iot.session.gatewayimpl.database.SessionMapper;
import org.laokou.iot.session.gatewayimpl.database.dataobject.SessionDO;
import org.laokou.iot.session.model.SessionA;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 会话网关实现.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class SessionGatewayImpl implements SessionGateway {

	private final SessionMapper sessionMapper;

	@Override
	public void createSession(SessionA sessionA) {
		sessionMapper.insert(SessionConvertor.toDataObject(sessionA));
	}

	@Override
	public void updateSession(SessionA sessionA) {
		SessionDO sessionDO = SessionConvertor.toDataObject(sessionA);
		sessionDO.setVersion(sessionMapper.selectVersion(sessionA.getId()));
		sessionMapper.updateById(sessionDO);
	}

	@Override
	public void deleteSession(Long[] ids) {
		sessionMapper.deleteByIds(Arrays.asList(ids));
	}

	private void verifyConnection(SessionA sessionA) {

	}

}

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

package org.laokou.iot.session.convertor;

import org.laokou.iot.session.dto.clientobject.SessionCO;
import org.laokou.iot.session.factory.SessionDomainFactory;
import org.laokou.iot.session.gatewayimpl.database.dataobject.SessionDO;
import org.laokou.iot.session.model.SessionA;
import org.laokou.iot.session.model.entity.SessionE;

import java.util.List;

/**
 * Network connection converter.
 *
 * @author laokou
 */
public final class SessionConvertor {

	private SessionConvertor() {
	}

	public static SessionDO toDataObject(SessionA sessionA) {
		SessionDO sessionDO = new SessionDO();
		SessionE sessionE = sessionA.getSessionE();
		sessionDO.setId(sessionA.getId());
		sessionDO.setName(sessionE.getName());
		sessionDO.setHost(sessionE.getHost());
		sessionDO.setPort(sessionE.getPort());
		sessionDO.setUsername(sessionE.getUsername());
		sessionDO.setPassword(sessionE.getPassword());
		sessionDO.setState(sessionE.getState());
		return sessionDO;
	}

	public static List<SessionCO> toClientObjects(List<SessionDO> list) {
		return list.stream().map(SessionConvertor::toClientObject).toList();
	}

	public static SessionCO toClientObject(SessionDO sessionDO) {
		SessionCO sessionCO = new SessionCO();
		sessionCO.setId(sessionDO.getId());
		sessionCO.setName(sessionDO.getName());
		sessionCO.setHost(sessionDO.getHost());
		sessionCO.setPort(sessionDO.getPort());
		sessionCO.setUsername(sessionDO.getUsername());
		sessionCO.setPassword(sessionDO.getPassword());
		sessionCO.setState(sessionDO.getState());
		sessionCO.setCreateTime(sessionDO.getCreateTime());
		return sessionCO;
	}

	public static SessionE toEntity(SessionCO SessionCO) {
		return SessionDomainFactory.createSessionE()
			.toBuilder()
			.id(SessionCO.getId())
			.name(SessionCO.getName())
			.host(SessionCO.getHost())
			.port(SessionCO.getPort())
			.username(SessionCO.getUsername())
			.password(SessionCO.getPassword())
			.state(SessionCO.getState())
			.build();
	}

}

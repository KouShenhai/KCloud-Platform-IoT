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
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.laokou.common.core.config.SystemSettingsProperties;
import org.laokou.common.mqtt.client.MqttClient;
import org.laokou.common.mqtt.client.MqttEventLoop;
import org.laokou.common.mqtt.client.NettyMqttClient;
import org.laokou.iot.session.convertor.SessionConvertor;
import org.laokou.iot.session.gateway.SessionGateway;
import org.laokou.iot.session.gatewayimpl.database.SessionMapper;
import org.laokou.iot.session.gatewayimpl.database.dataobject.SessionDO;
import org.laokou.iot.session.model.SessionA;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;

/**
 * 会话网关实现.
 *
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SessionGatewayImpl implements SessionGateway {

	private final SessionMapper sessionMapper;

	private final MqttEventLoop mqttEventLoop;

	private final SystemSettingsProperties systemSettingsProperties;

	@Override
	public void createSession(SessionA sessionA) throws MqttException {
		SessionDO sessionDO = SessionConvertor.toDataObject(sessionA);
		verifyConnect(sessionDO);
		sessionMapper.insert(sessionDO);
	}

	@Override
	public void updateSession(SessionA sessionA) throws MqttException {
		SessionDO sessionDO = SessionConvertor.toDataObject(sessionA);
		verifyConnect(sessionDO);
		sessionDO.setVersion(sessionMapper.selectVersion(sessionA.getId()));
		sessionMapper.updateById(sessionDO);
	}

	@Override
	public void deleteSession(Long[] ids) {
		sessionMapper.deleteByIds(Arrays.asList(ids));
	}

	@Override
	public void updateSessionState(SessionA sessionA) {
		SessionDO sessionDO = SessionConvertor.toDataObj(sessionA);
		sessionDO.setVersion(sessionMapper.selectVersion(sessionA.getId()));
		sessionMapper.updateById(sessionDO);
	}

	private void verifyConnect(SessionDO sessionDO) throws MqttException {
		MqttClient mqttClient = new NettyMqttClient(SessionConvertor.toProperties(sessionDO, systemSettingsProperties),
				mqttEventLoop, Collections.emptyList());
		mqttClient.test();
	}

}

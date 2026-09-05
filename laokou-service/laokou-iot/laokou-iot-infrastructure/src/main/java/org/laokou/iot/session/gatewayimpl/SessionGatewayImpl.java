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

import io.netty.handler.codec.mqtt.MqttVersion;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.iot.session.convertor.SessionConvertor;
import org.laokou.iot.session.gateway.SessionGateway;
import org.laokou.iot.session.gatewayimpl.database.SessionMapper;
import org.laokou.iot.session.gatewayimpl.database.dataobject.SessionDO;
import org.laokou.iot.session.model.SessionA;
import org.laokou.iot.session.model.entity.SessionE;
import org.springframework.stereotype.Component;

import java.util.Arrays;

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

	private final Vertx vertx;

	@Override
	public void createSession(SessionA sessionA) {
		verifyConnection(sessionA);
		sessionMapper.insert(SessionConvertor.toDataObject(sessionA));
	}

	@Override
	public void updateSession(SessionA sessionA) {
		verifyConnection(sessionA);
		SessionDO sessionDO = SessionConvertor.toDataObject(sessionA);
		sessionDO.setVersion(sessionMapper.selectVersion(sessionA.getId()));
		sessionMapper.updateById(sessionDO);
	}

	@Override
	public void deleteSession(Long[] ids) {
		sessionMapper.deleteByIds(Arrays.asList(ids));
	}

	private void verifyConnection(SessionA sessionA) {
		SessionE sessionE = sessionA.getSessionE();
		MqttClientOptions options = new MqttClientOptions();
		options.setCleanSession(true);
		options.setCleanSession(true);
		options.setVersion(MqttVersion.MQTT_5.protocolLevel());
		options.setUsername(sessionE.getUsername());
		options.setPassword(sessionE.getPassword());
		options.setConnectTimeout(1000);
		MqttClient client = MqttClient.create(vertx, options);
		client.connect(sessionE.getPort(), sessionE.getHost()).compose(_ -> {
			log.info("MQTT Broker连接成功");
			return client.disconnect();
		}).onSuccess(_ -> log.info("MQTT Broker连接关闭")).recover(ex -> {
			log.error("MQTT Broker连接失败，错误信息：{}", ex.getMessage(), ex);
			return Future.failedFuture(ex);
		}).toCompletionStage().toCompletableFuture().join();
	}

}

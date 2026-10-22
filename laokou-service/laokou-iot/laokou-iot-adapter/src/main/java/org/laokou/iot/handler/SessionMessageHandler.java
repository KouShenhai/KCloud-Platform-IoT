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

package org.laokou.iot.handler;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.SubscriptionType;
import org.apache.pulsar.common.schema.SchemaType;
import org.jspecify.annotations.NonNull;
import org.laokou.common.core.config.SystemSettingsProperties;
import org.laokou.common.fory.config.ForyFactory;
import org.laokou.common.mqtt.client.MqttClientManager;
import org.laokou.common.mqtt.client.MqttClientProperties;
import org.laokou.common.mqtt.client.MqttEventLoop;
import org.laokou.common.mqtt.client.listener.MessageListener;
import org.laokou.common.tenant.constant.DSConstants;
import org.laokou.iot.session.convertor.SessionConvertor;
import org.laokou.iot.session.dto.event.CloseSessionEvent;
import org.laokou.iot.session.dto.event.OpenSessionEvent;
import org.laokou.iot.session.gatewayimpl.database.SessionMapper;
import org.laokou.iot.session.gatewayimpl.database.dataobject.SessionDO;
import org.laokou.iot.session.model.enums.MqTopic;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.pulsar.annotation.PulsarListener;
import org.springframework.pulsar.annotation.PulsarListeners;
import org.springframework.pulsar.listener.AckMode;
import org.springframework.pulsar.listener.Acknowledgement;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public final class SessionMessageHandler implements ApplicationListener<@NonNull ApplicationReadyEvent> {

	private final SystemSettingsProperties systemSettingsProperties;

	private final SessionMapper sessionMapper;

	private final MqttClientManager mqttClientManager;

	private final List<MessageListener> listeners;

	private final MqttEventLoop mqttEventLoop;

	// @formatter:off
	@PulsarListeners(value = {
		@PulsarListener(
			topics = "persistent://${system-settings.tenant-code}/session/" + MqTopic.OPEN_SESSION_MESSAGE_TOPIC,
			subscriptionName = "${system-settings.tenant-code}-${random.uuid}",
			schemaType = SchemaType.BYTES,
			ackMode = AckMode.MANUAL,
			subscriptionType = SubscriptionType.Exclusive
		)
	})
	// @formatter:on
	public void handleOpenSessionMessage(byte[] msg, Acknowledgement ack) {
		try {
			if (ForyFactory.INSTANCE.deserialize(msg) instanceof OpenSessionEvent(Long id)) {
				try {
					DynamicDataSourceContextHolder.push(DSConstants.IOT);
					open(sessionMapper.selectById(id));
				}
				finally {
					DynamicDataSourceContextHolder.clear();
				}
			}
			ack.acknowledge();
		}
		catch (Exception ex) {
			log.error("handleOpenSessionMessage error", ex);
			ack.nack();
		}
	}

	// @formatter:off
	@PulsarListeners(value = {
		@PulsarListener(
			topics = "persistent://${system-settings.tenant-code}/session/" + MqTopic.CLOSE_SESSION_MESSAGE_TOPIC,
			subscriptionName = "${system-settings.tenant-code}-${random.uuid}",
			schemaType = SchemaType.BYTES,
			ackMode = AckMode.MANUAL,
			subscriptionType = SubscriptionType.Exclusive
		)
	})
	// @formatter:on
	public void handleCloseSessionMessage(byte[] msg, Acknowledgement ack) {
		try {
			if (ForyFactory.INSTANCE.deserialize(msg) instanceof CloseSessionEvent(Long id)) {
				close(id);
			}
			ack.acknowledge();
		}
		catch (Exception ex) {
			log.error("handleCloseSessionMessage error", ex);
			ack.nack();
		}
	}

	@Override
	public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
		try {
			DynamicDataSourceContextHolder.push(DSConstants.IOT);
			List<SessionDO> list = sessionMapper
				.selectList(Wrappers.lambdaQuery(SessionDO.class).eq(SessionDO::getState, 1));
			for (SessionDO sessionDO : list) {
				open(sessionDO);
			}
		}
		finally {
			DynamicDataSourceContextHolder.clear();
		}
	}

	private void open(SessionDO sessionDO) {
		MqttClientProperties properties = SessionConvertor.toProperties(sessionDO, systemSettingsProperties);
		mqttClientManager.open(sessionDO.getId(), properties, mqttEventLoop, listeners);
	}

	private void close(Long id) {
		mqttClientManager.close(id);
	}

}

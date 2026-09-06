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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.SubscriptionType;
import org.apache.pulsar.common.schema.SchemaType;
import org.laokou.common.fory.config.ForyFactory;
import org.laokou.iot.session.dto.event.CloseSessionEvent;
import org.laokou.iot.session.dto.event.OpenSessionEvent;
import org.laokou.iot.session.gatewayimpl.database.SessionMapper;
import org.laokou.iot.session.gatewayimpl.database.dataobject.SessionDO;
import org.laokou.iot.session.model.enums.MqTopic;
import org.springframework.pulsar.annotation.PulsarListener;
import org.springframework.pulsar.annotation.PulsarListeners;
import org.springframework.pulsar.listener.AckMode;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public final class SessionMessageHandler {

	private final SessionMapper sessionMapper;

	@PulsarListeners(value = { @PulsarListener(
			topics = "persistent://${system-settings.tenant-code}/session/" + MqTopic.OPEN_SESSION_MESSAGE_TOPIC,
			subscriptionName = "${system-settings.tenant-code}-${system-settings.client-id}", schemaType = SchemaType.BYTES,
			batch = true, ackMode = AckMode.BATCH, subscriptionType = SubscriptionType.Exclusive) })
	public void handleOpenSessionMessage(List<byte[]> messages) {
		for (byte[] message : messages) {
			if (ForyFactory.INSTANCE.deserialize(message) instanceof OpenSessionEvent(Long id)) {
				SessionDO sessionDO = sessionMapper.selectById(id);
			}
		}
	}

	@PulsarListeners(value = { @PulsarListener(
			topics = "persistent://${system-settings.tenant-code}/session/" + MqTopic.CLOSE_SESSION_MESSAGE_TOPIC,
			subscriptionName = "${system-settings.tenant-code}-${system-settings.client-id}", schemaType = SchemaType.BYTES,
			batch = true, ackMode = AckMode.BATCH, subscriptionType = SubscriptionType.Exclusive) })
	public void handleCloseSessionMessage(List<byte[]> messages) {
		for (byte[] message : messages) {
			if (ForyFactory.INSTANCE.deserialize(message) instanceof CloseSessionEvent(Long id)) {
				SessionDO sessionDO = sessionMapper.selectById(id);
			}
		}
	}

}

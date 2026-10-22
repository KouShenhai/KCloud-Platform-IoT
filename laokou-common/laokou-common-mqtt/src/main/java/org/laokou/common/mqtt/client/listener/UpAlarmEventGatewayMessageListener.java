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

package org.laokou.common.mqtt.client.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.MessageId;
import org.laokou.common.mqtt.client.message.MqttMessageType;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * 设备报警事件消息【上行】处理器.
 *
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
final class UpAlarmEventGatewayMessageListener implements MessageListener {

	@Override
	public CompletableFuture<MessageId> onMessage(MqttMessage message) {
		log.info("【Netty-MQTT-Client】 => 设备报警事件消息【上行】处理器，接收主题【{}】消息", message.topic());
		return CompletableFuture.completedFuture(null);
	}

	@Override
	public MqttMessageType getMessageType() {
		return MqttMessageType.UP_ALARM_EVENT_DEVICE_MESSAGE;
	}

}

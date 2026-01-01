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

package org.laokou.network.config.mqtt.handler;

import lombok.RequiredArgsConstructor;
import org.apache.pulsar.client.api.MessageId;
import org.laokou.network.model.MqttMessage;
import org.springframework.pulsar.core.PulsarTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * 属性修改回复【上行】处理器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class DownPropertyWriteReplyMqttMessageHandler implements MqttMessageHandler {

	private final PulsarTemplate<Object> pulsarTemplate;

	@Override
	public boolean isSubscribe(String topic) {
		return false;
		// return
		// VertxMqttUtils.matchTopic(MqttMessageEnum.UP_PROPERTY_WRITE_REPLY.getTopic(),
		// topic);
	}

	@Override
	public CompletableFuture<MessageId> handle(MqttMessage mqttMessage) {
		return null;
		// MqttMessageEnum upPropertyWriteReply = MqttMessageEnum.UP_PROPERTY_WRITE_REPLY;
		// String topic = TopicUtils.getTopic("laokouyun", "mqtt",
		// upPropertyWriteReply.getMqTopic());
		// return pulsarTemplate.sendAsync(topic, new
		// PropertyMessage(mqttMessage.getTopic(),
		// upPropertyWriteReply.getCode(), mqttMessage.getPayload().toString()));
	}

}

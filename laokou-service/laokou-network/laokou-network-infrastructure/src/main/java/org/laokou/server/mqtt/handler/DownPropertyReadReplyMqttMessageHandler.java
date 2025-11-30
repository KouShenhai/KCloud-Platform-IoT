/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.server.mqtt.handler;

import lombok.RequiredArgsConstructor;
import org.apache.pulsar.client.api.MessageId;
import org.laokou.common.i18n.util.JacksonUtils;
import org.laokou.common.network.mqtt.client.handler.MqttMessage;
import org.laokou.common.network.mqtt.client.handler.MqttMessageHandler;
import org.laokou.common.network.mqtt.client.util.VertxMqttUtils;
import org.laokou.common.vertx.model.MqttMessageEnum;
import org.laokou.common.vertx.model.PropertyMessage;
import org.springframework.pulsar.core.PulsarTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * 属性读取回复【上行】处理器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class DownPropertyReadReplyMqttMessageHandler implements MqttMessageHandler {

	private final PulsarTemplate<String> pulsarTemplate;

	@Override
	public boolean isSubscribe(String topic) {
		return VertxMqttUtils.matchTopic(MqttMessageEnum.UP_PROPERTY_READ_REPLY.getTopic(), topic);
	}

	@Override
	public CompletableFuture<MessageId> handle(MqttMessage mqttMessage) {
		MqttMessageEnum upPropertyReadReply = MqttMessageEnum.UP_PROPERTY_READ_REPLY;
		String topic = org.laokou.common.pulsar.util.TopicUtils.getTopic("laokouyun", "mqtt",
				upPropertyReadReply.getMqTopic());
		return pulsarTemplate.sendAsync(topic, JacksonUtils.toJsonStr(new PropertyMessage(mqttMessage.getTopic(),
				mqttMessage.getPayload().toString(), upPropertyReadReply.getCode())));
	}

}

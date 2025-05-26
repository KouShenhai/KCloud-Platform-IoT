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

package org.laokou.mqtt.server.handler;

import lombok.RequiredArgsConstructor;
import org.apache.pulsar.client.api.MessageId;
import org.laokou.common.i18n.util.JacksonUtils;
import org.laokou.common.network.mqtt.client.handler.MqttMessage;
import org.laokou.common.network.mqtt.client.handler.ReactiveMqttMessageHandler;
import org.laokou.common.network.mqtt.client.util.TopicUtils;
import org.laokou.common.vertx.model.MqttMessageEnum;
import org.laokou.common.vertx.model.PropertyMessage;
import org.springframework.pulsar.reactive.core.ReactivePulsarTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * 属性修改回复【上行】处理器.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class ReactiveDownPropertyWriteReplyMqttMessageHandler implements ReactiveMqttMessageHandler {

	private final ReactivePulsarTemplate<String> reactivePulsarTemplate;

	@Override
	public boolean isSubscribe(String topic) {
		return TopicUtils.match(MqttMessageEnum.UP_PROPERTY_WRITE_REPLY.getTopic(), topic);
	}

	@Override
	public Mono<MessageId> handle(MqttMessage mqttMessage) {
		MqttMessageEnum upPropertyWriteReply = MqttMessageEnum.UP_PROPERTY_WRITE_REPLY;
		String topic = org.laokou.common.pulsar.util.TopicUtils.getTopic("laokou", "mqtt",
				upPropertyWriteReply.getMqTopic());
		return reactivePulsarTemplate.send(topic, JacksonUtils.toJsonStr(new PropertyMessage(mqttMessage.getTopic(),
				mqttMessage.getPayload().toString(), upPropertyWriteReply.getCode())));
	}

}

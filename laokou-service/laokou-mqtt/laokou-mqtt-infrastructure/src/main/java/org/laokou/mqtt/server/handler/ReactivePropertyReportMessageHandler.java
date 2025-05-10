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
import org.laokou.common.kafka.template.KafkaSender;
import org.laokou.common.network.mqtt.client.handler.MqttMessage;
import org.laokou.common.network.mqtt.client.handler.ReactiveMessageHandler;
import org.laokou.common.network.mqtt.client.util.TopicUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import static org.laokou.common.network.mqtt.client.constant.MqConstants.LAOKOU_MQTT_PROPERTY_REPORT;

/**
 * 属性上报消息处理.
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class ReactivePropertyReportMessageHandler implements ReactiveMessageHandler {

	private final KafkaSender kafkaSender;

	@Override
	public boolean isSubscribe(String topic) {
		return TopicUtils.match("/+/+/property/report", topic);
	}

	@Override
	public Flux<Boolean> handle(MqttMessage mqttMessage) {
		return kafkaSender.send(LAOKOU_MQTT_PROPERTY_REPORT, mqttMessage.getPayload().toString());
	}

}

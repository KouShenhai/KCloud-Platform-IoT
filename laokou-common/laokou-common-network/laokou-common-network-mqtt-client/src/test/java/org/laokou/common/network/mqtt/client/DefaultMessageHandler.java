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

package org.laokou.common.network.mqtt.client;

import lombok.extern.slf4j.Slf4j;
import org.laokou.common.network.mqtt.client.handler.MqttMessage;
import org.laokou.common.network.mqtt.client.handler.MessageHandler;
import org.laokou.common.network.mqtt.client.util.TopicUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @author laokou
 */
@Slf4j
@Component
class DefaultMessageHandler implements MessageHandler {

	@Override
	public boolean isSubscribe(String topic) {
		return TopicUtils.match("/test-topic-1/#", topic);
	}

	@Async
	@Override
	public void handle(MqttMessage mqttMessage) {
		try {
			log.info("【Vertx-MQTT】 => 接收到MQTT消息，topic: {}, message: {}", mqttMessage.getTopic(),
					mqttMessage.getPayload().toString(StandardCharsets.UTF_8));
		}
		catch (DuplicateKeyException e) {
			// 忽略重复键异常
		}
		catch (Exception e) {
			log.error("【Vertx-MQTT】 => MQTT消息处理失败，Topic：{}，错误信息：{}", mqttMessage.getTopic(), e.getMessage(), e);
		}
	}

}

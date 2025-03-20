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

package org.laokou.test.mqtt.config;

import io.micrometer.common.lang.NonNullApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.mqtt.client.handler.MessageHandler;
import org.laokou.common.mqtt.client.config.MqttBrokerProperties;
import org.laokou.common.mqtt.config.PahoMqttClientManager;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * @author laokou
 */
@Slf4j
@Component
@NonNullApi
@RequiredArgsConstructor
public class MqttConfig implements ApplicationListener<ApplicationReadyEvent> {

	private final List<MessageHandler> messageHandlers;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		testPahoMqttClient();
	}

	private void testPahoMqttClient() {
		for (int i = 1; i <= 100; i++) {
			MqttBrokerProperties properties = new MqttBrokerProperties();
			properties.setClientId("test-" + i);
			properties.setTopics(Set.of("test-topic-" + i));
			PahoMqttClientManager.add(properties.getClientId(), properties, messageHandlers);
			// 启动MQTT客户端
			PahoMqttClientManager.open(properties.getClientId());
			try {
				Thread.sleep(1000);
			}
			catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}

}

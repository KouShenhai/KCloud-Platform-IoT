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

package org.laokou.common.mqtt;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.core.util.SpringContextUtils;
import org.laokou.common.mqtt.client.config.MqttClientProperties;
import org.laokou.common.mqtt.client.handler.MessageHandler;
import org.laokou.common.mqtt.config.HivemqMqttClientManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestConstructor;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

/**
 * @author laokou
 */
@SpringBootTest
@RequiredArgsConstructor
@ContextConfiguration(classes = { DefaultMessageHandler.class, SpringContextUtils.class })
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class HivemqMqttClientTest {

	private final List<MessageHandler> messageHandlers;

	@Test
	void testMqttClient() {
		MqttClientProperties properties = new MqttClientProperties();
		properties.setHost("127.0.0.1");
		properties.setPort(1883);
		properties.setUsername("emqx");
		properties.setPassword("laokou123");
		properties.setClientId("test-client-1");
		properties.setTopics(Set.of("$share/test-topic-1/#"));
		Assertions.assertDoesNotThrow(
				() -> HivemqMqttClientManager.add(properties.getClientId(), properties, messageHandlers));
		Assertions.assertDoesNotThrow(HivemqMqttClientManager::open);
		Assertions.assertDoesNotThrow(() -> HivemqMqttClientManager.publish(properties.getClientId(),
				"/test-topic-1/123", 1, "test11".getBytes(StandardCharsets.UTF_8)));
		Assertions.assertDoesNotThrow(HivemqMqttClientManager::destroy);
	}

}

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

import io.vertx.core.Vertx;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.network.mqtt.client.config.MqttClientProperties;
import org.laokou.common.network.mqtt.client.config.VertxConfig;
import org.laokou.common.network.mqtt.client.config.VertxMqttClient;
import org.laokou.common.network.mqtt.client.handler.MessageHandler;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestConstructor;
import java.util.List;
import java.util.Map;

/**
 * @author laokou
 */
@SpringBootTest
@RequiredArgsConstructor
@ContextConfiguration(classes = { DefaultMessageHandler.class, VertxConfig.class })
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class VertxMqttClientTest {

	private final List<MessageHandler> messageHandlers;

	private final Vertx vertx;

	@Test
	void testMqttClient() throws InterruptedException {
		MqttClientProperties properties = new MqttClientProperties();
		properties.setHost("127.0.0.1");
		properties.setPort(1883);
		properties.setUsername("emqx");
		properties.setPassword("laokou123");
		properties.setClientId("test-client-1");
		properties.setTopics(Map.of("/test-topic-1/#", 1));
		Assertions.assertDoesNotThrow(() -> {
			new VertxMqttClient(vertx, properties, messageHandlers).open();
		});
		Thread.sleep(1000);
	}

}

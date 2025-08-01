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
import org.junit.jupiter.api.Test;
import org.laokou.common.network.mqtt.client.config.SpringMqttClientProperties;
import org.laokou.common.network.mqtt.client.config.VertxMqttClient;
import org.laokou.common.network.mqtt.client.handler.MqttMessageHandler;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static org.assertj.core.api.Assertions.assertThatNoException;

/**
 * @author laokou
 */
@SpringBootTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class VertxMqttClient2Test {

	private final Vertx vertx;

	private final ExecutorService virtualThreadExecutor;

	private final List<MqttMessageHandler> mqttMessageHandlers;

	@Test
	void testMqttClientByQos1() throws InterruptedException {
		connectMqttServer(1);
	}

	@Test
	void testMqttClientByQos2() throws InterruptedException {
		connectMqttServer(2);
	}

	private void connectMqttServer(int qos) throws InterruptedException {
		for (int i = 1880; i <= 2000; i++) {
			if (i == 1883) {
				continue;
			}
			SpringMqttClientProperties properties = new SpringMqttClientProperties();
			properties.setHost("127.0.0.1");
			properties.setSubscribe(false);
			properties.setPort(i);
			properties.setUsername("vertx");
			properties.setPassword("laokou123");
			properties.setClientId("test-client-" + i);
			VertxMqttClient vertxMqttClient = new VertxMqttClient(vertx, virtualThreadExecutor, properties,
					mqttMessageHandlers);
			assertThatNoException().isThrownBy(vertxMqttClient::open);
			Thread.sleep(1000);
			assertThatNoException().isThrownBy(() -> vertxMqttClient.publish("/1/2/up/property/report", qos,
					"{\"id\":\"1\",\"name\":\"test\",\"value\":\"test\"}", false, false));
			Thread.sleep(1000);
			assertThatNoException().isThrownBy(vertxMqttClient::close);
		}
	}

}

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

package org.laokou.mqtt.server;

import io.vertx.core.Vertx;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.mqtt.server.config.MqttServerProperties;
import org.laokou.mqtt.server.config.VertxMqttServer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

/**
 * @author laokou
 */
@SpringBootTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class MqttServerTest {

	private final Vertx vertx;

	@Test
	void test() throws InterruptedException {
		MqttServerProperties properties = new MqttServerProperties();
		Assertions.assertDoesNotThrow(() -> properties.setPort(1884));
		VertxMqttServer vertxMqttServer = new VertxMqttServer(vertx, properties);
		Assertions.assertDoesNotThrow(vertxMqttServer::start);
		Thread.sleep(10000);
		Assertions.assertDoesNotThrow(vertxMqttServer::stop);
	}

}

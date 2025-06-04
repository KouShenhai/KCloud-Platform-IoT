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

package org.laokou.mqtt.server.config;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import org.laokou.common.network.mqtt.client.handler.ReactiveMqttMessageHandler;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author laokou
 */
public final class VertxMqttServerManager {

	private static volatile VertxMqttServer vertxMqttServer;

	private VertxMqttServerManager() {
	}

	public synchronized static void deployServer(final Vertx vertx, final SpringMqttServerProperties properties,
			final List<ReactiveMqttMessageHandler> reactiveMqttMessageHandlers) {
		vertxMqttServer = new VertxMqttServer(vertx, properties, reactiveMqttMessageHandlers);
		vertxMqttServer.deploy();
	}

	public static Mono<Future<Integer>> publishMessage(String clientId, String topic, int qos, Buffer payload) {
		return vertxMqttServer.publish(clientId, topic, qos, payload);
	}

}

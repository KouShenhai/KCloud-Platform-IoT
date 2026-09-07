/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.iot.common.config.mqtt;

import io.vertx.core.Vertx;
import org.laokou.iot.common.config.pulsar.handler.ConnectionStateHandler;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author laokou
 */
public final class VertxServiceManager {

	private static final Map<Long, VertxService> VERTX_SERVICE_MAP = new ConcurrentHashMap<>(8192);

	public static void deployVertxMqttClientService(Vertx vertx, MqttClientConfig config,
			ConnectionStateHandler connectionStateHandler, List<MessageHandler> messageHandlers) {
		VERTX_SERVICE_MAP
			.computeIfAbsent(config.getSnowflakeId(),
					_ -> new VertxMqttClientService(vertx, config, connectionStateHandler, messageHandlers))
			.deploy();
	}

	public static void unDeployVertxMqttClientService(Long id) {
		if (VERTX_SERVICE_MAP.containsKey(id)) {
			VERTX_SERVICE_MAP.get(id).undeploy();
			VERTX_SERVICE_MAP.remove(id);
		}
	}

}

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

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.ThreadingModel;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;
import org.laokou.iot.common.config.pulsar.handler.ConnectionStateHandler;
import java.util.List;

/**
 * @author laokou
 */
@Slf4j
final class VertxMqttClientService extends AbstractVertxService {

	private final Vertx vertx;

	private final MqttClientConfig config;

	private final ConnectionStateHandler connectionStateHandler;

	private final List<MessageHandler> messageHandlers;

	public VertxMqttClientService(Vertx vertx, MqttClientConfig config, ConnectionStateHandler connectionStateHandler,
			List<MessageHandler> messageHandlers) {
		this.vertx = vertx;
		this.config = config;
		this.connectionStateHandler = connectionStateHandler;
		this.messageHandlers = messageHandlers;
	}

	@Override
	public Future<String> doDeploy() {
		return vertx
			.deployVerticle(() -> new VertxMqttClient(vertx, config, connectionStateHandler, messageHandlers),
					buildOptions())
			.onSuccess(deploymentId -> log.info("【Vertx-MQTT-Client】 => MQTT服务部署成功，deploymentId：{}", deploymentId))
			.onFailure(ex -> log.error("【Vertx-MQTT-Client】 => MQTT服务部署失败", ex));
	}

	@Override
	public void doUndeploy() {
		deploymentIdFuture.get().compose(vertx::undeploy).onSuccess(ignored -> {
			log.info("【Vertx-MQTT-Client】 => MQTT服务卸载成功");
		}).onFailure(ex -> log.error("【Vertx-MQTT-Client】 => MQTT服务卸载失败", ex));
	}

	private DeploymentOptions buildOptions() {
		DeploymentOptions deploymentOptions = new DeploymentOptions();
		deploymentOptions.setThreadingModel(ThreadingModel.VIRTUAL_THREAD);
		deploymentOptions.setInstances(4);
		return deploymentOptions;
	}

}

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
import org.laokou.common.core.util.CollectionExtUtils;
import org.laokou.common.i18n.common.exception.BizException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author laokou
 */
@Slf4j
final class VertxMqttClientService extends AbstractVertxService {

	private final Vertx vertx;

	private final MqttClientConfig config;

	private final List<MessageHandler> messageHandlers;

	private volatile List<VertxMqttClient> vertxMqttClients;

	public VertxMqttClientService(Vertx vertx, MqttClientConfig config, List<MessageHandler> messageHandlers) {
		this.vertx = vertx;
		this.config = config;
		this.messageHandlers = messageHandlers;
		this.vertxMqttClients = null;
	}

	@Override
	public Future<String> doDeploy() {
		List<VertxMqttClient> clients = new ArrayList<>(4);
		return vertx.deployVerticle(() -> {
			VertxMqttClient vertxMqttClient = new VertxMqttClient(vertx, config, messageHandlers);
			clients.add(vertxMqttClient);
			return vertxMqttClient;
		}, buildOptions()).onSuccess(deploymentId -> {
			vertxMqttClients = List.copyOf(clients);
			log.info("【Vertx-MQTT-Client】 => MQTT服务部署成功，deploymentId：{}", deploymentId);
		}).onFailure(ex -> log.error("【Vertx-MQTT-Client】 => MQTT服务部署失败", ex));
	}

	@Override
	public void doUndeploy() {
		deploymentIdFuture.compose(vertx::undeploy).onSuccess(ignored -> {
			log.info("【Vertx-MQTT-Client】 => MQTT服务卸载成功");
		}).onFailure(ex -> log.error("【Vertx-MQTT-Client】 => MQTT服务卸载失败", ex));
	}

	@Override
	public void publish(PublishMessageConfig config) {
		try {
			List<VertxMqttClient> clients = vertxMqttClients;
			if (CollectionExtUtils.isEmpty(clients)) {
				throw new BizException("B_Mqtt_ClientNotInitializedOrDeployFailed", "MQTT客户端尚未初始化完成或部署失败");
			}
			int index = ThreadLocalRandom.current().nextInt(clients.size());
			VertxMqttClient vertxMqttClient = clients.get(index);
			vertxMqttClient.publish(config.topic(), config.qos(), config.payload(), config.isDup(), config.isRetain());
		}
		catch (Exception ex) {
			log.error("MQTT发布调用异常，错误信息：{}", ex.getMessage(), ex);
			throw ex;
		}
	}

	private DeploymentOptions buildOptions() {
		DeploymentOptions deploymentOptions = new DeploymentOptions();
		deploymentOptions.setThreadingModel(ThreadingModel.VIRTUAL_THREAD);
		deploymentOptions.setInstances(4);
		return deploymentOptions;
	}

}

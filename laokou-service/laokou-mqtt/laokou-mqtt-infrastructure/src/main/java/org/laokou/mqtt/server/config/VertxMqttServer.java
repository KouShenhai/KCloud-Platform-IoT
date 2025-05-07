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

import io.vertx.core.Vertx;
import io.vertx.mqtt.MqttServer;
import io.vertx.mqtt.MqttServerOptions;
import io.vertx.mqtt.MqttTopicSubscription;
import io.vertx.mqtt.messages.MqttPublishMessage;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.util.ThreadUtils;
import org.laokou.common.i18n.util.ObjectUtils;
import reactor.core.Disposable;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @author laokou
 */
@Slf4j
public final class VertxMqttServer {

	private final Sinks.Many<MqttPublishMessage> messageSink = Sinks.many()
		.multicast()
		.onBackpressureBuffer(Integer.MAX_VALUE, false);

	private final MqttServer mqttServer;

	private final MqttServerProperties properties;

	private final List<Disposable> disposables;

	public VertxMqttServer(final Vertx vertx, final MqttServerProperties properties) {
		this.properties = properties;
		mqttServer = MqttServer.create(vertx, getMqttServerOptions());
		this.disposables = Collections.synchronizedList(new ArrayList<>());
	}

	public void start() {
		mqttServer
			.exceptionHandler(
					error -> log.error("【Vertx-MQTT-Server】 => MQTT服务启动失败，错误信息：{}", error.getMessage(), error))
			.endpointHandler(endpoint -> {
				endpoint.subscribeHandler(subscribe -> {
					for (MqttTopicSubscription topicSubscription : subscribe.topicSubscriptions()) {
						log.info("【Vertx-MQTT-Server】 => MQTT客户端订阅主题：{}", topicSubscription.topicName());
					}
				});
				endpoint.publishHandler(messageSink::tryEmitNext);
				// 不保留会话
				endpoint.accept(false);
			})
			.listen(properties.getPort(), properties.getHost(), asyncResult -> {
				if (asyncResult.succeeded()) {
					log.info("【Vertx-MQTT-Server】 => MQTT服务启动成功，主机：{}，端口：{}", properties.getHost(),
							properties.getPort());
					try (ExecutorService virtualTaskExecutor = ThreadUtils.newVirtualTaskExecutor()) {
						virtualTaskExecutor.execute(this::publish);
					}
				}
				else {
					log.error("【Vertx-MQTT-Server】 => MQTT服务启动失败，错误信息：{}", asyncResult.cause().getMessage(),
							asyncResult.cause());
				}
			});
	}

	public void stop() {
		mqttServer.close(completionHandler -> {
			if (completionHandler.succeeded()) {
				disposable();
				log.info("【Vertx-MQTT-Server】 => MQTT服务停止成功");
				disposables.clear();
			}
			else {
				log.error("【Vertx-MQTT-Server】 => MQTT服务停止失败，错误信息：{}", completionHandler.cause().getMessage(),
						completionHandler.cause());
			}
		});
	}

	private void disposable() {
		for (Disposable disposable : disposables) {
			if (ObjectUtils.isNotNull(disposable) && !disposable.isDisposed()) {
				disposable.dispose();
			}
		}
	}

	private void publish() {
		Disposable disposable = messageSink.asFlux().doOnNext(mqttPublishMessage -> {
			log.info("【Vertx-MQTT-Server】 => MQTT服务收到消息，主题：{}，内容：{}", mqttPublishMessage.topicName(),
					mqttPublishMessage.payload());
		}).subscribeOn(Schedulers.boundedElastic()).subscribe();
		disposables.add(disposable);
	}

	private MqttServerOptions getMqttServerOptions() {
		MqttServerOptions mqttServerOptions = new MqttServerOptions();
		mqttServerOptions.setHost(properties.getHost());
		mqttServerOptions.setPort(properties.getPort());
		mqttServerOptions.setMaxMessageSize(properties.getMaxMessageSize());
		mqttServerOptions.setAutoClientId(properties.isAutoClientId());
		mqttServerOptions.setMaxClientIdLength(properties.getMaxClientIdLength());
		mqttServerOptions.setTimeoutOnConnect(properties.getTimeoutOnConnect());
		mqttServerOptions.setUseWebSocket(properties.isUseWebSocket());
		mqttServerOptions.setWebSocketMaxFrameSize(properties.getWebSocketMaxFrameSize());
		mqttServerOptions
			.setPerFrameWebSocketCompressionSupported(properties.isPerFrameWebSocketCompressionSupported());
		mqttServerOptions
			.setPerMessageWebSocketCompressionSupported(properties.isPerMessageWebSocketCompressionSupported());
		mqttServerOptions.setWebSocketCompressionLevel(properties.getWebSocketCompressionLevel());
		mqttServerOptions.setWebSocketAllowServerNoContext(properties.isWebSocketAllowServerNoContext());
		mqttServerOptions.setWebSocketPreferredClientNoContext(properties.isWebSocketPreferredClientNoContext());
		mqttServerOptions.setTcpNoDelay(properties.isTcpNoDelay());
		mqttServerOptions.setTcpKeepAlive(properties.isTcpKeepAlive());
		mqttServerOptions.setTcpKeepAliveIdleSeconds(properties.getTcpKeepAliveIdleSeconds());
		mqttServerOptions.setTcpKeepAliveIntervalSeconds(properties.getTcpKeepAliveIntervalSeconds());
		mqttServerOptions.setTcpKeepAliveCount(properties.getTcpKeepAliveCount());
		mqttServerOptions.setSoLinger(properties.getSoLinger());
		mqttServerOptions.setIdleTimeout(properties.getIdleTimeout());
		mqttServerOptions.setReadIdleTimeout(properties.getReadIdleTimeout());
		mqttServerOptions.setWriteIdleTimeout(properties.getWriteIdleTimeout());
		mqttServerOptions.setIdleTimeoutUnit(properties.getIdleTimeoutUnit());
		mqttServerOptions.setSsl(properties.isSsl());
		mqttServerOptions.setTcpFastOpen(properties.isTcpFastOpen());
		mqttServerOptions.setTcpCork(properties.isTcpCork());
		mqttServerOptions.setTcpQuickAck(properties.isTcpQuickAck());
		mqttServerOptions.setTcpUserTimeout(properties.getTcpUserTimeout());
		return mqttServerOptions;
	}

}

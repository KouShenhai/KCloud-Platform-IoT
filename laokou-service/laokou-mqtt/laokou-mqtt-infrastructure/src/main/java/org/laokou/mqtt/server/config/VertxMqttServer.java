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

import io.netty.handler.codec.mqtt.MqttConnectReturnCode;
import io.vertx.core.Vertx;
import io.vertx.mqtt.*;
import io.vertx.mqtt.messages.MqttPublishMessage;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.network.mqtt.client.handler.MqttMessage;
import org.laokou.common.network.mqtt.client.handler.ReactiveMessageHandler;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import java.util.List;
import java.util.Optional;

/**
 * @author laokou
 */
@Slf4j
final class VertxMqttServer {

	private final Sinks.Many<MqttPublishMessage> messageSink = Sinks.many()
		.multicast()
		.onBackpressureBuffer(Integer.MAX_VALUE, false);

	private volatile Flux<MqttServer> mqttServer;

	private final Vertx vertx;

	private final MqttServerProperties properties;

	private final List<ReactiveMessageHandler> reactiveMessageHandlers;

	private volatile boolean isClosed = false;

	public VertxMqttServer(final Vertx vertx, final MqttServerProperties properties,
			List<ReactiveMessageHandler> reactiveMessageHandlers) {
		this.properties = properties;
		this.vertx = vertx;
		this.reactiveMessageHandlers = reactiveMessageHandlers;
	}

	public Flux<MqttServer> start() {
		return mqttServer = getMqttServerOptions().map(mqttServerOption -> MqttServer.create(vertx, mqttServerOption)
			.exceptionHandler(
					error -> log.error("【Vertx-MQTT-Server】 => MQTT服务启动失败，错误信息：{}", error.getMessage(), error))
			.endpointHandler(endpoint -> Optional.ofNullable(authHandler(endpoint))
				.ifPresent(e -> e.closeHandler(close -> log.info("【Vertx-MQTT-Server】 => MQTT客户端断开连接"))
					.subscribeHandler(subscribe -> {
						for (MqttTopicSubscription topicSubscription : subscribe.topicSubscriptions()) {
							log.info("【Vertx-MQTT-Server】 => MQTT客户端订阅主题：{}", topicSubscription.topicName());
						}
					})
					.disconnectHandler(disconnect -> log.info("【Vertx-MQTT-Server】 => MQTT客户端主动断开连接"))
					.pingHandler(ping -> log.info("【Vertx-MQTT-Server】 => MQTT客户端发送心跳"))
					.publishHandler(messageSink::tryEmitNext)
					// 不保留会话
					.accept(false)))
			.listen(mqttServerOption.getPort(), mqttServerOption.getHost(), asyncResult -> {
				if (isClosed) {
					return;
				}
				if (asyncResult.succeeded()) {
					log.info("【Vertx-MQTT-Server】 => MQTT服务启动成功，主机：{}，端口：{}", mqttServerOption.getHost(),
							mqttServerOption.getPort());
				}
				else {
					log.error("【Vertx-MQTT-Server】 => MQTT服务启动失败，主机：{}，端口：{}，错误信息：{}", mqttServerOption.getHost(),
							mqttServerOption.getPort(), asyncResult.cause().getMessage(), asyncResult.cause());
				}
			}));
	}

	public Flux<MqttServer> stop() {
		isClosed = true;
		return mqttServer.doOnNext(server -> server.close(completionHandler -> {
			if (completionHandler.succeeded()) {
				log.info("【Vertx-MQTT-Server】 => MQTT服务停止成功");
			}
			else {
				log.error("【Vertx-MQTT-Server】 => MQTT服务停止失败，错误信息：{}", completionHandler.cause().getMessage(),
						completionHandler.cause());
			}
		}));
	}

	public Flux<Boolean> publish() {
		return messageSink.asFlux().flatMap(message -> {
			// @formatter:off
				// log.info("【Vertx-MQTT-Server】 => MQTT服务接收到消息，主题：{}，内容：{}", message.topicName(), message.payload().toString());
				// @formatter:on
			return Flux
				.fromStream(reactiveMessageHandlers.stream()
					.filter(reactiveMessageHandler -> reactiveMessageHandler.isSubscribe(message.topicName())))
				.flatMap(reactiveMessageHandler -> reactiveMessageHandler
					.handle(new MqttMessage(message.payload(), message.topicName())));
		});
	}

	private Flux<MqttServerOptions> getMqttServerOptions() {
		return Flux.fromIterable(properties.getPorts()).map(this::getMqttServerOption);
	}

	private MqttEndpoint authHandler(MqttEndpoint endpoint) {
		MqttAuth mqttAuth = endpoint.auth();
		if (properties.isAuth()) {
			if (ObjectUtils.isNull(mqttAuth)) {
				endpoint.reject(MqttConnectReturnCode.CONNECTION_REFUSED_BAD_USERNAME_OR_PASSWORD);
				return null;
			}
			if (!ObjectUtils.equals(mqttAuth.getUsername(), properties.getUsername())
					|| !ObjectUtils.equals(mqttAuth.getPassword(), properties.getPassword())) {
				endpoint.reject(MqttConnectReturnCode.CONNECTION_REFUSED_BAD_USERNAME_OR_PASSWORD);
				return null;
			}
		}
		return endpoint;
	}

	// @formatter:off
	private MqttServerOptions getMqttServerOption(int port) {
		MqttServerOptions mqttServerOptions = new MqttServerOptions();
		mqttServerOptions.setHost(properties.getHost());
		mqttServerOptions.setPort(port);
		mqttServerOptions.setMaxMessageSize(properties.getMaxMessageSize());
		mqttServerOptions.setAutoClientId(properties.isAutoClientId());
		mqttServerOptions.setMaxClientIdLength(properties.getMaxClientIdLength());
		mqttServerOptions.setTimeoutOnConnect(properties.getTimeoutOnConnect());
		mqttServerOptions.setUseWebSocket(properties.isUseWebSocket());
		mqttServerOptions.setWebSocketMaxFrameSize(properties.getWebSocketMaxFrameSize());
		mqttServerOptions.setPerFrameWebSocketCompressionSupported(properties.isPerFrameWebSocketCompressionSupported());
		mqttServerOptions.setPerMessageWebSocketCompressionSupported(properties.isPerMessageWebSocketCompressionSupported());
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
	// @formatter:on

}

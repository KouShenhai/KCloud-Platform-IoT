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
import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.mqtt.MqttAuth;
import io.vertx.mqtt.MqttEndpoint;
import io.vertx.mqtt.MqttServer;
import io.vertx.mqtt.MqttServerOptions;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.network.mqtt.client.handler.MqttMessage;
import org.laokou.common.network.mqtt.client.handler.MqttMessageHandler;
import org.laokou.common.network.mqtt.client.util.VertxMqttUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author laokou
 */
@Slf4j
final class VertxMqttServer extends AbstractVerticle implements VertxServer {

	private final Map<String, MqttEndpoint> endpoints = new ConcurrentHashMap<>(100000);

	private final List<MqttMessageHandler> mqttMessageHandlers;

	private final MqttServerProperties mqttServerProperties;

	private final MqttServerOptions mqttServerOptions;

	private Future<MqttServer> mqttServerFuture;

	private Future<String> deploymentIdFuture;

	VertxMqttServer(final Vertx vertx, final MqttServerProperties mqttServerProperties,
			final List<MqttMessageHandler> mqttMessageHandlers) {
		super.vertx = vertx;
		this.mqttServerProperties = mqttServerProperties;
		this.mqttServerOptions = getMqttServerOption(mqttServerProperties);
		this.mqttMessageHandlers = mqttMessageHandlers;
	}

	@Override
	public void start() {
		mqttServerFuture = start0();
	}

	@Override
	public void stop() {
		mqttServerFuture = stop0();
	}

	@Override
	public void deploy() {
		// 部署服务
		deploymentIdFuture = deploy0();
	}

	@Override
	public void undeploy() {
		deploymentIdFuture = undeploy0();
	}

	public void publish(PublishDTO dto) {
		String clientId = dto.clientId();
		String topic = dto.topic();
		Buffer payload = dto.payload();
		int qos = dto.qos();
		boolean isDup = dto.isDup();
		boolean isRetain = dto.isRetain();
		if (endpoints.containsKey(clientId)) {
			endpoints.get(clientId).publish(topic, payload, VertxMqttUtils.convertQos(qos), isDup, isRetain);
		}
		throw new UnsupportedOperationException();
	}

	private Future<MqttServer> start0() {
		return MqttServer.create(vertx, mqttServerOptions)
		// @formatter:off
			.exceptionHandler(
				error -> log.error("【Vertx-MQTT-Server】 => MQTT服务启动失败，错误信息：{}", error.getMessage(), error))
			.endpointHandler(endpoint -> Optional.ofNullable(authHandler(endpoint)).ifPresent(mqttEndpoint ->
				mqttEndpoint.closeHandler(_ -> {
						endpoints.remove(endpoint.clientIdentifier());
						log.info("【Vertx-MQTT-Server】 => MQTT客户端断开连接");
					})
					// 【Vertx-MQTT-Server】 => MQTT客户端订阅主题
					// .subscribeHandler(MqttSubscribeMessage::topicSubscriptions)
					.disconnectHandler(_ -> {
						endpoints.remove(endpoint.clientIdentifier());
						log.info("【Vertx-MQTT-Server】 => MQTT客户端主动断开连接");
					})
					.pingHandler(_ -> log.debug("【Vertx-MQTT-Server】 => 接收MQTT客户端心跳"))
					.publishHandler(mqttPublishMessage -> {
						int messageId = mqttPublishMessage.messageId();
						MqttQoS mqttQoS = mqttPublishMessage.qosLevel();
						if (ObjectUtils.equals(mqttQoS, MqttQoS.AT_LEAST_ONCE)) {
							// 【接收客户端发布消息】
							// 如果 QoS 等级是 1（AT_LEAST_ONCE），endpoint 需要使用 publishAcknowledge 方法回复一个PUBACK消息给客户端
							endpoint.publishAcknowledge(messageId);
							log.debug("【Vertx-MQTT-Server】 => 发送PUBACK数据包给客户端【Qos=1】，消息ID：{}【客户端发布】", messageId);
						} else if (ObjectUtils.equals(mqttQoS ,MqttQoS.EXACTLY_ONCE)) {
							// 【接收客户端发布消息】
							// 如果 QoS 等级是 2（EXACTLY_ONCE），endpoint 需要使用publishReceived方法回复一个PUBREC消息给客户端
							endpoint.publishReceived(messageId);
							log.debug("【Vertx-MQTT-Server】 => 发送PUBREL数据包给客户端【Qos=2】，消息ID：{}【客户端发布】", messageId);
						}
						for (MqttMessageHandler mqttMessageHandler : mqttMessageHandlers) {
							if (mqttMessageHandler.isSubscribe(mqttPublishMessage.topicName())) {
								mqttMessageHandler.handle(new MqttMessage(mqttPublishMessage.payload(), mqttPublishMessage.topicName())).thenAcceptAsync(messageId0 -> log.debug("【Vertx-MQTT-Server】 => 消息发布到Pulsar成功，消息ID：{}", messageId0));
							}
						}
					})
					// 【Vertx-MQTT-Server】 => 接收 PUBREC 响应并回复客户端，【服务端发布】"
					.publishReceivedHandler(mqttEndpoint::publishRelease)
					.publishAcknowledgeHandler(messageId -> log.debug("【Vertx-MQTT-Server】 => 接收 PUBACK 响应，消息ID：{}【服务端发布】", messageId))
					/*
					  【接收客户端发布消息】
					  在这种情况下，这个 endpoint 同时也要通过publishReleaseHandler指定一个 handler 来处理来自客户端的PUBREL（远程客户端接收到 endpoint 发送的 PUBREC 后发送的）消息 为了结束 QoS 等级为2的消息的传递，
					  endpoint 可以使用publishComplete方法发送一个 PUBCOMP 消息给客户端
					 */
					.publishReleaseHandler(messageId -> {
						endpoint.publishComplete(messageId);
						log.debug("【Vertx-MQTT-Server】 => 发送PUBCOMP数据包给客户端【Qos=2】，消息ID：{}【客户端发布】", messageId);
					})
					.publishCompletionHandler(messageId -> log.debug("【Vertx-MQTT-Server】 => 接收 PUBCOMP 响应，消息ID：{}【服务端发布】", messageId))
					// @formatter:on
				.accept(false)))
			.listen()
			.onComplete(asyncResult -> {
				if (asyncResult.succeeded()) {
					log.info("【Vertx-MQTT-Server】 => MQTT服务启动成功，端口：{}", mqttServerOptions.getPort());
				}
				else {
					log.error("【Vertx-MQTT-Server】 => MQTT服务启动失败，端口：{}，错误信息：{}", mqttServerOptions.getPort(),
							asyncResult.cause().getMessage(), asyncResult.cause());
				}
			});
	}

	private Future<MqttServer> stop0() {
		return mqttServerFuture.onSuccess(MqttServer::close).onComplete(result -> {
			if (result.succeeded()) {
				log.info("【Vertx-MQTT-Server】 => MQTT服务停止成功，端口：{}", mqttServerOptions.getPort());
			}
			else {
				Throwable ex = result.cause();
				log.error("【Vertx-MQTT-Server】 => MQTT服务停止失败，错误信息：{}", ex.getMessage(), ex);
			}
		});
	}

	private Future<String> deploy0() {
		return vertx.deployVerticle(this).onComplete(res -> {
			if (res.succeeded()) {
				log.info("【Vertx-MQTT-Server】 => MQTT服务部署成功，端口：{}", mqttServerOptions.getPort());
			}
			else {
				Throwable ex = res.cause();
				log.error("【Vertx-MQTT-Server】 => MQTT服务部署失败，错误信息：{}", ex.getMessage(), ex);
			}
		});
	}

	private Future<String> undeploy0() {
		return deploymentIdFuture.onSuccess(deploymentId -> vertx.undeploy(deploymentId)).onComplete(res -> {
			if (res.succeeded()) {
				log.info("【Vertx-MQTT-Server】 => MQTT服务卸载成功");
			}
			else {
				log.error("【Vertx-MQTT-Server】 => MQTT服务卸载失败，错误信息：{}", res.cause().getMessage(), res.cause());
			}
		});
	}

	private MqttEndpoint authHandler(MqttEndpoint endpoint) {
		MqttAuth mqttAuth = endpoint.auth();
		if (mqttServerProperties.isAuth()) {
			if (ObjectUtils.isNull(mqttAuth)) {
				endpoint.reject(MqttConnectReturnCode.CONNECTION_REFUSED_BAD_USERNAME_OR_PASSWORD);
				return null;
			}
			if (!ObjectUtils.equals(mqttAuth.getUsername(), mqttServerProperties.getUsername())
					|| !ObjectUtils.equals(mqttAuth.getPassword(), mqttServerProperties.getPassword())) {
				endpoint.reject(MqttConnectReturnCode.CONNECTION_REFUSED_BAD_USERNAME_OR_PASSWORD);
				return null;
			}
		}
		// 认证成功，保存连接
		endpoints.put(endpoint.clientIdentifier(), endpoint);
		return endpoint;
	}

	// @formatter:off
	private MqttServerOptions getMqttServerOption(MqttServerProperties mqttServerProperties) {
		MqttServerOptions mqttServerOptions = new MqttServerOptions();
		mqttServerOptions.setHost(mqttServerProperties.getHost());
		mqttServerOptions.setPort(mqttServerProperties.getPort());
		mqttServerOptions.setMaxMessageSize(mqttServerProperties.getMaxMessageSize());
		mqttServerOptions.setAutoClientId(mqttServerProperties.isAutoClientId());
		mqttServerOptions.setMaxClientIdLength(mqttServerProperties.getMaxClientIdLength());
		mqttServerOptions.setTimeoutOnConnect(mqttServerProperties.getTimeoutOnConnect());
		mqttServerOptions.setUseWebSocket(mqttServerProperties.isUseWebSocket());
		mqttServerOptions.setWebSocketMaxFrameSize(mqttServerProperties.getWebSocketMaxFrameSize());
		mqttServerOptions.setPerFrameWebSocketCompressionSupported(mqttServerProperties.isPerFrameWebSocketCompressionSupported());
		mqttServerOptions.setPerMessageWebSocketCompressionSupported(mqttServerProperties.isPerMessageWebSocketCompressionSupported());
		mqttServerOptions.setWebSocketCompressionLevel(mqttServerProperties.getWebSocketCompressionLevel());
		mqttServerOptions.setWebSocketAllowServerNoContext(mqttServerProperties.isWebSocketAllowServerNoContext());
		mqttServerOptions.setWebSocketPreferredClientNoContext(mqttServerProperties.isWebSocketPreferredClientNoContext());
		mqttServerOptions.setTcpNoDelay(mqttServerProperties.isTcpNoDelay());
		mqttServerOptions.setTcpKeepAlive(mqttServerProperties.isTcpKeepAlive());
		mqttServerOptions.setSoLinger(mqttServerProperties.getSoLinger());
		mqttServerOptions.setIdleTimeout(mqttServerProperties.getIdleTimeout());
		mqttServerOptions.setReadIdleTimeout(mqttServerProperties.getReadIdleTimeout());
		mqttServerOptions.setWriteIdleTimeout(mqttServerProperties.getWriteIdleTimeout());
		mqttServerOptions.setIdleTimeoutUnit(mqttServerProperties.getIdleTimeoutUnit());
		mqttServerOptions.setSsl(mqttServerProperties.isSsl());
		mqttServerOptions.setTcpFastOpen(mqttServerProperties.isTcpFastOpen());
		mqttServerOptions.setTcpCork(mqttServerProperties.isTcpCork());
		mqttServerOptions.setTcpQuickAck(mqttServerProperties.isTcpQuickAck());
		mqttServerOptions.setTcpUserTimeout(mqttServerProperties.getTcpUserTimeout());
		return mqttServerOptions;
	}
	// @formatter:on

	record PublishDTO(String clientId, String topic, Buffer payload, int qos, boolean isDup,
			boolean isRetain) implements Serializable {

	}

}

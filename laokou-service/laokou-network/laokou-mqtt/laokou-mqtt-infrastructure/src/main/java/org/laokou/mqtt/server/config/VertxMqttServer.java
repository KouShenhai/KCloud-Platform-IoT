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
import io.netty.handler.codec.mqtt.MqttProperties;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.mqtt.*;
import io.vertx.mqtt.messages.MqttAuthenticationExchangeMessage;
import io.vertx.mqtt.messages.MqttPublishMessage;
import io.vertx.mqtt.messages.codes.MqttAuthenticateReasonCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.MessageId;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.network.mqtt.client.handler.MqttMessage;
import org.laokou.common.network.mqtt.client.handler.ReactiveMqttMessageHandler;
import org.laokou.common.network.mqtt.client.util.VertxMqttUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author laokou
 */
@Slf4j
final class VertxMqttServer extends AbstractVerticle {

	private final Map<String, MqttEndpoint> endpoints = new ConcurrentHashMap<>(8192);

	private final Sinks.Many<MqttPublishMessage> messageSink = Sinks.many()
		.multicast()
		.onBackpressureBuffer(Integer.MAX_VALUE, false);

	private volatile Flux<MqttServer> mqttServer;

	private final SpringMqttServerProperties properties;

	private final List<ReactiveMqttMessageHandler> reactiveMqttMessageHandlers;

	private boolean isClosed = false;

	VertxMqttServer(final Vertx vertx, final SpringMqttServerProperties properties,
			List<ReactiveMqttMessageHandler> reactiveMqttMessageHandlers) {
		this.properties = properties;
		super.vertx = vertx;
		this.reactiveMqttMessageHandlers = reactiveMqttMessageHandlers;
	}

	@Override
	public void start() {
		mqttServer = getMqttServerOptions().map(options -> MqttServer.create(vertx, options))
		// @formatter:off
			.doOnNext(server -> server
				.exceptionHandler(
						error -> log.error("【Vertx-MQTT-Server】 => MQTT服务启动失败，错误信息：{}", error.getMessage(), error))
				.endpointHandler(endpoint -> Optional.ofNullable(authHandler(endpoint)).ifPresent(mqttEndpoint ->
				mqttEndpoint.closeHandler(close -> {
						endpoints.remove(endpoint.clientIdentifier());
						log.info("【Vertx-MQTT-Server】 => MQTT客户端断开连接");
					})
					// 【Vertx-MQTT-Server】 => MQTT客户端订阅主题
					// .subscribeHandler(MqttSubscribeMessage::topicSubscriptions)
					.disconnectHandler(disconnect -> {
						endpoints.remove(endpoint.clientIdentifier());
						log.info("【Vertx-MQTT-Server】 => MQTT客户端主动断开连接");
					})
					// .pingHandler(ping -> log.info("【Vertx-MQTT-Server】 => 接收MQTT客户端心跳"))
						.publishHandler(mqttPublishMessage -> {
							messageSink.tryEmitNext(mqttPublishMessage);
							int messageId = mqttPublishMessage.messageId();
							MqttQoS mqttQoS = mqttPublishMessage.qosLevel();
							if (ObjectUtils.equals(mqttQoS, MqttQoS.AT_LEAST_ONCE)) {
								// 【接收客户端发布消息】
								// 如果 QoS 等级是 1（AT_LEAST_ONCE），endpoint 需要使用 publishAcknowledge 方法回复一个PUBACK消息给客户端
								endpoint.publishAcknowledge(messageId);
								// log.info("【Vertx-MQTT-Server】 => 发送PUBACK数据包给客户端【Qos=1】，消息ID：{}【客户端发布】", messageId);
							} else if (ObjectUtils.equals(mqttQoS ,MqttQoS.EXACTLY_ONCE)) {
								// 【接收客户端发布消息】
								// 如果 QoS 等级是 2（EXACTLY_ONCE），endpoint 需要使用publishReceived方法回复一个PUBREC消息给客户端
								endpoint.publishReceived(messageId);
								// log.info("【Vertx-MQTT-Server】 => 发送PUBREL数据包给客户端【Qos=2】，消息ID：{}【客户端发布】", messageId);
							}
						})
						// 【Vertx-MQTT-Server】 => 接收 PUBREC 响应并回复客户端，【服务端发布】"
						.publishReceivedHandler(mqttEndpoint::publishRelease)
						.publishAcknowledgeHandler(messageId -> {
							// log.info("【Vertx-MQTT-Server】 => 接收 PUBACK 响应，消息ID：{}【服务端发布】", messageId);
						})
						/*
						   【接收客户端发布消息】
						  在这种情况下，这个 endpoint 同时也要通过publishReleaseHandler指定一个 handler 来处理来自客户端的PUBREL（远程客户端接收到 endpoint 发送的 PUBREC 后发送的）消息 为了结束 QoS 等级为2的消息的传递，
						  endpoint 可以使用publishComplete方法发送一个 PUBCOMP 消息给客户端
						 */
						.publishReleaseHandler(messageId -> {
							endpoint.publishComplete(messageId);
							log.info("【Vertx-MQTT-Server】 => 发送PUBCOMP数据包给客户端【Qos=2】，消息ID：{}【客户端发布】", messageId);
						})
						.publishCompletionHandler(messageId -> log.info("【Vertx-MQTT-Server】 => 接收 PUBCOMP 响应，消息ID：{}【服务端发布】", messageId))
						// @formatter:on
					.accept(false)))
				.listen()
				.onComplete(asyncResult -> {
					if (isClosed) {
						return;
					}
					if (asyncResult.succeeded()) {
						log.info("【Vertx-MQTT-Server】 => MQTT服务启动成功，端口：{}", server.actualPort());
					}
					else {
						log.error("【Vertx-MQTT-Server】 => MQTT服务启动失败，端口：{}，错误信息：{}", server.actualPort(),
								asyncResult.cause().getMessage(), asyncResult.cause());
					}
				}));
		mqttServer.subscribeOn(Schedulers.boundedElastic()).subscribe();
	}

	@Override
	public void stop() {
		isClosed = true;
		mqttServer.doOnNext(server -> server.close().onComplete(result -> {
			if (result.succeeded()) {
				log.info("【Vertx-MQTT-Server】 => MQTT服务停止成功，端口：{}", server.actualPort());
			}
			else {
				Throwable ex = result.cause();
				log.error("【Vertx-MQTT-Server】 => MQTT服务停止失败，错误信息：{}", ex.getMessage(), ex);
			}
		})).subscribeOn(Schedulers.boundedElastic()).subscribe(server -> {
			// 清理资源
			endpoints.clear();
			messageSink.asFlux().subscribe().dispose();
			log.info("【Vertx-MQTT-Server】 => MQTT服务资源清理完成");
		});
	}

	public void deploy() {
		// 部署服务
		vertx.deployVerticle(this);
		// 拉取数据
		pull().subscribeOn(Schedulers.boundedElastic()).subscribe();
		// 停止服务
		Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
	}

	public Mono<Future<Integer>> publish(String clientId, String topic, int qos, Buffer payload) {
		return Mono.fromFuture(CompletableFuture.completedFuture(
				endpoints.get(clientId).publish(topic, payload, VertxMqttUtils.convertQos(qos), false, false)));
	}

	private Flux<MessageId> pull() {
		return messageSink.asFlux()
			.flatMap(message -> Flux
				.fromStream(reactiveMqttMessageHandlers.stream()
					.filter(reactiveMessageHandler -> reactiveMessageHandler.isSubscribe(message.topicName())))
				.flatMap(reactiveMessageHandler -> reactiveMessageHandler
					.handle(new MqttMessage(message.payload(), message.topicName()))));
	}

	private Flux<MqttServerOptions> getMqttServerOptions() {
		return Flux.fromIterable(properties.getPorts()).map(this::getMqttServerOption);
	}

	private MqttEndpoint authHandler(MqttEndpoint endpoint) {
		// MQTT5
		if (properties.isMqtt5()) {
			endpoint
				.authenticationExchange(MqttAuthenticationExchangeMessage.create(MqttAuthenticateReasonCode.SUCCESS,
						MqttProperties.NO_PROPERTIES))
				.authenticationExchangeHandler(auth -> {
					if (ObjectUtils.equals(MqttAuthenticateReasonCode.SUCCESS, auth.reasonCode())) {
						log.info("【Vertx-MQTT-Server】 => MQTT5认证成功");
					}
					else {
						log.info("【Vertx-MQTT-Server】 => MQTT5认证失败，原因：{}", auth.reasonCode());
					}
				});
		}
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
		// 认证成功，保存连接
		endpoints.put(endpoint.clientIdentifier(), endpoint);
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

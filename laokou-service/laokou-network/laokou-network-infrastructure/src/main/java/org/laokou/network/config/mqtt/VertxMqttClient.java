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

package org.laokou.network.config.mqtt;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;
import lombok.extern.slf4j.Slf4j;
import org.laokou.network.config.AbstractVertxService;
import org.laokou.network.config.mqtt.handler.MqttMessageHandler;
import org.laokou.network.model.MqttMessage;
import org.laokou.network.util.VertxMqttUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author laokou
 */
@Slf4j
final class VertxMqttClient extends AbstractVertxService<Void> {

	private final MqttClientProperties mqttClientProperties;

	private final MqttClientOptions mqttClientOptions;

	private final List<MqttMessageHandler> mqttMessageHandlers;

	private final ExecutorService executorService;

	private final MqttClient mqttClient;

	private final AtomicBoolean disconnect;

	VertxMqttClient(Vertx vertx, MqttClientProperties mqttClientProperties,
			List<MqttMessageHandler> mqttMessageHandlers, ExecutorService executorService) {
		super(vertx);
		this.mqttClientOptions = getMqttClientOptions(mqttClientProperties);
		this.mqttClientProperties = mqttClientProperties;
		this.mqttMessageHandlers = mqttMessageHandlers;
		this.executorService = executorService;
		this.mqttClient = init();
		this.disconnect = new AtomicBoolean(false);
	}

	@Override
	public Future<String> deploy0() {
		return super.vertx.deployVerticle(this).onComplete(res -> {
			if (res.succeeded()) {
				log.info("【Vertx-MQTT-Client】 => MQTT服务部署成功，端口：{}", mqttClientProperties.getPort());
			}
			else {
				Throwable ex = res.cause();
				log.error("【Vertx-MQTT-Client】 => MQTT服务部署失败，错误信息：{}", ex.getMessage(), ex);
			}
		});
	}

	@Override
	public Future<String> undeploy0() {
		return deploymentIdFuture.onSuccess(deploymentId -> this.vertx.undeploy(deploymentId)).onComplete(res -> {
			if (res.succeeded()) {
				log.info("【Vertx-MQTT-Client】 => MQTT服务卸载成功，端口：{}", mqttClientProperties.getPort());
			}
			else {
				log.error("【Vertx-MQTT-Client】 => MQTT服务卸载失败，错误信息：{}", res.cause().getMessage(), res.cause());
			}
		});
	}

	@Override
	public Future<Void> start0() {
		mqttClient.connect(mqttClientProperties.getPort(), mqttClientProperties.getHost()).onComplete(connectResult -> {
			if (connectResult.succeeded()) {
				log.info("【Vertx-MQTT-Client】 => MQTT连接成功，主机：{}，端口：{}，客户端ID：{}", mqttClientProperties.getHost(),
						mqttClientProperties.getPort(), mqttClientProperties.getClientId());
				subscribe();
			}
			else {
				Throwable ex = connectResult.cause();
				log.error("【Vertx-MQTT-Client】 => MQTT连接失败，原因：{}，客户端ID：{}", ex.getMessage(),
						mqttClientProperties.getClientId(), ex);
			}
		});
		return Future.succeededFuture();
	}

	@Override
	public Future<Void> stop0() {
		if (disconnect.compareAndSet(false, true)) {
			mqttClient.disconnect().onComplete(disconnectResult -> {
				if (disconnectResult.succeeded()) {
					log.info("【Vertx-MQTT-Client】 => MQTT断开连接成功");
				}
				else {
					Throwable ex = disconnectResult.cause();
					log.error("【Vertx-MQTT-Client】 => MQTT断开连接失败，错误信息：{}", ex.getMessage(), ex);
				}
			});
		}
		return Future.succeededFuture();
	}

	/**
	 * Sends the PUBLISH message to the remote MQTT server.
	 * @param topic topic on which the message is published
	 * @param payload message payload
	 * @param qos QoS level
	 * @param isDup if the message is a duplicate
	 * @param isRetain if the message needs to be retained
	 */
	public void publish(String topic, int qos, String payload, boolean isDup, boolean isRetain) {
		mqttClient.publish(topic, Buffer.buffer(payload), VertxMqttUtils.convertQos(qos), isDup, isRetain);
	}

	private void restart() {
		if (disconnect.get()) {
			return;
		}
		log.debug("【Vertx-MQTT-Client】 => MQTT尝试重连");
		vertx.setTimer(mqttClientProperties.getReconnectInterval(), _ -> executorService.execute(this::start0));
	}

	private void subscribe() {
		Map<String, Integer> topics = mqttClientProperties.getTopics();
		mqttClient.subscribe(topics).onComplete(subscribeResult -> {
			if (subscribeResult.succeeded()) {
				log.info("【Vertx-MQTT-Client】 => MQTT订阅成功，主题: {}", String.join("、", topics.keySet()));
			}
			else {
				Throwable ex = subscribeResult.cause();
				log.error("【Vertx-MQTT-Client】 => MQTT订阅失败，主题：{}，错误信息：{}", String.join("、", topics.keySet()),
						ex.getMessage(), ex);
			}
		});
	}

	private MqttClient init() {
		return MqttClient.create(vertx, mqttClientOptions).closeHandler(_ -> {
			log.error("【Vertx-MQTT-Client】 => MQTT连接断开，客户端ID：{}", mqttClientOptions.getClientId());
			restart();
		}).publishHandler(publishHandler -> {
			String topic = publishHandler.topicName();
			log.debug("【Vertx-MQTT-Client】 => MQTT接收到消息，Topic：{}", topic);
			for (MqttMessageHandler mqttMessageHandler : mqttMessageHandlers) {
				if (mqttMessageHandler.isSubscribe(topic)) {
					mqttMessageHandler.handle(new MqttMessage(publishHandler.payload(), topic))
						.thenRunAsync(() -> log.debug("Pulsar消息发送完毕"));
				}
			}
		})
			// 仅接收QoS1和QoS2的数据包
			.publishCompletionHandler(
					messageId -> log.debug("【Vertx-MQTT-Client】 => 接收MQTT的PUBACK或PUBCOMP数据包，数据包ID：{}", messageId))
			.subscribeCompletionHandler(
					ack -> log.debug("【Vertx-MQTT-Client】 => 接收MQTT的SUBACK数据包，数据包ID：{}", ack.messageId()))
			.unsubscribeCompletionHandler(id -> log.debug("【Vertx-MQTT-Client】 => 接收MQTT的UNSUBACK数据包，数据包ID：{}", id))
			.pingResponseHandler(_ -> log.debug("【Vertx-MQTT-Client】 => 接收MQTT的PINGRESP数据包"));
	}

	private MqttClientOptions getMqttClientOptions(MqttClientProperties mqttClientProperties) {
		MqttClientOptions options = new MqttClientOptions();
		options.setClientId(mqttClientProperties.getClientId());
		options.setCleanSession(mqttClientProperties.isClearSession());
		options.setAutoKeepAlive(mqttClientProperties.isAutoKeepAlive());
		options.setKeepAliveInterval(mqttClientProperties.getKeepAliveInterval());
		options.setReconnectAttempts(mqttClientProperties.getReconnectAttempts());
		options.setReconnectInterval(mqttClientProperties.getReconnectInterval());
		options.setWillQoS(mqttClientProperties.getWillQos());
		options.setWillTopic(mqttClientProperties.getWillTopic());
		options.setAutoAck(mqttClientProperties.isAutoAck());
		options.setAckTimeout(mqttClientProperties.getAckTimeout());
		options.setWillRetain(mqttClientProperties.isWillRetain());
		options.setWillMessageBytes(Buffer.buffer(mqttClientProperties.getWillPayload()));
		options.setReceiveBufferSize(mqttClientProperties.getReceiveBufferSize());
		options.setMaxMessageSize(mqttClientProperties.getMaxMessageSize());
		if (mqttClientProperties.isAuth()) {
			options.setPassword(mqttClientProperties.getPassword());
			options.setUsername(mqttClientProperties.getUsername());
		}
		return options;
	}

}

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

package org.laokou.common.network.mqtt.client.config;

import io.netty.handler.codec.mqtt.MqttProperties;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;
import io.vertx.mqtt.messages.MqttAuthenticationExchangeMessage;
import io.vertx.mqtt.messages.MqttPublishMessage;
import io.vertx.mqtt.messages.codes.MqttAuthenticateReasonCode;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.util.CollectionUtils;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.StringUtils;
import org.laokou.common.network.mqtt.client.handler.MqttMessageHandler;
import org.laokou.common.network.mqtt.client.handler.MqttMessage;
import org.laokou.common.network.mqtt.client.util.VertxMqttUtils;
import reactor.core.Disposable;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author laokou
 */
@Slf4j
public final class VertxMqttClient {

	private final Sinks.Many<MqttPublishMessage> messageSink = Sinks.many()
		.multicast()
		.onBackpressureBuffer(Integer.MAX_VALUE, false);

	private final MqttClient mqttClient;

	private final Vertx vertx;

	private final ExecutorService virtualThreadExecutor;

	private final SpringMqttClientProperties springMqttClientProperties;

	private final List<MqttMessageHandler> mqttMessageHandlers;

	private final AtomicBoolean isConnected = new AtomicBoolean(false);

	private final AtomicBoolean isLoaded = new AtomicBoolean(false);

	private final AtomicBoolean isReconnected = new AtomicBoolean(true);

	private volatile Disposable consumeDisposable;

	public VertxMqttClient(final Vertx vertx, ExecutorService virtualThreadExecutor,
			final SpringMqttClientProperties springMqttClientProperties,
			final List<MqttMessageHandler> mqttMessageHandlers) {
		this.vertx = vertx;
		this.virtualThreadExecutor = virtualThreadExecutor;
		this.springMqttClientProperties = springMqttClientProperties;
		this.mqttClient = MqttClient.create(vertx, getOptions());
		this.mqttMessageHandlers = mqttMessageHandlers;
	}

	public void open() {
		if (springMqttClientProperties.isMqtt5()) {
			mqttClient.authenticationExchange(MqttAuthenticationExchangeMessage
				.create(MqttAuthenticateReasonCode.SUCCESS, MqttProperties.NO_PROPERTIES));
			mqttClient.authenticationExchangeHandler(auth -> {
				if (ObjectUtils.equals(MqttAuthenticateReasonCode.SUCCESS, auth.reasonCode())) {
					log.info("【Vertx-MQTT-Client】 => MQTT5认证成功");
				}
				else {
					log.info("【Vertx-MQTT-Client】 => MQTT5认证失败，原因：{}", auth.reasonCode());
				}
			});
		}
		mqttClient.closeHandler(v -> {
			isConnected.set(false);
			log.error("【Vertx-MQTT-Client】 => MQTT连接断开，客户端ID：{}", springMqttClientProperties.getClientId());
			reconnect();
		}).publishHandler(messageSink::tryEmitNext)
		// @formatter:off
			 // 仅接收QoS1和QoS2的数据包
			.publishCompletionHandler(messageId -> log.info("【Vertx-MQTT-Client】 => 接收MQTT的PUBACK或PUBCOMP数据包，数据包ID：{}", messageId))
			.subscribeCompletionHandler(ack -> {
				// log.info("【Vertx-MQTT-Client】 => 接收MQTT的SUBACK数据包，数据包ID：{}", ack.messageId());
			})
			.unsubscribeCompletionHandler(id -> {
				// log.info("【Vertx-MQTT-Client】 => 接收MQTT的UNSUBACK数据包，数据包ID：{}", id);
			})
			.pingResponseHandler(s -> {
				// log.info("【Vertx-MQTT-Client】 => 接收MQTT的PINGRESP数据包");
			})
			// @formatter:on
			.connect(springMqttClientProperties.getPort(), springMqttClientProperties.getHost())
			.onComplete(connectResult -> {
				if (connectResult.succeeded()) {
					isConnected.set(true);
					log.info("【Vertx-MQTT-Client】 => MQTT连接成功，主机：{}，端口：{}，客户端ID：{}",
							springMqttClientProperties.getHost(), springMqttClientProperties.getPort(),
							springMqttClientProperties.getClientId());
					resubscribe();
				}
				else {
					isConnected.set(false);
					Throwable ex = connectResult.cause();
					log.error("【Vertx-MQTT-Client】 => MQTT连接失败，原因：{}，客户端ID：{}", ex.getMessage(),
							springMqttClientProperties.getClientId(), ex);
					reconnect();
				}
			});
	}

	public void close() {
		disconnect();
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

	private void reconnect() {
		if (isReconnected.get()) {
			try {
				log.info("【Vertx-MQTT-Client】 => MQTT尝试重连");
				vertx.setTimer(springMqttClientProperties.getReconnectInterval(),
						handler -> virtualThreadExecutor.execute(this::open));
			}
			catch (Exception e) {
				Thread.currentThread().interrupt();
				reconnect();
			}
		}
	}

	private void subscribe() {
		Map<String, Integer> topics = springMqttClientProperties.getTopics();
		checkTopicAndQos(topics);
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

	private void resubscribe() {
		if ((isConnected.get() || mqttClient.isConnected()) && springMqttClientProperties.isSubscribe()) {
			virtualThreadExecutor.execute(this::subscribe);
		}
		if (isLoaded.compareAndSet(false, true)) {
			virtualThreadExecutor.execute(this::consumeMessage);
		}
	}

	private void consumeMessage() {
		consumeDisposable = messageSink.asFlux().doOnNext(mqttPublishMessage -> {
			String topic = mqttPublishMessage.topicName();
			log.info("【Vertx-MQTT-Client】 => MQTT接收到消息，Topic：{}", topic);
			for (MqttMessageHandler mqttMessageHandler : mqttMessageHandlers) {
				if (mqttMessageHandler.isSubscribe(topic)) {
					mqttMessageHandler.handle(new MqttMessage(mqttPublishMessage.payload(), topic));
				}
			}
		}).subscribeOn(Schedulers.boundedElastic()).subscribe();
	}

	private void disposable(Disposable disposable) {
		if (ObjectUtils.isNotNull(disposable) && !disposable.isDisposed()) {
			disposable.dispose();
		}
	}

	private void disposables() {
		disposable(consumeDisposable);
	}

	private void disconnect() {
		isReconnected.set(false);
		mqttClient.disconnect().onComplete(disconnectResult -> {
			if (disconnectResult.succeeded()) {
				log.info("【Vertx-MQTT-Client】 => MQTT断开连接成功");
				disposables();
			}
			else {
				Throwable ex = disconnectResult.cause();
				log.error("【Vertx-MQTT-Client】 => MQTT断开连接失败，错误信息：{}", ex.getMessage(), ex);
			}
		});
	}

	private void unsubscribe(List<String> topics) {
		checkTopic(topics);
		mqttClient.unsubscribe(topics).onComplete(unsubscribeResult -> {
			if (unsubscribeResult.succeeded()) {
				log.info("【Vertx-MQTT-Client】 => MQTT取消订阅成功，主题：{}", String.join("、", topics));
			}
			else {
				Throwable ex = unsubscribeResult.cause();
				log.error("【Vertx-MQTT-Client】 => MQTT取消订阅失败，主题：{}，错误信息：{}", String.join("、", topics), ex.getMessage(),
						ex);
			}
		});
	}

	private MqttClientOptions getOptions() {
		MqttClientOptions options = new MqttClientOptions();
		options.setClientId(springMqttClientProperties.getClientId());
		options.setCleanSession(springMqttClientProperties.isClearSession());
		options.setAutoKeepAlive(springMqttClientProperties.isAutoKeepAlive());
		options.setKeepAliveInterval(springMqttClientProperties.getKeepAliveInterval());
		options.setReconnectAttempts(springMqttClientProperties.getReconnectAttempts());
		options.setReconnectInterval(springMqttClientProperties.getReconnectInterval());
		options.setWillQoS(springMqttClientProperties.getWillQos());
		options.setWillTopic(springMqttClientProperties.getWillTopic());
		options.setAutoAck(springMqttClientProperties.isAutoAck());
		options.setAckTimeout(springMqttClientProperties.getAckTimeout());
		options.setWillRetain(springMqttClientProperties.isWillRetain());
		options.setWillMessageBytes(Buffer.buffer(springMqttClientProperties.getWillPayload()));
		options.setReceiveBufferSize(springMqttClientProperties.getReceiveBufferSize());
		options.setMaxMessageSize(springMqttClientProperties.getMaxMessageSize());
		if (springMqttClientProperties.isAuth()) {
			options.setPassword(springMqttClientProperties.getPassword());
			options.setUsername(springMqttClientProperties.getUsername());
		}
		return options;
	}

	private void checkTopicAndQos(Map<String, Integer> topics) {
		topics.forEach((topic, qos) -> {
			if (StringUtils.isEmpty(topic) || ObjectUtils.isNull(qos)) {
				throw new IllegalArgumentException("【Vertx-MQTT-Client】 => Topic and QoS cannot be null");
			}
		});
	}

	private void checkTopic(List<String> topics) {
		if (CollectionUtils.isEmpty(topics)) {
			throw new IllegalArgumentException("【Vertx-MQTT-Client】 => Topics list cannot be empty");
		}
	}

}

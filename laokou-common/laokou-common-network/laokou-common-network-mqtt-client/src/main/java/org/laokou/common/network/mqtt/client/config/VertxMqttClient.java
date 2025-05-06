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

import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;
import io.vertx.mqtt.messages.MqttPublishMessage;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.util.CollectionUtils;
import org.laokou.common.core.util.ThreadUtils;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.StringUtils;
import org.laokou.common.network.mqtt.client.handler.MessageHandler;
import org.laokou.common.network.mqtt.client.handler.MqttMessage;
import reactor.core.Disposable;
import reactor.core.publisher.Sinks;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author laokou
 */
@Slf4j
public class VertxMqttClient {

	private final Sinks.Many<MqttPublishMessage> messageSink = Sinks.many()
		.multicast()
		.onBackpressureBuffer(Integer.MAX_VALUE, false);

	private final MqttClient mqttClient;

	private final Vertx vertx;

	private final MqttClientProperties mqttClientProperties;

	private final List<MessageHandler> messageHandlers;

	private final List<Disposable> disposables;

	private final AtomicBoolean isConnected = new AtomicBoolean(false);

	private final AtomicBoolean isLoaded = new AtomicBoolean(false);

	private final AtomicBoolean isReconnected = new AtomicBoolean(true);

	public VertxMqttClient(final Vertx vertx, final MqttClientProperties mqttClientProperties,
			final List<MessageHandler> messageHandlers) {
		this.vertx = vertx;
		this.mqttClientProperties = mqttClientProperties;
		this.mqttClient = MqttClient.create(vertx, getOptions());
		this.messageHandlers = messageHandlers;
		this.disposables = Collections.synchronizedList(new ArrayList<>());
	}

	public void open() {
		mqttClient.closeHandler(v -> {
			isConnected.set(false);
			log.error("【Vertx-MQTT】 => MQTT连接断开，客户端ID：{}", mqttClientProperties.getClientId());
			reconnect();
		})
			.publishHandler(messageSink::tryEmitNext)
			// 仅接收QoS1和QoS2的消息
			.publishCompletionHandler(id -> {
				// log.info("【Vertx-MQTT】 => 接收MQTT的PUBACK或PUBCOMP数据包，数据包ID：{}", id);
			})
			.subscribeCompletionHandler(ack -> {
				// log.info("【Vertx-MQTT】 => 接收MQTT的SUBACK数据包，数据包ID：{}", ack.messageId());
			})
			.unsubscribeCompletionHandler(id -> {
				// log.info("【Vertx-MQTT】 => 接收MQTT的UNSUBACK数据包，数据包ID：{}", id);
			})
			.pingResponseHandler(s -> {
				// log.info("【Vertx-MQTT】 => 接收MQTT的PINGRESP数据包");
			})
			.connect(mqttClientProperties.getPort(), mqttClientProperties.getHost(), connectResult -> {
				if (connectResult.succeeded()) {
					isConnected.set(true);
					log.info("【Vertx-MQTT】 => MQTT连接成功，主机：{}，端口：{}，客户端ID：{}", mqttClientProperties.getHost(),
							mqttClientProperties.getPort(), mqttClientProperties.getClientId());
					resubscribe();
				}
				else {
					isConnected.set(false);
					Throwable ex = connectResult.cause();
					log.error("【Vertx-MQTT】 => MQTT连接失败，原因：{}，客户端ID：{}", ex.getMessage(),
							mqttClientProperties.getClientId(), ex);
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
		mqttClient.publish(topic, Buffer.buffer(payload), convertQos(qos), isDup, isRetain);
	}

	private void reconnect() {
		if (isReconnected.get()) {
			log.info("【Vertx-MQTT】 => MQTT尝试重连");
			vertx.setTimer(mqttClientProperties.getReconnectInterval(), handler -> {
				try (ExecutorService virtualTaskExecutor = ThreadUtils.newVirtualTaskExecutor()) {
					virtualTaskExecutor.execute(this::open);
				}
			});
		}
	}

	private void subscribe() {
		Map<String, Integer> topics = mqttClientProperties.getTopics();
		checkTopicAndQos(topics);
		mqttClient.subscribe(topics, subscribeResult -> {
			if (subscribeResult.succeeded()) {
				log.info("【Vertx-MQTT】 => MQTT订阅成功，主题: {}", String.join("、", topics.keySet()));
			}
			else {
				Throwable ex = subscribeResult.cause();
				log.error("【Vertx-MQTT】 => MQTT订阅失败，主题：{}，错误信息：{}", String.join("、", topics.keySet()), ex.getMessage(),
						ex);
			}
		});
	}

	private void resubscribe() {
		try (ExecutorService virtualTaskExecutor = ThreadUtils.newVirtualTaskExecutor()) {
			if (isConnected.get() || mqttClient.isConnected()) {
				virtualTaskExecutor.execute(this::subscribe);
			}
			if (isLoaded.compareAndSet(false, true)) {
				virtualTaskExecutor.execute(this::consume);
			}
		}
	}

	private void consume() {
		Disposable disposable = messageSink.asFlux().doOnNext(mqttPublishMessage -> {
			String topic = mqttPublishMessage.topicName();
			log.info("【Vertx-MQTT】 => MQTT接收到消息，Topic：{}", topic);
			for (MessageHandler messageHandler : messageHandlers) {
				if (messageHandler.isSubscribe(topic)) {
					messageHandler.handle(new MqttMessage(mqttPublishMessage.payload(), topic));
				}
			}
		}).subscribeOn(Schedulers.boundedElastic()).subscribe();
		disposables.add(disposable);
	}

	private void disposable() {
		for (Disposable disposable : disposables) {
			if (ObjectUtils.isNotNull(disposable) && !disposable.isDisposed()) {
				disposable.dispose();
			}
		}
	}

	private void disconnect() {
		isReconnected.set(false);
		mqttClient.disconnect(disconnectResult -> {
			if (disconnectResult.succeeded()) {
				disposable();
				log.info("【Vertx-MQTT】 => MQTT断开连接成功");
				disposables.clear();
			}
			else {
				Throwable ex = disconnectResult.cause();
				log.error("【Vertx-MQTT】 => MQTT断开连接失败，错误信息：{}", ex.getMessage(), ex);
			}
		});
	}

	private void unsubscribe(List<String> topics) {
		checkTopic(topics);
		mqttClient.unsubscribe(topics, unsubscribeResult -> {
			if (unsubscribeResult.succeeded()) {
				log.info("【Vertx-MQTT】 => MQTT取消订阅成功，主题：{}", String.join("、", topics));
			}
			else {
				Throwable ex = unsubscribeResult.cause();
				log.error("【Vertx-MQTT】 => MQTT取消订阅失败，主题：{}，错误信息：{}", String.join("、", topics), ex.getMessage(), ex);
			}
		});
	}

	private MqttClientOptions getOptions() {
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

	private void checkTopicAndQos(Map<String, Integer> topics) {
		topics.forEach((topic, qos) -> {
			if (StringUtils.isEmpty(topic) || ObjectUtils.isNull(qos)) {
				throw new IllegalArgumentException("【Vertx-MQTT】 => Topic and QoS cannot be null");
			}
		});
	}

	private void checkTopic(List<String> topics) {
		if (CollectionUtils.isEmpty(topics)) {
			throw new IllegalArgumentException("【Vertx-MQTT】 => Topics list cannot be empty");
		}
	}

	private MqttQoS convertQos(int qos) {
		return MqttQoS.valueOf(qos);
	}

}

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

package org.laokou.common.mqtt.config;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttActionListener;
import org.eclipse.paho.mqttv5.client.MqttAsyncClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.persist.MqttDefaultFilePersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;
import org.laokou.common.core.util.EventBus;
import org.laokou.common.core.util.CollectionUtils;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.mqtt.client.AbstractMqttClient;
import org.laokou.common.mqtt.client.config.MqttClientProperties;
import org.laokou.common.mqtt.client.handler.event.CloseEvent;
import org.laokou.common.mqtt.client.handler.event.OpenEvent;
import org.laokou.common.mqtt.client.handler.event.SubscribeEvent;
import org.laokou.common.mqtt.client.handler.event.UnsubscribeEvent;
import org.laokou.common.mqtt.client.handler.MessageHandler;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * MQTT客户端.
 *
 * @author laokou
 */
@Slf4j
public class PahoMqttClient extends AbstractMqttClient {

	private final MqttClientProperties mqttClientProperties;

	private final List<MessageHandler> messageHandlers;

	private final ScheduledExecutorService executor;

	private volatile MqttAsyncClient client;

	private final Object LOCK = new Object();

	private final AtomicInteger ATOMIC = new AtomicInteger(0);

	public PahoMqttClient(MqttClientProperties mqttClientProperties, List<MessageHandler> messageHandlers,
			ScheduledExecutorService executor) {
		this.mqttClientProperties = mqttClientProperties;
		this.messageHandlers = messageHandlers;
		this.executor = executor;
	}

	public void open() {
		try {
			if (ObjectUtils.isNull(client)) {
				synchronized (LOCK) {
					if (ObjectUtils.isNull(client)) {
						String clientId = mqttClientProperties.getClientId();
						client = new MqttAsyncClient(
								"tcp://" + mqttClientProperties.getHost() + ":" + mqttClientProperties.getPort(),
								clientId, new MqttDefaultFilePersistence(), null, executor);
						client.setManualAcks(mqttClientProperties.isManualAcks());
						client.setCallback(new PahoMqttClientMessageCallback(messageHandlers, mqttClientProperties));
						client.connect(options(), null, new MqttActionListener() {
							@Override
							public void onSuccess(IMqttToken asyncActionToken) {
								log.info("【Paho】 => MQTT连接成功，客户端ID：{}", clientId);
								// 发布订阅事件
								publishSubscribeEvent(mqttClientProperties.getTopics(),
										mqttClientProperties.getSubscribeQos());
							}

							@Override
							public void onFailure(IMqttToken asyncActionToken, Throwable e) {
								log.error("【Paho】 => MQTT连接失败，客户端ID：{}，错误信息：{}", clientId, e.getMessage(), e);
								if (ATOMIC.incrementAndGet() < 5) {
									open();
								}
							}
						});
					}
				}
			}
		}
		catch (Exception e) {
			log.error("【Paho】 => MQTT连接失败，错误信息：{}", e.getMessage(), e);
			throw new SystemException("S_Mqtt_ConnectError", e.getMessage(), e);
		}
	}

	public void close() {
		if (ObjectUtils.isNotNull(client)) {
			// 等待30秒
			try {
				client.disconnectForcibly(30);
				client.close();
				log.info("【Paho】 => 关闭MQTT连接成功");
			}
			catch (MqttException e) {
				log.error("【Paho】 => 关闭MQTT连接失败，错误信息：{}", e.getMessage(), e);
				throw new SystemException("S_Mqtt_CloseError", e.getMessage(), e);
			}
		}
	}

	public void subscribe(String[] topics, int[] qos) throws MqttException {
		checkTopicAndQos(topics, qos, "Paho");
		if (ObjectUtils.isNotNull(client)) {
			client.subscribe(topics, qos, null, new MqttActionListener() {
				@Override
				public void onSuccess(IMqttToken asyncActionToken) {
					log.info("【Paho】 => MQTT订阅成功，主题: {}", String.join(",", topics));
				}

				@Override
				public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
					log.error("【Paho】 => MQTT订阅失败，主题：{}，错误信息：{}", String.join(",", topics), exception.getMessage(),
							exception);
				}

			});
		}
	}

	public void unsubscribe(String[] topics) throws MqttException {
		checkTopic(topics, "Paho");
		if (ObjectUtils.isNotNull(client)) {
			client.unsubscribe(topics, null, new MqttActionListener() {
				@Override
				public void onSuccess(IMqttToken asyncActionToken) {
					log.info("【Paho】 => MQTT取消订阅成功，主题：{}", String.join(",", topics));
				}

				@Override
				public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
					log.error("【Paho】 => MQTT取消订阅失败，主题：{}，错误信息：{}", String.join(",", topics), exception.getMessage(),
							exception);
				}

			}, new MqttProperties());
		}
	}

	public void publish(String topic, byte[] payload, int qos) throws MqttException {
		if (ObjectUtils.isNotNull(client)) {
			client.publish(topic, payload, qos, false);
		}
	}

	public void publish(String topic, byte[] payload) throws MqttException {
		publish(topic, payload, mqttClientProperties.getPublishQos());
	}

	private MqttConnectionOptions options() {
		MqttConnectionOptions options = new MqttConnectionOptions();
		options.setCleanStart(mqttClientProperties.isClearStart());
		options.setUserName(mqttClientProperties.getUsername());
		options.setPassword(mqttClientProperties.getPassword().getBytes(StandardCharsets.UTF_8));
		options.setReceiveMaximum(mqttClientProperties.getReceiveMaximum());
		options.setMaximumPacketSize((long) mqttClientProperties.getMaximumPacketSize());
		options.setWill(WILL_TOPIC,
				new MqttMessage(WILL_DATA, mqttClientProperties.getWillQos(), false, new MqttProperties()));
		// 超时时间
		options.setConnectionTimeout(mqttClientProperties.getConnectionTimeout());
		// 会话心跳
		options.setKeepAliveInterval(mqttClientProperties.getKeepAliveInterval());
		// 开启重连
		options.setAutomaticReconnect(mqttClientProperties.isAutomaticReconnect());
		return options;
	}

	public void publishSubscribeEvent(Set<String> topics, int qos) {
		if (CollectionUtils.isNotEmpty(topics)) {
			EventBus.publish(new SubscribeEvent(this, mqttClientProperties.getClientId(), topics.toArray(String[]::new),
					topics.stream().mapToInt(item -> qos).toArray()));
		}
	}

	public void publishUnsubscribeEvent(Set<String> topics) {
		if (CollectionUtils.isNotEmpty(topics)) {
			EventBus
				.publish(new UnsubscribeEvent(this, mqttClientProperties.getClientId(), topics.toArray(String[]::new)));
		}
	}

	public void publishOpenEvent(String clientId) {
		EventBus.publish(new OpenEvent(this, clientId));
	}

	public void publishCloseEvent(String clientId) {
		EventBus.publish(new CloseEvent(this, clientId));
	}

}

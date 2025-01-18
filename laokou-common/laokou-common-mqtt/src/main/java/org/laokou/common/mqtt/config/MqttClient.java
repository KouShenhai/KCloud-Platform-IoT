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
import org.laokou.common.i18n.utils.ObjectUtil;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ScheduledExecutorService;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * MQTT客户端.
 *
 * @author laokou
 */
@Slf4j
public class MqttClient {

	/**
	 * 服务下线主题.
	 */
	private static final String WILL_TOPIC = "will_topic";

	/**
	 * 服务下线数据.
	 */
	private static final byte[] WILL_DATA = "offline".getBytes(UTF_8);

	private final MqttBrokerProperties mqttBrokerProperties;

	private final MqttLoadBalancer mqttLoadBalancer;

	private final ScheduledExecutorService executor;

	private volatile MqttAsyncClient client;

	public MqttClient(MqttBrokerProperties mqttBrokerProperties, MqttLoadBalancer mqttLoadBalancer,
			ScheduledExecutorService executor) {
		this.mqttBrokerProperties = mqttBrokerProperties;
		this.mqttLoadBalancer = mqttLoadBalancer;
		this.executor = executor;
	}

	public void open() {
		try {
			client = new MqttAsyncClient(mqttBrokerProperties.getUri(), mqttBrokerProperties.getClientId(),
					new MqttDefaultFilePersistence(), null, executor);
			client.setManualAcks(mqttBrokerProperties.isManualAcks());
			client.setCallback(new MqttMessageCallback(mqttLoadBalancer, mqttBrokerProperties, client));
			client.connect(options(), null, new MqttActionListener() {
				@Override
				public void onSuccess(IMqttToken asyncActionToken) {
					log.info("MQTT连接成功");
				}

				@Override
				public void onFailure(IMqttToken asyncActionToken, Throwable e) {
					log.error("MQTT连接失败，错误信息：{}", e.getMessage(), e);
				}
			});
		}
		catch (Exception e) {
			log.error("MQTT连接失败，错误信息：{}", e.getMessage(), e);
		}
	}

	public void close() {
		if (ObjectUtil.isNotNull(client)) {
			// 等待30秒
			try {
				client.disconnectForcibly(30);
				client.close();
				log.info("关闭MQTT连接");
			}
			catch (MqttException e) {
				log.error("关闭MQTT连接失败，错误信息：{}", e.getMessage(), e);
			}
		}
	}

	public void subscribe(String[] topics, int[] qos) throws MqttException {
		client.subscribe(topics, qos, null, new MqttActionListener() {
			@Override
			public void onSuccess(IMqttToken asyncActionToken) {
				log.info("MQTT订阅成功，主题: {}", String.join(",", topics));
			}

			@Override
			public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
				log.error("MQTT订阅失败，主题：{}，错误信息：{}", String.join(",", topics), exception.getMessage(), exception);
			}

		});
	}

	public void unsubscribe(String[] topics) throws MqttException {
		client.unsubscribe(topics, null, new MqttActionListener() {
			@Override
			public void onSuccess(IMqttToken asyncActionToken) {
				log.info("MQTT取消订阅成功，主题：{}", String.join(",", topics));
			}

			@Override
			public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
				log.error("MQTT取消订阅失败，主题：{}，错误信息：{}", String.join(",", topics), exception.getMessage(), exception);
			}

		}, new MqttProperties());
	}

	public void send(String topic, String payload, int qos) throws MqttException {
		client.publish(topic, payload.getBytes(StandardCharsets.UTF_8), qos, false);
	}

	public void send(String topic, String payload) throws MqttException {
		send(topic, payload, mqttBrokerProperties.getSendQos());
	}

	private MqttConnectionOptions options() {
		MqttConnectionOptions options = new MqttConnectionOptions();
		options.setCleanStart(mqttBrokerProperties.isClearStart());
		options.setUserName(mqttBrokerProperties.getUsername());
		options.setPassword(mqttBrokerProperties.getPassword().getBytes(StandardCharsets.UTF_8));
		options.setReceiveMaximum(mqttBrokerProperties.getReceiveMaximum());
		options.setMaximumPacketSize(mqttBrokerProperties.getMaximumPacketSize());
		options.setWill(WILL_TOPIC,
				new MqttMessage(WILL_DATA, mqttBrokerProperties.getSendQos(), false, new MqttProperties()));
		// 超时时间
		options.setConnectionTimeout(mqttBrokerProperties.getConnectionTimeout());
		// 会话心跳
		options.setKeepAliveInterval(mqttBrokerProperties.getKeepAliveInterval());
		// 开启重连
		options.setAutomaticReconnect(mqttBrokerProperties.isAutomaticReconnect());
		return options;
	}

}

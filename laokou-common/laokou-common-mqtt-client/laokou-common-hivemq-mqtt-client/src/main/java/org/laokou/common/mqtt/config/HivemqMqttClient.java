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

import com.hivemq.client.mqtt.MqttClientExecutorConfig;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.datatypes.MqttTopicFilter;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.Mqtt5ClientBuilder;
import com.hivemq.client.mqtt.mqtt5.message.Mqtt5ReasonCode;
import com.hivemq.client.mqtt.mqtt5.message.connect.connack.Mqtt5ConnAckReasonCode;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5PayloadFormatIndicator;
import com.hivemq.client.mqtt.mqtt5.message.subscribe.Mqtt5Subscription;
import com.hivemq.client.mqtt.mqtt5.message.unsubscribe.Mqtt5Unsubscribe;
import com.hivemq.client.mqtt.mqtt5.message.unsubscribe.unsuback.Mqtt5UnsubAck;
import io.micrometer.common.util.StringUtils;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.util.SpringEventBus;
import org.laokou.common.core.util.CollectionUtils;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.mqtt.client.AbstractMqttClient;
import org.laokou.common.mqtt.client.MqttMessage;
import org.laokou.common.mqtt.client.config.MqttClientProperties;
import org.laokou.common.mqtt.client.handler.MessageHandler;
import org.laokou.common.mqtt.client.handler.event.*;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * MQTT客户端.
 *
 * @author laokou
 */
@Slf4j
public class HivemqMqttClient extends AbstractMqttClient {

	private final MqttClientProperties mqttClientProperties;

	private final List<MessageHandler> messageHandlers;

	private final ExecutorService virtualThreadExecutor;

	private volatile Mqtt5AsyncClient client;

	private final Object LOCK = new Object();

	public HivemqMqttClient(MqttClientProperties mqttClientProperties, List<MessageHandler> messageHandlers,
			ExecutorService virtualThreadExecutor) {
		this.mqttClientProperties = mqttClientProperties;
		this.messageHandlers = messageHandlers;
		this.virtualThreadExecutor = virtualThreadExecutor;
	}

	public void open() {
		if (ObjectUtils.isNull(client)) {
			synchronized (LOCK) {
				if (ObjectUtils.isNull(client)) {
					String clientId = mqttClientProperties.getClientId();
					client = getClient(clientId);
					client.connectWith()
						.willPublish()
						.topic(WILL_TOPIC)
						.payload(WILL_PAYLOAD)
						.qos(getMqttQos(mqttClientProperties.getWillQos()))
						.retain(true)
						.messageExpiryInterval(100)
						.delayInterval(10)
						.payloadFormatIndicator(Mqtt5PayloadFormatIndicator.UTF_8)
						.contentType("text/plain")
						.responseTopic(RESPONSE_TOPIC)
						.correlationData(CORRELATION_DATA)
						.applyWillPublish()
						.keepAlive(mqttClientProperties.getKeepAliveInterval())
						.cleanStart(mqttClientProperties.isClearStart())
						.sessionExpiryInterval(mqttClientProperties.getSessionExpiryInterval())
						.restrictions()
						.receiveMaximum(mqttClientProperties.getReceiveMaximum())
						.sendMaximum(mqttClientProperties.getSendMaximum())
						.maximumPacketSize(mqttClientProperties.getMaximumPacketSize())
						.sendMaximumPacketSize(mqttClientProperties.getSendMaximumPacketSize())
						.topicAliasMaximum(mqttClientProperties.getTopicAliasMaximum())
						.sendTopicAliasMaximum(mqttClientProperties.getSendTopicAliasMaximum())
						.requestProblemInformation(mqttClientProperties.isRequestProblemInformation())
						.requestResponseInformation(mqttClientProperties.isRequestResponseInformation())
						.applyRestrictions()
						.send()
						.thenAcceptAsync(ack -> {
							Mqtt5ConnAckReasonCode reasonCode = ack.getReasonCode();
							if (!reasonCode.isError()) {
								log.info("【Hivemq】 => MQTT连接成功，客户端ID：{}", clientId);
								// 发布订阅事件
								publishSubscribeEvent(mqttClientProperties.getTopics(),
										mqttClientProperties.getSubscribeQos());
							}
							else {
								log.error("【Hivemq】 => MQTT连接失败，客户端ID：{}，错误信息：{}", clientId, reasonCode.name());
							}
						});
				}
			}
		}
	}

	public void close() {
		if (ObjectUtils.isNotNull(client)) {
			client.disconnectWith()
				.sessionExpiryInterval(mqttClientProperties.getSessionExpiryInterval())
				.send()
				.thenAcceptAsync(
						ack -> log.info("【Hivemq】 => MQTT断开连接成功，客户端ID：{}", mqttClientProperties.getClientId()));
		}
	}

	public void subscribe(String[] topics, int[] qos) {
		checkTopicAndQos(topics, qos, "Hivemq");
		if (ObjectUtils.isNotNull(client)) {
			List<Mqtt5Subscription> subscriptions = new ArrayList<>(topics.length);
			for (int i = 0; i < topics.length; i++) {
				subscriptions.add(Mqtt5Subscription.builder()
					.topicFilter(topics[i])
					.qos(getMqttQos(qos[i]))
					.retainAsPublished(mqttClientProperties.isRetain())
					.build());
			}
			client.subscribeWith().addSubscriptions(subscriptions).callback(ack -> {
				for (MessageHandler messageHandler : messageHandlers) {
					String topic = ack.getTopic().toString();
					if (messageHandler.isSubscribe(topic)) {
						try {
							messageHandler.handle(new MqttMessage(ack.getPayloadAsBytes(), topic));
							break;
						}
						catch (Exception e) {
							log.error("【Hivemq】 => MQTT消费失败，主题: {}，错误信息：{}", ack.getTopic(), e.getMessage(), e);
						}
					}
				}
			}).executor(virtualThreadExecutor).send().thenAcceptAsync(ack -> {
				if (!ack.getReasonCodes().stream().filter(Mqtt5ReasonCode::isError).toList().isEmpty()) {
					log.info("【Hivemq】 => MQTT订阅失败，主题: {}，错误信息：{}", String.join("、", topics),
							String.join("、", ack.getReasonCodes().stream().map(Enum::name).toList()));
				}
				else {
					log.info("【Hivemq】 => MQTT订阅成功，主题: {}", String.join("、", topics));
				}
			});
		}
	}

	public void unsubscribe(String[] topics) {
		checkTopic(topics, "Hivemq");
		if (ObjectUtils.isNotNull(client)) {
			List<MqttTopicFilter> matchedTopics = new ArrayList<>(topics.length);
			for (String topic : topics) {
				matchedTopics.add(MqttTopicFilter.of(topic));
			}
			Mqtt5UnsubAck ack = client.unsubscribe(Mqtt5Unsubscribe.builder().addTopicFilters(matchedTopics).build())
				.join();
			if (!ack.getReasonCodes().stream().filter(Mqtt5ReasonCode::isError).toList().isEmpty()) {
				log.info("【Hivemq】 => MQTT取消订阅失败，主题: {}，错误信息：{}", String.join("、", topics),
						String.join("、", ack.getReasonCodes().stream().map(Enum::name).toList()));
			}
			else {
				log.info("【Hivemq】 => MQTT取消订阅成功，主题: {}", String.join("、", topics));
			}
		}
	}

	public void publish(String topic, byte[] payload, int qos) {
		if (ObjectUtils.isNotNull(client)) {
			client.publishWith()
				.topic(topic)
				.qos(getMqttQos(qos))
				.payload(payload)
				.retain(mqttClientProperties.isRetain())
				.messageExpiryInterval(mqttClientProperties.getMessageExpiryInterval())
				.payloadFormatIndicator(Mqtt5PayloadFormatIndicator.UTF_8)
				.contentType("text/plain")
				.responseTopic(RESPONSE_TOPIC)
				.send()
				.thenAcceptAsync(ack -> {
					if (ack.getError().isPresent()) {
						Throwable throwable = ack.getError().get();
						String message = throwable.getMessage();
						if (StringUtils.isBlank(message)) {
							log.info("【Hivemq】 => MQTT消息发送成功，主题：{}", topic);
						}
						else {
							log.error("【Hivemq】 => MQTT消息发送失败，主题：{}，错误信息：{}", topic, message, throwable);
						}
					}
				});
		}
	}

	public void publish(String topic, byte[] payload) {
		publish(topic, payload, mqttClientProperties.getPublishQos());
	}

	private Mqtt5AsyncClient getClient(String clientId) {
		Mqtt5ClientBuilder builder = Mqtt5Client.builder()
			.identifier(clientId)
			.serverHost(mqttClientProperties.getHost())
			.serverPort(mqttClientProperties.getPort())
			.executorConfig(MqttClientExecutorConfig.builder()
				.nettyExecutor(virtualThreadExecutor)
				.nettyThreads(mqttClientProperties.getNettyThreads())
				.applicationScheduler(Schedulers.from(virtualThreadExecutor))
				.build());
		// 开启重连
		if (mqttClientProperties.isAutomaticReconnect()) {
			builder.automaticReconnect()
				.initialDelay(mqttClientProperties.getAutomaticReconnectInitialDelay(), TimeUnit.SECONDS)
				.maxDelay(mqttClientProperties.getAutomaticReconnectMaxDelay(), TimeUnit.SECONDS)
				.applyAutomaticReconnect();
		}
		if (mqttClientProperties.isAuth()) {
			builder.simpleAuth()
				.username(mqttClientProperties.getUsername())
				.password(mqttClientProperties.getPassword().getBytes())
				.applySimpleAuth();
		}
		return builder.buildAsync();
	}

	public void publishSubscribeEvent(Set<String> topics, int qos) {
		if (CollectionUtils.isNotEmpty(topics)) {
			SpringEventBus.publish(new SubscribeEvent(this, mqttClientProperties.getClientId(),
					topics.toArray(String[]::new), topics.stream().mapToInt(item -> qos).toArray()));
		}
	}

	public void publishUnsubscribeEvent(Set<String> topics) {
		if (CollectionUtils.isNotEmpty(topics)) {
			SpringEventBus
				.publish(new UnsubscribeEvent(this, mqttClientProperties.getClientId(), topics.toArray(String[]::new)));
		}
	}

	public void publishOpenEvent(String clientId) {
		SpringEventBus.publish(new OpenEvent(this, clientId));
	}

	public void publishCloseEvent(String clientId) {
		SpringEventBus.publish(new CloseEvent(this, clientId));
	}

	private MqttQos getMqttQos(int qos) {
		return MqttQos.fromCode(qos);
	}

}

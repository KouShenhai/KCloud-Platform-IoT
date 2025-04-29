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

import com.hivemq.client.mqtt.MqttClientConnectionConfig;
import com.hivemq.client.mqtt.MqttClientExecutorConfig;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.datatypes.MqttTopicFilter;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.Mqtt5ClientBuilder;
import com.hivemq.client.mqtt.mqtt5.message.connect.connack.Mqtt5ConnAck;
import com.hivemq.client.mqtt.mqtt5.message.connect.connack.Mqtt5ConnAckReasonCode;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5PayloadFormatIndicator;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5PublishResult;
import com.hivemq.client.mqtt.mqtt5.message.subscribe.Mqtt5Subscription;
import com.hivemq.client.mqtt.mqtt5.message.unsubscribe.unsuback.Mqtt5UnsubAck;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.util.ThreadUtils;
import org.laokou.common.i18n.common.exception.BizException;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.mqtt.client.AbstractMqttClient;
import org.laokou.common.mqtt.client.MqttMessage;
import org.laokou.common.mqtt.client.config.MqttClientProperties;
import org.laokou.common.mqtt.client.handler.MessageHandler;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * MQTT客户端.
 *
 * @author laokou
 */
@Slf4j
public class HivemqMqttClient extends AbstractMqttClient {

	private final MqttClientProperties mqttClientProperties;

	private final List<MessageHandler> messageHandlers;

	private volatile Mqtt5BlockingClient mqtt5BlockingClient;

	private final Object lock = new Object();

	public HivemqMqttClient(MqttClientProperties mqttClientProperties, List<MessageHandler> messageHandlers) {
		this.mqttClientProperties = mqttClientProperties;
		this.messageHandlers = messageHandlers;
	}

	public void open() {
		String[] topics = getTopics();
		connect();
		subscribe(topics, getQosArray(topics));
	}

	public void unSubscribe(String[] topics) {
		checkTopic(topics, "Hivemq");
		List<MqttTopicFilter> matchedTopics = new ArrayList<>(topics.length);
		for (String topic : topics) {
			matchedTopics.add(MqttTopicFilter.of(topic));
		}
		Mqtt5UnsubAck ack = getMqtt5Client().unsubscribeWith().addTopicFilters(matchedTopics).send();
		for (int i = 0; i < topics.length; i++) {
			if (ObjectUtils.equals(ack.getReasonCodes().get(i), Mqtt5ConnAckReasonCode.SUCCESS)) {
				log.info("【Hivemq】 => MQTT取消订阅成功，主题：{}", topics[i]);
			}
			else {
				log.error("【Hivemq】 => MQTT取消订阅失败，主题：{}，错误信息：{}", topics[i], ack.getReasonString());
			}
		}
	}

	public void publish(String topic, int qos, byte[] payload) {
		Mqtt5PublishResult result = getMqtt5Client().publishWith()
			.topic(topic)
			.qos(getMqttQos(qos))
			.payload(payload)
			.noMessageExpiry()
			.retain(mqttClientProperties.isRetain())
			.messageExpiryInterval(mqttClientProperties.getMessageExpiryInterval())
			.correlationData(CORRELATION_DATA)
			.payloadFormatIndicator(Mqtt5PayloadFormatIndicator.UTF_8)
			.contentType("text/plain")
			.responseTopic(RESPONSE_TOPIC)
			.send();
		if (result.getError().isPresent()) {
			Throwable throwable = result.getError().get();
			log.error("【Hivemq】 => MQTT消息发布失败，topic：{}，错误信息：{}", topic, throwable.getMessage(), throwable);
		}
		else {
			log.info("【Hivemq】 => MQTT消息发布成功，topic：{}", topic);
		}
	}

	public void close() {
		try {
			getMqtt5Client().disconnectWith()
				.sessionExpiryInterval(mqttClientProperties.getSessionExpiryInterval())
				.send();
			log.info("【Hivemq】 => MQTT断开连接成功，客户端ID：{}", mqttClientProperties.getClientId());
		}
		catch (Exception e) {
			log.error("【Hivemq】 => MQTT断开连接失败，错误信息：{}", e.getMessage(), e);
		}
	}

	private MqttQos getMqttQos(int qos) {
		return MqttQos.fromCode(qos);
	}

	private String[] getTopics() {
		return mqttClientProperties.getTopics().toArray(String[]::new);
	}

	private int[] getQosArray(String[] topics) {
		return Stream.of(topics).mapToInt(item -> mqttClientProperties.getSubscribeQos()).toArray();
	}

	private Mqtt5BlockingClient getMqtt5Client() {
		if (ObjectUtils.isNull(mqtt5BlockingClient)) {
			synchronized (lock) {
				if (ObjectUtils.isNull(mqtt5BlockingClient)) {
					mqtt5BlockingClient = getMqtt5ClientBuilder().buildBlocking();
				}
			}
		}
		return mqtt5BlockingClient;
	}

	private void connect() {
		Mqtt5ConnAck ack = getMqtt5Client().connectWith()
			.keepAlive(mqttClientProperties.getKeepAliveInterval())
			.cleanStart(mqttClientProperties.isClearStart())
			.sessionExpiryInterval(mqttClientProperties.getSessionExpiryInterval())
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
			.send();
		if (ObjectUtils.equals(Mqtt5ConnAckReasonCode.SUCCESS, ack.getReasonCode())) {
			log.info("【Hivemq】 => MQTT连接成功，主机：{}，端口：{}，客户端ID：{}", mqttClientProperties.getHost(),
					mqttClientProperties.getPort(), mqttClientProperties.getClientId());
		}
		else {
			log.error("【Hivemq】 => MQTT连接失败，原因：{}，客户端ID：{}", ack.getReasonString(), mqttClientProperties.getClientId());
		}
	}

	private void subscribe(String[] topics, int[] qosArray) {
		checkTopicAndQos(topics, qosArray, "Hivemq");
		List<Mqtt5Subscription> subscriptions = new ArrayList<>(topics.length);
		for (int i = 0; i < topics.length; i++) {
			subscriptions.add(Mqtt5Subscription.builder()
				.topicFilter(topics[i])
				.qos(getMqttQos(qosArray[i]))
				.retainAsPublished(mqttClientProperties.isRetain())
				.noLocal(mqttClientProperties.isNoLocal())
				.build());
		}
		getMqtt5Client().toAsync().subscribeWith().addSubscriptions(subscriptions).callback(publish -> {
			for (MessageHandler messageHandler : messageHandlers) {
				if (messageHandler.isSubscribe(publish.getTopic().toString())) {
					log.info("【Hivemq】 => MQTT接收到消息，Topic：{}", publish.getTopic());
					messageHandler.handle(new MqttMessage(publish.getPayloadAsBytes(), publish.getTopic().toString()));
				}
			}
		})
			.executor(ThreadUtils.newVirtualTaskExecutor())
			.send()
			.thenAcceptAsync(ack -> log.info("【Hivemq】 => MQTT订阅成功，主题: {}", String.join("、", topics)))
			.exceptionallyAsync(e -> {
				log.error("【Hivemq】 => MQTT订阅失败，主题：{}，错误信息：{}", String.join("、", topics), e.getMessage(), e);
				throw new BizException("B_HivemqMqttClient_SubscribeFailed", "MQTT订阅失败", e);
			})
			.join();
	}

	private Mqtt5ClientBuilder getMqtt5ClientBuilder() {
		Mqtt5ClientBuilder builder = Mqtt5Client.builder().addConnectedListener(listener -> {
			Optional<? extends MqttClientConnectionConfig> config = Optional
				.of(listener.getClientConfig().getConnectionConfig())
				.get();
			config.ifPresent(mqttClientConnectionConfig -> log.info("【Hivemq】 => MQTT连接保持时间：{}ms",
					mqttClientConnectionConfig.getKeepAlive()));
			log.info("【Hivemq】 => MQTT已连接，客户端ID：{}", mqttClientProperties.getClientId());
		})
			.addDisconnectedListener(
					listener -> log.error("【Hivemq】 => MQTT已断开连接，客户端ID：{}", mqttClientProperties.getClientId()))
			.identifier(mqttClientProperties.getClientId())
			.serverHost(mqttClientProperties.getHost())
			.serverPort(mqttClientProperties.getPort())
			.executorConfig(MqttClientExecutorConfig.builder()
				.nettyExecutor(ThreadUtils.newVirtualTaskExecutor())
				.nettyThreads(mqttClientProperties.getNettyThreads())
				.applicationScheduler(Schedulers.from(ThreadUtils.newVirtualTaskExecutor()))
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
		return builder;
	}

}

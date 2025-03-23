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
import com.hivemq.client.mqtt.MqttClientTransportConfig;
import com.hivemq.client.mqtt.MqttGlobalPublishFilter;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.datatypes.MqttTopicFilter;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.Mqtt5ClientBuilder;
import com.hivemq.client.mqtt.mqtt5.Mqtt5RxClient;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;
import com.hivemq.client.mqtt.mqtt5.message.subscribe.Mqtt5Subscription;
import com.hivemq.client.mqtt.mqtt5.message.unsubscribe.Mqtt5Unsubscribe;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.event.EventBus;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.mqtt.client.AbstractMqttClient;
import org.laokou.common.mqtt.client.MqttMessage;
import org.laokou.common.mqtt.client.config.MqttBrokerProperties;
import org.laokou.common.mqtt.client.handler.MessageHandler;
import org.laokou.common.mqtt.client.handler.event.CloseEvent;
import org.laokou.common.mqtt.client.handler.event.OpenEvent;
import org.laokou.common.mqtt.client.handler.event.SubscribeEvent;
import org.laokou.common.mqtt.client.handler.event.UnsubscribeEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * MQTT客户端.
 *
 * @author laokou
 */
@Slf4j
public class HivemqMqttClient extends AbstractMqttClient {

	private final MqttBrokerProperties mqttBrokerProperties;

	private final List<MessageHandler> messageHandlers;

	private final ExecutorService virtualThreadExecutor;

	private volatile Mqtt5RxClient client;

	private final Object LOCK = new Object();

	public HivemqMqttClient(MqttBrokerProperties mqttBrokerProperties, List<MessageHandler> messageHandlers,
			ExecutorService virtualThreadExecutor) {
		this.mqttBrokerProperties = mqttBrokerProperties;
		this.messageHandlers = messageHandlers;
		this.virtualThreadExecutor = virtualThreadExecutor;
	}

	public void open() {
		if (ObjectUtil.isNull(client)) {
			synchronized (LOCK) {
				if (ObjectUtil.isNull(client)) {
					String clientId = mqttBrokerProperties.getClientId();
					client = getClient(clientId);
					client.connectWith()
						.willPublish()
						.topic(WILL_TOPIC)
						.payload(WILL_DATA)
						.qos(getMqttQos(mqttBrokerProperties.getWillQos()))
						.retain(false)
						.applyWillPublish()
						.keepAlive(mqttBrokerProperties.getKeepAliveInterval())
						.cleanStart(mqttBrokerProperties.isClearStart())
						.sessionExpiryInterval(mqttBrokerProperties.getSessionExpiryInterval())
						.restrictions()
						.receiveMaximum(mqttBrokerProperties.getReceiveMaximum())
						.sendMaximum(mqttBrokerProperties.getSendMaximum())
						.maximumPacketSize(mqttBrokerProperties.getMaximumPacketSize())
						.sendMaximumPacketSize(mqttBrokerProperties.getSendMaximumPacketSize())
						.topicAliasMaximum(mqttBrokerProperties.getTopicAliasMaximum())
						.sendTopicAliasMaximum(mqttBrokerProperties.getSendTopicAliasMaximum())
						.requestProblemInformation(mqttBrokerProperties.isRequestProblemInformation())
						.requestResponseInformation(mqttBrokerProperties.isRequestProblemInformation())
						.applyRestrictions()
						.applyConnect()
						.doOnSuccess(s -> {
							log.info("【Hivemq】 => MQTT连接成功，客户端ID：{}", clientId);
							// 发布订阅事件
							publishSubscribeEvent(mqttBrokerProperties.getTopics(),
									mqttBrokerProperties.getSubscribeQos());
						})
						.doOnError(e -> log.error("【Hivemq】 => MQTT连接失败，错误信息：{}", e.getMessage(), e))
						.subscribeOn(Schedulers.from(virtualThreadExecutor))
						.subscribe();
				}
			}
		}
	}

	public void close() {
		if (ObjectUtil.isNotNull(client)) {
			client.disconnectWith()
				.applyDisconnect()
				.doOnError(e -> log.error("【Hivemq】 => MQTT断开连接失败，错误信息：{}", e.getMessage(), e))
				.subscribeOn(Schedulers.from(virtualThreadExecutor))
				.subscribe();
		}
	}

	public void subscribe(String[] topics, int[] qos) {
		checkTopicAndQos(topics, qos, "Hivemq");
		if (ObjectUtil.isNotNull(client)) {
			List<Mqtt5Subscription> subscriptions = new ArrayList<>(topics.length);
			for (int i = 0; i < topics.length; i++) {
				subscriptions.add(Mqtt5Subscription.builder().topicFilter(topics[i]).qos(getMqttQos(qos[i])).build());
			}
			client.subscribeWith()
				.addSubscriptions(subscriptions)
				.applySubscribe()
				.doOnSuccess(subscribeAck -> log.info("【Hivemq】 => MQTT订阅成功，主题: {}", String.join(",", topics)))
				.doOnError(r -> log.error("【Hivemq】 => MQTT订阅失败，主题：{}，错误信息：{}", String.join(",", topics),
						r.getMessage(), r))
				.subscribeOn(Schedulers.from(virtualThreadExecutor))
				.subscribe();
		}
	}

	public void unsubscribe(String[] topics) {
		checkTopic(topics, "Hivemq");
		if (ObjectUtil.isNotNull(client)) {
			List<MqttTopicFilter> matchedTopics = new ArrayList<>(topics.length);
			for (String topic : topics) {
				matchedTopics.add(MqttTopicFilter.of(topic));
			}
			client.unsubscribe(Mqtt5Unsubscribe.builder().addTopicFilters(matchedTopics).build())
				.doOnSuccess(r -> log.info("【Hivemq】 => MQTT取消订阅成功，主题：{}", String.join(",", topics)))
				.doOnError(r -> log.error("【Hivemq】 => MQTT取消订阅失败，主题：{}，错误信息：{}", String.join(",", topics),
						r.getMessage(), r))
				.subscribeOn(Schedulers.from(virtualThreadExecutor))
				.subscribe();
		}
	}

	public void consume() {
		if (ObjectUtil.isNotNull(client)) {
			client.publishes(MqttGlobalPublishFilter.ALL)
				.doOnNext(publish -> messageHandlers.forEach(messageHandler -> {
					if (messageHandler.isSubscribe(publish.getTopic().toString())) {
						messageHandler.handle(publish.getTopic().toString(),
								new MqttMessage(publish.getPayloadAsBytes()));
					}
				}))
				.doOnError(e -> log.error("【Hivemq】 => MQTT消息处理失败，错误信息：{}", e.getMessage(), e))
				.subscribeOn(Schedulers.from(virtualThreadExecutor))
				.subscribe();
		}
	}

	public void publish(String topic, byte[] payload, int qos) {
		if (ObjectUtil.isNotNull(client)) {
			client
				.publish(Flowable.just(Mqtt5Publish.builder()
					.topic(topic)
					.qos(getMqttQos(qos))
					.payload(payload)
					.retain(false)
					.messageExpiryInterval(mqttBrokerProperties.getMessageExpiryInterval())
					.build()))
				.singleOrError()
				.doOnError(e -> log.error("【Hivemq】 => MQTT消息发布失败，错误信息：{}", e.getMessage(), e))
				.subscribeOn(Schedulers.from(virtualThreadExecutor))
				.subscribe();
		}
	}

	public void publish(String topic, byte[] payload) {
		publish(topic, payload, mqttBrokerProperties.getPublishQos());
	}

	private Mqtt5RxClient getClient(String clientId) {
		Mqtt5ClientBuilder builder = Mqtt5Client.builder()
			.identifier(clientId)
			.serverHost(mqttBrokerProperties.getHost())
			.serverPort(mqttBrokerProperties.getPort())
			.transportConfig(MqttClientTransportConfig.builder()
				.socketConnectTimeout(mqttBrokerProperties.getConnectionTimeout(), TimeUnit.SECONDS)
				.build())
			.executorConfig(MqttClientExecutorConfig.builder().nettyExecutor(virtualThreadExecutor).build());
		// 开启重连
		if (mqttBrokerProperties.isAutomaticReconnect()) {
			builder.automaticReconnect()
				.maxDelay(mqttBrokerProperties.getAutomaticReconnectMaxDelay(), TimeUnit.MILLISECONDS)
				.applyAutomaticReconnect();
		}
		if (mqttBrokerProperties.isAuth()) {
			builder.simpleAuth()
				.username(mqttBrokerProperties.getUsername())
				.password(mqttBrokerProperties.getPassword().getBytes())
				.applySimpleAuth();
		}
		return builder.buildRx();
	}

	public void publishSubscribeEvent(Set<String> topics, int qos) {
		if (CollectionUtil.isNotEmpty(topics)) {
			EventBus.publish(new SubscribeEvent(this, mqttBrokerProperties.getClientId(), topics.toArray(String[]::new),
					topics.stream().mapToInt(item -> qos).toArray()));
		}
	}

	public void publishUnsubscribeEvent(Set<String> topics) {
		if (CollectionUtil.isNotEmpty(topics)) {
			EventBus
				.publish(new UnsubscribeEvent(this, mqttBrokerProperties.getClientId(), topics.toArray(String[]::new)));
		}
	}

	public void publishOpenEvent(String clientId) {
		EventBus.publish(new OpenEvent(this, clientId));
	}

	public void publishCloseEvent(String clientId) {
		EventBus.publish(new CloseEvent(this, clientId));
	}

	private MqttQos getMqttQos(int qos) {
		return MqttQos.fromCode(qos);
	}

}

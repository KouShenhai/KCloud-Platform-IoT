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
import com.hivemq.client.mqtt.MqttGlobalPublishFilter;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.datatypes.MqttTopicFilter;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.Mqtt5ClientBuilder;
import com.hivemq.client.mqtt.mqtt5.Mqtt5RxClient;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5PayloadFormatIndicator;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;
import com.hivemq.client.mqtt.mqtt5.message.subscribe.Mqtt5Subscription;
import com.hivemq.client.mqtt.mqtt5.message.unsubscribe.Mqtt5Unsubscribe;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.util.SpringEventBus;
import org.laokou.common.core.util.ThreadUtils;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.mqtt.client.AbstractMqttClient;
import org.laokou.common.mqtt.client.MqttMessage;
import org.laokou.common.mqtt.client.config.MqttClientProperties;
import org.laokou.common.mqtt.client.handler.MessageHandler;
import org.laokou.common.mqtt.client.handler.event.*;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
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

	private volatile Mqtt5RxClient client;

	private final Object lock = new Object();

	private final List<Disposable> disposableList = new CopyOnWriteArrayList<>();

	public HivemqMqttClient(MqttClientProperties mqttClientProperties, List<MessageHandler> messageHandlers) {
		this.mqttClientProperties = mqttClientProperties;
		this.messageHandlers = messageHandlers;
	}

	public void open() {
		if (ObjectUtils.isNull(client)) {
			synchronized (lock) {
				if (ObjectUtils.isNull(client)) {
					connect();
					subscribe();
					consume();
				}
			}
		}
	}

	public void close() {
		if (ObjectUtils.isNotNull(client)) {
			Disposable disposable = client.disconnectWith()
				.applyDisconnect()
				.doOnComplete(() -> log.info("【Hivemq】 => MQTT断开连接成功，客户端ID：{}", mqttClientProperties.getClientId()))
				.doOnError(e -> log.error("【Hivemq】 => MQTT断开连接失败，错误信息：{}", e.getMessage(), e))
				.subscribeOn(Schedulers.from(ThreadUtils.newVirtualTaskExecutor()))
				.subscribe(() -> {
				}, e -> {
					throw new SystemException("S_Mqtt_CloseError", e.getMessage(), e);
				});
			disposableList.add(disposable);
		}
	}

	public void subscribe() {
		String[] topics = mqttClientProperties.getTopics().toArray(String[]::new);
		int[] qosArray = Stream.of(topics).mapToInt(item -> mqttClientProperties.getSubscribeQos()).toArray();
		subscribe(topics, qosArray);
	}

	public void subscribe(String[] topics, int[] qosArray) {
		checkTopicAndQos(topics, qosArray, "Hivemq");
		if (ObjectUtils.isNotNull(client)) {
			List<Mqtt5Subscription> subscriptions = new ArrayList<>(topics.length);
			for (int i = 0; i < topics.length; i++) {
				subscriptions.add(Mqtt5Subscription.builder()
					.topicFilter(topics[i])
					.qos(getMqttQos(qosArray[i]))
					.retainAsPublished(mqttClientProperties.isRetain())
					.noLocal(mqttClientProperties.isNoLocal())
					.build());
			}
			Disposable disposable = client.subscribePublishesWith()
				.addSubscriptions(subscriptions)
				.applySubscribe()
				.doOnSingle(ack -> log.info("【Hivemq】 => MQTT订阅成功，主题: {}", String.join("、", topics)))
				.doOnError(e -> log.error("【Hivemq】 => MQTT订阅失败，主题：{}，错误信息：{}", String.join("、", topics),
						e.getMessage(), e))
				.subscribeOn(Schedulers.from(ThreadUtils.newVirtualTaskExecutor()))
				.subscribe(ack -> {
				}, e -> {
					throw new SystemException("S_Mqtt_ConsumeError", e.getMessage(), e);
				});
			disposableList.add(disposable);
		}
	}

	public void unSubscribe() {
		String[] topics = mqttClientProperties.getTopics().toArray(String[]::new);
		unSubscribe(topics);
	}

	public void unSubscribe(String[] topics) {
		checkTopic(topics, "Hivemq");
		if (ObjectUtils.isNotNull(client)) {
			List<MqttTopicFilter> matchedTopics = new ArrayList<>(topics.length);
			for (String topic : topics) {
				matchedTopics.add(MqttTopicFilter.of(topic));
			}
			Disposable disposable = client
				.unsubscribe(Mqtt5Unsubscribe.builder().addTopicFilters(matchedTopics).build())
				.doOnSuccess(ack -> log.info("【Hivemq】 => MQTT取消订阅成功，主题：{}", String.join("、", topics)))
				.doOnError(e -> log.error("【Hivemq】 => MQTT取消订阅失败，主题：{}，错误信息：{}", String.join("、", topics),
						e.getMessage(), e))
				.subscribeOn(Schedulers.from(ThreadUtils.newVirtualTaskExecutor()))
				.subscribe(ack -> {
				}, e -> {
					throw new SystemException("S_Mqtt_UnSubscribeError", e.getMessage(), e);
				});
			disposableList.add(disposable);
		}
	}

	public void publish(String topic, byte[] payload, int qos) {
		if (ObjectUtils.isNotNull(client)) {
			Disposable disposable = client
				.publish(Flowable.just(Mqtt5Publish.builder()
					.topic(topic)
					.qos(getMqttQos(qos))
					.payload(payload)
					.retain(mqttClientProperties.isRetain())
					.messageExpiryInterval(mqttClientProperties.getMessageExpiryInterval())
					.payloadFormatIndicator(Mqtt5PayloadFormatIndicator.UTF_8)
					.contentType("text/plain")
					.responseTopic(RESPONSE_TOPIC)
					.build()))
				.singleOrError()
				.doOnError(e -> log.error("【Hivemq】 => MQTT消息发布失败，错误信息：{}", e.getMessage(), e))
				.subscribeOn(Schedulers.from(ThreadUtils.newVirtualTaskExecutor()))
				.subscribe(ack -> {
				}, e -> {
					throw new SystemException("S_Mqtt_PublishError", e.getMessage(), e);
				});
			disposableList.add(disposable);
		}
	}

	public void publish(String topic, byte[] payload) {
		publish(topic, payload, mqttClientProperties.getPublishQos());
	}

	private Mqtt5RxClient getClient() {
		Mqtt5ClientBuilder builder = Mqtt5Client.builder()
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
		return builder.buildRx();
	}

	public void publishOpenEvent() {
		SpringEventBus.publish(new OpenEvent(this, mqttClientProperties.getClientId()));
	}

	public void publishCloseEvent() {
		SpringEventBus.publish(new CloseEvent(this, mqttClientProperties.getClientId()));
	}

	public void publishMessageEvent(String topic, byte[] payload) {
		SpringEventBus.publish(new PublishMessageEvent(this, mqttClientProperties.getClientId(), topic, payload));
	}

	public void publishSubscribeEvent(String[] topics, int[] qosArray) {
		SpringEventBus.publish(new SubscribeEvent(this, mqttClientProperties.getClientId(), topics, qosArray));
	}

	public void publishUnSubscribeEvent(String[] topics) {
		SpringEventBus.publish(new UnSubscribeEvent(this, mqttClientProperties.getClientId(), topics));
	}

	public void dispose() {
		disposableList.forEach(disposable -> {
			if (ObjectUtils.isNotNull(disposable) && !disposable.isDisposed()) {
				// 显式取消订阅
				disposable.dispose();
			}
		});
		disposableList.clear();
	}

	private MqttQos getMqttQos(int qos) {
		return MqttQos.fromCode(qos);
	}

	private void connect() {
		client = getClient();
		Disposable disposable = client.connectWith()
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
			.applyConnect()
			.doOnSuccess(ack -> log.info("【Hivemq】 => MQTT连接成功，客户端ID：{}", mqttClientProperties.getClientId()))
			.doOnError(e -> log.error("【Hivemq】 => MQTT连接失败，错误信息：{}", e.getMessage(), e))
			.subscribeOn(Schedulers.from(ThreadUtils.newVirtualTaskExecutor()))
			.subscribe(ack -> {
			}, e -> {
				throw new SystemException("S_Mqtt_ConnectFailed", e.getMessage(), e);
			});
		disposableList.add(disposable);
	}

	private void consume() {
		if (ObjectUtils.isNotNull(client)) {
			Disposable disposable = client.publishes(MqttGlobalPublishFilter.ALL)
				.onBackpressureDrop()
				.observeOn(Schedulers.from(ThreadUtils.newVirtualTaskExecutor()), false, 4096)
				.doOnNext(publish -> {
					for (MessageHandler messageHandler : messageHandlers) {
						if (messageHandler.isSubscribe(publish.getTopic().toString())) {
							try {
								messageHandler.handle(
										new MqttMessage(publish.getPayloadAsBytes(), publish.getTopic().toString()));
								break;
							}
							catch (Exception e) {
								log.error("【Hivemq】 => MQTT消息处理失败，错误信息：{}", e.getMessage(), e);
							}
						}
					}
				})
				.doOnError(e -> log.error("【Hivemq】 => MQTT消息处理失败，错误信息：{}", e.getMessage(), e))
				.subscribeOn(Schedulers.from(ThreadUtils.newVirtualTaskExecutor()))
				.subscribe(v -> {
				}, e -> {
					throw new SystemException("S_Mqtt_ConsumeError", e.getMessage(), e);
				});
			disposableList.add(disposable);
		}
	}

}

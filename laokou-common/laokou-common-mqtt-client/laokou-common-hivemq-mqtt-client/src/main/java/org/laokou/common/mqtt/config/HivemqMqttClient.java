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
import com.hivemq.client.mqtt.MqttGlobalPublishFilter;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.datatypes.MqttTopicFilter;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.Mqtt5ClientBuilder;
import com.hivemq.client.mqtt.mqtt5.Mqtt5RxClient;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5PayloadFormatIndicator;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;
import com.hivemq.client.mqtt.mqtt5.message.subscribe.Mqtt5Subscription;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.util.SpringEventBus;
import org.laokou.common.core.util.ThreadUtils;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.mqtt.client.AbstractMqttClient;
import org.laokou.common.mqtt.client.MqttMessage;
import org.laokou.common.mqtt.client.config.MqttClientProperties;
import org.laokou.common.mqtt.client.handler.MessageHandler;
import org.laokou.common.mqtt.client.handler.event.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
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

	private volatile Disposable connectDisposable;

	private volatile Disposable subscribeDisposable;

	private volatile Disposable unSubscribeDisposable;

	private volatile Disposable publishDisposable;

	private volatile Disposable disconnectDisposable;

	private volatile Disposable consumeDisposable;

	private final AtomicBoolean isConnected = new AtomicBoolean(false);

	public boolean getStatus() {
		return isConnected.get();
	}

	public HivemqMqttClient(MqttClientProperties mqttClientProperties, List<MessageHandler> messageHandlers) {
		this.mqttClientProperties = mqttClientProperties;
		this.messageHandlers = messageHandlers;
	}

	public void open() {
		if (ObjectUtils.isNull(client)) {
			synchronized (lock) {
				if (ObjectUtils.isNull(client)) {
					client = getClient();
				}
			}
		}
		connect();
		consume();
	}

	public void close() {
		if (ObjectUtils.isNotNull(client)) {
			disconnectDisposable = client.disconnectWith()
				.sessionExpiryInterval(mqttClientProperties.getSessionExpiryInterval())
				.applyDisconnect()
				.subscribeOn(Schedulers.io())
				.retryWhen(errors -> errors.scan(1, (retryCount, error) -> retryCount > 5 ? -1 : retryCount + 1)
					.takeWhile(retryCount -> retryCount != -1)
					.flatMap(retryCount -> Flowable.timer((long) Math.pow(2, retryCount) * 100, TimeUnit.MILLISECONDS)))
				.subscribe(() -> log.info("【Hivemq】 => MQTT断开连接成功，客户端ID：{}", mqttClientProperties.getClientId()),
						e -> log.error("【Hivemq】 => MQTT断开连接失败，错误信息：{}", e.getMessage(), e));
		}
	}

	public void subscribe() {
		String[] topics = getTopics();
		subscribe(topics, getQosArray(topics));
	}

	public String[] getTopics() {
		return mqttClientProperties.getTopics().toArray(String[]::new);
	}

	public int[] getQosArray(String[] topics) {
		return Stream.of(topics).mapToInt(item -> mqttClientProperties.getSubscribeQos()).toArray();
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
			subscribeDisposable = client.subscribeWith()
				.addSubscriptions(subscriptions)
				.applySubscribe()
				.subscribeOn(Schedulers.io())
				.retryWhen(errors -> errors.scan(1, (retryCount, error) -> retryCount > 5 ? -1 : retryCount + 1)
					.takeWhile(retryCount -> retryCount != -1)
					.flatMap(retryCount -> Flowable.timer((long) Math.pow(2, retryCount) * 100, TimeUnit.MILLISECONDS)))
				.subscribe(ack -> log.info("【Hivemq】 => MQTT订阅成功，主题: {}", String.join("、", topics)), e -> log
					.error("【Hivemq】 => MQTT订阅失败，主题：{}，错误信息：{}", String.join("、", topics), e.getMessage(), e));
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
			unSubscribeDisposable = client.unsubscribeWith()
				.addTopicFilters(matchedTopics)
				.applyUnsubscribe()
				.subscribeOn(Schedulers.io())
				.retryWhen(errors -> errors.scan(1, (retryCount, error) -> retryCount > 5 ? -1 : retryCount + 1)
					.takeWhile(retryCount -> retryCount != -1)
					.flatMap(retryCount -> Flowable.timer((long) Math.pow(2, retryCount) * 100, TimeUnit.MILLISECONDS)))
				.subscribe(ack -> log.info("【Hivemq】 => MQTT取消订阅成功，主题：{}", String.join("、", topics)), e -> log
					.error("【Hivemq】 => MQTT取消订阅失败，主题：{}，错误信息：{}", String.join("、", topics), e.getMessage(), e));
		}
	}

	public void publish(String topic, byte[] payload, int qos) {
		if (ObjectUtils.isNotNull(client)) {
			publishDisposable = client
				.publish(Flowable.just(Mqtt5Publish.builder()
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
					.build()))
				.subscribeOn(Schedulers.io())
				.retryWhen(errors -> errors.scan(1, (retryCount, error) -> retryCount > 5 ? -1 : retryCount + 1)
					.takeWhile(retryCount -> retryCount != -1)
					.flatMap(retryCount -> Flowable.timer((long) Math.pow(2, retryCount) * 100, TimeUnit.MILLISECONDS)))
				.subscribe(ack -> log.info("【Hivemq】 => MQTT消息发布成功，topic：{}", topic),
						e -> log.error("【Hivemq】 => MQTT消息发布失败，topic：{}，错误信息：{}", topic, e.getMessage(), e));
		}
	}

	public void publish(String topic, byte[] payload) {
		publish(topic, payload, mqttClientProperties.getPublishQos());
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

	public void dispose(Disposable disposable) {
		if (ObjectUtils.isNotNull(disposable) && !disposable.isDisposed()) {
			// 显式取消订阅
			disposable.dispose();
		}
	}

	public void dispose() {
		dispose(connectDisposable);
		dispose(subscribeDisposable);
		dispose(unSubscribeDisposable);
		dispose(publishDisposable);
		dispose(consumeDisposable);
		dispose(disconnectDisposable);
	}

	public void reSubscribe() {
		log.info("【Hivemq】 => MQTT重新订阅开始");
		dispose(subscribeDisposable);
		subscribe();
		log.info("【Hivemq】 => MQTT重新订阅结束");
	}

	private MqttQos getMqttQos(int qos) {
		return MqttQos.fromCode(qos);
	}

	private void connect() {
		connectDisposable = client.connectWith()
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
			.applyConnect()
			.toFlowable()
			.firstElement()
			.subscribeOn(Schedulers.io())
			.retryWhen(errors -> errors.scan(1, (retryCount, error) -> retryCount > 5 ? -1 : retryCount + 1)
				.takeWhile(retryCount -> retryCount != -1)
				.flatMap(retryCount -> Flowable.timer((long) Math.pow(2, retryCount) * 100, TimeUnit.MILLISECONDS)))
			.subscribe(
					ack -> log.info("【Hivemq】 => MQTT连接成功，主机：{}，端口：{}，客户端ID：{}", mqttClientProperties.getHost(),
							mqttClientProperties.getPort(), mqttClientProperties.getClientId()),
					e -> log.error("【Hivemq】 => MQTT连接失败，错误信息：{}", e.getMessage(), e));
	}

	private void consume() {
		if (ObjectUtils.isNotNull(client)) {
			consumeDisposable = client.publishes(MqttGlobalPublishFilter.ALL)
				.onBackpressureBuffer(8192)
				.observeOn(Schedulers.computation(), false, 8192)
				.doOnSubscribe(subscribe -> log.info("【Hivemq】 => MQTT开始订阅消息，请稍候。。。。。。"))
				.subscribeOn(Schedulers.io())
				.retryWhen(errors -> errors.scan(1, (retryCount, error) -> retryCount > 5 ? -1 : retryCount + 1)
					.takeWhile(retryCount -> retryCount != -1)
					.flatMap(retryCount -> Flowable.timer((long) Math.pow(2, retryCount) * 100, TimeUnit.MILLISECONDS)))
				.subscribe(publish -> {
					for (MessageHandler messageHandler : messageHandlers) {
						if (messageHandler.isSubscribe(publish.getTopic().toString())) {
							log.info("【Hivemq】 => MQTT接收到消息，Topic：{}", publish.getTopic());
							messageHandler
								.handle(new MqttMessage(publish.getPayloadAsBytes(), publish.getTopic().toString()));
						}
					}
				}, e -> log.error("【Hivemq】 => MQTT消息处理失败，错误信息：{}", e.getMessage(), e),
						() -> log.info("【Hivemq】 => MQTT订阅消息结束，请稍候。。。。。。"));
		}
	}

	private Mqtt5RxClient getClient() {
		Mqtt5ClientBuilder builder = Mqtt5Client.builder().addConnectedListener(listener -> {
			Optional<? extends MqttClientConnectionConfig> config = Optional
				.of(listener.getClientConfig().getConnectionConfig())
				.get();
			config.ifPresent(mqttClientConnectionConfig -> log.info("【Hivemq】 => MQTT连接保持时间：{}ms",
					mqttClientConnectionConfig.getKeepAlive()));
			log.info("【Hivemq】 => MQTT已连接，客户端ID：{}", mqttClientProperties.getClientId());
			isConnected.compareAndSet(false, true);
			reSubscribe();
		}).addDisconnectedListener(listener -> {
			log.error("【Hivemq】 => MQTT已断开连接，客户端ID：{}", mqttClientProperties.getClientId());
			isConnected.compareAndSet(true, false);
		})
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

}

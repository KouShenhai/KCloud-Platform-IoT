/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.iot.common.config.mqtt;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.netty.handler.codec.mqtt.MqttVersion;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.config.SystemSettingsProperties;
import org.laokou.iot.common.util.VertxMqttUtils;
import org.laokou.iot.session.dto.mqtt.MqttMessageType;
import org.laokou.iot.session.dto.mqtt.MqttMessageV;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author laokou
 */
@Slf4j
public final class VertxMqttClient extends AbstractVertxService<Void> {

	private final MqttClientConfig mqttClientProperties;

	private final MqttClientOptions mqttClientOptions;

	private final List<MessageHandler> messageHandlers;

	private final MqttClient mqttClient;

	private final AtomicBoolean stopping;

	private final AtomicBoolean connecting;

	private long reconnectTimerId;

	private final SystemSettingsProperties systemSettingsProperties;

	public VertxMqttClient(Vertx vertx, MqttClientConfig mqttClientProperties, List<MessageHandler> messageHandlers,
			SystemSettingsProperties systemSettingsProperties) {
		super(vertx);
		this.mqttClientOptions = getMqttClientOptions(mqttClientProperties);
		this.mqttClientProperties = mqttClientProperties;
		this.messageHandlers = messageHandlers;
		this.mqttClient = init();
		this.systemSettingsProperties = systemSettingsProperties;
		this.stopping = new AtomicBoolean(false);
		this.connecting = new AtomicBoolean(false);
		this.reconnectTimerId = -1L;
	}

	@Override
	public Future<String> doDeploy() {
		return super.vertx.deployVerticle(this)
			.onSuccess(_ -> log.info("【Vertx-MQTT-Client】 => MQTT服务部署成功，端口：{}", mqttClientProperties.getPort()))
			.onFailure(ex -> log.error("【Vertx-MQTT-Client】 => MQTT服务部署失败，错误信息：{}", ex.getMessage(), ex));
	}

	@Override
	public void doUndeploy() {
		deploymentIdFuture.compose(vertx::undeploy)
			.onSuccess(_ -> log.info("【Vertx-MQTT-Client】 => MQTT服务卸载成功，端口：{}", mqttClientProperties.getPort()))
			.onFailure(ex -> log.error("【Vertx-MQTT-Client】 => MQTT服务卸载失败，错误信息：{}", ex.getMessage(), ex));
	}

	@Override
	public Future<Void> doOpen() {
		stopping.set(false);
		return connectAndSubscribe();
	}

	@Override
	public Future<Void> doClose() {
		if (!stopping.compareAndSet(false, true)) {
			return Future.succeededFuture();
		}
		cancelReconnectTimer();
		return mqttClient.unsubscribe(getTopics().keySet().stream().toList())
			.compose(v -> mqttClient.disconnect())
			.onSuccess(_ -> log.info("【Vertx-MQTT-Client】 => MQTT断开连接成功，客户端ID：{}", mqttClientProperties.getClientId()))
			.onFailure(ex -> log.error("【Vertx-MQTT-Client】 => MQTT断开连接失败，客户端ID：{}，错误信息：{}",
					mqttClientProperties.getClientId(), ex.getMessage(), ex));
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

	private Future<Void> connectAndSubscribe() {
		if (stopping.get()) {
			return Future.failedFuture("MQTT客户端正在关闭");
		}

		if (mqttClient.isConnected()) {
			return Future.succeededFuture();
		}

		if (!connecting.compareAndSet(false, true)) {
			log.debug("【Vertx-MQTT-Client】 => MQTT正在连接中，忽略重复连接请求，客户端ID：{}", mqttClientProperties.getClientId());
			return Future.succeededFuture();
		}

		return mqttClient.connect(mqttClientProperties.getPort(), mqttClientProperties.getHost()).onSuccess(connAck -> {
			log.info("【Vertx-MQTT-Client】 => MQTT连接成功，主机：{}，端口：{}，客户端ID：{}，返回码：{}，存在会话：{}",
					mqttClientProperties.getHost(), mqttClientProperties.getPort(), mqttClientProperties.getClientId(),
					connAck.code(), connAck.isSessionPresent());
		}).compose(_ -> subscribe()).onFailure(ex -> {
			log.error("【Vertx-MQTT-Client】 => MQTT连接或订阅失败，主机：{}，端口：{}，客户端ID：{}，异常类型：{}，原因：{}",
					mqttClientProperties.getHost(), mqttClientProperties.getPort(), mqttClientProperties.getClientId(),
					ex.getClass().getName(), ex.getMessage(), ex);
			/*
			 * 初次连接过程中，如果TCP连接成功但CONNACK之前连接被关闭， Vert.x不会触发用户注册的closeHandler，只会让connect
			 * Future失败. 因此这里也必须重连.
			 */
			if (!mqttClient.isConnected()) {
				scheduleReconnect();
			}
		}).eventually(() -> {
			connecting.set(false);
			return Future.succeededFuture();
		});
	}

	private synchronized void scheduleReconnect() {
		if (stopping.get() || mqttClient.isConnected() || reconnectTimerId != -1L) {
			return;
		}

		long reconnectInterval = mqttClientProperties.getReconnectInterval();

		log.warn("【Vertx-MQTT-Client】 => MQTT将在{}毫秒后尝试重连，客户端ID：{}", reconnectInterval,
				mqttClientProperties.getClientId());

		reconnectTimerId = vertx.setTimer(reconnectInterval, timerId -> {
			synchronized (this) {
				reconnectTimerId = -1L;
			}
			connectAndSubscribe();
		});
	}

	private synchronized void cancelReconnectTimer() {
		if (reconnectTimerId == -1L) {
			return;
		}
		vertx.cancelTimer(reconnectTimerId);
		reconnectTimerId = -1L;
	}

	private Future<Void> subscribe() {
		Map<String, Integer> topics = getTopics();
		return mqttClient.subscribe(topics)
			.onSuccess(_ -> log.info("【Vertx-MQTT-Client】 => MQTT订阅成功，主题: {}", String.join("、", topics.keySet())))
			.onFailure(ex -> log.error("【Vertx-MQTT-Client】 => MQTT订阅失败，主题：{}，错误信息：{}",
					String.join("、", topics.keySet()), ex.getMessage(), ex))
			.mapEmpty();
	}

	private Map<String, Integer> getTopics() {
		return MqttMessageType.getTopics(systemSettingsProperties.getTenantCode(), MqttQoS.AT_MOST_ONCE.value());
	}

	private MqttClient init() {
		return MqttClient.create(vertx, mqttClientOptions)
			.exceptionHandler(ex -> log.error("【Vertx-MQTT-Client】 => MQTT底层网络或协议异常，客户端ID：{}，异常类型：{}，错误信息：{}",
					mqttClientProperties.getClientId(), ex.getClass().getName(), ex.getMessage(), ex))
			.disconnectMessageHandler(
					message -> log.error("【Vertx-MQTT-Client】 => Broker发送DISCONNECT，客户端ID：{}，原因码：{}，属性：{}",
							mqttClientProperties.getClientId(), message.code(), message.properties()))
			.closeHandler(_ -> {
				if (stopping.get()) {
					log.info("【Vertx-MQTT-Client】 => MQTT连接已正常关闭，客户端ID：{}", mqttClientProperties.getClientId());
					return;
				}

				log.error("【Vertx-MQTT-Client】 => MQTT连接异常断开，客户端ID：{}", mqttClientProperties.getClientId());

				scheduleReconnect();
			})
			.publishHandler(publishMessage -> {
				String topic = publishMessage.topicName();
				log.debug("【Vertx-MQTT-Client】 => MQTT接收到消息，Topic：{}，QoS：{}，数据包ID：{}", topic, publishMessage.qosLevel(),
						publishMessage.messageId());
				MqttMessageV messageV = MqttMessageV.builder().topic(topic).payload(publishMessage.payload()).build();
				for (MessageHandler messageHandler : messageHandlers) {
					Thread.startVirtualThread(() -> {
						try {
							messageHandler.handle(topic, messageV);
						}
						catch (Exception ex) {
							log.error("【Vertx-MQTT-Client】 => MQTT消息处理失败，Topic：{}，处理器：{}，错误信息：{}", topic,
									messageHandler.getClass().getName(), ex.getMessage(), ex);
						}
					});
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

	private MqttClientOptions getMqttClientOptions(MqttClientConfig mqttClientProperties) {
		MqttClientOptions options = new MqttClientOptions();
		options.setClientId(mqttClientProperties.getClientId());
		options.setCleanSession(mqttClientProperties.isClearSession());
		options.setAutoKeepAlive(mqttClientProperties.isAutoKeepAlive());
		options.setKeepAliveInterval(mqttClientProperties.getKeepAliveInterval());
		options.setReconnectAttempts(mqttClientProperties.getReconnectAttempts());
		options.setReconnectInterval(mqttClientProperties.getReconnectInterval());
		options.setAutoAck(mqttClientProperties.isAutoAck());
		options.setAutoGeneratedClientId(mqttClientProperties.isAutoGeneratedClientId());
		options.setAckTimeout(mqttClientProperties.getAckTimeout());
		options.setReceiveBufferSize(mqttClientProperties.getReceiveBufferSize());
		options.setMaxMessageSize(mqttClientProperties.getMaxMessageSize());
		options.setSsl(mqttClientProperties.isSsl());
		if (mqttClientProperties.isAuth()) {
			options.setPassword(mqttClientProperties.getPassword());
			options.setUsername(mqttClientProperties.getUsername());
		}
		options.setVersion(MqttVersion.MQTT_5.protocolLevel());
		return options;
	}

}

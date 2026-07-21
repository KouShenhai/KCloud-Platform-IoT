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

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.config.SystemSettingsProperties;
import org.laokou.iot.session.dto.mqtt.MqttMessageType;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author laokou
 */
@Slf4j
public final class VertxMqttClientTest extends AbstractVertxService<Void> {

	private final MqttClientConfig config;

	private final SystemSettingsProperties systemSettingsProperties;

	private final List<MessageHandler> messageHandlers;

	private final MqttClient mqttClient;

	/** 正在关闭后不再连接或重连。 */
	private final AtomicBoolean stopping;

	/** 防止多个异常同时触发重复 disconnect。 */
	private final AtomicBoolean disconnecting;

	/** 当前连续重连次数。连接成功后清零。 */
	private final AtomicInteger reconnectAttempt;

	private final AtomicLong reconnectTimerId;

	private final AtomicLong reconnectDelay;

	public VertxMqttClientTest(Vertx vertx, MqttClientConfig config, List<MessageHandler> messageHandlers,
						   SystemSettingsProperties systemSettingsProperties) {
		super(vertx);
		this.config = config;
		this.messageHandlers = messageHandlers;
		this.systemSettingsProperties = systemSettingsProperties;
		this.mqttClient = createClient(buildOptions(config));
		this.stopping = new AtomicBoolean(false);
		this.disconnecting = new AtomicBoolean(false);
		this.reconnectAttempt = new AtomicInteger(0);
		this.reconnectTimerId = new AtomicLong(-1);
		this.reconnectDelay = new AtomicLong(0);
	}

	private MqttClient createClient(MqttClientOptions options) {
		return MqttClient.create(vertx, options)
			.exceptionHandler(this::requestReconnect)
			.disconnectMessageHandler(this::onBrokerDisconnect)
			.closeHandler(ignored -> {
				if (!stopping.get()) {
					requestReconnect(new IllegalStateException("MQTT连接已关闭"));
				}
			})
			.publishHandler(this::onPublishMessage)
			.subscribeCompletionHandler(this::onSubAck)
			.publishAckMessageHandler(this::onPublishAck)
			.publishCompMessageHandler(this::onPublishComplete)
			.publishCompletionHandler(this::onPublishCompleted)
			.publishCompletionExpirationHandler(this::onPublishExpired);
	}

	private void requestReconnect(Throwable cause) {
		if (stopping.get()) {
			log.debug("【Vertx-MQTT-Client】 => 已停止MQTT，不允许重连");
			return;
		}
		log.error("【Vertx-MQTT-Client】 => MQTT连接异常，准备重连，客户端ID：{}，错误信息：{}", config.getClientId(), cause.getMessage(), cause);
		if (mqttClient.isConnected() && disconnecting.compareAndSet(false, true)) {
			mqttClient.disconnect().onComplete(ignored -> {
				disconnecting.set(false);
				scheduleReconnect();
			});
			return;
		}
		scheduleReconnect();
	}

	private void scheduleReconnect() {
		if (stopping.get() || reconnectTimerId.get() != -1) {
			return;
		}
		long delay = getDelay();
		log.warn("【Vertx-MQTT-Client】 => MQTT将在{}ms后执行第{}次重连，客户端ID：{}",
			delay, reconnectAttempt.incrementAndGet(), config.getClientId());

	}

	private long getDelay() {
		long baseDelay = Math.max(100L, config.getReconnectInterval());
		long jitter = ThreadLocalRandom.current().nextLong(Math.max(1L, Math.min(baseDelay / 5L, 1_000L)));
		long delay = reconnectDelay.get() + baseDelay + jitter;
		reconnectDelay.set(delay);
		return delay;
	}

	private Future<Void> subscribe() {
		Map<String, Integer> topics = MqttMessageType.getTopics(systemSettingsProperties.getTenantCode());
		if (topics.isEmpty()) {
			log.warn("【Vertx-MQTT-Client】 => 未配置订阅主题，客户端ID：{}", config.getClientId());
			return Future.succeededFuture();
		}
		return mqttClient.subscribe(topics)
			.onSuccess(ignored -> log.info("【Vertx-MQTT-Client】 => MQTT订阅确认成功，主题：{}，客户端ID：{}", topics.keySet(),
				config.getClientId()))
			.onFailure(throwable -> log.error("【Vertx-MQTT-Client】 => MQTT订阅失败，主题：{}，客户端ID：{}，错误信息：{}", topics.keySet(),
				config.getClientId(), throwable.getMessage(), throwable)).mapEmpty();
	}

	/**
	 * 把项目的 {@link MqttClientConfig} 转换为 Vert.x 的 {@link MqttClientOptions}。
	 * @param config 项目配置
	 * @return Vert.x 原生 MQTT 配置
	 */
	private MqttClientOptions buildOptions(MqttClientConfig config) {
		MqttClientOptions clientOptions = new MqttClientOptions();
		// 只有显式启用认证时才设置用户名密码，避免把空凭证发给 Broker。
		if (config.isAuth()) {
			clientOptions.setUsername(config.getUsername());
			clientOptions.setPassword(config.getPassword());
		}

		// TCP 连接相关配置。
		clientOptions.setTcpNoDelay(config.isTcpNoDelay());
		clientOptions.setTcpKeepAlive(config.isTcpKeepAlive());
		clientOptions.setConnectTimeout(config.getConnectTimeout());
		clientOptions.setSoLinger(config.getSoLinger());
		clientOptions.setTcpFastOpen(config.isTcpFastOpen());
		clientOptions.setReceiveBufferSize(config.getReceiveBufferSize());

		// MQTT 身份、心跳和会话相关配置。
		clientOptions.setClientId(config.getClientId());
		clientOptions.setAutoGeneratedClientId(config.isAutoGeneratedClientId());
		clientOptions.setAutoKeepAlive(config.isAutoKeepAlive());
		clientOptions.setKeepAliveInterval(config.getKeepAliveInterval());
		clientOptions.setCleanSession(config.isCleanSession());
		clientOptions.setVersion(config.getVersion());
		clientOptions.setSessionExpireInterval(config.getSessionExpireInterval());

		// 消息大小、在途消息、TLS 等资源和安全配置。
		clientOptions.setMaxMessageSize(config.getMaxMessageSize());
		clientOptions.setMaxInflightQueue(config.getMaxInflightQueue());
		clientOptions.setSsl(config.isSsl());

		/*
		 * Vert.x 自带重连参数，而这个类也实现了 scheduleReconnect 状态机。目前两处共用同一个 reconnectAttempts
		 * 配置，修改时要同时检查两条重连链，避免重复连接。若以后要明确只保留本类重连， 应拆分“底层重连次数”和“业务重连次数”两个配置，再在这里把底层次数固定为 0。
		 */
		clientOptions.setReconnectAttempts(config.getReconnectAttempts());
		clientOptions.setReconnectInterval(config.getReconnectInterval());

		/*
		 * 入站 ACK 由本类在全部业务处理完成以后统一执行，因此 autoAck 应保持 false。 ackTimeout 同时用于 Vert.x
		 * 的发布确认过期回调，本类也用它等待 SUBACK。
		 */
		clientOptions.setAutoAck(config.isAutoAck());
		clientOptions.setAckTimeout(config.getAckTimeout());
		return clientOptions;
	}

}

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

import io.netty.handler.codec.mqtt.MqttConnectReturnCode;
import io.vertx.core.AsyncResult;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.ThreadingModel;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;
import io.vertx.mqtt.messages.MqttConnAckMessage;
import io.vertx.mqtt.messages.MqttDisconnectMessage;
import io.vertx.mqtt.messages.MqttPublishMessage;
import io.vertx.mqtt.messages.MqttSubAckMessage;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.laokou.common.core.config.SystemSettingsProperties;
import org.laokou.common.core.util.MapUtils;
import org.laokou.iot.common.util.VertxMqttUtils;
import org.laokou.iot.session.dto.mqtt.MqttMessageType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author laokou
 */
@Slf4j
public final class VertxMqttClient extends AbstractVertxService<Void> {

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

	private final long timerInactive;

	/** 表示正在创建定时器，但还未获得 Vert.x 返回的 timerId. */
	private final long timerCreating;

	/** 当前连接流程。多个调用方复用同一个 Future。 */
	private final AtomicReference<Promise<Void>> connectionPromise;

	private final AtomicInteger inFlight;

	private final int maxInFlight;

	public VertxMqttClient(Vertx vertx, MqttClientConfig config, List<MessageHandler> messageHandlers,
			SystemSettingsProperties systemSettingsProperties) {
		super(vertx);
		this.config = config;
		this.timerInactive = -1;
		this.timerCreating = -2;
		this.messageHandlers = messageHandlers;
		this.systemSettingsProperties = systemSettingsProperties;
		this.mqttClient = createClient(buildOptions(config));
		this.stopping = new AtomicBoolean(false);
		this.disconnecting = new AtomicBoolean(false);
		this.reconnectAttempt = new AtomicInteger(0);
		this.reconnectTimerId = new AtomicLong(timerInactive);
		this.connectionPromise = new AtomicReference<>(null);
		this.inFlight = new AtomicInteger(0);
		this.maxInFlight = 8192;
	}

	@Override
	public Future<String> doDeploy() {
		return vertx.deployVerticle(this, buildOptions())
			.onSuccess(deploymentId -> log.info("【Vertx-MQTT-Client】 => MQTT服务部署成功，deploymentId：{}，客户端ID：{}",
					deploymentId, config.getClientId()))
			.onFailure(ex -> log.error("【Vertx-MQTT-Client】 => MQTT服务部署失败，客户端ID：{}", config.getClientId(), ex));
	}

	@Override
	public void doUndeploy() {
		deploymentIdFuture.compose(vertx::undeploy)
			.onSuccess(ignored -> log.info("【Vertx-MQTT-Client】 => MQTT服务卸载成功，客户端ID：{}", config.getClientId()))
			.onFailure(ex -> log.error("【Vertx-MQTT-Client】 => MQTT服务卸载失败，客户端ID：{}", config.getClientId(), ex));
	}

	@Override
	public Future<Void> doOpen() {
		if (stopping.get()) {
			return Future.failedFuture("MQTT客户端正在关闭");
		}
		return connectAndSubscribe();
	}

	@Override
	public Future<Void> doClose() {
		if (!stopping.compareAndSet(false, true)) {
			return Future.succeededFuture();
		}
		Promise<Void> connecting = connectionPromise.get();
		Future<Void> waitForConnect = connecting == null ? Future.succeededFuture()
				: connecting.future().recover(_ -> Future.succeededFuture());
		return waitForConnect.compose(_ -> disconnect());
	}

	private DeploymentOptions buildOptions() {
		DeploymentOptions deploymentOptions = new DeploymentOptions();
		deploymentOptions.setThreadingModel(ThreadingModel.VIRTUAL_THREAD);
		return deploymentOptions;
	}

	private Future<Void> disconnect() {
		if (!mqttClient.isConnected()) {
			return Future.succeededFuture();
		}
		return mqttClient.disconnect().recover(ex -> {
			log.warn("【Vertx-MQTT-Client】 => MQTT断开连接失败，客户端ID：{}，错误信息：{}", config.getClientId(), ex.getMessage(), ex);
			return Future.succeededFuture();
		});
	}

	// -------------------------------------------------------------------------
	// 公共发布 API
	// -------------------------------------------------------------------------

	/**
	 * 发布消息。
	 *
	 * <p>
	 * QoS 0 在 PUBLISH 写出后完成；QoS 1/2 在收到 PUBACK/PUBCOMP 后完成。
	 * </p>
	 * Sends the PUBLISH message to the remote MQTT server.
	 * @param topic topic on which the message is published
	 * @param payload message payload
	 * @param qos QoS level
	 * @param isDup if the message is a duplicate
	 * @param isRetain if the message needs to be retained
	 */
	public Future<Void> publish(@NonNull String topic, int qos, @NonNull Buffer payload, boolean isDup,
			boolean isRetain) {
		if (stopping.get()) {
			return Future.failedFuture("MQTT客户端正在关闭");
		}

		if (!mqttClient.isConnected()) {
			return Future.failedFuture("MQTT客户端未连接");
		}
		return mqttClient.publish(topic, payload, VertxMqttUtils.convertQos(qos), isDup, isRetain).mapEmpty();
	}

	private MqttClient createClient(MqttClientOptions options) {
		return MqttClient.create(vertx, options)
			.exceptionHandler(this::requestReconnect)
			.disconnectMessageHandler(this::onBrokerDisconnect)
			.closeHandler(_ -> {
				if (!stopping.get()) {
					requestReconnect(new IllegalStateException("MQTT连接已关闭"));
				}
			})
			.publishHandler(this::onPublishMessage)
			.subscribeCompletionHandler(this::onSubAck)
			.publishCompletionHandler(
					messageId -> log.debug("【Vertx-MQTT-Client】 => 接收MQTT的PUBACK或PUBCOMP数据包，数据包ID：{}", messageId))
			.subscribeCompletionHandler(
					ack -> log.debug("【Vertx-MQTT-Client】 => 接收MQTT的SUBACK数据包，数据包ID：{}", ack.messageId()))
			.unsubscribeCompletionHandler(id -> log.debug("【Vertx-MQTT-Client】 => 接收MQTT的UNSUBACK数据包，数据包ID：{}", id))
			.pingResponseHandler(_ -> log.debug("【Vertx-MQTT-Client】 => 接收MQTT的PINGRESP数据包"));
	}

	private void onSubAck(MqttSubAckMessage message) {
		List<Integer> grantedQos = message.grantedQoSLevels();
		if (grantedQos == null || grantedQos.isEmpty()) {
			requestReconnect(new IllegalStateException("SUBACK为空，packetId=" + message.messageId()));
			return;
		}
		List<Integer> rejected = new ArrayList<>();
		for (Integer qos : grantedQos) {
			if (qos == null || qos < 0 || qos > 2) {
				rejected.add(qos);
			}
		}
		if (!rejected.isEmpty()) {
			requestReconnect(new IllegalStateException(
					"Broker拒绝订阅，packetId=" + message.messageId() + "，reasonCodes=" + grantedQos));
			return;
		}
		log.info("【Vertx-MQTT-Client】 => MQTT订阅确认成功，packetId：{}，客户端ID：{}", message.messageId(), config.getClientId());
	}

	// -------------------------------------------------------------------------
	// 入站消息
	// -------------------------------------------------------------------------

	/**
	 * 一个主题只交给第一个匹配的处理器。
	 *
	 * <p>
	 * 处理器不能阻塞 Event Loop，应直接返回异步 Future，例如 Pulsar sendAsync。
	 * </p>
	 */
	private void onPublishMessage(MqttPublishMessage message) {
		int current = inFlight.incrementAndGet();
		if (current >= maxInFlight) {
			mqttClient.pause();
		}
		try {
			Future<Void> handler = findHandler(message);
			if (handler != null) {
				handler.onFailure(ex -> log.error("【Vertx-MQTT-Client】 => MQTT消息处理失败，主题：{}，客户端ID：{}，错误信息：{}",
						message.topicName(), config.getClientId(), ex.getMessage(), ex));
			}
		}
		catch (Throwable ex) {
			requestReconnect(ex);
		}
		finally {
			int remaining = inFlight.decrementAndGet();
			if (remaining <= maxInFlight / 2 && mqttClient.isConnected()) {
				mqttClient.resume();
			}
		}
	}

	private void onBrokerDisconnect(MqttDisconnectMessage message) {
		requestReconnect(new IllegalStateException("Broker主动断开连接，reasonCode=" + message.code()));
	}

	private Future<Void> findHandler(MqttPublishMessage message) {
		for (MessageHandler handler : messageHandlers) {
			try {
				if (handler.supports(message.topicName())) {
					return handler.handle(message);
				}
			}
			catch (Throwable ex) {
				log.error("【Vertx-MQTT-Client】 => MQTT消息处理器异常，主题：{}，客户端ID：{}，错误信息：{}", message.topicName(),
						config.getClientId(), ex.getMessage(), ex);
				throw ex;
			}
		}
		log.warn("【Vertx-MQTT-Client】 => MQTT消息没有匹配的处理器，主题：{}，客户端ID：{}", message.topicName(), config.getClientId());
		return null;
	}

	private void requestReconnect(Throwable cause) {
		if (stopping.get()) {
			log.debug("【Vertx-MQTT-Client】 => MQTT客户端正在停止，不允许重连，客户端ID：{}", config.getClientId());
			return;
		}

		log.error("【Vertx-MQTT-Client】 => MQTT连接异常，准备重连，客户端ID：{}，错误信息：{}", config.getClientId(),
				cause == null ? "未知异常" : cause.getMessage(), cause);

		if (!mqttClient.isConnected()) {
			scheduleReconnect();
			return;
		}
		// 已经有线程正在执行断开操作，等待该操作完成后统一安排重连。
		if (!disconnecting.compareAndSet(false, true)) {
			log.debug("【Vertx-MQTT-Client】 => MQTT客户端正在断开，忽略重复重连请求，客户端ID：{}", config.getClientId());
			return;
		}
		mqttClient.disconnect().onComplete(ar -> {
			try {
				if (stopping.get()) {
					return;
				}
				if (ar.failed()) {
					log.warn("【Vertx-MQTT-Client】 => MQTT客户端断开失败，仍将安排重连，客户端ID：{}，错误信息：{}", config.getClientId(),
							ar.cause() == null ? "未知异常" : ar.cause().getMessage(), ar.cause());
				}
				scheduleReconnect();
			}
			finally {
				disconnecting.set(false);
			}
		});
	}

	private void scheduleReconnect() {
		if (stopping.get() || reconnectTimerId.get() != timerInactive) {
			return;
		}

		/*
		 * 原子抢占定时器槽位。
		 *
		 * 只有一个线程能把： TIMER_INACTIVE -> TIMER_SCHEDULING
		 *
		 * 其他线程会直接返回，避免重复创建定时器。
		 */
		if (!reconnectTimerId.compareAndSet(timerInactive, timerCreating)) {
			return;
		}

		long delay = getDelay();
		log.warn("【Vertx-MQTT-Client】 => MQTT将在{}ms后执行第{}次重连，客户端ID：{}", delay, reconnectAttempt.incrementAndGet(),
				config.getClientId());

		try {
			long timerId = vertx.setTimer(delay, this::handleReconnectTimer);
			reconnectTimerId.compareAndSet(timerCreating, timerId);
		}
		catch (Throwable ex) {
			reconnectTimerId.compareAndSet(timerCreating, timerInactive);
			log.error("【Vertx-MQTT-Client】 => MQTT重连定时器创建失败，客户端ID：{}，错误信息：{}", config.getClientId(), ex.getMessage(),
					ex);
		}
	}

	private void handleReconnectTimer(long timerId) {
		/*
		 * 只有当前有效定时器才能执行：
		 *
		 * 当前 timerId -> TIMER_INACTIVE
		 *
		 * 如果定时器已经替换，CAS失败，直接返回。
		 */
		if (!reconnectTimerId.compareAndSet(timerId, timerInactive)) {
			return;
		}
		connectAndSubscribe()
			.onSuccess(ignored -> log.info("【Vertx-MQTT-Client】 => MQTT重连成功，客户端ID：{}", config.getClientId()))
			.onFailure(throwable -> {
				log.error("【Vertx-MQTT-Client】 => MQTT第{}次重连失败，客户端ID：{}，错误信息：{}", reconnectAttempt.get(),
						config.getClientId(), throwable.getMessage(), throwable);
				scheduleReconnect();
			});
	}

	// -------------------------------------------------------------------------
	// 连接、订阅和重连
	// -------------------------------------------------------------------------

	private Future<Void> connectAndSubscribe() {
		if (stopping.get()) {
			return Future.failedFuture("MQTT客户端正在关闭");
		}
		if (mqttClient.isConnected()) {
			return Future.succeededFuture();
		}
		while (true) {
			Promise<Void> existing = connectionPromise.get();

			if (existing != null) {
				return existing.future();
			}

			Promise<Void> created = Promise.promise();

			if (!connectionPromise.compareAndSet(null, created)) {
				continue;
			}

			try {
				mqttClient.connect(config.getPort(), config.getHost())
					.compose(this::validateConnection)
					.compose(_ -> subscribe())
					.onComplete(result -> completeConnection(created, result));
			}
			catch (Throwable ex) {
				connectionPromise.compareAndSet(created, null);
				created.tryFail(ex);
				scheduleReconnect();
			}
			return created.future();
		}
	}

	private void completeConnection(Promise<Void> promise, AsyncResult<Void> result) {
		/*
		 * 只清理当前连接任务。 防止旧连接回调把新的 connectionPromise 清空。
		 */
		connectionPromise.compareAndSet(promise, null);
		if (result.succeeded() && !stopping.get()) {
			reconnectAttempt.set(0);
			promise.tryComplete();
			log.info("【Vertx-MQTT-Client】 => MQTT连接并订阅成功，客户端ID：{}", config.getClientId());
			return;
		}
		Throwable cause = result.cause() == null ? new IllegalStateException("MQTT连接流程已经停止") : result.cause();

		promise.tryFail(cause);
		if (!stopping.get()) {
			log.error("【Vertx-MQTT-Client】 => MQTT连接或订阅失败，客户端ID：{}，错误信息：{}", config.getClientId(), cause.getMessage(),
					cause);
			scheduleReconnect();
		}
	}

	private Future<Void> validateConnection(MqttConnAckMessage connAck) {
		if (connAck.code() != MqttConnectReturnCode.CONNECTION_ACCEPTED) {
			return Future.failedFuture("Broker拒绝连接，reasonCode = " + connAck.code());
		}
		log.info("【Vertx-MQTT-Client】 => MQTT连接成功，主机：{}，端口：{}，客户端ID：{}，存在会话：{}", config.getHost(), config.getPort(),
				config.getClientId(), connAck.isSessionPresent());
		return Future.succeededFuture();
	}

	private long getDelay() {
		long baseDelay = Math.max(100L, config.getReconnectInterval());
		long jitter = ThreadLocalRandom.current().nextLong(Math.clamp(baseDelay / 5L, 1L, 1_000L));
		return baseDelay + jitter;
	}

	private Future<Void> subscribe() {
		Map<String, Integer> topics = MqttMessageType.getTopics(systemSettingsProperties.getTenantCode());
		if (MapUtils.isEmpty(topics)) {
			log.warn("【Vertx-MQTT-Client】 => 未配置订阅主题，客户端ID：{}", config.getClientId());
			return Future.succeededFuture();
		}
		return mqttClient.subscribe(topics)
			.onSuccess(ignored -> log.info("【Vertx-MQTT-Client】 => MQTT订阅确认成功，主题：{}，客户端ID：{}", topics.keySet(),
					config.getClientId()))
			.onFailure(throwable -> log.error("【Vertx-MQTT-Client】 => MQTT订阅失败，主题：{}，客户端ID：{}，错误信息：{}", topics.keySet(),
					config.getClientId(), throwable.getMessage(), throwable))
			.mapEmpty();
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

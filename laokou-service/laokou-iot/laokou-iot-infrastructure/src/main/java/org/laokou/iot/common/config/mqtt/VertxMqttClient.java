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
import io.netty.handler.codec.mqtt.MqttQoS;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;
import io.vertx.mqtt.messages.MqttConnAckMessage;
import io.vertx.mqtt.messages.MqttDisconnectMessage;
import io.vertx.mqtt.messages.MqttPubAckMessage;
import io.vertx.mqtt.messages.MqttPubCompMessage;
import io.vertx.mqtt.messages.MqttPublishMessage;
import io.vertx.mqtt.messages.MqttSubAckMessage;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.laokou.common.core.config.SystemSettingsProperties;
import org.laokou.iot.common.util.VertxMqttUtils;
import org.laokou.iot.session.dto.mqtt.MqttMessageType;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * 生产级 Vert.x MQTT 客户端.
 *
 * <p>
 * 客户端采用单 Context 状态机管理连接，使用带抖动的指数退避进行重连，并对入站消息实施有界并发和背压。QoS 1/2 消息只会在全部业务处理器成功返回后统一
 * ACK；业务失败时保持未确认并重建连接，让 Broker 在持久会话中重新投递。
 * </p>
 *
 * <p>
 * {@link MessageHandler} 只负责业务处理，不应自行调用 {@link MqttPublishMessage#ack()}。
 * </p>
 *
 * @author laokou
 */
@Slf4j
public final class VertxMqttClient extends AbstractVertxService<Void> {

	private static final long TIMER_INACTIVE = -1L;

	private static final long MAX_RECONNECT_DELAY_MILLIS = 60_000L;

	private static final int MAX_BACKOFF_EXPONENT = 6;

	private static final int JITTER_PERCENT = 20;

	private static final long SHUTDOWN_DRAIN_TIMEOUT_MILLIS = 30_000L;

	private static final long SHUTDOWN_DRAIN_CHECK_INTERVAL_MILLIS = 100L;

	private static final long EXECUTOR_SHUTDOWN_TIMEOUT_MILLIS = 10_000L;

	private final MqttClientConfig config;

	private final SystemSettingsProperties systemSettingsProperties;

	private final MqttClientOptions options;

	private final List<MessageHandler> messageHandlers;

	private final MqttClient mqttClient;

	private final Context boundContext;

	private final ExecutorService messageExecutor;

	private final int maxInFlightMessages;

	private final int resumeThreshold;

	private final int maxQueuedMessages;

	private final long protocolAckTimeoutMillis;

	private final AtomicReference<State> state = new AtomicReference<>(State.NEW);

	private final AtomicLong connectionEpoch = new AtomicLong();

	private final AtomicInteger inFlightMessages = new AtomicInteger();

	private final AtomicInteger queuedMessages = new AtomicInteger();

	private final AtomicInteger pendingPublishCount = new AtomicInteger();

	private final AtomicLong receivedMessages = new AtomicLong();

	private final AtomicLong processedMessages = new AtomicLong();

	private final AtomicLong failedMessages = new AtomicLong();

	private final AtomicLong droppedMessages = new AtomicLong();

	private final AtomicLong reconnectCount = new AtomicLong();

	private final Deque<InboundMessage> inboundQueue = new ArrayDeque<>();

	private final Map<Integer, PendingSubscription> pendingSubscriptions = new HashMap<>();

	private final Map<Integer, PendingPublish> pendingPublishes = new HashMap<>();

	private Promise<Void> connectionPromise;

	private Promise<Void> closePromise;

	private long reconnectTimerId = TIMER_INACTIVE;

	private int reconnectAttempt;

	private boolean closeRequested;

	private boolean intentionalDisconnect;

	private boolean readingPaused;

	public VertxMqttClient(Vertx vertx, MqttClientConfig config, List<MessageHandler> messageHandlers,
			SystemSettingsProperties systemSettingsProperties) {
		super(vertx);
		this.config = config;
		this.messageHandlers = messageHandlers;
		this.systemSettingsProperties = systemSettingsProperties;
		this.boundContext = vertx.getOrCreateContext();
		this.options = buildOptions(config);
		this.maxInFlightMessages = config.getMaxInflightQueue();
		this.resumeThreshold = Math.max(1, maxInFlightMessages / 2);
		this.maxQueuedMessages = maxInFlightMessages;
		this.protocolAckTimeoutMillis = TimeUnit.SECONDS.toMillis(config.getAckTimeout());
		this.messageExecutor = Executors.newThreadPerTaskExecutor(Thread.ofVirtual()
			.name("mqtt-client-message-", 0)
			.uncaughtExceptionHandler(
					(thread, throwable) -> log.error("【Vertx-MQTT-Client】 => 消息虚拟线程存在未捕获异常，线程：{}，客户端ID：{}",
							thread.getName(), config.getClientId(), throwable))
			.factory());
		this.mqttClient = createClient();

		if (config.isCleanSession()) {
			log.warn("【Vertx-MQTT-Client】 => cleanSession=true，断线期间的QoS消息可能无法恢复，生产环境建议使用持久会话，客户端ID：{}",
					config.getClientId());
		}
	}

	// -------------------------------------------------------------------------
	// 生命周期
	// -------------------------------------------------------------------------

	@Override
	public Future<String> doDeploy() {
		return executeOnContext(() -> vertx.deployVerticle(this)
			.onSuccess(deploymentId -> log.info("【Vertx-MQTT-Client】 => MQTT服务部署成功，deploymentId：{}，客户端ID：{}",
					deploymentId, config.getClientId()))
			.onFailure(throwable -> log.error("【Vertx-MQTT-Client】 => MQTT服务部署失败，客户端ID：{}", config.getClientId(),
					throwable)));
	}

	@Override
	public void doUndeploy() {
		Future<String> future = deploymentIdFuture;
		if (future == null) {
			doClose().onFailure(throwable -> log.error("【Vertx-MQTT-Client】 => MQTT服务关闭失败，客户端ID：{}",
					config.getClientId(), throwable));
			return;
		}

		future.compose(vertx::undeploy)
			.onSuccess(ignored -> log.info("【Vertx-MQTT-Client】 => MQTT服务卸载成功，客户端ID：{}", config.getClientId()))
			.onFailure(throwable -> log.error("【Vertx-MQTT-Client】 => MQTT服务卸载失败，客户端ID：{}", config.getClientId(),
					throwable));
	}

	@Override
	public void start(Promise<Void> startPromise) {
		doOpen().onComplete(result -> {
			if (result.failed()) {
				// Broker暂时不可用时仍允许Verticle完成部署，由内部重连状态机持续恢复。
				log.warn("【Vertx-MQTT-Client】 => MQTT初次连接失败，客户端已进入重连流程，客户端ID：{}", config.getClientId(), result.cause());
			}
			startPromise.complete();
		});
	}

	@Override
	public void stop(Promise<Void> stopPromise) {
		doClose().onComplete(result -> {
			if (result.succeeded()) {
				stopPromise.complete();
			}
			else {
				stopPromise.fail(result.cause());
			}
		});
	}

	@Override
	public Future<Void> doOpen() {
		return executeOnContext(() -> {
			State current = state.get();
			if (current.isOperational() && mqttClient.isConnected()) {
				return Future.succeededFuture();
			}
			if (closeRequested || current.isTerminal()) {
				return Future.failedFuture("MQTT客户端已经关闭，无法重新打开");
			}
			if (connectionPromise != null) {
				return connectionPromise.future();
			}

			cancelReconnectTimer();
			if (current == State.NEW) {
				state.set(State.STARTING);
			}
			else {
				state.set(State.RECONNECTING);
			}
			return connectAndSubscribe();
		});
	}

	@Override
	public Future<Void> doClose() {
		return executeOnContext(this::closeOnContext);
	}

	// -------------------------------------------------------------------------
	// 公共 API
	// -------------------------------------------------------------------------

	/**
	 * 发布二进制消息。
	 * @param topic 发布主题
	 * @param qos QoS 等级
	 * @param payload 消息内容
	 * @param duplicate 是否为重复消息
	 * @param retain 是否保留
	 * @return 发布结果
	 */
	public Future<Void> publish(@NonNull String topic, int qos, @NonNull Buffer payload, boolean duplicate,
			boolean retain) {
		return executeOnContext(() -> {
			State current = state.get();
			if (!current.isOperational() || !mqttClient.isConnected()) {
				return Future.failedFuture("MQTT客户端未连接，当前状态：" + current);
			}

			MqttQoS mqttQoS = VertxMqttUtils.convertQos(qos);
			long epoch = connectionEpoch.get();
			return mqttClient.publish(topic, payload, mqttQoS, duplicate, retain).compose(packetId -> {
				if (mqttQoS == MqttQoS.AT_MOST_ONCE) {
					return Future.succeededFuture();
				}
				return awaitPublishCompletion(packetId, epoch);
			});
		}).onFailure(throwable -> {
			if (!closeRequested && !mqttClient.isConnected()) {
				requestReconnect(new IllegalStateException("MQTT发布期间连接已断开", throwable));
			}
		});
	}

	// -------------------------------------------------------------------------
	// 连接、订阅与重连
	// -------------------------------------------------------------------------

	private Future<Void> connectAndSubscribe() {
		if (closeRequested) {
			return Future.failedFuture("MQTT客户端正在关闭");
		}
		if (connectionPromise != null) {
			return connectionPromise.future();
		}

		Promise<Void> promise = Promise.promise();
		connectionPromise = promise;
		state.set(State.CONNECTING);

		try {
			mqttClient.connect(config.getPort(), config.getHost())
				.compose(this::afterConnected)
				.compose(ignored -> subscribeAndAwaitSubAck())
				.onComplete(result -> completeConnectCycle(promise, result.succeeded(), result.cause()));
		}
		catch (Throwable throwable) {
			completeConnectCycle(promise, false, throwable);
		}

		return promise.future();
	}

	private void completeConnectCycle(Promise<Void> promise, boolean succeeded, Throwable failure) {
		connectionPromise = null;
		if (succeeded && !closeRequested) {
			try {
				onConnectionReady();
				promise.tryComplete();
				return;
			}
			catch (Throwable throwable) {
				failure = throwable;
			}
		}

		Throwable cause = failure == null ? new IllegalStateException("MQTT客户端正在关闭") : failure;
		onConnectCycleFailed(cause);
		promise.tryFail(cause);
	}

	private Future<Void> afterConnected(MqttConnAckMessage connAck) {
		if (closeRequested) {
			return Future.failedFuture("MQTT客户端正在关闭");
		}
		if (connAck.code() != MqttConnectReturnCode.CONNECTION_ACCEPTED) {
			return Future.failedFuture("Broker拒绝连接，reasonCode=" + connAck.code());
		}

		long epoch = connectionEpoch.incrementAndGet();
		state.set(State.SUBSCRIBING);
		readingPaused = false;
		log.info("【Vertx-MQTT-Client】 => MQTT连接成功，主机：{}，端口：{}，客户端ID：{}，返回码：{}，存在会话：{}，epoch：{}", config.getHost(),
				config.getPort(), config.getClientId(), connAck.code(), connAck.isSessionPresent(), epoch);
		return Future.succeededFuture();
	}

	private Future<Void> subscribeAndAwaitSubAck() {
		Map<String, Integer> topics = getTopics();
		if (topics.isEmpty()) {
			log.warn("【Vertx-MQTT-Client】 => 未配置MQTT订阅主题，客户端ID：{}", config.getClientId());
			return Future.succeededFuture();
		}

		return mqttClient.subscribe(topics)
			.compose(packetId -> registerSubscriptionWaiter(packetId, topics.size()))
			.onSuccess(ignored -> log.info("【Vertx-MQTT-Client】 => MQTT订阅确认成功，主题：{}，客户端ID：{}", topics.keySet(),
					config.getClientId()))
			.onFailure(throwable -> log.error("【Vertx-MQTT-Client】 => MQTT订阅失败，主题：{}，客户端ID：{}", topics.keySet(),
					config.getClientId(), throwable));
	}

	private Future<Void> registerSubscriptionWaiter(int packetId, int topicCount) {
		Promise<Void> promise = Promise.promise();
		PendingSubscription pending = new PendingSubscription(promise, topicCount);
		PendingSubscription previous = pendingSubscriptions.put(packetId, pending);
		if (previous != null) {
			cancelTimer(previous.timerId);
			previous.promise.tryFail("重复的SUBSCRIBE packetId：" + packetId);
		}

		pending.timerId = vertx.setTimer(protocolAckTimeoutMillis, ignored -> {
			PendingSubscription expired = pendingSubscriptions.remove(packetId);
			if (expired != null) {
				expired.promise.tryFail(new TimeoutException("等待SUBACK超时，packetId=" + packetId));
			}
		});
		return promise.future();
	}

	private void onSubAck(MqttSubAckMessage subAck) {
		runOnContext(() -> {
			PendingSubscription pending = pendingSubscriptions.remove(subAck.messageId());
			if (pending == null) {
				log.warn("【Vertx-MQTT-Client】 => 收到无法匹配的SUBACK，packetId：{}，客户端ID：{}", subAck.messageId(),
						config.getClientId());
				return;
			}

			cancelTimer(pending.timerId);
			List<Integer> grantedLevels = subAck.grantedQoSLevels();
			if (grantedLevels == null || grantedLevels.size() != pending.topicCount) {
				pending.promise.tryFail("SUBACK数量不匹配，packetId=" + subAck.messageId() + "，expected=" + pending.topicCount
						+ "，actual=" + (grantedLevels == null ? 0 : grantedLevels.size()));
				return;
			}

			for (Integer grantedLevel : grantedLevels) {
				if (grantedLevel == null || grantedLevel < 0 || grantedLevel > 2) {
					pending.promise
						.tryFail("Broker拒绝订阅，packetId=" + subAck.messageId() + "，reasonCodes=" + grantedLevels);
					return;
				}
			}
			pending.promise.tryComplete();
		});
	}

	private void onConnectionReady() {
		reconnectAttempt = 0;
		cancelReconnectTimer();
		state.set(State.CONNECTED);
		readingPaused = false;
		mqttClient.resume();
		log.info("【Vertx-MQTT-Client】 => MQTT客户端就绪，客户端ID：{}，最大并发：{}，队列容量：{}", config.getClientId(), maxInFlightMessages,
				maxQueuedMessages);
	}

	private void onConnectCycleFailed(Throwable cause) {
		if (!closeRequested) {
			state.set(State.DISCONNECTED);
			log.error("【Vertx-MQTT-Client】 => MQTT连接或订阅失败，主机：{}，端口：{}，客户端ID：{}，错误信息：{}", config.getHost(),
					config.getPort(), config.getClientId(), cause.getMessage(), cause);
		}

		invalidateConnection(cause);
		disconnectIntentionally().onComplete(ignored -> {
			if (!closeRequested) {
				scheduleReconnect();
			}
		});
	}

	private void scheduleReconnect() {
		if (closeRequested || state.get().isTerminal() || reconnectTimerId != TIMER_INACTIVE) {
			return;
		}

		int maxAttempts = config.getReconnectAttempts();
		if (maxAttempts == 0) {
			state.set(State.DISCONNECTED);
			log.error("【Vertx-MQTT-Client】 => MQTT自动重连已禁用，客户端ID：{}", config.getClientId());
			return;
		}

		int attempt = ++reconnectAttempt;
		if (maxAttempts > 0 && attempt > maxAttempts) {
			state.set(State.DISCONNECTED);
			log.error("【Vertx-MQTT-Client】 => MQTT重连次数已耗尽，最大次数：{}，客户端ID：{}", maxAttempts, config.getClientId());
			return;
		}

		long delay = calculateReconnectDelay(attempt);
		state.set(State.RECONNECTING);
		reconnectCount.incrementAndGet();
		log.warn("【Vertx-MQTT-Client】 => MQTT将在{}ms后重连，第{}次，客户端ID：{}", delay, attempt, config.getClientId());

		reconnectTimerId = vertx.setTimer(delay, ignored -> {
			reconnectTimerId = TIMER_INACTIVE;
			if (closeRequested || state.get() != State.RECONNECTING) {
				return;
			}
			connectAndSubscribe()
				.onSuccess(_ -> log.info("【Vertx-MQTT-Client】 => MQTT第{}次重连成功，客户端ID：{}", attempt, config.getClientId()))
				.onFailure(throwable -> log.error("【Vertx-MQTT-Client】 => MQTT第{}次重连失败，错误信息：{}，客户端ID：{}", attempt,
						throwable == null ? "未知异常" : throwable.getMessage(), config.getClientId(), throwable));
		});
	}

	private long calculateReconnectDelay(int attempt) {
		long base = config.getReconnectInterval();
		long cap = Math.max(base, MAX_RECONNECT_DELAY_MILLIS);
		int exponent = Math.clamp(attempt - 1, 0, MAX_BACKOFF_EXPONENT);
		long exponential;
		try {
			exponential = Math.multiplyExact(base, 1L << exponent);
		}
		catch (ArithmeticException ignored) {
			exponential = cap;
		}
		long delayWithoutJitter = Math.min(exponential, cap);
		long jitterBound = Math.max(1L, delayWithoutJitter / (100L / JITTER_PERCENT));
		long availableAbove = cap - delayWithoutJitter;
		if (availableAbove > 0) {
			long upperJitter = Math.min(jitterBound, availableAbove);
			return delayWithoutJitter + ThreadLocalRandom.current().nextLong(upperJitter + 1L);
		}
		return Math.max(base, delayWithoutJitter - ThreadLocalRandom.current().nextLong(jitterBound + 1L));
	}

	private void requestReconnect(Throwable cause) {
		runOnContext(() -> {
			State current = state.get();
			if (closeRequested || current.isTerminal() || current == State.DISCONNECTED
					|| current == State.RECONNECTING) {
				return;
			}
			log.warn("【Vertx-MQTT-Client】 => 请求重新建立MQTT连接，当前状态：{}，客户端ID：{}，原因：{}", current, config.getClientId(),
					cause.toString());
			state.set(State.DISCONNECTED);
			invalidateConnection(cause);
			disconnectIntentionally().onComplete(ignored -> {
				if (!closeRequested) {
					scheduleReconnect();
				}
			});
		});
	}

	private void cancelReconnectTimer() {
		long timerId = reconnectTimerId;
		reconnectTimerId = TIMER_INACTIVE;
		cancelTimer(timerId);
	}

	// -------------------------------------------------------------------------
	// 入站消息、背压与 ACK
	// -------------------------------------------------------------------------

	private void onPublishMessage(MqttPublishMessage publishMessage) {
		runOnContext(() -> acceptInboundMessage(publishMessage));
	}

	private void acceptInboundMessage(MqttPublishMessage publishMessage) {
		receivedMessages.incrementAndGet();
		State current = state.get();
		if (!current.isOperational()) {
			if (publishMessage.qosLevel() == MqttQoS.AT_MOST_ONCE) {
				droppedMessages.incrementAndGet();
			}
			return;
		}

		InboundMessage inboundMessage = new InboundMessage(publishMessage, connectionEpoch.get());
		if (inFlightMessages.get() < maxInFlightMessages) {
			dispatchInboundMessage(inboundMessage);
			if (inFlightMessages.get() >= maxInFlightMessages) {
				pauseReading();
			}
			return;
		}

		if (inboundQueue.size() < maxQueuedMessages) {
			inboundQueue.addLast(inboundMessage);
			queuedMessages.incrementAndGet();
			pauseReading();
			return;
		}

		if (publishMessage.qosLevel() == MqttQoS.AT_MOST_ONCE) {
			droppedMessages.incrementAndGet();
			log.warn("【Vertx-MQTT-Client】 => 入站队列已满，丢弃QoS0消息，主题：{}，客户端ID：{}", publishMessage.topicName(),
					config.getClientId());
		}
		else {
			failedMessages.incrementAndGet();
			requestReconnect(
					new RejectedExecutionException("MQTT入站队列已满，QoS " + publishMessage.qosLevel().value() + " 消息保持未确认"));
		}
	}

	private void dispatchInboundMessage(InboundMessage inboundMessage) {
		if (inboundMessage.epoch != connectionEpoch.get()) {
			if (inboundMessage.message.qosLevel() == MqttQoS.AT_MOST_ONCE) {
				droppedMessages.incrementAndGet();
			}
			return;
		}

		inFlightMessages.incrementAndGet();
		try {
			messageExecutor.execute(() -> processInboundMessage(inboundMessage));
		}
		catch (RejectedExecutionException ex) {
			inFlightMessages.decrementAndGet();
			failedMessages.incrementAndGet();
			if (inboundMessage.message.qosLevel() != MqttQoS.AT_MOST_ONCE) {
				requestReconnect(ex);
			}
		}
	}

	private void processInboundMessage(InboundMessage inboundMessage) {
		List<Future<Void>> handlerFutures = new ArrayList<>();
		boolean handled = false;
		try {
			for (MessageHandler messageHandler : messageHandlers) {
				if (messageHandler.supports(inboundMessage.message.topicName())) {
					handled = true;
					handlerFutures.add(messageHandler.handle(inboundMessage.message));
				}
			}
		}
		catch (Throwable throwable) {
			handlerFutures.add(Future.failedFuture(throwable));
		}

		if (!handled) {
			log.warn("【Vertx-MQTT-Client】 => MQTT消息没有匹配的处理器，主题：{}，客户端ID：{}", inboundMessage.message.topicName(),
					config.getClientId());
		}

		Future.join(handlerFutures)
			.mapEmpty()
			.onComplete(result -> runOnContext(
					() -> completeInboundMessage(inboundMessage, result.succeeded() ? null : result.cause())));
	}

	private void completeInboundMessage(InboundMessage inboundMessage, Throwable failure) {
		inFlightMessages.decrementAndGet();
		boolean sameConnection = inboundMessage.epoch == connectionEpoch.get();

		if (failure == null && sameConnection && mqttClient.isConnected()) {
			try {
				if (inboundMessage.message.qosLevel() != MqttQoS.AT_MOST_ONCE) {
					inboundMessage.message.ack();
				}
				processedMessages.incrementAndGet();
			}
			catch (Throwable throwable) {
				failure = throwable;
			}
		}

		if (failure != null) {
			failedMessages.incrementAndGet();
			log.error("【Vertx-MQTT-Client】 => MQTT消息处理或确认失败，主题：{}，QoS：{}，packetId：{}，客户端ID：{}",
					inboundMessage.message.topicName(), inboundMessage.message.qosLevel(),
					inboundMessage.message.messageId(), config.getClientId(), failure);
			if (inboundMessage.message.qosLevel() != MqttQoS.AT_MOST_ONCE && sameConnection) {
				requestReconnect(failure);
			}
		}
		else if (!sameConnection && inboundMessage.message.qosLevel() == MqttQoS.AT_MOST_ONCE) {
			droppedMessages.incrementAndGet();
		}

		drainInboundQueue();
		resumeReadingIfPossible();
	}

	private void drainInboundQueue() {
		while (inFlightMessages.get() < maxInFlightMessages && !inboundQueue.isEmpty()) {
			InboundMessage inboundMessage = inboundQueue.pollFirst();
			if (inboundMessage == null) {
				return;
			}
			queuedMessages.decrementAndGet();
			dispatchInboundMessage(inboundMessage);
		}
	}

	private void pauseReading() {
		if (readingPaused || !mqttClient.isConnected()) {
			return;
		}

		readingPaused = true;
		state.compareAndSet(State.CONNECTED, State.PAUSED);
		try {
			mqttClient.pause();
		}
		catch (Throwable throwable) {
			readingPaused = false;
			state.compareAndSet(State.PAUSED, State.CONNECTED);
			requestReconnect(throwable);
		}
	}

	private void resumeReadingIfPossible() {
		if (!readingPaused || closeRequested || !inboundQueue.isEmpty() || inFlightMessages.get() > resumeThreshold) {
			return;
		}

		if (state.compareAndSet(State.PAUSED, State.CONNECTED) && mqttClient.isConnected()) {
			readingPaused = false;
			try {
				mqttClient.resume();
			}
			catch (Throwable throwable) {
				requestReconnect(throwable);
			}
		}
	}

	private void clearInboundQueue() {
		InboundMessage inboundMessage;
		while ((inboundMessage = inboundQueue.pollFirst()) != null) {
			queuedMessages.decrementAndGet();
			if (inboundMessage.message.qosLevel() == MqttQoS.AT_MOST_ONCE) {
				droppedMessages.incrementAndGet();
			}
		}
	}

	// -------------------------------------------------------------------------
	// 出站发布确认
	// -------------------------------------------------------------------------

	private Future<Void> awaitPublishCompletion(int packetId, long epoch) {
		Promise<Void> promise = Promise.promise();
		PendingPublish previous = pendingPublishes.put(packetId, new PendingPublish(promise, epoch));
		if (previous == null) {
			pendingPublishCount.incrementAndGet();
		}
		else {
			previous.promise.tryFail("重复的PUBLISH packetId：" + packetId);
		}
		return promise.future();
	}

	private void onPublishCompleted(int packetId) {
		runOnContext(() -> {
			PendingPublish pending = pendingPublishes.remove(packetId);
			if (pending == null) {
				return;
			}
			pendingPublishCount.decrementAndGet();
			if (pending.epoch == connectionEpoch.get()) {
				pending.promise.tryComplete();
			}
			else {
				pending.promise.tryFail("MQTT连接已变化，忽略旧连接的发布确认，packetId=" + packetId);
			}
		});
	}

	private void onPublishAck(MqttPubAckMessage message) {
		if (message.code().isError()) {
			failPendingPublish(message.messageId(), new IllegalStateException(
					"Broker拒绝QoS1消息，packetId=" + message.messageId() + "，reasonCode=" + message.code()));
		}
	}

	private void onPublishComplete(MqttPubCompMessage message) {
		if (message.code().isError()) {
			failPendingPublish(message.messageId(), new IllegalStateException(
					"Broker拒绝QoS2消息，packetId=" + message.messageId() + "，reasonCode=" + message.code()));
		}
	}

	private void onPublishCompletionExpired(int packetId) {
		TimeoutException timeout = new TimeoutException("等待PUBACK/PUBCOMP超时，packetId=" + packetId);
		failPendingPublish(packetId, timeout);
		requestReconnect(timeout);
	}

	private void failPendingPublish(int packetId, Throwable cause) {
		runOnContext(() -> {
			PendingPublish pending = pendingPublishes.remove(packetId);
			if (pending != null) {
				pendingPublishCount.decrementAndGet();
				pending.promise.tryFail(cause);
			}
		});
	}

	private void failPendingPublishes(Throwable cause) {
		List<PendingPublish> pending = new ArrayList<>(pendingPublishes.values());
		pendingPublishes.clear();
		pendingPublishCount.set(0);
		for (PendingPublish item : pending) {
			item.promise.tryFail(cause);
		}
	}

	// -------------------------------------------------------------------------
	// 连接事件
	// -------------------------------------------------------------------------

	private MqttClient createClient() {
		return MqttClient.create(vertx, options)
			.exceptionHandler(this::onClientException)
			.disconnectMessageHandler(this::onBrokerDisconnect)
			.closeHandler(this::onConnectionClosed)
			.publishHandler(this::onPublishMessage)
			.subscribeCompletionHandler(this::onSubAck)
			.publishAckMessageHandler(this::onPublishAck)
			.publishCompMessageHandler(this::onPublishComplete)
			.publishCompletionHandler(this::onPublishCompleted)
			.publishCompletionExpirationHandler(this::onPublishCompletionExpired)
			.publishCompletionUnknownPacketIdHandler(packetId -> log
				.warn("【Vertx-MQTT-Client】 => 收到未知packetId的发布确认，packetId：{}，客户端ID：{}", packetId, config.getClientId()))
			.unsubscribeCompletionHandler(packetId -> log
				.debug("【Vertx-MQTT-Client】 => 收到UNSUBACK，packetId：{}，客户端ID：{}", packetId, config.getClientId()))
			.pingResponseHandler(
					ignored -> log.trace("【Vertx-MQTT-Client】 => 收到PINGRESP，客户端ID：{}", config.getClientId()));
	}

	private void onClientException(Throwable throwable) {
		log.error("【Vertx-MQTT-Client】 => MQTT网络或协议异常，当前状态：{}，客户端ID：{}", state.get(), config.getClientId(), throwable);
		requestReconnect(throwable);
	}

	private void onBrokerDisconnect(MqttDisconnectMessage message) {
		log.warn("【Vertx-MQTT-Client】 => Broker主动断开连接，原因码：{}，客户端ID：{}", message.code(), config.getClientId());
		requestReconnect(new IllegalStateException("Broker主动断开连接，reasonCode=" + message.code()));
	}

	private void onConnectionClosed(Void ignored) {
		runOnContext(() -> {
			if (intentionalDisconnect || closeRequested || state.get().isTerminal()) {
				log.debug("【Vertx-MQTT-Client】 => MQTT连接正常关闭，客户端ID：{}", config.getClientId());
				return;
			}
			requestReconnect(new IllegalStateException("MQTT底层连接已关闭"));
		});
	}

	private Future<Void> disconnectIntentionally() {
		if (!mqttClient.isConnected()) {
			return Future.succeededFuture();
		}

		intentionalDisconnect = true;
		return mqttClient.disconnect().recover(throwable -> {
			log.warn("【Vertx-MQTT-Client】 => MQTT断开连接失败，客户端ID：{}", config.getClientId(), throwable);
			return Future.succeededFuture();
		}).eventually(() -> {
			intentionalDisconnect = false;
			return Future.succeededFuture();
		});
	}

	private void invalidateConnection(Throwable cause) {
		connectionEpoch.incrementAndGet();
		readingPaused = false;
		clearInboundQueue();
		failPendingSubscriptions(cause);
		failPendingPublishes(cause);
	}

	private void failPendingSubscriptions(Throwable cause) {
		List<PendingSubscription> pending = new ArrayList<>(pendingSubscriptions.values());
		pendingSubscriptions.clear();
		for (PendingSubscription item : pending) {
			cancelTimer(item.timerId);
			item.promise.tryFail(cause);
		}
	}

	private Future<Void> closeOnContext() {
		if (closePromise != null) {
			return closePromise.future();
		}

		closePromise = Promise.promise();
		closeRequested = true;
		state.set(State.CLOSING);
		cancelReconnectTimer();
		pauseForShutdown();
		failPendingSubscriptions(new IllegalStateException("MQTT客户端正在关闭"));

		Future<Void> waitForConnection = connectionPromise == null ? Future.succeededFuture()
				: connectionPromise.future().recover(ignored -> Future.succeededFuture());

		waitForConnection.compose(ignored -> waitForDrain())
			.compose(ignored -> disconnectIntentionally())
			.compose(ignored -> shutdownExecutor())
			.onComplete(result -> {
				invalidateConnection(new IllegalStateException("MQTT客户端已关闭"));
				state.set(State.CLOSED);
				if (result.succeeded()) {
					log.info("【Vertx-MQTT-Client】 => MQTT客户端已关闭，客户端ID：{}", config.getClientId());
					closePromise.tryComplete();
				}
				else {
					log.error("【Vertx-MQTT-Client】 => MQTT客户端关闭失败，客户端ID：{}", config.getClientId(), result.cause());
					closePromise.tryFail(result.cause());
				}
			});
		return closePromise.future();
	}

	private void pauseForShutdown() {
		readingPaused = true;
		if (mqttClient.isConnected()) {
			try {
				mqttClient.pause();
			}
			catch (Throwable throwable) {
				log.warn("【Vertx-MQTT-Client】 => 关闭阶段暂停读取失败，客户端ID：{}", config.getClientId(), throwable);
			}
		}
		drainInboundQueue();
	}

	private Future<Void> waitForDrain() {
		Promise<Void> promise = Promise.promise();
		long deadline = System.currentTimeMillis() + SHUTDOWN_DRAIN_TIMEOUT_MILLIS;
		checkDrain(promise, deadline);
		return promise.future();
	}

	private void checkDrain(Promise<Void> promise, long deadline) {
		drainInboundQueue();
		if (inFlightMessages.get() == 0 && inboundQueue.isEmpty() && pendingPublishes.isEmpty()) {
			promise.tryComplete();
			return;
		}

		if (System.currentTimeMillis() >= deadline) {
			log.warn("【Vertx-MQTT-Client】 => 优雅排空超时，inFlight：{}，queued：{}，pendingPublish：{}，客户端ID：{}",
					inFlightMessages.get(), queuedMessages.get(), pendingPublishes.size(), config.getClientId());
			clearInboundQueue();
			failPendingPublishes(new TimeoutException("MQTT客户端关闭排空超时"));
			promise.tryComplete();
			return;
		}

		vertx.setTimer(SHUTDOWN_DRAIN_CHECK_INTERVAL_MILLIS, ignored -> checkDrain(promise, deadline));
	}

	private Future<Void> shutdownExecutor() {
		Promise<Void> promise = Promise.promise();
		messageExecutor.shutdown();
		Thread.ofVirtual().name("mqtt-client-shutdown").start(() -> {
			try {
				if (!messageExecutor.awaitTermination(EXECUTOR_SHUTDOWN_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)) {
					messageExecutor.shutdownNow();
				}
				runOnContext(promise::complete);
			}
			catch (InterruptedException ex) {
				messageExecutor.shutdownNow();
				Thread.currentThread().interrupt();
				runOnContext(() -> promise.fail(ex));
			}
		});
		return promise.future();
	}

	private MqttClientOptions buildOptions(MqttClientConfig config) {
		MqttClientOptions clientOptions = new MqttClientOptions();
		if (config.isAuth()) {
			clientOptions.setUsername(config.getUsername());
			clientOptions.setPassword(config.getPassword());
		}

		clientOptions.setTcpNoDelay(config.isTcpNoDelay());
		clientOptions.setTcpKeepAlive(config.isTcpKeepAlive());
		clientOptions.setConnectTimeout(config.getConnectTimeout());
		clientOptions.setSoLinger(config.getSoLinger());
		clientOptions.setReceiveBufferSize(config.getReceiveBufferSize());
		clientOptions.setClientId(config.getClientId());
		clientOptions.setAutoGeneratedClientId(config.isAutoGeneratedClientId());
		clientOptions.setAutoKeepAlive(config.isAutoKeepAlive());
		clientOptions.setKeepAliveInterval(config.getKeepAliveInterval());
		clientOptions.setCleanSession(config.isCleanSession());
		clientOptions.setVersion(config.getVersion());
		clientOptions.setSessionExpireInterval(config.getSessionExpireInterval());
		clientOptions.setMaxMessageSize(config.getMaxMessageSize());
		clientOptions.setMaxInflightQueue(config.getMaxInflightQueue());
		clientOptions.setSsl(config.isSsl());

		// 禁用Vert.x内建重连，避免与本类的状态机产生双重连接和重连风暴。
		clientOptions.setReconnectAttempts(config.getReconnectAttempts());
		clientOptions.setReconnectInterval(config.getReconnectInterval());

		// ACK由本类在全部业务处理完成以后统一执行。
		clientOptions.setAutoAck(config.isAutoAck());
		clientOptions.setAckTimeout(config.getAckTimeout());
		return clientOptions;
	}

	private Map<String, Integer> getTopics() {
		Map<String, Integer> topics = MqttMessageType.getTopics(systemSettingsProperties.getTenantCode());
		for (Map.Entry<String, Integer> entry : topics.entrySet()) {
			if (entry.getKey() == null || entry.getKey().isBlank()) {
				throw new IllegalArgumentException("MQTT订阅主题不能为空");
			}
			Integer qos = entry.getValue();
			if (qos == null || qos < 0 || qos > 2) {
				throw new IllegalArgumentException("MQTT订阅QoS不合法，topic=" + entry.getKey() + "，qos=" + qos);
			}
		}
		return Map.copyOf(topics);
	}

	private <T> Future<T> executeOnContext(@NonNull Supplier<Future<T>> action) {
		if (Vertx.currentContext() == boundContext) {
			try {
				return action.get();
			}
			catch (Throwable throwable) {
				return Future.failedFuture(throwable);
			}
		}

		Promise<T> promise = Promise.promise();
		boundContext.runOnContext(ignored -> {
			try {
				action.get().onComplete(promise);
			}
			catch (Throwable throwable) {
				promise.fail(throwable);
			}
		});
		return promise.future();
	}

	private void runOnContext(@NonNull Runnable action) {
		if (Vertx.currentContext() == boundContext) {
			try {
				action.run();
			}
			catch (Throwable throwable) {
				log.error("【Vertx-MQTT-Client】 => Context任务执行失败，客户端ID：{}", config.getClientId(), throwable);
			}
			return;
		}

		boundContext.runOnContext(ignored -> {
			try {
				action.run();
			}
			catch (Throwable throwable) {
				log.error("【Vertx-MQTT-Client】 => Context任务执行失败，客户端ID：{}", config.getClientId(), throwable);
			}
		});
	}

	private void cancelTimer(long timerId) {
		if (timerId != TIMER_INACTIVE) {
			vertx.cancelTimer(timerId);
		}
	}

	private static final class PendingSubscription {

		private final Promise<Void> promise;

		private final int topicCount;

		private long timerId = TIMER_INACTIVE;

		private PendingSubscription(Promise<Void> promise, int topicCount) {
			this.promise = promise;
			this.topicCount = topicCount;
		}

	}

	private record PendingPublish(Promise<Void> promise, long epoch) {
	}

	private record InboundMessage(MqttPublishMessage message, long epoch) {
	}

}

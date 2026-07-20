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
 */
package org.laokou.iot;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.netty.handler.codec.mqtt.MqttVersion;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;
import io.vertx.mqtt.messages.MqttConnAckMessage;
import io.vertx.mqtt.messages.MqttDisconnectMessage;
import io.vertx.mqtt.messages.MqttPublishMessage;
import io.vertx.mqtt.messages.MqttSubAckMessage;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.config.SystemSettingsProperties;
import org.laokou.iot.common.util.VertxMqttUtils;

import java.time.Duration;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * 生产级 Vert.x MQTT 5 客户端。
 *
 * <p>核心保证：</p>
 * <ul>
 *     <li>所有 MQTT 客户端操作都串行切回绑定的 Vert.x Context。</li>
 *     <li>订阅必须等待 SUBACK，拒绝码不会被误判为订阅成功。</li>
 *     <li>关闭顺序为：停止读取 -> 排空业务 -> 完成 ACK -> 断开连接 -> 关闭线程池。</li>
 *     <li>不在 Vert.x EventLoop 上执行 awaitTermination、Future#get 等阻塞操作。</li>
 * </ul>
 */
@Slf4j
public final class VertxMqttClient extends AbstractVertxService<Void> implements AutoCloseable {

    private static final long TIMER_INACTIVE = -1L;
    private static final long SHUTDOWN_DRAIN_TIMEOUT_MS = 30_000L;
    private static final long EXECUTOR_SHUTDOWN_TIMEOUT_MS = 10_000L;
    private static final long DRAIN_CHECK_INTERVAL_MS = 100L;
    private static final long DEFAULT_PROTOCOL_CONFIRM_TIMEOUT_MS = 30_000L;
    private static final double RESUME_RATIO = 0.50D;
    private static final int QUEUE_MULTIPLIER = 2;
    private static final int MIN_QUEUE_CAPACITY = 32;

    private final MqttClientConfig config;
    private final List<MessageHandler> handlers;
    private final SystemSettingsProperties systemSettings;
    private final MeterRegistry meterRegistry;
    private final MqttClient client;
    private final Context boundContext;

    private final int maxInFlight;
    private final int resumeThreshold;
    private final int maxQueuedMessages;
    private final long protocolConfirmTimeoutMs;

    private final ExecutorService executor;
    private final Deque<InboundEnvelope> inboundQueue = new ArrayDeque<>();
    private final AtomicInteger inFlightCount = new AtomicInteger();
    private final AtomicInteger pendingInboundAcks = new AtomicInteger();
    private final AtomicBoolean readingPaused = new AtomicBoolean();

    private enum State {
        NEW(0),
        STARTING(1),
        CONNECTING(2),
        SUBSCRIBING(3),
        CONNECTED(4),
        PAUSED(5),
        DISCONNECTED(6),
        RECONNECTING(7),
        CLOSING(8),
        CLOSED(9);

        private final int code;

        State(int code) {
            this.code = code;
        }

        boolean isOperational() {
            return this == CONNECTED || this == PAUSED;
        }

        boolean isTerminal() {
            return this == CLOSING || this == CLOSED;
        }
    }

    private final AtomicReference<State> state = new AtomicReference<>(State.NEW);
    private final AtomicBoolean closeRequested = new AtomicBoolean();
    private final AtomicBoolean intentionalDisconnect = new AtomicBoolean();
    private final AtomicInteger reconnectAttempt = new AtomicInteger();
    private final AtomicLong connectionEpoch = new AtomicLong();
    private final AtomicLong reconnectTimerId = new AtomicLong(TIMER_INACTIVE);
    private final AtomicReference<Promise<Void>> closePromiseRef = new AtomicReference<>();

    /** SUBSCRIBE packetId -> waiting promise. */
    private final Object subscriptionLock = new Object();
    private final Map<Integer, PendingSubscription> pendingSubscriptions = new HashMap<>();

    /** Outbound PUBLISH packetId -> waiting PUBACK/PUBCOMP promise. */
    private final Object publishLock = new Object();
    private final Map<Integer, PendingPublish> pendingPublishes = new HashMap<>();

    private final AtomicLong msgReceived = new AtomicLong();
    private final AtomicLong msgProcessed = new AtomicLong();
    private final AtomicLong msgFailed = new AtomicLong();
    private final AtomicLong msgDropped = new AtomicLong();
    private final AtomicLong totalReconnects = new AtomicLong();

    private Timer processTimer;
    private Counter receiveCounter;
    private Counter processCounter;
    private Counter failureCounter;
    private Counter dropCounter;
    private Counter reconnectCounter;
    private final List<Meter> registeredMeters = new ArrayList<>();

    public VertxMqttClient(Vertx vertx,
                           MqttClientConfig config,
                           List<MessageHandler> handlers,
                           SystemSettingsProperties systemSettings,
                           MeterRegistry meterRegistry) {
        super(Objects.requireNonNull(vertx, "vertx"));
        this.boundContext = vertx.getOrCreateContext();
        this.config = validateConfig(config);
        this.handlers = validateHandlers(handlers);
        this.systemSettings = Objects.requireNonNull(systemSettings, "systemSettings");
        this.meterRegistry = meterRegistry;

        this.maxInFlight = Math.max(1, config.getMaxInFlightMessages());
        this.resumeThreshold = Math.max(1, (int) Math.floor(maxInFlight * RESUME_RATIO));
        this.maxQueuedMessages = Math.max(MIN_QUEUE_CAPACITY, maxInFlight * QUEUE_MULTIPLIER);
        this.protocolConfirmTimeoutMs = resolveProtocolConfirmTimeoutMs(config.getAckTimeout());

        this.executor = java.util.concurrent.Executors.newThreadPerTaskExecutor(
            Thread.ofVirtual()
                .name("mqtt-message-", 0)
                .uncaughtExceptionHandler((thread, throwable) ->
                    log.error("【Vertx-MQTT-Client】 => 虚拟线程未捕获异常，线程：{}", thread.getName(), throwable))
                .factory()
        );

        this.client = createClient();
        initMetrics();
    }

    // -------------------------------------------------------------------------
    // 生命周期
    // -------------------------------------------------------------------------

    @Override
    public Future<String> doDeploy() {
        return executeOnContext(() -> {
            if (!state.compareAndSet(State.NEW, State.STARTING)) {
                return Future.failedFuture("MQTT客户端无法部署，当前状态：" + state.get());
            }
            return vertx.deployVerticle(this)
                .onSuccess(deploymentId -> log.info(
                    "【Vertx-MQTT-Client】 => MQTT服务部署成功，deploymentId：{}，客户端ID：{}",
                    deploymentId, config.getClientId()))
                .onFailure(throwable -> {
                    state.compareAndSet(State.STARTING, State.NEW);
                    log.error("【Vertx-MQTT-Client】 => MQTT服务部署失败，客户端ID：{}",
                        config.getClientId(), throwable);
                });
        });
    }

    @Override
    public void start(Promise<Void> startPromise) {
        connectAndSubscribe().onComplete(ar -> {
            if (ar.failed()) {
                // 初次连接失败不让 Verticle 部署失败，由内部重连机制继续恢复。
                log.warn("【Vertx-MQTT-Client】 => 初次连接失败，已进入重连流程，客户端ID：{}",
                    config.getClientId(), ar.cause());
            }
            startPromise.complete();
        });
    }

    @Override
    public void stop(Promise<Void> stopPromise) {
        doCloseAsync().onComplete(ar -> {
            if (ar.succeeded()) {
                stopPromise.complete();
            } else {
                stopPromise.fail(ar.cause());
            }
        });
    }

    @Override
    public Future<Void> doOpen() {
        return executeOnContext(() -> {
            State current = state.get();
            if (current.isOperational()) {
                return Future.succeededFuture();
            }
            if (current == State.STARTING
                || current == State.CONNECTING
                || current == State.SUBSCRIBING
                || current == State.RECONNECTING) {
                return Future.succeededFuture();
            }
            if (current == State.NEW) {
                state.set(State.STARTING);
                return connectAndSubscribe();
            }
            if (current == State.DISCONNECTED) {
                reconnectAttempt.set(0);
                state.set(State.RECONNECTING);
                return connectAndSubscribe();
            }
            return Future.failedFuture("MQTT客户端无法打开，当前状态：" + current);
        });
    }

    @Override
    public Future<Void> doClose() {
        return doCloseAsync();
    }

    /**
     * AutoCloseable 的同步入口。
     * EventLoop 线程上禁止阻塞，只触发异步关闭；普通线程最多等待完整关闭超时。
     */
    @Override
    public void close() {
        Future<Void> closeFuture = doCloseAsync();
        if (Context.isOnEventLoopThread()) {
            closeFuture.onFailure(throwable ->
                log.error("【Vertx-MQTT-Client】 => EventLoop异步关闭失败，客户端ID：{}",
                    config.getClientId(), throwable));
            return;
        }

        try {
            closeFuture.toCompletionStage()
                .toCompletableFuture()
                .get(SHUTDOWN_DRAIN_TIMEOUT_MS + EXECUTOR_SHUTDOWN_TIMEOUT_MS + 5_000L,
                    TimeUnit.MILLISECONDS);
        } catch (Exception ex) {
            log.error("【Vertx-MQTT-Client】 => 同步关闭超时，执行强制清理，客户端ID：{}",
                config.getClientId(), ex);
            forceCleanup();
        }
    }

    // -------------------------------------------------------------------------
    // 公共 API
    // -------------------------------------------------------------------------

    /**
     * 发布消息。
     *
     * <ul>
     *     <li>QoS 0：PUBLISH 写出后完成。</li>
     *     <li>QoS 1：收到 PUBACK 后完成。</li>
     *     <li>QoS 2：收到 PUBCOMP 后完成。</li>
     * </ul>
     */
    public Future<Void> publish(String topic, int qos, String payload, boolean dup, boolean retain) {
        Objects.requireNonNull(payload, "payload");
        return publish(topic, qos, Buffer.buffer(payload), dup, retain);
    }

    public Future<Void> publish(String topic, int qos, Buffer payload, boolean dup, boolean retain) {
        Objects.requireNonNull(topic, "topic");
        Objects.requireNonNull(payload, "payload");

        return executeOnContext(() -> {
            State current = state.get();
            if (!current.isOperational() || !client.isConnected()) {
                return Future.failedFuture("MQTT客户端未连接，当前状态：" + current);
            }

            MqttQoS mqttQoS = VertxMqttUtils.convertQos(qos);
            Future<Integer> sendFuture = client.publish(topic, payload, mqttQoS, dup, retain);

            if (mqttQoS == MqttQoS.AT_MOST_ONCE) {
                return sendFuture.mapEmpty();
            }

            long epoch = connectionEpoch.get();
            return sendFuture.compose(packetId -> awaitPublishCompletion(packetId, epoch));
        }).onFailure(throwable -> {
            if (!closeRequested.get() && !client.isConnected()) {
                requestReconnect(new IllegalStateException("MQTT发布期间连接已断开", throwable));
            }
        });
    }

    public boolean isConnected() {
        return state.get().isOperational() && client.isConnected();
    }

    public String getState() {
        return state.get().name();
    }

    public MqttMetrics getMetrics() {
        return new MqttMetrics(
            msgReceived.get(),
            msgProcessed.get(),
            msgFailed.get(),
            msgDropped.get(),
            saturatedInt(totalReconnects.get()),
            inFlightCount.get(),
            pendingInboundAcks.get(),
            state.get().name(),
            System.currentTimeMillis()
        );
    }

    public HealthStatus health() {
        State current = state.get();
        boolean healthy = current.isOperational() && client.isConnected();
        return new HealthStatus(healthy, current.name(), getMetrics());
    }

    // -------------------------------------------------------------------------
    // 连接与订阅
    // -------------------------------------------------------------------------

    private Future<Void> connectAndSubscribe() {
        return executeOnContext(() -> {
            if (closeRequested.get()) {
                return Future.failedFuture("MQTT客户端正在关闭");
            }

            State current = state.get();
            if (current != State.STARTING
                && current != State.RECONNECTING
                && current != State.DISCONNECTED) {
                return Future.failedFuture("MQTT客户端无法连接，当前状态：" + current);
            }

            state.set(State.CONNECTING);

            Future<Void> prepare = client.isConnected()
                ? disconnectIntentionally()
                : Future.succeededFuture();

            return prepare
                .compose(ignored -> client.connect(config.getPort(), config.getHost()))
                .compose(this::afterConnected)
                .compose(ignored -> subscribeAndAwaitSubAck())
                .onSuccess(ignored -> onConnectionReady())
                .recover(throwable -> onConnectCycleFailed(throwable)
                    .compose(ignored -> Future.failedFuture(throwable)));
        });
    }

    private Future<Void> afterConnected(MqttConnAckMessage connAck) {
        long epoch = connectionEpoch.incrementAndGet();
        state.set(State.SUBSCRIBING);
        readingPaused.set(false);

        log.info(
            "【Vertx-MQTT-Client】 => MQTT连接成功，主机：{}，端口：{}，客户端ID：{}，返回码：{}，存在会话：{}，epoch：{}",
            config.getHost(),
            config.getPort(),
            config.getClientId(),
            connAck.code(),
            connAck.isSessionPresent(),
            epoch
        );
        return Future.succeededFuture();
    }

    private Future<Void> subscribeAndAwaitSubAck() {
        Map<String, Integer> topics = getTopics();
        if (topics.isEmpty()) {
            log.warn("【Vertx-MQTT-Client】 => 未配置订阅主题，客户端ID：{}", config.getClientId());
            return Future.succeededFuture();
        }

        return client.subscribe(topics)
            .compose(packetId -> registerSubscriptionWaiter(packetId, topics.size()))
            .onSuccess(ignored -> log.info(
                "【Vertx-MQTT-Client】 => MQTT订阅确认成功，主题：{}，客户端ID：{}",
                topics.keySet(), config.getClientId()))
            .onFailure(throwable -> log.error(
                "【Vertx-MQTT-Client】 => MQTT订阅失败，主题：{}，客户端ID：{}",
                topics.keySet(), config.getClientId(), throwable));
    }

    private Future<Void> registerSubscriptionWaiter(int packetId, int expectedTopicCount) {
        Promise<Void> promise = Promise.promise();
        PendingSubscription pending = new PendingSubscription(promise, expectedTopicCount);

        synchronized (subscriptionLock) {
            PendingSubscription previous = pendingSubscriptions.put(packetId, pending);
            if (previous != null) {
                previous.promise.tryFail("重复的SUBSCRIBE packetId：" + packetId);
                cancelTimer(previous.timerId);
            }
        }

        pending.timerId = vertx.setTimer(protocolConfirmTimeoutMs, ignored -> {
            PendingSubscription removed;
            synchronized (subscriptionLock) {
                removed = pendingSubscriptions.remove(packetId);
            }
            if (removed != null) {
                removed.promise.tryFail(new TimeoutException(
                    "等待SUBACK超时，packetId=" + packetId));
            }
        });
        return promise.future();
    }

    private void onSubAck(MqttSubAckMessage subAck) {
        runOnContext(() -> {
            PendingSubscription pending;
            synchronized (subscriptionLock) {
                pending = pendingSubscriptions.remove(subAck.messageId());
            }
            if (pending == null) {
                log.warn(
                    "【Vertx-MQTT-Client】 => 收到无法匹配的SUBACK，packetId：{}，客户端ID：{}",
                    subAck.messageId(), config.getClientId()
                );
                return;
            }
            completeSubscription(subAck.messageId(), pending, subAck);
        });
    }

    private void completeSubscription(int packetId,
                                      PendingSubscription pending,
                                      MqttSubAckMessage subAck) {
        cancelTimer(pending.timerId);
        List<Integer> granted = subAck.grantedQoSLevels();

        if (granted == null || granted.size() != pending.expectedTopicCount) {
            pending.promise.fail(new IllegalStateException(
                "SUBACK数量不匹配，packetId=" + packetId
                    + "，expected=" + pending.expectedTopicCount
                    + "，actual=" + (granted == null ? 0 : granted.size())));
            return;
        }

        for (Integer code : granted) {
            // MQTT 3.x/5.x 中 0、1、2 表示成功授予的最大QoS，其余均视为失败码。
            if (code == null || code < 0 || code > 2) {
                pending.promise.fail(new IllegalStateException(
                    "Broker拒绝订阅，packetId=" + packetId + "，reasonCodes=" + granted));
                return;
            }
        }

        pending.promise.complete();
    }

    private void onConnectionReady() {
        reconnectAttempt.set(0);
        cancelReconnectTimer();

        if (closeRequested.get()) {
            state.set(State.CLOSING);
            return;
        }

        state.set(State.CONNECTED);
        readingPaused.set(false);
        safeResume();

        log.info(
            "【Vertx-MQTT-Client】 => MQTT客户端就绪，客户端ID：{}，最大并发：{}，队列容量：{}",
            config.getClientId(), maxInFlight, maxQueuedMessages
        );
    }

    private Future<Void> onConnectCycleFailed(Throwable throwable) {
        log.error(
            "【Vertx-MQTT-Client】 => MQTT连接或订阅失败，主机：{}，端口：{}，客户端ID：{}",
            config.getHost(), config.getPort(), config.getClientId(), throwable
        );

        invalidateConnection(throwable);
        state.set(State.DISCONNECTED);

        Future<Void> disconnect = client.isConnected()
            ? disconnectIntentionally()
            : Future.succeededFuture();

        return disconnect.onComplete(ignored -> {
            if (!closeRequested.get()) {
                scheduleReconnect();
            }
        });
    }

    // -------------------------------------------------------------------------
    // 重连
    // -------------------------------------------------------------------------

    private void scheduleReconnect() {
        runOnContext(() -> {
            if (closeRequested.get() || state.get().isTerminal()) {
                return;
            }
            if (reconnectTimerId.get() != TIMER_INACTIVE) {
                return;
            }
            if (!state.compareAndSet(State.DISCONNECTED, State.RECONNECTING)) {
                return;
            }

            int attempt = reconnectAttempt.incrementAndGet();
            int maxAttempts = config.getReconnectAttempts();
            if (maxAttempts > 0 && attempt > maxAttempts) {
                state.set(State.DISCONNECTED);
                log.error(
                    "【Vertx-MQTT-Client】 => MQTT重连次数已耗尽，最大次数：{}，客户端ID：{}",
                    maxAttempts, config.getClientId()
                );
                return;
            }

            long delay = calculateBackoff(attempt);
            totalReconnects.incrementAndGet();
            if (reconnectCounter != null) {
                reconnectCounter.increment();
            }

            log.warn(
                "【Vertx-MQTT-Client】 => MQTT将在{}ms后重连，第{}次，客户端ID：{}",
                delay, attempt, config.getClientId()
            );

            long timerId = vertx.setTimer(delay, ignored -> {
                reconnectTimerId.set(TIMER_INACTIVE);
                if (closeRequested.get() || state.get() != State.RECONNECTING) {
                    return;
                }
                connectAndSubscribe().onFailure(ex -> {
                    // connectAndSubscribe 已负责转换状态并安排下一轮重连。
                });
            });
            reconnectTimerId.set(timerId);
        });
    }

    private long calculateBackoff(int attempt) {
        long base = config.getReconnectInterval();
        int exponent = Math.min(Math.max(0, attempt - 1), 6);
        long multiplier = Math.min(1L << exponent, 60L);
        long fixed = saturatedMultiply(base, multiplier);
        long jitter = ThreadLocalRandom.current().nextLong(base);
        return saturatedAdd(fixed, jitter);
    }

    private void requestReconnect(Throwable cause) {
        runOnContext(() -> {
            if (closeRequested.get() || state.get().isTerminal()) {
                return;
            }

            State current = state.get();
            if (current == State.DISCONNECTED || current == State.RECONNECTING) {
                return;
            }

            log.warn(
                "【Vertx-MQTT-Client】 => 请求重新建立MQTT连接，状态：{}，客户端ID：{}，原因：{}",
                current, config.getClientId(), cause.toString()
            );

            state.set(State.DISCONNECTED);
            invalidateConnection(cause);

            Future<Void> disconnect = client.isConnected()
                ? disconnectIntentionally()
                : Future.succeededFuture();

            disconnect.onComplete(ignored -> {
                if (!closeRequested.get()) {
                    scheduleReconnect();
                }
            });
        });
    }

    private void cancelReconnectTimer() {
        long timerId = reconnectTimerId.getAndSet(TIMER_INACTIVE);
        cancelTimer(timerId);
    }

    // -------------------------------------------------------------------------
    // 入站消息、背压、手动 ACK
    // -------------------------------------------------------------------------

    private void onPublishMessage(MqttPublishMessage publishMessage) {
        runOnContext(() -> acceptInboundMessage(publishMessage));
    }

    private void acceptInboundMessage(MqttPublishMessage publishMessage) {
        msgReceived.incrementAndGet();
        if (receiveCounter != null) {
            receiveCounter.increment();
        }

        MqttQoS qos = publishMessage.qosLevel();
        State current = state.get();

        if (!current.isOperational()) {
            // QoS 1/2 不 ACK，断线后由持久会话重投；QoS 0 无法恢复，只能统计丢弃。
            if (qos == MqttQoS.AT_MOST_ONCE) {
                markDropped();
            }
            return;
        }

        Buffer payloadCopy = publishMessage.payload().copy();
        long epoch = connectionEpoch.get();
        InboundEnvelope envelope = new InboundEnvelope(
            publishMessage,
            new ImmutableMessage(
                publishMessage.topicName(),
                payloadCopy,
                qos,
                publishMessage.messageId()
            ),
            epoch
        );

        if (qos != MqttQoS.AT_MOST_ONCE) {
            pendingInboundAcks.incrementAndGet();
            envelope.pendingAckTracked = true;
        }

        if (inFlightCount.get() < maxInFlight) {
            dispatchInbound(envelope);
            if (inFlightCount.get() >= maxInFlight) {
                pauseReading();
            }
            return;
        }

        if (inboundQueue.size() < maxQueuedMessages) {
            inboundQueue.addLast(envelope);
            pauseReading();
            return;
        }

        // 队列满：QoS 0 明确丢弃；QoS 1/2 绝不 ACK，主动重连要求 Broker 重投。
        settlePendingAckTracking(envelope);
        if (qos == MqttQoS.AT_MOST_ONCE) {
            markDropped();
            log.warn(
                "【Vertx-MQTT-Client】 => 背压队列已满，丢弃QoS0消息，主题：{}，客户端ID：{}",
                publishMessage.topicName(), config.getClientId()
            );
        } else {
            msgFailed.incrementAndGet();
            if (failureCounter != null) {
                failureCounter.increment();
            }
            requestReconnect(new RejectedExecutionException(
                "MQTT入站队列已满，QoS " + qos.value() + " 消息保持未确认并请求重投"));
        }
    }

    private void dispatchInbound(InboundEnvelope envelope) {
        if (envelope.epoch != connectionEpoch.get()) {
            settlePendingAckTracking(envelope);
            if (envelope.message.qos() == MqttQoS.AT_MOST_ONCE) {
                markDropped();
            }
            return;
        }

        inFlightCount.incrementAndGet();
        try {
            executor.submit(() -> processInbound(envelope));
        } catch (RejectedExecutionException ex) {
            inFlightCount.decrementAndGet();
            settlePendingAckTracking(envelope);
            msgFailed.incrementAndGet();
            if (failureCounter != null) {
                failureCounter.increment();
            }
            requestReconnect(ex);
        }
    }

    private void processInbound(InboundEnvelope envelope) {
        long startNanos = System.nanoTime();
        Throwable failure = null;

        try {
            for (MessageHandler handler : handlers) {
                handler.handle(envelope.message);
            }
        } catch (Throwable throwable) {
            failure = throwable;
        } finally {
            long duration = System.nanoTime() - startNanos;
            recordProcessTime(duration);
            Throwable finalFailure = failure;
            runOnContext(() -> completeInbound(envelope, finalFailure));
        }
    }

    private void completeInbound(InboundEnvelope envelope, Throwable failure) {
        inFlightCount.decrementAndGet();

        boolean sameConnection = envelope.epoch == connectionEpoch.get();
        boolean connected = client.isConnected();

        if (failure == null) {
            msgProcessed.incrementAndGet();
            if (processCounter != null) {
                processCounter.increment();
            }

            if (envelope.message.qos() != MqttQoS.AT_MOST_ONCE
                && sameConnection
                && connected) {
                try {
                    // Vert.x 根据 QoS 自动完成正确的入站 ACK 流程：QoS1=PUBACK，QoS2=PUBREC/PUBCOMP。
                    envelope.source.ack();
                } catch (IllegalStateException ex) {
                    log.error(
                        "【Vertx-MQTT-Client】 => MQTT消息ACK失败，messageId：{}，主题：{}，客户端ID：{}",
                        envelope.message.messageId(), envelope.message.topic(), config.getClientId(), ex
                    );
                    requestReconnect(ex);
                }
            }
        } else {
            msgFailed.incrementAndGet();
            if (failureCounter != null) {
                failureCounter.increment();
            }
            log.error(
                "【Vertx-MQTT-Client】 => MQTT消息业务处理失败，不发送ACK，messageId：{}，主题：{}，QoS：{}，客户端ID：{}",
                envelope.message.messageId(),
                envelope.message.topic(),
                envelope.message.qos(),
                config.getClientId(),
                failure
            );

            if (envelope.message.qos() != MqttQoS.AT_MOST_ONCE
                && !closeRequested.get()) {
                // 保持未确认，并通过重连让持久会话重新投递。
                requestReconnect(failure);
            }
        }

        settlePendingAckTracking(envelope);
        drainInboundQueue();
        resumeReadingIfPossible();
    }

    private void drainInboundQueue() {
        State current = state.get();
        boolean canDrain = current.isOperational() || current == State.CLOSING;
        if (!canDrain) {
            return;
        }

        while (inFlightCount.get() < maxInFlight && !inboundQueue.isEmpty()) {
            InboundEnvelope envelope = inboundQueue.pollFirst();
            if (envelope == null) {
                break;
            }
            if (envelope.epoch != connectionEpoch.get()) {
                settlePendingAckTracking(envelope);
                if (envelope.message.qos() == MqttQoS.AT_MOST_ONCE) {
                    markDropped();
                }
                continue;
            }
            dispatchInbound(envelope);
        }
    }

    private void pauseReading() {
        if (!readingPaused.compareAndSet(false, true)) {
            return;
        }

        if (state.compareAndSet(State.CONNECTED, State.PAUSED)) {
            try {
                client.pause();
                log.debug(
                    "【Vertx-MQTT-Client】 => 已暂停读取，inFlight：{}，queued：{}，客户端ID：{}",
                    inFlightCount.get(), inboundQueue.size(), config.getClientId()
                );
            } catch (Exception ex) {
                readingPaused.set(false);
                state.compareAndSet(State.PAUSED, State.CONNECTED);
                log.error("【Vertx-MQTT-Client】 => 暂停读取失败，客户端ID：{}",
                    config.getClientId(), ex);
                requestReconnect(ex);
            }
        } else {
            readingPaused.set(false);
        }
    }

    private void resumeReadingIfPossible() {
        if (!readingPaused.get()) {
            return;
        }
        if (!inboundQueue.isEmpty() || inFlightCount.get() > resumeThreshold) {
            return;
        }
        if (!readingPaused.compareAndSet(true, false)) {
            return;
        }

        if (state.compareAndSet(State.PAUSED, State.CONNECTED)) {
            safeResume();
            log.debug(
                "【Vertx-MQTT-Client】 => 已恢复读取，inFlight：{}，queued：{}，客户端ID：{}",
                inFlightCount.get(), inboundQueue.size(), config.getClientId()
            );
        } else {
            readingPaused.set(state.get() == State.PAUSED);
        }
    }

    private void safeResume() {
        if (!client.isConnected()) {
            return;
        }
        try {
            client.resume();
        } catch (Exception ex) {
            log.error("【Vertx-MQTT-Client】 => 恢复读取失败，客户端ID：{}",
                config.getClientId(), ex);
            requestReconnect(ex);
        }
    }

    private void settlePendingAckTracking(InboundEnvelope envelope) {
        if (envelope.pendingAckTracked && envelope.ackTrackingSettled.compareAndSet(false, true)) {
            pendingInboundAcks.decrementAndGet();
        }
    }

    // -------------------------------------------------------------------------
    // 出站 QoS 发布确认
    // -------------------------------------------------------------------------

    private Future<Void> awaitPublishCompletion(int packetId, long epoch) {
        Promise<Void> promise = Promise.promise();
        PendingPublish pending = new PendingPublish(promise, epoch);

        synchronized (publishLock) {
            PendingPublish previous = pendingPublishes.put(packetId, pending);
            if (previous != null) {
                previous.promise.tryFail("重复的PUBLISH packetId：" + packetId);
                cancelTimer(previous.timerId);
            }
        }

        pending.timerId = vertx.setTimer(protocolConfirmTimeoutMs, ignored -> {
            PendingPublish removed;
            synchronized (publishLock) {
                removed = pendingPublishes.remove(packetId);
            }
            if (removed != null) {
                TimeoutException timeout = new TimeoutException(
                    "等待PUBACK/PUBCOMP超时，packetId=" + packetId);
                removed.promise.tryFail(timeout);
                requestReconnect(timeout);
            }
        });
        return promise.future();
    }

    private void onPublishCompleted(int packetId) {
        if (packetId <= 0) {
            return;
        }
        runOnContext(() -> {
            PendingPublish pending;
            synchronized (publishLock) {
                pending = pendingPublishes.remove(packetId);
            }
            if (pending == null) {
                log.debug(
                    "【Vertx-MQTT-Client】 => 发布确认没有匹配的等待者，packetId：{}，客户端ID：{}",
                    packetId, config.getClientId()
                );
                return;
            }

            cancelTimer(pending.timerId);
            if (pending.epoch != connectionEpoch.get()) {
                pending.promise.tryFail("MQTT连接已变化，忽略旧连接的发布确认，packetId=" + packetId);
            } else {
                pending.promise.tryComplete();
            }
        });
    }

    private void onPublishCompletionExpired(int packetId) {
        if (packetId <= 0) {
            return;
        }
        runOnContext(() -> {
            TimeoutException timeout = new TimeoutException(
                "Vert.x等待PUBACK/PUBCOMP超时，packetId=" + packetId);
            PendingPublish pending;

            synchronized (publishLock) {
                pending = pendingPublishes.remove(packetId);
            }
            if (pending == null) {
                return;
            }

            cancelTimer(pending.timerId);
            pending.promise.tryFail(timeout);
            requestReconnect(timeout);
        });
    }

    private void failPendingPublishes(Throwable cause) {
        List<PendingPublish> pending;
        synchronized (publishLock) {
            pending = new ArrayList<>(pendingPublishes.values());
            pendingPublishes.clear();
        }

        for (PendingPublish item : pending) {
            cancelTimer(item.timerId);
            item.promise.tryFail(cause);
        }
    }

    // -------------------------------------------------------------------------
    // 连接事件
    // -------------------------------------------------------------------------

    private void onException(Throwable throwable) {
        log.error(
            "【Vertx-MQTT-Client】 => MQTT客户端异常，状态：{}，客户端ID：{}",
            state.get(), config.getClientId(), throwable
        );
        if (!closeRequested.get() && !client.isConnected()) {
            requestReconnect(throwable);
        }
    }

    private void onBrokerDisconnect(MqttDisconnectMessage message) {
        log.warn(
            "【Vertx-MQTT-Client】 => Broker主动断开连接，返回码：{}，客户端ID：{}",
            message.code(), config.getClientId()
        );
        requestReconnect(new IllegalStateException(
            "Broker主动断开连接，code=" + message.code()));
    }

    private void onConnectionClose(Void ignored) {
        runOnContext(() -> {
            if (intentionalDisconnect.get() || closeRequested.get() || state.get().isTerminal()) {
                log.debug("【Vertx-MQTT-Client】 => MQTT连接正常关闭，客户端ID：{}",
                    config.getClientId());
                return;
            }
            requestReconnect(new IllegalStateException("MQTT底层连接已关闭"));
        });
    }

    private Future<Void> disconnectIntentionally() {
        if (!client.isConnected()) {
            return Future.succeededFuture();
        }

        intentionalDisconnect.set(true);
        return client.disconnect()
            .otherwiseEmpty()
            .onComplete(ignored -> intentionalDisconnect.set(false));
    }

    private void invalidateConnection(Throwable cause) {
        connectionEpoch.incrementAndGet();
        readingPaused.set(false);
        clearQueuedInboundMessages();
        failPendingSubscriptions(cause);
        failPendingPublishes(cause);
    }

    private void clearQueuedInboundMessages() {
        while (!inboundQueue.isEmpty()) {
            InboundEnvelope envelope = inboundQueue.pollFirst();
            if (envelope == null) {
                continue;
            }
            settlePendingAckTracking(envelope);
            if (envelope.message.qos() == MqttQoS.AT_MOST_ONCE) {
                markDropped();
            }
        }
    }

    private void failPendingSubscriptions(Throwable cause) {
        List<PendingSubscription> pending;
        synchronized (subscriptionLock) {
            pending = new ArrayList<>(pendingSubscriptions.values());
            pendingSubscriptions.clear();
        }

        for (PendingSubscription item : pending) {
            cancelTimer(item.timerId);
            item.promise.tryFail(cause);
        }
    }

    // -------------------------------------------------------------------------
    // 优雅关闭
    // -------------------------------------------------------------------------

    private Future<Void> doCloseAsync() {
        return executeOnContext(() -> {
            Promise<Void> existing = closePromiseRef.get();
            if (existing != null) {
                return existing.future();
            }

            Promise<Void> closePromise = Promise.promise();
            if (!closePromiseRef.compareAndSet(null, closePromise)) {
                return Objects.requireNonNull(closePromiseRef.get()).future();
            }

            closeRequested.set(true);
            state.set(State.CLOSING);
            cancelReconnectTimer();
            pauseForShutdown();

            waitForDrain()
                .compose(ignored -> disconnectIntentionally())
                .compose(ignored -> shutdownExecutorAsync())
                .onComplete(ar -> {
                    invalidateConnection(new IllegalStateException("MQTT客户端已关闭"));
                    removeMetrics();
                    state.set(State.CLOSED);

                    if (ar.succeeded()) {
                        log.info("【Vertx-MQTT-Client】 => MQTT客户端已关闭，客户端ID：{}",
                            config.getClientId());
                        closePromise.complete();
                    } else {
                        log.error("【Vertx-MQTT-Client】 => MQTT客户端关闭异常，客户端ID：{}",
                            config.getClientId(), ar.cause());
                        closePromise.fail(ar.cause());
                    }
                });

            return closePromise.future();
        });
    }

    private void pauseForShutdown() {
        readingPaused.set(true);
        if (client.isConnected()) {
            try {
                client.pause();
            } catch (Exception ex) {
                log.warn("【Vertx-MQTT-Client】 => 关闭阶段暂停读取失败，客户端ID：{}",
                    config.getClientId(), ex);
            }
        }
        drainInboundQueue();
    }

    private Future<Void> waitForDrain() {
        Promise<Void> promise = Promise.promise();
        long deadline = System.currentTimeMillis() + SHUTDOWN_DRAIN_TIMEOUT_MS;
        checkDrain(promise, deadline);
        return promise.future();
    }

    private void checkDrain(Promise<Void> promise, long deadline) {
        drainInboundQueue();

        boolean drained = inFlightCount.get() == 0
            && inboundQueue.isEmpty()
            && pendingPublishSize() == 0;

        if (drained) {
            promise.complete();
            return;
        }

        if (System.currentTimeMillis() >= deadline) {
            log.warn(
                "【Vertx-MQTT-Client】 => 优雅排空超时，inFlight：{}，queued：{}，pendingPublish：{}，客户端ID：{}",
                inFlightCount.get(), inboundQueue.size(), pendingPublishSize(), config.getClientId()
            );
            clearQueuedInboundMessages();
            failPendingPublishes(new TimeoutException("MQTT客户端关闭排空超时"));
            promise.complete();
            return;
        }

        vertx.setTimer(DRAIN_CHECK_INTERVAL_MS, ignored -> checkDrain(promise, deadline));
    }

    private Future<Void> shutdownExecutorAsync() {
        Promise<Void> promise = Promise.promise();
        executor.shutdown();

        Thread.ofVirtual().name("mqtt-executor-shutdown").start(() -> {
            try {
                if (!executor.awaitTermination(EXECUTOR_SHUTDOWN_TIMEOUT_MS, TimeUnit.MILLISECONDS)) {
                    executor.shutdownNow();
                    if (!executor.awaitTermination(2_000L, TimeUnit.MILLISECONDS)) {
                        log.warn("【Vertx-MQTT-Client】 => MQTT消息线程池未能完全停止，客户端ID：{}",
                            config.getClientId());
                    }
                }
                runOnContext(promise::complete);
            } catch (InterruptedException ex) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
                runOnContext(() -> promise.fail(ex));
            }
        });

        return promise.future();
    }

    private void forceCleanup() {
        runOnContext(() -> {
            closeRequested.set(true);
            state.set(State.CLOSING);
            cancelReconnectTimer();
            invalidateConnection(new IllegalStateException("MQTT客户端被强制关闭"));

            if (client.isConnected()) {
                disconnectIntentionally().onFailure(throwable ->
                    log.warn("【Vertx-MQTT-Client】 => 强制关闭连接失败，客户端ID：{}",
                        config.getClientId(), throwable));
            }

            executor.shutdownNow();
            removeMetrics();
            state.set(State.CLOSED);

            Promise<Void> closePromise = closePromiseRef.get();
            if (closePromise != null) {
                closePromise.tryComplete();
            }
        });
    }

    // -------------------------------------------------------------------------
    // Client、配置、指标
    // -------------------------------------------------------------------------

    private MqttClient createClient() {
        return MqttClient.create(vertx, buildOptions(config))
            .exceptionHandler(this::onException)
            .disconnectMessageHandler(this::onBrokerDisconnect)
            .closeHandler(this::onConnectionClose)
            .publishHandler(this::onPublishMessage)
            .subscribeCompletionHandler(this::onSubAck)
            .publishCompletionHandler(this::onPublishCompleted)
            .publishCompletionExpirationHandler(this::onPublishCompletionExpired)
            .publishCompletionUnknownPacketIdHandler(packetId -> log.warn(
                "【Vertx-MQTT-Client】 => 收到未知packetId的发布确认，packetId：{}，客户端ID：{}",
                packetId, config.getClientId()))
            .unsubscribeCompletionHandler(packetId -> log.debug(
                "【Vertx-MQTT-Client】 => UNSUBACK，packetId：{}，客户端ID：{}",
                packetId, config.getClientId()))
            .pingResponseHandler(ignored -> log.trace(
                "【Vertx-MQTT-Client】 => PINGRESP，客户端ID：{}", config.getClientId()));
    }

    private static MqttClientConfig validateConfig(MqttClientConfig config) {
        Objects.requireNonNull(config, "config");

        if (config.getHost() == null || config.getHost().isBlank()) {
            throw new IllegalArgumentException("MQTT host不能为空");
        }
        if (config.getPort() <= 0 || config.getPort() > 65_535) {
            throw new IllegalArgumentException("MQTT port不合法：" + config.getPort());
        }
        if (config.getClientId() == null || config.getClientId().isBlank()) {
            throw new IllegalArgumentException("MQTT clientId不能为空");
        }
        if (config.isAuth()
            && (config.getUsername() == null || config.getPassword() == null)) {
            throw new IllegalArgumentException("MQTT已开启认证，但用户名或密码为空");
        }
        if (config.getReconnectInterval() <= 0) {
            throw new IllegalArgumentException("MQTT reconnectInterval必须大于0");
        }
        if (config.getReconnectAttempts() < 0) {
            throw new IllegalArgumentException("MQTT reconnectAttempts不能小于0，0表示无限重连");
        }
        if (config.getMaxInFlightMessages() <= 0) {
            throw new IllegalArgumentException("MQTT maxInFlightMessages必须大于0");
        }
        if (config.getAckTimeout() == 0 || config.getAckTimeout() < -1) {
            throw new IllegalArgumentException("MQTT ackTimeout只能为-1或大于0，单位为秒");
        }
        if (config.isAutoAck()) {
            throw new IllegalArgumentException(
                "生产消费必须配置autoAck=false，否则业务处理前Vert.x就会自动ACK");
        }
        return config;
    }

    private static List<MessageHandler> validateHandlers(List<MessageHandler> handlers) {
        if (handlers == null || handlers.isEmpty()) {
            throw new IllegalArgumentException("MQTT MessageHandler不能为空");
        }
        for (MessageHandler handler : handlers) {
            Objects.requireNonNull(handler, "MessageHandler不能为null");
        }
        return List.copyOf(handlers);
    }

    private MqttClientOptions buildOptions(MqttClientConfig properties) {
        MqttClientOptions options = new MqttClientOptions();

        if (properties.isAuth()) {
            options.setUsername(properties.getUsername());
            options.setPassword(properties.getPassword());
        }

        options.setTcpNoDelay(properties.isTcpNoDelay());
        options.setTcpKeepAlive(properties.isTcpKeepAlive());
        options.setConnectTimeout(properties.getConnectTimeout());
        options.setSoLinger(properties.getSoLinger());
        options.setReceiveBufferSize(properties.getReceiveBufferSize());

        options.setClientId(properties.getClientId());
        options.setAutoGeneratedClientId(properties.isAutoGeneratedClientId());
        options.setCleanSession(properties.isCleanSession());

        options.setAutoKeepAlive(properties.isAutoKeepAlive());
        options.setKeepAliveInterval(properties.getKeepAliveInterval());

        // 禁用Vert.x内置重连，全部由本类状态机统一控制，避免双重重连。
        options.setReconnectAttempts(0);
        options.setReconnectInterval(properties.getReconnectInterval());

        // 必须手动ACK，业务成功以后调用MqttPublishMessage.ack()。
        options.setAutoAck(false);
        options.setAckTimeout(properties.getAckTimeout());
        options.setMaxMessageSize(properties.getMaxMessageSize());

        options.setSsl(properties.isSsl());
        options.setVersion(MqttVersion.MQTT_5.protocolLevel());
        options.setSessionExpireInterval(
            properties.isCleanSession()
                ? 0L
                : Math.max(properties.getSessionExpireInterval(), 3_600L)
        );

        return options;
    }

    private Map<String, Integer> getTopics() {
        Map<String, Integer> topics = MqttMessageType.getTopics(systemSettings.getTenantCode());
        return topics == null ? Map.of() : Map.copyOf(topics);
    }

    private void initMetrics() {
        if (meterRegistry == null) {
            return;
        }

        Tags tags = Tags.of(
            "client.id", config.getClientId(),
            "broker", config.getHost() + ":" + config.getPort()
        );

        processTimer = Timer.builder("mqtt.process.duration")
            .tags(tags)
            .register(meterRegistry);
        receiveCounter = Counter.builder("mqtt.messages.received")
            .tags(tags)
            .register(meterRegistry);
        processCounter = Counter.builder("mqtt.messages.processed")
            .tags(tags)
            .register(meterRegistry);
        failureCounter = Counter.builder("mqtt.messages.failed")
            .tags(tags)
            .register(meterRegistry);
        dropCounter = Counter.builder("mqtt.messages.dropped")
            .tags(tags)
            .register(meterRegistry);
        reconnectCounter = Counter.builder("mqtt.reconnects")
            .tags(tags)
            .register(meterRegistry);

        registeredMeters.add(processTimer);
        registeredMeters.add(receiveCounter);
        registeredMeters.add(processCounter);
        registeredMeters.add(failureCounter);
        registeredMeters.add(dropCounter);
        registeredMeters.add(reconnectCounter);

        registeredMeters.add(Gauge.builder("mqtt.state", state, ref -> ref.get().code)
            .tags(tags)
            .register(meterRegistry));
        registeredMeters.add(Gauge.builder("mqtt.inflight", inFlightCount, AtomicInteger::get)
            .tags(tags)
            .register(meterRegistry));
        registeredMeters.add(Gauge.builder("mqtt.queue.size", this, client -> client.inboundQueueSize())
            .tags(tags)
            .register(meterRegistry));
        registeredMeters.add(Gauge.builder("mqtt.pending.inbound.acks", pendingInboundAcks, AtomicInteger::get)
            .tags(tags)
            .register(meterRegistry));
        registeredMeters.add(Gauge.builder("mqtt.pending.outbound.acks", this, client -> client.pendingPublishSize())
            .tags(tags)
            .register(meterRegistry));
    }

    private void removeMetrics() {
        if (meterRegistry == null) {
            return;
        }
        for (Meter meter : registeredMeters) {
            try {
                meterRegistry.remove(meter);
            } catch (Exception ex) {
                log.debug("【Vertx-MQTT-Client】 => 移除指标失败：{}", meter.getId(), ex);
            }
        }
        registeredMeters.clear();
    }

    private void recordProcessTime(long nanos) {
        if (processTimer != null) {
            processTimer.record(Duration.ofNanos(nanos));
        }
    }

    private void markDropped() {
        msgDropped.incrementAndGet();
        if (dropCounter != null) {
            dropCounter.increment();
        }
    }

    // -------------------------------------------------------------------------
    // 通用工具
    // -------------------------------------------------------------------------

    private <T> Future<T> executeOnContext(Supplier<Future<T>> action) {
        Objects.requireNonNull(action, "action");
        if (Vertx.currentContext() == boundContext) {
            try {
                return Objects.requireNonNull(action.get(), "action future");
            } catch (Throwable throwable) {
                return Future.failedFuture(throwable);
            }
        }

        Promise<T> promise = Promise.promise();
        boundContext.runOnContext(ignored -> {
            try {
                Future<T> future = Objects.requireNonNull(action.get(), "action future");
                future.onComplete(promise);
            } catch (Throwable throwable) {
                promise.fail(throwable);
            }
        });
        return promise.future();
    }

    private void runOnContext(Runnable action) {
        Objects.requireNonNull(action, "action");
        if (Vertx.currentContext() == boundContext) {
            try {
                action.run();
            } catch (Throwable throwable) {
                log.error("【Vertx-MQTT-Client】 => Context任务执行失败，客户端ID：{}",
                    config.getClientId(), throwable);
            }
            return;
        }

        boundContext.runOnContext(ignored -> {
            try {
                action.run();
            } catch (Throwable throwable) {
                log.error("【Vertx-MQTT-Client】 => Context任务执行失败，客户端ID：{}",
                    config.getClientId(), throwable);
            }
        });
    }

    private void cancelTimer(long timerId) {
        if (timerId != TIMER_INACTIVE) {
            vertx.cancelTimer(timerId);
        }
    }

    private int inboundQueueSize() {
        if (Vertx.currentContext() == boundContext) {
            return inboundQueue.size();
        }
        // Gauge可能从指标线程读取；这里只提供近似值，避免跨线程遍历ArrayDeque。
        return Math.max(0, pendingInboundAcks.get() - inFlightCount.get());
    }

    private int pendingPublishSize() {
        synchronized (publishLock) {
            return pendingPublishes.size();
        }
    }

    private static long resolveProtocolConfirmTimeoutMs(int ackTimeoutSeconds) {
        if (ackTimeoutSeconds > 0) {
            return Math.max(1_000L, TimeUnit.SECONDS.toMillis(ackTimeoutSeconds));
        }
        // Vert.x的-1表示无限等待；业务Future不能无限悬挂，因此额外使用30秒保护并触发重连。
        return DEFAULT_PROTOCOL_CONFIRM_TIMEOUT_MS;
    }

    private static long saturatedMultiply(long left, long right) {
        try {
            return Math.multiplyExact(left, right);
        } catch (ArithmeticException ignored) {
            return Long.MAX_VALUE;
        }
    }

    private static long saturatedAdd(long left, long right) {
        try {
            return Math.addExact(left, right);
        } catch (ArithmeticException ignored) {
            return Long.MAX_VALUE;
        }
    }

    private static int saturatedInt(long value) {
        if (value > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        if (value < Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        return (int) value;
    }

    // -------------------------------------------------------------------------
    // 内部数据结构
    // -------------------------------------------------------------------------

    private static final class PendingSubscription {
        private final Promise<Void> promise;
        private final int expectedTopicCount;
        private volatile long timerId = TIMER_INACTIVE;

        private PendingSubscription(Promise<Void> promise, int expectedTopicCount) {
            this.promise = promise;
            this.expectedTopicCount = expectedTopicCount;
        }
    }

    private static final class PendingPublish {
        private final Promise<Void> promise;
        private final long epoch;
        private volatile long timerId = TIMER_INACTIVE;

        private PendingPublish(Promise<Void> promise, long epoch) {
            this.promise = promise;
            this.epoch = epoch;
        }
    }

    private static final class InboundEnvelope {
        private final MqttPublishMessage source;
        private final ImmutableMessage message;
        private final long epoch;
        private final AtomicBoolean ackTrackingSettled = new AtomicBoolean();
        private volatile boolean pendingAckTracked;

        private InboundEnvelope(MqttPublishMessage source,
                                ImmutableMessage message,
                                long epoch) {
            this.source = source;
            this.message = message;
            this.epoch = epoch;
        }
    }

    public record MqttMetrics(
        long received,
        long processed,
        long failed,
        long dropped,
        int reconnects,
        int inFlight,
        int pendingAcks,
        String state,
        long timestamp
    ) {
    }

    public record HealthStatus(boolean healthy, String state, MqttMetrics metrics) {
    }

    public record ImmutableMessage(
        String topic,
        Buffer payload,
        MqttQoS qos,
        int messageId
    ) implements MqttMessage {
    }
}

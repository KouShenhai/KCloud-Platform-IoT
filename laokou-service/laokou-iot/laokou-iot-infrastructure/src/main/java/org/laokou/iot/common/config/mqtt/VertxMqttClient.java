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
 * <strong>先看懂几个名词：</strong>
 * </p>
 *
 * <ul>
 * <li>Broker：MQTT 服务端，例如 EMQX、Mosquitto。</li>
 * <li>Future：一个“将来才会完成”的异步结果；成功表示操作完成，失败会携带异常。</li>
 * <li>Promise：Future 的写入端。本类把 Promise 交给回调完成，再把只读的 Future 返回给调用方。</li>
 * <li>Context：Vert.x 的串行执行上下文。本类把连接状态、队列和 Map 的读写都切回同一个
 * {@link Context}，从而避免多个线程同时修改状态。</li>
 * <li>packetId：MQTT 为订阅和 QoS 1/2 消息分配的协议流水号，用来把请求和确认包对应起来。</li>
 * <li>epoch：本类为每一条新连接生成的“连接代数”。旧连接迟到的异步回调不能影响新连接。</li>
 * <li>背压：下游处理不过来时暂停从网络继续读取，避免消息无限堆积并耗尽内存。</li>
 * </ul>
 *
 * <p>
 * <strong>建议按下面的顺序阅读：</strong>
 * </p>
 *
 * <ol>
 * <li>启动：{@link #doDeploy()} -&gt; {@link #start(Promise)} -&gt; {@link #doOpen()}。</li>
 * <li>建连：{@link #connectAndSubscribe()} -&gt; {@link #afterConnected(MqttConnAckMessage)}
 * -&gt; {@link #subscribeAndAwaitSubAck()}。</li>
 * <li>接收：{@link #onPublishMessage(MqttPublishMessage)} -&gt;
 * {@link #acceptInboundMessage(MqttPublishMessage)} -&gt;
 * {@link #processInboundMessage(InboundMessage)} -&gt;
 * {@link #completeInboundMessage(InboundMessage, Throwable)}。</li>
 * <li>发送：{@link #publish(String, int, Buffer, boolean, boolean)} -&gt;
 * {@link #awaitPublishCompletion(int, long)} -&gt; PUBACK/PUBCOMP 回调。</li>
 * <li>断线：连接事件 -&gt; {@link #requestReconnect(Throwable)} -&gt;
 * {@link #scheduleReconnect()}。</li>
 * <li>关闭：{@link #doClose()} -&gt; {@link #closeOnContext()} -&gt; 排空消息 -&gt; 断开连接 -&gt;
 * 关闭线程池。</li>
 * </ol>
 *
 * <p>
 * <strong>必须保持的三个维护原则：</strong>
 * </p>
 *
 * <ol>
 * <li>除业务处理器外，连接状态和集合只能在 {@link #boundContext} 中修改。</li>
 * <li>{@link MessageHandler} 不得自行调用 {@link MqttPublishMessage#ack()}；客户端会在所有处理器的 Future
 * 成功后统一 ACK。</li>
 * <li>QoS 1/2 处理失败时不要 ACK。这里会主动重连，让 Broker 在持久会话中重新投递，所以业务端还需要做幂等。</li>
 * </ol>
 *
 * <p>
 * 客户端使用带抖动的指数退避进行重连，并对入站消息实施有界并发和背压。类虽然较长，但每个分区只负责上面的一条流程。
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

	/**
	 * 表示“当前没有启用定时器”。Vert.x 返回的有效 timerId 不会使用这个值。
	 */
	private static final long TIMER_INACTIVE = -1L;

	/**
	 * 默认的单次重连等待上限。若配置的基础间隔本身大于该值，代码会以配置值为准。
	 */
	private static final long MAX_RECONNECT_DELAY_MILLIS = 60_000L;

	/**
	 * 指数退避最多放大到 {@code 2^6} 倍，防止移位和乘法不断变大。
	 */
	private static final int MAX_BACKOFF_EXPONENT = 6;

	/**
	 * 重连延迟的随机抖动比例。多个实例同时断线时，抖动可以避免它们在同一时刻一起冲击 Broker。
	 */
	private static final int JITTER_PERCENT = 20;

	/**
	 * 关闭客户端时，最多等待业务消息和待确认发布排空 30 秒。
	 */
	private static final long SHUTDOWN_DRAIN_TIMEOUT_MILLIS = 30_000L;

	/**
	 * 优雅关闭期间，每隔 100 毫秒检查一次是否已经排空。
	 */
	private static final long SHUTDOWN_DRAIN_CHECK_INTERVAL_MILLIS = 100L;

	/**
	 * 消息执行器关闭时的最长等待时间。
	 */
	private static final long EXECUTOR_SHUTDOWN_TIMEOUT_MILLIS = 10_000L;

	/**
	 * MQTT 连接、重连、并发、超时等业务配置。
	 */
	private final MqttClientConfig config;

	/**
	 * 系统级配置。这里主要使用租户编码来生成带租户隔离的订阅主题。
	 */
	private final SystemSettingsProperties systemSettingsProperties;

	/**
	 * 由 {@link MqttClientConfig} 转换得到的 Vert.x 原生 MQTT 配置。
	 */
	private final MqttClientOptions options;

	/**
	 * Spring 注入的消息处理器集合。收到消息后通过 {@link MessageHandler#supports(String)} 选择处理器。
	 */
	private final List<MessageHandler> messageHandlers;

	/**
	 * 真正执行 MQTT 协议收发的 Vert.x 客户端。
	 */
	private final MqttClient mqttClient;

	/**
	 * 本类绑定的唯一 Vert.x Context。
	 *
	 * <p>
	 * 可以把它理解为本类的“单线程控制室”：连接状态、队列和待确认 Map 都回到这里串行修改。
	 * </p>
	 */
	private final Context boundContext;

	/**
	 * 执行业务处理器的虚拟线程池。
	 *
	 * <p>
	 * MQTT 网络回调不能被耗时业务阻塞，所以业务处理从 Event Loop 转移到虚拟线程。真正的并发上限由 {@link #maxInFlightMessages}
	 * 控制，而不是让线程数无限增长。
	 * </p>
	 */
	private final ExecutorService messageExecutor;

	/**
	 * 同一时刻最多允许多少条入站消息正在被业务处理。
	 */
	private final int maxInFlightMessages;

	/**
	 * 恢复网络读取的低水位线。并发降到该值以下后才 resume，避免 pause/resume 高频抖动。
	 */
	private final int resumeThreshold;

	/**
	 * 内存等待队列的最大容量。并发槽位用满后，新消息会暂存在该队列。
	 */
	private final int maxQueuedMessages;

	/**
	 * 本类等待 SUBACK 的超时时间，单位毫秒。同一份 ackTimeout 配置也会交给 Vert.x 控制发布确认超时。
	 */
	private final long protocolAckTimeoutMillis;

	/**
	 * 客户端状态机。AtomicReference 让状态读取可见，并支持 compareAndSet 条件切换。
	 */
	private final AtomicReference<State> state = new AtomicReference<>(State.NEW);

	/**
	 * 当前连接代数。每次建立或废弃连接都会递增，用于识别旧连接迟到的回调。
	 */
	private final AtomicLong connectionEpoch = new AtomicLong();

	/**
	 * 当前正在处理、尚未走到 {@link #completeInboundMessage(InboundMessage, Throwable)} 的消息数。
	 */
	private final AtomicInteger inFlightMessages = new AtomicInteger();

	/**
	 * {@link #inboundQueue} 中等待调度的消息数，单独保存便于监控和日志读取。
	 */
	private final AtomicInteger queuedMessages = new AtomicInteger();

	/**
	 * 已发送但仍在等待 Broker 确认的 QoS 1/2 出站消息数量。
	 */
	private final AtomicInteger pendingPublishCount = new AtomicInteger();

	/** 累计收到的入站消息数。 */
	private final AtomicLong receivedMessages = new AtomicLong();

	/** 累计成功处理并完成必要 ACK 的入站消息数。 */
	private final AtomicLong processedMessages = new AtomicLong();

	/** 累计处理、协议确认或队列接收失败的消息数。 */
	private final AtomicLong failedMessages = new AtomicLong();

	/** 累计明确丢弃的 QoS 0 消息数。QoS 1/2 不会主动丢弃，而是保持未确认等待重投。 */
	private final AtomicLong droppedMessages = new AtomicLong();

	/** 累计安排过的重连次数。 */
	private final AtomicLong reconnectCount = new AtomicLong();

	/**
	 * 有界入站等待队列。ArrayDeque 不是线程安全集合，因此只能在 {@link #boundContext} 中访问。
	 */
	private final Deque<InboundMessage> inboundQueue = new ArrayDeque<>();

	/**
	 * 等待 SUBACK 的订阅请求，key 是 SUBSCRIBE 的 packetId。
	 */
	private final Map<Integer, PendingSubscription> pendingSubscriptions = new HashMap<>();

	/**
	 * 等待 PUBACK/PUBCOMP 的出站消息，key 是 PUBLISH 的 packetId。
	 */
	private final Map<Integer, PendingPublish> pendingPublishes = new HashMap<>();

	/**
	 * 当前连接周期的共享结果。非 null 时说明连接/订阅流程正在进行，后来的调用复用同一个 Future。
	 */
	private Promise<Void> connectionPromise;

	/**
	 * 当前关闭流程的共享结果，保证重复调用 close 不会启动多条关闭链。
	 */
	private Promise<Void> closePromise;

	/**
	 * 当前重连定时器 ID；没有定时器时为 {@link #TIMER_INACTIVE}。
	 */
	private long reconnectTimerId = TIMER_INACTIVE;

	/**
	 * 当前连续重连次数。连接并订阅成功后清零。
	 */
	private int reconnectAttempt;

	/**
	 * 是否已经请求关闭。一旦为 true，连接和重连流程都必须停止。
	 */
	private boolean closeRequested;

	/**
	 * 是否由本类主动断开连接。用于区分正常 disconnect 与意外网络断开，避免正常关闭触发重连。
	 */
	private boolean intentionalDisconnect;

	/**
	 * 是否已经调用 {@link MqttClient#pause()} 暂停网络读取。
	 */
	private boolean readingPaused;

	/**
	 * 创建 MQTT 客户端并准备好所有运行时组件，真正连接 Broker 要等 {@link #doOpen()}。
	 * @param vertx Vert.x 运行时
	 * @param config MQTT 客户端配置
	 * @param messageHandlers 所有可用的业务消息处理器
	 * @param systemSettingsProperties 系统配置
	 */
	public VertxMqttClient(Vertx vertx, MqttClientConfig config, List<MessageHandler> messageHandlers,
			SystemSettingsProperties systemSettingsProperties) {
		super(vertx);
		// 保存依赖，并把配置转换为 Vert.x 能识别的 MqttClientOptions。
		this.config = config;
		this.messageHandlers = messageHandlers;
		this.systemSettingsProperties = systemSettingsProperties;
		// 在构造阶段固定 Context；以后通过 executeOnContext/runOnContext 回到这里。
		this.boundContext = vertx.getOrCreateContext();
		this.options = buildOptions(config);
		// “处理中”和“排队中”各设置一层上限，最坏情况下内存中约保留两倍 maxInflightQueue 条消息。
		this.maxInFlightMessages = config.getMaxInflightQueue();
		this.resumeThreshold = Math.max(1, maxInFlightMessages / 2);
		this.maxQueuedMessages = maxInFlightMessages;
		this.protocolAckTimeoutMillis = TimeUnit.SECONDS.toMillis(config.getAckTimeout());
		// 每条已调度消息使用一个虚拟线程，线程执行到异步 Future 注册完成后即可退出。
		this.messageExecutor = Executors.newThreadPerTaskExecutor(Thread.ofVirtual()
			.name("mqtt-client-message-", 0)
			.uncaughtExceptionHandler(
					(thread, throwable) -> log.error("【Vertx-MQTT-Client】 => 消息虚拟线程存在未捕获异常，线程：{}，客户端ID：{}",
							thread.getName(), config.getClientId(), throwable))
			.factory());
		// 只创建客户端和注册事件处理器，此处不会发起网络连接。
		this.mqttClient = createClient();

		if (config.isCleanSession()) {
			// 清理会话意味着断线后 Broker 不再为本客户端保留未确认消息和订阅状态。
			log.warn("【Vertx-MQTT-Client】 => cleanSession=true，断线期间的QoS消息可能无法恢复，生产环境建议使用持久会话，客户端ID：{}",
					config.getClientId());
		}
	}

	// -------------------------------------------------------------------------
	// 生命周期
	// -------------------------------------------------------------------------

	/**
	 * 把当前对象作为 Verticle 部署到 Vert.x。
	 *
	 * <p>
	 * 部署后 Vert.x 会自动调用 {@link #start(Promise)}。Future 中的 String 是 Vert.x 分配的
	 * deploymentId。
	 * </p>
	 * @return 部署结果
	 */
	@Override
	public Future<String> doDeploy() {
		// 部署也放进 boundContext，保证生命周期入口和其他状态操作使用同一个执行上下文。
		return executeOnContext(() -> vertx.deployVerticle(this)
			.onSuccess(deploymentId -> log.info("【Vertx-MQTT-Client】 => MQTT服务部署成功，deploymentId：{}，客户端ID：{}",
					deploymentId, config.getClientId()))
			.onFailure(throwable -> log.error("【Vertx-MQTT-Client】 => MQTT服务部署失败，客户端ID：{}", config.getClientId(),
					throwable)));
	}

	/**
	 * 卸载 Verticle。Vert.x 会在卸载过程中调用 {@link #stop(Promise)}。
	 *
	 * <p>
	 * 如果还没有 deploymentId（例如部署尚未成功），就直接关闭底层资源。
	 * </p>
	 */
	@Override
	public void doUndeploy() {
		Future<String> future = deploymentIdFuture;
		if (future == null) {
			// 没有成功部署时无法调用 undeploy，但已创建的 MQTT 客户端和线程池仍需要释放。
			doClose().onFailure(throwable -> log.error("【Vertx-MQTT-Client】 => MQTT服务关闭失败，客户端ID：{}",
					config.getClientId(), throwable));
			return;
		}

		future.compose(vertx::undeploy)
			.onSuccess(ignored -> log.info("【Vertx-MQTT-Client】 => MQTT服务卸载成功，客户端ID：{}", config.getClientId()))
			.onFailure(throwable -> log.error("【Vertx-MQTT-Client】 => MQTT服务卸载失败，客户端ID：{}", config.getClientId(),
					throwable));
	}

	/**
	 * Verticle 启动回调。
	 *
	 * <p>
	 * 初次连接失败时仍然完成 startPromise，因为 Broker 临时不可用不应该让整个应用启动失败；本类自己的重连状态机会继续恢复连接。
	 * </p>
	 * @param startPromise 通知 Vert.x“启动完成/失败”的 Promise
	 */
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

	/**
	 * Verticle 停止回调。必须等 {@link #doClose()} 完成后再通知 Vert.x。
	 * @param stopPromise 通知 Vert.x“停止完成/失败”的 Promise
	 */
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

	/**
	 * 打开 MQTT 客户端，建立连接并完成订阅。
	 *
	 * <p>
	 * 这个方法是幂等的：已连接时直接成功；正在连接时返回同一个 {@link #connectionPromise}，不会重复 connect。
	 * </p>
	 * @return 连接并订阅完成的异步结果
	 */
	@Override
	public Future<Void> doOpen() {
		return executeOnContext(() -> {
			State current = state.get();
			// CONNECTED 和 PAUSED 都属于可正常工作的状态，不需要重复建连。
			if (current.isOperational() && mqttClient.isConnected()) {
				return Future.succeededFuture();
			}
			// CLOSED 是终态；一个已经关闭的实例不能再次复用，需要重新创建实例。
			if (closeRequested || current.isTerminal()) {
				return Future.failedFuture("MQTT客户端已经关闭，无法重新打开");
			}
			// 多个调用方同时 open 时，共享正在进行的连接结果。
			if (connectionPromise != null) {
				return connectionPromise.future();
			}

			// 手动 open 应取消尚未触发的重连定时器，避免稍后再启动第二次连接。
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

	/**
	 * 关闭 MQTT 客户端。具体步骤由 {@link #closeOnContext()} 串联。
	 * @return 完整关闭结果
	 */
	@Override
	public Future<Void> doClose() {
		return executeOnContext(this::closeOnContext);
	}

	// -------------------------------------------------------------------------
	// 公共 API
	// -------------------------------------------------------------------------

	/**
	 * 发布二进制消息。
	 *
	 * <p>
	 * QoS 0 的 publish Future 在消息交给网络层后即可成功；QoS 1/2 还要继续等待 Broker 返回 PUBACK/PUBCOMP。
	 * 因此调用方可以把该 Future 当成“本次发送是否真正得到协议确认”的结果。
	 * </p>
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
			// 发送前快速检查，避免在未连接状态把消息交给底层客户端。
			if (!current.isOperational() || !mqttClient.isConnected()) {
				return Future.failedFuture("MQTT客户端未连接，当前状态：" + current);
			}

			MqttQoS mqttQoS = VertxMqttUtils.convertQos(qos);
			// 保存发送时的连接代数。即使 packetId 在新连接中被复用，也不会误认旧确认包。
			long epoch = connectionEpoch.get();
			return mqttClient.publish(topic, payload, mqttQoS, duplicate, retain).compose(packetId -> {
				// QoS 0 没有 PUBACK/PUBCOMP，无法等待 Broker 的协议确认。
				if (mqttQoS == MqttQoS.AT_MOST_ONCE) {
					return Future.succeededFuture();
				}
				// QoS 1/2 把 packetId 登记起来，由后面的确认回调完成 Future。
				return awaitPublishCompletion(packetId, epoch);
			});
		}).onFailure(throwable -> {
			// 发送失败本身不一定表示断线；只有底层已经显示未连接时才触发重连。
			if (!closeRequested && !mqttClient.isConnected()) {
				requestReconnect(new IllegalStateException("MQTT发布期间连接已断开", throwable));
			}
		});
	}

	// -------------------------------------------------------------------------
	// 连接、订阅与重连
	// -------------------------------------------------------------------------

	/**
	 * 执行一个完整连接周期：TCP/MQTT 建连 -&gt; 校验 CONNACK -&gt; 订阅 -&gt; 等待 SUBACK。
	 *
	 * <p>
	 * 只有所有步骤都成功，客户端才会进入 CONNECTED。这样可以避免“网络已连接但订阅失败”时误报为可用。
	 * </p>
	 * @return 当前连接周期的共享 Future
	 */
	private Future<Void> connectAndSubscribe() {
		if (closeRequested) {
			return Future.failedFuture("MQTT客户端正在关闭");
		}
		if (connectionPromise != null) {
			return connectionPromise.future();
		}

		// Promise 贯穿整个连接周期，连接成功、订阅成功或任一步失败都会完成它。
		Promise<Void> promise = Promise.promise();
		connectionPromise = promise;
		state.set(State.CONNECTING);

		try {
			// compose 表示前一步成功才执行下一步；任意一步失败都会直接进入 onComplete。
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

	/**
	 * 统一收尾一次连接周期，清理共享 Promise，并决定进入 CONNECTED 还是失败重连。
	 * @param promise 本次连接周期的 Promise
	 * @param succeeded 异步链是否成功
	 * @param failure 失败原因；成功时通常为 null
	 */
	private void completeConnectCycle(Promise<Void> promise, boolean succeeded, Throwable failure) {
		// 先置空，后续重连才能创建新的连接周期。
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

		// 连接链成功但此时已收到关闭请求，也按关闭失败原因结束，不能再进入 CONNECTED。
		Throwable cause = failure == null ? new IllegalStateException("MQTT客户端正在关闭") : failure;
		onConnectCycleFailed(cause);
		promise.tryFail(cause);
	}

	/**
	 * 处理 Broker 返回的 CONNACK（连接确认包）。
	 * @param connAck Broker 的连接确认
	 * @return 校验结果
	 */
	private Future<Void> afterConnected(MqttConnAckMessage connAck) {
		if (closeRequested) {
			return Future.failedFuture("MQTT客户端正在关闭");
		}
		if (connAck.code() != MqttConnectReturnCode.CONNECTION_ACCEPTED) {
			return Future.failedFuture("Broker拒绝连接，reasonCode=" + connAck.code());
		}

		// 每条成功建立的连接拥有不同 epoch，后续所有消息和确认都带着它做隔离。
		long epoch = connectionEpoch.incrementAndGet();
		state.set(State.SUBSCRIBING);
		readingPaused = false;
		log.info("【Vertx-MQTT-Client】 => MQTT连接成功，主机：{}，端口：{}，客户端ID：{}，返回码：{}，存在会话：{}，epoch：{}", config.getHost(),
				config.getPort(), config.getClientId(), connAck.code(), connAck.isSessionPresent(), epoch);
		return Future.succeededFuture();
	}

	/**
	 * 向 Broker 发送全部主题订阅，并等待 SUBACK。
	 *
	 * <p>
	 * {@code mqttClient.subscribe()} 成功只代表 SUBSCRIBE 已写出，并不代表 Broker 接受了订阅，所以还要通过
	 * {@link #registerSubscriptionWaiter(int, int)} 等待协议确认。
	 * </p>
	 * @return 所有订阅均被 Broker 接受时成功
	 */
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

	/**
	 * 用 packetId 登记一个“等待 SUBACK”的 Promise，并安装超时定时器。
	 * @param packetId SUBSCRIBE 协议包 ID
	 * @param topicCount 本次订阅的主题数量，用于校验 SUBACK 返回项数量
	 * @return SUBACK 校验结果
	 */
	private Future<Void> registerSubscriptionWaiter(int packetId, int topicCount) {
		Promise<Void> promise = Promise.promise();
		PendingSubscription pending = new PendingSubscription(promise, topicCount);
		PendingSubscription previous = pendingSubscriptions.put(packetId, pending);
		if (previous != null) {
			// 正常情况下同一连接内 packetId 不应重复；防御性地结束旧请求，防止它永久等待。
			cancelTimer(previous.timerId);
			previous.promise.tryFail("重复的SUBSCRIBE packetId：" + packetId);
		}

		// Broker 长时间不回复时必须结束等待，否则客户端会永远停在 SUBSCRIBING。
		pending.timerId = vertx.setTimer(protocolAckTimeoutMillis, ignored -> {
			PendingSubscription expired = pendingSubscriptions.remove(packetId);
			if (expired != null) {
				expired.promise.tryFail(new TimeoutException("等待SUBACK超时，packetId=" + packetId));
			}
		});
		return promise.future();
	}

	/**
	 * 处理 SUBACK：找到对应订阅请求，校验每个主题的授权 QoS，并完成等待中的 Promise。
	 * @param subAck Broker 的订阅确认包
	 */
	private void onSubAck(MqttSubAckMessage subAck) {
		// MQTT 事件回调不假设运行在哪个线程，统一切回 boundContext 修改 Map。
		runOnContext(() -> {
			PendingSubscription pending = pendingSubscriptions.remove(subAck.messageId());
			if (pending == null) {
				log.warn("【Vertx-MQTT-Client】 => 收到无法匹配的SUBACK，packetId：{}，客户端ID：{}", subAck.messageId(),
						config.getClientId());
				return;
			}

			// 已收到确认，不论后续校验成功或失败，都不应再让超时定时器触发。
			cancelTimer(pending.timerId);
			List<Integer> grantedLevels = subAck.grantedQoSLevels();
			// 每个订阅主题都应该有一个对应的 reasonCode/授权 QoS。
			if (grantedLevels == null || grantedLevels.size() != pending.topicCount) {
				pending.promise.tryFail("SUBACK数量不匹配，packetId=" + subAck.messageId() + "，expected=" + pending.topicCount
						+ "，actual=" + (grantedLevels == null ? 0 : grantedLevels.size()));
				return;
			}

			for (Integer grantedLevel : grantedLevels) {
				// 0、1、2 是合法授权 QoS；其他值表示拒绝或非法响应。
				if (grantedLevel == null || grantedLevel < 0 || grantedLevel > 2) {
					pending.promise
						.tryFail("Broker拒绝订阅，packetId=" + subAck.messageId() + "，reasonCodes=" + grantedLevels);
					return;
				}
			}
			pending.promise.tryComplete();
		});
	}

	/**
	 * 连接和订阅全部成功后的统一入口。
	 */
	private void onConnectionReady() {
		// 成功一次就结束当前连续失败周期，下次断线从第 1 次重连重新计数。
		reconnectAttempt = 0;
		cancelReconnectTimer();
		state.set(State.CONNECTED);
		readingPaused = false;
		mqttClient.resume();
		log.info("【Vertx-MQTT-Client】 => MQTT客户端就绪，客户端ID：{}，最大并发：{}，队列容量：{}", config.getClientId(), maxInFlightMessages,
				maxQueuedMessages);
	}

	/**
	 * 连接或订阅任一步失败后的统一处理：失效旧连接数据、主动断开，然后安排重连。
	 * @param cause 失败原因
	 */
	private void onConnectCycleFailed(Throwable cause) {
		if (!closeRequested) {
			state.set(State.DISCONNECTED);
			log.error("【Vertx-MQTT-Client】 => MQTT连接或订阅失败，主机：{}，端口：{}，客户端ID：{}，错误信息：{}", config.getHost(),
					config.getPort(), config.getClientId(), cause.getMessage(), cause);
		}

		// 先让旧 epoch、队列和待确认请求全部失效，再断开底层连接。
		invalidateConnection(cause);
		disconnectIntentionally().onComplete(ignored -> {
			if (!closeRequested) {
				scheduleReconnect();
			}
		});
	}

	/**
	 * 根据配置安排下一次重连。
	 *
	 * <p>
	 * 同一时间只允许一个重连定时器。{@code reconnectAttempts == 0} 表示禁用，负数通常表示无限重试，正数表示最大次数。
	 * </p>
	 */
	private void scheduleReconnect() {
		// 已关闭、处于终态或已经有重连定时器时，什么都不做。
		if (closeRequested || state.get().isTerminal() || reconnectTimerId != TIMER_INACTIVE) {
			return;
		}

		int maxAttempts = config.getReconnectAttempts();
		if (maxAttempts == 0) {
			state.set(State.DISCONNECTED);
			log.error("【Vertx-MQTT-Client】 => MQTT自动重连已禁用，客户端ID：{}", config.getClientId());
			return;
		}

		// 先递增再比较，因此日志中的 attempt 从 1 开始，符合人的阅读习惯。
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
			// 定时器只执行一次，进入回调后先标记为空闲，失败后才能安排下一次。
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

	/**
	 * 计算“指数退避 + 随机抖动”的重连等待时间。
	 *
	 * <p>
	 * 大致公式为 {@code base * 2^(attempt-1)}，再限制最大值并加入最多 20% 随机量。这样故障持续时重试会逐渐变慢，
	 * 同时多个实例不会完全同时重连。
	 * </p>
	 * @param attempt 当前是第几次连续重连，从 1 开始
	 * @return 延迟毫秒数
	 */
	private long calculateReconnectDelay(int attempt) {
		long base = config.getReconnectInterval();
		long cap = Math.max(base, MAX_RECONNECT_DELAY_MILLIS);
		int exponent = Math.clamp(attempt - 1, 0, MAX_BACKOFF_EXPONENT);
		long exponential;
		try {
			// multiplyExact 在 long 溢出时抛异常，比得到一个错误的负数更安全。
			exponential = Math.multiplyExact(base, 1L << exponent);
		}
		catch (ArithmeticException ignored) {
			exponential = cap;
		}
		long delayWithoutJitter = Math.min(exponential, cap);
		long jitterBound = Math.max(1L, delayWithoutJitter / (100L / JITTER_PERCENT));
		long availableAbove = cap - delayWithoutJitter;
		if (availableAbove > 0) {
			// 尚未达到上限时向上增加随机值。
			long upperJitter = Math.min(jitterBound, availableAbove);
			return delayWithoutJitter + ThreadLocalRandom.current().nextLong(upperJitter + 1L);
		}
		// 已达到上限时改为向下减随机值，确保最终结果不会超过 cap。
		return Math.max(base, delayWithoutJitter - ThreadLocalRandom.current().nextLong(jitterBound + 1L));
	}

	/**
	 * 任何线程都可以调用的重连入口。实际状态修改会被切回 {@link #boundContext}。
	 * @param cause 触发重连的异常或原因
	 */
	private void requestReconnect(Throwable cause) {
		runOnContext(() -> {
			State current = state.get();
			// 去重：关闭流程或已有重连流程进行中时，不再重复断开和创建定时器。
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

	/**
	 * 取消尚未触发的重连定时器，并立即把字段恢复为空闲值。
	 */
	private void cancelReconnectTimer() {
		long timerId = reconnectTimerId;
		reconnectTimerId = TIMER_INACTIVE;
		cancelTimer(timerId);
	}

	// -------------------------------------------------------------------------
	// 入站消息、背压与 ACK
	// -------------------------------------------------------------------------

	/*
	 * 入站消息有三层保护：
	 *
	 * 1. inFlightMessages：正在处理的数量不超过 maxInFlightMessages； 2. inboundQueue：并发用满后只允许有限数量排队；
	 * 3. mqttClient.pause()：达到上限后从网络源头暂停读取。
	 *
	 * 处理完成后按相反方向执行：减少 inFlight -> 从 queue 补充任务 -> 达到低水位后 resume。
	 */

	/**
	 * Vert.x MQTT 客户端收到 PUBLISH 时的第一入口。
	 *
	 * <p>
	 * 这里只负责切换 Context，不直接做业务，避免网络回调线程和状态机线程并发修改队列。
	 * </p>
	 * @param publishMessage 收到的 MQTT 消息
	 */
	private void onPublishMessage(MqttPublishMessage publishMessage) {
		runOnContext(() -> acceptInboundMessage(publishMessage));
	}

	/**
	 * 根据当前负载决定“立即处理、进入队列、丢弃 QoS 0 或重连重投”。
	 * @param publishMessage 收到的 MQTT 消息
	 */
	private void acceptInboundMessage(MqttPublishMessage publishMessage) {
		receivedMessages.incrementAndGet();
		State current = state.get();
		// 连接未就绪或正在关闭时不再接收新任务。
		if (!current.isOperational()) {
			// QoS 0 没有 ACK 和重投保证，离开当前回调后只能视为丢弃。
			if (publishMessage.qosLevel() == MqttQoS.AT_MOST_ONCE) {
				droppedMessages.incrementAndGet();
			}
			return;
		}

		// 消息绑定当前连接 epoch，防止断线后旧任务完成时误 ACK 新连接。
		InboundMessage inboundMessage = new InboundMessage(publishMessage, connectionEpoch.get());
		if (inFlightMessages.get() < maxInFlightMessages) {
			// 还有并发槽位，直接交给业务线程池。
			dispatchInboundMessage(inboundMessage);
			if (inFlightMessages.get() >= maxInFlightMessages) {
				// 本次调度恰好用满槽位，立即暂停底层读取，尽量减少继续涌入的消息。
				pauseReading();
			}
			return;
		}

		if (inboundQueue.size() < maxQueuedMessages) {
			// 并发已满但等待队列还有空间，先按 FIFO 顺序暂存。
			inboundQueue.addLast(inboundMessage);
			queuedMessages.incrementAndGet();
			pauseReading();
			return;
		}

		if (publishMessage.qosLevel() == MqttQoS.AT_MOST_ONCE) {
			// QoS 0 无法要求 Broker 重投，队列满时只能明确丢弃并记录指标。
			droppedMessages.incrementAndGet();
			log.warn("【Vertx-MQTT-Client】 => 入站队列已满，丢弃QoS0消息，主题：{}，客户端ID：{}", publishMessage.topicName(),
					config.getClientId());
		}
		else {
			/*
			 * QoS 1/2 绝不能在没有处理时 ACK。主动重连会让当前会话中的未确认消息由 Broker 再次投递，从而把“本机队列已满”转化为协议层重试。
			 */
			failedMessages.incrementAndGet();
			requestReconnect(
					new RejectedExecutionException("MQTT入站队列已满，QoS " + publishMessage.qosLevel().value() + " 消息保持未确认"));
		}
	}

	/**
	 * 占用一个 in-flight 槽位，并把业务处理提交给虚拟线程池。
	 * @param inboundMessage 带连接 epoch 的入站消息
	 */
	private void dispatchInboundMessage(InboundMessage inboundMessage) {
		// 排队期间如果连接已变化，该消息已经属于旧连接，不能继续处理或 ACK。
		if (inboundMessage.epoch != connectionEpoch.get()) {
			if (inboundMessage.message.qosLevel() == MqttQoS.AT_MOST_ONCE) {
				droppedMessages.incrementAndGet();
			}
			return;
		}

		// 必须在 execute 前增加计数；提交失败时再回滚，保证统计和真实任务数量一致。
		inFlightMessages.incrementAndGet();
		try {
			messageExecutor.execute(() -> processInboundMessage(inboundMessage));
		}
		catch (RejectedExecutionException ex) {
			// 常见于关闭过程中线程池已经 shutdown。
			inFlightMessages.decrementAndGet();
			failedMessages.incrementAndGet();
			if (inboundMessage.message.qosLevel() != MqttQoS.AT_MOST_ONCE) {
				requestReconnect(ex);
			}
		}
	}

	/**
	 * 在虚拟线程中查找并调用所有匹配的业务处理器。
	 *
	 * <p>
	 * {@link MessageHandler#handle(MqttPublishMessage)} 返回 Future，所以这里不能在调用 handle 后立即
	 * ACK。 {@link Future#join(List)} 会等待所有匹配处理器结束：全部成功才算成功，任意一个失败都会把失败原因交给统一完成逻辑。
	 * </p>
	 *
	 * <p>
	 * 处理器实现特别注意：如果要把消息转发到 Pulsar，应返回由 {@code sendAsync()} 转换得到的 Future；不能先返回
	 * {@code Future.succeededFuture()} 再在后台发送，否则 MQTT 会过早 ACK。
	 * </p>
	 * @param inboundMessage 带连接 epoch 的入站消息
	 */
	private void processInboundMessage(InboundMessage inboundMessage) {
		// 同一个主题理论上通常只匹配一个处理器，但这里支持多个处理器共同处理。
		List<Future<Void>> handlerFutures = new ArrayList<>();
		boolean handled = false;
		try {
			for (MessageHandler messageHandler : messageHandlers) {
				if (messageHandler.supports(inboundMessage.message.topicName())) {
					handled = true;
					// 接口约定 Future 不能为 null；返回的 Future 代表该处理器的真实完成时间。
					handlerFutures.add(messageHandler.handle(inboundMessage.message));
				}
			}
		}
		catch (Throwable throwable) {
			// supports 或 handle 同步抛出的异常也转换为失败 Future，统一走后面的失败流程。
			handlerFutures.add(Future.failedFuture(throwable));
		}

		if (!handled) {
			log.warn("【Vertx-MQTT-Client】 => MQTT消息没有匹配的处理器，主题：{}，客户端ID：{}", inboundMessage.message.topicName(),
					config.getClientId());
		}

		// join 与 all 的区别：join 会等待所有 Future 都结束后再完成，便于安全地统一收尾。
		Future.join(handlerFutures)
			.mapEmpty()
			// Future 可能在任意线程完成，ACK、计数和队列操作必须切回 boundContext。
			.onComplete(result -> runOnContext(
					() -> completeInboundMessage(inboundMessage, result.succeeded() ? null : result.cause())));
	}

	/**
	 * 入站消息的唯一完成出口：释放并发槽位、按条件 ACK、处理失败，然后继续排空队列。
	 * @param inboundMessage 已处理的消息
	 * @param failure null 表示所有处理器成功；非 null 表示处理失败
	 */
	private void completeInboundMessage(InboundMessage inboundMessage, Throwable failure) {
		// 无论成功失败都先释放槽位，后面 drainInboundQueue 才能调度下一条。
		inFlightMessages.decrementAndGet();
		boolean sameConnection = inboundMessage.epoch == connectionEpoch.get();

		// 只有业务成功、连接未变化且底层仍连接时，才允许确认消息。
		if (failure == null && sameConnection && mqttClient.isConnected()) {
			try {
				// QoS 0 没有需要发送的 ACK；QoS 1/2 使用手动 ACK。
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
				// 保持 QoS 1/2 未确认并重连，Broker 会在持久会话中重投，所以处理器必须支持幂等。
				requestReconnect(failure);
			}
		}
		else if (!sameConnection && inboundMessage.message.qosLevel() == MqttQoS.AT_MOST_ONCE) {
			// QoS 0 在连接切换后没有重投机会，只能计入丢弃。
			droppedMessages.incrementAndGet();
		}

		// 先用刚释放的槽位处理内存队列，再判断是否可以恢复网络读取。
		drainInboundQueue();
		resumeReadingIfPossible();
	}

	/**
	 * 按 FIFO 顺序把等待队列中的消息补充到空闲并发槽位。
	 */
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

	/**
	 * 暂停 MQTT 网络读取，形成从业务处理端向 Broker 方向传播的背压。
	 */
	private void pauseReading() {
		// pause 是幂等保护；未连接时调用也没有意义。
		if (readingPaused || !mqttClient.isConnected()) {
			return;
		}

		readingPaused = true;
		// 只有 CONNECTED 才切换为 PAUSED；关闭或重连状态不会被覆盖。
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

	/**
	 * 当队列已经清空并且处理量降到低水位后，恢复 MQTT 网络读取。
	 */
	private void resumeReadingIfPossible() {
		/*
		 * 必须同时满足： 1. 当前确实处于 pause； 2. 没有关闭请求； 3. 内存队列已经清空； 4. in-flight 不高于低水位线。
		 */
		if (!readingPaused || closeRequested || !inboundQueue.isEmpty() || inFlightMessages.get() > resumeThreshold) {
			return;
		}

		// compareAndSet 防止把其他生命周期状态错误覆盖成 CONNECTED。
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

	/**
	 * 清空尚未开始处理的入站队列。
	 *
	 * <p>
	 * QoS 1/2 因为没有 ACK，连接恢复后可以由 Broker 重投；QoS 0 无法重投，所以需要记录为 dropped。
	 * </p>
	 */
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

	/*
	 * 出站 QoS 对应的确认流程：
	 *
	 * QoS 0：PUBLISH 写出后结束，没有 Broker 确认包； QoS 1：PUBLISH -> PUBACK； QoS 2：PUBLISH -> PUBREC
	 * -> PUBREL -> PUBCOMP。
	 *
	 * Vert.x 负责协议握手，本类用 pendingPublishes 保存业务调用方正在等待的 Future。
	 */

	/**
	 * 登记一个等待 Broker 发布确认的 Future。
	 * @param packetId PUBLISH 的协议包 ID
	 * @param epoch 发送消息时的连接代数
	 * @return 收到正确发布确认后成功的 Future
	 */
	private Future<Void> awaitPublishCompletion(int packetId, long epoch) {
		Promise<Void> promise = Promise.promise();
		PendingPublish previous = pendingPublishes.put(packetId, new PendingPublish(promise, epoch));
		if (previous == null) {
			pendingPublishCount.incrementAndGet();
		}
		else {
			// 同一连接中 packetId 不应覆盖尚未完成的发送；出现时先明确失败旧调用。
			previous.promise.tryFail("重复的PUBLISH packetId：" + packetId);
		}
		return promise.future();
	}

	/**
	 * Vert.x 通知某个 QoS 1/2 发布握手已经完整结束。
	 * @param packetId 已完成的 PUBLISH 协议包 ID
	 */
	private void onPublishCompleted(int packetId) {
		runOnContext(() -> {
			PendingPublish pending = pendingPublishes.remove(packetId);
			if (pending == null) {
				// 可能已经因超时、断线或错误原因被移除，无需重复完成。
				return;
			}
			pendingPublishCount.decrementAndGet();
			if (pending.epoch == connectionEpoch.get()) {
				pending.promise.tryComplete();
			}
			else {
				// packetId 可能在新连接中复用，epoch 不一致时不能把旧确认当成新发送成功。
				pending.promise.tryFail("MQTT连接已变化，忽略旧连接的发布确认，packetId=" + packetId);
			}
		});
	}

	/**
	 * 检查 QoS 1 PUBACK 中的 reasonCode。正常成功最终由 {@link #onPublishCompleted(int)} 完成。
	 * @param message PUBACK
	 */
	private void onPublishAck(MqttPubAckMessage message) {
		if (message.code().isError()) {
			failPendingPublish(message.messageId(), new IllegalStateException(
					"Broker拒绝QoS1消息，packetId=" + message.messageId() + "，reasonCode=" + message.code()));
		}
	}

	/**
	 * 检查 QoS 2 PUBCOMP 中的 reasonCode。正常成功最终由 {@link #onPublishCompleted(int)} 完成。
	 * @param message PUBCOMP
	 */
	private void onPublishComplete(MqttPubCompMessage message) {
		if (message.code().isError()) {
			failPendingPublish(message.messageId(), new IllegalStateException(
					"Broker拒绝QoS2消息，packetId=" + message.messageId() + "，reasonCode=" + message.code()));
		}
	}

	/**
	 * Vert.x 在配置的 ACK 超时到达后回调这里。
	 *
	 * <p>
	 * 超时意味着无法确定 Broker 是否成功处理，先让发送 Future 失败，再重连以恢复一个确定的连接状态。
	 * </p>
	 * @param packetId 超时的 PUBLISH 协议包 ID
	 */
	private void onPublishCompletionExpired(int packetId) {
		TimeoutException timeout = new TimeoutException("等待PUBACK/PUBCOMP超时，packetId=" + packetId);
		failPendingPublish(packetId, timeout);
		requestReconnect(timeout);
	}

	/**
	 * 让指定 packetId 的出站发布失败，并从等待集合移除。
	 * @param packetId PUBLISH 协议包 ID
	 * @param cause 失败原因
	 */
	private void failPendingPublish(int packetId, Throwable cause) {
		runOnContext(() -> {
			PendingPublish pending = pendingPublishes.remove(packetId);
			if (pending != null) {
				pendingPublishCount.decrementAndGet();
				pending.promise.tryFail(cause);
			}
		});
	}

	/**
	 * 连接失效或关闭时，批量结束所有仍在等待确认的出站 Future。
	 * @param cause 统一失败原因
	 */
	private void failPendingPublishes(Throwable cause) {
		// 先复制再清空，避免完成 Promise 的回调间接修改原 Map。
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

	/**
	 * 创建 Vert.x MQTT 客户端，并集中注册所有协议和网络事件回调。
	 *
	 * <p>
	 * 这里只“接线”，不会连接 Broker。以后排查某个 MQTT 事件从哪里进入本类，可以先从这个方法查找对应 handler。
	 * </p>
	 * @return 已注册事件处理器的 MQTT 客户端
	 */
	private MqttClient createClient() {
		return MqttClient.create(vertx, options)
			// 网络异常、解码异常等统一进入重连入口。
			.exceptionHandler(this::onClientException)
			// MQTT 5 Broker 主动发出的 DISCONNECT。
			.disconnectMessageHandler(this::onBrokerDisconnect)
			// TCP 连接真正关闭。
			.closeHandler(this::onConnectionClosed)
			// 收到入站 PUBLISH。
			.publishHandler(this::onPublishMessage)
			// 收到订阅确认 SUBACK。
			.subscribeCompletionHandler(this::onSubAck)
			// 收到 QoS 1 的 PUBACK，可读取 reasonCode。
			.publishAckMessageHandler(this::onPublishAck)
			// 收到 QoS 2 最后的 PUBCOMP，可读取 reasonCode。
			.publishCompMessageHandler(this::onPublishComplete)
			// Vert.x 认为整个 QoS 发布握手成功完成。
			.publishCompletionHandler(this::onPublishCompleted)
			// 发布握手超过 ackTimeout。
			.publishCompletionExpirationHandler(this::onPublishCompletionExpired)
			.publishCompletionUnknownPacketIdHandler(packetId -> log
				.warn("【Vertx-MQTT-Client】 => 收到未知packetId的发布确认，packetId：{}，客户端ID：{}", packetId, config.getClientId()))
			.unsubscribeCompletionHandler(packetId -> log
				.debug("【Vertx-MQTT-Client】 => 收到UNSUBACK，packetId：{}，客户端ID：{}", packetId, config.getClientId()))
			.pingResponseHandler(
					ignored -> log.trace("【Vertx-MQTT-Client】 => 收到PINGRESP，客户端ID：{}", config.getClientId()));
	}

	/**
	 * 处理底层网络或 MQTT 协议异常。
	 * @param throwable 异常原因
	 */
	private void onClientException(Throwable throwable) {
		log.error("【Vertx-MQTT-Client】 => MQTT网络或协议异常，当前状态：{}，客户端ID：{}", state.get(), config.getClientId(), throwable);
		requestReconnect(throwable);
	}

	/**
	 * 处理 Broker 主动发送的 DISCONNECT。
	 * @param message Broker 的断开通知
	 */
	private void onBrokerDisconnect(MqttDisconnectMessage message) {
		log.warn("【Vertx-MQTT-Client】 => Broker主动断开连接，原因码：{}，客户端ID：{}", message.code(), config.getClientId());
		requestReconnect(new IllegalStateException("Broker主动断开连接，reasonCode=" + message.code()));
	}

	/**
	 * 处理底层连接关闭事件。
	 *
	 * <p>
	 * 主动 disconnect 也会触发 closeHandler，因此必须通过 {@link #intentionalDisconnect}
	 * 区分，否则正常关闭会被误判为故障并重连。
	 * </p>
	 * @param ignored Vert.x closeHandler 的占位参数
	 */
	private void onConnectionClosed(Void ignored) {
		runOnContext(() -> {
			if (intentionalDisconnect || closeRequested || state.get().isTerminal()) {
				log.debug("【Vertx-MQTT-Client】 => MQTT连接正常关闭，客户端ID：{}", config.getClientId());
				return;
			}
			requestReconnect(new IllegalStateException("MQTT底层连接已关闭"));
		});
	}

	/**
	 * 主动并安全地断开 MQTT 连接。
	 *
	 * <p>
	 * 即使 disconnect 调用失败，也把它恢复为成功，让后续重连或关闭步骤继续执行；旧连接状态会由
	 * {@link #invalidateConnection(Throwable)} 清理。
	 * </p>
	 * @return 断开步骤的 Future
	 */
	private Future<Void> disconnectIntentionally() {
		if (!mqttClient.isConnected()) {
			return Future.succeededFuture();
		}

		// 标记期间 closeHandler 不应触发重连。
		intentionalDisconnect = true;
		return mqttClient.disconnect().recover(throwable -> {
			log.warn("【Vertx-MQTT-Client】 => MQTT断开连接失败，客户端ID：{}", config.getClientId(), throwable);
			return Future.succeededFuture();
		}).eventually(() -> {
			intentionalDisconnect = false;
			return Future.succeededFuture();
		});
	}

	/**
	 * 宣告当前连接以及所有依附于它的临时状态失效。
	 * @param cause 失效原因，用于结束等待中的 Future
	 */
	private void invalidateConnection(Throwable cause) {
		// 正在虚拟线程中执行的旧消息无法强制取消；递增 epoch 后，它们完成时不会再 ACK。
		connectionEpoch.incrementAndGet();
		readingPaused = false;
		clearInboundQueue();
		failPendingSubscriptions(cause);
		failPendingPublishes(cause);
	}

	/**
	 * 批量结束所有等待 SUBACK 的订阅请求，并取消各自超时定时器。
	 * @param cause 统一失败原因
	 */
	private void failPendingSubscriptions(Throwable cause) {
		List<PendingSubscription> pending = new ArrayList<>(pendingSubscriptions.values());
		pendingSubscriptions.clear();
		for (PendingSubscription item : pending) {
			// 先取消定时器，再失败 Promise，防止稍后又收到一次超时回调。
			cancelTimer(item.timerId);
			item.promise.tryFail(cause);
		}
	}

	/**
	 * 在绑定 Context 中启动完整的优雅关闭流程。
	 *
	 * <p>
	 * 顺序不能随意调整：
	 * </p>
	 *
	 * <ol>
	 * <li>标记 CLOSING 并阻止新消息、新连接和新重连；</li>
	 * <li>暂停网络读取，等待正在处理、正在排队和正在等待发布确认的任务排空；</li>
	 * <li>主动断开 MQTT 连接；</li>
	 * <li>关闭业务虚拟线程池；</li>
	 * <li>清理残留状态并进入 CLOSED 终态。</li>
	 * </ol>
	 * @return 所有关闭步骤完成后的共享 Future
	 */
	private Future<Void> closeOnContext() {
		// close 是幂等的；重复调用方共享第一次创建的关闭结果。
		if (closePromise != null) {
			return closePromise.future();
		}

		closePromise = Promise.promise();
		closeRequested = true;
		state.set(State.CLOSING);
		// 从这一刻起不再允许任何定时重连。
		cancelReconnectTimer();
		// 先阻止新消息进入，再处理已经接收的消息。
		pauseForShutdown();
		// 关闭期间不再需要等待新订阅确认。
		failPendingSubscriptions(new IllegalStateException("MQTT客户端正在关闭"));

		// 如果 connect/subscribe 正在进行，先等它结束；连接失败也被 recover，因为关闭流程仍需继续。
		Future<Void> waitForConnection = connectionPromise == null ? Future.succeededFuture()
				: connectionPromise.future().recover(ignored -> Future.succeededFuture());

		// compose 严格保证前一步结束后才执行下一步。
		waitForConnection.compose(ignored -> waitForDrain())
			.compose(ignored -> disconnectIntentionally())
			.compose(ignored -> shutdownExecutor())
			.onComplete(result -> {
				// 无论关闭链成功或失败，最后都让旧连接数据失效并进入不可复用的 CLOSED。
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

	/**
	 * 关闭开始时暂停网络读取，并把内存队列中已有消息尽量提交到执行器。
	 */
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
		// pause 只阻止新网络数据，已在内存队列中的任务仍应尽量正常完成。
		drainInboundQueue();
	}

	/**
	 * 创建一个 Future，等待入站处理、等待队列和出站确认全部排空。
	 * @return 排空完成或排空超时后的 Future
	 */
	private Future<Void> waitForDrain() {
		Promise<Void> promise = Promise.promise();
		long deadline = System.currentTimeMillis() + SHUTDOWN_DRAIN_TIMEOUT_MILLIS;
		checkDrain(promise, deadline);
		return promise.future();
	}

	/**
	 * 非阻塞地轮询排空状态。
	 *
	 * <p>
	 * 这里使用 Vert.x timer，而不是 {@link Thread#sleep(long)}，因为 sleep 会阻塞 Event
	 * Loop，导致消息完成回调反而无法执行。
	 * </p>
	 * @param promise 排空完成信号
	 * @param deadline 最晚等待到的时间戳
	 */
	private void checkDrain(Promise<Void> promise, long deadline) {
		// 每次检查都尝试把队列中的消息填入刚释放的并发槽位。
		drainInboundQueue();
		if (inFlightMessages.get() == 0 && inboundQueue.isEmpty() && pendingPublishes.isEmpty()) {
			promise.tryComplete();
			return;
		}

		if (System.currentTimeMillis() >= deadline) {
			// 超时后不再无限等待：清队列、结束发送 Future，然后继续执行后续 disconnect。
			log.warn("【Vertx-MQTT-Client】 => 优雅排空超时，inFlight：{}，queued：{}，pendingPublish：{}，客户端ID：{}",
					inFlightMessages.get(), queuedMessages.get(), pendingPublishes.size(), config.getClientId());
			clearInboundQueue();
			failPendingPublishes(new TimeoutException("MQTT客户端关闭排空超时"));
			promise.tryComplete();
			return;
		}

		vertx.setTimer(SHUTDOWN_DRAIN_CHECK_INTERVAL_MILLIS, ignored -> checkDrain(promise, deadline));
	}

	/**
	 * 关闭消息虚拟线程池，同时避免阻塞 Vert.x Context。
	 *
	 * <p>
	 * {@link ExecutorService#awaitTermination(long, TimeUnit)}
	 * 是阻塞方法，所以额外启动一个虚拟线程等待；等待结束后再切回 Context 完成 Promise。
	 * </p>
	 * @return 执行器关闭结果
	 */
	private Future<Void> shutdownExecutor() {
		Promise<Void> promise = Promise.promise();
		// shutdown 不再接受新任务，但允许已提交任务继续执行。
		messageExecutor.shutdown();
		Thread.ofVirtual().name("mqtt-client-shutdown").start(() -> {
			try {
				if (!messageExecutor.awaitTermination(EXECUTOR_SHUTDOWN_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)) {
					// 超过等待时间后尝试中断仍未结束的任务。
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

	/**
	 * 获取当前租户要订阅的所有 MQTT 主题，并在连接前校验主题和 QoS。
	 */
	private Map<String, Integer> getTopics() {
		return MqttMessageType.getTopics(systemSettingsProperties.getTenantCode());
	}

	/**
	 * 在绑定 Context 中执行一个“返回 Future 的操作”，并把结果返回给调用方。
	 *
	 * <p>
	 * 与 {@link #runOnContext(Runnable)} 的区别：本方法会把 action 返回的成功值或异常继续传给调用方，适合
	 * open、close、publish 等公共异步 API。
	 * </p>
	 * @param action 要执行的异步操作
	 * @param <T> Future 成功值类型
	 * @return action 的异步结果
	 */
	private <T> Future<T> executeOnContext(@NonNull Supplier<Future<T>> action) {
		// 已经在目标 Context 时直接执行，避免无意义地再排队一次。
		if (Vertx.currentContext() == boundContext) {
			try {
				return action.get();
			}
			catch (Throwable throwable) {
				// 把同步异常转换成失败 Future，保持公共 API 始终以异步方式报告错误。
				return Future.failedFuture(throwable);
			}
		}

		// 不在目标 Context 时创建桥接 Promise，等 action 的 Future 完成后把结果转交出去。
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

	/**
	 * 在绑定 Context 中执行一个没有返回值的状态操作。
	 *
	 * <p>
	 * 适合网络事件、业务 Future 回调等“只需要安全修改本类状态”的场景。异常会记录日志，不会抛回网络回调线程。
	 * </p>
	 * @param action 要执行的操作
	 */
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

	/**
	 * 安全取消 Vert.x 定时器。传入 {@link #TIMER_INACTIVE} 时什么都不做。
	 * @param timerId Vert.x timerId
	 */
	private void cancelTimer(long timerId) {
		if (timerId != TIMER_INACTIVE) {
			vertx.cancelTimer(timerId);
		}
	}

	/**
	 * 一次等待 SUBACK 的订阅请求。
	 *
	 * <p>
	 * 这里不用 record，是因为 {@link #timerId} 要在创建定时器后再赋值。
	 * </p>
	 */
	private static final class PendingSubscription {

		/** SUBACK 成功、拒绝或超时时要完成的 Promise。 */
		private final Promise<Void> promise;

		/** SUBSCRIBE 中的主题数量，用来校验 SUBACK 返回项。 */
		private final int topicCount;

		/** 对应的 SUBACK 超时定时器。 */
		private long timerId = TIMER_INACTIVE;

		/**
		 * 创建待确认订阅。
		 * @param promise 等待结果
		 * @param topicCount 主题数量
		 */
		private PendingSubscription(Promise<Void> promise, int topicCount) {
			this.promise = promise;
			this.topicCount = topicCount;
		}

	}

	/**
	 * 一次等待 PUBACK/PUBCOMP 的出站发布。
	 *
	 * @param promise 发布调用方正在等待的结果
	 * @param epoch 发送时的连接代数
	 */
	private record PendingPublish(Promise<Void> promise, long epoch) {
	}

	/**
	 * 一条等待或正在处理的入站消息。
	 *
	 * @param message Vert.x MQTT 消息对象
	 * @param epoch 收到消息时的连接代数
	 */
	private record InboundMessage(MqttPublishMessage message, long epoch) {
	}

}

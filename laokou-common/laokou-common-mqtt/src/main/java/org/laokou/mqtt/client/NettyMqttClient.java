package org.laokou.mqtt.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.IoHandlerFactory;
import io.netty.channel.MultiThreadIoEventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.uring.IoUring;
import io.netty.channel.uring.IoUringIoHandler;
import io.netty.channel.uring.IoUringServerSocketChannel;
import io.netty.handler.codec.mqtt.MqttConnectMessage;
import io.netty.handler.codec.mqtt.MqttConnectPayload;
import io.netty.handler.codec.mqtt.MqttConnectVariableHeader;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttProperties;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.netty.handler.codec.mqtt.MqttVersion;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.util.UUIDGenerator;
import org.laokou.common.i18n.util.SystemUtils;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author laokou
 */
@Slf4j
final class NettyMqttClient implements MqttClient {

	private final String tenantCode;

	private final String clientId;

	private final InflightStore inflightStore;

	private final MqttClientProperties mqttClientProperties;

	private final ExecutorService virtualThreadExecutor;

	private final List<MqttEventListener> eventListeners = new CopyOnWriteArrayList<>();

	/** SUBACK / UNSUBACK 等待表 */
	private final Map<Integer, CompletableFuture<Integer>> pendingSubAcks = new ConcurrentHashMap<>();

	private final Map<Integer, CompletableFuture<Void>> pendingUnsubAcks = new ConcurrentHashMap<>();

	private volatile CompletableFuture<Void> connectFuture;

	private volatile Channel channel;

	private volatile EventLoopGroup worker;

	NettyMqttClient(String tenantCode, InflightStore inflightStore, MqttClientProperties mqttClientProperties,
	                ExecutorService virtualThreadExecutor) {
		this.tenantCode = tenantCode;
		this.clientId = String.format("%s-%s", tenantCode, UUIDGenerator.generateUUID().substring(0, 16));
		this.inflightStore = inflightStore;
		this.mqttClientProperties = mqttClientProperties;
		this.virtualThreadExecutor = virtualThreadExecutor;
	}

	@Override
	public CompletableFuture<Void> connect() {
		return null;
	}

	@Override
	public CompletableFuture<Void> disconnect() {
		return null;
	}

	@Override
	public CompletableFuture<Void> publish(String topic, byte[] payload, MqttQoS qos, boolean retained) {
		return null;
	}

	@Override
	public CompletableFuture<Integer> subscribe(String topicFilter, MqttQoS qos, MqttMessageListener listener) {
		return null;
	}

	private MqttConnectMessage buildConnectMessage() {
		MqttFixedHeader fixedHeader = new MqttFixedHeader(MqttMessageType.CONNECT, false, MqttQoS.AT_MOST_ONCE, false,
				0);
		MqttConnectVariableHeader variableHeader = new MqttConnectVariableHeader(MqttVersion.MQTT_5.protocolName(),
				MqttVersion.MQTT_5.protocolLevel(), true, true, false, 0, false, mqttClientProperties.isCleanSession(),
				mqttClientProperties.getKeepAliveTimeSeconds(), MqttProperties.NO_PROPERTIES);
		MqttConnectPayload payload = new MqttConnectPayload(clientId, null, null, mqttClientProperties.getUsername(),
				mqttClientProperties.getPassword().getBytes(StandardCharsets.UTF_8));
		return new MqttConnectMessage(fixedHeader, variableHeader, payload);
	}

	private void doConnect() {
		worker = new MultiThreadIoEventLoopGroup(mqttClientProperties.getWorkerCorePoolSize(), virtualThreadExecutor,
				getIoHandlerFactory());
		Bootstrap bootstrap = new Bootstrap().group(worker)
			.channel(getServerChannel())
			.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, mqttClientProperties.getConnectTimeoutMs())
			.option(ChannelOption.TCP_NODELAY, true)
			.option(ChannelOption.SO_KEEPALIVE, true)
			.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) {
					ch.pipeline()
						.addLast("mqttEncoder", MqttEncoder.INSTANCE)
						.addLast("mqttDecoder", new MqttDecoder(mqttClientProperties.getMaxBytesInMessage()))
						.addLast("idleStateHandler",
								new IdleStateHandler(mqttClientProperties.getKeepAliveTimeSeconds(), 0, 0,
										TimeUnit.SECONDS))
						.addLast("mqttHandler", new MqttClientHandler(NettyMqttClient.this));
				}
			});
		log.info("正在连接 MQTT Broker => {}:{} 客户端ID => {}", mqttClientProperties.getHost(),
				mqttClientProperties.getPort(), clientId);
		bootstrap.connect(mqttClientProperties.getHost(), mqttClientProperties.getPort()).addListener(future -> {
			if (!future.isSuccess()) {
				log.error("TCP 连接失败，错误信息：{}", future.cause().getMessage(), future.cause());
				onConnectFailure(future.cause());
				return;
			}
			if (future instanceof ChannelFuture channelFuture) {
				Channel ch = channelFuture.channel();
				ch.writeAndFlush(buildConnectMessage()).addListener(w -> {
					if (!w.isSuccess()) {
						ch.close();
						onConnectFailure(w.cause());
					}
				});
			}
		});
	}

	private void onConnectFailure(Throwable cause) {
		CompletableFuture<Void> f = connectFuture;
		if (f != null && !f.isDone()) {
			// 首次连接失败不直接失败 future，交给重连机制；显式 connect() 的调用方通过事件感知
			log.error("连接失败，进入重连流程，错误信息：{}", cause.getMessage());
		}
		// handleDisconnect(cause);
	}


	public void onPubAck(int packetId) {
		OutboundMessage msg = inflightStore.remove(packetId);
		if (msg != null) {
			cancelTimeout(msg);
			msg.future().complete(null);
		}
	}

	public void onPubRec(int packetId) {
		OutboundMessage msg = inflightStore.get(packetId);
		if (msg != null && msg.state() == OutboundMessage.State.WAIT_PUBREC) {
			msg.state = OutboundMessage.State.WAIT_PUBCOMP;
		}
	}

	public void onPubComp(int packetId) {
		OutboundMessage msg = inflightStore.remove(packetId);
		if (msg != null) {
			cancelTimeout(msg);
			msg.future().complete(null);
		}
	}

	private IoHandlerFactory getIoHandlerFactory() {
		if (isIoUringAvailable()) {
			log.info("Use IoUringIoHandler factory");
			return IoUringIoHandler.newFactory();
		}
		log.info("Use NioIoHandler factory");
		return NioIoHandler.newFactory();
	}

	private Class<? extends ServerChannel> getServerChannel() {
		if (isIoUringAvailable()) {
			log.info("Use IoUringServerSocketChannel");
			return IoUringServerSocketChannel.class;
		}
		log.info("Use NioServerSocketChannel");
		return NioServerSocketChannel.class;
	}

	private boolean isIoUringAvailable() {
		if (SystemUtils.isLinux()) {
			return IoUring.isAvailable();
		}
		return false;
	}

}

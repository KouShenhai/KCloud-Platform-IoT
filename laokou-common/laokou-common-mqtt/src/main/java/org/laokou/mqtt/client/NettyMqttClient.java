package org.laokou.mqtt.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.uring.IoUring;
import io.netty.channel.uring.IoUringIoHandler;
import io.netty.channel.uring.IoUringServerSocketChannel;
import io.netty.handler.codec.mqtt.*;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.util.UUIDGenerator;
import org.laokou.common.i18n.util.SystemUtils;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author laokou
 */
@Slf4j
final class NettyMqttClient implements MqttClient {

	private final String tenantCode;

	private final String clientId;

	private final MqttClientProperties mqttClientProperties;

	private final ExecutorService virtualThreadExecutor;

	private volatile CompletableFuture<Void> connectFuture;

	private volatile Channel channel;

	private volatile EventLoopGroup worker;

	NettyMqttClient(String tenantCode, MqttClientProperties mqttClientProperties,
			ExecutorService virtualThreadExecutor) {
		this.tenantCode = tenantCode;
		this.clientId = String.format("%s-%s", tenantCode, UUIDGenerator.generateUUID().substring(0, 16));
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

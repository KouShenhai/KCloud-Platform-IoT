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

package org.laokou.mqtt.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.channel.uring.IoUring;
import io.netty.channel.uring.IoUringSocketChannel;
import io.netty.handler.codec.mqtt.MqttConnectMessage;
import io.netty.handler.codec.mqtt.MqttConnectPayload;
import io.netty.handler.codec.mqtt.MqttConnectVariableHeader;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageIdVariableHeader;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttProperties;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttPublishVariableHeader;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.netty.handler.codec.mqtt.MqttVersion;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.HashedWheelTimer;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.Timeout;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.util.VirtualThreadFactory;
import org.laokou.common.i18n.common.exception.BizException;
import org.laokou.common.i18n.util.SystemUtils;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * @author loakou
 */
@Slf4j
@Data
final class NettyMqttClient implements MqttClient {

	private volatile Channel channel;

	private volatile Timeout reconnectTask;

	private final InflightStore inflightStore;

	private final AtomicReference<ClientState> state;

	private final AtomicInteger reconnectAttempts;

	private final MqttClientProperties properties;

	private final MqttEventLoop mqttEventLoop;

	private final HashedWheelTimer timer;

	private final PacketIdGenerator packetIdGenerator;

	NettyMqttClient(MqttClientProperties properties, MqttEventLoop mqttEventLoop) {
		this.state = new AtomicReference<>(ClientState.DISCONNECTED);
		this.reconnectAttempts = new AtomicInteger(0);
		this.inflightStore = new InMemoryInflightStore();
		this.timer = new HashedWheelTimer(VirtualThreadFactory.INSTANCE, 100, TimeUnit.MILLISECONDS);
		this.packetIdGenerator = new PacketIdGenerator();
		this.properties = properties;
		this.mqttEventLoop = mqttEventLoop;
	}

	@Override
	public void connect() {
		ClientState s = state.get();
		if (s == ClientState.CLOSED || s == ClientState.CONNECTED || s == ClientState.CONNECTING) {
			return;
		}
		if (state.compareAndSet(s, ClientState.CONNECTING)) {
			doConnect();
		}
	}

	@Override
	public void disconnect() {
		this.state.set(ClientState.CLOSED);
		cancelReconnectTask();
		clearInflight();
		if (this.channel != null) {
			this.channel.close();
		}
		this.timer.stop();
	}

	@Override
	public void publish(String topic, byte[] payload, MqttQoS qos) {
		if (state.get() != ClientState.CONNECTED || this.channel == null || !this.channel.isActive()) {
			throw new BizException("MQTT client is not connected");
		}
		if (qos == MqttQoS.AT_MOST_ONCE) {
			if (this.state.get() != ClientState.CONNECTED) {
				return;
			}
			writePublish(topic, payload, qos, 0, false, _ -> handleDisconnect());
		}
		else {
			int packetId = packetIdGenerator.nextId();
			OutboundMessage msg = new OutboundMessage(packetId, topic, payload, qos, false);
			// 写入缓存
			boolean isSet = inflightStore.putIfAbsent(msg);
			if (!isSet) {
				throw new BizException("Message is in inflight state");
			}
			writePublish(topic, msg.getPayload(), qos, packetId, false, _ -> handleDisconnect());
		}
	}

	void scheduleAckTimeout(OutboundMessage msg) {
		if (this.state.get() != ClientState.CONNECTED) {
			return;
		}
		msg.setTimeoutTask(
				timer.newTimeout(_ -> onAckTimeout(msg), properties.getAckTimeoutMs(), TimeUnit.MILLISECONDS));
	}

	void handleDisconnect() {
		if (state.get() == ClientState.CLOSED) {
			return;
		}
		ClientState prev = state.getAndSet(ClientState.RECONNECTING);
		if (prev == ClientState.RECONNECTING) {
			return;
		}
		if (prev == ClientState.DISCONNECTED) {
			// 已放弃重连（次数耗尽）后的迟到的 channelInactive，不再重新进入重连
			state.set(ClientState.DISCONNECTED);
			return;
		}
		clearInflight();
		scheduleReconnect();
	}

	void cancelAckTimeout(OutboundMessage msg) {
		Timeout timeout = msg.getTimeoutTask();
		if (timeout != null) {
			timeout.cancel();
		}
	}

	void writeAndFlush(Channel channel, MqttMessage message, Consumer<Throwable> onFailure) {
		if (channel == null || !channel.isActive()) {
			ReferenceCountUtil.release(message);
			return;
		}
		channel.writeAndFlush(message).addListener(f -> {
			if (!f.isSuccess()) {
				log.error("【Netty-MQTT-Client】 => 写入到 MQTT Broker 消息失败，错误信息：{}", f.cause().getMessage(), f.cause());
				onFailure.accept(f.cause());
			}
		});
	}

	private void doConnect() {
		cancelReconnectTask();
		Bootstrap bootstrap = new Bootstrap().group(mqttEventLoop.getWorkerGroup())
			.channel(getChannel())
			.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, properties.getConnectTimeoutMs())
			.option(ChannelOption.TCP_NODELAY, true)
			.option(ChannelOption.SO_KEEPALIVE, true)
			.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) {
					ch.pipeline()
						.addLast("mqttEncoder", MqttEncoder.INSTANCE)
						.addLast("mqttDecoder", new MqttDecoder(properties.getMaxBytesInMessage()))
						.addLast("idleStateHandler",
								new IdleStateHandler(properties.getKeepAliveTimeSeconds(), 0, 0, TimeUnit.SECONDS))
						.addLast("nettyMqttHandler", new NettyMqttClientHandler(NettyMqttClient.this));
				}
			});
		log.info("【Netty-MQTT-Client】 => 开始连接 MQTT Broker");
		bootstrap.connect(properties.getHost(), properties.getPort()).addListener(f -> {
			if (!f.isSuccess()) {
				log.error("【Netty-MQTT-Client】 => 连接 MQTT Broker 失败，错误信息：{}", f.cause().getMessage(), f.cause());
				handleDisconnect();
				return;
			}
			if (f instanceof ChannelFuture future) {
				Channel ch = future.channel();
				ch.writeAndFlush(buildConnectMessage()).addListener(w -> {
					if (!w.isSuccess()) {
						log.error("【Netty-MQTT-Client】 => 发送到 MQTT Broker 连接消息失败，错误信息：{}", f.cause().getMessage(),
								f.cause());
						ch.close();
						handleDisconnect();
					}
				});
			}
		});
	}

	private void clearInflight() {
		for (int i = 1; i <= 0xFFFF; i++) {
			OutboundMessage msg = inflightStore.remove(i);
			cancelAckTimeout(msg);
		}
	}

	private void onAckTimeout(OutboundMessage msg) {
		if (this.state.get() != ClientState.CONNECTED || this.channel == null || !this.channel.isActive()) {
			return;
		}
		retransmit(msg);
		scheduleAckTimeout(msg);
	}

	/**
	 * QoS 1/2 消息等待确认超时后，按当前 QoS 阶段重发报文.
	 */
	private void retransmit(OutboundMessage msg) {
		if (this.state.get() != ClientState.CONNECTED || this.channel == null || !this.channel.isActive()) {
			return;
		}
		if (msg.getState() == State.WAIT_PUBCOMP) {
			writePubRel(msg.getPacketId());
			return;
		}
		writePublish(msg.getTopic(), msg.getPayload(), msg.getQos(), msg.getPacketId(), true, _ -> handleDisconnect());
	}

	private void writePubRel(int packetId) {
		if (this.state.get() != ClientState.CONNECTED || this.channel == null || !this.channel.isActive()) {
			return;
		}
		MqttMessage pubRel = new MqttMessage(
				new MqttFixedHeader(MqttMessageType.PUBREL, true, MqttQoS.AT_LEAST_ONCE, false, 0),
				MqttMessageIdVariableHeader.from(packetId));
		writeAndFlush(this.channel, pubRel, _ -> handleDisconnect());
	}

	private void scheduleReconnect() {
		int attempt = this.reconnectAttempts.incrementAndGet();
		int exponent = Math.min(attempt - 1, 6);
		long delayMs = Math.min(1000L << exponent, 60000L);
		this.reconnectTask = timer.newTimeout(_ -> {
			if (state.get() == ClientState.RECONNECTING
					&& state.compareAndSet(ClientState.RECONNECTING, ClientState.CONNECTING)) {
				log.info("【Netty-MQTT-Client】 => MQTT正在重连，请稍候~");
				doConnect();
			}
		}, delayMs, TimeUnit.MILLISECONDS);
	}

	private MqttConnectMessage buildConnectMessage() {
		MqttFixedHeader fixedHeader = new MqttFixedHeader(MqttMessageType.CONNECT, false, MqttQoS.AT_MOST_ONCE, false,
				0);
		MqttConnectVariableHeader variableHeader = new MqttConnectVariableHeader(MqttVersion.MQTT_5.protocolName(),
				MqttVersion.MQTT_5.protocolLevel(), true, true, false, 0, false, true,
				properties.getKeepAliveTimeSeconds(), MqttProperties.NO_PROPERTIES);
		MqttConnectPayload payload = new MqttConnectPayload(
				String.format("%s-%d", properties.getTenantCode(), System.currentTimeMillis()), null, null,
				properties.getUsername(), properties.getPassword().getBytes(StandardCharsets.UTF_8));
		return new MqttConnectMessage(fixedHeader, variableHeader, payload);
	}

	private void cancelReconnectTask() {
		if (this.reconnectTask != null) {
			this.reconnectTask.cancel();
			this.reconnectTask = null;
		}
	}

	private void writePublish(String topic, byte[] payload, MqttQoS qos, int packetId, boolean dup,
			Consumer<Throwable> onFailure) {
		if (this.state.get() != ClientState.CONNECTED || this.channel == null || !this.channel.isActive()) {
			return;
		}
		MqttPublishMessage msg = new MqttPublishMessage(
				new MqttFixedHeader(MqttMessageType.PUBLISH, dup, qos, false, 0),
				new MqttPublishVariableHeader(topic, packetId), Unpooled.wrappedBuffer(payload));
		writeAndFlush(this.channel, msg, onFailure);
	}

	private Class<? extends SocketChannel> getChannel() {
		if (isIoUringAvailable()) {
			log.info("Use IoUringSocketChannel");
			return IoUringSocketChannel.class;
		}
		log.info("Use NioSocketChannel");
		return NioSocketChannel.class;
	}

	private boolean isIoUringAvailable() {
		if (SystemUtils.isLinux()) {
			return IoUring.isAvailable();
		}
		return false;
	}

}

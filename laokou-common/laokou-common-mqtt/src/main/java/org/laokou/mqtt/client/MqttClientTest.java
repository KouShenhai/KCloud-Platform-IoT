package org.laokou.mqtt.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.MultiThreadIoEventLoopGroup;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.mqtt.MqttConnectMessage;
import io.netty.handler.codec.mqtt.MqttConnectPayload;
import io.netty.handler.codec.mqtt.MqttConnectVariableHeader;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttProperties;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttPublishVariableHeader;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.netty.handler.codec.mqtt.MqttVersion;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.HashedWheelTimer;
import org.laokou.common.core.util.ThreadUtils;
import org.laokou.common.core.util.VirtualThreadFactory;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class MqttClientTest {

	volatile Channel channel;

	final PacketIdGenerator packetIdGenerator = new PacketIdGenerator();

	final AtomicReferenceArray<OutboundMessage> slots = new AtomicReferenceArray<>(0xFFFF);

	final HashedWheelTimer timer = new HashedWheelTimer(VirtualThreadFactory.INSTANCE, 100, TimeUnit.MILLISECONDS);

	static void main() throws InterruptedException {
		new MqttClientTest().run();
	}

	private void run() throws InterruptedException {
		EventLoopGroup workerGroup = new MultiThreadIoEventLoopGroup(16, ThreadUtils.newVirtualTaskExecutor(),
				NioIoHandler.newFactory());
		Bootstrap bootstrap = new Bootstrap().group(workerGroup)
			.channel(NioSocketChannel.class)
			.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 15000)
			.option(ChannelOption.TCP_NODELAY, true)
			.option(ChannelOption.SO_KEEPALIVE, true)
			.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) {
					ch.pipeline()
						.addLast("mqttEncoder", MqttEncoder.INSTANCE)
						.addLast("mqttDecoder", new MqttDecoder(64 * 1024 * 1024))
						.addLast("idleStateHandler", new IdleStateHandler(60, 0, 0, TimeUnit.SECONDS))
						.addLast("mqttHandler", new MqttClientHandler(MqttClientTest.this));
				}
			});
		System.out.println("开始启动");
		bootstrap.connect("127.0.0.1", 41883).addListener(f -> {
			if (!f.isSuccess()) {
				System.out.println("TCP 连接失败，" + f.cause().getMessage());
				return;
			}
			if (f instanceof ChannelFuture future) {
				Channel ch = future.channel();
				ch.writeAndFlush(buildConnectMessage()).addListener(w -> {
					if (!w.isSuccess()) {
						ch.close();
					}
					else {
						System.out.println("启动成功");
					}
				});
			}
		});
		Thread.sleep(5000);
		publish("test/123", "2222".getBytes(StandardCharsets.UTF_8), MqttQoS.AT_MOST_ONCE);
		publish("test/456", "3333".getBytes(StandardCharsets.UTF_8), MqttQoS.AT_LEAST_ONCE);
		publish("test/456", "4444".getBytes(StandardCharsets.UTF_8), MqttQoS.AT_LEAST_ONCE);
		publish("test/456", "5555".getBytes(StandardCharsets.UTF_8), MqttQoS.AT_LEAST_ONCE);
		publish("test/456", "6666".getBytes(StandardCharsets.UTF_8), MqttQoS.AT_LEAST_ONCE);
		publish("test/456", "7777".getBytes(StandardCharsets.UTF_8), MqttQoS.AT_LEAST_ONCE);
		publish("test/456", "8888".getBytes(StandardCharsets.UTF_8), MqttQoS.AT_LEAST_ONCE);
		publish("test/456", "9999".getBytes(StandardCharsets.UTF_8), MqttQoS.AT_LEAST_ONCE);
		publish("test/456", "0000".getBytes(StandardCharsets.UTF_8), MqttQoS.AT_LEAST_ONCE);
		publish("test/456", "aaaa".getBytes(StandardCharsets.UTF_8), MqttQoS.AT_LEAST_ONCE);
		publish("test/456", "bbbb".getBytes(StandardCharsets.UTF_8), MqttQoS.AT_LEAST_ONCE);
		publish("test/456", "xxxx".getBytes(StandardCharsets.UTF_8), MqttQoS.AT_LEAST_ONCE);
		// publish("test/789", "4444".getBytes(StandardCharsets.UTF_8),
		// MqttQoS.EXACTLY_ONCE);
		Thread.sleep(15000);
	}

	private MqttConnectMessage buildConnectMessage() {
		MqttFixedHeader fixedHeader = new MqttFixedHeader(MqttMessageType.CONNECT, false, MqttQoS.AT_MOST_ONCE, false,
				0);
		MqttConnectVariableHeader variableHeader = new MqttConnectVariableHeader(MqttVersion.MQTT_5.protocolName(),
				MqttVersion.MQTT_5.protocolLevel(), true, true, false, 0, false, true, 30000,
				MqttProperties.NO_PROPERTIES);
		MqttConnectPayload payload = new MqttConnectPayload("test-mqtt-client", null, null, "root",
				"laokou123".getBytes(StandardCharsets.UTF_8));
		return new MqttConnectMessage(fixedHeader, variableHeader, payload);
	}

	/**
	 * 发布 MQTT 消息（完整控制版）
	 * @param topic 主题路径，如 "device/001/sensor"，不可含 +# 通配符
	 * @param payload 消息体，最大 256MB
	 * @param qos 服务质量 0/1/2，见 {@link MqttQoS}
	 *
	 */
	private void publish(String topic, byte[] payload, MqttQoS qos) {
		if (qos == MqttQoS.AT_MOST_ONCE) {
			// 发布消息
			writePublish(topic, payload, qos, 0);
		}
		else {
			int packetId = packetIdGenerator.nextId();
			OutboundMessage msg = new OutboundMessage(packetId, topic, payload, qos, false);
			// 写入缓存
			boolean b = slots.compareAndSet(packetId, null, msg);
			if (!b) {
				throw new IllegalStateException("消息处理不过来，请稍后再试");
			}
			// 发布消息
			writePublish(topic, payload, qos, packetId);
			// 开启定时任务【ACK超时 => 接收端未 PUBACK，重新发送消息】
			scheduleAckTimeout(msg);
		}
	}

	private void scheduleAckTimeout(OutboundMessage msg) {
		msg.setTimeoutTask(timer.newTimeout(_ -> onAckTimeout(msg), 5000, TimeUnit.MILLISECONDS));
	}

	private void onAckTimeout(OutboundMessage msg) {
		OutboundMessage current = slots.get(msg.getPacketId());
		if (current != msg) {
			return;
		}
		// isDup = true，重试
		msg.setDup(true);
		// 重新发布消息
		retransmit(msg);
		onAckTimeout(msg);
	}

	private void retransmit(OutboundMessage msg) {
		if (msg.getState() == OutboundMessage.State.WAIT_PUBCOMP) {
			// writePubRel(msg.packetId());
		}
		else {
			writePublish(msg.getTopic(), msg.getPayload(), msg.getQos(), msg.getPacketId());
		}
	}

	private void writePublish(String topic, byte[] payload, MqttQoS qos, int packetId) {
		if (this.channel == null || !this.channel.isActive()) {
			return;
		}
		// isDup 重发标志，QoS>0 时首次 false，重试 true
		MqttPublishMessage msg = new MqttPublishMessage(
				new MqttFixedHeader(MqttMessageType.PUBLISH, false, qos, false, 0),
				new MqttPublishVariableHeader(topic, packetId), Unpooled.wrappedBuffer(payload));
		this.channel.writeAndFlush(msg).addListener(f -> {
			if (f.isSuccess()) {
				System.out.println("发布消息成功");
			}
			else {
				System.out.println("发布消息失败");
			}
		});
	}

}

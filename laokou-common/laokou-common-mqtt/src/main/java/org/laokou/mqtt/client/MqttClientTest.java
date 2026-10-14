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
import org.laokou.common.core.util.ThreadUtils;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class MqttClientTest {

	volatile Channel channel;

	final PacketIdGenerator packetIdGenerator = new PacketIdGenerator();

	final Map<Integer, OutboundMessage> inflightMap = new ConcurrentHashMap<>();

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
		// publish("test/123", "1123".getBytes(StandardCharsets.UTF_8), MqttQoS.AT_MOST_ONCE, false);
		publish("test/456", "3333".getBytes(StandardCharsets.UTF_8), MqttQoS.AT_LEAST_ONCE, false);
		Thread.sleep(5000);
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
	 *
	 * @param topic     主题路径，如 "device/001/sensor"，不可含 +# 通配符
	 * @param payload   消息体，最大 256MB
	 * @param qos       服务质量 0/1/2，见 {@link MqttQoS}
	 * @param isRetain  保留标志，true 则新订阅者立即可收到最后一条
	 *
	 */
	private void publish(String topic, byte[] payload, MqttQoS qos, boolean isRetain) {
		int packetId = 0;
		if (qos != MqttQoS.AT_MOST_ONCE) {
			packetId = packetIdGenerator.nextId();
			inflightMap.putIfAbsent(packetId, new OutboundMessage(packetId, topic, payload, qos, isRetain));
		}
		// isDup 重发标志，QoS>0 时首次 false，重试 true
		publish(packetId, topic, payload, qos, false, isRetain);
		// 开启定时任务【ACK超时 => 接收端未 PUBACK，重新发送消息】
	}

	private void publish(int packetId, String topic, byte[] payload, MqttQoS qos, boolean isDup, boolean isRetain) {
		MqttPublishMessage msg = new MqttPublishMessage(
			new MqttFixedHeader(MqttMessageType.PUBLISH, isDup, qos, isRetain, 0),
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

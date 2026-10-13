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
import java.util.concurrent.TimeUnit;

public class MqttClientTest {

	volatile Channel channel;

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
		publish("test/123", "1123".getBytes(StandardCharsets.UTF_8), MqttQoS.AT_MOST_ONCE, false);
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

	private void publish(String topic, byte[] payload, MqttQoS qos, boolean retained) {
		if (qos == MqttQoS.AT_MOST_ONCE) {
			publishQos0(topic, payload, retained);
		}
		// publishQos12(topic, payload, qos, retained);
	}

	private void publishQos0(String topic, byte[] payload, boolean retained) {
		MqttPublishMessage msg = new MqttPublishMessage(
				new MqttFixedHeader(MqttMessageType.PUBLISH, false, MqttQoS.AT_MOST_ONCE, retained, 0),
				new MqttPublishVariableHeader(topic, 0), Unpooled.wrappedBuffer(payload));
		this.channel.writeAndFlush(msg).addListener(f -> {
			if (f.isSuccess()) {
				System.out.println("发布Qos = 0 消息成功");
			}
			else {
				System.out.println("发布Qos = 0 消息失败");
			}
		});
	}

}

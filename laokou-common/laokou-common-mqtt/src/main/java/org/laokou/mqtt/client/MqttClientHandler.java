package org.laokou.mqtt.client;

import io.netty.buffer.ByteBufUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.mqtt.MqttConnAckMessage;
import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageIdVariableHeader;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.netty.handler.codec.mqtt.MqttSubAckMessage;
import io.netty.handler.codec.mqtt.MqttSubscribeMessage;
import io.netty.handler.codec.mqtt.MqttSubscribePayload;
import io.netty.handler.codec.mqtt.MqttTopicSubscription;
import lombok.RequiredArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RequiredArgsConstructor
final class MqttClientHandler extends SimpleChannelInboundHandler<MqttMessage> {

	private final MqttClientTest client;

	private final PacketIdGenerator packetIdGenerator = new PacketIdGenerator();

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, MqttMessage msg) {
		MqttMessageType type = msg.fixedHeader().messageType();
		System.out.println(type);
		switch (type) {
			case CONNACK -> onConnAck(ctx.channel(), (MqttConnAckMessage) msg);
			case PINGRESP -> System.out.println("接收 PINGRESP");
			case SUBACK -> onSubAck((MqttSubAckMessage) msg);
			case PUBLISH -> handlePublish(ctx, (MqttPublishMessage) msg);
			default -> throw new IllegalStateException("Unexpected MQTT message type: " + type);
		}
	}

	private void handlePublish(ChannelHandlerContext ctx, MqttPublishMessage msg) {
		int packetId = msg.variableHeader().packetId();
		MqttQoS qos = msg.fixedHeader().qosLevel();

		switch (qos) {
			case AT_MOST_ONCE -> {
				onInboundMessage(msg);
			}
			case AT_LEAST_ONCE -> {

			}
			case EXACTLY_ONCE -> {

			}
		}
	}

	void onInboundMessage(MqttPublishMessage msg) {
		byte[] payload = ByteBufUtil.getBytes(msg.payload());
		System.out.println(new String(payload, StandardCharsets.UTF_8));
	}

	private void onConnAck(Channel ch, MqttConnAckMessage msg) {
		boolean sessionPresent = msg.variableHeader().isSessionPresent();
		int returnCode = msg.variableHeader().connectReturnCode().byteValue();
		System.out.println(sessionPresent + " , " + returnCode);
		if (returnCode == 0) {
			client.channel = ch;
			System.out.println("MQTT 已连接");
			subscribe(ch);
		}
	}

	private void subscribe(Channel channel) {
		MqttSubscribeMessage subscribeMsg = new MqttSubscribeMessage(
				new MqttFixedHeader(MqttMessageType.SUBSCRIBE, false, MqttQoS.AT_LEAST_ONCE, false, 0),
				MqttMessageIdVariableHeader.from(packetIdGenerator.nextId()),
				new MqttSubscribePayload(
						List.of(new MqttTopicSubscription("$share/laokou/test/123", MqttQoS.AT_MOST_ONCE),
								new MqttTopicSubscription("$share/laokou/test/456", MqttQoS.AT_LEAST_ONCE),
								new MqttTopicSubscription("$share/laokou/test/789", MqttQoS.EXACTLY_ONCE))));
		channel.writeAndFlush(subscribeMsg).addListener(future -> {
			if (future.isSuccess()) {
				System.out.println("订阅 写入成功");
			}
			else {
				System.out.println("订阅 写入失败");
			}
		});
	}

	private void onSubAck(MqttSubAckMessage msg) {
		int packetId = msg.variableHeader().messageId();
		int granted = msg.payload().grantedQoSLevels().isEmpty() ? 0x80 : msg.payload().grantedQoSLevels().getFirst();
		if (granted == 0x80) {
			System.out.println("订阅被拒绝, packetId=" + packetId);
		}
		else {
			System.out.println("订阅主题成功");
		}
	}

}

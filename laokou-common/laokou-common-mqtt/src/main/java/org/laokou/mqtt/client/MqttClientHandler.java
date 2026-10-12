package org.laokou.mqtt.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
final class MqttClientHandler extends SimpleChannelInboundHandler<MqttMessage> {

	private final MqttClient nettyMqttClient;

	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, MqttMessage mqttMessage) {

	}

}

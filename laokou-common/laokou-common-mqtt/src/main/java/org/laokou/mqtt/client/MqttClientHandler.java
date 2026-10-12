package org.laokou.mqtt.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

final class MqttClientHandler extends SimpleChannelInboundHandler<MqttMessage> {

	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, MqttMessage mqttMessage) {

	}

}

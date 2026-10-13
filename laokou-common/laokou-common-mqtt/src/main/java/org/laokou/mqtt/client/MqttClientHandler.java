package org.laokou.mqtt.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.mqtt.MqttConnAckMessage;
import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessageIdVariableHeader;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttPubAckMessage;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.netty.handler.codec.mqtt.MqttSubAckMessage;
import io.netty.handler.codec.mqtt.MqttUnsubAckMessage;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
final class MqttClientHandler extends SimpleChannelInboundHandler<io.netty.handler.codec.mqtt.MqttMessage> {

	private final MqttClient client;

	/**
	 * 入站 QoS2 去重表：已收到 PUBLISH、等待 PUBREL 的 packetId。
	 * 保证 Broker 重发 PUBLISH（DUP=1）时消息只投递一次（exactly-once）。
	 */
	private final Set<Integer> inboundQos2Pending = ConcurrentHashMap.newKeySet();

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, io.netty.handler.codec.mqtt.MqttMessage msg) {
		MqttMessageType type = msg.fixedHeader().messageType();
		switch (type) {
			case CONNACK -> handleConnAck(ctx, (MqttConnAckMessage) msg);
			case PUBLISH -> handlePublish(ctx, (MqttPublishMessage) msg);
			case PUBACK -> client.onPubAck(((MqttPubAckMessage) msg).variableHeader().messageId());
			case PUBREC -> handlePubRec(ctx, msg);
			case PUBREL -> handlePubRel(ctx, msg);
			case PUBCOMP -> client.onPubComp(((MqttMessageIdVariableHeader) msg.variableHeader()).messageId());
			case SUBACK -> client.onSubAck((MqttSubAckMessage) msg);
			case UNSUBACK -> client.onUnsubAck((MqttUnsubAckMessage) msg);
			case PINGRESP -> log.trace("收到 PINGRESP");
			default -> log.warn("未处理的报文类型: {}", type);
		}
	}

	private void handleConnAck(ChannelHandlerContext ctx, MqttConnAckMessage msg) {
		boolean sessionPresent = msg.variableHeader().isSessionPresent();
		int returnCode = msg.variableHeader().connectReturnCode().byteValue();
		client.onConnAck(ctx.channel(), sessionPresent, returnCode);
	}

	/** 入站 PUBLISH：按 QoS 等级应答并去重投递 */
	private void handlePublish(ChannelHandlerContext ctx, MqttPublishMessage msg) {
		int packetId = msg.variableHeader().packetId();
		MqttQoS qos = msg.fixedHeader().qosLevel();

		switch (qos) {
			case AT_MOST_ONCE -> client.onInboundMessage(msg);
			case AT_LEAST_ONCE -> {
				// QoS1：先应答 PUBACK，再投递（投递失败由上层重试策略负责）
				sendAck(ctx, MqttMessageType.PUBACK, MqttQoS.AT_MOST_ONCE, packetId);
				client.onInboundMessage(msg);
			}
			case EXACTLY_ONCE -> {
				// QoS2：首次收到才投递；重复 PUBLISH 只重答 PUBREC
				if (inboundQos2Pending.add(packetId)) {
					client.client(msg);
				} else {
					log.debug("QoS2 重复 PUBLISH packetId={}，仅重答 PUBREC", packetId);
				}
				sendAck(ctx, MqttMessageType.PUBREC, MqttQoS.AT_MOST_ONCE, packetId);
			}
		}
	}

	/** 收到 PUBREC：QoS2 出站第二步，回复 PUBREL */
	private void handlePubRec(ChannelHandlerContext ctx, io.netty.handler.codec.mqtt.MqttMessage msg) {
		int packetId = ((MqttMessageIdVariableHeader) msg.variableHeader()).messageId();
		// PUBREL 固定头 QoS 必须为 1
		sendAck(ctx, MqttMessageType.PUBREL, MqttQoS.AT_LEAST_ONCE, packetId);
		client.onPubRec(packetId);
	}

	/** 收到 PUBREL：QoS2 入站最后一步，回复 PUBCOMP 并允许该 packetId 复用 */
	private void handlePubRel(ChannelHandlerContext ctx, io.netty.handler.codec.mqtt.MqttMessage msg) {
		int packetId = ((MqttMessageIdVariableHeader) msg.variableHeader()).messageId();
		sendAck(ctx, MqttMessageType.PUBCOMP, MqttQoS.AT_MOST_ONCE, packetId);
		inboundQos2Pending.remove(packetId);
	}

	private void sendAck(ChannelHandlerContext ctx, MqttMessageType type, MqttQoS qos, int packetId) {
		MqttFixedHeader header = new MqttFixedHeader(type, false, qos, false, 0);
		io.netty.handler.codec.mqtt.MqttMessage ack = type == MqttMessageType.PUBACK ? new MqttPubAckMessage(header, MqttMessageIdVariableHeader.from(packetId)) : new io.netty.handler.codec.mqtt.MqttMessage(header, MqttMessageIdVariableHeader.from(packetId));
		ctx.writeAndFlush(ack).addListener(f -> {
			if (!f.isSuccess()) {
				log.warn("发送 {} packetId={} 失败: {}", type, packetId, f.cause().getMessage());
			}
		});
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent ex) {
			if (ex.state() == IdleState.WRITER_IDLE) {
				// 写空闲：发送心跳
				ctx.writeAndFlush(new io.netty.handler.codec.mqtt.MqttMessage(
					new MqttFixedHeader(MqttMessageType.PINGREQ, false, MqttQoS.AT_MOST_ONCE, false, 0)));
			} else if (ex.state() == IdleState.READER_IDLE) {
				// 读空闲超时：Broker 可能已失联，关闭连接触发重连
				log.warn("读空闲超时，关闭连接触发重连");
				ctx.close();
			}
			return;
		}
		super.userEventTriggered(ctx, evt);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		inboundQos2Pending.clear();
		client.onChannelInactive();
		super.channelInactive(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		log.error("MQTT 通道异常，错误信息：{}", cause.getMessage(), cause);
		ctx.close();
	}

}

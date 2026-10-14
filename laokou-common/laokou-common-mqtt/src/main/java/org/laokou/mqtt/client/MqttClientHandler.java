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
import io.netty.handler.codec.mqtt.MqttPubAckMessage;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.netty.handler.codec.mqtt.MqttSubAckMessage;
import io.netty.handler.codec.mqtt.MqttSubscribeMessage;
import io.netty.handler.codec.mqtt.MqttSubscribePayload;
import io.netty.handler.codec.mqtt.MqttTopicSubscription;
import io.netty.util.Timeout;
import lombok.RequiredArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.util.List;
/**
 * MQTT QoS 等级对比
 *
 * <pre>
 * ┌────────┬──────────────────┬──────────────────────────────────────────────┐
 * │  QoS   │      语义         │                   报文交互                     │
 * ├────────┼──────────────────┼──────────────────────────────────────────────┤
 * │   0    │ 最多一次 (Fire)   │ PUBLISH  → (无确认)                           │
 * │   1    │ 至少一次          │ PUBLISH  → PUBACK                             │
 * │   2    │ 恰好一次          │ PUBLISH → PUBREC → PUBREL → PUBCOMP (四次握手) │
 * └────────┴──────────────────┴──────────────────────────────────────────────┘
 * </pre>
 *
 * QoS 1 消息流程 - 至少一次投递保证
 *
 * <pre>
 *  Publisher                           Broker                           Subscriber
 *      |                                 |                                 |
 *      | --- PUBLISH (msgId=1) --------> |                                 |
 *      |                                 | --- PUBLISH (msgId=1) --------> |
 *      |                                 |                                 |
 *      |                                 | <-------- PUBACK (msgId=1) ---- |
 *      | <-------- PUBACK (msgId=1) ---- |                                 |
 *      |                                 |                                 |
 *
 *  说明:
 *  1. Publisher 发送 PUBLISH 报文，携带消息标识符 msgId
 *  2. Broker 收到后回复 PUBACK 确认，并转发给 Subscriber
 *  3. Subscriber 收到消息后回复 PUBACK 确认
 *  4. 若 Publisher 未收到 PUBACK，会重发 PUBLISH (DUP=1)
 * </pre>
 *
 * QoS 2 消息流程 - 恰好一次投递保证（四次握手）
 *
 * <pre>
 *  Publisher                           Broker                           Subscriber
 *      |                                 |                                 |
 *      | --- PUBLISH (msgId=2) --------> |                                 |
 *      |                                 | --- PUBLISH (msgId=2) --------> |
 *      |                                 |                                 |
 *      | <-------- PUBREC (msgId=2) ---- |                                 |
 *      |                                 | <-------- PUBREC (msgId=2) ---- |
 *      |                                 |                                 |
 *      | --- PUBREL (msgId=2) ---------> |                                 |
 *      |                                 | --- PUBREL (msgId=2) ---------> |
 *      |                                 |                                 |
 *      | <-------- PUBCOMP (msgId=2) --- |                                 |
 *      |                                 | <-------- PUBCOMP (msgId=2) --- |
 *      |                                 |                                 |
 *
 *  阶段说明:
 *  ┌─────────────┬─────────────────────────────────────────────────────────┐
 *  │    阶段      │                      报文交互                            │
 *  ├─────────────┼─────────────────────────────────────────────────────────┤
 *  │ 1. 发布      │ Publisher → PUBLISH  → Broker                          │
 *  │ 2. 接收确认   │ Publisher ← PUBREC   ← Broker  (Broker已存储, 可释放本地) │
 *  │ 3. 发布释放   │ Publisher → PUBREL   → Broker  (确认不再重发)            │
 *  │ 4. 完成      │ Publisher ← PUBCOMP  ← Broker  (消息正式完成投递)         │
 *  └─────────────┴─────────────────────────────────────────────────────────┘
 *
 *  关键机制:
 *  - PUBREC: 通知发送方"已收到"，发送方停止重传，但消息不删除
 *  - PUBREL: 发送方承诺不再重发，Broker 可正式投递给 Subscriber
 *  - PUBCOMP: 双方确认流程结束，释放 msgId 资源
 *  - 任意阶段丢失报文都会触发重传，保证幂等性
 * </pre>
 *
 * @author laokou
 */
@RequiredArgsConstructor
final class MqttClientHandler extends SimpleChannelInboundHandler<MqttMessage> {

	private final MqttClientTest client;

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, MqttMessage msg) {
		MqttMessageType type = msg.fixedHeader().messageType();
		System.out.println(type);
		switch (type) {
			case CONNACK -> onConnAck(ctx.channel(), (MqttConnAckMessage) msg);
			case PINGRESP -> System.out.println("接收 PINGRESP");
			case SUBACK -> onSubAck((MqttSubAckMessage) msg);
			case PUBLISH -> handlePublish(ctx, (MqttPublishMessage) msg);
			case PUBACK -> onPubAck((MqttPubAckMessage) msg);
			default -> throw new IllegalStateException("Unexpected MQTT message type: " + type);
		}
	}

	private void onPubAck(MqttPubAckMessage msg) {
		OutboundMessage message = client.inflightMap.remove(msg.variableHeader().messageId());
		if (message != null) {
			cancelTimeout(message);
		}
	}

	private void cancelTimeout(OutboundMessage msg) {
		Timeout task = msg.getTimeoutTask();
		if (task != null) {
			task.cancel();
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
				// QoS1：先应答 PUBACK，再投递（投递失败由上层重试策略负责）
				sendAck(ctx, MqttMessageType.PUBACK, MqttQoS.AT_MOST_ONCE, packetId);
			}
			case EXACTLY_ONCE -> {
				// QoS2：首次收到才投递；重复 PUBLISH 只重答 PUBREC
				sendAck(ctx, MqttMessageType.PUBREC, MqttQoS.AT_MOST_ONCE, packetId);
			}
		}
	}

	private void sendAck(ChannelHandlerContext ctx, MqttMessageType type, MqttQoS qos, int packetId) {
		MqttFixedHeader header = new MqttFixedHeader(type, false, qos, false, 0);
		MqttMessage ack = type == MqttMessageType.PUBACK
			? new MqttPubAckMessage(header, MqttMessageIdVariableHeader.from(packetId))
			: new MqttMessage(header, MqttMessageIdVariableHeader.from(packetId));
		ctx.writeAndFlush(ack).addListener(f -> {
			if (!f.isSuccess()) {
				System.out.println(f.cause().toString());
			} else {
				System.out.println("写入成功");
			}
		});
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
				MqttMessageIdVariableHeader.from(client.packetIdGenerator.nextId()),
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

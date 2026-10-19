package org.laokou.mqtt.client;

import io.netty.buffer.ByteBufUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.mqtt.MqttConnAckMessage;
import io.netty.handler.codec.mqtt.MqttConnectReturnCode;
import io.netty.handler.codec.mqtt.MqttFixedHeader;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageIdVariableHeader;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttPubAckMessage;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.netty.handler.codec.mqtt.MqttReasonCodes;
import io.netty.handler.codec.mqtt.MqttSubAckMessage;
import io.netty.handler.codec.mqtt.MqttSubscribeMessage;
import io.netty.handler.codec.mqtt.MqttSubscribePayload;
import io.netty.handler.codec.mqtt.MqttTopicSubscription;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * MQTT QoS 等级对比
 *
 * <pre>
 * ┌────────┬──────────────────┬──────────────────────────────────────────────┐
 * │  QoS   │      语义         │                   报文交互                     │
 * ├────────┼──────────────────┼──────────────────────────────────────────────┤
 * │   0    │ 最多一次 (Fire)   │ PUBLISH  → (无确认)                            │
 * │   1    │ 至少一次          │ PUBLISH  → PUBACK                             │
 * │   2    │ 恰好一次          │ PUBLISH → PUBREC → PUBREL → PUBCOMP (四次握手)  │
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
@Slf4j
final class NettyMqttClientHandler extends SimpleChannelInboundHandler<MqttMessage> {

	private final NettyMqttClient client;

	private final InboundQos2[] inboundQos2Pending;

	NettyMqttClientHandler(NettyMqttClient client) {
		this.client = client;
		this.inboundQos2Pending = new InboundQos2[0XFFFF];
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, MqttMessage msg) {
		MqttMessageType mqttMessageType = msg.fixedHeader().messageType();
		switch (mqttMessageType) {
			// 建立连接ACK
			case CONNACK -> onConnAck(ctx.channel(), (MqttConnAckMessage) msg);
			// 订阅主题ACK
			case SUBACK -> onSubAck((MqttSubAckMessage) msg);
			// Qos1消息收发流程
			// 发布消息ACK
			case PUBACK -> onPubAck((MqttPubAckMessage) msg);
			// Qos2消息收发流程
			// PUBREC => 通知发送方"已收到"，发送方停止重传，但消息不删除
			case PUBREC -> onPubRec(ctx, msg);
			// PUBREL => 发送方承诺不再重发，Broker可正式投递给 Subscriber
			case PUBREL -> onPubRel(ctx, msg);
			// PUBCOMP => 双方确认流程结束，释放packetId
			case PUBCOMP -> onPubComp(msg);
			// 消息投递
			case PUBLISH -> onPublish(ctx, (MqttPublishMessage) msg);
			case PINGRESP -> log.debug("【Netty-MQTT-Client】 => 接收 PINGRESP");
			default -> throw new IllegalStateException(
					String.format("【Netty-MQTT-Client】 => 不支持类型：%s", mqttMessageType.name()));
		}
	}

	private void onConnAck(Channel ch, MqttConnAckMessage msg) {
		MqttConnectReturnCode returnCode = msg.variableHeader().connectReturnCode();
		if (returnCode == MqttConnectReturnCode.CONNECTION_ACCEPTED) {
			log.info("【Netty-MQTT-Client】 => 连接 MQTT Broker 成功");
			subscribe(ch);
			client.setChannel(ch);
			client.getReconnectAttempts().set(0);
			client.getState().set(ClientState.CONNECTED);
		}
		else {
			log.error("【Netty-MQTT-Client】 => 连接 MQTT Broker 失败，错误信息：{}", returnCode);
			ch.close();
		}
	}

	private void onSubAck(MqttSubAckMessage msg) {
		int packetId = msg.variableHeader().messageId();
		int granted = msg.payload().grantedQoSLevels().isEmpty() ? MqttReasonCodes.SubAck.UNSPECIFIED_ERROR.byteValue()
				: msg.payload().grantedQoSLevels().getFirst();
		if (granted == MqttReasonCodes.SubAck.UNSPECIFIED_ERROR.byteValue()) {
			log.info("【Netty-MQTT-Client】 => 订阅 MQTT Broker 主题失败, packetId：{}", packetId);
		}
		else {
			log.info("【Netty-MQTT-Client】 => 订阅 MQTT Broker 主题成功, packetId：{}", packetId);
		}
	}

	private void onPubAck(MqttPubAckMessage msg) {
		int packetId = msg.variableHeader().messageId();
		OutboundMessage message = client.getInflightStore().remove(packetId);
		if (message != null && message.getState() == State.WAIT_PUBACK) {
			client.cancelAckTimeout(message);
		}
	}

	/**
	 * 收到 PUBREC：QoS2 出站第二步，回复 PUBREL.
	 * @param ctx 上下文
	 * @param msg 消息
	 */
	private void onPubRec(ChannelHandlerContext ctx, MqttMessage msg) {
		int packetId = ((MqttMessageIdVariableHeader) msg.variableHeader()).messageId();
		onPubRec(packetId, ctx);
	}

	private void onPubRec(int packetId, ChannelHandlerContext ctx) {
		OutboundMessage msg = client.getInflightStore().get(packetId);
		if (msg == null || msg.getQos() != MqttQoS.EXACTLY_ONCE) {
			return;
		}
		// 首次接收 PUBREC
		if (msg.getState() == State.WAIT_PUBREC) {
			client.cancelAckTimeout(msg);
			msg.setState(State.WAIT_PUBCOMP);
			// 首次接收 PUBREC，发送 PUBREL
			sendPubRel(ctx.channel(), msg.getPacketId(), false);
			// 发送端开启发送 MqttMessageType.PUBREL 消息的定时任务，防止Broker因网络问题未接收到
			client.scheduleAckTimeout(msg);
		}
		else if (msg.getState() == State.WAIT_PUBCOMP) {
			// 重复接收到 PUBREC，重发 PUBREL
			sendPubRel(ctx.channel(), msg.getPacketId(), true);
		}
	}

	private void sendPubRel(Channel channel, int packetId, boolean isDup) {
		MqttMessage pubRel = new MqttMessage(
				new MqttFixedHeader(MqttMessageType.PUBREL, isDup, MqttQoS.AT_LEAST_ONCE, false, 0),
				MqttMessageIdVariableHeader.from(packetId));
		if (channel != null && channel.isActive()) {
			client.writeAndFlush(channel, pubRel, _ -> channel.close());
		}
	}

	/**
	 * 收到 PUBREL：QoS2 入站最后一步，回复 PUBCOMP 并允许该 packetId 复用.
	 * @param ctx 上下文
	 * @param msg 消息
	 */
	private void onPubRel(ChannelHandlerContext ctx, MqttMessage msg) {
		int packetId = ((MqttMessageIdVariableHeader) msg.variableHeader()).messageId();
		InboundQos2 inbound = inboundQos2Pending[packetId];
		if (inbound != null) {
			inboundQos2Pending[packetId] = null;
			ReferenceCountUtil.retain(msg);
			// 处理消息
			onInboundMessage(inbound.topic(), inbound.payload(), MqttQoS.EXACTLY_ONCE);
			ReferenceCountUtil.release(msg);
		}
		sendAck(ctx, MqttMessageType.PUBCOMP, packetId);
	}

	private void onPubComp(MqttMessage msg) {
		int packetId = ((MqttMessageIdVariableHeader) msg.variableHeader()).messageId();
		OutboundMessage message = client.getInflightStore().get(packetId);
		if (message != null && message.getState() == State.WAIT_PUBACK
				&& client.getInflightStore().putIfAbsent(message)) {
			client.cancelAckTimeout(message);
		}
	}

	private void onPublish(ChannelHandlerContext ctx, MqttPublishMessage msg) {
		int packetId = msg.variableHeader().packetId();
		switch (msg.fixedHeader().qosLevel()) {
			case AT_MOST_ONCE -> {
				// 把这条消息的所有权分一份给别的线程，防止它被别人提前释放掉
				ReferenceCountUtil.retain(msg);
				onInboundMessage(msg.variableHeader().topicName(), ByteBufUtil.getBytes(msg.payload()),
						MqttQoS.AT_LEAST_ONCE);
				ReferenceCountUtil.release(msg);
			}
			case AT_LEAST_ONCE -> {
				// 把这条消息的所有权分一份给别的线程，防止它被别人提前释放掉
				ReferenceCountUtil.retain(msg);
				onInboundMessage(msg.variableHeader().topicName(), ByteBufUtil.getBytes(msg.payload()),
						MqttQoS.AT_LEAST_ONCE);
				sendAck(ctx, MqttMessageType.PUBACK, packetId);
				ReferenceCountUtil.release(msg);
			}
			case EXACTLY_ONCE -> {
				// QoS2：首次收到才投递；重复 PUBLISH 只重答 PUBREC
				if (inboundQos2Pending[packetId] == null) {
					byte[] payload = ByteBufUtil.getBytes(msg.payload());
					inboundQos2Pending[packetId] = new InboundQos2(msg.variableHeader().topicName(), payload);
				}
				sendAck(ctx, MqttMessageType.PUBREC, packetId);
			}
		}
	}

	private void sendAck(ChannelHandlerContext ctx, MqttMessageType type, int packetId) {
		MqttFixedHeader header = new MqttFixedHeader(type, false, MqttQoS.AT_LEAST_ONCE, false, 0);
		MqttMessage ack = type == MqttMessageType.PUBACK
				? new MqttPubAckMessage(header, MqttMessageIdVariableHeader.from(packetId))
				: new MqttMessage(header, MqttMessageIdVariableHeader.from(packetId));
		client.writeAndFlush(ctx.channel(), ack, _ -> ctx.close());
	}

	private void onInboundMessage(String topic, byte[] payload, MqttQoS qos) {
		System.out.println(new String(payload, StandardCharsets.UTF_8));
	}

	private void subscribe(Channel channel) {
		MqttSubscribeMessage subscribeMsg = new MqttSubscribeMessage(
				new MqttFixedHeader(MqttMessageType.SUBSCRIBE, false, MqttQoS.AT_LEAST_ONCE, false, 0),
				MqttMessageIdVariableHeader.from(client.getPacketIdGenerator().nextId()),
				new MqttSubscribePayload(
						List.of(new MqttTopicSubscription("$share/laokou/test/123", MqttQoS.AT_MOST_ONCE),
								new MqttTopicSubscription("$share/laokou/test/456", MqttQoS.AT_LEAST_ONCE),
								new MqttTopicSubscription("$share/laokou/test/789", MqttQoS.EXACTLY_ONCE))));
		client.writeAndFlush(channel, subscribeMsg, _ -> channel.close());
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.close();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		onChannelInactive();
		super.channelInactive(ctx);
	}

	public void onChannelInactive() {
		if (client.getState().get() == ClientState.CLOSED) {
			return;
		}
		client.handleDisconnect();
	}

}

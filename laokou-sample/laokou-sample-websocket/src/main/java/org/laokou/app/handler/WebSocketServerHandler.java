/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.app.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.laokou.client.dto.clientobject.MessageCO;
import org.laokou.client.dto.clientobject.PayloadCO;
import org.laokou.client.dto.domainevent.PublishMessageEvent;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.core.utils.SpringUtil;
import org.laokou.common.domain.support.DomainEventPublisher;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.netty.config.WebSocketSessionManager;
import org.laokou.common.rocketmq.template.SendMessageType;
import org.laokou.domain.model.MessageType;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import static org.laokou.common.i18n.common.constant.StringConstant.EMPTY;
import static org.laokou.common.i18n.common.exception.StatusCode.UNAUTHORIZED;
import static org.laokou.domain.model.MessageType.PING;
import static org.laokou.infrastructure.common.constant.MqConstant.LAOKOU_MESSAGE_TOPIC;

/**
 * WebSocket自定义处理器.
 *
 * @author laokou
 */
@Slf4j
@Component
@ChannelHandler.Sharable
@RequiredArgsConstructor
public class WebSocketServerHandler extends ChannelInboundHandlerAdapter {

	private final DomainEventPublisher rocketMQDomainEventPublisher;

	private final SpringUtil springUtil;

	/**
	 * see
	 * {@link io.netty.channel.SimpleChannelInboundHandler#channelRead(ChannelHandlerContext, Object)}.
	 * @param ctx 处理器上下文
	 * @param msg 消息
	 */
	@Override
	@SneakyThrows
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		boolean release = true;
		try {
			if (msg instanceof WebSocketFrame frame && frame instanceof TextWebSocketFrame textWebSocketFrame) {
				read(ctx, textWebSocketFrame);
			}
			else {
				// 传递下一个处理器
				release = false;
				super.channelRead(ctx, msg);
			}
		}
		finally {
			if (release) {
				ReferenceCountUtil.release(msg);
			}
		}
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) {
		log.info("建立连接：{}", ctx.channel().id().asLongText());
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) {
		String channelId = ctx.channel().id().asLongText();
		log.info("断开连接：{}", channelId);
		WebSocketSessionManager.remove(channelId);
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		// 读空闲事件
		if (evt instanceof IdleStateEvent idleStateEvent
				&& ObjectUtil.equals(idleStateEvent.state(), IdleStateEvent.READER_IDLE_STATE_EVENT.state())) {
			Channel channel = ctx.channel();
			String ping = PING.name().toLowerCase();
			log.info("发送{}心跳{}", channel.id().asLongText(), ping);
			ctx.writeAndFlush(new TextWebSocketFrame(ping));
		}
		else {
			super.userEventTriggered(ctx, evt);
		}
	}

	private void read(ChannelHandlerContext ctx, TextWebSocketFrame frame) {
		Channel channel = ctx.channel();
		String str = frame.text();

		if (StringUtil.isEmpty(str)) {
			channel.writeAndFlush(new TextWebSocketFrame(JacksonUtil.toJsonStr(Result.fail(UNAUTHORIZED))));
			ctx.close();
			return;
		}

		MessageCO co = JacksonUtil.toBean(str, MessageCO.class);
		Object payload = co.getPayload();
		String type = co.getType();
		Assert.notNull(payload, "payload不能为空");
		Assert.notNull(type, "type不能为空");
		switch (MessageType.valueOf(type.toUpperCase())) {
			case PONG: {
				log.info("接收{}心跳{}", channel.id().asLongText(), payload);
				break;
			}
			case CONNECT: {
				log.info("已连接ClientID：{}", payload);
				WebSocketSessionManager.add(payload.toString(), channel);
				break;
			}
			case MESSAGE: {
				log.info("接收消息：{}", payload);
				rocketMQDomainEventPublisher.publish(
						new PublishMessageEvent(LAOKOU_MESSAGE_TOPIC, EMPTY,
								JacksonUtil.toValue(payload, PayloadCO.class), springUtil.getServiceId()),
						SendMessageType.TRANSACTION);
				break;
			}
			default: {
			}
		}
	}

}

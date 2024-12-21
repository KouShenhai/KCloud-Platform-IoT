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

package org.laokou.infrastructure.config;

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
import org.laokou.common.i18n.utils.JacksonUtil;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.netty.config.SpringWebSocketServerProperties;
import org.laokou.common.netty.config.WebSocketSessionHeartBeatManager;
import org.laokou.common.netty.config.WebSocketSessionManager;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import static org.laokou.common.i18n.common.exception.StatusCode.UNAUTHORIZED;
import static org.laokou.domain.model.MessageType.PING;
import static org.laokou.domain.model.MessageType.PONG;

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

	private final WebSocketMessageProcessor messageProcessor;

	private final SpringWebSocketServerProperties springWebSocketServerProperties;

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
		WebSocketSessionHeartBeatManager.removeHeartBeat(channelId);
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		// 读空闲事件
		if (evt instanceof IdleStateEvent idleStateEvent
				&& ObjectUtil.equals(idleStateEvent.state(), IdleStateEvent.READER_IDLE_STATE_EVENT.state())) {
			Channel channel = ctx.channel();
			String clientId = channel.id().asLongText();
			int maxHeartBeatCount = springWebSocketServerProperties.getMaxHeartBeatCount();
			if (WebSocketSessionHeartBeatManager.getHeartBeat(clientId) >= maxHeartBeatCount) {
				log.info("关闭连接，超过{}次未接收{}心跳{}", maxHeartBeatCount, clientId, PONG.name().toLowerCase());
				ctx.close();
				return;
			}
			String ping = PING.name().toLowerCase();
			log.info("发送{}心跳{}", clientId, ping);
			ctx.writeAndFlush(new TextWebSocketFrame(ping));
			WebSocketSessionHeartBeatManager.incrementHeartBeat(clientId);
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
		MessageCO message = JacksonUtil.toBean(str, MessageCO.class);
		Assert.notNull(message.getPayload(), "payload must not be null");
		Assert.notNull(message.getType(), "type must not bee null");
		messageProcessor.processMessage(message, channel);
	}

}

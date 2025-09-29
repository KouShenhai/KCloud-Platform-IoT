/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.websocket.config;

import tools.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.util.JacksonUtils;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.StringUtils;
import org.laokou.common.security.config.OAuth2OpaqueTokenIntrospector;
import org.laokou.common.context.util.UserDetails;
import org.laokou.common.websocket.model.WebSocketMessageCO;
import org.laokou.common.websocket.model.WebSocketTypeEnum;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

/**
 * WebSocket自定义处理器.
 *
 * @author laokou
 */
@Slf4j
@ChannelHandler.Sharable
@RequiredArgsConstructor
public class WebSocketServerHandler extends ChannelInboundHandlerAdapter {

	private final SpringWebSocketServerProperties springWebSocketServerProperties;

	private final OAuth2OpaqueTokenIntrospector oAuth2OpaqueTokenIntrospector;

	/**
	 * see
	 * {@link io.netty.channel.SimpleChannelInboundHandler#channelRead(ChannelHandlerContext, Object)}.
	 * @param ctx 处理器上下文
	 * @param msg 消息
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		boolean release = true;
		// @formatter:off
		try {
			if (msg instanceof WebSocketFrame frame
				&& frame instanceof TextWebSocketFrame textWebSocketFrame) {
				read(ctx, textWebSocketFrame);
			}
			else {
				// 传递下一个处理器
				release = false;
				ctx.fireChannelRead(msg);
			}
		}
		finally {
			if (release) {
				ReferenceCountUtil.release(msg);
			}
		}
		// @formatter:on
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) {
		log.info("【WebSocket-Server】 => 建立连接：{}", ctx.channel().id().asLongText());
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws InterruptedException {
		String channelId = ctx.channel().id().asLongText();
		log.info("【WebSocket-Server】 => 断开连接：{}", channelId);
		WebSocketSessionManager.remove(ctx.channel());
		WebSocketSessionHeartBeatManager.remove(channelId);
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		// 读空闲事件
		if (evt instanceof IdleStateEvent idleStateEvent
				&& ObjectUtils.equals(idleStateEvent.state(), IdleStateEvent.READER_IDLE_STATE_EVENT.state())) {
			Channel channel = ctx.channel();
			String channelId = channel.id().asLongText();
			int maxHeartBeatCount = springWebSocketServerProperties.getMaxHeartBeatCount();
			if (WebSocketSessionHeartBeatManager.get(channelId) >= maxHeartBeatCount) {
				log.info("【WebSocket-Server】 => 关闭连接，超过{}次未接收{}心跳{}", maxHeartBeatCount, channelId,
						WebSocketTypeEnum.PONG.getCode());
				ctx.close();
				return;
			}
			String ping = WebSocketTypeEnum.PING.getCode();
			log.info("【WebSocket-Server】 => 发送{}心跳{}", channelId, ping);
			ctx.writeAndFlush(new TextWebSocketFrame(ping));
			WebSocketSessionHeartBeatManager.increment(channelId);
		}
		else {
			super.userEventTriggered(ctx, evt);
		}
	}

	private void read(ChannelHandlerContext ctx, TextWebSocketFrame frame)
			throws JsonProcessingException, InterruptedException {
		Channel channel = ctx.channel();
		String str = frame.text();
		if (StringUtils.isEmpty(str)) {
			return;
		}
		try {
			WebSocketMessageCO co = JacksonUtils.toBean(str, WebSocketMessageCO.class);
			OAuth2AuthenticatedPrincipal principal = oAuth2OpaqueTokenIntrospector.introspect(co.getToken());
			UserDetails userDetails = (UserDetails) principal;
			log.info("【WebSocket-Server】 => 令牌校验成功，用户名：{}", principal.getName());
			WebSocketTypeEnum.getByCode(co.getType()).handle(userDetails, co, channel);
		}
		catch (JsonParseException e) {
			log.error("【WebSocket-Server】 => JSON格式转换失败，错误信息：{}", e.getMessage(), e);
			ctx.close();
		}
		catch (OAuth2AuthenticationException ex) {
			OAuth2Error error = ex.getError();
			log.error("【WebSocket-Server】 => 令牌校验失败，错误信息：{}", error.getDescription(), ex);
			ctx.close();
		}
	}

}

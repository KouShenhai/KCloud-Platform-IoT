/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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
package org.laokou.im.module.websocket;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.ReferenceCountUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.laokou.auth.domain.user.User;
import org.laokou.common.core.utils.MapUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.redis.utils.ReactiveRedisUtil;
import org.laokou.common.redis.utils.RedisKeyUtil;
import org.laokou.common.redis.utils.RedisUtil;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.laokou.common.core.constant.BizConstant.AUTHORIZATION;
import static org.laokou.common.i18n.common.Constant.MARK;

/**
 * @author laokou
 */
@Component
@Slf4j
@ChannelHandler.Sharable
@RequiredArgsConstructor
public class WebsocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

	private static final String WS_HEADER_NAME = "Upgrade";

	private static final String WS_HEADER_VALUE = "websocket";

	private final ReactiveRedisUtil reactiveRedisUtil;

	public static final Cache<String, Channel> USER_CACHE;

	static {
		USER_CACHE = Caffeine.newBuilder()
			.expireAfterAccess(RedisUtil.HOUR_ONE_EXPIRE, TimeUnit.SECONDS)
			.initialCapacity(100)
			.build();
	}

	@Override
	@SneakyThrows
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		if (msg instanceof FullHttpRequest request) {
			initWsInfo(ctx, request);
		}
		super.channelRead(ctx, msg);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) {

	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) {
		log.info("建立连接：{}", ctx.channel().id().asLongText());
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) {
		// 移除channel
		String channelId = ctx.channel().id().asLongText();
		log.info("断开连接：{}", channelId);
	}

	private String getAuthorization(Map<String, String> paramMap) {
		String Authorization = paramMap.getOrDefault(AUTHORIZATION, "");
		if (StringUtil.isNotEmpty(Authorization)) {
			return Authorization.substring(7);
		}
		return Authorization;
	}

	private void initWsInfo(ChannelHandlerContext ctx, FullHttpRequest request) {
		try {
			if (request.decoderResult().isFailure() || !WS_HEADER_VALUE.equals(request.headers().get(WS_HEADER_NAME))) {
				handleRequestError(ctx, HttpResponseStatus.BAD_REQUEST);
				return;
			}
			String uri = request.uri();
			int index = uri.indexOf(MARK);
			String param = uri.substring(index + 1);
			Map<String, String> paramMap = MapUtil.parseParamMap(param);
			String Authorization = getAuthorization(paramMap);
			request.setUri(uri.substring(0, index));
			if (StringUtil.isEmpty(Authorization)) {
				handleRequestError(ctx, HttpResponseStatus.UNAUTHORIZED);
				return;
			}
			String userInfoKey = RedisKeyUtil.getUserInfoKey(Authorization);
			reactiveRedisUtil.get(userInfoKey).subscribe(obj -> {
				if (obj == null) {
					handleRequestError(ctx, HttpResponseStatus.UNAUTHORIZED);
					return;
				}
				User user = (User) obj;
				Channel channel = ctx.channel();
				String userId = user.getId().toString();
				USER_CACHE.put(userId, channel);
			});
		}
		catch (Exception e) {
			log.error("错误信息：{}", e.getMessage());
		}
	}

	private void handleRequestError(ChannelHandlerContext ctx, HttpResponseStatus httpResponseStatus) {
		DefaultFullHttpResponse defaultFullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
				httpResponseStatus);
		ByteBuf byteBuf = Unpooled.copiedBuffer(httpResponseStatus.toString(), StandardCharsets.UTF_8);
		defaultFullHttpResponse.content().writeBytes(byteBuf);
		// 释放资源
		ReferenceCountUtil.release(byteBuf);
		ctx.channel().writeAndFlush(defaultFullHttpResponse);
		ctx.close();
	}

}

/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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
import org.laokou.common.core.utils.MapUtil;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.redis.utils.ReactiveRedisUtil;
import org.laokou.common.redis.utils.RedisKeyUtil;
import org.laokou.common.redis.utils.RedisUtil;
import org.laokou.common.security.utils.User;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.netty.handler.codec.http.HttpResponseStatus.UNAUTHORIZED;
import static org.laokou.common.i18n.common.RequestHeaderConstants.*;
import static org.laokou.common.i18n.common.StringConstants.EMPTY;
import static org.laokou.common.i18n.common.StringConstants.MARK;

/**
 * @author laokou
 */
@Component
@Slf4j
@ChannelHandler.Sharable
@RequiredArgsConstructor
public class WebsocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

	private final ReactiveRedisUtil reactiveRedisUtil;

	/**
	 * 建立连接的用户.
	 */
	public static final Cache<String, Channel> USER_CACHE;

	static {
		USER_CACHE = Caffeine.newBuilder()
			.expireAfterAccess(RedisUtil.HOUR_ONE_EXPIRE, TimeUnit.SECONDS)
			.initialCapacity(500)
			.build();
	}

	@Override
	@SneakyThrows
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		if (msg instanceof FullHttpRequest request) {
			init(ctx, request);
		}
		super.channelRead(ctx, msg);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) {
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) {
		// log.info("建立连接：{}", ctx.channel().id().asLongText());
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) {
		// 移除channel
		// log.info("断开连接：{}", ctx.channel().id().asLongText());
	}

	private String getAuthorization(Map<String, String> paramMap) {
		String Authorization = paramMap.getOrDefault(AUTHORIZATION, EMPTY);
		if (StringUtil.isNotEmpty(Authorization)) {
			return Authorization.substring(7);
		}
		return Authorization;
	}

	private void init(ChannelHandlerContext ctx, FullHttpRequest request) {
		try {
			if (request.decoderResult().isFailure() || !WEBSOCKET.equals(request.headers().get(UPGRADE))) {
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
				handleRequestError(ctx, UNAUTHORIZED);
				return;
			}
			String userInfoKey = RedisKeyUtil.getUserInfoKey(Authorization);
			reactiveRedisUtil.get(userInfoKey).subscribe(obj -> {
				if (ObjectUtil.isNull(obj)) {
					handleRequestError(ctx, UNAUTHORIZED);
					return;
				}
				User user = (User) obj;
				Channel channel = ctx.channel();
				String userId = user.getId().toString();
				USER_CACHE.put(userId, channel);
			});
		}
		catch (Exception e) {
			log.error("错误信息：{}，详情见日志", LogUtil.result(e.getMessage()), e);
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

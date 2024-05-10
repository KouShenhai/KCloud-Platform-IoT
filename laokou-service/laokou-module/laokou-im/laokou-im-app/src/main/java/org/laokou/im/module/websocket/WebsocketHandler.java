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

package org.laokou.im.module.websocket;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.redis.utils.RedisKeyUtil;
import org.laokou.common.redis.utils.RedisUtil;
import org.laokou.common.security.utils.UserDetail;
import org.springframework.stereotype.Component;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.laokou.common.i18n.common.exception.StatusCode.UNAUTHORIZED;
import static org.laokou.common.redis.utils.RedisUtil.HOUR_ONE_EXPIRE;

/**
 * websocket自定义处理器.
 *
 * @author laokou
 */
@Component
@Slf4j
@ChannelHandler.Sharable
@RequiredArgsConstructor
public class WebsocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

	private final RedisUtil redisUtil;

	/**
	 * 建立连接的用户集合.
	 */
	private static final Cache<String, Channel> CLIENT_CACHE = Caffeine.newBuilder()
		.expireAfterAccess(HOUR_ONE_EXPIRE, SECONDS)
		.initialCapacity(500)
		.build();

	/**
	 * 根据客户ID获取通道.
	 * @param clientId 客户ID
	 * @return 通道
	 */
	public static Channel getChannel(String clientId) {
		return CLIENT_CACHE.getIfPresent(clientId);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) {
		Channel channel = ctx.channel();
		String authorization = frame.text();
		String userInfoKey = RedisKeyUtil.getUserInfoKey(authorization);
		Object obj = redisUtil.get(userInfoKey);
		if (obj != null) {
			UserDetail userDetail = (UserDetail) obj;
			Long id = userDetail.getId();
			CLIENT_CACHE.put(id.toString(), channel);
		}
		else {
			channel.writeAndFlush(new TextWebSocketFrame(JacksonUtil.toJsonStr(Result.fail("" + UNAUTHORIZED))));
			ctx.close();
		}
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) {
		log.info("建立连接：{}", ctx.channel().id().asLongText());
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) {
		log.info("断开连接：{}", ctx.channel().id().asLongText());
	}

}

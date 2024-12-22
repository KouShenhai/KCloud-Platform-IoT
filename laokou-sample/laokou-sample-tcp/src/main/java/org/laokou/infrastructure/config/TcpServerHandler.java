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

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.laokou.client.dto.clientobject.SensorCO;
import org.laokou.common.i18n.utils.JacksonUtil;
import org.laokou.common.netty.config.SessionManager;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class TcpServerHandler extends ChannelInboundHandlerAdapter {

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
			if (msg instanceof SensorCO co) {
				String clientId = String.valueOf(co.getId());
				SessionManager.add(clientId, ctx.channel());
				log.info("接收数据：{}", JacksonUtil.toJsonStr(co));
				co.setId(co.getId());
				co.setX((byte) (co.getX() + 1));
				co.setY((byte) (co.getY() + 1));
				co.setZ((byte) (co.getZ() + 1));
				ctx.writeAndFlush(co);
			}
			else {
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
		SessionManager.remove(channelId);
	}

}

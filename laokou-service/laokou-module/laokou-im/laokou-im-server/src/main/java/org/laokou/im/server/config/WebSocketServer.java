/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.laokou.im.server.config;

import io.netty.bootstrap.AbstractBootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.netty.config.Server;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;

import static org.laokou.im.server.config.WebsocketHandler.USER_MAP;

/**
 * @author laokou
 */
@Slf4j
@RequiredArgsConstructor
public class WebSocketServer extends Server {

	private final WebsocketChannelInitializer websocketChannelInitializer;

	public static final int PORT = 7777;

	private static final String POOL_NAME = "laokou-websocket-pool";

	private final TaskExecutionProperties taskExecutionProperties;

	@Override
	protected int getPort() {
		return PORT;
	}

	@Override
	protected AbstractBootstrap<?, ?> init() {
		// 核心线程数
		int coreSize = taskExecutionProperties.getPool().getCoreSize();
		// boss负责监听端口
		boss = new NioEventLoopGroup(1, new DefaultThreadFactory(POOL_NAME, 10));
		// work负责线程读写
		work = new NioEventLoopGroup(coreSize, new DefaultThreadFactory(POOL_NAME, 10));
		// 配置引导
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		// 绑定线程组
		return serverBootstrap.group(boss, work)
				// 指定通道
				.channel(NioServerSocketChannel.class)
				// 维持长连接
				.childOption(ChannelOption.SO_KEEPALIVE, true)
				// 请求队列最大长度
				.option(ChannelOption.SO_BACKLOG, 1024)
				// websocket处理类
				.childHandler(websocketChannelInitializer);
	}

	/**
	 * 发送消息
	 * @param userId 用户ID
	 * @param msg 消息
	 */
	public void send(String userId, String msg) {
		Channel channel = USER_MAP.get(userId);
		if (channel != null) {
			channel.writeAndFlush(new TextWebSocketFrame(msg));
		}
	}

}

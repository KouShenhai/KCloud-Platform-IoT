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

package org.laokou.common.netty.config;

import io.netty.bootstrap.AbstractBootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 *
 * @author laokou
 */
public class TcpServer extends AbstractServer {

	public TcpServer(int port, ChannelInitializer<?> channelInitializer) {
		super(port, channelInitializer);
	}

	@Override
	protected AbstractBootstrap<?, ?> init() {
		// boss负责监听端口
		boss = new NioEventLoopGroup();
		// work负责线程读写
		work = new NioEventLoopGroup();
		// 配置引导
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		// 绑定线程组
		return serverBootstrap.group(boss, work)
			// 指定通道
			.channel(NioServerSocketChannel.class)
			// 开启TCP底层心跳，维持长连接
			.childOption(ChannelOption.SO_KEEPALIVE, true)
			.childOption(NioChannelOption.SO_KEEPALIVE, true)
			// 请求队列最大长度（如果连接建立频繁，服务器处理创建新连接较慢，可以适当调整参数）
			.option(ChannelOption.SO_BACKLOG, 2048)
			// 重复使用端口
			.option(NioChannelOption.SO_REUSEADDR, true)
			// 接收缓冲区大小
			.option(ChannelOption.SO_RCVBUF, 10 * 1024)
			// 发送缓冲区大小
			.option(ChannelOption.SO_SNDBUF, 10 * 1024)
			// 设置高低水位，防止占用大量内存
			.childOption(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(1024, 10 * 1024))
			// tcp处理类
			.childHandler(channelInitializer);
	}

	@Override
	public void send(String clientId, Object obj) {

	}

}

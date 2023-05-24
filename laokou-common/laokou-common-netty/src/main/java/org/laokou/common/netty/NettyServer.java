/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *   http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.common.netty;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.WriteBufferWaterMark;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author laokou
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class NettyServer implements Runnable {

	private String ip;
	private int port;

	@Override
	public void run() {
		// boss线程负责监听端口
		EventLoopGroup boss = new NioEventLoopGroup();
		// worker线程负责读写
		EventLoopGroup worker = new NioEventLoopGroup();
		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			// 设置线程池
			serverBootstrap.group(boss,worker);
			// 设置管道工厂（非阻塞，用来建立新的accept连接）
			serverBootstrap.channel(NioServerSocketChannel.class);
			// 请求队列的最大长度
			serverBootstrap.option(NioChannelOption.SO_BACKLOG, 512);
			// 维持长连接
			serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE,true);
			// 获取接收缓冲区大小
			serverBootstrap.option(ChannelOption.SO_RCVBUF, 1024 * 1024);
			// 设置缓冲区阈值
			serverBootstrap.childOption(ChannelOption.WRITE_BUFFER_WATER_MARK,new WriteBufferWaterMark(1024 * 1024,5 * 1024 * 1024));
			// 对work添加handler
			serverBootstrap.childHandler(null);
			// 绑定端口，等待启动
			ChannelFuture channelFuture = serverBootstrap.bind(this.ip, this.port).sync();
			// 监听端口关闭
			channelFuture.channel().closeFuture().sync();
		} catch (Exception e) {

		} finally {
			// 释放资源
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}
}

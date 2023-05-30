/**
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

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author laokou
 */
@Slf4j
public class WebSocketServer{

    private static final int PORT = 8088;

    public void start() {
        // boss负责监听端口
        EventLoopGroup boss = new NioEventLoopGroup();
        // work负责线程读写
        EventLoopGroup work = new NioEventLoopGroup();
        try {
            // 配置引导
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // 绑定线程组
            serverBootstrap.group(boss,work)
                    // 指定通道
                    .channel(NioServerSocketChannel.class)
                    // 心跳机制，默认7200
                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    // 请求队列最大长度
                    .option(ChannelOption.SO_BACKLOG,1024);
            // 服务器异步操作绑定
            ChannelFuture channelFuture = serverBootstrap.bind(PORT).sync();
            log.info("启动成功，端口：{}",PORT);
            // 监听端口关闭
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("绑定失败，端口：{}，错误信息:{}",PORT,e.getMessage());
        } finally {
            // 释放资源
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }
    }
}

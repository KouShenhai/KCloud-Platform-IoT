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

package org.laokou.common.netty.config;

import io.netty.bootstrap.AbstractBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import lombok.extern.slf4j.Slf4j;

/**
 * @author laokou
 */
@Slf4j
public abstract class Server {

    /**
     * 运行标记
     */
    protected boolean isRunning;
    protected EventLoopGroup boss;
    protected EventLoopGroup work;

    /**
     * 端口
     * @return int
     */
    protected abstract int getPort();

    /**
     * 初始化配置
     * @return AbstractBootstrap
     */
    protected abstract AbstractBootstrap<?,?> init();

    /**
     * 开始
     */
    public synchronized void start() {
        int port = getPort();
        AbstractBootstrap<?, ?> bootstrap = init();
        try {
            // 服务器异步操作绑定
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            log.info("启动成功，端口：{}",port);
            isRunning = true;
            // 监听端口关闭
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("启动失败，端口：{}，错误信息:{}",port,e.getMessage());
        } finally {
            stop();
        }
    }

    /**
     * 关闭
     */
    public synchronized void stop() {
        if (!isRunning) {
            log.info("未启动，不能关闭");
        }
        // 释放资源
        if (boss != null) {
            boss.shutdownGracefully();
        }
        if (work != null) {
            work.shutdownGracefully();
        }
        log.info("优雅关闭，释放资源");
    }

}

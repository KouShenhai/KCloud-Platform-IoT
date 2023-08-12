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

package org.laokou.common.netty.config;

import io.netty.bootstrap.AbstractBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author laokou
 */
@Slf4j
public abstract class AbstractServer implements Server {

	/**
	 * 运行标记
	 */
	private final AtomicBoolean RUNNING = new AtomicBoolean(false);

	protected EventLoopGroup boss;

	protected EventLoopGroup work;

	protected final int port;

	protected final String poolName;

	protected final ChannelInitializer<?> channelInitializer;

	protected final TaskExecutionProperties taskExecutionProperties;

	public AbstractServer(int port, String poolName, ChannelInitializer<?> channelInitializer,
			TaskExecutionProperties taskExecutionProperties) {
		this.port = port;
		this.poolName = poolName;
		this.channelInitializer = channelInitializer;
		this.taskExecutionProperties = taskExecutionProperties;
	}

	/**
	 * 初始化配置
	 * @return AbstractBootstrap
	 */
	protected abstract AbstractBootstrap<?, ?> init();

	/**
	 * 启动
	 */
	public void start() {
		if (RUNNING.get()) {
			log.error("已启动监听，端口：{}", port);
			return;
		}
		AbstractBootstrap<?, ?> bootstrap = init();
		try {
			// 服务器异步操作绑定
			// sync -> 等待任务结束，如果任务产生异常或被中断则抛出异常，否则返回Future自身
			// awaitUninterruptibly -> 等待任务结束，任务不可中断
			ChannelFuture channelFuture = bind(bootstrap, port);
			// 监听端口关闭
			channelFuture.channel().closeFuture().addListener(future -> {
				if (RUNNING.get()) {
					stop();
				}
			});
		}
		catch (Exception e) {
			log.error("启动失败，端口：{}，错误信息:{}", port, e.getMessage());
		}
	}

	/**
	 * 关闭
	 */
	public void stop() {
		if (RUNNING.compareAndSet(true, false)) {
			// 释放资源
			if (boss != null) {
				boss.shutdownGracefully();
			}
			if (work != null) {
				work.shutdownGracefully();
			}
			log.info("优雅关闭，释放资源");
		}
		else {
			log.error("关闭失败，请启动服务");
		}
	}

	/**
	 * 绑定
	 */
	private ChannelFuture bind(final AbstractBootstrap<?, ?> bootstrap, final int port) {
		return bootstrap.bind(port).awaitUninterruptibly().addListener(future -> {
			if (!future.isSuccess()) {
				log.error("启动失败，端口{}被占用", port);
				bind(bootstrap, port + 1);
			}
			else {
				log.info("启动成功，端口{}已绑定", port);
				RUNNING.compareAndSet(false, true);
			}
		});
	}

}

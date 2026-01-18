/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.websocket.config;

import io.netty.bootstrap.AbstractBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.util.ObjectUtils;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author laokou
 */
@Slf4j
public abstract class AbstractServer implements Server {

	protected final String ip;

	protected final int port;

	protected final ChannelHandler channelHandler;

	protected final int bossCorePoolSize;

	protected final int workerCorePoolSize;

	/**
	 * 完成初始化，但程序未启动完毕，其他线程结束程序，不能及时回收资源（对其他线程可见）.
	 */
	protected volatile EventLoopGroup boss;

	/**
	 * 完成初始化，但程序未启动完毕，其他线程结束程序，不能及时回收资源（对其他线程可见）.
	 */
	protected volatile EventLoopGroup worker;

	/**
	 * 运行标记.
	 */
	private final AtomicBoolean running = new AtomicBoolean(false);

	protected AbstractServer(String ip, int port, ChannelHandler channelHandler, int bossCorePoolSize,
			int workerCorePoolSize) {
		this.ip = ip;
		this.port = port;
		this.channelHandler = channelHandler;
		this.bossCorePoolSize = bossCorePoolSize;
		this.workerCorePoolSize = workerCorePoolSize;
	}

	/**
	 * 初始化配置.
	 * @return AbstractBootstrap
	 */
	protected abstract AbstractBootstrap<?, ?> init();

	/**
	 * 启动(Bean单例存在资源竞争).
	 */
	@Override
	public final void start() {
		if (running.get()) {
			log.error("已启动监听，端口：{}，运行状态【true已启动，false已停止】：{}", port, running.get());
			return;
		}
		// -Dnetty.server.parentgroup.size=2 -Dnetty.server.childgroup.size=32
		AbstractBootstrap<?, ?> serverBootstrap = init();
		try {
			// 服务器异步操作绑定
			// sync -> 等待任务结束，如果任务产生异常或被中断则抛出异常，否则返回Future自身
			// awaitUninterruptibly -> 等待任务结束，任务不可中断
			ChannelFuture channelFuture = bind(serverBootstrap, port);
			// 监听端口关闭
			channelFuture.channel().closeFuture().addListener(_ -> stop());
		}
		catch (Exception e) {
			log.error("启动失败，端口：{}，错误信息：{}", port, e.getMessage());
		}
	}

	/**
	 * 关闭(Bean单例存在资源竞争).
	 */
	@Override
	public final void stop() {
		if (running.compareAndExchange(true, false)) {
			// 释放资源
			if (ObjectUtils.isNotNull(boss)) {
				boss.shutdownGracefully();
			}
			if (ObjectUtils.isNotNull(worker)) {
				worker.shutdownGracefully();
			}
			log.info("优雅关闭，释放资源，运行状态【true已启动，false已停止】：{}", running.get());
		}
	}

	/**
	 * 是否正在运行.
	 * @return true表示正在运行，false表示已停止
	 */
	@Override
	public final boolean isRunning() {
		return running.get();
	}

	/**
	 * 绑定端口.
	 * @param bootstrap 启动类
	 * @param port 端口
	 */
	private ChannelFuture bind(final AbstractBootstrap<?, ?> bootstrap, final int port) {
		return bootstrap.bind(port).awaitUninterruptibly().addListener(future -> {
			if (!future.isSuccess()) {
				log.error("启动失败，端口{}被占用", port);
				bind(bootstrap, port + 1);
			}
			else {
				log.info("启动成功，端口{}已绑定，运行状态【true已启动，false已停止】：{}", port, running.getAndSet(true));
			}
		});
	}

}

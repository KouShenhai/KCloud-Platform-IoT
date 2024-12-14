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
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.i18n.utils.StringUtil;

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
	private volatile boolean running;

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
	public final synchronized void start() {
		if (running) {
			log.error("已启动监听，端口：{}", port);
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
			channelFuture.channel().closeFuture().addListener(future -> {
				if (running) {
					stop();
				}
			});
		}
		catch (Exception e) {
			log.error("启动失败，端口：{}，错误信息：{}，详情见日志", port, StringUtil.isEmpty(e.getMessage()) ? "暂无错误信息" : e.getMessage(), e);
		}
	}

	/**
	 * 关闭(Bean单例存在资源竞争).
	 */
	@Override
	public final synchronized void stop() {
		// 修改状态
		running = false;
		// 释放资源
		if (ObjectUtil.isNotNull(boss)) {
			boss.shutdownGracefully();
		}
		if (ObjectUtil.isNotNull(worker)) {
			worker.shutdownGracefully();
		}
		log.info("优雅关闭，释放资源");
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
				log.info("启动成功，端口{}已绑定", port);
				running = true;
			}
		});
	}

}

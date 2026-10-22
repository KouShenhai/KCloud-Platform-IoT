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

package org.laokou.common.mqtt.client;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.IoHandlerFactory;
import io.netty.channel.MultiThreadIoEventLoopGroup;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.uring.IoUring;
import io.netty.channel.uring.IoUringIoHandler;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.util.ThreadUtils;
import org.laokou.common.i18n.util.SystemUtils;

import java.util.concurrent.ExecutorService;

/**
 * @author laokou
 */
@Slf4j
@Getter
public final class MqttEventLoop implements AutoCloseable {

	private final ExecutorService executor;

	private final EventLoopGroup workerGroup;

	public MqttEventLoop(int ioThreads) {
		this.executor = ThreadUtils.newVirtualTaskExecutor();
		this.workerGroup = new MultiThreadIoEventLoopGroup(ioThreads, executor, getIoHandlerFactory());
	}

	@Override
	public void close() {
		workerGroup.shutdownGracefully().addListener(_ -> ThreadUtils.shutdown(executor, 30));
	}

	private IoHandlerFactory getIoHandlerFactory() {
		if (isIoUringAvailable()) {
			log.info("Use IoUringIoHandler factory");
			return IoUringIoHandler.newFactory();
		}
		log.info("Use NioIoHandler factory");
		return NioIoHandler.newFactory();
	}

	private boolean isIoUringAvailable() {
		if (SystemUtils.isLinux()) {
			return IoUring.isAvailable();
		}
		return false;
	}

}

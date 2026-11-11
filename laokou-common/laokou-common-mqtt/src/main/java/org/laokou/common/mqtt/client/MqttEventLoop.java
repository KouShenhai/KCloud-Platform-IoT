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
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollIoHandler;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.uring.IoUring;
import io.netty.channel.uring.IoUringIoHandler;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.util.SystemUtils;

/**
 * @author laokou
 */
@Slf4j
@Getter
public final class MqttEventLoop implements AutoCloseable {

	private final EventLoopGroup workerGroup;

	public MqttEventLoop() {
		this.workerGroup = new MultiThreadIoEventLoopGroup(16, getIoHandlerFactory());
	}

	private IoHandlerFactory getIoHandlerFactory() {
		if (SystemUtils.isLinux() && IoUring.isAvailable()) {
			log.info("Use IoUringIoHandler factory");
			return IoUringIoHandler.newFactory();
		}
		if (SystemUtils.isLinux() && Epoll.isAvailable()) {
			log.info("Use EpollIoHandler factory");
			return EpollIoHandler.newFactory();
		}
		log.info("Use NioIoHandler factory");
		return NioIoHandler.newFactory();
	}

	@Override
	public void close() {
		workerGroup.shutdownGracefully().addListener(_ -> log.info("MqttEventLoop shutdown gracefully"));
	}

}

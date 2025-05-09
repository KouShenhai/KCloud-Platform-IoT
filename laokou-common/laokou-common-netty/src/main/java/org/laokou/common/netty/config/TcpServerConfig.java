/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

import io.netty.channel.ChannelHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.UnorderedThreadPoolEventExecutor;
import org.springframework.context.annotation.Bean;

import java.util.List;

// @formatter:off
/**
 * @author laokou
 */
public class TcpServerConfig {

    @Bean(name = "tcpServerManager", initMethod = "start", destroyMethod = "stop")
	public TcpServerManager tcpServerManager(SpringTcpServerProperties springTcpServerProperties, List<ChannelHandler> channelHandlers) {
		TcpServerManager tcpServerManager = new TcpServerManager(springTcpServerProperties, channelHandlers);
		// 初始化
		tcpServerManager.initialize();
		return tcpServerManager;
    }

    @Bean
    public EventExecutorGroup tcpServerEventExecutorGroup(SpringTcpServerProperties springTcpServerProperties) {
        return new UnorderedThreadPoolEventExecutor(springTcpServerProperties.getGroupCorePoolSize(), new DefaultThreadFactory("tcpServerHandler"));
    }

}
// @formatter:on

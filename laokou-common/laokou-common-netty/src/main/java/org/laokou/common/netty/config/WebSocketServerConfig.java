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
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.UnorderedThreadPoolEventExecutor;
import org.laokou.common.core.config.VirtualThreadFactory;
import org.laokou.common.core.util.ThreadUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.util.Assert;

import java.util.List;

// @formatter:off
/**
 * @author laokou
 */
public class WebSocketServerConfig {

    @Bean(name = "webSocketServer", initMethod = "start", destroyMethod = "stop")
	public Server webSocketServer(List<ChannelHandler> channelHandlers, SpringWebSocketServerProperties springWebSocketServerProperties) {
		List<ChannelHandler> webSocketServerList = channelHandlers.stream().filter(item -> item.getClass().isAnnotationPresent(org.laokou.common.netty.annotation.WebSocketServer.class)).toList();
		Assert.noNullElements(webSocketServerList, "WebSocket Server not found");
		Assert.isTrue(webSocketServerList.size() == 1, "There must be only one WebSocket Server handler present");
		return new WebSocketServer(webSocketServerList.getFirst(), springWebSocketServerProperties, ThreadUtils.newVirtualTaskExecutor());
    }

    @Bean(name = "webSocketEventExecutorGroup")
    public EventExecutorGroup webSocketEventExecutorGroup(SpringWebSocketServerProperties springWebSocketServerProperties) {
        return new UnorderedThreadPoolEventExecutor(springWebSocketServerProperties.getGroupCorePoolSize(), VirtualThreadFactory.INSTANCE);
    }

}
// @formatter:on

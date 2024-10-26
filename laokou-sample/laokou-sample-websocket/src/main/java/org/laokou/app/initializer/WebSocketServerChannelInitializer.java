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

package org.laokou.app.initializer;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.concurrent.EventExecutorGroup;
import lombok.RequiredArgsConstructor;
import org.laokou.common.netty.annotation.WebSocketServer;
import org.laokou.common.netty.config.AbstractWebSocketServerChannelInitializer;
import org.laokou.common.netty.config.SpringWebSocketServerProperties;

/**
 * WebSocket处理类.
 *
 * @author laokou
 */
@WebSocketServer
@RequiredArgsConstructor
public class WebSocketServerChannelInitializer extends AbstractWebSocketServerChannelInitializer {

	private final ChannelInboundHandlerAdapter webSocketServerHandler;

	private final EventExecutorGroup webSocketEventExecutorGroup;

	private final SpringWebSocketServerProperties springWebSocketServerProperties;

	@Override
	protected void preHandler(ChannelPipeline pipeline) {

	}

	@Override
	protected void postHandler(ChannelPipeline pipeline) {
		// 业务处理
		pipeline.addLast(webSocketEventExecutorGroup, webSocketServerHandler);
	}

	@Override
	protected SpringWebSocketServerProperties getProperties() {
		return springWebSocketServerProperties;
	}

}

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

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;

/**
 * @author laokou
 */
public class WebSocketClient extends AbstractClient {

	public WebSocketClient(WebSocketProperties webSocketProperties, ChannelInitializer<?> channelInitializer) {
		super(webSocketProperties, channelInitializer);
	}

	@Override
	protected Bootstrap init() {
		Bootstrap bootstrap = new Bootstrap();
		client = new NioEventLoopGroup(1, new DefaultThreadFactory("client"));
		return bootstrap.group(client).channel(NioSocketChannel.class).handler(channelInitializer);
	}

}

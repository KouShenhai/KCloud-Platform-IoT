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
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;

@Slf4j
public abstract class AbstractClient implements Client {

	protected volatile EventLoopGroup client;

	protected Channel channel;

	protected final WebSocketProperties webSocketProperties;

	protected final ChannelInitializer<?> channelInitializer;

	protected AbstractClient(WebSocketProperties webSocketProperties, ChannelInitializer<?> channelInitializer) {
		this.webSocketProperties = webSocketProperties;
		this.channelInitializer = channelInitializer;
	}

	@Override
	@SneakyThrows
	public synchronized void open() {
		URI uri = URI.create(webSocketProperties.getClient().getUri());
		ChannelFuture future = init().connect(uri.getHost(), webSocketProperties.getPort(uri)).sync();
		this.channel = future.channel();
	}

	@Override
	public synchronized void close() {
		channel.close();
		client.shutdownGracefully();
	}

	@Override
	public void send(String key, String payload) {

	}

	protected abstract Bootstrap init();

}

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

package org.laokou.im.module.websocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class WebsocketChannelInitializer extends ChannelInitializer<NioSocketChannel> {

	private final WebsocketHandler websocketHandler;

	private static final String WEBSOCKET_PATH = "/ws";

	private static final int MAX_CONTENT_LENGTH = 655350;

	@Override
	protected void initChannel(NioSocketChannel channel) {
		ChannelPipeline pipeline = channel.pipeline();
		// 心跳检测
		pipeline.addLast(new IdleStateHandler(60, 0, 0, TimeUnit.SECONDS));
		// HTTP解码器
		pipeline.addLast(new HttpServerCodec());
		// 块状方式写入
		pipeline.addLast(new ChunkedWriteHandler());
		// 最大内容长度
		pipeline.addLast(new HttpObjectAggregator(MAX_CONTENT_LENGTH));
		// 自定义处理器
		pipeline.addLast(websocketHandler);
		// websocket协议
		pipeline.addLast(new WebSocketServerProtocolHandler(WEBSOCKET_PATH));
	}

}

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

package org.laokou.common.websocket.config;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.flush.FlushConsolidationHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;

/**
 * @author laokou
 */
@RequiredArgsConstructor
public abstract class AbstractWebSocketServerChannelInitializer extends AbstractChannelInitializer<NioSocketChannel> {

	protected final SpringWebSocketServerProperties springWebSocketServerProperties;

	// @formatter:off
	@Override
	protected void initChannel(NioSocketChannel channel) throws Exception {
		ChannelPipeline pipeline = channel.pipeline();
		// 前置处理
		preHandler(channel, pipeline);
		// HTTP解码器
		pipeline.addLast("httpServerCodec", new HttpServerCodec());
		// 块状方式写入
		pipeline.addLast("chunkedWriteHandler", new ChunkedWriteHandler());
		// 最大内容长度
		pipeline.addLast("httpObjectAggregator", new HttpObjectAggregator(springWebSocketServerProperties.getMaxContentLength()));
		// WebSocket协议
		pipeline.addLast("webSocketServerProtocolHandler", new WebSocketServerProtocolHandler(springWebSocketServerProperties.getWebsocketPath()));
		// 心跳检测
		pipeline.addLast("idleStateHandler", new IdleStateHandler(springWebSocketServerProperties.getReaderIdleTime(), springWebSocketServerProperties.getWriterIdleTime(), springWebSocketServerProperties.getAllIdleTime(), TimeUnit.SECONDS));
		// Flush合并
		pipeline.addLast("flushConsolidationHandler", new FlushConsolidationHandler(springWebSocketServerProperties.getExplicitFlushAfterFlushes(), springWebSocketServerProperties.isConsolidateWhenNoReadInProgress()));
		// 后置处理
		postHandler(channel, pipeline);
	}
	// @formatter:on

}

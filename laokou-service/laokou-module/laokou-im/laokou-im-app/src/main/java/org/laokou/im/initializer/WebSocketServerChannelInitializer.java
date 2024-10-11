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

package org.laokou.im.initializer;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.flush.FlushConsolidationHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.EventExecutorGroup;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * WebSocket处理类.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class WebSocketServerChannelInitializer extends ChannelInitializer<NioSocketChannel> {

	private final ChannelInboundHandlerAdapter webSocketServerHandler;

	private final EventExecutorGroup eventExecutorGroup;

	@Override
	@SneakyThrows
	protected void initChannel(NioSocketChannel channel) {
		ChannelPipeline pipeline = channel.pipeline();
		// HTTP解码器
		pipeline.addLast("httpServerCodec", new HttpServerCodec());
		// 数据压缩
		pipeline.addLast("webSocketServerCompressionHandler", new WebSocketServerCompressionHandler());
		// 块状方式写入
		pipeline.addLast("chunkedWriteHandler", new ChunkedWriteHandler());
		// 最大内容长度
		pipeline.addLast("httpObjectAggregator", new HttpObjectAggregator(65536));
		// WebSocket协议
		pipeline.addLast("webSocketServerProtocolHandler", new WebSocketServerProtocolHandler("/ws"));
        // 心跳检测
        pipeline.addLast("idleStateHandler", new IdleStateHandler(60, 0, 0, SECONDS));
		// flush合并
		pipeline.addLast("flushConsolidationHandler", new FlushConsolidationHandler(10, true));
		// 业务处理handler
		pipeline.addLast(eventExecutorGroup, webSocketServerHandler);
	}

}

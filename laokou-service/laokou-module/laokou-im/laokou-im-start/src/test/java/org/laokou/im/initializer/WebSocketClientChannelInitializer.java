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

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.flush.FlushConsolidationHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.SneakyThrows;
import org.laokou.common.netty.config.WebSocketProperties;
import org.laokou.im.handler.WebSocketClientHandler;

import java.net.URI;

public class WebSocketClientChannelInitializer extends ChannelInitializer<SocketChannel> {

	private final WebSocketProperties webSocketProperties;

	private final WebSocketClientHandler webSocketClientHandler;

	public WebSocketClientChannelInitializer(WebSocketProperties webSocketProperties,
			WebSocketClientHandler webSocketClientHandler) {
		this.webSocketProperties = webSocketProperties;
		this.webSocketClientHandler = webSocketClientHandler;
	}

	@Override
	protected void initChannel(SocketChannel channel) {
		ChannelPipeline pipeline = channel.pipeline();
		addSSL(channel);
		// 日志
		pipeline.addLast("loggingHandler", new LoggingHandler(LogLevel.INFO));
		// HTTP解码器
		pipeline.addLast("httpServerCodec", new HttpServerCodec());
		// 块状方式写入
		pipeline.addLast("chunkedWriteHandler", new ChunkedWriteHandler());
		// 最大内容长度
		pipeline.addLast("httpObjectAggregator", new HttpObjectAggregator(65536));
		// flush合并
		pipeline.addLast("flushConsolidationHandler", new FlushConsolidationHandler(10, true));
		// 业务处理
		pipeline.addLast("webSocketClientHandler", webSocketClientHandler);
	}

	@SneakyThrows
	private void addSSL(SocketChannel ch) {
		SslContext sslContext = SslContextBuilder.forClient()
			.trustManager(InsecureTrustManagerFactory.INSTANCE)
			.build();
		// SSL
		URI uri = URI.create(webSocketProperties.getClient().getUri());
		ch.pipeline().addLast(sslContext.newHandler(ch.alloc(), uri.getHost(), webSocketProperties.getPort(uri)));
	}

}

/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.laokou.common.core.utils.ResourceUtil;
import org.laokou.common.core.utils.SslUtil;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.server.Ssl;
import org.springframework.stereotype.Component;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import java.io.InputStream;
import java.security.KeyStore;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * WebSocket处理类.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class WebsocketChannelInitializer extends ChannelInitializer<NioSocketChannel> {

	@Schema(name = "WEBSOCKET_PATH", description = "WebSocket路径")
	private static final String WEBSOCKET_PATH = "/ws";

	private final WebsocketHandler websocketHandler;

	private final ServerProperties serverProperties;

	@Override
	@SneakyThrows
	protected void initChannel(NioSocketChannel channel) {
		ChannelPipeline pipeline = channel.pipeline();
		if (serverProperties.getSsl().isEnabled()) {
			SSLEngine sslEngine = sslContext().createSSLEngine();
			sslEngine.setNeedClientAuth(false);
			sslEngine.setUseClientMode(false);
			// TLS
			pipeline.addLast(new SslHandler(sslEngine));
		}
		// 心跳检测
		pipeline.addLast(new IdleStateHandler(60, 0, 0, SECONDS));
		// HTTP解码器
		pipeline.addLast(new HttpServerCodec());
		// 数据压缩
		pipeline.addLast(new WebSocketServerCompressionHandler());
		// 块状方式写入
		pipeline.addLast(new ChunkedWriteHandler());
		// 最大内容长度
		pipeline.addLast(new HttpObjectAggregator(65536));
		// websocket协议
		pipeline.addLast(new WebSocketServerProtocolHandler(WEBSOCKET_PATH));
		// 自定义处理器
		pipeline.addLast(websocketHandler);
	}

	@SneakyThrows
	private SSLContext sslContext() {
		Ssl ssl = serverProperties.getSsl();
		String path = ssl.getKeyStore();
		String password = ssl.getKeyStorePassword();
		String type = ssl.getKeyStoreType();
		try (InputStream inputStream = ResourceUtil.getResource(path).getInputStream()) {
			char[] passArray = password.toCharArray();
			KeyStore keyStore = KeyStore.getInstance(type);
			SSLContext sslContext = SSLContext.getInstance(SslUtil.TLS_PROTOCOL_VERSION);
			keyStore.load(inputStream, passArray);
			KeyManagerFactory keyManagerFactory = KeyManagerFactory
				.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			keyManagerFactory.init(keyStore, passArray);
			sslContext.init(keyManagerFactory.getKeyManagers(), null, null);
			return sslContext;
		}
	}

}

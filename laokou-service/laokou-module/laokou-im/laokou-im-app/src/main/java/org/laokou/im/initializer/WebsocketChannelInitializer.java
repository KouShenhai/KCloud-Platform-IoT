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
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.flush.FlushConsolidationHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.concurrent.EventExecutorGroup;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.i18n.utils.ResourceUtil;
import org.laokou.common.i18n.utils.SslUtil;
import org.laokou.im.handler.MetricHandler;
import org.laokou.im.handler.WebsocketIdleStateHandler;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.server.Ssl;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import java.io.InputStream;
import java.security.KeyStore;

/**
 * WebSocket处理类.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class WebsocketChannelInitializer extends ChannelInitializer<NioSocketChannel> {

	private final SimpleChannelInboundHandler<?> websocketHandler;

	private final ServerProperties serverProperties;

	private final MetricHandler metricHandler;

	private final EventExecutorGroup eventExecutorGroup;

	private final Environment environment;

	@Override
	@SneakyThrows
	protected void initChannel(NioSocketChannel channel) {
		ChannelPipeline pipeline = channel.pipeline();
		// SSL认证
		addSSL(pipeline);
		// 日志
		pipeline.addLast("loggingHandler", new LoggingHandler(getLogLevel()));
		// 心跳检测
		pipeline.addLast("websocketIdleStateHandler", new WebsocketIdleStateHandler());
		// HTTP解码器
		pipeline.addLast("httpServerCodec", new HttpServerCodec());
		// 数据压缩
		pipeline.addLast("websocketServerCompressionHandler", new WebSocketServerCompressionHandler());
		// 块状方式写入
		pipeline.addLast("chunkedWriteHandler", new ChunkedWriteHandler());
		// 最大内容长度
		pipeline.addLast("httpObjectAggregator", new HttpObjectAggregator(65536));
		// websocket协议
		pipeline.addLast("websocketServerProtocolHandler", new WebSocketServerProtocolHandler("/ws"));
		// 度量
		pipeline.addLast("metricHandler", metricHandler);
		// flush合并
		pipeline.addLast("flushConsolidationHandler", new FlushConsolidationHandler(10, true));
		// 业务处理handler
		pipeline.addLast(eventExecutorGroup, websocketHandler);
	}

	private LogLevel getLogLevel() {
		String env = environment.getProperty("spring.profiles.active", "test");
		if (ObjectUtil.equals("prod", env)) {
			return LogLevel.ERROR;
		}
		return LogLevel.INFO;
	}

	private void addSSL(ChannelPipeline pipeline) {
		if (serverProperties.getSsl().isEnabled()) {
			SSLEngine sslEngine = sslContext().createSSLEngine();
			sslEngine.setNeedClientAuth(false);
			sslEngine.setUseClientMode(false);
			// SSL
			pipeline.addLast(new SslHandler(sslEngine));
		}
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

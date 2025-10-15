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

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.common.i18n.util.ResourceExtUtils;
import org.springframework.boot.autoconfigure.web.ServerProperties;

import java.io.InputStream;

/**
 * WebSocket处理类.
 *
 * @author laokou
 */
public final class WebSocketServerChannelInitializer extends AbstractWebSocketServerChannelInitializer {

	private final ChannelHandler webSocketServerHandler;

	private final SslContext sslContext;

	private final ServerProperties serverProperties;

	public WebSocketServerChannelInitializer(SpringWebSocketServerProperties springWebSocketServerProperties,
			ChannelHandler webSocketServerHandler, ServerProperties serverProperties) throws Exception {
		super(springWebSocketServerProperties);
		this.webSocketServerHandler = webSocketServerHandler;
		this.serverProperties = serverProperties;
		this.sslContext = getSslContext();
	}

	@Override
	protected void preHandler(SocketChannel channel, ChannelPipeline pipeline) {
		if (ObjectUtils.isNotNull(sslContext)) {
			pipeline.addLast("sslHandler", new SslHandler(this.sslContext.newEngine(channel.alloc())));
		}
	}

	@Override
	protected void postHandler(SocketChannel channel, ChannelPipeline pipeline) {
		// 业务处理
		pipeline.addLast("webSocketServerHandler", webSocketServerHandler);
	}

	private SslContext getSslContext() throws Exception {
		if (serverProperties.getSsl().isEnabled()) {
			// @formatter:off
		// 生成证书 => openssl pkcs12 -in scg-keystore.p12 -clcerts -nokeys -out certificate.crt
		InputStream keyCertChainIn = ResourceExtUtils.getResource(springWebSocketServerProperties.getKeyCertChainPath()).getInputStream();
		// 生成私钥 => openssl pkcs12 -in scg-keystore.p12 -nocerts -out private.key
		InputStream keyCertPrivateKeyIn = ResourceExtUtils.getResource(springWebSocketServerProperties.getKeyCertPrivateKeyPath()).getInputStream();
		return SslContextBuilder.forServer(keyCertChainIn, keyCertPrivateKeyIn, springWebSocketServerProperties.getKeyCertPrivateKeyPassword())
			// 忽略SSL验证
			.trustManager(InsecureTrustManagerFactory.INSTANCE)
			.build();
		// @formatter:on
		}
		return null;
	}

}

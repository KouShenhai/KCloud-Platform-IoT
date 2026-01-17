/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

import com.redis.testcontainers.RedisStackContainer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.MultiThreadIoEventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketHandshakeException;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laokou.common.context.util.UserExtDetails;
import org.laokou.common.security.config.OAuth2OpaqueTokenIntrospector;
import org.laokou.common.testcontainers.util.DockerImageNames;
import org.mockito.Mockito;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.net.ServerSocket;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * WebSocket integration tests using real Netty WebSocket client and Redis Testcontainers.
 *
 * @author laokou
 */
class WebSocketServerIntegrationTest {

	private static final String TEST_HOST = "127.0.0.1";

	private static final String TEST_TOKEN_KEY = "oauth2:token:test-token";

	private static final String TEST_TOKEN = "test-token";

	private static RedisStackContainer redisContainer;

	private static StringRedisTemplate redisTemplate;

	private int testPort;

	private WebSocketServer server;

	private MultiThreadIoEventLoopGroup clientGroup;

	@BeforeAll
	static void startRedis() {
		// Start Redis Testcontainer
		redisContainer = new RedisStackContainer(DockerImageNames.redis("7.4"));
		redisContainer.start();

		// Create Redis connection factory
		RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
		config.setHostName(redisContainer.getHost());
		config.setPort(redisContainer.getFirstMappedPort());
		LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(config);
		connectionFactory.afterPropertiesSet();

		// Create RedisTemplate
		redisTemplate = new StringRedisTemplate(connectionFactory);
		redisTemplate.afterPropertiesSet();

		// Store test token in Redis
		redisTemplate.opsForValue().set(TEST_TOKEN_KEY, "{\"userId\":1,\"username\":\"testuser\"}");
	}

	@AfterAll
	static void stopRedis() {
		if (redisContainer != null) {
			redisContainer.stop();
		}
	}

	/**
	 * Find an available port dynamically.
	 */
	private static int findAvailablePort() {
		try (ServerSocket socket = new ServerSocket(0)) {
			socket.setReuseAddress(true);
			return socket.getLocalPort();
		}
		catch (Exception e) {
			throw new RuntimeException("Failed to find available port", e);
		}
	}

	@BeforeEach
	void setUp() throws Exception {
		testPort = findAvailablePort();

		// Create mock OAuth2 introspector that reads from Redis
		OAuth2OpaqueTokenIntrospector introspector = Mockito.mock(OAuth2OpaqueTokenIntrospector.class);
		UserExtDetails userDetails = Mockito.mock(UserExtDetails.class);
		Mockito.when(userDetails.getId()).thenReturn(1L);
		Mockito.when(userDetails.getName()).thenReturn("testuser");

		// Mock introspector to validate token from Redis
		Mockito.when(introspector.introspect(TEST_TOKEN)).thenAnswer(_ -> {
			String tokenValue = redisTemplate.opsForValue().get(TEST_TOKEN_KEY);
			if (tokenValue != null) {
				return userDetails;
			}
			throw new RuntimeException("Token not found in Redis");
		});

		// Setup properties
		SpringWebSocketServerProperties properties = new SpringWebSocketServerProperties();
		properties.setBindIp("0.0.0.0");
		properties.setPort(testPort);
		properties.setWebsocketPath("/ws");
		properties.setMaxContentLength(65536);
		properties.setReaderIdleTime(60);
		properties.setBossCorePoolSize(1);
		properties.setWorkerCorePoolSize(2);

		// Create handler
		WebSocketServerHandler handler = new WebSocketServerHandler(properties, introspector);

		// Create channel initializer
		TestWebSocketServerChannelInitializer initializer = new TestWebSocketServerChannelInitializer(properties,
				handler);

		// Create and start server
		server = new WebSocketServer(initializer, properties, Executors.newVirtualThreadPerTaskExecutor());
		Thread.ofVirtual().start(() -> server.start());

		// Wait for server to start
		Thread.sleep(1000);

		// Initialize client event loop
		clientGroup = new MultiThreadIoEventLoopGroup(1, NioIoHandler.newFactory());
	}

	@AfterEach
	void tearDown() {
		if (server != null) {
			server.stop();
		}
		if (clientGroup != null) {
			clientGroup.shutdownGracefully();
		}
	}

	@Test
	void test_websocket_handshake_successful() throws Exception {
		// Given
		WebSocketTestClient client = new WebSocketTestClient(testPort);

		// When
		client.connect();
		client.awaitHandshake();

		// Then
		Assertions.assertThat(client.isHandshakeComplete()).isTrue();

		// Cleanup
		client.close();
	}

	@Test
	void test_send_message_with_redis_token() throws Exception {
		// Given
		WebSocketTestClient client = new WebSocketTestClient(testPort);
		String message = "{\"token\":\"" + TEST_TOKEN + "\",\"type\":\"connect\",\"payload\":\"test\"}";

		// When
		client.connect();
		client.awaitHandshake();
		client.sendMessage(message);

		// Wait for processing
		Thread.sleep(500);

		// Then
		Assertions.assertThat(client.isHandshakeComplete()).isTrue();

		// Cleanup
		client.close();
	}

	@Test
	void test_client_disconnect() throws Exception {
		// Given
		WebSocketTestClient client = new WebSocketTestClient(testPort);

		// When
		client.connect();
		client.awaitHandshake();
		client.close();

		// Wait for close
		Thread.sleep(500);

		// Then
		Assertions.assertThat(client.isActive()).isFalse();
	}

	@Test
	void test_multiple_clients() throws Exception {
		// Given
		int clientCount = 3;
		List<WebSocketTestClient> clients = new ArrayList<>();

		// When
		for (int i = 0; i < clientCount; i++) {
			WebSocketTestClient client = new WebSocketTestClient(testPort);
			client.connect();
			client.awaitHandshake();
			clients.add(client);
		}

		// Then
		for (WebSocketTestClient client : clients) {
			Assertions.assertThat(client.isHandshakeComplete()).isTrue();
			Assertions.assertThat(client.isActive()).isTrue();
		}

		// Cleanup
		for (WebSocketTestClient client : clients) {
			client.close();
		}
	}

	/**
	 * Simple WebSocket test client wrapper.
	 */
	private class WebSocketTestClient {

		private final int port;

		private Channel channel;

		private WebSocketClientHandshaker handshaker;

		private volatile boolean handshakeComplete = false;

		private volatile ChannelPromise handshakeFuture;

		WebSocketTestClient(int port) {
			this.port = port;
		}

		void connect() throws Exception {
			handshaker = WebSocketClientHandshakerFactory.newHandshaker(
					new URI("ws://" + TEST_HOST + ":" + port + "/ws"), WebSocketVersion.V13, null, true,
					new DefaultHttpHeaders());

			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(clientGroup)
				.channel(NioSocketChannel.class)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) {
						ChannelPipeline pipeline = ch.pipeline();
						pipeline.addLast(new HttpClientCodec());
						pipeline.addLast(new HttpObjectAggregator(65536));
						pipeline.addLast(new ClientHandler());
					}
				});

			channel = bootstrap.connect(TEST_HOST, port).sync().channel();
		}

		void awaitHandshake() throws Exception {
			if (handshakeFuture != null) {
				handshakeFuture.await((long) 5, TimeUnit.SECONDS);
			}
		}

		void sendMessage(String message) {
			if (channel != null && channel.isActive()) {
				channel.writeAndFlush(new TextWebSocketFrame(message));
			}
		}

		void close() throws Exception {
			if (channel != null && channel.isActive()) {
				channel.writeAndFlush(new CloseWebSocketFrame());
				channel.closeFuture().await(5, TimeUnit.SECONDS);
			}
		}

		boolean isHandshakeComplete() {
			return handshakeComplete;
		}

		boolean isActive() {
			return channel != null && channel.isActive();
		}

		private class ClientHandler extends SimpleChannelInboundHandler<Object> {

			@Override
			public void handlerAdded(ChannelHandlerContext ctx) {
				handshakeFuture = ctx.newPromise();
			}

			@Override
			public void channelActive(ChannelHandlerContext ctx) {
				handshaker.handshake(ctx.channel());
			}

			@Override
			protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
				if (!handshaker.isHandshakeComplete()) {
					try {
						handshaker.finishHandshake(ctx.channel(), (FullHttpResponse) msg);
						handshakeComplete = true;
						handshakeFuture.setSuccess();
					}
					catch (WebSocketHandshakeException e) {
						handshakeFuture.setFailure(e);
					}
					return;
				}

				if (msg instanceof FullHttpResponse response) {
					throw new IllegalStateException("Unexpected FullHttpResponse (status=" + response.status()
							+ ", content=" + response.content().toString(StandardCharsets.UTF_8) + ')');
				}

				if (msg instanceof WebSocketFrame) {
					if (msg instanceof CloseWebSocketFrame) {
						ctx.channel().close();
					}
				}
			}

			@Override
			public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
				if (!handshakeFuture.isDone()) {
					handshakeFuture.setFailure(cause);
				}
				ctx.close();
			}

		}

	}

	/**
	 * Simplified channel initializer for testing (without SSL).
	 */
	private static class TestWebSocketServerChannelInitializer extends AbstractWebSocketServerChannelInitializer {

		private final WebSocketServerHandler webSocketServerHandler;

		TestWebSocketServerChannelInitializer(SpringWebSocketServerProperties properties,
				WebSocketServerHandler handler) {
			super(properties);
			this.webSocketServerHandler = handler;
		}

		@Override
		protected void preHandler(SocketChannel channel, ChannelPipeline pipeline) {
			// No SSL for testing
		}

		@Override
		protected void postHandler(SocketChannel channel, ChannelPipeline pipeline) {
			pipeline.addLast("webSocketServerHandler", webSocketServerHandler);
		}

	}

}

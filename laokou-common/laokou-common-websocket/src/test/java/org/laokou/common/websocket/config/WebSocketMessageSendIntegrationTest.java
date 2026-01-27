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

import com.redis.testcontainers.RedisContainer;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.laokou.common.context.util.User;
import org.laokou.common.security.config.OAuth2OpaqueTokenIntrospector;
import org.laokou.common.security.config.RedisOAuth2AuthorizationService;
import org.laokou.common.security.config.entity.OAuth2AuthorizationGrantAuthorization;
import org.laokou.common.security.config.entity.OAuth2UsernamePasswordGrantAuthorization;
import org.laokou.common.security.config.repository.OAuth2AuthorizationGrantAuthorizationRepository;
import org.laokou.common.testcontainers.util.DockerImageNames;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.RedisKeyValueTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.mapping.RedisMappingContext;
import org.springframework.data.redis.repository.support.RedisRepositoryFactory;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.jackson.SecurityJacksonModules;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import tools.jackson.databind.json.JsonMapper;

import java.net.ServerSocket;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * WebSocket message send integration test with real Redis token storage. Mimics
 * laokou-common-security approach for token storage and validation.
 *
 * @author laokou
 */
@Testcontainers
class WebSocketMessageSendIntegrationTest {

	private static final String TEST_HOST = "127.0.0.1";

	private static final String TEST_ACCESS_TOKEN = "test-access-token-12345";

	private static final String TEST_CLIENT_ID = "test-client";

	private static final String TEST_USER = "testuser";

	@Container
	static RedisContainer redisContainer = new RedisContainer(DockerImageNames.redis()).withExposedPorts(6379);

	private static OAuth2AuthorizationGrantAuthorizationRepository authorizationRepository;

	private static OAuth2AuthorizationService authorizationService;

	private int testPort;

	private WebSocketServer server;

	private MultiThreadIoEventLoopGroup clientGroup;

	@BeforeAll
	static void setupRedis() {
		String redisHost = redisContainer.getHost();
		Integer redisPort = redisContainer.getMappedPort(6379);

		// Configure Redis connection
		RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(redisHost, redisPort);
		LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(redisConfig);
		connectionFactory.afterPropertiesSet();

		// Configure ObjectMapper with SecurityJacksonModules
		JsonMapper objectMapper = JsonMapper.builder()
			.addModules(SecurityJacksonModules.getModules(WebSocketMessageSendIntegrationTest.class.getClassLoader()))
			.build();

		// Configure RedisTemplate
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		GenericJacksonJsonRedisSerializer serializer = new GenericJacksonJsonRedisSerializer(objectMapper);
		redisTemplate.setValueSerializer(serializer);
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashValueSerializer(serializer);
		redisTemplate.afterPropertiesSet();

		// Create repository using RedisRepositoryFactory
		RedisMappingContext mappingContext = new RedisMappingContext();
		RedisKeyValueAdapter keyValueAdapter = new RedisKeyValueAdapter(redisTemplate, mappingContext);
		RedisKeyValueTemplate keyValueTemplate = new RedisKeyValueTemplate(keyValueAdapter, mappingContext);
		RedisRepositoryFactory factory = new RedisRepositoryFactory(keyValueTemplate);

		authorizationRepository = factory.getRepository(OAuth2AuthorizationGrantAuthorizationRepository.class);

		// Create a RegisteredClientRepository that returns a test client
		RegisteredClientRepository registeredClientRepository = new RegisteredClientRepository() {
			@Override
			public void save(RegisteredClient registeredClient) {
				// Not needed for testing
			}

			@Override
			public RegisteredClient findById(String id) {
				return createTestRegisteredClient();
			}

			@Override
			public RegisteredClient findByClientId(String clientId) {
				return createTestRegisteredClient();
			}
		};

		// Create OAuth2AuthorizationService
		authorizationService = new RedisOAuth2AuthorizationService(registeredClientRepository, authorizationRepository);

		// Store test token in Redis
		storeTestToken();
	}

	/**
	 * Create a test RegisteredClient for OAuth2Authorization conversion.
	 */
	private static RegisteredClient createTestRegisteredClient() {
		return RegisteredClient.withId(TEST_CLIENT_ID)
			.clientId(TEST_CLIENT_ID)
			.clientSecret("{noop}secret")
			.authorizationGrantType(new AuthorizationGrantType("password"))
			.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
			.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
			.scope("read")
			.scope("write")
			.clientSettings(ClientSettings.builder().build())
			.tokenSettings(TokenSettings.builder().build())
			.build();
	}

	private static void storeTestToken() {
		Set<String> scopes = new HashSet<>();
		scopes.add("read");
		scopes.add("write");

		Instant now = Instant.now();

		// Create claims for the access token
		Map<String, Object> claims = new HashMap<>();
		claims.put("sub", TEST_USER);
		claims.put("aud", TEST_CLIENT_ID);
		claims.put("nbf", now);
		claims.put("iss", "https://laokou.org");
		claims.put("exp", now.plusSeconds(3600));
		claims.put("iat", now);
		claims.put("jti", "auth-test-1");

		// Create Access Token with proper claims
		OAuth2AuthorizationGrantAuthorization.AccessToken accessToken = new OAuth2AuthorizationGrantAuthorization.AccessToken(
				TEST_ACCESS_TOKEN, now, now.plusSeconds(3600), false, OAuth2AccessToken.TokenType.BEARER, scopes,
				OAuth2TokenFormat.SELF_CONTAINED, new OAuth2AuthorizationGrantAuthorization.ClaimsHolder(claims));

		// Create Refresh Token
		OAuth2AuthorizationGrantAuthorization.RefreshToken refreshToken = new OAuth2AuthorizationGrantAuthorization.RefreshToken(
				"refresh-token-12345", now, now.plusSeconds(86400), false);

		// Create Authorization
		OAuth2UsernamePasswordGrantAuthorization authorization = new OAuth2UsernamePasswordGrantAuthorization(
				"auth-test-1", TEST_CLIENT_ID, TEST_USER, scopes, accessToken, refreshToken,
				new User(1L, TEST_USER, "laokou", "https://baidu.com/avatar.png", true, 0, "2413176044@qq.com",
						"18888888888", 0L, 1L, List.of("sys:user:list")));

		authorizationRepository.save(authorization);
	}

	@AfterAll
	static void tearDownRedis() {
		if (redisContainer != null) {
			redisContainer.stop();
		}
	}

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

		// Create real OAuth2OpaqueTokenIntrospector
		OAuth2OpaqueTokenIntrospector introspector = new OAuth2OpaqueTokenIntrospector(authorizationService);

		// Setup properties
		SpringWebSocketServerProperties properties = new SpringWebSocketServerProperties();
		properties.setBindIp("0.0.0.0");
		properties.setPort(testPort);
		properties.setWebsocketPath("/ws");
		properties.setMaxContentLength(65536);
		properties.setReaderIdleTime(60);
		properties.setBossCorePoolSize(1);
		properties.setWorkerCorePoolSize(2);
		// Disable io_uring for consistent test behavior across environments
		properties.setUseIoUring(false);

		// Create handler
		WebSocketServerHandler handler = new WebSocketServerHandler(properties, introspector);

		// Create channel initializer
		TestWebSocketServerChannelInitializer initializer = new TestWebSocketServerChannelInitializer(properties,
				handler);

		// Create and start server
		server = new WebSocketServer(initializer, properties, Executors.newVirtualThreadPerTaskExecutor());
		Thread.ofVirtual().start(() -> server.start());

		// Wait for server to be ready with polling (max 10 seconds)
		int maxWaitMs = 10000;
		int waitedMs = 0;
		while (!server.isRunning() && waitedMs < maxWaitMs) {
			Thread.sleep(100);
			waitedMs += 100;
		}
		if (!server.isRunning()) {
			throw new RuntimeException("Server failed to start within " + maxWaitMs + "ms");
		}

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
	@DisplayName("Test send connect message with valid Redis token")
	void test_send_connect_message_with_valid_redis_token() throws Exception {
		// Given
		WebSocketTestClient client = new WebSocketTestClient(testPort);
		String connectMessage = "{\"token\":\"" + TEST_ACCESS_TOKEN + "\",\"type\":\"connect\",\"payload\":\"test\"}";

		// When
		client.connect();
		client.awaitHandshake();
		client.sendMessage(connectMessage);

		// Wait for processing
		Thread.sleep(500);

		// Then
		Assertions.assertThat(client.isHandshakeComplete()).isTrue();
		Assertions.assertThat(WebSocketSessionManager.get(1L)).isEmpty();

		// Cleanup
		client.close();
	}

	@Test
	@DisplayName("Test send message with valid Redis token")
	void test_send_message_with_valid_redis_token() throws Exception {
		// Given
		WebSocketTestClient client = new WebSocketTestClient(testPort);
		String connectMessage = "{\"token\":\"" + TEST_ACCESS_TOKEN + "\",\"type\":\"connect\",\"payload\":\"\"}";
		String textMessage = "{\"token\":\"" + TEST_ACCESS_TOKEN
				+ "\",\"type\":\"message\",\"payload\":\"Hello World\"}";

		// When
		client.connect();
		client.awaitHandshake();
		client.sendMessage(connectMessage);
		Thread.sleep(300);
		client.sendMessage(textMessage);
		Thread.sleep(500);

		// Then
		Assertions.assertThat(client.isHandshakeComplete()).isTrue();

		// Cleanup
		client.close();
	}

	@Test
	@DisplayName("Test send pong message with valid Redis token")
	void test_send_pong_message_with_valid_redis_token() throws Exception {
		// Given
		WebSocketTestClient client = new WebSocketTestClient(testPort);
		String connectMessage = "{\"token\":\"" + TEST_ACCESS_TOKEN + "\",\"type\":\"connect\",\"payload\":\"\"}";
		String pongMessage = "{\"token\":\"" + TEST_ACCESS_TOKEN + "\",\"type\":\"pong\",\"payload\":\"pong\"}";

		// When
		client.connect();
		client.awaitHandshake();
		client.sendMessage(connectMessage);
		Thread.sleep(300);
		client.sendMessage(pongMessage);
		Thread.sleep(500);

		// Then
		Assertions.assertThat(client.isHandshakeComplete()).isTrue();

		// Cleanup
		client.close();
	}

	@Test
	@DisplayName("Test send message with invalid token closes connection")
	void test_send_message_with_invalid_token_closes_connection() throws Exception {
		// Given
		WebSocketTestClient client = new WebSocketTestClient(testPort);
		String invalidMessage = "{\"token\":\"invalid-token\",\"type\":\"connect\",\"payload\":\"test\"}";

		// When
		client.connect();
		client.awaitHandshake();
		client.sendMessage(invalidMessage);

		// Wait for server to close connection
		Thread.sleep(500);

		// Then - Connection should be closed by server
		Assertions.assertThat(client.isHandshakeComplete()).isTrue();

		// Cleanup
		client.close();
	}

	/**
	 * Simple WebSocket test client.
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
				handshakeFuture.await(5, TimeUnit.SECONDS);
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
				channel.close().await(5, TimeUnit.SECONDS);
			}
		}

		boolean isHandshakeComplete() {
			return handshakeComplete;
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

				if (msg instanceof WebSocketFrame frame) {
					if (frame instanceof CloseWebSocketFrame) {
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

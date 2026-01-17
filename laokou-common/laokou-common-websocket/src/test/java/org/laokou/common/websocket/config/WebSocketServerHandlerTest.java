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

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleStateEvent;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.laokou.common.context.util.UserExtDetails;
import org.laokou.common.security.config.OAuth2OpaqueTokenIntrospector;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;

/**
 * WebSocketServerHandler unit tests.
 *
 * @author laokou
 */
class WebSocketServerHandlerTest {

	private WebSocketServerHandler handler;

	private OAuth2OpaqueTokenIntrospector introspector;

	private ChannelHandlerContext ctx;

	private Channel channel;

	private static final String TEST_CHANNEL_ID = "test-handler-channel-id";

	@BeforeEach
	void setUp() {
		SpringWebSocketServerProperties properties = new SpringWebSocketServerProperties();
		properties.setMaxHeartBeatCount(3);

		introspector = Mockito.mock(OAuth2OpaqueTokenIntrospector.class);
		handler = new WebSocketServerHandler(properties, introspector);

		ctx = Mockito.mock(ChannelHandlerContext.class);
		channel = Mockito.mock(Channel.class);
		ChannelId channelId = Mockito.mock(ChannelId.class);

		Mockito.when(ctx.channel()).thenReturn(channel);
		Mockito.when(channel.id()).thenReturn(channelId);
		Mockito.when(channelId.asLongText()).thenReturn(TEST_CHANNEL_ID);
	}

	@AfterEach
	void tearDown() {
		WebSocketSessionManager.remove(channel);
		WebSocketSessionHeartBeatManager.remove(TEST_CHANNEL_ID);
	}

	@Test
	void test_handlerAdded_logs_connection() {
		// When
		handler.handlerAdded(ctx);

		// Then - No exception should be thrown (logs connection)
		Mockito.verify(ctx).channel();
	}

	@Test
	void test_handlerRemoved_cleans_session() {
		// Given - Add a session first
		WebSocketSessionManager.add(1001L, channel);
		WebSocketSessionHeartBeatManager.get(TEST_CHANNEL_ID);

		// When
		handler.handlerRemoved(ctx);

		// Then - Session should be cleaned
		Assertions.assertThat(WebSocketSessionManager.get(1001L)).doesNotContain(channel);
	}

	@Test
	void test_channelRead_with_valid_token() {
		// Given
		UserExtDetails userDetails = Mockito.mock(UserExtDetails.class);
		Mockito.when(userDetails.getId()).thenReturn(1001L);
		Mockito.when(userDetails.getName()).thenReturn("testuser");
		Mockito.when(introspector.introspect("valid-token")).thenReturn(userDetails);

		String json = "{\"token\":\"valid-token\",\"type\":\"connect\",\"payload\":\"test\"}";
		TextWebSocketFrame frame = new TextWebSocketFrame(json);

		// When
		handler.channelRead(ctx, frame);

		// Then - Session should be added
		Assertions.assertThat(WebSocketSessionManager.get(1001L)).contains(channel);
	}

	@Test
	void test_channelRead_with_invalid_token_closes_connection() {
		// Given - Setup mock to throw exception for any token
		OAuth2Error error = new OAuth2Error("invalid_token", "Token is invalid", null);
		Mockito.doThrow(new OAuth2AuthenticationException(error)).when(introspector).introspect(Mockito.anyString());

		String json = "{\"token\":\"invalid-token\",\"type\":\"connect\",\"payload\":\"test\"}";
		TextWebSocketFrame frame = new TextWebSocketFrame(json);

		// When - Handler should catch the exception and close the connection
		handler.channelRead(ctx, frame);

		// Then - Connection should be closed
		Mockito.verify(ctx).close();
	}

	@Test
	void test_channelRead_passes_non_websocket_frame_to_next_handler() {
		// Given
		String nonWebSocketMessage = "plain text message";

		// When
		handler.channelRead(ctx, nonWebSocketMessage);

		// Then - Should pass to next handler
		Mockito.verify(ctx).fireChannelRead(nonWebSocketMessage);
	}

	@Test
	void test_userEventTriggered_idle_state_sends_ping() throws Exception {
		// Given
		WebSocketSessionHeartBeatManager.get(TEST_CHANNEL_ID); // Initialize
		IdleStateEvent idleEvent = IdleStateEvent.READER_IDLE_STATE_EVENT;

		// When
		handler.userEventTriggered(ctx, idleEvent);

		// Then - Ping should be sent
		ArgumentCaptor<TextWebSocketFrame> frameCaptor = ArgumentCaptor.forClass(TextWebSocketFrame.class);
		Mockito.verify(ctx).writeAndFlush(frameCaptor.capture());
		Assertions.assertThat(frameCaptor.getValue().text()).isEqualTo("ping");
	}

	@Test
	void test_userEventTriggered_exceeds_max_heartbeat_closes_connection() throws Exception {
		// Given
		WebSocketSessionHeartBeatManager.get(TEST_CHANNEL_ID);
		for (int i = 0; i < 3; i++) {
			WebSocketSessionHeartBeatManager.increment(TEST_CHANNEL_ID);
		}
		IdleStateEvent idleEvent = IdleStateEvent.READER_IDLE_STATE_EVENT;

		// When
		handler.userEventTriggered(ctx, idleEvent);

		// Then - Connection should be closed
		Mockito.verify(ctx).close();
	}

	@Test
	void test_channelRead_with_empty_message() {
		// Given
		TextWebSocketFrame frame = new TextWebSocketFrame("");

		// When
		handler.channelRead(ctx, frame);

		// Then - Should not throw exception and not interact with introspector
		Mockito.verifyNoInteractions(introspector);
	}

	@Test
	void test_channelRead_with_message_type() {
		// Given
		UserExtDetails userDetails = Mockito.mock(UserExtDetails.class);
		Mockito.when(userDetails.getId()).thenReturn(1002L);
		Mockito.when(userDetails.getName()).thenReturn("testuser");
		Mockito.when(introspector.introspect("valid-token")).thenReturn(userDetails);

		// First connect to establish session
		String connectJson = "{\"token\":\"valid-token\",\"type\":\"connect\",\"payload\":\"\"}";
		TextWebSocketFrame connectFrame = new TextWebSocketFrame(connectJson);
		handler.channelRead(ctx, connectFrame);

		// Then send message
		String messageJson = "{\"token\":\"valid-token\",\"type\":\"message\",\"payload\":\"Hello World\"}";
		TextWebSocketFrame messageFrame = new TextWebSocketFrame(messageJson);

		// When
		handler.channelRead(ctx, messageFrame);

		// Then - Token should be validated and session should exist
		Mockito.verify(introspector, Mockito.times(2)).introspect("valid-token");
		Assertions.assertThat(WebSocketSessionManager.get(1002L)).contains(channel);
	}

	@Test
	void test_channelRead_with_pong_type_resets_heartbeat() {
		// Given
		UserExtDetails userDetails = Mockito.mock(UserExtDetails.class);
		Mockito.when(userDetails.getId()).thenReturn(1003L);
		Mockito.when(userDetails.getName()).thenReturn("testuser");
		Mockito.when(introspector.introspect("valid-token")).thenReturn(userDetails);

		// Initialize heartbeat counter (get() creates the entry if not exists)
		Assertions.assertThat(WebSocketSessionHeartBeatManager.get(TEST_CHANNEL_ID)).isZero();
		// Then increment to simulate missed heartbeats
		WebSocketSessionHeartBeatManager.increment(TEST_CHANNEL_ID);
		WebSocketSessionHeartBeatManager.increment(TEST_CHANNEL_ID);
		Assertions.assertThat(WebSocketSessionHeartBeatManager.get(TEST_CHANNEL_ID)).isEqualTo(2);

		// Send pong message
		String pongJson = "{\"token\":\"valid-token\",\"type\":\"pong\",\"payload\":\"pong\"}";
		TextWebSocketFrame pongFrame = new TextWebSocketFrame(pongJson);

		// When
		handler.channelRead(ctx, pongFrame);

		// Then - Heartbeat counter should be reset to 0
		Assertions.assertThat(WebSocketSessionHeartBeatManager.get(TEST_CHANNEL_ID)).isEqualTo(0);
		Mockito.verify(introspector).introspect("valid-token");
	}

	@Test
	void test_channelRead_connect_and_receive_message_success() {
		// Given
		UserExtDetails userDetails = Mockito.mock(UserExtDetails.class);
		Mockito.when(userDetails.getId()).thenReturn(1004L);
		Mockito.when(userDetails.getName()).thenReturn("sender");
		Mockito.when(introspector.introspect("sender-token")).thenReturn(userDetails);

		// When - Connect
		String connectJson = "{\"token\":\"sender-token\",\"type\":\"connect\",\"payload\":\"\"}";
		TextWebSocketFrame connectFrame = new TextWebSocketFrame(connectJson);
		handler.channelRead(ctx, connectFrame);

		// Then - Session established
		Assertions.assertThat(WebSocketSessionManager.get(1004L)).contains(channel);

		// When - Send message
		String messageJson = "{\"token\":\"sender-token\",\"type\":\"message\",\"payload\":\"Test message content\"}";
		TextWebSocketFrame messageFrame = new TextWebSocketFrame(messageJson);
		handler.channelRead(ctx, messageFrame);

		// Then - Token validated twice (connect + message)
		Mockito.verify(introspector, Mockito.times(2)).introspect("sender-token");
	}

}

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
import io.netty.channel.ChannelId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Set;

/**
 * WebSocketSessionManager unit tests.
 *
 * @author laokou
 */
class WebSocketSessionManagerTest {

	private Channel mockChannel1;

	private Channel mockChannel2;

	@BeforeEach
	void setUp() {
		// Create mock channels
		mockChannel1 = Mockito.mock(Channel.class);
		mockChannel2 = Mockito.mock(Channel.class);
		ChannelId mockChannelId1 = Mockito.mock(ChannelId.class);
		ChannelId mockChannelId2 = Mockito.mock(ChannelId.class);

		Mockito.when(mockChannel1.id()).thenReturn(mockChannelId1);
		Mockito.when(mockChannel2.id()).thenReturn(mockChannelId2);
		Mockito.when(mockChannelId1.asLongText()).thenReturn("channel-id-1");
		Mockito.when(mockChannelId2.asLongText()).thenReturn("channel-id-2");
	}

	@AfterEach
	void tearDown() {
		// Clean up sessions
		WebSocketSessionManager.remove(mockChannel1);
		WebSocketSessionManager.remove(mockChannel2);
	}

	@Test
	void test_add_and_get_channel() {
		// Given
		Long clientId = 1001L;

		// When
		WebSocketSessionManager.add(clientId, mockChannel1);
		Set<Channel> channels = WebSocketSessionManager.get(clientId);

		// Then
		Assertions.assertThat(channels).isNotNull().hasSize(1).contains(mockChannel1);
		Assertions.assertThat(WebSocketSessionManager.getClientCount()).isGreaterThanOrEqualTo(1);
		Assertions.assertThat(WebSocketSessionManager.getChannelCount()).isGreaterThanOrEqualTo(1);
	}

	@Test
	void test_remove_channel() {
		// Given
		Long clientId = 1002L;
		WebSocketSessionManager.add(clientId, mockChannel1);

		// When
		WebSocketSessionManager.remove(mockChannel1);
		Set<Channel> channels = WebSocketSessionManager.get(clientId);

		// Then
		Assertions.assertThat(channels).isEmpty();
	}

	@Test
	void test_get_returns_empty_for_unknown_client() {
		// Given
		Long unknownClientId = 9999L;

		// When
		Set<Channel> channels = WebSocketSessionManager.get(unknownClientId);

		// Then
		Assertions.assertThat(channels).isNotNull().isEmpty();
	}

	@Test
	void test_multiple_channels_for_same_client() {
		// Given
		Long clientId = 1003L;

		// When
		WebSocketSessionManager.add(clientId, mockChannel1);
		WebSocketSessionManager.add(clientId, mockChannel2);
		Set<Channel> channels = WebSocketSessionManager.get(clientId);

		// Then
		Assertions.assertThat(channels).isNotNull().hasSize(2).contains(mockChannel1, mockChannel2);
	}

	@Test
	void test_remove_one_channel_keeps_others() {
		// Given
		Long clientId = 1004L;
		WebSocketSessionManager.add(clientId, mockChannel1);
		WebSocketSessionManager.add(clientId, mockChannel2);

		// When
		WebSocketSessionManager.remove(mockChannel1);
		Set<Channel> channels = WebSocketSessionManager.get(clientId);

		// Then
		Assertions.assertThat(channels).isNotNull().hasSize(1).contains(mockChannel2);
	}

}

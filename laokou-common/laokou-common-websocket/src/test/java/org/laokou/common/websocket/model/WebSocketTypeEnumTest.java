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

package org.laokou.common.websocket.model;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.laokou.common.context.util.UserExtDetails;
import org.laokou.common.i18n.common.exception.BizException;
import org.laokou.common.websocket.config.WebSocketSessionHeartBeatManager;
import org.laokou.common.websocket.config.WebSocketSessionManager;
import org.mockito.Mockito;

import java.util.Set;

/**
 * WebSocketTypeEnum unit tests.
 *
 * @author laokou
 */
class WebSocketTypeEnumTest {

	private static final String TEST_CHANNEL_ID = "test-channel-enum";

	@AfterEach
	void tearDown() {
		WebSocketSessionHeartBeatManager.remove(TEST_CHANNEL_ID);
	}

	@Test
	void test_getByCode_for_message() {
		// When
		WebSocketTypeEnum result = WebSocketTypeEnum.getByCode("message");

		// Then
		Assertions.assertThat(result).isEqualTo(WebSocketTypeEnum.MESSAGE);
		Assertions.assertThat(result.getCode()).isEqualTo("message");
		Assertions.assertThat(result.getDesc()).isEqualTo("消息");
	}

	@Test
	void test_getByCode_for_connect() {
		// When
		WebSocketTypeEnum result = WebSocketTypeEnum.getByCode("connect");

		// Then
		Assertions.assertThat(result).isEqualTo(WebSocketTypeEnum.CONNECT);
		Assertions.assertThat(result.getCode()).isEqualTo("connect");
		Assertions.assertThat(result.getDesc()).isEqualTo("建立连接");
	}

	@Test
	void test_getByCode_for_ping() {
		// When
		WebSocketTypeEnum result = WebSocketTypeEnum.getByCode("ping");

		// Then
		Assertions.assertThat(result).isEqualTo(WebSocketTypeEnum.PING);
		Assertions.assertThat(result.getCode()).isEqualTo("ping");
		Assertions.assertThat(result.getDesc()).isEqualTo("发送心跳");
	}

	@Test
	void test_getByCode_for_pong() {
		// When
		WebSocketTypeEnum result = WebSocketTypeEnum.getByCode("pong");

		// Then
		Assertions.assertThat(result).isEqualTo(WebSocketTypeEnum.PONG);
		Assertions.assertThat(result.getCode()).isEqualTo("pong");
		Assertions.assertThat(result.getDesc()).isEqualTo("心跳应答");
	}

	@Test
	void test_getByCode_throws_exception_for_unknown() {
		// When & Then
		Assertions.assertThatThrownBy(() -> WebSocketTypeEnum.getByCode("unknown"))
			.isInstanceOf(BizException.class)
			.hasMessageContaining("枚举类型不存在");
	}

	@Test
	void test_handle_for_connect_adds_session() {
		// Given
		UserExtDetails userDetails = Mockito.mock(UserExtDetails.class);
		Mockito.when(userDetails.getId()).thenReturn(1001L);

		Channel mockChannel = Mockito.mock(Channel.class);
		ChannelId mockChannelId = Mockito.mock(ChannelId.class);
		Mockito.when(mockChannel.id()).thenReturn(mockChannelId);
		Mockito.when(mockChannelId.asLongText()).thenReturn(TEST_CHANNEL_ID);

		WebSocketMessageCO message = new WebSocketMessageCO();
		message.setType("connect");

		// When
		WebSocketTypeEnum.CONNECT.handle(userDetails, message, mockChannel);

		// Then
		Set<Channel> channels = WebSocketSessionManager.get(1001L);
		Assertions.assertThat(channels).contains(mockChannel);

		// Cleanup
		WebSocketSessionManager.remove(mockChannel);
	}

	@Test
	void test_handle_for_pong_resets_heartbeat() {
		// Given
		UserExtDetails userDetails = Mockito.mock(UserExtDetails.class);
		Channel mockChannel = Mockito.mock(Channel.class);
		ChannelId mockChannelId = Mockito.mock(ChannelId.class);
		Mockito.when(mockChannel.id()).thenReturn(mockChannelId);
		Mockito.when(mockChannelId.asLongText()).thenReturn(TEST_CHANNEL_ID);

		// Initialize heartbeat count
		WebSocketSessionHeartBeatManager.get(TEST_CHANNEL_ID);
		WebSocketSessionHeartBeatManager.increment(TEST_CHANNEL_ID);
		WebSocketSessionHeartBeatManager.increment(TEST_CHANNEL_ID);

		WebSocketMessageCO message = new WebSocketMessageCO();
		message.setType("pong");
		message.setPayload("pong");

		// When
		WebSocketTypeEnum.PONG.handle(userDetails, message, mockChannel);

		// Then
		int count = WebSocketSessionHeartBeatManager.get(TEST_CHANNEL_ID);
		Assertions.assertThat(count).isZero();
	}

}

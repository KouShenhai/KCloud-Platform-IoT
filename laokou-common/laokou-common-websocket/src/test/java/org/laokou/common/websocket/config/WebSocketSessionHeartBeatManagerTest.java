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

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * WebSocketSessionHeartBeatManager unit tests.
 *
 * @author laokou
 */
class WebSocketSessionHeartBeatManagerTest {

	private static final String TEST_CHANNEL_ID = "test-channel-heartbeat";

	@AfterEach
	void tearDown() {
		WebSocketSessionHeartBeatManager.remove(TEST_CHANNEL_ID);
	}

	@Test
	void test_get_returns_zero_for_new_channel() {
		// Given
		String newChannelId = "new-channel-id";

		// When
		int count = WebSocketSessionHeartBeatManager.get(newChannelId);

		// Then
		Assertions.assertThat(count).isZero();

		// Cleanup
		WebSocketSessionHeartBeatManager.remove(newChannelId);
	}

	@Test
	void test_increment_heartbeat_count() {
		// Given
		WebSocketSessionHeartBeatManager.get(TEST_CHANNEL_ID); // Initialize

		// When
		WebSocketSessionHeartBeatManager.increment(TEST_CHANNEL_ID);
		WebSocketSessionHeartBeatManager.increment(TEST_CHANNEL_ID);
		WebSocketSessionHeartBeatManager.increment(TEST_CHANNEL_ID);
		int count = WebSocketSessionHeartBeatManager.get(TEST_CHANNEL_ID);

		// Then
		Assertions.assertThat(count).isEqualTo(3);
	}

	@Test
	void test_reset_heartbeat_count() {
		// Given
		WebSocketSessionHeartBeatManager.get(TEST_CHANNEL_ID); // Initialize
		WebSocketSessionHeartBeatManager.increment(TEST_CHANNEL_ID);
		WebSocketSessionHeartBeatManager.increment(TEST_CHANNEL_ID);

		// When
		WebSocketSessionHeartBeatManager.reset(TEST_CHANNEL_ID);
		int count = WebSocketSessionHeartBeatManager.get(TEST_CHANNEL_ID);

		// Then
		Assertions.assertThat(count).isZero();
	}

	@Test
	void test_remove_channel() {
		// Given
		String channelToRemove = "channel-to-remove";
		WebSocketSessionHeartBeatManager.get(channelToRemove); // Initialize
		WebSocketSessionHeartBeatManager.increment(channelToRemove);

		// When
		WebSocketSessionHeartBeatManager.remove(channelToRemove);
		int countAfterRemove = WebSocketSessionHeartBeatManager.get(channelToRemove);

		// Then
		Assertions.assertThat(countAfterRemove).isZero();

		// Cleanup
		WebSocketSessionHeartBeatManager.remove(channelToRemove);
	}

	@Test
	void test_multiple_channels_independent() {
		// Given
		String channel1 = "channel-1";
		String channel2 = "channel-2";
		WebSocketSessionHeartBeatManager.get(channel1);
		WebSocketSessionHeartBeatManager.get(channel2);

		// When
		WebSocketSessionHeartBeatManager.increment(channel1);
		WebSocketSessionHeartBeatManager.increment(channel1);
		WebSocketSessionHeartBeatManager.increment(channel2);

		// Then
		Assertions.assertThat(WebSocketSessionHeartBeatManager.get(channel1)).isEqualTo(2);
		Assertions.assertThat(WebSocketSessionHeartBeatManager.get(channel2)).isEqualTo(1);

		// Cleanup
		WebSocketSessionHeartBeatManager.remove(channel1);
		WebSocketSessionHeartBeatManager.remove(channel2);
	}

}

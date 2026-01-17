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

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.i18n.util.JacksonUtils;

/**
 * WebSocketMessageCO unit tests.
 *
 * @author laokou
 */
class WebSocketMessageCOTest {

	@Test
	void test_setter_getter() {
		// Given
		WebSocketMessageCO message = new WebSocketMessageCO();

		// When
		message.setToken("test-token-12345");
		message.setType("message");
		message.setPayload("Hello WebSocket");

		// Then
		Assertions.assertThat(message.getToken()).isEqualTo("test-token-12345");
		Assertions.assertThat(message.getType()).isEqualTo("message");
		Assertions.assertThat(message.getPayload()).isEqualTo("Hello WebSocket");
	}

	@Test
	void test_serialization_to_json() {
		// Given
		WebSocketMessageCO message = new WebSocketMessageCO();
		message.setToken("Bearer abc123");
		message.setType("connect");
		message.setPayload("connection payload");

		// When
		String json = JacksonUtils.toJsonStr(message);

		// Then
		Assertions.assertThat(json).isNotNull();
		Assertions.assertThat(json).contains("\"token\":\"Bearer abc123\"");
		Assertions.assertThat(json).contains("\"type\":\"connect\"");
		Assertions.assertThat(json).contains("\"payload\":\"connection payload\"");
	}

	@Test
	void test_deserialization_from_json() {
		// Given
		String json = "{\"token\":\"test-token\",\"type\":\"message\",\"payload\":\"test message\"}";

		// When
		WebSocketMessageCO message = JacksonUtils.toBean(json, WebSocketMessageCO.class);

		// Then
		Assertions.assertThat(message).isNotNull();
		Assertions.assertThat(message.getToken()).isEqualTo("test-token");
		Assertions.assertThat(message.getType()).isEqualTo("message");
		Assertions.assertThat(message.getPayload()).isEqualTo("test message");
	}

	@Test
	void test_serialization_roundtrip() {
		// Given
		WebSocketMessageCO original = new WebSocketMessageCO();
		original.setToken("roundtrip-token");
		original.setType("pong");
		original.setPayload("pong response");

		// When
		String json = JacksonUtils.toJsonStr(original);
		WebSocketMessageCO deserialized = JacksonUtils.toBean(json, WebSocketMessageCO.class);

		// Then
		Assertions.assertThat(deserialized.getToken()).isEqualTo(original.getToken());
		Assertions.assertThat(deserialized.getType()).isEqualTo(original.getType());
		Assertions.assertThat(deserialized.getPayload()).isEqualTo(original.getPayload());
	}

	@Test
	void test_null_fields() {
		// Given
		WebSocketMessageCO message = new WebSocketMessageCO();

		// Then
		Assertions.assertThat(message.getToken()).isNull();
		Assertions.assertThat(message.getType()).isNull();
		Assertions.assertThat(message.getPayload()).isNull();
	}

}

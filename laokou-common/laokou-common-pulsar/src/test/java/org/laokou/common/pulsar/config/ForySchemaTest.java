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

package org.laokou.common.pulsar.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.pulsar.common.schema.SchemaInfo;
import org.apache.pulsar.common.schema.SchemaType;
import org.apache.pulsar.shade.io.netty.buffer.ByteBuf;
import org.apache.pulsar.shade.io.netty.buffer.Unpooled;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.laokou.common.fory.config.ForyFactory;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * ForySchema test class.
 *
 * @author laokou
 */
class ForySchemaTest {

	private static ForySchema forySchema;

	@BeforeAll
	static void setup() {
		forySchema = ForySchema.INSTANCE;
		// Register test classes for Fory serialization
		ForyFactory.INSTANCE.register(TestPulsarMessage.class);
		ForyFactory.INSTANCE.register(TestPulsarEvent.class);
		ForyFactory.INSTANCE.register(ArrayList.class);
	}

	// ==================== Singleton Tests ====================

	@Test
	@DisplayName("Test ForySchema singleton instance")
	void testSingletonInstance() {
		ForySchema instance1 = ForySchema.INSTANCE;
		ForySchema instance2 = ForySchema.INSTANCE;

		Assertions.assertThat(instance1).isSameAs(instance2);
	}

	// ==================== Encode Tests ====================

	@Test
	@DisplayName("Test encode null object returns empty byte array")
	void testEncodeNull() {
		byte[] result = forySchema.encode(null);

		Assertions.assertThat(result).isEmpty();
	}

	@Test
	@DisplayName("Test encode string to UTF-8 bytes")
	void testEncodeString() {
		String testString = "Hello, Pulsar!";

		byte[] result = forySchema.encode(testString);

		Assertions.assertThat(result).isEqualTo(testString.getBytes(StandardCharsets.UTF_8));
	}

	@Test
	@DisplayName("Test encode empty string")
	void testEncodeEmptyString() {
		String emptyString = "";

		byte[] result = forySchema.encode(emptyString);

		Assertions.assertThat(result).isEmpty();
	}

	@Test
	@DisplayName("Test encode Chinese string")
	void testEncodeChineseString() {
		String chineseString = "你好，Pulsar！";

		byte[] result = forySchema.encode(chineseString);

		Assertions.assertThat(result).isEqualTo(chineseString.getBytes(StandardCharsets.UTF_8));
	}

	@Test
	@DisplayName("Test encode simple object")
	void testEncodeSimpleObject() {
		TestPulsarMessage message = new TestPulsarMessage(1L, "Test Content", 100);

		byte[] result = forySchema.encode(message);

		Assertions.assertThat(result).isNotEmpty();
	}

	@Test
	@DisplayName("Test encode complex object with nested list")
	void testEncodeComplexObject() {
		List<String> items = new ArrayList<>();
		items.add("Item1");
		items.add("Item2");
		TestPulsarEvent event = new TestPulsarEvent("EVT-001", "device-sensor", items);

		byte[] result = forySchema.encode(event);

		Assertions.assertThat(result).isNotEmpty();
	}

	// ==================== Decode Tests (byte array) ====================

	@Test
	@DisplayName("Test encode and decode simple object")
	void testEncodeAndDecodeSimpleObject() {
		TestPulsarMessage message = new TestPulsarMessage(1L, "Test Message", 50);

		byte[] encoded = forySchema.encode(message);
		Object decoded = forySchema.decode(encoded);

		Assertions.assertThat(decoded).isInstanceOf(TestPulsarMessage.class);

		TestPulsarMessage result = (TestPulsarMessage) decoded;
		Assertions.assertThat(result.getId()).isEqualTo(message.getId());
		Assertions.assertThat(result.getContent()).isEqualTo(message.getContent());
		Assertions.assertThat(result.getPriority()).isEqualTo(message.getPriority());
	}

	@Test
	@DisplayName("Test encode and decode complex object")
	void testEncodeAndDecodeComplexObject() {
		List<String> items = new ArrayList<>();
		items.add("Event A");
		items.add("Event B");
		items.add("Event C");
		TestPulsarEvent event = new TestPulsarEvent("EVT-002", "iot-device", items);

		byte[] encoded = forySchema.encode(event);
		Object decoded = forySchema.decode(encoded);

		Assertions.assertThat(decoded).isInstanceOf(TestPulsarEvent.class);

		TestPulsarEvent result = (TestPulsarEvent) decoded;
		Assertions.assertThat(result.getEventId()).isEqualTo(event.getEventId());
		Assertions.assertThat(result.getEventType()).isEqualTo(event.getEventType());
		Assertions.assertThat(result.getPayloads()).containsExactlyElementsOf(event.getPayloads());
	}

	@Test
	@DisplayName("Test encode and decode object with null fields")
	void testEncodeAndDecodeWithNullFields() {
		TestPulsarMessage message = new TestPulsarMessage(2L, null, 0);

		byte[] encoded = forySchema.encode(message);
		Object decoded = forySchema.decode(encoded);

		Assertions.assertThat(decoded).isInstanceOf(TestPulsarMessage.class);

		TestPulsarMessage result = (TestPulsarMessage) decoded;
		Assertions.assertThat(result.getId()).isEqualTo(2L);
		Assertions.assertThat(result.getContent()).isNull();
		Assertions.assertThat(result.getPriority()).isZero();
	}

	@Test
	@DisplayName("Test encode and decode object with empty list")
	void testEncodeAndDecodeWithEmptyList() {
		TestPulsarEvent event = new TestPulsarEvent("EVT-003", "test-event", new ArrayList<>());

		byte[] encoded = forySchema.encode(event);
		Object decoded = forySchema.decode(encoded);

		Assertions.assertThat(decoded).isInstanceOf(TestPulsarEvent.class);

		TestPulsarEvent result = (TestPulsarEvent) decoded;
		Assertions.assertThat(result.getEventId()).isEqualTo("EVT-003");
		Assertions.assertThat(result.getPayloads()).isEmpty();
	}

	// ==================== Decode Tests (ByteBuf) ====================

	@Test
	@DisplayName("Test decode from ByteBuf")
	void testDecodeFromByteBuf() {
		TestPulsarMessage message = new TestPulsarMessage(3L, "ByteBuf Test", 75);

		byte[] encoded = forySchema.encode(message);
		ByteBuf byteBuf = Unpooled.wrappedBuffer(encoded);

		Object decoded = forySchema.decode(byteBuf);

		Assertions.assertThat(decoded).isInstanceOf(TestPulsarMessage.class);

		TestPulsarMessage result = (TestPulsarMessage) decoded;
		Assertions.assertThat(result.getId()).isEqualTo(message.getId());
		Assertions.assertThat(result.getContent()).isEqualTo(message.getContent());
		Assertions.assertThat(result.getPriority()).isEqualTo(message.getPriority());
	}

	@Test
	@DisplayName("Test decode complex object from ByteBuf")
	void testDecodeComplexObjectFromByteBuf() {
		List<String> items = new ArrayList<>();
		items.add("Payload 1");
		items.add("Payload 2");
		TestPulsarEvent event = new TestPulsarEvent("EVT-BYTEBUF", "bytebuf-event", items);

		byte[] encoded = forySchema.encode(event);
		ByteBuf byteBuf = Unpooled.wrappedBuffer(encoded);

		Object decoded = forySchema.decode(byteBuf);

		Assertions.assertThat(decoded).isInstanceOf(TestPulsarEvent.class);

		TestPulsarEvent result = (TestPulsarEvent) decoded;
		Assertions.assertThat(result.getEventId()).isEqualTo(event.getEventId());
		Assertions.assertThat(result.getEventType()).isEqualTo(event.getEventType());
		Assertions.assertThat(result.getPayloads()).containsExactlyElementsOf(event.getPayloads());
	}

	// ==================== SchemaInfo Tests ====================

	@Test
	@DisplayName("Test getSchemaInfo returns correct schema info")
	void testGetSchemaInfo() {
		SchemaInfo schemaInfo = forySchema.getSchemaInfo();

		Assertions.assertThat(schemaInfo).isNotNull();
		Assertions.assertThat(schemaInfo.getName()).isEqualTo("Fory");
		Assertions.assertThat(schemaInfo.getType()).isEqualTo(SchemaType.BYTES);
		Assertions.assertThat(schemaInfo.getSchema()).isEmpty();
	}

	@Test
	@DisplayName("Test getSchemaInfo is consistent")
	void testGetSchemaInfoConsistency() {
		SchemaInfo schemaInfo1 = forySchema.getSchemaInfo();
		SchemaInfo schemaInfo2 = forySchema.getSchemaInfo();

		Assertions.assertThat(schemaInfo1.getName()).isEqualTo(schemaInfo2.getName());
		Assertions.assertThat(schemaInfo1.getType()).isEqualTo(schemaInfo2.getType());
	}

	// ==================== Test Classes ====================

	/**
	 * Test message class for Pulsar.
	 */
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	static class TestPulsarMessage implements Serializable {

		private Long id;

		private String content;

		private Integer priority;

	}

	/**
	 * Test event class for Pulsar.
	 */
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	static class TestPulsarEvent implements Serializable {

		private String eventId;

		private String eventType;

		private List<String> payloads;

	}

}

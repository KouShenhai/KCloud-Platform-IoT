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

package org.laokou.common.log4j2.config;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * TtlThreadContextMap test class.
 *
 * @author laokou
 */
class TtlThreadContextMapTest {

	private final TtlThreadContextMap contextMap = new TtlThreadContextMap();

	@AfterEach
	void tearDown() {
		contextMap.clear();
	}

	@Test
	@DisplayName("Test put and get key-value pairs")
	void testPutAndGet() {
		contextMap.put("key1", "value1");
		contextMap.put("key2", "value2");

		Assertions.assertThat(contextMap.get("key1")).isEqualTo("value1");
		Assertions.assertThat(contextMap.get("key2")).isEqualTo("value2");
	}

	@Test
	@DisplayName("Test get returns null for non-existent key")
	void testGetNonExistentKey() {
		Assertions.assertThat(contextMap.get("nonexistent")).isNull();
	}

	@Test
	@DisplayName("Test remove key")
	void testRemoveKey() {
		contextMap.put("key", "value");
		Assertions.assertThat(contextMap.get("key")).isEqualTo("value");

		contextMap.remove("key");
		Assertions.assertThat(contextMap.get("key")).isNull();
	}

	@Test
	@DisplayName("Test clear removes all values")
	void testClear() {
		contextMap.put("key1", "value1");
		contextMap.put("key2", "value2");

		contextMap.clear();

		Assertions.assertThat(contextMap.get("key1")).isNull();
		Assertions.assertThat(contextMap.get("key2")).isNull();
		Assertions.assertThat(contextMap.isEmpty()).isTrue();
	}

	@Test
	@DisplayName("Test containsKey")
	void testContainsKey() {
		contextMap.put("key", "value");

		Assertions.assertThat(contextMap.containsKey("key")).isTrue();
		Assertions.assertThat(contextMap.containsKey("nonexistent")).isFalse();
	}

	@Test
	@DisplayName("Test isEmpty")
	void testIsEmpty() {
		Assertions.assertThat(contextMap.isEmpty()).isTrue();

		contextMap.put("key", "value");
		Assertions.assertThat(contextMap.isEmpty()).isFalse();

		contextMap.clear();
		Assertions.assertThat(contextMap.isEmpty()).isTrue();
	}

	@Test
	@DisplayName("Test size")
	void testSize() {
		Assertions.assertThat(contextMap.size()).isZero();

		contextMap.put("key1", "value1");
		Assertions.assertThat(contextMap.size()).isEqualTo(1);

		contextMap.put("key2", "value2");
		Assertions.assertThat(contextMap.size()).isEqualTo(2);

		contextMap.remove("key1");
		Assertions.assertThat(contextMap.size()).isEqualTo(1);
	}

	@Test
	@DisplayName("Test getCopy returns mutable copy")
	void testGetCopy() {
		contextMap.put("key1", "value1");
		contextMap.put("key2", "value2");

		Map<String, String> copy = contextMap.getCopy();

		Assertions.assertThat(copy).containsEntry("key1", "value1");
		Assertions.assertThat(copy).containsEntry("key2", "value2");

		// Modifying copy should not affect original
		copy.put("key3", "value3");
		Assertions.assertThat(contextMap.get("key3")).isNull();
	}

	@Test
	@DisplayName("Test toMap returns map")
	void testToMap() {
		contextMap.put("key", "value");

		Map<String, String> map = contextMap.toMap();

		Assertions.assertThat(map).containsEntry("key", "value");
	}

	@Test
	@DisplayName("Test getValue")
	void testGetValue() {
		contextMap.put("key", "value");

		String value = contextMap.getValue("key");

		Assertions.assertThat(value).isEqualTo("value");
	}

	@Test
	@DisplayName("Test put overwrites existing value")
	void testPutOverwritesExistingValue() {
		contextMap.put("key", "value1");
		contextMap.put("key", "value2");

		Assertions.assertThat(contextMap.get("key")).isEqualTo("value2");
	}

	@Test
	@DisplayName("Test thread-local isolation with TTL")
	void testThreadLocalIsolation() {
		contextMap.put("mainKey", "mainValue");

		CompletableFuture.runAsync(() -> {
			contextMap.put("childKey", "childValue");
		}).join();

		// Main thread should have its value (TTL transmits to child)
		Assertions.assertThat(contextMap.get("mainKey")).isEqualTo("mainValue");
		// Child value should not be visible in parent
		Assertions.assertThat(contextMap.get("childKey")).isNull();
	}

	@Test
	@DisplayName("Test getImmutableMapOrNull")
	void testGetImmutableMapOrNull() {
		// Initially null when empty
		contextMap.clear();

		contextMap.put("key", "value");
		Map<String, String> immutableMap = contextMap.getImmutableMapOrNull();

		Assertions.assertThat(immutableMap).isNotNull();
		Assertions.assertThat(immutableMap).containsEntry("key", "value");
	}

	@Test
	@DisplayName("Test forEach with BiConsumer")
	void testForEachBiConsumer() {
		contextMap.put("key1", "value1");
		contextMap.put("key2", "value2");

		StringBuilder result = new StringBuilder();
		contextMap.forEach((key, value) -> result.append(key).append("=").append(value).append(";"));

		Assertions.assertThat(result.toString()).contains("key1=value1;");
		Assertions.assertThat(result.toString()).contains("key2=value2;");
	}

	@Test
	@DisplayName("Test toString")
	void testToString() {
		contextMap.put("key", "value");

		String str = contextMap.toString();

		Assertions.assertThat(str).contains("key");
		Assertions.assertThat(str).contains("value");
	}

	@Test
	@DisplayName("Test removeAll")
	void testRemoveAll() {
		contextMap.put("key1", "value1");
		contextMap.put("key2", "value2");
		contextMap.put("key3", "value3");

		contextMap.removeAll(java.util.List.of("key1", "key2"));

		Assertions.assertThat(contextMap.get("key1")).isNull();
		Assertions.assertThat(contextMap.get("key2")).isNull();
		Assertions.assertThat(contextMap.get("key3")).isEqualTo("value3");
	}

}

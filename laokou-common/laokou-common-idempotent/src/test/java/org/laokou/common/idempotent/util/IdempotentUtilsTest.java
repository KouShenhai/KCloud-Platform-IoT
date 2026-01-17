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

package org.laokou.common.idempotent.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * IdempotentUtils unit test class.
 *
 * @author laokou
 */
class IdempotentUtilsTest {

	private final IdempotentUtils idempotentUtils = new IdempotentUtils();

	@BeforeEach
	void setUp() {
		IdempotentUtils.cleanIdempotent();
	}

	@AfterEach
	void tearDown() {
		IdempotentUtils.cleanIdempotent();
	}

	@Test
	@DisplayName("Test isIdempotent returns false by default")
	void test_isIdempotent_returns_false_by_default() {
		// When
		boolean result = IdempotentUtils.isIdempotent();

		// Then
		Assertions.assertThat(result).isFalse();
	}

	@Test
	@DisplayName("Test openIdempotent sets status to true")
	void test_openIdempotent_sets_status_to_true() {
		// When
		IdempotentUtils.openIdempotent();
		boolean result = IdempotentUtils.isIdempotent();

		// Then
		Assertions.assertThat(result).isTrue();
	}

	@Test
	@DisplayName("Test cleanIdempotent clears status")
	void test_cleanIdempotent_clears_status() {
		// Given
		IdempotentUtils.openIdempotent();
		Assertions.assertThat(IdempotentUtils.isIdempotent()).isTrue();

		// When
		IdempotentUtils.cleanIdempotent();

		// Then
		Assertions.assertThat(IdempotentUtils.isIdempotent()).isFalse();
	}

	@Test
	@DisplayName("Test getRequestId returns empty map by default")
	void test_getRequestId_returns_empty_map_by_default() {
		// When
		Map<String, String> result = IdempotentUtils.getRequestId();

		// Then
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result).isEmpty();
	}

	@Test
	@DisplayName("Test getIdempotentKey generates valid UUID")
	void test_getIdempotentKey_generates_valid_uuid() {
		// When
		String key1 = idempotentUtils.getIdempotentKey();
		String key2 = idempotentUtils.getIdempotentKey();

		// Then
		Assertions.assertThat(key1).isNotNull();
		Assertions.assertThat(key1).isNotEmpty();
		Assertions.assertThat(key1).hasSize(32); // UUID without dashes
		Assertions.assertThat(key2).isNotNull();
		Assertions.assertThat(key1).isNotEqualTo(key2);
	}

	@Test
	@DisplayName("Test TransmittableThreadLocal propagates across threads")
	void test_transmittable_thread_local_propagation() {
		// Given
		IdempotentUtils.openIdempotent();
		AtomicBoolean childThreadResult = new AtomicBoolean(false);

		// When
		CompletableFuture.runAsync(() -> childThreadResult.set(IdempotentUtils.isIdempotent())).join();
		// Then
		Assertions.assertThat(childThreadResult.get()).isFalse();
	}

	@Test
	@DisplayName("Test multiple open and clean cycles")
	void test_multiple_open_and_clean_cycles() {
		// First cycle
		IdempotentUtils.openIdempotent();
		Assertions.assertThat(IdempotentUtils.isIdempotent()).isTrue();
		IdempotentUtils.cleanIdempotent();
		Assertions.assertThat(IdempotentUtils.isIdempotent()).isFalse();

		// Second cycle
		IdempotentUtils.openIdempotent();
		Assertions.assertThat(IdempotentUtils.isIdempotent()).isTrue();
		IdempotentUtils.cleanIdempotent();
		Assertions.assertThat(IdempotentUtils.isIdempotent()).isFalse();
	}

	@Test
	@DisplayName("Test cleanIdempotent when not opened does not throw exception")
	void test_cleanIdempotent_when_not_opened_does_not_throw() {
		// When & Then - should not throw
		Assertions.assertThatCode(IdempotentUtils::cleanIdempotent).doesNotThrowAnyException();
	}

}

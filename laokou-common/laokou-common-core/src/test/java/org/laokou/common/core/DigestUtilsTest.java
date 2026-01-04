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

package org.laokou.common.core;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.core.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * @author laokou
 */
class DigestUtilsTest {

	@Test
	void test_digest_default_sha512() {
		// Given
		String input = "test data";
		byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);

		// When
		byte[] result = DigestUtils.digest(inputBytes);

		// Then
		Assertions.assertThat(result).isNotNull().hasSize(64); // SHA-512 produces 64
																// bytes
	}

	@Test
	void test_digest_with_sha256() {
		// Given
		String input = "test data";
		byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);

		// When
		byte[] result = DigestUtils.digest("SHA-256", inputBytes);

		// Then
		Assertions.assertThat(result).isNotNull().hasSize(32); // SHA-256 produces 32
																// bytes
	}

	@Test
	void test_digest_with_sha512() {
		// Given
		String input = "test data";
		byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);

		// When
		byte[] result = DigestUtils.digest("SHA-512", inputBytes);

		// Then
		Assertions.assertThat(result).isNotNull().hasSize(64); // SHA-512 produces 64
																// bytes
	}

	@Test
	void test_digest_with_md5() {
		// Given
		String input = "test data";
		byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);

		// When
		byte[] result = DigestUtils.digest("MD5", inputBytes);

		// Then
		Assertions.assertThat(result).isNotNull().hasSize(16); // MD5 produces 16 bytes
	}

	@Test
	void test_digest_empty_input() {
		// Given
		byte[] emptyInput = new byte[0];

		// When
		byte[] result = DigestUtils.digest(emptyInput);

		// Then
		Assertions.assertThat(result).isNotNull().hasSize(64); // SHA-512 produces 64
																// bytes even for empty
																// input
	}

	@Test
	void test_digest_consistency() {
		// Given
		String input = "consistent data";
		byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);

		// When
		byte[] result1 = DigestUtils.digest(inputBytes);
		byte[] result2 = DigestUtils.digest(inputBytes);

		// Then
		Assertions.assertThat(result1).isEqualTo(result2); // Same input should produce
															// same hash
	}

	@Test
	void test_digest_different_inputs_produce_different_hashes() {
		// Given
		String input1 = "data1";
		String input2 = "data2";
		byte[] inputBytes1 = input1.getBytes(StandardCharsets.UTF_8);
		byte[] inputBytes2 = input2.getBytes(StandardCharsets.UTF_8);

		// When
		byte[] result1 = DigestUtils.digest(inputBytes1);
		byte[] result2 = DigestUtils.digest(inputBytes2);

		// Then
		Assertions.assertThat(result1).isNotEqualTo(result2); // Different inputs should
																// produce different
																// hashes
	}

	@Test
	void test_digest_same_inputs_produce_different_hashes() {
		// Given
		String input1 = "data";
		String input2 = "data";
		byte[] inputBytes1 = input1.getBytes(StandardCharsets.UTF_8);
		byte[] inputBytes2 = input2.getBytes(StandardCharsets.UTF_8);

		// When
		byte[] result1 = DigestUtils.digest(inputBytes1);
		byte[] result2 = DigestUtils.digest(inputBytes2);

		// Then
		Assertions.assertThat(result1).isEqualTo(result2); // Different inputs should
															// produce different hashes
	}

	@Test
	void test_digest_with_invalid_algorithm() {
		// Given
		String input = "test data";
		byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);

		// When & Then
		Assertions.assertThatThrownBy(() -> DigestUtils.digest("INVALID-ALGORITHM", inputBytes))
			.isInstanceOf(IllegalStateException.class)
			.hasMessageContaining("Could not find MessageDigest with algorithm \"INVALID-ALGORITHM\"");
	}

	@Test
	void test_digest_with_large_input() {
		// Given
		byte[] largeInput = new byte[1024 * 1024]; // 1MB
		for (int i = 0; i < largeInput.length; i++) {
			largeInput[i] = (byte) (i % 256);
		}

		// When
		byte[] result = DigestUtils.digest(largeInput);

		// Then
		Assertions.assertThat(result).isNotNull().hasSize(64); // SHA-512 produces 64
																// bytes
	}

	@Test
	void test_digest_with_special_characters() {
		// Given
		String input = "特殊字符测试!@#$%^&*()_+-=[]{}|;':\",./<>?";
		byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);

		// When
		byte[] result = DigestUtils.digest(inputBytes);

		// Then
		Assertions.assertThat(result).isNotNull().hasSize(64); // SHA-512 produces 64
																// bytes
	}

}

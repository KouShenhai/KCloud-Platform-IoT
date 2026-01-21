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
import org.laokou.common.core.util.RandomIdGenerator;

/**
 * @author laokou
 */
class RandomIdGeneratorTest {

	@Test
	void test_generateRandomId_withDefaultLength_returns16CharString() {
		String id = RandomIdGenerator.generateRandomId();
		Assertions.assertThat(id).isNotBlank();
		Assertions.assertThat(id.length()).isEqualTo(16);
	}

	@Test
	void test_generateRandomId_withCustomLength_returnsCorrectLength() {
		Assertions.assertThat(RandomIdGenerator.generateRandomId(8).length()).isEqualTo(8);
		Assertions.assertThat(RandomIdGenerator.generateRandomId(32).length()).isEqualTo(32);
		Assertions.assertThat(RandomIdGenerator.generateRandomId(1).length()).isEqualTo(1);
	}

	@Test
	void test_generateRandomId_multipleInvocations_returnsUniqueIds() {
		String id1 = RandomIdGenerator.generateRandomId();
		String id2 = RandomIdGenerator.generateRandomId();
		String id3 = RandomIdGenerator.generateRandomId();
		Assertions.assertThat(id1).isNotEqualTo(id2);
		Assertions.assertThat(id2).isNotEqualTo(id3);
		Assertions.assertThat(id1).isNotEqualTo(id3);
	}

	@Test
	void test_generateBase64RandomId_withDefaultLength_returnsValidString() {
		String id = RandomIdGenerator.generateBase64RandomId();
		Assertions.assertThat(id).isNotBlank();
	}

	@Test
	void test_generateBase64RandomId_withCustomLength_returnsValidString() {
		String id = RandomIdGenerator.generateBase64RandomId(16);
		Assertions.assertThat(id).isNotBlank();
	}

}

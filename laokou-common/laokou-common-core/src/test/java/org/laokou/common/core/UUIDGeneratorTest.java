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
import org.laokou.common.core.util.UUIDGenerator;

/**
 * @author laokou
 */
class UUIDGeneratorTest {

	@Test
	void test_generateUUID_returnsNonBlankString() {
		Assertions.assertThat(UUIDGenerator.generateUUID()).isNotBlank();
	}

	@Test
	void test_generateUUID_returns32CharString() {
		String uuid = UUIDGenerator.generateUUID();
		Assertions.assertThat(uuid.length()).isEqualTo(32);
	}

	@Test
	void test_generateUUID_containsNoHyphens() {
		String uuid = UUIDGenerator.generateUUID();
		Assertions.assertThat(uuid).doesNotContain("-");
	}

	@Test
	void test_generateUUID_returnsValidHexString() {
		String uuid = UUIDGenerator.generateUUID();
		Assertions.assertThat(uuid.matches("[a-f0-9]{32}")).isTrue();
	}

	@Test
	void test_generateUUID_multipleInvocations_returnsUniqueIds() {
		String uuid1 = UUIDGenerator.generateUUID();
		String uuid2 = UUIDGenerator.generateUUID();
		String uuid3 = UUIDGenerator.generateUUID();
		Assertions.assertThat(uuid1).isNotEqualTo(uuid2);
		Assertions.assertThat(uuid2).isNotEqualTo(uuid3);
		Assertions.assertThat(uuid1).isNotEqualTo(uuid3);
	}

}

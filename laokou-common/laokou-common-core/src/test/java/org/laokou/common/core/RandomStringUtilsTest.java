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
import org.laokou.common.core.util.RandomStringUtils;

/**
 * @author laokou
 */
class RandomStringUtilsTest {

	@Test
	void test_randomNumeric_withDefaultLength_returns6CharString() {
		Assertions.assertThat(RandomStringUtils.randomNumeric()).isNotBlank();
		Assertions.assertThat(RandomStringUtils.randomNumeric().length()).isEqualTo(6);
	}

	@Test
	void test_randomNumeric_withCustomLength_returnsCorrectLength() {
		Assertions.assertThat(RandomStringUtils.randomNumeric(7).length()).isEqualTo(7);
		Assertions.assertThat(RandomStringUtils.randomNumeric(10).length()).isEqualTo(10);
	}

	@Test
	void test_randomNumeric_returnsOnlyDigits() {
		String result = RandomStringUtils.randomNumeric();
		Assertions.assertThat(result.matches("\\d+")).isTrue();
	}

	@Test
	void test_randomNumeric_withZeroLength_throwsException() {
		Assertions.assertThatThrownBy(() -> RandomStringUtils.randomNumeric(0))
			.isInstanceOf(org.laokou.common.i18n.common.exception.SystemException.class);
	}

	@Test
	void test_randomNumeric_withNegativeLength_throwsException() {
		Assertions.assertThatThrownBy(() -> RandomStringUtils.randomNumeric(-1))
			.isInstanceOf(org.laokou.common.i18n.common.exception.SystemException.class);
	}

}

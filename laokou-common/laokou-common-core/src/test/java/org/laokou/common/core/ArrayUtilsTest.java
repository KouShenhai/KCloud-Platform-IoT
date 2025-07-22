/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

import org.junit.jupiter.api.Test;
import org.laokou.common.core.util.ArrayUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author laokou
 */
class ArrayUtilsTest {

	@Test
	void testByteArray() {
		byte[] bytes = { 1 };
		assertThat(ArrayUtils.isNotEmpty(bytes)).isTrue();
		assertThat(ArrayUtils.isEmpty(bytes)).isFalse();
	}

	@Test
	void testStrArray() {
		String[] str = { "1" };
		assertThat(ArrayUtils.isNotEmpty(str)).isTrue();
		assertThat(ArrayUtils.isEmpty(str)).isFalse();
	}

	@Test
	void testObjArray() {
		Object[] obj = { "1" };
		assertThat(ArrayUtils.isNotEmpty(obj)).isTrue();
		assertThat(ArrayUtils.isEmpty(obj)).isFalse();
	}

}

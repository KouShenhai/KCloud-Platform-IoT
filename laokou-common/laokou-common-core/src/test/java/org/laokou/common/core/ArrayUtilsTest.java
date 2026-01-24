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
import org.laokou.common.core.util.ArrayUtils;

/**
 * @author laokou
 */
class ArrayUtilsTest {

	@Test
	void test_isNotEmpty_withByteArray_returnsTrue() {
		byte[] bytes = { 1 };
		Assertions.assertThat(ArrayUtils.isNotEmpty(bytes)).isTrue();
		Assertions.assertThat(ArrayUtils.isEmpty(bytes)).isFalse();
	}

	@Test
	void test_isNotEmpty_withStringArray_returnsTrue() {
		String[] str = { "1" };
		Assertions.assertThat(ArrayUtils.isNotEmpty(str)).isTrue();
		Assertions.assertThat(ArrayUtils.isEmpty(str)).isFalse();
	}

	@Test
	void test_isNotEmpty_withObjectArray_returnsTrue() {
		Object[] obj = { "1" };
		Assertions.assertThat(ArrayUtils.isNotEmpty(obj)).isTrue();
		Assertions.assertThat(ArrayUtils.isEmpty(obj)).isFalse();
	}

	@Test
	void test_isEmpty_withEmptyByteArray_returnsTrue() {
		byte[] emptyBytes = new byte[0];
		Assertions.assertThat(ArrayUtils.isEmpty(emptyBytes)).isTrue();
		Assertions.assertThat(ArrayUtils.isNotEmpty(emptyBytes)).isFalse();
	}

	@Test
	void test_isEmpty_withNullByteArray_returnsTrue() {
		byte[] nullBytes = null;
		Assertions.assertThat(ArrayUtils.isEmpty(nullBytes)).isTrue();
		Assertions.assertThat(ArrayUtils.isNotEmpty(nullBytes)).isFalse();
	}

	@Test
	void test_isEmpty_withNullObjectArray_returnsTrue() {
		Object[] nullArray = null;
		Assertions.assertThat(ArrayUtils.isEmpty(nullArray)).isTrue();
		Assertions.assertThat(ArrayUtils.isNotEmpty(nullArray)).isFalse();
	}

	@Test
	void test_isEmpty_withEmptyObjectArray_returnsTrue() {
		Object[] emptyArray = new Object[0];
		Assertions.assertThat(ArrayUtils.isEmpty(emptyArray)).isTrue();
		Assertions.assertThat(ArrayUtils.isNotEmpty(emptyArray)).isFalse();
	}

}

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

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.core.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * CollectionUtils测试类.
 *
 * @author laokou
 */
class CollectionUtilsTest {

	@Test
	void test_isNotEmpty() {
		Assertions.assertThat(CollectionUtils.isNotEmpty(Arrays.asList("1", "2", "3"))).isTrue();
		Assertions.assertThat(CollectionUtils.isNotEmpty(new ArrayList<>())).isFalse();
		Assertions.assertThat(CollectionUtils.isNotEmpty(null)).isFalse();
	}

	@Test
	void test_isEmpty() {
		Assertions.assertThat(CollectionUtils.isEmpty(Arrays.asList("1", "2", "3"))).isFalse();
		Assertions.assertThat(CollectionUtils.isEmpty(new ArrayList<>())).isTrue();
		Assertions.assertThat(CollectionUtils.isEmpty(null)).isTrue();
	}

	@Test
	void test_toStr() {
		Assertions.assertThat(CollectionUtils.toStr(Arrays.asList("a", "b", "c"), ",")).isEqualTo("a,b,c");
		Assertions.assertThat(CollectionUtils.toStr(new ArrayList<>(), ",")).isEqualTo("");
		Assertions.assertThat(CollectionUtils.toStr(Arrays.asList("a", null, "c"), ",")).isEqualTo("a,c");
	}

	@Test
	void test_toList() {
		Assertions.assertThat(CollectionUtils.toList("a,b,c", ",")).isEqualTo(Arrays.asList("a", "b", "c"));
		Assertions.assertThat(CollectionUtils.toList("", ",").isEmpty()).isTrue();
		Assertions.assertThat(CollectionUtils.toList(null, ",").isEmpty()).isTrue();
		Assertions.assertThat(CollectionUtils.toList("a, b , c", ",")).isEqualTo(Arrays.asList("a", "b", "c"));
	}

	@Test
	void test_contains() {
		Assertions.assertThat(CollectionUtils.contains(Arrays.asList("a", "b", "c"), "a")).isTrue();
		Assertions.assertThat(CollectionUtils.contains(Arrays.asList("a", "b", "c"), "d")).isFalse();
	}

	@Test
	void test_anyMatch() {
		Assertions.assertThat(CollectionUtils.anyMatch(Arrays.asList("a", "b", "c"), Arrays.asList("c", "d", "e")))
			.isTrue();
		Assertions.assertThat(CollectionUtils.anyMatch(Arrays.asList("c", "d", "e"), Arrays.asList("x", "y", "z")))
			.isFalse();
	}

	@Test
	void test_containsAll() {
		Assertions.assertThat(CollectionUtils.containsAll(Arrays.asList("a", "b"), Arrays.asList("a", "b", "c")))
			.isTrue();
		Assertions.assertThat(CollectionUtils.containsAll(Arrays.asList("x", "y"), Arrays.asList("a", "b", "c")))
			.isFalse();
	}

}

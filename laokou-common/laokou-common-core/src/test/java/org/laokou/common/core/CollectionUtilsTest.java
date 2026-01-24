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
import org.laokou.common.core.util.CollectionExtUtils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * CollectionUtils测试类.
 *
 * @author laokou
 */
class CollectionUtilsTest {

	@Test
	void test_isNotEmpty_withNonEmptyCollection_returnsTrue() {
		Assertions.assertThat(CollectionExtUtils.isNotEmpty(Arrays.asList("1", "2", "3"))).isTrue();
		Assertions.assertThat(CollectionExtUtils.isNotEmpty(new ArrayList<>())).isFalse();
		Assertions.assertThat(CollectionExtUtils.isNotEmpty(null)).isFalse();
	}

	@Test
	void test_isEmpty_withEmptyCollection_returnsTrue() {
		Assertions.assertThat(CollectionExtUtils.isEmpty(Arrays.asList("1", "2", "3"))).isFalse();
		Assertions.assertThat(CollectionExtUtils.isEmpty(new ArrayList<>())).isTrue();
	}

	@Test
	void test_toStr_withListAndDelimiter_returnsJoinedString() {
		Assertions.assertThat(CollectionExtUtils.toStr(Arrays.asList("a", "b", "c"), ",")).isEqualTo("a,b,c");
		Assertions.assertThat(CollectionExtUtils.toStr(new ArrayList<>(), ",")).isEqualTo("");
		Assertions.assertThat(CollectionExtUtils.toStr(Arrays.asList("a", null, "c"), ",")).isEqualTo("a,c");
	}

	@Test
	void test_toList_withStringAndDelimiter_returnsSplitList() {
		Assertions.assertThat(CollectionExtUtils.toList("a,b,c", ",")).isEqualTo(Arrays.asList("a", "b", "c"));
		Assertions.assertThat(CollectionExtUtils.toList("", ",")).hasSize(0);
		Assertions.assertThat(CollectionExtUtils.toList(null, ",")).hasSize(0);
		Assertions.assertThat(CollectionExtUtils.toList("a, b , c", ",")).isEqualTo(Arrays.asList("a", "b", "c"));
	}

	@Test
	void test_contains_withExistingElement_returnsTrue() {
		Assertions.assertThat(CollectionExtUtils.contains(Arrays.asList("a", "b", "c"), "a")).isTrue();
		Assertions.assertThat(CollectionExtUtils.contains(Arrays.asList("a", "b", "c"), "d")).isFalse();
	}

	@Test
	void test_anyMatch_withMatchingElements_returnsTrue() {
		Assertions.assertThat(CollectionExtUtils.anyMatch(Arrays.asList("a", "b", "c"), Arrays.asList("c", "d", "e")))
			.isTrue();
		Assertions.assertThat(CollectionExtUtils.anyMatch(Arrays.asList("c", "d", "e"), Arrays.asList("x", "y", "z")))
			.isFalse();
	}

	@Test
	void test_containsAll_withAllMatchingElements_returnsTrue() {
		Assertions.assertThat(CollectionExtUtils.containsAll(Arrays.asList("a", "b"), Arrays.asList("a", "b", "c")))
			.isTrue();
		Assertions.assertThat(CollectionExtUtils.containsAll(Arrays.asList("x", "y"), Arrays.asList("a", "b", "c")))
			.isFalse();
	}

}

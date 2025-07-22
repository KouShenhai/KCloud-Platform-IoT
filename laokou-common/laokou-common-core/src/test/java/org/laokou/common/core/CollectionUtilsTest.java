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
import org.laokou.common.core.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * CollectionUtils测试类.
 *
 * @author laokou
 */
class CollectionUtilsTest {

	@Test
	void testIsNotEmpty() {
		assertThat(CollectionUtils.isNotEmpty(Arrays.asList("1", "2", "3"))).isTrue();
		assertThat(CollectionUtils.isNotEmpty(new ArrayList<>())).isFalse();
		assertThat(CollectionUtils.isNotEmpty(null)).isFalse();
	}

	@Test
	void testIsEmpty() {
		assertThat(CollectionUtils.isEmpty(Arrays.asList("1", "2", "3"))).isFalse();
		assertThat(CollectionUtils.isEmpty(new ArrayList<>())).isTrue();
		assertThat(CollectionUtils.isEmpty(null)).isTrue();
	}

	@Test
	void testToStr() {
		assertThat(CollectionUtils.toStr(Arrays.asList("a", "b", "c"), ",")).isEqualTo("a,b,c");
		assertThat(CollectionUtils.toStr(new ArrayList<>(), ",")).isEqualTo("");
		assertThat(CollectionUtils.toStr(Arrays.asList("a", null, "c"), ",")).isEqualTo("a,c");
	}

	@Test
	void testToList() {
		assertThat(CollectionUtils.toList("a,b,c", ",")).isEqualTo(Arrays.asList("a", "b", "c"));
		assertThat(CollectionUtils.toList("", ",").isEmpty()).isTrue();
		assertThat(CollectionUtils.toList(null, ",").isEmpty()).isTrue();
		assertThat(CollectionUtils.toList("a, b , c", ",")).isEqualTo(Arrays.asList("a", "b", "c"));
	}

	@Test
	void testContains() {
		assertThat(CollectionUtils.contains(Arrays.asList("a", "b", "c"), "a")).isTrue();
		assertThat(CollectionUtils.contains(Arrays.asList("a", "b", "c"), "d")).isFalse();
	}

	@Test
	void testAnyMatch() {
		assertThat(CollectionUtils.anyMatch(Arrays.asList("a", "b", "c"), Arrays.asList("c", "d", "e"))).isTrue();
		assertThat(CollectionUtils.anyMatch(Arrays.asList("c", "d", "e"), Arrays.asList("x", "y", "z"))).isFalse();
	}

	@Test
	void testContainsAll() {
		assertThat(CollectionUtils.containsAll(Arrays.asList("a", "b"), Arrays.asList("a", "b", "c"))).isTrue();
		assertThat(CollectionUtils.containsAll(Arrays.asList("x", "y"), Arrays.asList("a", "b", "c"))).isFalse();
	}

}

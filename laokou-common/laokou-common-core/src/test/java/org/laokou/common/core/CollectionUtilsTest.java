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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
		assertEquals(Arrays.asList("a", "b", "c"), CollectionUtils.toList("a,b,c", ","));
		// 测试空字符串
		assertTrue(CollectionUtils.toList("", ",").isEmpty());

		// 测试null
		assertTrue(CollectionUtils.toList(null, ",").isEmpty());

		// 测试带空格的字符串
		String strWithSpaces = "a, b , c";
		List<String> expectedTrimmed = Arrays.asList("a", "b", "c");
		assertEquals(expectedTrimmed, CollectionUtils.toList(strWithSpaces, ","));
	}

	@Test
	void testContains() {
		List<String> list = Arrays.asList("a", "b", "c");
		assertTrue(CollectionUtils.contains(list, "a"));
		assertFalse(CollectionUtils.contains(list, "d"));
	}

	@Test
	void testAnyMatch() {
		List<String> list1 = Arrays.asList("a", "b", "c");
		List<String> list2 = Arrays.asList("c", "d", "e");
		assertTrue(CollectionUtils.anyMatch(list1, list2));

		List<String> list3 = Arrays.asList("x", "y", "z");
		assertFalse(CollectionUtils.anyMatch(list1, list3));
	}

	@Test
	void testContainsAll() {
		List<String> subList = Arrays.asList("a", "b");
		List<String> fullList = Arrays.asList("a", "b", "c");
		assertTrue(CollectionUtils.containsAll(subList, fullList));

		List<String> differentList = Arrays.asList("x", "y");
		assertFalse(CollectionUtils.containsAll(differentList, fullList));
	}

}

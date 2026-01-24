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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.core.util.ConvertUtils;

import java.io.Serializable;
import java.util.List;

/**
 * @author laokou
 */
class ConvertUtilsTest {

	@Test
	void test_sourceToTarget_withValidObject_returnsConvertedObject() {
		User testUser = new User(1L, "laokou");
		User user = ConvertUtils.sourceToTarget(testUser, User.class);
		Assertions.assertThat(user).isNotNull();
		Assertions.assertThat(user.getId()).isEqualTo(testUser.getId());
		Assertions.assertThat(user.getName()).isEqualTo(testUser.getName());
	}

	@Test
	void test_sourceToTarget_withNullSource_returnsNull() {
		User user = ConvertUtils.sourceToTarget((Object) null, User.class);
		Assertions.assertThat(user).isNull();
	}

	@Test
	void test_sourceToTarget_withCollection_returnsConvertedList() {
		User testUser1 = new User(1L, "laokou");
		User testUser2 = new User(2L, "老寇");
		List<User> userList = ConvertUtils.sourceToTarget(List.of(testUser1, testUser2), User.class);
		Assertions.assertThat(userList).hasSize(2);
		Assertions.assertThat(userList.getFirst().getId()).isEqualTo(testUser1.getId());
		Assertions.assertThat(userList.get(1).getName()).isEqualTo(testUser2.getName());
	}

	@Test
	void test_sourceToTarget_withEmptyCollection_returnsEmptyList() {
		List<User> userList = ConvertUtils.sourceToTarget(List.of(), User.class);
		Assertions.assertThat(userList).isEmpty();
	}

	@Test
	void test_sourceToTarget_withNullCollection_returnsEmptyList() {
		List<User> userList = ConvertUtils.sourceToTarget((java.util.Collection<User>) null, User.class);
		Assertions.assertThat(userList).isEmpty();
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	static class User implements Serializable {

		private Long id;

		private String name;

	}

}

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

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.core.util.ConvertUtils;

import java.io.Serializable;
import java.util.List;

/**
 * @author laokou
 */
class ConvertUtilsTest {

	@Test
	void test() {
		TestUser testUser = new TestUser(1L, "laokou");
		User user = ConvertUtils.sourceToTarget(testUser, User.class);
		Assertions.assertNotNull(user);
		Assertions.assertEquals(testUser.getId(), user.getId());
		Assertions.assertEquals(testUser.getName(), user.getName());
		TestUser testUser2 = new TestUser(2L, "老寇");
		List<User> userList = ConvertUtils.sourceToTarget(List.of(testUser2), User.class);
		Assertions.assertEquals(1, userList.size());
		Assertions.assertEquals(testUser2.getId(), userList.getFirst().getId());
		Assertions.assertEquals(testUser2.getName(), userList.getFirst().getName());
	}

	@Data
	static class User implements Serializable {

		private Long id;

		private String name;

	}

}

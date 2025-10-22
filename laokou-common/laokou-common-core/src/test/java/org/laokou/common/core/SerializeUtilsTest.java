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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.core.util.SerializeUtils;

import java.io.Serializable;

/**
 * @author laokou
 */
class SerializeUtilsTest {

	@Test
	void test() {
		User user = new User(1L, "laokou");
		Assertions.assertThat(SerializeUtils.deserialize(SerializeUtils.serialize(user)))
			.isInstanceOf(User.class)
			.isEqualTo(user);
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	private static class User implements Serializable {

		private Long id;

		private String name;

	}

}

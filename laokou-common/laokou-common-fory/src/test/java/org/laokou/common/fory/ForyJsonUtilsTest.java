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

package org.laokou.common.fory;

import lombok.Data;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.fory.util.ForyJsonUtils;

/**
 * @author laokou
 */
class ForyJsonUtilsTest {

	@Test
	void test() {
		TestUser testUser = new TestUser();
		testUser.setId(1L);
		testUser.setName("laokou");
		Assertions.assertThat(ForyJsonUtils.toJsonStr(testUser)).isEqualTo("{\"id\":1,\"name\":\"laokou\"}");
		String str = ForyJsonUtils.toJsonStr(testUser);
		Assertions.assertThat(ForyJsonUtils.toBean(str, TestUser.class)).isEqualTo(testUser);
		Assertions.assertThat(ForyJsonUtils.toBean(str.getBytes(), TestUser.class)).isEqualTo(testUser);
	}

	@Data
	static class TestUser {

		private Long id;

		private String name;

	}

}

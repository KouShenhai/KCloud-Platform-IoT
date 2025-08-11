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

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.laokou.common.core.util.GsonUtils;
import org.laokou.common.i18n.util.JacksonUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author laokou
 */
class GsonUtilsTest {

	@Test
	void test_gson() throws JsonProcessingException {
		TestUser user = new TestUser(1L, "laokou");
		String json = GsonUtils.toPrettyFormat(user);
		assertThat(json).isEqualTo("""
				{
				  "id": 1,
				  "name": "laokou"
				}""");
		TestUser testUser = JacksonUtils.toBean(json, TestUser.class);
		assertThat(testUser.getId()).isEqualTo(1L);
		assertThat(testUser.getName()).isEqualTo("laokou");
	}

}

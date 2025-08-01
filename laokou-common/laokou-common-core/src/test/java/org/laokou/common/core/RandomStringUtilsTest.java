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
import org.laokou.common.core.util.RandomStringUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author laokou
 */
class RandomStringUtilsTest {

	@Test
	void testRandomNumeric() {
		assertThat(RandomStringUtils.randomNumeric(6)).isNotBlank();
		assertThat(RandomStringUtils.randomNumeric()).isNotBlank();
		// 验证长度正确
		assertThat(RandomStringUtils.randomNumeric().length()).isEqualTo(6);
		assertThat(RandomStringUtils.randomNumeric(7).length()).isEqualTo(7);
		// 验证字符串仅包含数字
		assertThat(RandomStringUtils.randomNumeric().matches("\\d+")).isTrue();
	}

}

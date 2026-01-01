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
import org.laokou.common.core.util.RandomStringUtils;

/**
 * @author laokou
 */
class RandomStringUtilsTest {

	@Test
	void test_randomNumeric() {
		Assertions.assertThat(RandomStringUtils.randomNumeric(6)).isNotBlank();
		Assertions.assertThat(RandomStringUtils.randomNumeric()).isNotBlank();
		// 验证长度正确
		Assertions.assertThat(RandomStringUtils.randomNumeric().length()).isEqualTo(6);
		Assertions.assertThat(RandomStringUtils.randomNumeric(7).length()).isEqualTo(7);
		// 验证字符串仅包含数字
		Assertions.assertThat(RandomStringUtils.randomNumeric().matches("\\d+")).isTrue();
	}

}

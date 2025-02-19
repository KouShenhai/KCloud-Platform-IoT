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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.core.utils.RandomStringUtil;

/**
 * @author laokou
 */
class RandomStringUtilTest {

	@Test
	void testRandomNumeric() {
		Assertions.assertNotNull(RandomStringUtil.randomNumeric(6));
		Assertions.assertNotNull(RandomStringUtil.randomNumeric());
		// 验证长度正确
		Assertions.assertEquals(6, RandomStringUtil.randomNumeric().length());
		Assertions.assertEquals(7, RandomStringUtil.randomNumeric(7).length());
		// 验证字符串仅包含数字
		Assertions.assertTrue(RandomStringUtil.randomNumeric().matches("\\d+"), "生成的字符串应仅包含数字字符");
	}

}

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
import org.laokou.common.core.util.RegexUtils;

/**
 * @author laokou
 */
class RegexUtilsTest {

	@Test
	void test() {
		Assertions.assertTrue(RegexUtils.mailRegex("2413176044@qq.com"));
		Assertions.assertFalse(RegexUtils.mailRegex("123"));
		Assertions.assertTrue(RegexUtils.ipv4Regex("127.0.0.1"));
		Assertions.assertFalse(RegexUtils.ipv4Regex("127.0.0.256"));
		Assertions.assertFalse(RegexUtils.ipv4Regex("127.0.0.x"));
		Assertions.assertFalse(RegexUtils.ipv4Regex("2001:0db8:85a3:0000:0000:8a2e:0370:7334"));
		Assertions.assertTrue(RegexUtils.ipv6Regex("2001:0db8:85a3:0000:0000:8a2e:0370:7334"));
		Assertions.assertFalse(RegexUtils.ipv6Regex("127.0.0.1"));
		Assertions.assertFalse(RegexUtils.numberRegex("-1"));
		Assertions.assertTrue(RegexUtils.numberRegex("123"));
		Assertions.assertFalse(RegexUtils.numberRegex("xx"));
		Assertions.assertTrue(RegexUtils.mobileRegex("18888888888"));
		Assertions.assertFalse(RegexUtils.mobileRegex("188888888888"));
		Assertions.assertFalse(RegexUtils.mobileRegex("1888888888"));
		Assertions.assertFalse(RegexUtils.mobileRegex("1888888888x"));
		Assertions.assertFalse(RegexUtils.matches("^[A-Za-z]+$|^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z0-9]+$", "哈哈哈"));
		Assertions.assertTrue(RegexUtils.matches("^[A-Za-z]+$|^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z0-9]+$", "admin123"));
		Assertions.assertEquals("v3", RegexUtils.getRegexValue("/v3/test", "/(v\\d+)/"));
	}

}

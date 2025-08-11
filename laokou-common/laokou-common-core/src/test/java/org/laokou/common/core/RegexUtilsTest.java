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
import org.laokou.common.core.util.RegexUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author laokou
 */
class RegexUtilsTest {

	@Test
	void test_regex() {
		assertThat(RegexUtils.mailRegex("2413176044@qq.com")).isTrue();
		assertThat(RegexUtils.mailRegex("123")).isFalse();
		assertThat(RegexUtils.ipv4Regex("127.0.0.1")).isTrue();
		assertThat(RegexUtils.ipv4Regex("127.0.0.256")).isFalse();
		assertThat(RegexUtils.ipv4Regex("127.0.0.x")).isFalse();
		assertThat(RegexUtils.ipv4Regex("2001:0db8:85a3:0000:0000:8a2e:0370:7334")).isFalse();
		assertThat(RegexUtils.ipv6Regex("2001:0db8:85a3:0000:0000:8a2e:0370:7334")).isTrue();
		assertThat(RegexUtils.ipv6Regex("127.0.0.1")).isFalse();
		assertThat(RegexUtils.numberRegex("-1")).isFalse();
		assertThat(RegexUtils.numberRegex("123")).isTrue();
		assertThat(RegexUtils.numberRegex("xx")).isFalse();
		assertThat(RegexUtils.mobileRegex("18888888888")).isTrue();
		assertThat(RegexUtils.mobileRegex("188888888888")).isFalse();
		assertThat(RegexUtils.mobileRegex("1888888888")).isFalse();
		assertThat(RegexUtils.mobileRegex("1888888888x")).isFalse();
		assertThat(RegexUtils.matches("^[A-Za-z]+$|^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z0-9]+$", "哈哈哈")).isFalse();
		assertThat(RegexUtils.matches("^[A-Za-z]+$|^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z0-9]+$", "admin123")).isTrue();
		assertThat(RegexUtils.getRegexValue("/v3/test", "/(v\\d+)/")).isEqualTo("v3");
	}

}

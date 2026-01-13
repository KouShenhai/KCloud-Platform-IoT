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

package org.laokou.common.i18n;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.i18n.util.SystemUtils;

/**
 * SystemUtils测试类.
 *
 * @author laokou
 */
class SystemUtilsTest {

	@Test
	void testIsWindows() {
		String osName = System.getProperty("os.name");
		boolean expected = osName.contains("Windows");
		Assertions.assertThat(SystemUtils.isWindows()).isEqualTo(expected);
	}

	@Test
	void testIsLinux() {
		String osName = System.getProperty("os.name");
		boolean expected = osName.contains("Linux");
		Assertions.assertThat(SystemUtils.isLinux()).isEqualTo(expected);
	}

	@Test
	void testIsArchLinux() {
		// isArchLinux返回true的前提是isLinux为true
		if (!SystemUtils.isLinux()) {
			Assertions.assertThat(SystemUtils.isArchLinux()).isFalse();
		}
		// 如果是Linux系统，验证方法不抛异常
		Assertions.assertThatNoException().isThrownBy(SystemUtils::isArchLinux);
	}

	@Test
	void testMutualExclusive() {
		// Windows和Linux互斥（但可能两者都不是，如macOS）
		if (SystemUtils.isWindows()) {
			Assertions.assertThat(SystemUtils.isLinux()).isFalse();
		}
		if (SystemUtils.isLinux()) {
			Assertions.assertThat(SystemUtils.isWindows()).isFalse();
		}
	}

}

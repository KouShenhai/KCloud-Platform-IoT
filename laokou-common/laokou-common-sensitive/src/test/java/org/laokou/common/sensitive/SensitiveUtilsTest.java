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

package org.laokou.common.sensitive;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.i18n.common.constant.StringConstants;
import org.laokou.common.sensitive.util.SensitiveUtils;

/**
 * @author laokou
 */
class SensitiveUtilsTest {

	@Test
	void test_mobile() {
		Assertions.assertThat(SensitiveUtils.formatMobile("18888888888")).isEqualTo("188****8888");
		Assertions.assertThat(SensitiveUtils.formatMobile(StringConstants.EMPTY)).isEqualTo(StringConstants.EMPTY);
		Assertions.assertThat(SensitiveUtils.formatMobile(null)).isEqualTo(StringConstants.EMPTY);
	}

	@Test
	void test_str() {
		Assertions.assertThat(SensitiveUtils.formatStr(null, "", 3, 7)).isEqualTo(StringConstants.EMPTY);
		Assertions.assertThat(SensitiveUtils.formatStr("", "", 3, 7)).isEqualTo(StringConstants.EMPTY);
		Assertions.assertThat(SensitiveUtils.formatStr("1234567890", null, 3, 7)).isEqualTo("123890");
		Assertions.assertThat(SensitiveUtils.formatStr("1234567890", StringConstants.EMPTY, 3, 7)).isEqualTo("123890");
		Assertions.assertThat(SensitiveUtils.formatStr("1234567890", StringConstants.EMPTY, -1, 7)).isEqualTo("890");
		Assertions.assertThat(SensitiveUtils.formatStr("1234567890", StringConstants.EMPTY, 11, 7))
			.isEqualTo("1234567");
		Assertions.assertThat(SensitiveUtils.formatStr("1234567890", StringConstants.EMPTY, 1, 12)).isEqualTo("1");
		Assertions.assertThat(SensitiveUtils.formatStr("1234567890", StringConstants.EMPTY, 1, -1))
			.isEqualTo("234567890");
	}

	@Test
	void test_mail() {
		Assertions.assertThat(SensitiveUtils.formatMail("2413176044@qq.com")).isEqualTo("2****@qq.com");
		Assertions.assertThat(SensitiveUtils.formatMail(null)).isEqualTo(StringConstants.EMPTY);
		Assertions.assertThat(SensitiveUtils.formatMail(StringConstants.EMPTY)).isEqualTo(StringConstants.EMPTY);
	}

}

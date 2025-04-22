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

package org.laokou.common.sensitive;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.sensitive.util.SensitiveUtils;

import static org.laokou.common.i18n.common.constant.StringConstants.EMPTY;

/**
 * @author laokou
 */
class SensitiveUtilsTest {

	@Test
	void testMobile() {
		Assertions.assertEquals("188****8888", SensitiveUtils.formatMobile("18888888888"));
		Assertions.assertEquals(EMPTY, SensitiveUtils.formatMobile(EMPTY));
		Assertions.assertEquals(EMPTY, SensitiveUtils.formatMobile(null));
	}

	@Test
	void testStr() {
		Assertions.assertEquals(EMPTY, SensitiveUtils.formatStr(null, "", 3, 7));
		Assertions.assertEquals(EMPTY, SensitiveUtils.formatStr("", "", 3, 7));
		Assertions.assertEquals("123890", SensitiveUtils.formatStr("1234567890", null, 3, 7));
		Assertions.assertEquals("123890", SensitiveUtils.formatStr("1234567890", EMPTY, 3, 7));
		Assertions.assertEquals("890", SensitiveUtils.formatStr("1234567890", EMPTY, -1, 7));
		Assertions.assertEquals("1234567", SensitiveUtils.formatStr("1234567890", EMPTY, 11, 7));
		Assertions.assertEquals("1", SensitiveUtils.formatStr("1234567890", EMPTY, 1, 12));
		Assertions.assertEquals("234567890", SensitiveUtils.formatStr("1234567890", EMPTY, 1, -1));
	}

	@Test
	void testMail() {
		Assertions.assertEquals("2****@qq.com", SensitiveUtils.formatMail("2413176044@qq.com"));
		Assertions.assertEquals(EMPTY, SensitiveUtils.formatMail(null));
		Assertions.assertEquals(EMPTY, SensitiveUtils.formatMail(EMPTY));
	}

}

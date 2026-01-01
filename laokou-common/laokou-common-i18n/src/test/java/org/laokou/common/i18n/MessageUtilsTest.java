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
import org.laokou.common.i18n.common.exception.StatusCode;
import org.laokou.common.i18n.util.LocaleUtils;
import org.laokou.common.i18n.util.MessageUtils;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * @author laokou
 */
class MessageUtilsTest {

	@Test
	void test_zh() {
		try {
			LocaleContextHolder.setLocale(LocaleUtils.toLocale("zh-CN,zh"), true);
			Assertions.assertThat(MessageUtils.getMessage(StatusCode.OK)).isEqualTo("请求成功");
			Assertions.assertThat(MessageUtils.getMessage(StatusCode.OK, null)).isEqualTo("请求成功");
		}
		finally {
			LocaleContextHolder.resetLocaleContext();
		}
	}

	@Test
	void test_en() {
		try {
			LocaleContextHolder.setLocale(LocaleUtils.toLocale("en-US,en"), true);
			Assertions.assertThat(MessageUtils.getMessage(StatusCode.OK)).isEqualTo("Request successful");
			Assertions.assertThat(MessageUtils.getMessage(StatusCode.OK, null)).isEqualTo("Request successful");
		}
		finally {
			LocaleContextHolder.resetLocaleContext();
		}
	}

}

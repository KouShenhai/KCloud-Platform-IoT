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

package org.laokou.common.i18n;

import org.junit.jupiter.api.Test;
import org.laokou.common.i18n.util.DateUtils;

import java.time.Instant;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author laokou
 */
class DateUtilsTest {

	@Test
	void test() {
		String str = "2024-09-24 11:33:33";
		LocalDateTime localDateTime = DateUtils.parseTime(str, DateUtils.YYYY_B_MM_B_DD_HH_R_MM_R_SS);
		assertThat(DateUtils.format(localDateTime, DateUtils.YYYY_B_MM_B_DD_HH_R_MM_R_SS)).isEqualTo(str);
		str = "2024-09-24 13:59:00";
		Instant instant = DateUtils.parsInstant(str, DateUtils.YYYY_B_MM_B_DD_HH_R_MM_R_SS);
		long between = DateUtils.betweenSeconds(DateUtils.nowInstant(), instant);
		assertThat(between < 0).isTrue();
	}

}

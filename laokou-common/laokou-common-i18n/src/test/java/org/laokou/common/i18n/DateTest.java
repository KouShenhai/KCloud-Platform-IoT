/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.i18n.utils.DateUtil;

import java.time.Instant;
import java.time.LocalDateTime;

/**
 * @author laokou
 */
class DateTest {

	@Test
	void test_local_date_time_yyyy_rod_mm_rod_dd_space_hh_risk_hh_risk_ss() {
		String str = "2024-09-24 11:33:33";
		LocalDateTime localDateTime = DateUtil.parseTime(str, DateUtil.YYYY_B_MM_B_DD_HH_R_MM_R_SS);
		Assertions.assertEquals(str, DateUtil.format(localDateTime, DateUtil.YYYY_B_MM_B_DD_HH_R_MM_R_SS));
	}

	@Test
	void testInstant() {
		String str = "2024-09-24 13:59:00";
		Instant instant = DateUtil.parsInstant(str, DateUtil.YYYY_B_MM_B_DD_HH_R_MM_R_SS);
		long between = DateUtil.betweenSeconds(DateUtil.nowInstant(), instant);
		Assertions.assertTrue(between < 0);
	}

}

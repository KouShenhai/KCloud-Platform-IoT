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
import org.laokou.common.i18n.common.constant.DateConstants;
import org.laokou.common.i18n.util.InstantUtils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author laokou
 */
class InstantUtilsTest {

	@Test
	void test() {
		Instant instant = InstantUtils.now();
		Assertions.assertThat(instant).isNotNull();
		Instant instant1 = InstantUtils.plusHours(instant, 2);
		Assertions.assertThat(instant).isNotNull();
		Assertions.assertThat(InstantUtils.betweenHours(instant, instant1)).isEqualTo(2);

		Instant instant2 = InstantUtils.plusSeconds(instant, 2);
		Assertions.assertThat(instant2).isNotNull();
		Assertions.assertThat(InstantUtils.betweenSeconds(instant, instant2)).isEqualTo(2);

		Instant instant3 = InstantUtils.parse("2025-10-15 12:00:00", DateConstants.YYYY_B_MM_B_DD_HH_R_MM_R_SS);
		Assertions.assertThat(instant3).isNotNull();
		Instant instant4 = InstantUtils.parse("2025-10-15 12:00:00", ZoneId.of("Asia/Shanghai"),
				DateConstants.YYYY_B_MM_B_DD_HH_R_MM_R_SS);
		Assertions.assertThat(instant4).isNotNull().isEqualTo(instant3);
		Assertions.assertThat(InstantUtils.format(instant3, DateConstants.YYYY_B_MM_B_DD_HH_R_MM_R_SS))
			.isEqualTo("2025-10-15 12:00:00");
		Assertions
			.assertThat(InstantUtils.format(instant3, ZoneId.of("Asia/Shanghai"),
					DateConstants.YYYY_B_MM_B_DD_HH_R_MM_R_SS))
			.isEqualTo("2025-10-15 12:00:00");

		DateTimeFormatter dateTimeFormatter = InstantUtils
			.getDateTimeFormatter(DateConstants.YYYY_B_MM_B_DD_HH_R_MM_R_SS);
		Assertions.assertThat(dateTimeFormatter).isNotNull();
		ZonedDateTime zonedDateTime = instant4.atZone(ZoneId.of("Asia/Shanghai"));
		Assertions.assertThat(zonedDateTime).isNotNull();
		Assertions.assertThat(dateTimeFormatter.format(zonedDateTime)).isEqualTo("2025-10-15 12:00:00");

		DateTimeFormatter dateTimeFormatter2 = InstantUtils
			.getDateTimeFormatter(DateConstants.YYYY_B_MM_B_DD_HH_R_MM_R_SS, ZoneId.of("Asia/Shanghai"));
		Assertions.assertThat(dateTimeFormatter2).isNotNull();
		Assertions.assertThat(dateTimeFormatter2.format(zonedDateTime)).isEqualTo("2025-10-15 12:00:00");

		Assertions.assertThat(InstantUtils.getTimestampOfInstant(instant4)).isEqualTo(1760500800000L);
		Assertions.assertThat(InstantUtils.getInstantOfTimestamp(1760500800000L)).isEqualTo(instant4);
	}

}

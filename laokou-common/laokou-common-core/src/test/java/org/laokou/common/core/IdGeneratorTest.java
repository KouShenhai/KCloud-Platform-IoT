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

package org.laokou.common.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.i18n.utils.DateUtil;

import java.time.Instant;
import java.time.LocalDateTime;

/**
 * @author laokou
 */
class IdGeneratorTest {

	@Test
	void testSnowflakeId() {
		long snowflakeId = IdGenerator.defaultSnowflakeId();
		LocalDateTime localDateTime = IdGenerator.getLocalDateTime(snowflakeId, DateUtil.getDefaultZoneId());
		String time1 = DateUtil.format(localDateTime, DateUtil.YYYY_B_MM_B_DD_HH_R_MM_R_SS);
		Instant instant = IdGenerator.getInstant(snowflakeId);
		String time2 = DateUtil.format(instant, DateUtil.YYYY_B_MM_B_DD_HH_R_MM_R_SS);
		Assertions.assertEquals(time1, time2);
	}

}

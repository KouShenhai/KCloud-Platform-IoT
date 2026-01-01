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
import org.laokou.common.i18n.util.LocalDateTimeUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author laokou
 */
class LocalDateTimeUtilsTest {

	@Test
	void test_format() {
		LocalDateTime localDateTime = LocalDateTime.of(2025, 10, 15, 12, 30, 45);
		String formatted = LocalDateTimeUtils.format(localDateTime, DateConstants.YYYY_B_MM_B_DD_HH_R_MM_R_SS);
		Assertions.assertThat(formatted).isEqualTo("2025-10-15 12:30:45");
	}

	@Test
	void test_isAfter() {
		LocalDateTime localDateTime1 = LocalDateTime.of(2025, 10, 15, 12, 0, 0);
		LocalDateTime localDateTime2 = LocalDateTime.of(2025, 10, 14, 12, 0, 0);
		Assertions.assertThat(LocalDateTimeUtils.isAfter(localDateTime1, localDateTime2)).isTrue();
		Assertions.assertThat(LocalDateTimeUtils.isAfter(localDateTime2, localDateTime1)).isFalse();
	}

	@Test
	void test_isBefore() {
		LocalDateTime localDateTime1 = LocalDateTime.of(2025, 10, 14, 12, 0, 0);
		LocalDateTime localDateTime2 = LocalDateTime.of(2025, 10, 15, 12, 0, 0);
		Assertions.assertThat(LocalDateTimeUtils.isBefore(localDateTime1, localDateTime2)).isTrue();
		Assertions.assertThat(LocalDateTimeUtils.isBefore(localDateTime2, localDateTime1)).isFalse();
	}

	@Test
	void test_betweenMillis() {
		LocalDateTime start = LocalDateTime.of(2025, 10, 15, 12, 0, 0);
		LocalDateTime end = LocalDateTime.of(2025, 10, 15, 12, 0, 2);
		long millis = LocalDateTimeUtils.betweenMillis(start, end);
		Assertions.assertThat(millis).isEqualTo(2000);
	}

	@Test
	void test_betweenSeconds() {
		LocalDateTime start = LocalDateTime.of(2025, 10, 15, 12, 0, 0);
		LocalDateTime end = LocalDateTime.of(2025, 10, 15, 12, 1, 30);
		long seconds = LocalDateTimeUtils.betweenSeconds(start, end);
		Assertions.assertThat(seconds).isEqualTo(90);
	}

	@Test
	void test_betweenMinutes() {
		LocalDateTime start = LocalDateTime.of(2025, 10, 15, 12, 0, 0);
		LocalDateTime end = LocalDateTime.of(2025, 10, 15, 12, 30, 0);
		long minutes = LocalDateTimeUtils.betweenMinutes(start, end);
		Assertions.assertThat(minutes).isEqualTo(30);
	}

	@Test
	void test_betweenHours() {
		LocalDateTime start = LocalDateTime.of(2025, 10, 15, 12, 0, 0);
		LocalDateTime end = LocalDateTime.of(2025, 10, 15, 15, 0, 0);
		long hours = LocalDateTimeUtils.betweenHours(start, end);
		Assertions.assertThat(hours).isEqualTo(3);
	}

	@Test
	void test_betweenDays() {
		LocalDateTime start = LocalDateTime.of(2025, 10, 15, 12, 0, 0);
		LocalDateTime end = LocalDateTime.of(2025, 10, 18, 12, 0, 0);
		long days = LocalDateTimeUtils.betweenDays(start, end);
		Assertions.assertThat(days).isEqualTo(3);
	}

	@Test
	void test_parseTime() {
		String dateTime = "2025-10-15 12:30:45";
		LocalDateTime parsed = LocalDateTimeUtils.parseTime(dateTime, DateConstants.YYYY_B_MM_B_DD_HH_R_MM_R_SS);
		Assertions.assertThat(parsed).isNotNull();
		Assertions.assertThat(parsed.getYear()).isEqualTo(2025);
		Assertions.assertThat(parsed.getMonthValue()).isEqualTo(10);
		Assertions.assertThat(parsed.getDayOfMonth()).isEqualTo(15);
		Assertions.assertThat(parsed.getHour()).isEqualTo(12);
		Assertions.assertThat(parsed.getMinute()).isEqualTo(30);
		Assertions.assertThat(parsed.getSecond()).isEqualTo(45);
	}

	@Test
	void test_plusDays() {
		LocalDateTime localDateTime = LocalDateTime.of(2025, 10, 15, 12, 0, 0);
		LocalDateTime result = LocalDateTimeUtils.plusDays(localDateTime, 5);
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result.getDayOfMonth()).isEqualTo(20);

		// Test negative days (going back)
		LocalDateTime resultMinus = LocalDateTimeUtils.plusDays(localDateTime, -5);
		Assertions.assertThat(resultMinus).isNotNull();
		Assertions.assertThat(resultMinus.getDayOfMonth()).isEqualTo(10);
	}

	@Test
	void test_plusSeconds() {
		LocalDateTime localDateTime = LocalDateTime.of(2025, 10, 15, 12, 0, 0);
		LocalDateTime result = LocalDateTimeUtils.plusSeconds(localDateTime, 90);
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result.getMinute()).isEqualTo(1);
		Assertions.assertThat(result.getSecond()).isEqualTo(30);

		// Test negative seconds (going back)
		LocalDateTime resultMinus = LocalDateTimeUtils.plusSeconds(localDateTime, -30);
		Assertions.assertThat(resultMinus).isNotNull();
		Assertions.assertThat(resultMinus.getMinute()).isEqualTo(59);
		Assertions.assertThat(resultMinus.getSecond()).isEqualTo(30);
	}

	@Test
	void test_plusMonths() {
		LocalDateTime localDateTime = LocalDateTime.of(2025, 10, 15, 12, 0, 0);
		LocalDateTime result = LocalDateTimeUtils.plusMonths(localDateTime, 3);
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result.getYear()).isEqualTo(2026);
		Assertions.assertThat(result.getMonthValue()).isEqualTo(1);

		// Test negative months (going back)
		LocalDateTime resultMinus = LocalDateTimeUtils.plusMonths(localDateTime, -2);
		Assertions.assertThat(resultMinus).isNotNull();
		Assertions.assertThat(resultMinus.getMonthValue()).isEqualTo(8);
	}

	@Test
	void test_plusYears() {
		LocalDateTime localDateTime = LocalDateTime.of(2025, 10, 15, 12, 0, 0);
		LocalDateTime result = LocalDateTimeUtils.plusYears(localDateTime, 5);
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result.getYear()).isEqualTo(2030);

		// Test negative years (going back)
		LocalDateTime resultMinus = LocalDateTimeUtils.plusYears(localDateTime, -5);
		Assertions.assertThat(resultMinus).isNotNull();
		Assertions.assertThat(resultMinus.getYear()).isEqualTo(2020);
	}

	@Test
	void test_now() {
		LocalDateTime now = LocalDateTimeUtils.now();
		Assertions.assertThat(now).isNotNull().isBeforeOrEqualTo(LocalDateTime.now());
	}

	@Test
	void test_getTimestampOfLocalDateTime() {
		LocalDateTime localDateTime = LocalDateTime.of(2025, 10, 15, 12, 0, 0);
		long timestamp = LocalDateTimeUtils.getTimestampOfLocalDateTime(localDateTime);
		Assertions.assertThat(timestamp).isGreaterThan(0);
	}

	@Test
	void test_getLocalDateTimeOfTimestamp() {
		// Use a known timestamp: 2025-10-15 12:00:00 in Asia/Shanghai timezone
		long timestamp = 1760500800000L;
		ZoneId zoneId = ZoneId.of("Asia/Shanghai");
		LocalDateTime localDateTime = LocalDateTimeUtils.getLocalDateTimeOfTimestamp(timestamp, zoneId);
		Assertions.assertThat(localDateTime).isNotNull();
		Assertions.assertThat(localDateTime.getYear()).isEqualTo(2025);
		Assertions.assertThat(localDateTime.getMonthValue()).isEqualTo(10);
		Assertions.assertThat(localDateTime.getDayOfMonth()).isEqualTo(15);
		Assertions.assertThat(localDateTime.getHour()).isEqualTo(12);
		Assertions.assertThat(localDateTime.getMinute()).isZero();
		Assertions.assertThat(localDateTime.getSecond()).isZero();
	}

	@Test
	void test_timestampConversionRoundTrip() {
		// Test that converting LocalDateTime -> timestamp -> LocalDateTime gives the
		// same result
		LocalDateTime original = LocalDateTime.of(2025, 10, 15, 12, 30, 45);
		long timestamp = LocalDateTimeUtils.getTimestampOfLocalDateTime(original);
		LocalDateTime converted = LocalDateTimeUtils.getLocalDateTimeOfTimestamp(timestamp, ZoneId.systemDefault());

		Assertions.assertThat(converted).isNotNull();
		Assertions.assertThat(converted.getYear()).isEqualTo(original.getYear());
		Assertions.assertThat(converted.getMonthValue()).isEqualTo(original.getMonthValue());
		Assertions.assertThat(converted.getDayOfMonth()).isEqualTo(original.getDayOfMonth());
		Assertions.assertThat(converted.getHour()).isEqualTo(original.getHour());
		Assertions.assertThat(converted.getMinute()).isEqualTo(original.getMinute());
		Assertions.assertThat(converted.getSecond()).isEqualTo(original.getSecond());
	}

}

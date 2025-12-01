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

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.i18n.common.constant.DateConstants;
import org.laokou.common.i18n.util.LocalDateUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * @author laokou
 */
class LocalDateUtilsTest {

	@Test
	void test_format() {
		LocalDate localDate = LocalDate.of(2025, 10, 15);
		String formatted = LocalDateUtils.format(localDate, DateConstants.YYYYMMDD);
		Assertions.assertThat(formatted).isEqualTo("20251015");
	}

	@Test
	void test_isBefore() {
		LocalDate localDate1 = LocalDate.of(2025, 10, 14);
		LocalDate localDate2 = LocalDate.of(2025, 10, 15);
		Assertions.assertThat(LocalDateUtils.isBefore(localDate1, localDate2)).isTrue();
		Assertions.assertThat(LocalDateUtils.isBefore(localDate2, localDate1)).isFalse();
		Assertions.assertThat(LocalDateUtils.isBefore(localDate1, localDate1)).isFalse();
	}

	@Test
	void test_parse() {
		String date = "20251015";
		LocalDate parsed = LocalDateUtils.parse(date, DateConstants.YYYYMMDD);
		Assertions.assertThat(parsed).isNotNull();
		Assertions.assertThat(parsed.getYear()).isEqualTo(2025);
		Assertions.assertThat(parsed.getMonthValue()).isEqualTo(10);
		Assertions.assertThat(parsed.getDayOfMonth()).isEqualTo(15);
	}

	@Test
	void test_plusDays() {
		LocalDate localDate = LocalDate.of(2025, 10, 15);
		LocalDate result = LocalDateUtils.plusDays(localDate, 5);
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result.getDayOfMonth()).isEqualTo(20);

		// Test negative days (going back)
		LocalDate resultMinus = LocalDateUtils.plusDays(localDate, -5);
		Assertions.assertThat(resultMinus).isNotNull();
		Assertions.assertThat(resultMinus.getDayOfMonth()).isEqualTo(10);

		// Test crossing month boundary
		LocalDate resultCrossMonth = LocalDateUtils.plusDays(localDate, 20);
		Assertions.assertThat(resultCrossMonth.getMonthValue()).isEqualTo(11);
		Assertions.assertThat(resultCrossMonth.getDayOfMonth()).isEqualTo(4);
	}

	@Test
	void test_plusMonths() {
		LocalDate localDate = LocalDate.of(2025, 10, 15);
		LocalDate result = LocalDateUtils.plusMonths(localDate, 3);
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result.getYear()).isEqualTo(2026);
		Assertions.assertThat(result.getMonthValue()).isEqualTo(1);
		Assertions.assertThat(result.getDayOfMonth()).isEqualTo(15);

		// Test negative months (going back)
		LocalDate resultMinus = LocalDateUtils.plusMonths(localDate, -2);
		Assertions.assertThat(resultMinus).isNotNull();
		Assertions.assertThat(resultMinus.getMonthValue()).isEqualTo(8);
		Assertions.assertThat(resultMinus.getDayOfMonth()).isEqualTo(15);
	}

	@Test
	void test_now() {
		LocalDate now = LocalDateUtils.now();
		Assertions.assertThat(now).isNotNull();
		Assertions.assertThat(now).isBeforeOrEqualTo(LocalDate.now());
	}

	@Test
	void test_betweenDays() {
		LocalDate start = LocalDate.of(2025, 10, 15);
		LocalDate end = LocalDate.of(2025, 10, 18);
		long days = LocalDateUtils.betweenDays(start, end);
		Assertions.assertThat(days).isEqualTo(3);

		// Test same date
		long sameDays = LocalDateUtils.betweenDays(start, start);
		Assertions.assertThat(sameDays).isEqualTo(0);

		// Test reverse order
		long reverseDays = LocalDateUtils.betweenDays(end, start);
		Assertions.assertThat(reverseDays).isEqualTo(-3);
	}

	@Test
	void test_betweenMonths() {
		LocalDate start = LocalDate.of(2025, 10, 15);
		LocalDate end = LocalDate.of(2026, 1, 15);
		long months = LocalDateUtils.betweenMonths(start, end);
		Assertions.assertThat(months).isEqualTo(3);

		// Test same month
		long sameMonths = LocalDateUtils.betweenMonths(start, start);
		Assertions.assertThat(sameMonths).isEqualTo(0);

		// Test reverse order
		long reverseMonths = LocalDateUtils.betweenMonths(end, start);
		Assertions.assertThat(reverseMonths).isEqualTo(-3);
	}

	@Test
	void test_betweenYears() {
		LocalDate start = LocalDate.of(2025, 10, 15);
		LocalDate end = LocalDate.of(2030, 10, 15);
		long years = LocalDateUtils.betweenYears(start, end);
		Assertions.assertThat(years).isEqualTo(5);

		// Test same year
		long sameYears = LocalDateUtils.betweenYears(start, start);
		Assertions.assertThat(sameYears).isEqualTo(0);

		// Test reverse order
		long reverseYears = LocalDateUtils.betweenYears(end, start);
		Assertions.assertThat(reverseYears).isEqualTo(-5);
	}

	@Test
	void test_getDayOfWeek() {
		// 2025-10-15 is a Wednesday
		LocalDate localDate = LocalDate.of(2025, 10, 15);

		// Get next or same Monday (DayOfWeek.MONDAY = 1)
		LocalDate monday = LocalDateUtils.getDayOfWeek(localDate, DayOfWeek.MONDAY.getValue());
		Assertions.assertThat(monday).isNotNull();
		Assertions.assertThat(monday.getDayOfWeek()).isEqualTo(DayOfWeek.MONDAY);
		Assertions.assertThat(monday.getDayOfMonth()).isEqualTo(20); // Next Monday

		// Get next or same Wednesday (same day)
		LocalDate wednesday = LocalDateUtils.getDayOfWeek(localDate, DayOfWeek.WEDNESDAY.getValue());
		Assertions.assertThat(wednesday).isNotNull();
		Assertions.assertThat(wednesday.getDayOfWeek()).isEqualTo(DayOfWeek.WEDNESDAY);
		Assertions.assertThat(wednesday).isEqualTo(localDate); // Same day

		// Get next or same Friday
		LocalDate friday = LocalDateUtils.getDayOfWeek(localDate, DayOfWeek.FRIDAY.getValue());
		Assertions.assertThat(friday).isNotNull();
		Assertions.assertThat(friday.getDayOfWeek()).isEqualTo(DayOfWeek.FRIDAY);
		Assertions.assertThat(friday.getDayOfMonth()).isEqualTo(17); // This Friday
	}

	@Test
	void test_getFirstDayOfMonth() {
		LocalDate localDate = LocalDate.of(2025, 10, 15);
		LocalDate firstDay = LocalDateUtils.getFirstDayOfMonth(localDate);
		Assertions.assertThat(firstDay).isNotNull();
		Assertions.assertThat(firstDay.getYear()).isEqualTo(2025);
		Assertions.assertThat(firstDay.getMonthValue()).isEqualTo(10);
		Assertions.assertThat(firstDay.getDayOfMonth()).isEqualTo(1);

		// Test with last day of month
		LocalDate lastDayOfMonth = LocalDate.of(2025, 10, 31);
		LocalDate firstDayFromLast = LocalDateUtils.getFirstDayOfMonth(lastDayOfMonth);
		Assertions.assertThat(firstDayFromLast.getDayOfMonth()).isEqualTo(1);
	}

	@Test
	void test_getLastDayOfMonth() {
		LocalDate localDate = LocalDate.of(2025, 10, 15);
		LocalDate lastDay = LocalDateUtils.getLastDayOfMonth(localDate);
		Assertions.assertThat(lastDay).isNotNull();
		Assertions.assertThat(lastDay.getYear()).isEqualTo(2025);
		Assertions.assertThat(lastDay.getMonthValue()).isEqualTo(10);
		Assertions.assertThat(lastDay.getDayOfMonth()).isEqualTo(31);

		// Test with February (non-leap year)
		LocalDate february = LocalDate.of(2025, 2, 15);
		LocalDate lastDayOfFeb = LocalDateUtils.getLastDayOfMonth(february);
		Assertions.assertThat(lastDayOfFeb.getDayOfMonth()).isEqualTo(28);

		// Test with February (leap year)
		LocalDate februaryLeap = LocalDate.of(2024, 2, 15);
		LocalDate lastDayOfFebLeap = LocalDateUtils.getLastDayOfMonth(februaryLeap);
		Assertions.assertThat(lastDayOfFebLeap.getDayOfMonth()).isEqualTo(29);
	}

	@Test
	void test_getDayOfWeekText() {
		// 2025-10-15 is a Wednesday
		LocalDate localDate = LocalDate.of(2025, 10, 15);
		String dayOfWeekText = LocalDateUtils.getDayOfWeekText(localDate);
		Assertions.assertThat(dayOfWeekText).isNotNull();
		Assertions.assertThat(dayOfWeekText).isNotEmpty();
		// The actual text depends on the system locale, so we just verify it's not
		// null/empty

		// Test with Monday
		LocalDate monday = LocalDate.of(2025, 10, 13);
		String mondayText = LocalDateUtils.getDayOfWeekText(monday);
		Assertions.assertThat(mondayText).isNotNull();
		Assertions.assertThat(mondayText).isNotEmpty();
	}

	@Test
	void test_formatAndParseRoundTrip() {
		// Test that formatting and parsing gives the same result
		LocalDate original = LocalDate.of(2025, 10, 15);
		String formatted = LocalDateUtils.format(original, DateConstants.YYYYMMDD);
		LocalDate parsed = LocalDateUtils.parse(formatted, DateConstants.YYYYMMDD);

		Assertions.assertThat(parsed).isNotNull();
		Assertions.assertThat(parsed).isEqualTo(original);
	}

	@Test
	void test_monthBoundaries() {
		// Test getting first and last day of the same month
		LocalDate localDate = LocalDate.of(2025, 10, 15);
		LocalDate firstDay = LocalDateUtils.getFirstDayOfMonth(localDate);
		LocalDate lastDay = LocalDateUtils.getLastDayOfMonth(localDate);

		// Verify the difference
		long daysBetween = LocalDateUtils.betweenDays(firstDay, lastDay);
		Assertions.assertThat(daysBetween).isEqualTo(30); // October has 31 days, so 30
															// days between 1st and 31st
	}

}

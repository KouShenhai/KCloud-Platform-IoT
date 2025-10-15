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

package org.laokou.common.i18n.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;

/**
 * @author laokou
 */
public final class LocalDateUtils {

	private LocalDateUtils() {
	}

	/**
	 * 日期格式化.
	 * @param localDate 日期
	 * @param pattern 格式
	 * @return 字符串
	 */
	public static String format(LocalDate localDate, String pattern) {
		DateTimeFormatter dateTimeFormatter = InstantUtils.getDateTimeFormatter(pattern);
		return localDate.format(dateTimeFormatter);
	}

	/**
	 * 判断 d1 在 d2 前.
	 * @param localDate1 日期1
	 * @param localDate2 日期2
	 * @return 判断结果
	 */
	public static boolean isBefore(LocalDate localDate1, LocalDate localDate2) {
		return localDate1.isBefore(localDate2);
	}

	/**
	 * 字符串转换日期.
	 * @param date 日期
	 * @param pattern 格式
	 * @return 日期
	 */
	public static LocalDate parse(String date, String pattern) {
		return LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern));
	}

	/**
	 * 日期 前/后 x天 的日期.
	 * @param localDate 日期
	 * @param days 天
	 * @return 日期
	 */
	public static LocalDate plusDays(LocalDate localDate, long days) {
		return localDate.plusDays(days);
	}

	/**
	 * 获取 前/后 x月 的日期.
	 * @param localDate 日期
	 * @param months 月
	 * @return 日期
	 */
	public static LocalDate plusMonths(LocalDate localDate, long months) {
		return localDate.plusMonths(months);
	}

	/**
	 * 现在日期.
	 * @return 日期
	 */
	public static LocalDate now() {
		return LocalDate.now();
	}

	/**
	 * 开始日期到结束日期相差多少天.
	 * @param start 开始日期
	 * @param end 结束日期
	 * @return 天
	 */
	public static long betweenDays(LocalDate start, LocalDate end) {
		return Period.between(start, end).getDays();
	}

	/**
	 * 开始日期到结束日期相差多少月.
	 * @param start 开始日期
	 * @param end 结束日期
	 * @return 月
	 */
	public static long betweenMonths(LocalDate start, LocalDate end) {
		return Period.between(start, end).getMonths();
	}

	/**
	 * 开始日期到结束日期相差多少年.
	 * @param start 开始日期
	 * @param end 结束日期
	 * @return 年
	 */
	public static long betweenYears(LocalDate start, LocalDate end) {
		return Period.between(start, end).getYears();
	}

	public static LocalDate getDayOfWeek(LocalDate localDate, int dayOfWeek) {
		return localDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.of(dayOfWeek)));
	}

	/**
	 * 根据日期获取该月的第一天.
	 * @param localDate 日期
	 * @return 该月的第一天
	 */
	public static LocalDate getFirstDayOfMonth(LocalDate localDate) {
		return localDate.with(TemporalAdjusters.firstDayOfMonth());
	}

	/**
	 * 根据日期获取这该月的最后一天.
	 * @param localDate 日期
	 * @return 该月的最后一天
	 */
	public static LocalDate getLastDayOfMonth(LocalDate localDate) {
		return localDate.with(TemporalAdjusters.lastDayOfMonth());
	}

	/**
	 * 获取日期在该周的文本格式.
	 * @param localDate 日期
	 * @return 文本格式
	 */
	public static String getDayOfWeekText(LocalDate localDate) {
		return localDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
	}

}

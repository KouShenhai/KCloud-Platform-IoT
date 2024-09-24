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

package org.laokou.common.i18n.utils;

import org.laokou.common.i18n.common.exception.SystemException;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;

/**
 * 日期处理.
 *
 * @author laokou
 */
public final class DateUtil {

	/**
	 * yyyy-MM-dd HH:mm:ss.
	 */
	public static final int YYYY_ROD_MM_ROD_DD_SPACE_HH_RISK_HH_RISK_SS = 0;

	/**
	 * yyyyMMddHHmmss.
	 */
	public static final int YYYYMMDDHHMMSS = 1;

	/**
	 * yyyyMM.
	 */
	public static final int YYYYMM = 2;

	/**
	 * yyyy-MM-dd.
	 */
	public static final int YYYY_ROD_MM_ROD_DD = 3;

	/**
	 * yyyy年MM月dd日.
	 */
	public static final int YYYY_TEXT_MM_TEXT_DD_TEXT = 4;

	/**
	 * yyyy.MM.dd.
	 */
	public static final int YYYY_DOT_MM_DOT_DD = 5;

	/**
	 * yyyymmdd.
	 */
	public static final int YYYYMMDD = 6;

	/**
	 * yyyy-MM-dd HH:mm:ss.SSS.
	 */
	public static final int YYYY_ROD_MM_ROD_DD_SPACE_HH_RISK_HH_RISK_SS_SSS = 7;

	/**
	 * 星期一.
	 */
	public static final int MONDAY = 0;

	/**
	 * 时间格式.
	 */
	private static final String[] TIME_PATTERNS = { Constant.YYYY_ROD_MM_ROD_DD_SPACE_HH_RISK_HH_RISK_SS,
			Constant.YYYYMMDDHHMMSS, Constant.YYYYMM, Constant.YYYY_ROD_MM_ROD_DD, Constant.YYYY_TEXT_MM_TEXT_DD_TEXT,
			Constant.YYYY_DOT_MM_DOT_DD, Constant.YYYYMMDD, Constant.YYYY_ROD_MM_ROD_DD_SPACE_HH_RISK_HH_RISK_SS_SSS };

	/**
	 * 星期数组.
	 */
	private static final DayOfWeek[] WEEK_PATTERNS = { DayOfWeek.MONDAY };

	/**
	 * 时间格式.
	 * @param index 索引
	 * @return 时间格式
	 */
	public static String getTimePattern(int index) {
		if (index >= TIME_PATTERNS.length || index < 0) {
			throw new SystemException("时间格式不存在");
		}
		return TIME_PATTERNS[index];
	}

	/**
	 * 星期格式.
	 * @param index 索引
	 * @return 星球格式
	 */
	public static DayOfWeek getWeekPattern(int index) {
		if (index >= WEEK_PATTERNS.length || index < 0) {
			throw new SystemException("星期格式不存在");
		}
		return WEEK_PATTERNS[index];
	}

	/**
	 * 时间格式化.
	 * @param localDateTime 时间
	 * @param index 索引
	 * @return 字符串
	 */
	public static String format(LocalDateTime localDateTime, int index) {
		DateTimeFormatter dateTimeFormatter = getDateTimeFormatter(index);
		return localDateTime.format(dateTimeFormatter);
	}

	/**
	 * 日期格式化.
	 * @param localDate 日期
	 * @param index 索引
	 * @return 字符串
	 */
	public static String format(LocalDate localDate, int index) {
		DateTimeFormatter dateTimeFormatter = getDateTimeFormatter(index);
		return localDate.format(dateTimeFormatter);
	}

	public static String format(Instant instant, ZoneId zoneId, int index) {
		// 指定时区
		ZonedDateTime zonedDateTime = instant.atZone(zoneId);
		DateTimeFormatter dateTimeFormatter = getDateTimeFormatter(index);
		return zonedDateTime.format(dateTimeFormatter);
	}

	public static ZoneId getDefaultZoneId() {
		return ZoneId.systemDefault();
	}

	/**
	 * 格式化配置.
	 * @param index 索引
	 * @return 格式化配置
	 */
	public static DateTimeFormatter getDateTimeFormatter(int index) {
		String timePattern = getTimePattern(index);
		return DateTimeFormatter.ofPattern(timePattern).withZone(getDefaultZoneId());
	}

	/**
	 * 格式化配置.
	 * @param index 索引
	 * @param zoneId 时区
	 * @return 格式化配置
	 */
	public static DateTimeFormatter getDateTimeFormatter(int index, ZoneId zoneId) {
		String timePattern = getTimePattern(index);
		return DateTimeFormatter.ofPattern(timePattern).withZone(zoneId);
	}

	/**
	 * 判断 d1 在 d2 后.
	 * @param localDateTime1 时间1
	 * @param localDateTime2 时间2
	 * @return 判断结果
	 */
	public static boolean isAfter(LocalDateTime localDateTime1, LocalDateTime localDateTime2) {
		return localDateTime1.isAfter(localDateTime2);
	}

	public static Instant nowInstant() {
		return Instant.now();
	}

	public static long betweenSeconds(Instant instant1, Instant instant2) {
		return ChronoUnit.SECONDS.between(instant1, instant2);
	}

	/**
	 * 判断 d1 在 d2 前.
	 * @param localDateTime1 时间1
	 * @param localDateTime2 时间2
	 * @return 判断结果
	 */
	public static boolean isBefore(LocalDateTime localDateTime1, LocalDateTime localDateTime2) {
		return localDateTime1.isBefore(localDateTime2);
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
	 * 字符串转换时间.
	 * @param dateTime 时间
	 * @param index 索引
	 * @return 时间
	 */
	public static LocalDateTime parseTime(String dateTime, int index) {
		return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(getTimePattern(index)));
	}

	/**
	 * 字符串转换日期.
	 * @param date 日期
	 * @param index 索引
	 * @return 日期
	 */
	public static LocalDate parseDate(String date, int index) {
		return LocalDate.parse(date, DateTimeFormatter.ofPattern(getTimePattern(index)));
	}

	public static ZoneOffset getDefaultZoneOffset() {
		return OffsetDateTime.now().getOffset();
	}

	public static ZoneOffset getZoneOffset(ZoneId zoneId) {
		return OffsetDateTime.now(zoneId).getOffset();
	}

	public static Instant parsInstant(String instant, ZoneId zoneId, int index) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(getTimePattern(index));
		LocalDateTime localDateTime = LocalDateTime.parse(instant, dateTimeFormatter);
		return localDateTime.toInstant(getZoneOffset(zoneId));
	}

	public static Instant parsInstant(String instant, int index) {
		return parsInstant(instant, getDefaultZoneId(), index);
	}

	/**
	 * 获取 前/后 x天 的时间.
	 * @param localDateTime 时间
	 * @param days 天
	 * @return 时间
	 */
	public static LocalDateTime plusDays(LocalDateTime localDateTime, long days) {
		return localDateTime.plusDays(days);
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
	 * 获取 前/后 x秒 的时间.
	 * @param localDateTime 时间
	 * @param seconds 秒
	 * @return 时间
	 */
	public static LocalDateTime plusSeconds(LocalDateTime localDateTime, long seconds) {
		return localDateTime.plusSeconds(seconds);
	}

	/**
	 * 获取 前/后 x月 的时间.
	 * @param localDateTime 时间
	 * @param months 月
	 * @return 时间
	 */
	public static LocalDateTime plusMonths(LocalDateTime localDateTime, long months) {
		return localDateTime.plusMonths(months);
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
	 * 获取 前/后 x年 的时间.
	 * @param localDateTime 时间
	 * @param years 年
	 * @return 时间
	 */
	public static LocalDateTime plusYears(LocalDateTime localDateTime, long years) {
		return localDateTime.plusYears(years);
	}

	/**
	 * 现在时间.
	 * @return 时间
	 */
	public static LocalDateTime now() {
		return LocalDateTime.now();
	}

	/**
	 * 现在日期.
	 * @return 日期
	 */
	public static LocalDate nowDate() {
		return LocalDate.now();
	}

	/**
	 * 时间戳转时间.
	 * @param timestamp 时间戳
	 * @return 时间
	 */
	public static LocalDateTime getLocalDateTimeOfTimestamp(long timestamp, ZoneId zoneId) {
		Instant instant = Instant.ofEpochMilli(timestamp);
		return LocalDateTime.ofInstant(instant, zoneId);
	}

	/**
	 * 时间转时间戳.
	 * @param localDateTime 时间
	 * @return 时间戳
	 */
	public static long getTimestampOfLocalDateTime(LocalDateTime localDateTime) {
		ZoneId zoneId = ZoneId.systemDefault();
		return localDateTime.atZone(zoneId).toInstant().toEpochMilli();
	}

	/**
	 * 开始时间到结束时间相差多少天.
	 * @param start 开始时间
	 * @param end 结束时间
	 * @return 天
	 */
	public static long getDays(LocalDateTime start, LocalDateTime end) {
		return Duration.between(start, end).toDays();
	}

	/**
	 * 开始日期到结束日期相差多少天.
	 * @param start 开始日期
	 * @param end 结束日期
	 * @return 天
	 */
	public static long getDays(LocalDate start, LocalDate end) {
		return Period.between(start, end).getDays();
	}

	/**
	 * 开始时间到结束时间相差多少小时.
	 * @param start 开始时间
	 * @param end 结束时间
	 * @return 小时
	 */
	public static long getHours(LocalDateTime start, LocalDateTime end) {
		return Duration.between(start, end).toHours();
	}

	/**
	 * 开始日期到结束日期相差多少月.
	 * @param start 开始日期
	 * @param end 结束日期
	 * @return 月
	 */
	public static long getMonths(LocalDate start, LocalDate end) {
		return Period.between(start, end).getMonths();
	}

	/**
	 * 开始日期到结束日期相差多少年.
	 * @param start 开始日期
	 * @param end 结束日期
	 * @return 年
	 */
	public static long getYears(LocalDate start, LocalDate end) {
		return Period.between(start, end).getYears();
	}

	public static LocalDate getDayOfWeek(LocalDate localDate, int index) {
		return localDate.with(TemporalAdjusters.nextOrSame(getWeekPattern(index)));
	}

	/**
	 * 开始时间到结束时间相差多少分钟.
	 * @param start 开始时间
	 * @param end 结束时间
	 * @return 分钟
	 */
	public static long getMinutes(LocalDateTime start, LocalDateTime end) {
		return Duration.between(start, end).toMinutes();
	}

	/**
	 * 开始时间到结束时间相差多少秒.
	 * @param start 开始时间
	 * @param end 结束时间
	 * @return 秒
	 */
	public static long getSeconds(LocalDateTime start, LocalDateTime end) {
		return Duration.between(start, end).toSeconds();
	}

	/**
	 * 开始时间到结束时间相差多少毫秒.
	 * @param start 开始时间
	 * @param end 结束时间
	 * @return 毫秒
	 */
	public static long getMillis(LocalDateTime start, LocalDateTime end) {
		return Duration.between(start, end).toMillis();
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

	public static final class Constant {

		/**
		 * yyyy-MM-dd HH:mm:ss.
		 */
		public static final String YYYY_ROD_MM_ROD_DD_SPACE_HH_RISK_HH_RISK_SS = "yyyy-MM-dd HH:mm:ss";

		/**
		 * yyyy-MM-dd HH:mm:ss.SSS.
		 */
		public static final String YYYY_ROD_MM_ROD_DD_SPACE_HH_RISK_HH_RISK_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";

		/**
		 * yyyyMMddHHmmss.
		 */
		public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

		/**
		 * yyyyMM.
		 */
		public static final String YYYYMM = "yyyyMM";

		/**
		 * yyyy-MM-dd.
		 */
		public static final String YYYY_ROD_MM_ROD_DD = "yyyy-MM-dd";

		/**
		 * yyyy年MM月dd日.
		 */
		public static final String YYYY_TEXT_MM_TEXT_DD_TEXT = "yyyy年MM月dd日";

		/**
		 * yyyy.MM.dd.
		 */
		public static final String YYYY_DOT_MM_DOT_DD = "yyyy.MM.dd";

		/**
		 * yyyyMMdd.
		 */
		public static final String YYYYMMDD = "yyyyMMdd";

		/**
		 * GMT+8.
		 */
		public static final String DEFAULT_TIMEZONE = "GMT+8";

		private Constant() {

		}

	}

}

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
public final class DateUtils {

	/**
	 * yyyy-MM-dd HH:mm:ss.
	 */
	public static final String YYYY_B_MM_B_DD_HH_R_MM_R_SS = "yyyy-MM-dd HH:mm:ss";

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
	public static final String YYYY_B_MM_B_DD = "yyyy-MM-dd";

	/**
	 * yyyy年MM月dd日.
	 */
	public static final String YYYY_T_MM_T_DD_T = "yyyy年MM月dd日";

	/**
	 * yyyy.MM.dd.
	 */
	public static final String YYYY_D_MM_D_DD = "yyyy.MM.dd";

	/**
	 * yyyyMMdd.
	 */
	public static final String YYYYMMDD = "yyyyMMdd";

	/**
	 * GMT+8.
	 */
	public static final String DEFAULT_TIMEZONE = "GMT+8";

	/**
	 * yyyy-MM-dd HH:mm:ss.SSS.
	 */
	public static final String YYYY_B_MM_B_DD_HH_R_MM_R_SS_D_SSS = "yyyy-MM-dd HH:mm:ss.SSS";

	private DateUtils() {
	}

	/**
	 * 时间格式化.
	 * @param localDateTime 时间
	 * @param pattern 格式
	 * @return 字符串
	 */
	public static String format(LocalDateTime localDateTime, String pattern) {
		DateTimeFormatter dateTimeFormatter = getDateTimeFormatter(pattern);
		return localDateTime.format(dateTimeFormatter);
	}

	/**
	 * 日期格式化.
	 * @param localDate 日期
	 * @param pattern 格式
	 * @return 字符串
	 */
	public static String format(LocalDate localDate, String pattern) {
		DateTimeFormatter dateTimeFormatter = getDateTimeFormatter(pattern);
		return localDate.format(dateTimeFormatter);
	}

	public static String format(Instant instant, ZoneId zoneId, String pattern) {
		// 指定时区
		ZonedDateTime zonedDateTime = instant.atZone(zoneId);
		DateTimeFormatter dateTimeFormatter = getDateTimeFormatter(pattern);
		return zonedDateTime.format(dateTimeFormatter);
	}

	public static String format(Instant instant, String pattern) {
		// 指定时区
		ZonedDateTime zonedDateTime = instant.atZone(getDefaultZoneId());
		DateTimeFormatter dateTimeFormatter = getDateTimeFormatter(pattern);
		return zonedDateTime.format(dateTimeFormatter);
	}

	public static ZoneId getDefaultZoneId() {
		return ZoneId.systemDefault();
	}

	/**
	 * 格式化配置.
	 * @param pattern 格式
	 * @return 格式化配置
	 */
	public static DateTimeFormatter getDateTimeFormatter(String pattern) {
		return DateTimeFormatter.ofPattern(pattern).withZone(getDefaultZoneId());
	}

	/**
	 * 格式化配置.
	 * @param pattern 格式
	 * @param zoneId 时区
	 * @return 格式化配置
	 */
	public static DateTimeFormatter getDateTimeFormatter(String pattern, ZoneId zoneId) {
		return DateTimeFormatter.ofPattern(pattern).withZone(zoneId);
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

	public static Instant plusHours(Instant instant, long hours) {
		return instant.plus(Duration.ofHours(hours));
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
	 * @param pattern 格式
	 * @return 时间
	 */
	public static LocalDateTime parseTime(String dateTime, String pattern) {
		return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(pattern));
	}

	/**
	 * 字符串转换日期.
	 * @param date 日期
	 * @param pattern 格式
	 * @return 日期
	 */
	public static LocalDate parseDate(String date, String pattern) {
		return LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern));
	}

	public static ZoneOffset getDefaultZoneOffset() {
		return OffsetDateTime.now().getOffset();
	}

	public static ZoneOffset getZoneOffset(ZoneId zoneId) {
		return OffsetDateTime.now(zoneId).getOffset();
	}

	public static Instant parsInstant(String instant, ZoneId zoneId, String pattern) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
		LocalDateTime localDateTime = LocalDateTime.parse(instant, dateTimeFormatter);
		return localDateTime.toInstant(getZoneOffset(zoneId));
	}

	public static Instant parsInstant(String instant, String pattern) {
		return parsInstant(instant, getDefaultZoneId(), pattern);
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
		return LocalDateTime.ofInstant(getInstantOfTimestamp(timestamp), zoneId);
	}

	public static Instant getInstantOfTimestamp(long timestamp) {
		return Instant.ofEpochMilli(timestamp);
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

	public static LocalDate getDayOfWeek(LocalDate localDate, int dayOfWeek) {
		return localDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.of(dayOfWeek)));
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

}

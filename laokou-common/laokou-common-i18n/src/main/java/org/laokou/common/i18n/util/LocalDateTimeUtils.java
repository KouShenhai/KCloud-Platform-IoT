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

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @author laokou
 */
public final class LocalDateTimeUtils {

	private LocalDateTimeUtils() {
	}

	/**
	 * 时间格式化.
	 * @param localDateTime 时间
	 * @param pattern 格式
	 * @return 字符串
	 */
	public static String format(LocalDateTime localDateTime, String pattern) {
		DateTimeFormatter dateTimeFormatter = InstantUtils.getDateTimeFormatter(pattern);
		return localDateTime.format(dateTimeFormatter);
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
	 * 开始时间到结束时间相差多少毫秒.
	 * @param start 开始时间
	 * @param end 结束时间
	 * @return 毫秒
	 */
	public static long betweenMillis(LocalDateTime start, LocalDateTime end) {
		return Duration.between(start, end).toMillis();
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
	 * 获取 前/后 x天 的时间.
	 * @param localDateTime 时间
	 * @param days 天
	 * @return 时间
	 */
	public static LocalDateTime plusDays(LocalDateTime localDateTime, long days) {
		return localDateTime.plusDays(days);
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
	 * 时间转时间戳.
	 * @param localDateTime 时间
	 * @return 时间戳
	 */
	public static long getTimestampOfLocalDateTime(LocalDateTime localDateTime) {
		ZoneId zoneId = ZoneId.systemDefault();
		return localDateTime.atZone(zoneId).toInstant().toEpochMilli();
	}

	/**
	 * 时间戳转时间.
	 * @param timestamp 时间戳
	 * @return 时间
	 */
	public static LocalDateTime getLocalDateTimeOfTimestamp(long timestamp, ZoneId zoneId) {
		return LocalDateTime.ofInstant(InstantUtils.getInstantOfTimestamp(timestamp), zoneId);
	}

	/**
	 * 开始时间到结束时间相差多少小时.
	 * @param start 开始时间
	 * @param end 结束时间
	 * @return 小时
	 */
	public static long betweenHours(LocalDateTime start, LocalDateTime end) {
		return Duration.between(start, end).toHours();
	}

	/**
	 * 开始时间到结束时间相差多少天.
	 * @param start 开始时间
	 * @param end 结束时间
	 * @return 天
	 */
	public static long betweenDays(LocalDateTime start, LocalDateTime end) {
		return Duration.between(start, end).toDays();
	}

	/**
	 * 开始时间到结束时间相差多少分钟.
	 * @param start 开始时间
	 * @param end 结束时间
	 * @return 分钟
	 */
	public static long betweenMinutes(LocalDateTime start, LocalDateTime end) {
		return Duration.between(start, end).toMinutes();
	}

	/**
	 * 开始时间到结束时间相差多少秒.
	 * @param start 开始时间
	 * @param end 结束时间
	 * @return 秒
	 */
	public static long betweenSeconds(LocalDateTime start, LocalDateTime end) {
		return Duration.between(start, end).toSeconds();
	}

}

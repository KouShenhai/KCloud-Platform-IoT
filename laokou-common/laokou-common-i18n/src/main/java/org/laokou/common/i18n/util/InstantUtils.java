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

package org.laokou.common.i18n.util;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * @author laokou
 */
public final class InstantUtils {

	private InstantUtils() {
	}

	public static Instant now() {
		return Instant.now();
	}

	public static Instant plusHours(Instant instant, long hours) {
		return instant.plus(Duration.ofHours(hours));
	}

	public static Instant plusSeconds(Instant instant, long hours) {
		return instant.plus(Duration.ofSeconds(hours));
	}

	public static long betweenSeconds(Instant instant1, Instant instant2) {
		return ChronoUnit.SECONDS.between(instant1, instant2);
	}

	public static long betweenHours(Instant instant1, Instant instant2) {
		return ChronoUnit.HOURS.between(instant1, instant2);
	}

	public static Instant parse(String instant, ZoneId zoneId, String pattern) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
		LocalDateTime localDateTime = LocalDateTime.parse(instant, dateTimeFormatter);
		return localDateTime.toInstant(getZoneOffset(zoneId));
	}

	public static String format(Instant instant, ZoneId zoneId, String pattern) {
		// 指定时区
		ZonedDateTime zonedDateTime = instant.atZone(zoneId);
		DateTimeFormatter dateTimeFormatter = getDateTimeFormatter(pattern);
		return zonedDateTime.format(dateTimeFormatter);
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
	 * 格式化配置.
	 * @param pattern 格式
	 * @return 格式化配置
	 */
	public static DateTimeFormatter getDateTimeFormatter(String pattern) {
		return DateTimeFormatter.ofPattern(pattern);
	}

	public static Instant getInstantOfTimestamp(long timestamp) {
		return Instant.ofEpochMilli(timestamp);
	}

	public static long getTimestampOfInstant(Instant instant) {
		return instant.toEpochMilli();
	}

	private static ZoneId getDefaultZoneId() {
		return ZoneId.systemDefault();
	}

	private static ZoneOffset getZoneOffset(ZoneId zoneId) {
		return OffsetDateTime.now(zoneId).getOffset();
	}

}

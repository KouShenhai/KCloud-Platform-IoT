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

package org.laokou.common.cors.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.config.CustomInstantDeserializer;
import org.laokou.common.core.config.CustomInstantSerializer;
import org.laokou.common.i18n.util.DateUtils;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.TimeZone;

/**
 * 消息转换器配置.
 *
 * @author laokou
 */
@Slf4j
@AutoConfiguration
public class HttpMessageConverterAutoConfig {

	// @formatter:off
	@Bean("jackson2HttpMessageConverter")
	@Order(Ordered.LOWEST_PRECEDENCE - 10000)
	public MappingJackson2HttpMessageConverter jackson2HttpMessageConverter() {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		ObjectMapper mapper = new ObjectMapper();
		// 反序列化时，属性不存在的兼容处理
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// 时区
		TimeZone timeZone = TimeZone.getTimeZone(DateUtils.DEFAULT_TIMEZONE);
		DateTimeFormatter dateTimeFormatter = DateUtils.getDateTimeFormatter(DateUtils.YYYY_B_MM_B_DD_HH_R_MM_R_SS);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtils.YYYY_B_MM_B_DD_HH_R_MM_R_SS);
		simpleDateFormat.setTimeZone(timeZone);
		mapper.setDateFormat(simpleDateFormat);
		mapper.setTimeZone(timeZone);
		// Long类型转String类型
		JavaTimeModule javaTimeModule = new JavaTimeModule();
		javaTimeModule.addSerializer(Long.class, ToStringSerializer.instance);
		javaTimeModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
		// LocalDateTime
		javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
		javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));
		// Instant
		javaTimeModule.addSerializer(Instant.class, new CustomInstantSerializer(InstantSerializer.INSTANCE, false,false, dateTimeFormatter));
		javaTimeModule.addDeserializer(Instant.class, new CustomInstantDeserializer(InstantDeserializer.INSTANT, dateTimeFormatter));
		converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
		mapper.registerModule(javaTimeModule);
		converter.setObjectMapper(mapper);
		log.info("{} => jackson配置加载完毕", Thread.currentThread().getName());
		return converter;
	}
	// @formatter:on

}

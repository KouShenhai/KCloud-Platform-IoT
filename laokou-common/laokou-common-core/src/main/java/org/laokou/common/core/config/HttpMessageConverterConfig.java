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

package org.laokou.common.core.config;

import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.common.constant.DateConstants;
import org.laokou.common.i18n.util.InstantUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ext.javatime.deser.InstantDeserializer;
import tools.jackson.databind.ext.javatime.deser.LocalDateTimeDeserializer;
import tools.jackson.databind.ext.javatime.ser.InstantSerializer;
import tools.jackson.databind.ext.javatime.ser.LocalDateTimeSerializer;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;
import tools.jackson.databind.ser.std.ToStringSerializer;

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
@Configuration
class HttpMessageConverterConfig {

	// @formatter:off
	@Bean("jackson2HttpMessageConverter")
	@Order(Ordered.LOWEST_PRECEDENCE - 10000)
	public JacksonJsonHttpMessageConverter jacksonJsonHttpMessageConverter() {
		// 时区
		TimeZone timeZone = TimeZone.getTimeZone(DateConstants.DEFAULT_TIMEZONE);
		DateTimeFormatter dateTimeFormatter = InstantUtils.getDateTimeFormatter(DateConstants.YYYY_B_MM_B_DD_HH_R_MM_R_SS);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateConstants.YYYY_B_MM_B_DD_HH_R_MM_R_SS);
		simpleDateFormat.setTimeZone(timeZone);
		// Long类型转String类型
		SimpleModule simpleModule = new SimpleModule();
		simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
		simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
		// LocalDateTime
		simpleModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
		simpleModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));
		// Instant
		simpleModule.addSerializer(Instant.class, new CustomInstantSerializer(InstantSerializer.INSTANCE, dateTimeFormatter, false,false));
		simpleModule.addDeserializer(Instant.class, new CustomInstantDeserializer(InstantDeserializer.INSTANT, dateTimeFormatter));
		JsonMapper jsonMapper = JsonMapper.builder()
			.findAndAddModules()
			// 反序列化时，属性不存在的兼容处理
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			.defaultDateFormat(simpleDateFormat)
			.defaultTimeZone(timeZone)
			.addModule(simpleModule)
			.build();
		log.info("{} => jackson配置加载完毕", Thread.currentThread().getName());
		JacksonJsonHttpMessageConverter jacksonJsonHttpMessageConverter = new JacksonJsonHttpMessageConverter(jsonMapper);
		jacksonJsonHttpMessageConverter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
		return jacksonJsonHttpMessageConverter;
	}
	// @formatter:on

}

/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.utils.DateUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.TimeZone;

import static org.laokou.common.i18n.utils.DateUtil.Constant.DEFAULT_TIMEZONE;

/**
 * 消息转换器配置.
 *
 * @author laokou
 */
@Configuration
@Slf4j
public class HttpMessageConverterConfig {

	// @formatter:off
	@Bean("jackson2HttpMessageConverter")
	@Order(Ordered.LOWEST_PRECEDENCE - 10000)
	public MappingJackson2HttpMessageConverter jackson2HttpMessageConverter() {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		ObjectMapper mapper = new ObjectMapper();
		// 反序列化时，属性不存在的兼容处理
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// 时区
		TimeZone timeZone = TimeZone.getTimeZone(DEFAULT_TIMEZONE);
		DateTimeFormatter dateTimeFormatter = DateUtil.getDateTimeFormatter(DateUtil.YYYY_ROD_MM_ROD_DD_SPACE_HH_RISK_HH_RISK_SS);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtil.getTimePattern(DateUtil.YYYY_ROD_MM_ROD_DD_SPACE_HH_RISK_HH_RISK_SS));
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
		converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
		mapper.registerModule(javaTimeModule);
		converter.setObjectMapper(mapper);
		log.info("jackson配置加载完毕");
		return converter;
	}
	// @formatter:on

}

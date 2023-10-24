/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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
package org.laokou.common.redis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.laokou.common.i18n.utils.DateUtil;
import org.redisson.codec.JsonJacksonCodec;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping.NON_FINAL;

/**
 * @author laokou
 */
public class GlobalJsonJacksonCodec extends JsonJacksonCodec {

	public static final GlobalJsonJacksonCodec INSTANCE = new GlobalJsonJacksonCodec();

	public GlobalJsonJacksonCodec() {
		super(objectMapper());
	}

	public static ObjectMapper objectMapper() {
		// 解决查询缓存转换异常的问题
		ObjectMapper objectMapper = JsonMapper.builder().build();
		DateTimeFormatter dateTimeFormatter = DateUtil.getDateTimeFormatter(DateUtil.YYYY_MM_DD_HH_MM_SS);
		// Long类型转String类型
		JavaTimeModule javaTimeModule = new JavaTimeModule();
		javaTimeModule.addSerializer(Long.class, ToStringSerializer.instance);
		javaTimeModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
		// LocalDateTime
		javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
		javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));
		objectMapper.registerModule(javaTimeModule);
		objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, NON_FINAL, JsonTypeInfo.As.PROPERTY);
		return objectMapper;
	}

}

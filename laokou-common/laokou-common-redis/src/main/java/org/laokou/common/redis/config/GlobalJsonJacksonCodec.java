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

package org.laokou.common.redis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.laokou.common.core.config.CustomInstantDeserializer;
import org.laokou.common.core.config.CustomInstantSerializer;
import org.laokou.common.i18n.context.TenantRedisContextHolder;
import org.laokou.common.i18n.utils.DateUtil;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.redisson.codec.JsonJacksonCodec;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.DigestUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping.NON_FINAL;

/**
 * @author laokou
 */
public final class GlobalJsonJacksonCodec extends JsonJacksonCodec {

	/**
	 * 实例.
	 */
	public static final GlobalJsonJacksonCodec INSTANCE = new GlobalJsonJacksonCodec();

	private GlobalJsonJacksonCodec() {
		super(objectMapper());
	}

	// @formatter:off
	/**
	 * 解决查询缓存转换异常的问题.
	 * @return ObjectMapper
	 */
	private static ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		DateTimeFormatter dateTimeFormatter = DateUtil.getDateTimeFormatter(DateUtil.YYYY_B_MM_B_DD_HH_R_MM_R_SS);
		JavaTimeModule javaTimeModule = new JavaTimeModule();
		// Long类型转String类型
		javaTimeModule.addSerializer(Long.class, ToStringSerializer.instance);
		javaTimeModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
		// LocalDateTime
		javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
		javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));
		// Instant
		javaTimeModule.addSerializer(Instant.class, new CustomInstantSerializer(InstantSerializer.INSTANCE, false,false, dateTimeFormatter));
		javaTimeModule.addDeserializer(Instant.class, new CustomInstantDeserializer(InstantDeserializer.INSTANT, dateTimeFormatter));
		objectMapper.registerModule(javaTimeModule);
		// 所有属性访问器（字段、getter和setter），将自动检测所有字段属性
		objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		// 对于所有非final类型，使用LaissezFaire子类型验证器来推断类型
		objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, NON_FINAL, JsonTypeInfo.As.PROPERTY);
		// 反序列化时，属性不存在的兼容处理
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// 自动查找并注册相关模块
		objectMapper.findAndRegisterModules();
		return objectMapper;
	}
	// @formatter:on

	public static Jackson2JsonRedisSerializer<Object> getJsonRedisSerializer() {
		// Json序列化配置
		return new Jackson2JsonRedisSerializer<>(objectMapper(), Object.class);
	}

	public static StringRedisSerializer getStringRedisSerializer() {
		// String序列化配置【多租户】
		return new TenantStringRedisSerializer(StandardCharsets.UTF_8);
	}

	public static class TenantStringRedisSerializer extends StringRedisSerializer {

		public TenantStringRedisSerializer(Charset charset) {
			super(charset);
		}

		@Nullable
		@Override
		public byte[] serialize(@Nullable String value) {
			Assert.notNull(value, "Cannot serialize null");
			Long tenantId = TenantRedisContextHolder.get();
			if (ObjectUtil.isNotNull(tenantId)) {
				return super.serialize(
						DigestUtils.md5DigestAsHex((tenantId + ":" + value).getBytes(StandardCharsets.UTF_8)));
			}
			return super.serialize(DigestUtils.md5DigestAsHex(value.getBytes(StandardCharsets.UTF_8)));
		}

	}

}

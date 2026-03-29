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

package org.laokou.common.redis.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.jspecify.annotations.NonNull;
import org.laokou.common.core.config.HttpMessageConverterConfig;
import org.redisson.codec.JsonJackson3Codec;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import tools.jackson.databind.DefaultTyping;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;

import java.nio.charset.StandardCharsets;

/**
 * @author laokou
 */
public final class JacksonCodec extends JsonJackson3Codec {

	private JacksonCodec() {
		super(getJsonMapper());
	}

	/**
	 * 对象序列化.
	 */
	public static final RedisSerializer<@NonNull Object> OBJECT_REDIS_SERIALIZER = new JacksonJsonRedisSerializer<>(
			getJsonMapper(), Object.class);

	/**
	 * string序列化.
	 */
	public static final StringRedisSerializer STRING_REDIS_SERIALIZER = new Sha512DigestStringRedisSerializer(
			StandardCharsets.UTF_8);

	/**
	 * 实例.
	 */
	public static final JacksonCodec INSTANCE = new JacksonCodec();

	private static JsonMapper getJsonMapper() {
		JsonMapper.Builder builder = HttpMessageConverterConfig.getJsonMapperBuilder();
		BasicPolymorphicTypeValidator validator = BasicPolymorphicTypeValidator.builder()
			.allowIfSubType("org.laokou")
			.allowIfSubType("org.springframework.cloud.gateway")
			.allowIfSubType("java.util")
			.build();
		builder.activateDefaultTyping(validator, DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
		return builder.build();
	}

}

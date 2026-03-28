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

package org.laokou.gateway.config;

import org.jspecify.annotations.NonNull;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import tools.jackson.databind.json.JsonMapper;

import java.nio.charset.StandardCharsets;

/**
 * @author laokou
 */
@Configuration
public class GatewayConfig {

	@Bean
	ReactiveRedisTemplate<@NonNull String, @NonNull RouteDefinition> reactiveRedisTemplate(
			ReactiveRedisConnectionFactory reactiveRedisConnectionFactory) {
		Sha512DigestStringRedisSerializer stringRedisSerializer = new Sha512DigestStringRedisSerializer(
				StandardCharsets.UTF_8);
		JacksonJsonRedisSerializer<@NonNull RouteDefinition> jsonRedisSerializer = new JacksonJsonRedisSerializer<>(
				JsonMapper.builder().build(), RouteDefinition.class);
		RedisSerializationContext<@NonNull String, @NonNull RouteDefinition> serializationContext = RedisSerializationContext
			.<String, RouteDefinition>newSerializationContext()
			.key(stringRedisSerializer)
			.value(jsonRedisSerializer)
			.hashKey(stringRedisSerializer)
			.hashValue(jsonRedisSerializer)
			.build();
		return new ReactiveRedisTemplate<>(reactiveRedisConnectionFactory, serializationContext);
	}

}

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

import org.laokou.common.redis.utils.ReactiveRedisUtil;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import reactor.core.publisher.Flux;

import static org.laokou.common.redis.config.GlobalJsonJacksonCodec.getJsonRedisSerializer;
import static org.laokou.common.redis.config.GlobalJsonJacksonCodec.getStringRedisSerializer;

/**
 * @author laokou
 */
@AutoConfiguration
@ConditionalOnClass({ RedissonAutoConfig.class, ReactiveRedisConnectionFactory.class, ReactiveRedisTemplate.class,
		Flux.class })
public class ReactiveRedisAutoConfig {

	@Bean("reactiveRedisTemplate")
	@ConditionalOnMissingBean(ReactiveRedisTemplate.class)
	public ReactiveRedisTemplate<String, Object> reactiveRedisTemplate(
			ReactiveRedisConnectionFactory reactiveRedisConnectionFactory) {
		Jackson2JsonRedisSerializer<Object> jsonRedisSerializer = getJsonRedisSerializer();
		StringRedisSerializer stringRedisSerializer = getStringRedisSerializer();
		RedisSerializationContext<String, Object> serializationContext = RedisSerializationContext
			.<String, Object>newSerializationContext()
			.key(stringRedisSerializer)
			.value(jsonRedisSerializer)
			.hashKey(stringRedisSerializer)
			.hashValue(jsonRedisSerializer)
			.build();
		return new ReactiveRedisTemplate<>(reactiveRedisConnectionFactory, serializationContext);
	}

	@Bean
	@ConditionalOnMissingBean(ReactiveStringRedisTemplate.class)
	public ReactiveStringRedisTemplate reactiveStringRedisTemplate(
			ReactiveRedisConnectionFactory reactiveRedisConnectionFactory) {
		return new ReactiveStringRedisTemplate(reactiveRedisConnectionFactory);
	}

	@Bean(destroyMethod = "shutdown")
	@ConditionalOnMissingBean(RedissonReactiveClient.class)
	public RedissonReactiveClient redissonReactiveClient(RedissonClient redissonClient) {
		return redissonClient.reactive();
	}

	@Bean
	@ConditionalOnMissingBean(ReactiveRedisUtil.class)
	public ReactiveRedisUtil reactiveRedisUtil(RedissonReactiveClient redissonReactiveClient,
			ReactiveRedisTemplate<String, Object> reactiveRedisTemplate) {
		return new ReactiveRedisUtil(reactiveRedisTemplate, redissonReactiveClient);
	}

}

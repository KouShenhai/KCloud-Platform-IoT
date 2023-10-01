/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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
package org.laokou.common.redis.config.auto;

import org.laokou.common.redis.config.RedissonConfig;
import org.laokou.common.redis.utils.ReactiveRedisUtil;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import reactor.core.publisher.Flux;

/**
 * @author laokou
 */
@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@ConditionalOnClass({ RedissonConfig.class, ReactiveRedisConnectionFactory.class, ReactiveRedisTemplate.class,
		Flux.class })
public class ReactiveRedisAutoConfig {

	@Bean
	@ConditionalOnMissingBean(name = "reactiveRedisTemplate")
	public ReactiveRedisTemplate<Object, Object> reactiveRedisTemplate(
			ReactiveRedisConnectionFactory reactiveRedisConnectionFactory, ResourceLoader resourceLoader) {
		JdkSerializationRedisSerializer jdkSerializer = new JdkSerializationRedisSerializer(
				resourceLoader.getClassLoader());
		RedisSerializationContext<Object, Object> serializationContext = RedisSerializationContext
			.newSerializationContext()
			.key(jdkSerializer)
			.value(jdkSerializer)
			.hashKey(jdkSerializer)
			.hashValue(jdkSerializer)
			.build();
		return new ReactiveRedisTemplate<>(reactiveRedisConnectionFactory, serializationContext);
	}

	@Bean
	@ConditionalOnMissingBean(name = "reactiveStringRedisTemplate")
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
	public ReactiveRedisUtil reactiveRedisUtil(RedissonReactiveClient redissonReactiveClient) {
		return new ReactiveRedisUtil(redissonReactiveClient);
	}

}

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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.laokou.common.redis.config.CustomJsonJacksonCodec;
import org.laokou.common.redis.utils.RedisUtil;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis配置
 *
 * @author laokou
 */
@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass(LettuceConnectionFactory.class)
public class RedisAutoConfig {

	/**
	 * 自定义RedisTemplate
	 * @return RedisTemplate<String, Object>
	 */
	@Bean("redisTemplate")
	@ConditionalOnMissingBean(RedisTemplate.class)
	public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(lettuceConnectionFactory);
		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = getJsonRedisSerializer();
		// string序列化
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		// key
		redisTemplate.setKeySerializer(stringRedisSerializer);
		// value
		redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
		// hash-key
		redisTemplate.setHashKeySerializer(stringRedisSerializer);
		// hash-value
		redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
		// 初始化
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}

	private Jackson2JsonRedisSerializer<Object> getJsonRedisSerializer() {
		// Json序列化配置
		ObjectMapper objectMapper = CustomJsonJacksonCodec.getObjectMapper();
		return new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);
	}

	@Bean
	public RedisUtil redisUtil(RedissonClient redissonClient, RedisTemplate<String, Object> redisTemplate) {
		return new RedisUtil(redisTemplate, redissonClient);
	}

}

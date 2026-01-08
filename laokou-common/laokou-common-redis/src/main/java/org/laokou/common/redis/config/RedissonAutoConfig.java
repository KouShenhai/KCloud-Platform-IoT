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

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.data.redis.autoconfigure.DataRedisProperties;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.ExecutorService;

/**
 * @author livk
 * @author laokou
 */
@AutoConfiguration
@ConditionalOnClass(Redisson.class)
@EnableConfigurationProperties(DataRedisProperties.class)
public class RedissonAutoConfig {

	/**
	 * redisson配置.
	 * @param springRedissonProperties redisson配置文件
	 * @return RedissonClient
	 */
	@Bean(name = "redisClient", destroyMethod = "shutdown")
	public RedissonClient redisClient(SpringRedissonProperties springRedissonProperties,
			ExecutorService virtualThreadExecutor) {
		return Redisson.create(springRedissonProperties.getConfig(virtualThreadExecutor));
	}

}

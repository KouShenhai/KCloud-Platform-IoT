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

package org.laokou.gateway.repository;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.redis.testcontainers.RedisStackContainer;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.laokou.common.redis.config.JacksonCodec;
import org.laokou.common.testcontainers.container.NacosContainer;
import org.laokou.common.testcontainers.util.DockerImageNames;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * @author laokou
 */
@Testcontainers
@DisplayName("Nacos Route Definition Repository Tests")
public class NacosRouteDefinitionRepositoryTest {

	@Container
	static NacosContainer nacosContainer = new NacosContainer(DockerImageNames.nacos(), 18848, 19848);

	@Container
	static RedisStackContainer redisContainer = new RedisStackContainer(DockerImageNames.redis());

	private NacosConfigManager nacosConfigManager;

	private ReactiveRedisTemplate<@NonNull String, @NonNull Object> reactiveRedisTemplate;

	@BeforeEach
	void setUp() {
		NacosConfigProperties nacosConfigProperties = new NacosConfigProperties();
		nacosConfigProperties.setServerAddr(nacosContainer.getServerAddr());
		nacosConfigProperties.setNamespace("public");
		nacosConfigProperties.setGroup("DEFAULT_GROUP");
		nacosConfigProperties.setUsername("nacos");
		nacosConfigProperties.setPassword("nacos");
		nacosConfigManager = new NacosConfigManager(nacosConfigProperties);
		LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisContainer.getHost(), redisContainer.getRedisPort());
		RedisSerializationContext<@NonNull String, @NonNull Object> serializationContext = RedisSerializationContext
			.<String, Object>newSerializationContext()
			.key(JacksonCodec.STRING_REDIS_SERIALIZER)
			.value(JacksonCodec.OBJECT_REDIS_SERIALIZER)
			.hashKey(JacksonCodec.STRING_REDIS_SERIALIZER)
			.hashValue(JacksonCodec.OBJECT_REDIS_SERIALIZER)
			.build();
		reactiveRedisTemplate = new ReactiveRedisTemplate<>(lettuceConnectionFactory, serializationContext);
	}

	@Test
	void test() {
	}

}

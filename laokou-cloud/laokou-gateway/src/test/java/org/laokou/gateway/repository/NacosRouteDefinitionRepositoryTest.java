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
import com.redis.testcontainers.RedisStackContainer;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.laokou.common.testcontainers.container.NacosContainer;
import org.laokou.common.testcontainers.util.DockerImageNames;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * @author laokou
 */
@Testcontainers
@EnableDiscoveryClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
		classes = { com.alibaba.cloud.nacos.NacosConfigAutoConfiguration.class })
@DisplayName("Nacos Route Definition Repository Tests")
public class NacosRouteDefinitionRepositoryTest {

	@Container
	static NacosContainer nacosContainer = new NacosContainer(DockerImageNames.nacos(), 18848, 19848);

	@Container
	static RedisStackContainer redisContainer = new RedisStackContainer(DockerImageNames.redis());

	private NacosConfigManager nacosConfigManager;

	private ReactiveRedisTemplate<@NonNull String, @NonNull Object> reactiveRedisTemplate;

	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry) {
		registry.add("spring.data.redis.host", redisContainer::getHost);
		registry.add("spring.data.redis.port", redisContainer::getFirstMappedPort);
		registry.add("spring.cloud.nacos.discovery.server-addr", nacosContainer::getServerAddr);
		registry.add("spring.cloud.nacos.config.server-addr", nacosContainer::getServerAddr);
		registry.add("spring.cloud.nacos.discovery.namespace", () -> "public");
		registry.add("spring.cloud.nacos.config.namespace", () -> "public");
		registry.add("spring.cloud.nacos.discovery.group", () -> "DEFAULT_GROUP");
		registry.add("spring.cloud.nacos.config.group", () -> "DEFAULT_GROUP");
		registry.add("spring.cloud.nacos.discovery.username", () -> "nacos");
		registry.add("spring.cloud.nacos.config.username", () -> "nacos");
		registry.add("spring.cloud.nacos.discovery.password", () -> "nacos");
		registry.add("spring.cloud.nacos.config.password", () -> "nacos");
	}

	@BeforeAll
	static void setUp() {
	}

	@Test
	void testSyncRouter() {

	}

}

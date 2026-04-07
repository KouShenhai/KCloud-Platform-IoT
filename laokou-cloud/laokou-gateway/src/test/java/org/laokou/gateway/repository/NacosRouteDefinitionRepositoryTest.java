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
import com.alibaba.nacos.api.config.ConfigService;
import com.redis.testcontainers.RedisStackContainer;
import org.assertj.core.api.Assertions;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.laokou.common.i18n.util.ResourceExtUtils;
import org.laokou.common.i18n.util.SpringContextUtils;
import org.laokou.common.i18n.util.StringExtUtils;
import org.laokou.common.redis.config.JacksonCodec;
import org.laokou.common.testcontainers.container.NacosContainer;
import org.laokou.common.testcontainers.util.DockerImageNames;
import org.springframework.boot.web.context.reactive.AnnotationConfigReactiveWebApplicationContext;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

	private NacosRouteDefinitionRepository nacosRouteDefinitionRepository;

	private LettuceConnectionFactory lettuceConnectionFactory;

	private ExecutorService virtualThreadExecutor;

	@BeforeEach
	void setUp() throws Exception {
		// 初始化Nacos配置管理器
		NacosConfigProperties nacosConfigProperties = new NacosConfigProperties();
		nacosConfigProperties.setServerAddr(nacosContainer.getServerAddr());
		nacosConfigProperties.setNamespace("public");
		nacosConfigProperties.setGroup("DEFAULT_GROUP");
		nacosConfigProperties.setUsername("nacos");
		nacosConfigProperties.setPassword("nacos");
		nacosConfigManager = new NacosConfigManager(nacosConfigProperties);
		// 初始化Redis连接工厂
		lettuceConnectionFactory = new LettuceConnectionFactory(redisContainer.getHost(),
				redisContainer.getRedisPort());
		lettuceConnectionFactory.afterPropertiesSet();
		// 初始化ReactiveRedisTemplate
		RedisSerializationContext<@NonNull String, @NonNull Object> serializationContext = RedisSerializationContext
			.<String, Object>newSerializationContext()
			.key(JacksonCodec.STRING_REDIS_SERIALIZER)
			.value(JacksonCodec.OBJECT_REDIS_SERIALIZER)
			.hashKey(JacksonCodec.STRING_REDIS_SERIALIZER)
			.hashValue(JacksonCodec.OBJECT_REDIS_SERIALIZER)
			.build();
		ReactiveRedisTemplate<@NonNull String, @NonNull Object> reactiveRedisTemplate = new ReactiveRedisTemplate<>(
				lettuceConnectionFactory, serializationContext);
		// 初始化虚拟线程执行器
		virtualThreadExecutor = Executors.newVirtualThreadPerTaskExecutor();
		// 初始化NacosRouteDefinitionRepository
		nacosRouteDefinitionRepository = new NacosRouteDefinitionRepository(nacosConfigManager, virtualThreadExecutor,
				reactiveRedisTemplate);
		// 设置ApplicationContext（syncRouter内部会调用SpringContextUtils.publishEvent）
		Field applicationContextField = SpringContextUtils.class.getDeclaredField("applicationContext");
		applicationContextField.setAccessible(true);
		applicationContextField.set(null, new AnnotationConfigReactiveWebApplicationContext());
	}

	@AfterEach
	void tearDown() {
		if (lettuceConnectionFactory != null) {
			lettuceConnectionFactory.destroy();
		}
		if (virtualThreadExecutor != null) {
			virtualThreadExecutor.shutdown();
		}
	}

	@Test
	@DisplayName("Test publish route config to Nacos and sync to Redis")
	void testPublishConfigAndSyncRouter() throws Exception {
		// 获取Nacos配置服务
		ConfigService configService = nacosConfigManager.getConfigService();
		// 发布路由配置到Nacos
		String routerJson = ResourceExtUtils.getResource("router.json").getContentAsString(StandardCharsets.UTF_8);
		Assertions.assertThatNoException()
			.isThrownBy(() -> configService.publishConfig("router.json", "DEFAULT_GROUP", routerJson));
		// 等待Nacos配置生效
		Thread.sleep(Duration.ofSeconds(5));
		// 同步路由到Redis
		Assertions.assertThatNoException()
			.isThrownBy(() -> nacosRouteDefinitionRepository.syncRouter()
				.take(Duration.ofSeconds(15))
				.subscribeOn(Schedulers.boundedElastic())
				.block(Duration.ofSeconds(20)));
		// 从Redis获取路由定义并验证
		List<RouteDefinition> routeDefinitions = nacosRouteDefinitionRepository.getRouteDefinitions()
			.collectList()
			.block(Duration.ofSeconds(10));
		Assertions.assertThat(routeDefinitions).isNotNull();
		Assertions.assertThat(routeDefinitions).hasSize(5);
		// 验证路由ID
		List<String> routeIds = routeDefinitions.stream()
			.map(RouteDefinition::getId)
			.filter(StringExtUtils::isNotEmpty)
			.sorted()
			.toList();
		Assertions.assertThat(routeIds)
			.containsExactly("laokou-admin", "laokou-auth", "laokou-generator", "laokou-iot", "laokou-iot-websocket");
	}

	@Test
	@DisplayName("Test save and delete methods return empty Mono")
	void testSaveAndDeleteReturnEmpty() {
		Assertions.assertThatNoException()
			.isThrownBy(() -> nacosRouteDefinitionRepository.save(Mono.empty()).block(Duration.ofSeconds(5)));
		Assertions.assertThatNoException()
			.isThrownBy(() -> nacosRouteDefinitionRepository.delete(Mono.empty()).block(Duration.ofSeconds(5)));
	}

	@Test
	@DisplayName("Test re-sync router overwrites old routes")
	void testResyncRouterOverwritesOldRoutes() throws Exception {
		ConfigService configService = nacosConfigManager.getConfigService();
		// 第一次发布完整路由
		String routerJson = ResourceExtUtils.getResource("router.json").getContentAsString(StandardCharsets.UTF_8);
		Assertions.assertThatNoException()
			.isThrownBy(() -> configService.publishConfig("router.json", "DEFAULT_GROUP", routerJson));
		Thread.sleep(Duration.ofSeconds(5));
		nacosRouteDefinitionRepository.syncRouter()
			.take(Duration.ofSeconds(15))
			.subscribeOn(Schedulers.boundedElastic())
			.block(Duration.ofSeconds(20));
		List<RouteDefinition> firstSync = nacosRouteDefinitionRepository.getRouteDefinitions()
			.collectList()
			.block(Duration.ofSeconds(10));
		Assertions.assertThat(firstSync).hasSize(5);
		// 第二次发布少量路由
		String partialRouterJson = """
				[
				  {
				    "id": "laokou-auth",
				    "uri": "lb://laokou-auth",
				    "predicates": [
				      {
				        "name": "Path",
				        "args": {
				          "pattern": "/api-gateway/auth/**"
				        }
				      }
				    ],
				    "filters": [],
				    "metadata": {},
				    "order": 1
				  }
				]
				""";
		configService.publishConfig("router.json", "DEFAULT_GROUP", partialRouterJson);
		Thread.sleep(Duration.ofSeconds(5));
		// 重新同步
		Assertions.assertThatNoException()
			.isThrownBy(() -> nacosRouteDefinitionRepository.syncRouter()
				.take(Duration.ofSeconds(15))
				.subscribeOn(Schedulers.boundedElastic())
				.block(Duration.ofSeconds(20)));
		List<RouteDefinition> secondSync = nacosRouteDefinitionRepository.getRouteDefinitions()
			.collectList()
			.block(Duration.ofSeconds(10));
		Assertions.assertThat(secondSync).isNotNull();
		Assertions.assertThat(secondSync).hasSize(1);
		Assertions.assertThat(secondSync.getFirst().getId()).isEqualTo("laokou-auth");
	}

}

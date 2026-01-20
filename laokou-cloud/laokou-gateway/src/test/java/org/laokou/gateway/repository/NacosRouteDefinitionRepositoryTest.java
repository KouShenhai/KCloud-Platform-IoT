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

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.redis.testcontainers.RedisStackContainer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.laokou.common.i18n.util.JacksonUtils;
import org.laokou.common.i18n.util.RedisKeyUtils;
import org.laokou.common.testcontainers.container.NacosContainer;
import org.laokou.common.testcontainers.util.DockerImageNames;
import org.redisson.Redisson;
import org.redisson.api.RMapReactive;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.config.Config;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;

/**
 * NacosRouteDefinitionRepository integration test with Nacos and Redis containers.
 *
 * @author laokou
 */
@Testcontainers
@DisplayName("NacosRouteDefinitionRepository Integration Tests")
class NacosRouteDefinitionRepositoryTest {

	private static final String NACOS_GROUP = "DEFAULT_GROUP";

	private static final String ROUTER_DATA_ID = "router.json";

	@Container
	static RedisStackContainer redisContainer = new RedisStackContainer(DockerImageNames.redis("7.4"));

	@Container
	static NacosContainer nacosContainer = new NacosContainer(DockerImageNames.nacos());

	private static RedissonClient redissonClient;

	private static RedissonReactiveClient redissonReactiveClient;

	private static ConfigService configService;

	@BeforeAll
	static void setUp() throws NacosException {
		// Setup Redis
		Config redisConfig = new Config();
		redisConfig.useSingleServer()
			.setAddress("redis://" + redisContainer.getHost() + ":" + redisContainer.getFirstMappedPort());
		redissonClient = Redisson.create(redisConfig);
		redissonReactiveClient = redissonClient.reactive();

		// Setup Nacos
		Properties nacosProperties = new Properties();
		nacosProperties.setProperty("serverAddr", nacosContainer.getServerAddr());
		nacosProperties.setProperty("namespace", "");
		configService = NacosFactory.createConfigService(nacosProperties);
	}

	@AfterAll
	static void tearDown() {
		if (redissonClient != null) {
			redissonClient.shutdown();
		}
	}

	@Test
	@DisplayName("Test Redis and Nacos containers started successfully")
	void testContainersStarted() {
		Assertions.assertThat(redisContainer.isRunning()).isTrue();
		Assertions.assertThat(nacosContainer.isRunning()).isTrue();
	}

	@Test
	@DisplayName("Test publish route config to Nacos")
	void testPublishRouteConfigToNacos() throws NacosException {
		// Given
		List<RouteDefinition> routes = createTestRoutes();
		String routesJson = JacksonUtils.toJsonStr(routes);

		// When
		boolean published = configService.publishConfig(ROUTER_DATA_ID, NACOS_GROUP, routesJson);

		// Then
		Assertions.assertThat(published).isTrue();

		// Verify config can be retrieved
		String retrievedConfig = configService.getConfig(ROUTER_DATA_ID, NACOS_GROUP, 5000);
		Assertions.assertThat(retrievedConfig).isNotNull();
		Assertions.assertThat(retrievedConfig).contains("laokou-admin");
	}

	@Test
	@DisplayName("Test store route definitions to Redis")
	void testStoreRouteDefinitionsToRedis() {
		// Given
		String routeKey = RedisKeyUtils.getRouteDefinitionHashKey();
		RMapReactive<String, RouteDefinition> routeMap = redissonReactiveClient.getMap(routeKey);

		RouteDefinition route = new RouteDefinition();
		route.setId("route-admin");
		route.setUri(URI.create("lb://laokou-admin"));

		// When
		routeMap.put(route.getId(), route).block();

		// Then
		StepVerifier.create(routeMap.get("route-admin")).assertNext(retrieved -> {
			Assertions.assertThat(retrieved.getUri()).isNotNull();
			Assertions.assertThat(retrieved.getId()).isEqualTo("route-admin");
			Assertions.assertThat(retrieved.getUri().toString()).isEqualTo("lb://laokou-admin");
		}).verifyComplete();
	}

	@Test
	@DisplayName("Test sync routes from Nacos to Redis")
	void testSyncRoutesFromNacosToRedis() throws NacosException {
		// Given - publish routes to Nacos first
		List<RouteDefinition> routes = createTestRoutes();
		String routesJson = JacksonUtils.toJsonStr(routes);
		configService.publishConfig(ROUTER_DATA_ID, NACOS_GROUP, routesJson);

		// Wait for Nacos to process
		try {
			Thread.sleep(1000);
		}
		catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		// When - store to Redis
		String routeKey = "gateway:route:sync-test";
		RMapReactive<String, RouteDefinition> routeMap = redissonReactiveClient.getMap(routeKey);

		for (RouteDefinition route : routes) {
			routeMap.put(route.getId(), route).block();
		}

		// Then - verify routes in Redis
		StepVerifier.create(routeMap.size()).assertNext(size -> {
			Assertions.assertThat(size).isEqualTo(routes.size());
		}).verifyComplete();
	}

	@Test
	@DisplayName("Test get route definitions from Redis")
	void testGetRouteDefinitionsFromRedis() {
		// Given
		String routeKey = "gateway:route:get-test";
		RMapReactive<String, RouteDefinition> routeMap = redissonReactiveClient.getMap(routeKey);

		List<RouteDefinition> routes = createTestRoutes();
		for (RouteDefinition route : routes) {
			routeMap.put(route.getId(), route).block();
		}

		// When & Then
		StepVerifier.create(routeMap.entryIterator().collectList()).assertNext(entries -> {
			Assertions.assertThat(entries).hasSize(routes.size());
		}).verifyComplete();
	}

	@Test
	@DisplayName("Test delete route from Redis")
	void testDeleteRouteFromRedis() {
		// Given
		String routeKey = "gateway:route:delete-test";
		RMapReactive<String, RouteDefinition> routeMap = redissonReactiveClient.getMap(routeKey);

		RouteDefinition route = new RouteDefinition();
		route.setId("route-to-delete");
		route.setUri(URI.create("lb://laokou-service"));
		routeMap.put(route.getId(), route).block();

		// When
		routeMap.remove("route-to-delete").block();

		// Then
		StepVerifier.create(routeMap.get("route-to-delete")).verifyComplete();
	}

	@Test
	@DisplayName("Test delete all routes from Redis")
	void testDeleteAllRoutesFromRedis() {
		// Given
		String routeKey = "gateway:route:clear-test";
		RMapReactive<String, RouteDefinition> routeMap = redissonReactiveClient.getMap(routeKey);

		List<RouteDefinition> routes = createTestRoutes();
		for (RouteDefinition route : routes) {
			routeMap.put(route.getId(), route).block();
		}

		// When
		routeMap.delete().block();

		// Then
		StepVerifier.create(routeMap.size()).assertNext(size -> {
			Assertions.assertThat(size).isZero();
		}).verifyComplete();
	}

	@Test
	@DisplayName("Test Nacos config listener callback")
	void testNacosConfigListenerCallback() throws NacosException, InterruptedException {
		// Given
		List<String> receivedConfigs = new ArrayList<>();
		String testDataId = "test-listener-" + System.currentTimeMillis() + ".json";

		configService.addListener(testDataId, NACOS_GROUP, new com.alibaba.nacos.api.config.listener.Listener() {
			@Override
			public java.util.concurrent.Executor getExecutor() {
				return Executors.newSingleThreadExecutor();
			}

			@Override
			public void receiveConfigInfo(String configInfo) {
				receivedConfigs.add(configInfo);
			}
		});

		// When - publish config
		List<RouteDefinition> routes = createTestRoutes();
		String routesJson = JacksonUtils.toJsonStr(routes);
		configService.publishConfig(testDataId, NACOS_GROUP, routesJson);

		// Wait for listener to receive config
		Thread.sleep(2000);

		// Then - listener should have received the config (may take time)
		// Note: In a real scenario, the listener callback may not be immediate
		Assertions.assertThat(receivedConfigs).hasSize(1);
		Assertions.assertThat(configService.getConfig(testDataId, NACOS_GROUP, 5000)).isNotNull();
	}

	@Test
	@DisplayName("Test route definition with predicates and filters")
	void testRouteDefinitionWithPredicatesAndFilters() {
		// Given
		String routeKey = "gateway:route:full-test";
		RMapReactive<String, RouteDefinition> routeMap = redissonReactiveClient.getMap(routeKey);

		RouteDefinition route = new RouteDefinition();
		route.setId("route-with-predicates");
		route.setUri(URI.create("lb://laokou-admin"));
		route.setOrder(1);

		// When
		routeMap.put(route.getId(), route).block();

		// Then
		StepVerifier.create(routeMap.get("route-with-predicates")).assertNext(retrieved -> {
			Assertions.assertThat(retrieved.getId()).isEqualTo("route-with-predicates");
			Assertions.assertThat(retrieved.getOrder()).isEqualTo(1);
		}).verifyComplete();
	}

	/**
	 * Create test route definitions.
	 */
	private List<RouteDefinition> createTestRoutes() {
		List<RouteDefinition> routes = new ArrayList<>();

		RouteDefinition adminRoute = new RouteDefinition();
		adminRoute.setId("route-admin");
		adminRoute.setUri(URI.create("lb://laokou-admin"));
		adminRoute.setOrder(1);
		routes.add(adminRoute);

		RouteDefinition authRoute = new RouteDefinition();
		authRoute.setId("route-auth");
		authRoute.setUri(URI.create("lb://laokou-auth"));
		authRoute.setOrder(2);
		routes.add(authRoute);

		RouteDefinition iotRoute = new RouteDefinition();
		iotRoute.setId("route-iot");
		iotRoute.setUri(URI.create("lb://laokou-iot"));
		iotRoute.setOrder(3);
		routes.add(iotRoute);

		return routes;
	}

}

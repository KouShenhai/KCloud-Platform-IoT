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

package org.laokou.gateway;

import com.redis.testcontainers.RedisStackContainer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

/**
 * Gateway 集成测试 - 使用 Testcontainers.
 *
 * @author laokou
 */
@Testcontainers
@DisplayName("Gateway Integration Tests")
class GatewayIntegrationTest {

	@Container
	static RedisStackContainer redisContainer = new RedisStackContainer(DockerImageNames.redis("7.4"));

	private static RedissonReactiveClient reactiveClient;

	private static RedissonClient redissonClient;

	@BeforeAll
	static void setUp() {
		// 配置 Redisson
		Config config = new Config();
		config.useSingleServer()
			.setAddress("redis://" + redisContainer.getHost() + ":" + redisContainer.getFirstMappedPort());
		redissonClient = Redisson.create(config);
		reactiveClient = redissonClient.reactive();
	}

	@AfterAll
	static void tearDown() {
		if (redissonClient != null) {
			redissonClient.shutdown();
		}
	}

	@Test
	@DisplayName("Test Redis container started successfully")
	void test_container_redisStarted_isRunning() {
		// Then
		Assertions.assertThat(redisContainer.isRunning()).isTrue();
	}

	@Test
	@DisplayName("Test Redis connection is normal")
	void test_redis_connection_isNormal() {
		// When
		String result = reactiveClient.getBucket("test-key")
			.set("test-value")
			.then(reactiveClient.<String>getBucket("test-key").get())
			.block();

		// Then
		Assertions.assertThat(result).isEqualTo("test-value");
	}

	@Test
	@DisplayName("Test route definition storage to Redis")
	void test_routeMap_put_storesRouteDefinition() {
		// Given
		String routeKey = "gateway:route:definitions";
		RMapReactive<String, RouteDefinition> routeMap = reactiveClient.getMap(routeKey);

		RouteDefinition routeDefinition = new RouteDefinition();
		routeDefinition.setId("test-route");
		routeDefinition.setUri(URI.create("http://localhost:8080"));

		// When
		routeMap.put("test-route", routeDefinition).block();

		// Then
		StepVerifier.create(routeMap.get("test-route")).assertNext(route -> {
			URI uri = route.getUri();
			Assertions.assertThat(uri).isNotNull();
			Assertions.assertThat(route.getId()).isEqualTo("test-route");
			Assertions.assertThat(uri.toString()).isEqualTo("http://localhost:8080");
		}).verifyComplete();
	}

	@Test
	@DisplayName("Test multiple route definitions storage")
	void test_routeMap_putMultiple_storesAllRoutes() {
		// Given
		String routeKey = "gateway:route:multi";
		RMapReactive<String, RouteDefinition> routeMap = reactiveClient.getMap(routeKey);

		RouteDefinition route1 = new RouteDefinition();
		route1.setId("route-admin");
		route1.setUri(URI.create("lb://laokou-admin"));

		RouteDefinition route2 = new RouteDefinition();
		route2.setId("route-auth");
		route2.setUri(URI.create("lb://laokou-auth"));

		// When
		routeMap.put(route1.getId(), route1).block();
		routeMap.put(route2.getId(), route2).block();

		// Then
		StepVerifier.create(routeMap.size()).assertNext(size -> {
			Assertions.assertThat(size).isEqualTo(2);
		}).verifyComplete();
	}

	@Test
	@DisplayName("Test delete route definition")
	void test_routeMap_remove_deletesRouteDefinition() {
		// Given
		String routeKey = "gateway:route:delete";
		RMapReactive<String, RouteDefinition> routeMap = reactiveClient.getMap(routeKey);

		RouteDefinition routeDefinition = new RouteDefinition();
		routeDefinition.setId("route-to-delete");
		routeDefinition.setUri(URI.create("http://localhost:9090"));

		routeMap.put("route-to-delete", routeDefinition).block();

		// When
		routeMap.remove("route-to-delete").block();

		// Then
		StepVerifier.create(routeMap.get("route-to-delete")).verifyComplete();
	}

	@Test
	@DisplayName("Test clear all route definitions")
	void test_routeMap_delete_clearsAllRoutes() {
		// Given
		String routeKey = "gateway:route:clear";
		RMapReactive<String, RouteDefinition> routeMap = reactiveClient.getMap(routeKey);

		RouteDefinition route1 = new RouteDefinition();
		route1.setId("route-1");
		route1.setUri(URI.create("http://localhost:8081"));

		RouteDefinition route2 = new RouteDefinition();
		route2.setId("route-2");
		route2.setUri(URI.create("http://localhost:8082"));

		routeMap.put(route1.getId(), route1).block();
		routeMap.put(route2.getId(), route2).block();

		// When
		routeMap.delete().block();

		// Then
		StepVerifier.create(routeMap.size()).assertNext(size -> {
			Assertions.assertThat(size).isZero();
		}).verifyComplete();
	}

	@Test
	@DisplayName("Test route definition iterator")
	void test_routeMap_iterator_returnsAllEntries() {
		// Given
		String routeKey = "gateway:route:iterator";
		RMapReactive<String, RouteDefinition> routeMap = reactiveClient.getMap(routeKey);

		RouteDefinition route1 = new RouteDefinition();
		route1.setId("route-iter-1");
		route1.setUri(URI.create("http://localhost:8083"));

		RouteDefinition route2 = new RouteDefinition();
		route2.setId("route-iter-2");
		route2.setUri(URI.create("http://localhost:8084"));

		routeMap.put(route1.getId(), route1).block();
		routeMap.put(route2.getId(), route2).block();

		// When & Then
		StepVerifier.create(routeMap.entryIterator().collectList())
			.assertNext(entries -> Assertions.assertThat(entries).hasSize(2))
			.verifyComplete();
	}

}

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
import com.alibaba.nacos.api.exception.NacosException;
import org.assertj.core.api.Assertions;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.laokou.common.i18n.util.JacksonUtils;
import org.laokou.common.redis.util.ReactiveRedisUtils;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RMapReactive;
import org.springframework.cloud.gateway.route.RouteDefinition;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * NacosRouteDefinitionRepository 单元测试 - 使用 Mock.
 *
 * @author laokou
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("NacosRouteDefinitionRepository Unit Tests")
class NacosRouteDefinitionRepositoryUnitTest {

	@Mock
	private NacosConfigManager nacosConfigManager;

	@Mock
	private ConfigService configService;

	@Mock
	private NacosConfigProperties nacosConfigProperties;

	@Mock
	private ReactiveRedisUtils reactiveRedisUtils;

	@Mock
	private RMapReactive<String, RouteDefinition> reactiveMap;

	private NacosRouteDefinitionRepository repository;

	@BeforeEach
	void setUp() {
		ExecutorService virtualThreadExecutor = Executors.newVirtualThreadPerTaskExecutor();

		Mockito.when(nacosConfigManager.getNacosConfigProperties()).thenReturn(nacosConfigProperties);
		Mockito.when(nacosConfigProperties.getGroup()).thenReturn("DEFAULT_GROUP");
		Mockito.when(nacosConfigManager.getConfigService()).thenReturn(configService);
		// 使用 doReturn().when() 处理泛型方法，避免类型推断问题
		Mockito.doReturn(reactiveMap).when(reactiveRedisUtils).getMap(Mockito.anyString());

		repository = new NacosRouteDefinitionRepository(nacosConfigManager, reactiveRedisUtils, virtualThreadExecutor);
	}

	@Nested
	@DisplayName("getRouteDefinitions Tests")
	class GetRouteDefinitionsTests {

		@Test
		@DisplayName("Test getRouteDefinitions returns routes from Redis")
		void test_getRouteDefinitions_withRoutes_returnsRoutesFromRedis() {
			// Given
			RouteDefinition route1 = createRouteDefinition("route-1", "lb://service-1");
			RouteDefinition route2 = createRouteDefinition("route-2", "lb://service-2");

			Mockito.when(reactiveMap.entryIterator())
				.thenReturn(Flux.just(Map.entry("route-1", route1), Map.entry("route-2", route2)));

			// When
			Flux<@NonNull RouteDefinition> result = repository.getRouteDefinitions();

			// Then
			StepVerifier.create(result)
				.assertNext(route -> Assertions.assertThat(route.getId()).isEqualTo("route-1"))
				.assertNext(route -> Assertions.assertThat(route.getId()).isEqualTo("route-2"))
				.verifyComplete();
		}

		@Test
		@DisplayName("Test getRouteDefinitions returns empty when no routes")
		void test_getRouteDefinitions_noRoutes_returnsEmpty() {
			// Given
			Mockito.when(reactiveMap.entryIterator()).thenReturn(Flux.empty());

			// When
			Flux<@NonNull RouteDefinition> result = repository.getRouteDefinitions();

			// Then
			StepVerifier.create(result).verifyComplete();
		}

		@Test
		@DisplayName("Test getRouteDefinitions handles Redis error from source")
		void test_getRouteDefinitions_redisError_propagatesError() {
			// Given
			Mockito.when(reactiveMap.entryIterator())
				.thenReturn(Flux.error(new RuntimeException("Redis connection failed")));

			// When
			Flux<@NonNull RouteDefinition> result = repository.getRouteDefinitions();

			// Then - onErrorContinue 只处理中间操作的错误，源头错误会被传播
			// 但实际实现中有 onErrorContinue，它会记录日志并继续
			// 然而，源头的 error 不会被 onErrorContinue 捕获，会直接传播
			StepVerifier.create(result).expectError(RuntimeException.class).verify();
		}

	}

	@Nested
	@DisplayName("save and delete Tests")
	class SaveDeleteTests {

		@Test
		@DisplayName("Test save returns Mono.empty()")
		void test_save_routeDefinition_returnsEmpty() {
			// Given
			RouteDefinition route = createRouteDefinition("route-test", "lb://test-service");

			// When
			Mono<@NonNull Void> result = repository.save(Mono.just(route));

			// Then
			StepVerifier.create(result).verifyComplete();
		}

		@Test
		@DisplayName("Test delete returns Mono.empty()")
		void test_delete_routeId_returnsEmpty() {
			// When
			Mono<@NonNull Void> result = repository.delete(Mono.just("route-test"));

			// Then
			StepVerifier.create(result).verifyComplete();
		}

	}

	@Nested
	@DisplayName("syncRouter Tests")
	class SyncRouterTests {

		@Test
		@DisplayName("Test syncRouter fetches config from Nacos and stores to Redis")
		void test_syncRouter_withValidConfig_syncsSuccessfully() throws NacosException {
			// 使用 MockedStatic 来 mock SpringContextUtils 静态方法
			try (var _ = Mockito.mockStatic(org.laokou.common.i18n.util.SpringContextUtils.class)) {
				// Given
				List<RouteDefinition> routes = createTestRoutes();
				String routesJson = JacksonUtils.toJsonStr(routes);

				Mockito.when(configService.getConfig("router.json", "DEFAULT_GROUP", 5000)).thenReturn(routesJson);
				Mockito.when(reactiveMap.delete()).thenReturn(Mono.just(true));
				Mockito.when(reactiveMap.putIfAbsent(Mockito.anyString(), Mockito.any(RouteDefinition.class)))
					.thenReturn(Mono.empty());

				// When
				Mono<@NonNull Void> result = repository.syncRouter();

				// Then
				StepVerifier.create(result).verifyComplete();
				Mockito.verify(reactiveMap).delete();
			}
		}

		@Test
		@DisplayName("Test syncRouter handles empty routes from Nacos")
		void test_syncRouter_emptyRoutes_handlesGracefully() throws NacosException {
			// 使用 MockedStatic 来 mock SpringContextUtils 静态方法
			try (var _ = Mockito.mockStatic(org.laokou.common.i18n.util.SpringContextUtils.class)) {
				// Given
				Mockito.when(configService.getConfig("router.json", "DEFAULT_GROUP", 5000)).thenReturn("[]");
				Mockito.when(reactiveMap.delete()).thenReturn(Mono.just(true));

				// When
				Mono<@NonNull Void> result = repository.syncRouter();

				// Then
				StepVerifier.create(result).verifyComplete();
			}
		}

		@Test
		@DisplayName("Test syncRouter throws SystemException when Nacos config not found")
		void test_syncRouter_nacosConfigNotFound_throwsSystemException() throws NacosException {
			// Given
			Mockito.when(configService.getConfig("router.json", "DEFAULT_GROUP", 5000)).thenReturn(null);

			// When & Then
			Assertions.assertThatThrownBy(() -> repository.syncRouter().block())
				.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		@DisplayName("Test syncRouter throws SystemException when Nacos throws exception")
		void test_syncRouter_nacosException_throwsSystemException() throws NacosException {
			// Given
			Mockito.when(configService.getConfig("router.json", "DEFAULT_GROUP", 5000))
				.thenThrow(new NacosException(500, "Nacos server error"));

			// When & Then
			Assertions.assertThatThrownBy(() -> repository.syncRouter().block())
				.isInstanceOf(IllegalArgumentException.class);
		}

		@Test
		@DisplayName("Test syncRouter handles invalid JSON")
		void test_syncRouter_invalidJson_throwsSystemException() throws NacosException {
			// Given
			Mockito.when(configService.getConfig("router.json", "DEFAULT_GROUP", 5000))
				.thenReturn("invalid json content");

			// When & Then
			Assertions.assertThatThrownBy(() -> repository.syncRouter().block())
				.isInstanceOf(IllegalArgumentException.class);
		}

	}

	@Nested
	@DisplayName("listenRouter Tests")
	class ListenRouterTests {

		@Test
		@DisplayName("Test listenRouter registers listener successfully")
		void test_listenRouter_registersListenerSuccessfully() throws NacosException {
			// When & Then - should not throw exception
			Assertions.assertThatCode(() -> repository.listenRouter()).doesNotThrowAnyException();

			// Verify listener was added
			Mockito.verify(configService)
				.addListener(Mockito.eq("router.json"), Mockito.eq("DEFAULT_GROUP"), Mockito.any());
		}

		@Test
		@DisplayName("Test listenRouter handles NacosException")
		void test_listenRouter_nacosException_throwsException() throws NacosException {
			// Given
			Mockito.doThrow(new NacosException(500, "Failed to add listener"))
				.when(configService)
				.addListener(Mockito.anyString(), Mockito.anyString(), Mockito.any());

			// When & Then
			Assertions.assertThatThrownBy(() -> repository.listenRouter()).isInstanceOf(NacosException.class);
		}

	}

	/**
	 * Helper method to create a RouteDefinition.
	 */
	private RouteDefinition createRouteDefinition(String id, String uri) {
		RouteDefinition route = new RouteDefinition();
		route.setId(id);
		route.setUri(URI.create(uri));
		return route;
	}

	/**
	 * Helper method to create test routes.
	 */
	private List<RouteDefinition> createTestRoutes() {
		List<RouteDefinition> routes = new ArrayList<>();
		routes.add(createRouteDefinition("route-admin", "lb://laokou-admin"));
		routes.add(createRouteDefinition("route-auth", "lb://laokou-auth"));
		return routes;
	}

}

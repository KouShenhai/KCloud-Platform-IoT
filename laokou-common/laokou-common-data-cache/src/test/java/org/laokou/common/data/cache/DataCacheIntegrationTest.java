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

package org.laokou.common.data.cache;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.laokou.common.data.cache.config.SpringCacheProperties;
import org.laokou.common.redis.util.RedisUtils;
import org.mockito.Mockito;
import org.redisson.api.RLocalCachedMap;
import org.redisson.api.RMapCacheNative;
import org.redisson.api.options.LocalCachedMapOptions;
import org.redisson.api.options.MapOptions;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.lang.reflect.Constructor;
import java.time.Duration;
import java.util.Collection;
import java.util.Map;

/**
 * Data cache integration test class.
 *
 * @author laokou
 */
@DisplayName("DataCache Integration Tests")
class DataCacheIntegrationTest {

	@Nested
	@DisplayName("DistributedCacheManager Tests")
	class DistributedCacheManagerTests {

		@Test
		@DisplayName("Test getCacheNames with configured caches returns names")
		void test_getCacheNames_configuredCaches_returnsNames() throws Exception {
			// Given
			RedisUtils redisUtils = Mockito.mock(RedisUtils.class);
			SpringCacheProperties properties = new SpringCacheProperties();

			SpringCacheProperties.DistributedCacheConfig config1 = new SpringCacheProperties.DistributedCacheConfig();
			config1.setTtl(Duration.ofMinutes(10));
			SpringCacheProperties.DistributedCacheConfig config2 = new SpringCacheProperties.DistributedCacheConfig();
			config2.setTtl(Duration.ofMinutes(20));

			properties.setDistributedConfigs(Map.of("cache1", config1, "cache2", config2));

			// Create manager using reflection since it's package-private
			Class<?> managerClass = Class.forName("org.laokou.common.data.cache.config.DistributedCacheManager");
			Constructor<?> constructor = managerClass.getDeclaredConstructor(RedisUtils.class,
					SpringCacheProperties.class);
			constructor.setAccessible(true);
			CacheManager manager = (CacheManager) constructor.newInstance(redisUtils, properties);

			// When
			Collection<String> cacheNames = manager.getCacheNames();

			// Then
			Assertions.assertThat(cacheNames).containsExactlyInAnyOrder("cache1", "cache2");
		}

		@Test
		@DisplayName("Test getCache with existing cache returns cache")
		@SuppressWarnings("unchecked")
		void test_getCache_existingCache_returnsCache() throws Exception {
			// Given
			RedisUtils redisUtils = Mockito.mock(RedisUtils.class);
			RMapCacheNative<Object, Object> mapCacheNative = Mockito.mock(RMapCacheNative.class);
			Mockito.when(redisUtils.getMapCacheNative(Mockito.any(MapOptions.class))).thenReturn(mapCacheNative);

			SpringCacheProperties properties = new SpringCacheProperties();
			SpringCacheProperties.DistributedCacheConfig config = new SpringCacheProperties.DistributedCacheConfig();
			properties.setDistributedConfigs(Map.of("testCache", config));

			Class<?> managerClass = Class.forName("org.laokou.common.data.cache.config.DistributedCacheManager");
			Constructor<?> constructor = managerClass.getDeclaredConstructor(RedisUtils.class,
					SpringCacheProperties.class);
			constructor.setAccessible(true);
			CacheManager manager = (CacheManager) constructor.newInstance(redisUtils, properties);

			// When
			Cache cache = manager.getCache("testCache");

			// Then
			Assertions.assertThat(cache).isNotNull();
		}

		@Test
		@DisplayName("Test getCache with dynamic cache creates new cache")
		@SuppressWarnings("unchecked")
		void test_getCache_dynamicCache_createsNewCache() throws Exception {
			// Given
			RedisUtils redisUtils = Mockito.mock(RedisUtils.class);
			RMapCacheNative<Object, Object> mapCacheNative = Mockito.mock(RMapCacheNative.class);
			Mockito.when(redisUtils.getMapCacheNative(Mockito.any(MapOptions.class))).thenReturn(mapCacheNative);

			SpringCacheProperties properties = new SpringCacheProperties();

			Class<?> managerClass = Class.forName("org.laokou.common.data.cache.config.DistributedCacheManager");
			Constructor<?> constructor = managerClass.getDeclaredConstructor(RedisUtils.class,
					SpringCacheProperties.class);
			constructor.setAccessible(true);
			CacheManager manager = (CacheManager) constructor.newInstance(redisUtils, properties);

			// When
			Cache cache = manager.getCache("dynamicCache");

			// Then
			Assertions.assertThat(cache).isNotNull();
		}

	}

	@Nested
	@DisplayName("LocalCacheManager Tests")
	class LocalCacheManagerTests {

		@Test
		@DisplayName("Test getCacheNames with configured caches returns names")
		void test_getCacheNames_configuredCaches_returnsNames() throws Exception {
			// Given
			RedisUtils redisUtils = Mockito.mock(RedisUtils.class);
			SpringCacheProperties properties = new SpringCacheProperties();

			SpringCacheProperties.LocalCacheConfig config1 = new SpringCacheProperties.LocalCacheConfig();
			SpringCacheProperties.LocalCacheConfig config2 = new SpringCacheProperties.LocalCacheConfig();

			properties.setLocalConfigs(Map.of("localCache1", config1, "localCache2", config2));

			Class<?> managerClass = Class.forName("org.laokou.common.data.cache.config.LocalCacheManager");
			Constructor<?> constructor = managerClass.getDeclaredConstructor(RedisUtils.class,
					SpringCacheProperties.class);
			constructor.setAccessible(true);
			CacheManager manager = (CacheManager) constructor.newInstance(redisUtils, properties);

			// When
			Collection<String> cacheNames = manager.getCacheNames();

			// Then
			Assertions.assertThat(cacheNames).containsExactlyInAnyOrder("localCache1", "localCache2");
		}

		@Test
		@DisplayName("Test getCache returns cache instance")
		@SuppressWarnings("unchecked")
		void test_getCache_validName_returnsCache() throws Exception {
			// Given
			RedisUtils redisUtils = Mockito.mock(RedisUtils.class);
			RLocalCachedMap<Object, Object> localCachedMap = Mockito.mock(RLocalCachedMap.class);
			Mockito.when(redisUtils.getLocalCachedMap(Mockito.any(LocalCachedMapOptions.class)))
				.thenReturn(localCachedMap);

			SpringCacheProperties properties = new SpringCacheProperties();
			SpringCacheProperties.LocalCacheConfig config = new SpringCacheProperties.LocalCacheConfig();
			properties.setLocalConfigs(Map.of("testLocalCache", config));

			Class<?> managerClass = Class.forName("org.laokou.common.data.cache.config.LocalCacheManager");
			Constructor<?> constructor = managerClass.getDeclaredConstructor(RedisUtils.class,
					SpringCacheProperties.class);
			constructor.setAccessible(true);
			CacheManager manager = (CacheManager) constructor.newInstance(redisUtils, properties);

			// When
			Cache cache = manager.getCache("testLocalCache");

			// Then
			Assertions.assertThat(cache).isNotNull();
		}

		@Test
		@DisplayName("Test getCache with non-configured name creates cache with defaults")
		@SuppressWarnings("unchecked")
		void test_getCache_nonConfiguredName_createsCacheWithDefaults() throws Exception {
			// Given
			RedisUtils redisUtils = Mockito.mock(RedisUtils.class);
			RLocalCachedMap<Object, Object> localCachedMap = Mockito.mock(RLocalCachedMap.class);
			Mockito.when(redisUtils.getLocalCachedMap(Mockito.any(LocalCachedMapOptions.class)))
				.thenReturn(localCachedMap);

			SpringCacheProperties properties = new SpringCacheProperties();

			Class<?> managerClass = Class.forName("org.laokou.common.data.cache.config.LocalCacheManager");
			Constructor<?> constructor = managerClass.getDeclaredConstructor(RedisUtils.class,
					SpringCacheProperties.class);
			constructor.setAccessible(true);
			CacheManager manager = (CacheManager) constructor.newInstance(redisUtils, properties);

			// When
			Cache cache = manager.getCache("newCache");

			// Then
			Assertions.assertThat(cache).isNotNull();
		}

	}

}

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

package org.laokou.common.data.cache.config;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.redisson.api.options.LocalCachedMapOptions;

import java.time.Duration;

/**
 * SpringCacheProperties test class.
 *
 * @author laokou
 */
@DisplayName("SpringCacheProperties Unit Tests")
class SpringCachePropertiesTest {

	@Nested
	@DisplayName("DistributedCacheConfig Tests")
	class DistributedCacheConfigTests {

		@Test
		@DisplayName("Test default TTL is 5 minutes")
		void test_distributedCacheConfig_defaultTtl_isFiveMinutes() {
			// Given
			SpringCacheProperties.DistributedCacheConfig config = new SpringCacheProperties.DistributedCacheConfig();

			// Then
			Assertions.assertThat(config.getTtl()).isEqualTo(Duration.ofMinutes(5));
		}

		@Test
		@DisplayName("Test custom TTL is applied")
		void test_distributedCacheConfig_customTtl_isApplied() {
			// Given
			SpringCacheProperties.DistributedCacheConfig config = new SpringCacheProperties.DistributedCacheConfig();
			Duration customTtl = Duration.ofHours(1);

			// When
			config.setTtl(customTtl);

			// Then
			Assertions.assertThat(config.getTtl()).isEqualTo(customTtl);
		}

	}

	@Nested
	@DisplayName("LocalCacheConfig Tests")
	class LocalCacheConfigTests {

		@Test
		@DisplayName("Test default reconnection strategy is CLEAR")
		void test_localCacheConfig_defaultReconnectionStrategy_isClear() {
			// Given
			SpringCacheProperties.LocalCacheConfig config = new SpringCacheProperties.LocalCacheConfig();

			// Then
			Assertions.assertThat(config.getReconnectionStrategy())
				.isEqualTo(LocalCachedMapOptions.ReconnectionStrategy.CLEAR);
		}

		@Test
		@DisplayName("Test default sync strategy is INVALIDATE")
		void test_localCacheConfig_defaultSyncStrategy_isInvalidate() {
			// Given
			SpringCacheProperties.LocalCacheConfig config = new SpringCacheProperties.LocalCacheConfig();

			// Then
			Assertions.assertThat(config.getSyncStrategy()).isEqualTo(LocalCachedMapOptions.SyncStrategy.INVALIDATE);
		}

		@Test
		@DisplayName("Test default eviction policy is LRU")
		void test_localCacheConfig_defaultEvictionPolicy_isLru() {
			// Given
			SpringCacheProperties.LocalCacheConfig config = new SpringCacheProperties.LocalCacheConfig();

			// Then
			Assertions.assertThat(config.getEvictionPolicy()).isEqualTo(LocalCachedMapOptions.EvictionPolicy.LRU);
		}

		@Test
		@DisplayName("Test default max size is 1024")
		void test_localCacheConfig_defaultMaxSize_is1024() {
			// Given
			SpringCacheProperties.LocalCacheConfig config = new SpringCacheProperties.LocalCacheConfig();

			// Then
			Assertions.assertThat(config.getMaxSize()).isEqualTo(1024);
		}

		@Test
		@DisplayName("Test default TTL is 5 minutes")
		void test_localCacheConfig_defaultTtl_isFiveMinutes() {
			// Given
			SpringCacheProperties.LocalCacheConfig config = new SpringCacheProperties.LocalCacheConfig();

			// Then
			Assertions.assertThat(config.getTtl()).isEqualTo(Duration.ofMinutes(5));
		}

		@Test
		@DisplayName("Test default max idle time is 1 minute")
		void test_localCacheConfig_defaultMaxIdleTime_isOneMinute() {
			// Given
			SpringCacheProperties.LocalCacheConfig config = new SpringCacheProperties.LocalCacheConfig();

			// Then
			Assertions.assertThat(config.getMaxIdleTime()).isEqualTo(Duration.ofMinutes(1));
		}

		@Test
		@DisplayName("Test default cache provider is CAFFEINE")
		void test_localCacheConfig_defaultCacheProvider_isCaffeine() {
			// Given
			SpringCacheProperties.LocalCacheConfig config = new SpringCacheProperties.LocalCacheConfig();

			// Then
			Assertions.assertThat(config.getCacheProvider()).isEqualTo(LocalCachedMapOptions.CacheProvider.CAFFEINE);
		}

		@Test
		@DisplayName("Test default store mode is LOCALCACHE_REDIS")
		void test_localCacheConfig_defaultStoreMode_isLocalCacheRedis() {
			// Given
			SpringCacheProperties.LocalCacheConfig config = new SpringCacheProperties.LocalCacheConfig();

			// Then
			Assertions.assertThat(config.getStoreMode()).isEqualTo(LocalCachedMapOptions.StoreMode.LOCALCACHE_REDIS);
		}

		@Test
		@DisplayName("Test default store cache miss is true")
		void test_localCacheConfig_defaultStoreCacheMiss_isTrue() {
			// Given
			SpringCacheProperties.LocalCacheConfig config = new SpringCacheProperties.LocalCacheConfig();

			// Then
			Assertions.assertThat(config.isStoreCacheMiss()).isTrue();
		}

		@Test
		@DisplayName("Test default expiration event policy is SUBSCRIBE_WITH_KEYEVENT_PATTERN")
		void test_localCacheConfig_defaultExpirationEventPolicy_isSubscribeWithKeyeventPattern() {
			// Given
			SpringCacheProperties.LocalCacheConfig config = new SpringCacheProperties.LocalCacheConfig();

			// Then
			Assertions.assertThat(config.getExpirationEventPolicy())
				.isEqualTo(LocalCachedMapOptions.ExpirationEventPolicy.SUBSCRIBE_WITH_KEYEVENT_PATTERN);
		}

		@Test
		@DisplayName("Test default use topic pattern is false")
		void test_localCacheConfig_defaultUseTopicPattern_isFalse() {
			// Given
			SpringCacheProperties.LocalCacheConfig config = new SpringCacheProperties.LocalCacheConfig();

			// Then
			Assertions.assertThat(config.isUseTopicPattern()).isFalse();
		}

		@Test
		@DisplayName("Test custom values are applied")
		void test_localCacheConfig_customValues_areApplied() {
			// Given
			SpringCacheProperties.LocalCacheConfig config = new SpringCacheProperties.LocalCacheConfig();

			// When
			config.setMaxSize(2048);
			config.setTtl(Duration.ofHours(2));
			config.setMaxIdleTime(Duration.ofMinutes(30));
			config.setEvictionPolicy(LocalCachedMapOptions.EvictionPolicy.LFU);
			config.setSyncStrategy(LocalCachedMapOptions.SyncStrategy.UPDATE);
			config.setStoreCacheMiss(false);
			config.setUseTopicPattern(true);

			// Then
			Assertions.assertThat(config.getMaxSize()).isEqualTo(2048);
			Assertions.assertThat(config.getTtl()).isEqualTo(Duration.ofHours(2));
			Assertions.assertThat(config.getMaxIdleTime()).isEqualTo(Duration.ofMinutes(30));
			Assertions.assertThat(config.getEvictionPolicy()).isEqualTo(LocalCachedMapOptions.EvictionPolicy.LFU);
			Assertions.assertThat(config.getSyncStrategy()).isEqualTo(LocalCachedMapOptions.SyncStrategy.UPDATE);
			Assertions.assertThat(config.isStoreCacheMiss()).isFalse();
			Assertions.assertThat(config.isUseTopicPattern()).isTrue();
		}

	}

	@Nested
	@DisplayName("SpringCacheProperties Main Tests")
	class MainTests {

		@Test
		@DisplayName("Test default distributed configs is empty map")
		void test_springCacheProperties_defaultDistributedConfigs_isEmptyMap() {
			// Given
			SpringCacheProperties properties = new SpringCacheProperties();

			// Then
			Assertions.assertThat(properties.getDistributedConfigs()).isNotNull().isEmpty();
		}

		@Test
		@DisplayName("Test default local configs is empty map")
		void test_springCacheProperties_defaultLocalConfigs_isEmptyMap() {
			// Given
			SpringCacheProperties properties = new SpringCacheProperties();

			// Then
			Assertions.assertThat(properties.getLocalConfigs()).isNotNull().isEmpty();
		}

	}

}

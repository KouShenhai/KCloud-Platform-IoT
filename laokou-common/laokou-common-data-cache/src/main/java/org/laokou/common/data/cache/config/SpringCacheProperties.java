/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

import lombok.Data;
import org.redisson.api.options.LocalCachedMapOptions;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author laokou
 */
@Data
@Component
@ConfigurationProperties("spring.cache")
public class SpringCacheProperties {

	private Map<String, DistributedCacheConfig> distributedConfigs = new HashMap<>(0);

	private Map<String, LocalCacheConfig> localConfigs = new HashMap<>(0);

	@Data
	public static class DistributedCacheConfig {

		private Duration ttl = Duration.ofMinutes(5);

	}

	@Data
	public static class LocalCacheConfig {

		private LocalCachedMapOptions.ReconnectionStrategy reconnectionStrategy = LocalCachedMapOptions.ReconnectionStrategy.CLEAR;

		private LocalCachedMapOptions.SyncStrategy syncStrategy = LocalCachedMapOptions.SyncStrategy.INVALIDATE;

		private LocalCachedMapOptions.EvictionPolicy evictionPolicy = LocalCachedMapOptions.EvictionPolicy.LRU;

		private int maxSize = 1024;

		private Duration ttl = Duration.ofMinutes(5);

		private Duration maxIdleTime = Duration.ofMinutes(1);

		private LocalCachedMapOptions.CacheProvider cacheProvider = LocalCachedMapOptions.CacheProvider.CAFFEINE;

		private LocalCachedMapOptions.StoreMode storeMode = LocalCachedMapOptions.StoreMode.LOCALCACHE_REDIS;

		private boolean storeCacheMiss = true;

		private LocalCachedMapOptions.ExpirationEventPolicy expirationEventPolicy = LocalCachedMapOptions.ExpirationEventPolicy.SUBSCRIBE_WITH_KEYEVENT_PATTERN;

		private boolean useTopicPattern;

	}

}

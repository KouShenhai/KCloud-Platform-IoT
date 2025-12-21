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

import org.jspecify.annotations.NonNull;
import org.laokou.common.redis.util.RedisUtils;
import org.redisson.api.RLocalCachedMap;
import org.redisson.api.options.LocalCachedMapOptions;
import org.redisson.spring.cache.RedissonCache;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author laokou
 */
final class LocalCacheManager implements CacheManager {

	private final RedisUtils redisUtils;

	private final Map<String, LocalCachedMapOptions<Object, Object>> configMap;

	public LocalCacheManager(RedisUtils redisUtils, SpringCacheProperties springCacheProperties) {
		this.redisUtils = redisUtils;
		this.configMap = createDefaultConfigMap(springCacheProperties.getLocalConfigs());
	}

	@Override
	public Cache getCache(@NonNull String name) {
		return new RedissonCache(getMapCache(name), false);
	}

	@NonNull
	@Override
	public Collection<String> getCacheNames() {
		return Collections.unmodifiableSet(configMap.keySet());
	}

	private Map<String, LocalCachedMapOptions<Object, Object>> createDefaultConfigMap(
			Map<String, SpringCacheProperties.LocalCacheConfig> localConfigs) {
		return localConfigs.entrySet()
			.stream()
			.collect(Collectors.toConcurrentMap(Map.Entry::getKey,
					entry -> createLocalCachedOption(entry.getKey(), entry.getValue())));
	}

	private RLocalCachedMap<Object, Object> getMapCache(String name) {
		if (configMap.containsKey(name)) {
			return redisUtils.getLocalCachedMap(configMap.get(name));
		}
		return redisUtils
			.getLocalCachedMap(createLocalCachedOption(name, new SpringCacheProperties.LocalCacheConfig()));
	}

	private LocalCachedMapOptions<Object, Object> createLocalCachedOption(String name,
			SpringCacheProperties.LocalCacheConfig localCacheConfig) {
		return LocalCachedMapOptions.name(name)
			.cacheSize(localCacheConfig.getMaxSize())
			.timeToLive(localCacheConfig.getTtl())
			.maxIdle(localCacheConfig.getMaxIdleTime())
			.storeMode(localCacheConfig.getStoreMode())
			.storeCacheMiss(localCacheConfig.isStoreCacheMiss())
			.expirationEventPolicy(localCacheConfig.getExpirationEventPolicy())
			.useTopicPattern(localCacheConfig.isUseTopicPattern())
			.cacheProvider(localCacheConfig.getCacheProvider())
			.evictionPolicy(localCacheConfig.getEvictionPolicy())
			.reconnectionStrategy(localCacheConfig.getReconnectionStrategy())
			.syncStrategy(localCacheConfig.getSyncStrategy());
	}

}

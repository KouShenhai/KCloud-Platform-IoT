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

/*
 * Copyright (c) 2013-2022 Nikita Koksharov
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.laokou.common.data.cache.config;

import lombok.Data;
import org.jspecify.annotations.NonNull;
import org.laokou.common.redis.util.RedisUtils;
import org.redisson.api.RMapCache;
import org.redisson.api.map.event.MapEntryListener;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonCache;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.transaction.TransactionAwareCacheDecorator;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * 分布式数据缓存扩展管理类. {@link org.springframework.cache.CacheManager}. implementation backed by
 * Redisson instance.
 *
 * @author Nikita Koksharov
 * @author laokou
 * @see RedissonSpringCacheManager
 */
@Data
final class DistributedCacheManager implements CacheManager {

	private final RedisUtils redisUtils;

	private boolean dynamic = true;

	private boolean allowNullValues = false;

	private boolean transactionAware = false;

	private final Map<String, CacheConfig> configMap;

	private final ConcurrentMap<String, Cache> instanceMap;

	public DistributedCacheManager(RedisUtils redisUtils, SpringCacheProperties properties) {
		this.redisUtils = redisUtils;
		Map<String, SpringCacheProperties.DistributedCacheConfig> configs = properties.getDistributedConfigs();
		this.instanceMap = new ConcurrentHashMap<>(configs.size());
		this.configMap = createConfigMap(configs);
	}

	@Override
	public Cache getCache(@NonNull String name) {
		Cache cache = instanceMap.get(name);
		if (cache != null) {
			return cache;
		}
		if (!dynamic) {
			return null;
		}
		return createMapCache(name, createDefaultConfig(name));
	}

	private Cache createMapCache(String name, CacheConfig config) {
		RMapCache<Object, Object> map = getMapCache(name);

		Cache cache = new RedissonCache(map, config, allowNullValues);
		if (transactionAware) {
			cache = new TransactionAwareCacheDecorator(cache);
		}
		Cache oldCache = instanceMap.putIfAbsent(name, cache);
		if (oldCache != null) {
			cache = oldCache;
		}
		else {
			map.setMaxSize(config.getMaxSize(), config.getEvictionMode());
			for (MapEntryListener listener : config.getListeners()) {
				map.addListener(listener);
			}
		}
		return cache;
	}

	private RMapCache<Object, Object> getMapCache(String name) {
		return redisUtils.getMapCache(name);
	}

	@Override
	@NonNull public Collection<String> getCacheNames() {
		return Collections.unmodifiableSet(configMap.keySet());
	}

	private Map<String, CacheConfig> createConfigMap(
			Map<String, SpringCacheProperties.DistributedCacheConfig> configs) {
		return configs.entrySet()
			.stream()
			.collect(Collectors.toConcurrentMap(Map.Entry::getKey, entry -> createConfig(entry.getValue())));
	}

	private CacheConfig createDefaultConfig(String name) {
		if (configMap.containsKey(name)) {
			return configMap.get(name);
		}
		CacheConfig config = createConfig(new SpringCacheProperties.DistributedCacheConfig());
		configMap.put(name, config);
		return config;
	}

	private CacheConfig createConfig(SpringCacheProperties.DistributedCacheConfig cacheProperties) {
		CacheConfig config = new CacheConfig();
		config.setMaxIdleTime(cacheProperties.getMaxIdleTime().toMillis());
		config.setMaxSize(cacheProperties.getMaxSize());
		config.setTTL(cacheProperties.getTtl().toMillis());
		config.setEvictionMode(cacheProperties.getEvictionMode());
		return config;
	}

}

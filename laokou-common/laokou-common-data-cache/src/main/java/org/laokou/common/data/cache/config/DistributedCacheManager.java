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
import org.redisson.MapCacheNativeWrapper;
import org.redisson.api.RMapCache;
import org.redisson.api.options.MapOptions;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonCache;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
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

	private final Map<String, CacheConfig> configMap;

	private final ConcurrentMap<String, Cache> instanceMap;

	public DistributedCacheManager(RedisUtils redisUtils, SpringCacheProperties properties) {
		this.redisUtils = redisUtils;
		this.configMap = properties.getDistributedConfigs()
			.entrySet()
			.stream()
			.collect(Collectors.toMap(Map.Entry::getKey, entry -> createConfig(entry.getValue())));
		this.instanceMap = new ConcurrentHashMap<>(configMap.size());
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
		return instanceMap.computeIfAbsent(name, _ -> new RedissonCache(map, config, false));
	}

	private RMapCache<Object, Object> getMapCache(String name) {
		return new MapCacheNativeWrapper<>(redisUtils.getMapCacheNative(MapOptions.name(name)));
	}

	@Override
	@NonNull public Collection<String> getCacheNames() {
		return Collections.unmodifiableSet(configMap.keySet());
	}

	private CacheConfig createDefaultConfig(String name) {
		if (configMap.containsKey(name)) {
			return configMap.get(name);
		}
		return createConfig(new SpringCacheProperties.DistributedCacheConfig());
	}

	private CacheConfig createConfig(SpringCacheProperties.DistributedCacheConfig distributedCacheConfig) {
		CacheConfig cacheConfig = new CacheConfig();
		cacheConfig.setTTL(distributedCacheConfig.getTtl().toMillis());
		return cacheConfig;
	}

}

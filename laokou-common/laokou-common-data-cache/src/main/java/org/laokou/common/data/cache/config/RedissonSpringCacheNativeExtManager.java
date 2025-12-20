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
import org.redisson.MapCacheNativeWrapper;
import org.redisson.api.RMapCache;
import org.redisson.api.map.event.MapEntryListener;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonCache;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.transaction.TransactionAwareCacheDecorator;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 分布式数据缓存扩展管理类. {@link org.springframework.cache.CacheManager}. implementation backed by
 * Redisson instance.
 *
 * @author Nikita Koksharov
 * @author laokou
 * @see RedissonSpringCacheManager
 */
@Data
public class RedissonSpringCacheNativeExtManager implements CacheManager, InitializingBean {

	private final RedisUtils redisUtils;

	private boolean dynamic = true;

	private boolean allowNullValues = true;

	private boolean transactionAware = false;

	private final Map<String, CacheConfig> configMap;

	private final ConcurrentMap<String, Cache> instanceMap;

	public RedissonSpringCacheNativeExtManager(RedisUtils redisUtils, SpringCacheProperties properties) {
		this.redisUtils = redisUtils;
		Map<String, SpringCacheProperties.CacheConfig> configs = properties.getConfigs();
		int size = configs.size();
		this.instanceMap = new ConcurrentHashMap<>(size);
		this.configMap = new ConcurrentHashMap<>(size);
		createConfigMap(configs);
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

	protected RMapCache<Object, Object> getMapCache(String name) {
		return new MapCacheNativeWrapper<>(redisUtils.getMapCacheNative(name));
	}

	@Override
	@NonNull public Collection<String> getCacheNames() {
		return Collections.unmodifiableSet(configMap.keySet());
	}

	@Override
	public void afterPropertiesSet() {
		validateProps();
	}

	private void createConfigMap(Map<String, SpringCacheProperties.CacheConfig> configs) {
		configs.forEach((key, value) -> configMap.put(key, createConfig(value)));
	}

	private CacheConfig createDefaultConfig(String name) {
		if (configMap.containsKey(name)) {
			return configMap.get(name);
		}
		CacheConfig config = createConfig(new SpringCacheProperties.CacheConfig());
		configMap.put(name, config);
		return config;
	}

	private CacheConfig createConfig(SpringCacheProperties.CacheConfig cacheProperties) {
		CacheConfig config = new CacheConfig();
		config.setMaxIdleTime(cacheProperties.getMaxIdleTime().toMillis());
		config.setMaxSize(cacheProperties.getMaxSize());
		config.setTTL(cacheProperties.getTtl().toMillis());
		config.setEvictionMode(cacheProperties.getEvictionMode());
		return config;
	}

	private void validateProps() {
		for (CacheConfig value : configMap.values()) {
			if (value.getTTL() > 0) {
				throw new UnsupportedOperationException("ttl isn't supported");
			}
			if (value.getMaxIdleTime() > 0) {
				throw new UnsupportedOperationException("maxIdleTime isn't supported");
			}
			if (value.getMaxSize() > 0) {
				throw new UnsupportedOperationException("maxSize isn't supported");
			}
		}
	}

}

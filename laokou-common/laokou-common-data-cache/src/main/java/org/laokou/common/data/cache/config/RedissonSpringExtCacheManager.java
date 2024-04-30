/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

import io.micrometer.common.lang.NonNullApi;
import lombok.Data;
import org.redisson.api.RMap;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonCache;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.transaction.TransactionAwareCacheDecorator;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.laokou.common.i18n.common.StringConstant.UNDER;

/**
 * 数据缓存扩展管理类. A {@link org.springframework.cache.CacheManager} implementation backed by
 * Redisson instance.
 *
 * @author Nikita Koksharov
 * @author laokou
 * @see RedissonSpringCacheManager
 *
 */
@Data
@NonNullApi
@SuppressWarnings("unchecked")
public class RedissonSpringExtCacheManager implements CacheManager, ResourceLoaderAware, InitializingBean {

	private ResourceLoader resourceLoader;

	private boolean dynamic = true;

	private boolean allowNullValues = true;

	private boolean transactionAware = false;

	/**
	 * -- SETTER -- Set Codec instance shared between all Cache instances.
	 *
	 */
	private Codec codec;

	/**
	 * -- SETTER -- Set Redisson instance.
	 *
	 */
	private RedissonClient redisson;

	private Map<String, CacheConfig> configMap = new ConcurrentHashMap<>();

	private ConcurrentMap<String, Cache> instanceMap = new ConcurrentHashMap<>();

	private String configLocation;

	/**
	 * Creates CacheManager supplied by Redisson instance, Codec instance and Cache config
	 * mapped by Cache name.
	 * <p>
	 * Each Cache instance share one Codec instance.
	 * @param redisson object
	 * @param codec object
	 */
	public RedissonSpringExtCacheManager(RedissonClient redisson, Codec codec) {
		this.redisson = redisson;
		this.codec = codec;
	}

	protected CacheConfig createDefaultConfig(String[] values) {
		CacheConfig config = new CacheConfig();
		if (values.length > 1) {
			config.setTTL(DurationStyle.detectAndParse(values[1]).toMillis());
		}
		if (values.length > 2) {
			config.setMaxIdleTime(DurationStyle.detectAndParse(values[2]).toMillis());
		}
		if (values.length > 3) {
			config.setMaxSize(Integer.parseInt(values[3]));
		}
		return config;
	}

	@Override
	public Cache getCache(String name) {

		String[] values = name.split(UNDER);
		name = values[0];

		Cache cache = instanceMap.get(name);
		if (cache != null) {
			return cache;
		}
		if (!dynamic) {
			return null;
		}

		CacheConfig config = configMap.get(name);
		if (Objects.isNull(config)) {
			config = createDefaultConfig(values);
			configMap.put(name, config);
		}

		if (config.getMaxIdleTime() == 0 && config.getTTL() == 0 && config.getMaxSize() == 0) {
			return createMap(name);
		}

		return createMapCache(name, config);
	}

	private Cache createMap(String name) {
		RMap<Object, Object> map = getMap(name);

		Cache cache = new RedissonCache(map, allowNullValues);
		if (transactionAware) {
			cache = new TransactionAwareCacheDecorator(cache);
		}
		Cache oldCache = instanceMap.putIfAbsent(name, cache);
		if (oldCache != null) {
			cache = oldCache;
		}
		return cache;
	}

	protected RMap<Object, Object> getMap(String name) {
		if (codec != null) {
			return redisson.getMap(name, codec);
		}
		return redisson.getMap(name);
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
			map.setMaxSize(config.getMaxSize());
		}
		return cache;
	}

	protected RMapCache<Object, Object> getMapCache(String name) {
		if (codec != null) {
			return redisson.getMapCache(name, codec);
		}
		return redisson.getMapCache(name);
	}

	@Override
	public Collection<String> getCacheNames() {
		return Collections.unmodifiableSet(configMap.keySet());
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	@Override
	public void afterPropertiesSet() {
		if (configLocation == null) {
			return;
		}

		Resource resource = resourceLoader.getResource(configLocation);
		try {
			this.configMap = (Map<String, CacheConfig>) CacheConfig.fromYAML(resource.getInputStream());
		}
		catch (IOException e) {
			// try to read yaml
			try {
				this.configMap = (Map<String, CacheConfig>) CacheConfig.fromJSON(resource.getInputStream());
			}
			catch (IOException e1) {
				e1.addSuppressed(e);
				throw new BeanDefinitionStoreException(
						"Could not parse cache configuration at [" + configLocation + "]", e1);
			}
		}
	}

}

/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

import com.github.benmanes.caffeine.cache.Cache;
import io.micrometer.common.lang.NonNullApi;
import io.micrometer.common.lang.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.redis.utils.RedisUtil;
import org.redisson.spring.cache.CacheConfig;
import org.springframework.cache.support.AbstractValueAdaptingCache;

import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * @author laokou
 */
@Slf4j
@NonNullApi
public class CaffeineRedisCache extends AbstractValueAdaptingCache {

	private final String key;

	private final Cache<String, Object> caffeineCache;

	private final RedisUtil redisUtil;

	private final CacheConfig config;

	protected CaffeineRedisCache(Cache<String, Object> caffeineCache, RedisUtil redisUtil, String key,
			CacheConfig config, boolean allowNullValues) {
		super(allowNullValues);
		this.redisUtil = redisUtil;
		this.caffeineCache = caffeineCache;
		this.config = config;
		this.key = key;
	}

	@Override
	protected Object lookup(Object name) {
		String k = name.toString();
		Object obj = caffeineCache.getIfPresent(k);
		if (Objects.nonNull(obj)) {
			return obj;
		}
		obj = redisUtil.get(k);
		if (Objects.nonNull(obj)) {
			caffeineCache.put(k, obj);
		}
		return obj;
	}

	@Override
	public String getName() {
		return this.key;
	}

	@Override
	public Object getNativeCache() {
		return this;
	}

	@Override
	public <T> T get(Object name, Callable<T> valueLoader) {
		// 分布式锁
		return null;
	}

	@Override
	public void put(Object name, @Nullable Object value) {

	}

	@Override
	public void evict(Object name) {
		String k = name.toString();
		redisUtil.delete(k);
		caffeineCache.invalidate(k);
	}

	@Override
	public void clear() {
		redisUtil.delete(key);
		caffeineCache.invalidateAll();
	}

}

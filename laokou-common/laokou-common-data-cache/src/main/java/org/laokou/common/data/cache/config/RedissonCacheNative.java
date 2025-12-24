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
import org.jspecify.annotations.Nullable;
import org.redisson.api.RMapCache;
import org.redisson.spring.cache.NullValue;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import java.time.Duration;
import java.util.concurrent.Callable;

final class RedissonCacheNative implements Cache {

	private final RMapCache<Object, Object> mapCache;

	private final Duration ttl;

	RedissonCacheNative(RMapCache<Object, Object> mapCache, Duration ttl) {
		this.mapCache = mapCache;
		this.ttl = ttl;
	}

	@NonNull
	@Override
	public String getName() {
		return mapCache.getName();
	}

	@NonNull
	@Override
	public Object getNativeCache() {
		return mapCache;
	}

	@Override
	public @Nullable ValueWrapper get(@NonNull Object key) {
		return toValueWrapper(mapCache.getWithTTLOnly(key));
	}

	@Override
	public <T> @Nullable T get(@NonNull Object key, @Nullable Class<T> type) {
		throw new UnsupportedOperationException("Not supported get(Object key, Class<T> type)");
	}

	@Override
	public <T> @Nullable T get(@NonNull Object key, @NonNull Callable<T> valueLoader) {
		throw new UnsupportedOperationException("Not supported get(Object key, Callable<T> type)");
	}

	@Override
	public void put(@NonNull Object key, @Nullable Object value) {
		throw new UnsupportedOperationException("Not supported put(Object key, Object value)");
	}

	@Override
	public ValueWrapper putIfAbsent(@NonNull Object key, Object value) {
		return toValueWrapper(mapCache.computeIfAbsent(key, ttl, _ -> toValueWrapper(toStoreValue(value))));
	}

	@Override
	public void evict(@NonNull Object key) {
		throw new UnsupportedOperationException("Not supported evict(Object key)");
	}

	@Override
	public boolean evictIfPresent(@NonNull Object key) {
		return mapCache.fastRemove(key) > 0;
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException("Not supported clear()");
	}

	private ValueWrapper toValueWrapper(Object value) {
		if (value == null) {
			return null;
		}
		if (NullValue.class.isAssignableFrom(value.getClass())) {
			return NullValue.INSTANCE;
		}
		return new SimpleValueWrapper(value);
	}

	private Object toStoreValue(Object userValue) {
		if (userValue == null) {
			return NullValue.INSTANCE;
		}
		return userValue;
	}

}

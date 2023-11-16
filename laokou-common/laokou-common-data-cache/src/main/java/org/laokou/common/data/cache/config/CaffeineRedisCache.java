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
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.redis.utils.RedisUtil;
import org.springframework.cache.support.AbstractValueAdaptingCache;

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

	protected CaffeineRedisCache(Cache<String, Object> caffeineCache, RedisUtil redisUtil, String key,
			boolean allowNullValues) {
		super(allowNullValues);
		this.redisUtil = redisUtil;
		this.caffeineCache = caffeineCache;
		this.key = key;
	}

	@Override
	protected Object lookup(Object name) {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public Object getNativeCache() {
		return null;
	}

	@Override
	public <T> T get(Object name, Callable<T> valueLoader) {
		return null;
	}

	@Override
	public void put(Object name, Object value) {

	}

	@Override
	public void evict(Object name) {

	}

	@Override
	public void clear() {

	}

}

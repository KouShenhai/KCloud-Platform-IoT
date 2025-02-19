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

package org.laokou.common.data.cache.aop;

import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.laokou.common.core.utils.SpringExpressionUtil;
import org.laokou.common.data.cache.annotation.DataCache;
import org.laokou.common.data.cache.constant.Type;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 数据缓存切面.
 *
 * @author laokou
 */
@Aspect
@Component
public class CacheAop {

	private static final ReentrantReadWriteLock READ_WRITE_LOCK = new ReentrantReadWriteLock();

	private static final Lock READ_LOCK = READ_WRITE_LOCK.readLock();

	private static final Lock WRITE_LOCK = READ_WRITE_LOCK.writeLock();

	@Autowired
	@Qualifier("redissonCacheManager")
	private CacheManager redissonCacheManager;

	@Autowired
	@Qualifier("caffeineCacheManager")
	private CacheManager caffineCacheManager;

	@Around("@annotation(dataCache)")
	public Object doAround(ProceedingJoinPoint point, DataCache dataCache) {
		MethodSignature signature = (MethodSignature) point.getSignature();
		String[] parameterNames = signature.getParameterNames();
		Type type = dataCache.type();
		String name = dataCache.name();
		String key = SpringExpressionUtil.parse(dataCache.key(), parameterNames, point.getArgs(), String.class);
		return switch (type) {
			case GET -> get(name, key, point);
			case DEL -> del(name, key, point);
		};
	}

	@SneakyThrows
	private Object get(String name, String key, ProceedingJoinPoint point) {
		boolean isLocked = false;
		int retry = 3;
		try {
			do {
				isLocked = READ_LOCK.tryLock(50, TimeUnit.MILLISECONDS);
			}
			while (!isLocked && --retry > 0);
			if (isLocked) {
				Cache caffineCache = getCaffineCache(name);
				Cache.ValueWrapper caffineValueWrapper = caffineCache.get(key);
				if (ObjectUtil.isNotNull(caffineValueWrapper)) {
					return caffineValueWrapper.get();
				}
				Cache redissonCache = getRedissonCache(name);
				Cache.ValueWrapper redissonValueWrapper = redissonCache.get(key);
				if (ObjectUtil.isNotNull(redissonValueWrapper)) {
					Object value = redissonValueWrapper.get();
					caffineCache.putIfAbsent(key, value);
					return value;
				}
				Object value = point.proceed();
				redissonCache.putIfAbsent(key, value);
				return value;
			}
			return point.proceed();
		}
		finally {
			if (isLocked) {
				READ_LOCK.unlock();
			}
		}
	}

	@SneakyThrows
	private Object del(String name, String key, ProceedingJoinPoint point) {
		boolean isLocked = false;
		int retry = 3;
		try {
			do {
				isLocked = WRITE_LOCK.tryLock(50, TimeUnit.MILLISECONDS);
			}
			while (!isLocked && --retry > 0);
			if (isLocked) {
				Cache redissonCache = getRedissonCache(name);
				Cache caffineCache = getCaffineCache(name);
				redissonCache.evictIfPresent(key);
				caffineCache.evictIfPresent(key);
			}
			return point.proceed();
		}
		finally {
			if (isLocked) {
				WRITE_LOCK.unlock();
			}
		}
	}

	private Cache getRedissonCache(String name) {
		Cache cache = redissonCacheManager.getCache(name);
		if (ObjectUtil.isNull(cache)) {
			throw new SystemException("S_Cache_NameNotExist", "缓存名称不存在");
		}
		return cache;
	}

	private Cache getCaffineCache(String name) {
		Cache cache = caffineCacheManager.getCache(name);
		if (ObjectUtil.isNull(cache)) {
			throw new SystemException("S_Cache_NameNotExist", "缓存名称不存在");
		}
		return cache;
	}

}

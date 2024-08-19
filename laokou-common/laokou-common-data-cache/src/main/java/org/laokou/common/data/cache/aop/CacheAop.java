/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.laokou.common.core.utils.SpringExpressionUtil;
import org.laokou.common.data.cache.annotation.DataCache;
import org.laokou.common.data.cache.constant.Type;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * 数据缓存切面.
 *
 * @author laokou
 */
@Component
@Aspect
@RequiredArgsConstructor
public class CacheAop {

	private final CacheManager cacheManager;

	@Around("@annotation(dataCache)")
	public Object doAround(ProceedingJoinPoint point, DataCache dataCache) {
		MethodSignature signature = (MethodSignature) point.getSignature();
		String[] parameterNames = signature.getParameterNames();
		Type type = dataCache.type();
		String name = dataCache.name();
		String field = SpringExpressionUtil.parse(dataCache.key(), parameterNames, point.getArgs(), String.class);
		return switch (type) {
			case GET -> get(name, field, point);
			case DEL -> del(name, field, point);
		};
	}

	@SneakyThrows
	private Object get(String name, String field, ProceedingJoinPoint point) {
		Cache cache = cache(name);
		Cache.ValueWrapper valueWrapper = cache.get(field);
		if (ObjectUtil.isNotNull(valueWrapper)) {
			return valueWrapper.get();
		}
		Object value = point.proceed();
		cache.putIfAbsent(field, value);
		return value;
	}

	@SneakyThrows
	private Object del(String name, String field, ProceedingJoinPoint point) {
		Cache cache = cache(name);
		cache.evictIfPresent(field);
		return point.proceed();
	}

	private Cache cache(String name) {
		Cache cache = cacheManager.getCache(name);
		Assert.isTrue(ObjectUtil.isNotNull(cache), "cache is null");
		return cache;
	}

}

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
package org.laokou.common.data.cache.aspect;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.laokou.common.core.utils.SpringExpressionUtil;
import org.laokou.common.data.cache.annotation.DataCache;
import org.laokou.common.redis.utils.RedisKeyUtil;
import org.laokou.common.redis.utils.RedisUtil;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author laokou
 */
@Component
@Aspect
@RequiredArgsConstructor
public class CacheAspect {

	private final RedisUtil redisUtil;

	private final Cache<String, Object> caffeineCache;

	@Around("@annotation(org.laokou.common.data.cache.annotation.DataCache)")
	public Object doAround(ProceedingJoinPoint point) throws Throwable {
		MethodSignature signature = (MethodSignature) point.getSignature();
		Method method = signature.getMethod();
		String[] parameterNames = signature.getParameterNames();
		DataCache dataCache = AnnotationUtils.findAnnotation(method, DataCache.class);
		Assert.isTrue(Objects.nonNull(dataCache), "@DataCache is not empty");
		long expire = dataCache.expire();
		Type type = dataCache.type();
		String key = dataCache.key();
		String name = dataCache.name();
		Object[] args = point.getArgs();
		key = RedisKeyUtil.getDataCacheKey(name, SpringExpressionUtil.parse(key, parameterNames, args, Long.class));
		return switch (type) {
			case GET -> get(key, point, expire);
			case PUT -> put(key, point, expire);
			case DEL -> del(key, point);
		};
	}

	private Object put(String key, ProceedingJoinPoint point, long expire) throws Throwable {
		Object value = point.proceed();
		redisUtil.set(key, value, expire);
		caffeineCache.put(key, value);
		return value;
	}

	private Object get(String key, ProceedingJoinPoint point, long expire) throws Throwable {
		Object obj = caffeineCache.get(key, t -> redisUtil.get(key));
		if (Objects.nonNull(obj)) {
			return obj;
		}
		Object value = point.proceed();
		redisUtil.setIfAbsent(key, value, expire);
		return value;
	}

	private Object del(String key, ProceedingJoinPoint point) throws Throwable {
		redisUtil.delete(key);
		caffeineCache.invalidate(key);
		return point.proceed();
	}

}

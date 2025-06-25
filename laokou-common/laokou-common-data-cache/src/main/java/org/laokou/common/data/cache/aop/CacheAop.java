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

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.laokou.common.core.util.SpringExpressionUtils;
import org.laokou.common.data.cache.annotation.DataCache;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

/**
 * 数据缓存切面.
 *
 * @author laokou
 */
@Slf4j
@Aspect
@Component
public class CacheAop {

	private final CacheManager redissonCacheManager;

	public CacheAop(@Qualifier("redissonCacheManager") CacheManager redissonCacheManager) {
		this.redissonCacheManager = redissonCacheManager;
	}

	@Around("@annotation(dataCache)")
	public Object doAround(ProceedingJoinPoint point, DataCache dataCache) {
		MethodSignature signature = (MethodSignature) point.getSignature();
		String[] parameterNames = signature.getParameterNames();
		String name = dataCache.name();
		String key = SpringExpressionUtils.parse(dataCache.key(), parameterNames, point.getArgs(), String.class);
		return dataCache.operateType().execute(name, key, point, redissonCacheManager);
	}

}

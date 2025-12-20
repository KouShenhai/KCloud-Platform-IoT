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

package org.laokou.common.data.cache.aspectj;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.laokou.common.core.util.SpringExpressionUtils;
import org.laokou.common.data.cache.annotation.DistributedCache;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

/**
 * 分布式数据缓存切面.
 *
 * @author laokou
 */
@Slf4j
@Aspect
@Component
public class DistributedCacheAspectj {

	private final CacheManager distributedCacheManager;

	public DistributedCacheAspectj(@Qualifier("distributedCacheManager") CacheManager distributedCacheManager) {
		this.distributedCacheManager = distributedCacheManager;
	}

	@Around("@annotation(distributedCache)")
	public Object doAround(ProceedingJoinPoint point, DistributedCache distributedCache) {
		MethodSignature signature = (MethodSignature) point.getSignature();
		String[] parameterNames = signature.getParameterNames();
		String name = distributedCache.name();
		String key = SpringExpressionUtils.parse(distributedCache.key(), parameterNames, point.getArgs(), String.class);
		return distributedCache.operateType().execute(name, key, point, distributedCacheManager);
	}

}

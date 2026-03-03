/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.laokou.common.data.cache.annotation.DataCache;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;

/**
 * CacheAspectj test class.
 *
 * @author laokou
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("DataCacheAspectj Unit Tests")
class DataCacheAspectjTest {

	@Mock
	private CacheManager redisCacheManager;

	@Mock
	private ProceedingJoinPoint point;

	@Mock
	private MethodSignature methodSignature;

	@Mock
	private DataCache dataCache;

	@Mock
	private org.springframework.cache.Cache cache;

	@Mock
	private org.springframework.cache.Cache.ValueWrapper valueWrapper;

	private DataCacheAspectj aspectj;

	@BeforeEach
	void setUp() {
		aspectj = new DataCacheAspectj(redisCacheManager);
	}

	@Test
	@DisplayName("Test doAround with GET operation delegates to cache manager")
	void test_doAround_getOperation_delegatesToCacheManager() {
		// Given
		String cacheName = "testCache";
		String cacheKey = "testKey";
		String cachedValue = "cachedResult";

		Mockito.when(dataCache.name()).thenReturn(cacheName);
		Mockito.when(dataCache.key()).thenReturn("'" + cacheKey + "'");
		Mockito.when(dataCache.operateType()).thenReturn(OperateType.GET);
		Mockito.when(point.getSignature()).thenReturn(methodSignature);
		Mockito.when(methodSignature.getParameterNames()).thenReturn(new String[] {});
		Mockito.when(point.getArgs()).thenReturn(new Object[] {});
		Mockito.when(redisCacheManager.getCache(cacheName)).thenReturn(cache);
		Mockito.when(cache.get(cacheKey)).thenReturn(valueWrapper);
		Mockito.when(valueWrapper.get()).thenReturn(cachedValue);

		// When
		Object result = aspectj.doAround(point, dataCache);

		// Then
		Assertions.assertThat(result).isEqualTo(cachedValue);
		Mockito.verify(redisCacheManager).getCache(cacheName);
	}

	@Test
	@DisplayName("Test doAround with DEL operation evicts cache")
	void test_doAround_delOperation_evictsCache() throws Throwable {
		// Given
		String cacheName = "testCache";
		String cacheKey = "testKey";
		Object expectedResult = "result";

		Mockito.when(dataCache.name()).thenReturn(cacheName);
		Mockito.when(dataCache.key()).thenReturn("'" + cacheKey + "'");
		Mockito.when(dataCache.operateType()).thenReturn(OperateType.DEL);
		Mockito.when(point.getSignature()).thenReturn(methodSignature);
		Mockito.when(methodSignature.getParameterNames()).thenReturn(new String[] {});
		Mockito.when(point.getArgs()).thenReturn(new Object[] {});
		Mockito.when(redisCacheManager.getCache(cacheName)).thenReturn(cache);
		Mockito.when(point.proceed()).thenReturn(expectedResult);

		// When
		Object result = aspectj.doAround(point, dataCache);

		// Then
		Assertions.assertThat(result).isEqualTo(expectedResult);
		Mockito.verify(cache).evictIfPresent(cacheKey);
		Mockito.verify(point).proceed();
	}

	@Test
	@DisplayName("Test doAround with SpEL key expression parses correctly")
	void test_doAround_spelKeyExpression_parsesCorrectly() throws Throwable {
		// Given
		String cacheName = "userCache";
		String userId = "123";
		Object expectedResult = "user";

		Mockito.when(dataCache.name()).thenReturn(cacheName);
		Mockito.when(dataCache.key()).thenReturn("#userId");
		Mockito.when(dataCache.operateType()).thenReturn(OperateType.GET);
		Mockito.when(point.getSignature()).thenReturn(methodSignature);
		Mockito.when(methodSignature.getParameterNames()).thenReturn(new String[] { "userId" });
		Mockito.when(point.getArgs()).thenReturn(new Object[] { userId });
		Mockito.when(redisCacheManager.getCache(cacheName)).thenReturn(cache);
		Mockito.when(cache.get(userId)).thenReturn(null);
		Mockito.when(point.proceed()).thenReturn(expectedResult);
		Mockito.when(cache.putIfAbsent(userId, expectedResult)).thenReturn(null);

		// When
		Object result = aspectj.doAround(point, dataCache);

		// Then
		Assertions.assertThat(result).isEqualTo(expectedResult);
		Mockito.verify(cache).get(userId);
	}

}

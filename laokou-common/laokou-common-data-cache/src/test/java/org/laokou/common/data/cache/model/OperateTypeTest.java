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

package org.laokou.common.data.cache.model;

import org.aspectj.lang.ProceedingJoinPoint;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.laokou.common.i18n.common.exception.GlobalException;
import org.laokou.common.i18n.common.exception.SystemException;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.SimpleValueWrapper;

/**
 * OperateTypeEnum test class.
 *
 * @author laokou
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("OperateTypeEnum Unit Tests")
class OperateTypeTest {

	@Mock
	private CacheManager cacheManager;

	@Mock
	private Cache cache;

	@Mock
	private ProceedingJoinPoint point;

	@Mock
	private Cache.ValueWrapper valueWrapper;

	private static final String CACHE_NAME = "testCache";

	private static final String CACHE_KEY = "testKey";

	@BeforeEach
	void setUp() {
		Mockito.lenient().when(cacheManager.getCache(CACHE_NAME)).thenReturn(cache);
	}

	@Nested
	@DisplayName("GET Operation Tests")
	class GetOperationTests {

		@Test
		@DisplayName("Test GET with cache hit returns cached value")
		void test_get_cacheHit_returnsCachedValue() throws Throwable {
			// Given
			String cachedValue = "cachedResult";
			Mockito.when(cache.get(CACHE_KEY)).thenReturn(valueWrapper);
			Mockito.when(valueWrapper.get()).thenReturn(cachedValue);

			// When
			Object result = OperateType.GET.execute(CACHE_NAME, CACHE_KEY, point, cacheManager);

			// Then
			Assertions.assertThat(result).isEqualTo(cachedValue);
			Mockito.verify(point, Mockito.never()).proceed();
		}

		@Test
		@DisplayName("Test GET with cache miss executes and caches result")
		void test_get_cacheMiss_executesAndCaches() throws Throwable {
			// Given
			String newValue = "newResult";
			Mockito.when(cache.get(CACHE_KEY)).thenReturn(null);
			Mockito.when(point.proceed()).thenReturn(newValue);
			Mockito.when(cache.putIfAbsent(CACHE_KEY, newValue)).thenReturn(null);

			// When
			Object result = OperateType.GET.execute(CACHE_NAME, CACHE_KEY, point, cacheManager);

			// Then
			Assertions.assertThat(result).isEqualTo(newValue);
			Mockito.verify(point).proceed();
			Mockito.verify(cache).putIfAbsent(CACHE_KEY, newValue);
		}

		@Test
		@DisplayName("Test GET with cache miss and concurrent put returns old value")
		void test_get_cacheMissConcurrentPut_returnsOldValue() throws Throwable {
			// Given
			String newValue = "newResult";
			String oldValue = "oldResult";
			Mockito.when(cache.get(CACHE_KEY)).thenReturn(null);
			Mockito.when(point.proceed()).thenReturn(newValue);
			// putIfAbsent returns a ValueWrapper when there's an existing value
			Mockito.when(cache.putIfAbsent(CACHE_KEY, newValue)).thenReturn(new SimpleValueWrapper(oldValue));

			// When
			Object result = OperateType.GET.execute(CACHE_NAME, CACHE_KEY, point, cacheManager);

			// Then - the source code returns the ValueWrapper directly, which is the
			// actual
			// behavior
			Assertions.assertThat(result).isInstanceOf(Cache.ValueWrapper.class);
			Assertions.assertThat(((Cache.ValueWrapper) result).get()).isEqualTo(oldValue);
		}

		@Test
		@DisplayName("Test GET with GlobalException rethrows exception")
		void test_get_globalException_rethrows() throws Throwable {
			// Given
			GlobalException exception = new SystemException("S_Test_Error", "Test error");
			Mockito.when(cache.get(CACHE_KEY)).thenReturn(null);
			Mockito.when(point.proceed()).thenThrow(exception);

			// When & Then
			Assertions.assertThatThrownBy(() -> OperateType.GET.execute(CACHE_NAME, CACHE_KEY, point, cacheManager))
				.isInstanceOf(GlobalException.class)
				.isSameAs(exception);
		}

		@Test
		@DisplayName("Test GET with Throwable wraps in SystemException")
		void test_get_throwable_wrapsInSystemException() throws Throwable {
			// Given
			RuntimeException exception = new RuntimeException("Unexpected error");
			Mockito.when(cache.get(CACHE_KEY)).thenReturn(null);
			Mockito.when(point.proceed()).thenThrow(exception);

			// When & Then
			Assertions.assertThatThrownBy(() -> OperateType.GET.execute(CACHE_NAME, CACHE_KEY, point, cacheManager))
				.isInstanceOf(SystemException.class)
				.hasMessageContaining("获取缓存失败");
		}

	}

	@Nested
	@DisplayName("DEL Operation Tests")
	class DelOperationTests {

		@Test
		@DisplayName("Test DEL evicts cache and proceeds")
		void test_del_cacheExists_evictsAndProceeds() throws Throwable {
			// Given
			Object expectedResult = "result";
			Mockito.when(point.proceed()).thenReturn(expectedResult);

			// When
			Object result = OperateType.DEL.execute(CACHE_NAME, CACHE_KEY, point, cacheManager);

			// Then
			Assertions.assertThat(result).isEqualTo(expectedResult);
			Mockito.verify(cache).evictIfPresent(CACHE_KEY);
			Mockito.verify(point).proceed();
		}

		@Test
		@DisplayName("Test DEL with GlobalException rethrows exception")
		void test_del_globalException_rethrows() throws Throwable {
			// Given
			GlobalException exception = new SystemException("S_Test_Error", "Test error");
			Mockito.when(point.proceed()).thenThrow(exception);

			// When & Then
			Assertions.assertThatThrownBy(() -> OperateType.DEL.execute(CACHE_NAME, CACHE_KEY, point, cacheManager))
				.isInstanceOf(GlobalException.class)
				.isSameAs(exception);
		}

		@Test
		@DisplayName("Test DEL with Throwable wraps in SystemException")
		void test_del_throwable_wrapsInSystemException() throws Throwable {
			// Given
			RuntimeException exception = new RuntimeException("Unexpected error");
			Mockito.when(point.proceed()).thenThrow(exception);

			// When & Then
			Assertions.assertThatThrownBy(() -> OperateType.DEL.execute(CACHE_NAME, CACHE_KEY, point, cacheManager))
				.isInstanceOf(SystemException.class)
				.hasMessageContaining("删除缓存失败");
		}

	}

	@Nested
	@DisplayName("getCache Method Tests")
	class GetCacheTests {

		@Test
		@DisplayName("Test getCache with null cache throws SystemException")
		void test_getCache_nullCache_throwsSystemException() {
			// Given
			Mockito.when(cacheManager.getCache("nonExistent")).thenReturn(null);

			// When & Then
			Assertions.assertThatThrownBy(() -> OperateType.getCache(cacheManager, "nonExistent"))
				.isInstanceOf(SystemException.class)
				.hasMessageContaining("缓存名称不存在");
		}

		@Test
		@DisplayName("Test getCache with valid cache returns cache")
		void test_getCache_validCache_returnsCache() {
			// When
			Cache result = OperateType.getCache(cacheManager, CACHE_NAME);

			// Then
			Assertions.assertThat(result).isSameAs(cache);
		}

	}

	@Nested
	@DisplayName("Enum Properties Tests")
	class EnumPropertiesTests {

		@Test
		@DisplayName("Test GET enum has correct code and desc")
		void test_get_enumProperties_areCorrect() {
			// Then
			Assertions.assertThat(OperateType.GET.getCode()).isEqualTo("get");
			Assertions.assertThat(OperateType.GET.getDesc()).isEqualTo("查看");
		}

		@Test
		@DisplayName("Test DEL enum has correct code and desc")
		void test_del_enumProperties_areCorrect() {
			// Then
			Assertions.assertThat(OperateType.DEL.getCode()).isEqualTo("del");
			Assertions.assertThat(OperateType.DEL.getDesc()).isEqualTo("删除");
		}

		@Test
		@DisplayName("Test all enum values are defined")
		void test_values_allDefined_hasExpectedCount() {
			// Then
			Assertions.assertThat(OperateType.values()).hasSize(2);
			Assertions.assertThat(OperateType.values()).containsExactlyInAnyOrder(OperateType.GET, OperateType.DEL);
		}

	}

}

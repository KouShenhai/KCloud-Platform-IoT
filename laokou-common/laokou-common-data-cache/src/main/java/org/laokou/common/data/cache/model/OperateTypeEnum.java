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

package org.laokou.common.data.cache.model;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.laokou.common.i18n.common.exception.GlobalException;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.util.ObjectUtils;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

/**
 * 操作类型枚举.
 *
 * @author laokou
 */
@Slf4j
@Getter
public enum OperateTypeEnum {

	GET("get", "查看") {
		@Override
		public Object execute(String name, String key, ProceedingJoinPoint point, CacheManager redissonCacheManager) {
			try {
				Cache redissonCache = getCache(redissonCacheManager, name);
				Cache.ValueWrapper redissonValueWrapper = redissonCache.get(key);
				if (ObjectUtils.isNotNull(redissonValueWrapper)) {
					return redissonValueWrapper.get();
				}
				Object value = point.proceed();
				redissonCache.putIfAbsent(key, value);
				return value;
			}
			catch (GlobalException e) {
				// 系统异常/业务异常/参数异常直接捕获并抛出
				throw e;
			}
			catch (Throwable e) {
				log.error("获取缓存失败", e);
				throw new SystemException("S_Cache_GetError", "获取缓存失败", e);
			}
		}
	},

	DEL("del", "删除") {
		@Override
		public Object execute(String name, String key, ProceedingJoinPoint point, CacheManager redissonCacheManager) {
			try {
				getCache(redissonCacheManager, name).evictIfPresent(key);
				return point.proceed();
			}
			catch (GlobalException e) {
				// 系统异常/业务异常/参数异常直接捕获并抛出
				throw e;
			}
			catch (Throwable e) {
				log.error("获取缓存失败，错误信息：{}", e.getMessage(), e);
				throw new SystemException("S_Cache_DelError", String.format("删除缓存失败，%s", e.getMessage()), e);
			}
		}
	};

	private final String code;

	private final String desc;

	OperateTypeEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public abstract Object execute(String name, String key, ProceedingJoinPoint point,
			CacheManager redissonCacheManager);

	public static Cache getCache(CacheManager cacheManager, String name) {
		Cache cache = cacheManager.getCache(name);
		if (ObjectUtils.isNull(cache)) {
			throw new SystemException("S_Cache_NameNotExist", "缓存名称不存在");
		}
		return cache;
	}

}

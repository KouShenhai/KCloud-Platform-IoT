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

package org.laokou.common.lock.aspectj;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.laokou.common.core.util.SpringExpressionUtils;
import org.laokou.common.i18n.common.constant.StringConstants;
import org.laokou.common.i18n.common.exception.BizException;
import org.laokou.common.i18n.util.StringExtUtils;
import org.laokou.common.lock.Type;
import org.laokou.common.lock.annotation.Lock4j;
import org.laokou.common.lock.util.LockUtils;
import org.laokou.common.redis.util.RedisUtils;
import org.springframework.stereotype.Component;

/**
 * 分布式锁切面.
 *
 * @author laokou
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LockAspectj {

	private final RedisUtils redisUtils;

	@Around("@annotation(lock4j)")
	public Object doAround(ProceedingJoinPoint joinPoint, Lock4j lock4j) throws Throwable {
		// 获取注解
		Signature signature = joinPoint.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		String[] parameterNames = methodSignature.getParameterNames();
		String name = lock4j.name();
		String key = lock4j.key();
		if (StringExtUtils.isNotEmpty(key) && key.contains("#")) {
			key = name + StringConstants.UNDER
					+ SpringExpressionUtils.parse(key, parameterNames, joinPoint.getArgs(), String.class);
		}
		else {
			key = name;
		}
		long timeout = lock4j.timeout();
		int retry = lock4j.retry();
		Type lockType = lock4j.type();
		return LockUtils.executeWithLock(key, lockType, timeout, retry, redisUtils, () -> {
			try {
				return joinPoint.proceed();
			}
			catch (Throwable ex) {
				log.error("获取值失败，错误信息：{}", ex.getMessage(), ex);
				throw new BizException("B_Value_GetFailed", "获取值失败", ex);
			}
		});
	}

}

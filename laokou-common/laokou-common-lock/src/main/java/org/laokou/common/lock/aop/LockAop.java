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

package org.laokou.common.lock.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.laokou.common.core.utils.SpringExpressionUtil;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.lock.Lock;
import org.laokou.common.lock.RedissonLock;
import org.laokou.common.lock.TypeEnum;
import org.laokou.common.lock.annotation.Lock4j;
import org.laokou.common.redis.utils.RedisUtil;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.reflect.Method;

import static org.laokou.common.i18n.common.exception.StatusCode.TOO_MANY_REQUESTS;

/**
 * 分布式锁切面.
 *
 * @author laokou
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LockAop {

	private final RedisUtil redisUtil;

	@Around("@annotation(org.laokou.common.lock.annotation.Lock4j)")
	public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
		Object obj;
		// 获取注解
		Signature signature = joinPoint.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		Method method = methodSignature.getMethod();
		String[] parameterNames = methodSignature.getParameterNames();
		Lock4j lock4j = AnnotationUtils.findAnnotation(method, Lock4j.class);
		Assert.isTrue(ObjectUtil.isNotNull(lock4j), "@Lock4j is null");
		String expression = lock4j.key();
		if (expression.contains("#{") && expression.contains("}")) {
			expression = SpringExpressionUtil.parse(expression, parameterNames, joinPoint.getArgs(), String.class);
		}
		long expire = lock4j.expire();
		long timeout = lock4j.timeout();
		int retry = lock4j.retry();
		final TypeEnum lockType = lock4j.type();
		Lock lock = new RedissonLock(redisUtil);

		boolean isLocked = false;

		try {

			do {
				// 设置锁的自动过期时间，则执行业务的时间一定要小于锁的自动过期时间，否则就会报错
				isLocked = lock.tryLock(lockType, expression, expire, timeout);
			}
			while (!isLocked && --retry > 0);

			if (!isLocked) {
				throw new SystemException(TOO_MANY_REQUESTS);
			}

			obj = joinPoint.proceed();
		}
		catch (Throwable throwable) {
			log.error("错误信息：{}，详情见日志", LogUtil.record(throwable.getMessage()), throwable);
			throw throwable;
		}
		finally {
			// 释放锁
			if (isLocked) {
				lock.unlock(lockType, expression);
			}
		}
		return obj;
	}

}

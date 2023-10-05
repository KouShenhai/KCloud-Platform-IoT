/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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
package org.laokou.common.lock.aspect;

import lombok.RequiredArgsConstructor;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.i18n.common.GlobalException;
import org.laokou.common.lock.annotation.Lock4j;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.laokou.common.lock.enums.LockScope;
import org.laokou.common.lock.enums.LockType;
import org.laokou.common.lock.factory.LockFactory;
import org.laokou.common.lock.factory.Locks;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;

/**
 * @author laokou
 */
@Component
@Aspect
@Slf4j
@RequiredArgsConstructor
public class LockAspect {

	private final LockFactory factory;

	@Around(value = "@annotation(org.laokou.common.lock.annotation.Lock4j)")
	public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
		// 获取注解
		Signature signature = joinPoint.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		Method method = methodSignature.getMethod();
		Lock4j lock4j = method.getAnnotation(Lock4j.class);
		if (lock4j == null) {
			lock4j = AnnotationUtils.findAnnotation(method, Lock4j.class);
		}
		assert lock4j != null;
		// 时间戳
		String key = lock4j.key() + IdGenerator.SystemClock.now();
		long expire = lock4j.expire();
		long timeout = lock4j.timeout();
		final LockType type = lock4j.type();
		final LockScope scope = lock4j.scope();
		Locks locks = factory.build(scope);
		Object obj = null;
		// 设置锁的自动过期时间，则执行业务的时间一定要小于锁的自动过期时间，否则就会报错
		try {
			if (locks.tryLock(type, key, expire, timeout)) {
				obj = joinPoint.proceed();
			}
			else {
				throw new GlobalException("请求太频繁，请稍后再试");
			}
		}
		catch (Throwable throwable) {
			log.error("异常信息：{}", throwable.getMessage());
			throw throwable;
		}
		finally {
			// 释放锁
			locks.unlock(type, key);
		}
		return obj;
	}

}

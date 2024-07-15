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

package org.laokou.common.nacos.aop;

import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.laokou.common.core.context.ShutdownHolder;
import org.laokou.common.i18n.common.exception.SystemException;
import org.springframework.stereotype.Component;

import static org.laokou.common.i18n.common.exception.StatusCode.SERVICE_UNAVAILABLE;

/**
 * @author laokou
 */
@Aspect
@Component
public class RequestAop {

	@Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
	public void postMapping() {
	}

	@Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
	public void getMapping() {
	}

	@Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
	public void putMapping() {
	}

	@Pointcut("@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
	public void deleteMapping() {
	}

	@Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
	public void requestMapping() {
	}

	@Before("postMapping() || getMapping() || putMapping() || deleteMapping() || requestMapping()")
	public void doBefore() {
		if (ShutdownHolder.status()) {
			throw new SystemException(SERVICE_UNAVAILABLE);
		}
		ShutdownHolder.add();
	}

	@SneakyThrows
	@Around("postMapping() || getMapping() || putMapping() || deleteMapping() || requestMapping()")
	public Object doAround(ProceedingJoinPoint joinPoint) {
		try {
			return joinPoint.proceed();
		}
		finally {
			ShutdownHolder.sub();
		}
	}

}

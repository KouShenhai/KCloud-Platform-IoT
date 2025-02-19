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

package org.laokou.common.log.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.laokou.common.core.utils.RequestUtil;
import org.laokou.common.core.utils.SpringUtil;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.log.model.LogE;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * 操作日志切面.
 *
 * @author laokou
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class OperateLogAop {

	private final SpringUtil springUtil;

	@SneakyThrows
	@Around("@annotation(operateLog)")
	public Object doAround(ProceedingJoinPoint point, OperateLog operateLog) {
		StopWatch stopWatch = new StopWatch("操作日志");
		stopWatch.start();
		// 服务ID
		String serviceId = springUtil.getServiceId();
		HttpServletRequest request = RequestUtil.getHttpServletRequest();
		LogE operate = new LogE(operateLog.module(), operateLog.operation(), request, serviceId);
		String className = point.getTarget().getClass().getName();
		String methodName = point.getSignature().getName();
		Object[] args = point.getArgs();
		// 组装类名
		operate.decorateMethodName(className, methodName);
		// 组装请求参数
		operate.decorateRequestParams(args);
		Object proceed;
		Throwable throwable = null;
		try {
			proceed = point.proceed();
		}
		catch (Throwable e) {
			throw throwable = e;
		}
		finally {
			// 计算消耗时间
			operate.calculateTaskTime(stopWatch);
			// 修改错误
			operate.updateThrowable(throwable);
		}
		return proceed;
	}

}

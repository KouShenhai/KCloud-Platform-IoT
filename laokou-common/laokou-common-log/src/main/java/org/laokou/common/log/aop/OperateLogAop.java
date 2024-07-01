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

package org.laokou.common.log.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.core.utils.RequestUtil;
import org.laokou.common.core.utils.SpringContextUtil;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.log.model.LogA;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.reflect.Method;

/**
 * 操作日志切面.
 *
 * @author laokou
 */
@Aspect
@Component
@RequiredArgsConstructor
public class OperateLogAop {

	private final SpringContextUtil springContextUtil;

	private static final ThreadLocal<Long> TASK_TIME_LOCAL = new ThreadLocal<>();

	@Before("@annotation(org.laokou.common.log.annotation.OperateLog)")
	public void doBefore() {
		TASK_TIME_LOCAL.set(IdGenerator.SystemClock.now());
	}

	/**
	 * 处理完请求后执行.
	 * @param joinPoint 切面对象
	 */
	@AfterReturning(pointcut = "@annotation(org.laokou.common.log.annotation.OperateLog)")
	public void doAfterReturning(JoinPoint joinPoint) {
		handleLog(joinPoint, null);
	}

	/**
	 * 异常处理。
	 * @param joinPoint 切面对象
	 * @param e 异常
	 */
	@AfterThrowing(pointcut = "@annotation(org.laokou.common.log.annotation.OperateLog)", throwing = "e")
	public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
		handleLog(joinPoint, e);
	}

	private void handleLog(JoinPoint joinPoint, Exception e) {
		try {
			// 应用名称
			String appName = springContextUtil.getAppName();
			HttpServletRequest request = RequestUtil.getHttpServletRequest();
			MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
			Method method = methodSignature.getMethod();
			OperateLog log = AnnotationUtils.findAnnotation(method, OperateLog.class);
			Assert.isTrue(ObjectUtil.isNotNull(log), "@OperateLog is null");
			String className = joinPoint.getTarget().getClass().getName();
			String methodName = joinPoint.getSignature().getName();
			Object[] args = joinPoint.getArgs();
			LogA operate = new LogA(log.module(), log.operation(), request, appName);
			// 组装类名
			operate.decorateMethodName(className, methodName);
			// 组装请求参数
			operate.decorateRequestParams(args);
			// 计算消耗时间
			operate.calculateTaskTime(TASK_TIME_LOCAL.get());
			// 修改状态
			operate.updateStatus(e);
		}
		finally {
			TASK_TIME_LOCAL.remove();
		}
	}

}

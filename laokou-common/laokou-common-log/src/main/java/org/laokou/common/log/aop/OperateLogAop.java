/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.core.utils.RequestUtil;
import org.laokou.common.domain.context.DomainEventContextHolder;
import org.laokou.common.domain.publish.DomainEventPublisher;
import org.laokou.common.domain.service.DomainEventService;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.core.common.domain.OperateLog;
import org.springframework.core.NamedThreadLocal;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.reflect.Method;

import static org.laokou.common.i18n.common.JobModeEnum.SYNC;
import static org.laokou.common.i18n.common.PropertiesConstant.SPRING_APPLICATION_NAME;

/**
 * 操作日志切面.
 *
 * @author laokou
 */
@Component
@Aspect
@Slf4j
@RequiredArgsConstructor
public class OperateLogAop {

	private final Environment environment;

	private final DomainEventService domainEventService;

	private final DomainEventPublisher domainEventPublisher;

	private static final ThreadLocal<Long> TASK_TIME_LOCAL = new NamedThreadLocal<>("耗时");

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

	private void handleLog(final JoinPoint joinPoint, final Exception e) {
		try {
			// 应用名称
			String appName = environment.getProperty(SPRING_APPLICATION_NAME);
			HttpServletRequest request = RequestUtil.getHttpServletRequest();
			MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
			Method method = methodSignature.getMethod();
			org.laokou.common.log.annotation.OperateLog operateLog = AnnotationUtils.findAnnotation(method,
					org.laokou.common.log.annotation.OperateLog.class);
			Assert.isTrue(ObjectUtil.isNotNull(operateLog), "@OperateLog is null");
			String className = joinPoint.getTarget().getClass().getName();
			String methodName = joinPoint.getSignature().getName();
			Object[] args = joinPoint.getArgs();
			OperateLog operate = new OperateLog(operateLog.module(), operateLog.operation());
			// 组装类名
			operate.decorateMethodName(className, methodName);
			// 组装请求参数
			operate.decorateRequestParams(args);
			// 计算消耗时间
			operate.calculateTaskTime(TASK_TIME_LOCAL.get());
			// 修改状态
			operate.modifyStatus(e, request, appName);
			// 保存领域事件（事件溯源）
			domainEventService.create(operate.getEvents());
			// 发布当前线程的领域事件(同步发布)
			domainEventPublisher.publish(SYNC);
			// 清除领域事件上下文
			DomainEventContextHolder.clear();
			// 清空领域事件
			operate.clearEvents();
		}
		finally {
			TASK_TIME_LOCAL.remove();
		}
	}

}

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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.laokou.common.core.util.RequestUtils;
import org.laokou.common.core.util.SpringUtils;
import org.laokou.common.domain.support.DomainEventPublisher;
import org.laokou.common.fory.config.ForyFactory;
import org.laokou.common.log.annotation.OperateLog;
import org.laokou.common.log.convertor.OperateLogConvertor;
import org.laokou.common.log.model.MqEnum;
import org.laokou.common.log.model.OperateLogA;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.core.env.Environment;
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
@MapperScan(basePackages = "org.laokou.common.log.mapper")
@RequiredArgsConstructor
public class OperateLogAop {

	private final SpringUtils springUtils;

	private final Environment environment;

	private final DomainEventPublisher kafkaDomainEventPublisher;

	static {
		ForyFactory.INSTANCE.register(org.laokou.common.log.handler.event.OperateEvent.class);
	}

	@Around("@annotation(operateLog)")
	public Object doAround(ProceedingJoinPoint point, OperateLog operateLog) throws Throwable {
		StopWatch stopWatch = new StopWatch("操作日志");
		stopWatch.start();
		OperateLogA operateLogA = OperateLogConvertor.toEntity();
		operateLogA.getModuleName(operateLog.module());
		operateLogA.getName(operateLog.operation());
		operateLogA.getServiceId(springUtils.getServiceId());
		operateLogA.getRequest(RequestUtils.getHttpServletRequest());
		operateLogA.getProfile(environment.getActiveProfiles()[0]);
		String className = point.getTarget().getClass().getName();
		String methodName = point.getSignature().getName();
		// 组装类名
		operateLogA.decorateMethodName(className, methodName);
		// 组装请求参数
		operateLogA.decorateRequestParams(point.getArgs());
		Throwable throwable = null;
		try {
			return point.proceed();
		}
		catch (Throwable e) {
			throw throwable = e;
		}
		finally {
			// 计算消耗时间
			operateLogA.calculateTaskTime(stopWatch);
			// 获取错误
			operateLogA.getThrowable(throwable);
			// 发布事件
			kafkaDomainEventPublisher.publish(MqEnum.OPERATE_LOG.getTopic(),
					OperateLogConvertor.toDomainEvent(operateLogA));
		}
	}

}

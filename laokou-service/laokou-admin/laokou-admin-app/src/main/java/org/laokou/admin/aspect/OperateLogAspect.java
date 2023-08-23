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
package org.laokou.admin.aspect;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.HttpHeaders;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.laokou.admin.client.dto.domainevent.OperateLogEvent;
import org.laokou.admin.domain.annotation.OperateLog;
import org.laokou.common.core.constant.Constant;
import org.laokou.common.core.utils.*;
import org.laokou.common.ip.region.utils.AddressUtil;
import org.laokou.common.security.utils.UserUtil;
import org.springframework.core.NamedThreadLocal;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.laokou.common.core.constant.Constant.FAIL_STATUS;
import static org.laokou.common.core.constant.Constant.SUCCESS_STATUS;

/**
 * @author laokou
 */
@Component
@Aspect
@Slf4j
public class OperateLogAspect {

	private static final String[] REMOVE_PARAMS = { "username", "password", "mobile", "mail" };

	private static final ThreadLocal<StopWatch> TASK_TIME_LOCAL = new NamedThreadLocal<>("耗时");

	@Before(value = "@annotation(org.laokou.admin.domain.annotation.OperateLog)")
	public void doBefore() {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		TASK_TIME_LOCAL.set(stopWatch);
	}

	/**
	 * 处理完请求后执行
	 */
	@AfterReturning(pointcut = "@annotation(org.laokou.admin.domain.annotation.OperateLog)")
	public void doAfterReturning(JoinPoint joinPoint) {
		handleLog(joinPoint, null);
	}

	@AfterThrowing(pointcut = "@annotation(org.laokou.admin.domain.annotation.OperateLog)", throwing = "e")
	public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
		handleLog(joinPoint, e);
	}

	@Async
	protected void handleLog(final JoinPoint joinPoint, final Exception e) {
		HttpServletRequest request = RequestUtil.getHttpServletRequest();
		// 获取注解
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		Method method = methodSignature.getMethod();
		OperateLog operateLog = method.getAnnotation(OperateLog.class);
		if (operateLog == null) {
			operateLog = AnnotationUtils.findAnnotation(method, OperateLog.class);
		}
		// 构建事件对象
		assert operateLog != null;
		OperateLogEvent event = buildEvent(operateLog, request, joinPoint, e);
		SpringContextUtil.publishEvent(event);
	}

	private OperateLogEvent buildEvent(OperateLog operateLog, HttpServletRequest request, JoinPoint joinPoint,
			Exception e) {
		try {
			String ip = IpUtil.getIpAddr(request);
			String className = joinPoint.getTarget().getClass().getName();
			String methodName = joinPoint.getSignature().getName();
			Object[] args = joinPoint.getArgs();
			List<Object> params = new ArrayList<>(Arrays.asList(args)).stream().filter(this::filterArgs).toList();
			OperateLogEvent event = new OperateLogEvent(this);
			assert operateLog != null;
			event.setModuleName(operateLog.module());
			event.setOperationName(operateLog.operation());
			event.setRequestUri(request.getRequestURI());
			event.setRequestIp(ip);
			event.setRequestAddress(AddressUtil.getRealAddress(ip));
			event.setOperator(UserUtil.getUserName());
			event.setDeptId(UserUtil.getDeptId());
			if (null != e) {
				event.setRequestStatus(FAIL_STATUS);
				event.setErrorMsg(e.getMessage());
			}
			else {
				event.setRequestStatus(SUCCESS_STATUS);
			}
			event.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
			event.setMethodName(className + "." + methodName + "()");
			event.setRequestMethod(request.getMethod());
			Object obj;
			if (CollectionUtil.isEmpty(params)) {
				obj = null;
			}
			else {
				obj = params.get(0);
			}
			if (obj == null) {
				event.setRequestParams(JacksonUtil.EMPTY_JSON);
			}
			else {
				String str = JacksonUtil.toJsonStr(obj);
				if (Constant.RISK.contains(str)) {
					Map<String, String> map = removeAny(JacksonUtil.toMap(str, String.class, String.class),
							REMOVE_PARAMS);
					event.setRequestParams(JacksonUtil.toJsonStr(map, true));
				}
				else {
					event.setRequestParams(str);
				}
			}
			event.setTenantId(UserUtil.getTenantId());
			StopWatch stopWatch = TASK_TIME_LOCAL.get();
			stopWatch.stop();
			event.setTakeTime(stopWatch.getTotalTimeMillis());
			return event;
		}
		catch (Exception ex) {
			log.error("错误信息：{}", ex.getMessage());
			throw ex;
		}
		finally {
			TASK_TIME_LOCAL.remove();
		}
	}

	private boolean filterArgs(Object arg) {
		return !(arg instanceof HttpServletRequest) && !(arg instanceof MultipartFile)
				&& !(arg instanceof HttpServletResponse);
	}

	private Map<String, String> removeAny(Map<String, String> map, String... keys) {
		for (String key : keys) {
			map.remove(key);
		}
		return map;
	}

}

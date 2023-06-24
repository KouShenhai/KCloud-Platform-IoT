/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.laokou.common.trace.aspect;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.laokou.common.i18n.core.HttpResult;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import static org.laokou.common.core.constant.Constant.TRACE_ID;

/**
 * @author laokou
 */
@Component
@Aspect
@Slf4j
public class TraceLogAspect {

	@SneakyThrows
	@Around("@annotation(org.laokou.common.trace.annotation.TraceLog)")
	public Object doAround(ProceedingJoinPoint point) {
		Object proceed = point.proceed();
		if (proceed instanceof HttpResult<?> result) {
			result.setTraceId(MDC.get(TRACE_ID));
			return result;
		}
		return proceed;
	}

}

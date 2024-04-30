/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

package org.laokou.common.sensitive.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.sensitive.utils.SensitiveUtil;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Component
@Aspect
@Slf4j
public class SensitiveAop {

	@Around("@annotation(org.laokou.common.sensitive.annotation.Sensitive)")
	public Object doAround(ProceedingJoinPoint point) throws Throwable {
		Object proceed = point.proceed();
		if (proceed instanceof Result<?> result) {
			Object data = result.getData();
			SensitiveUtil.transform(data);
		}
		return proceed;
	}

}

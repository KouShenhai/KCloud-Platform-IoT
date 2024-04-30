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

package org.laokou.common.crypto.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.laokou.common.crypto.annotation.Crypto;
import org.laokou.common.crypto.utils.AesUtil;
import org.laokou.common.i18n.common.AlgorithmEnum;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * AES切面.
 *
 * @author laokou
 */
@Aspect
@Component
@Slf4j
public class AesAop {

	@Around("@annotation(org.laokou.common.crypto.annotation.Crypto)")
	public Object doAround(ProceedingJoinPoint point) throws Throwable {
		MethodSignature signature = (MethodSignature) point.getSignature();
		Method method = signature.getMethod();
		Crypto crypto = AnnotationUtils.findAnnotation(method, Crypto.class);
		Object proceed = point.proceed();
		if (ObjectUtil.requireNotNull(crypto).type() == AlgorithmEnum.AES) {
			if (proceed instanceof Result<?> result) {
				Object data = result.getData();
				AesUtil.transform(data);
			}
		}
		return proceed;
	}

}

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

package org.laokou.common.jasypt.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Component
@Aspect
@Slf4j
public class AesAspect {

	@Before(value = "@annotation(org.laokou.common.jasypt.annotation.Aes)")
	public void doBefore(JoinPoint point) {
		Object arg = point.getArgs()[0];

		MethodSignature methodSignature = (MethodSignature) point.getSignature();
		// Method method = methodSignature.getMethod();
		// Aes aes = AnnotationUtils.findAnnotation(method, Aes.class);
		// Object proceed = point.proceed();
		// switch (Objects.requireNonNull(aes).type()) {
		// case ENCRYPT -> {
		// if (proceed != null) {
		// return AesUtil.encrypt(proceed.toString());
		// }
		// }
		// case DECRYPT -> {
		// if (proceed != null) {
		// return AesUtil.decrypt(proceed.toString());
		// }
		// }
		// default -> {
		//
		// }
		// }
	}

}

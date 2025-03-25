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

package org.laokou.common.core.util;

import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.common.exception.SystemException;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author laokou
 */
@Slf4j
public final class ClassUtils extends org.springframework.util.ClassUtils {

	private ClassUtils() {
	}

	public static Class<?> parseClass(String className) throws ClassNotFoundException {
		return Class.forName(className);
	}

	public static Set<Class<?>> scanAnnotatedClasses(String basePackage, Class<? extends Annotation> annotationType) {
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AnnotationTypeFilter(annotationType));
		return scanner.findCandidateComponents(basePackage).stream().map(beanDefinition -> {
			try {
				return Class.forName(beanDefinition.getBeanClassName());
			}
			catch (ClassNotFoundException e) {
				log.error("类扫描失败，错误信息：{}", e.getMessage(), e);
				throw new SystemException("S_UnKnow_Error", String.format("类%s扫描失败", beanDefinition.getBeanClassName()),
						e);
			}
		}).collect(Collectors.toSet());
	}

}

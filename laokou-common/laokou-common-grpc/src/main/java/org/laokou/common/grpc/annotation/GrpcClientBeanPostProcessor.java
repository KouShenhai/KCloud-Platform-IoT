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

package org.laokou.common.grpc.annotation;

import org.jspecify.annotations.NonNull;
import org.laokou.common.i18n.util.ObjectUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.grpc.client.GrpcClientFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * @author laokou
 */
public record GrpcClientBeanPostProcessor(GrpcClientFactory grpcClientFactory,
		DiscoveryClient discoveryClient) implements BeanPostProcessor {

	@Override
	public Object postProcessBeforeInitialization(@NonNull Object bean, @NonNull String beanName)
			throws BeansException {
		Class<?> clazz = bean.getClass();
		do {
			processFields(bean, clazz);
			clazz = clazz.getSuperclass();
		}
		while (ObjectUtils.isNotNull(clazz));
		return bean;
	}

	private void processFields(Object bean, Class<?> clazz) {
		for (Field field : clazz.getDeclaredFields()) {
			GrpcClient grpcClient = AnnotationUtils.findAnnotation(field, GrpcClient.class);
			if (ObjectUtils.isNotNull(grpcClient)) {
				ReflectionUtils.makeAccessible(field);
				ReflectionUtils.setField(field, bean, processInjectionPoint(field.getType(), grpcClient));
			}
		}
	}

	private <T> T processInjectionPoint(Class<T> injectionType, GrpcClient grpcClient) {
		String target = String.format("discovery://%s", grpcClient.serviceId());
		return grpcClientFactory.getClient(target, injectionType, null);
	}

}

/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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
package org.laokou.common.core.utils;

import io.micrometer.common.lang.NonNullApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

/**
 * @author laokou
 */
@Slf4j
@Component
@NonNullApi
public class SpringContextUtil implements ApplicationContextAware, DisposableBean {

	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringContextUtil.applicationContext = applicationContext;
	}

	public static DefaultListableBeanFactory getFactory() {
		return (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
	}

	public static Object getBean(String name) {
		return applicationContext.getBean(name);
	}

	public static <T> T getBean(Class<T> requiredType) {
		return applicationContext.getBean(requiredType);
	}

	public static <T> T getBean(String name, Class<T> requiredType) {
		return applicationContext.getBean(name, requiredType);
	}

	public static boolean containsBean(String name) {
		return applicationContext.containsBean(name);
	}

	public static boolean isSingleton(String name) {
		return applicationContext.isSingleton(name);
	}

	public static Class<?> getType(String name) {
		return Objects.requireNonNull(applicationContext.getType(name));
	}

	public static <T> Map<String, T> getType(Class<T> requiredType) {
		return applicationContext.getBeansOfType(requiredType);
	}

	public static <T> void registerBean(Class<T> clazz, String beanName) {
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
		getFactory().registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());
	}

	public static void removeBean(String beanName) {
		DefaultListableBeanFactory beanFactory = getFactory();
		if (beanFactory.containsBeanDefinition(beanName)) {
			beanFactory.removeBeanDefinition(beanName);
		}
	}

	@Override
	public void destroy() {
		applicationContext = null;
	}

	public static void publishEvent(ApplicationEvent event) {
		if (Objects.nonNull(applicationContext)) {
			log.info("发布Spring事件");
			applicationContext.publishEvent(event);
		}
	}

}

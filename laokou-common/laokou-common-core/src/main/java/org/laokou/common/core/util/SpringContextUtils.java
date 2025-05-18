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

import io.micrometer.common.lang.NonNullApi;
import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * spring上下文工具类.
 *
 * @author laokou
 */
@Component
@NonNullApi
@Order(Ordered.HIGHEST_PRECEDENCE)
public final class SpringContextUtils implements ApplicationContextAware, DisposableBean {

	public static final String APPLICATION_NAME = "spring.application.name";

	public static final String DEFAULT_SERVICE_ID = "application";

	@Getter
	private static volatile ApplicationContext applicationContext = null;

	private SpringContextUtils() {
	}

	/**
	 * 获取工厂.
	 * @return 工厂
	 */
	public static DefaultListableBeanFactory getFactory() {
		return (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
	}

	/**
	 * 根据名称获取Bean.
	 * @param name 名称
	 * @return Bean
	 */
	public static Object getBean(String name) {
		return applicationContext.getBean(name);
	}

	/**
	 * 根据名称判断Bean.
	 * @param name 名称
	 * @return 判断结果
	 */
	public static boolean containsBean(String name) {
		return applicationContext.containsBean(name);
	}

	/**
	 * 根据名称判断单例.
	 * @param name 名称
	 * @return 判断结果
	 */
	public static boolean isSingleton(String name) {
		return applicationContext.isSingleton(name);
	}

	/**
	 * 根据类型获取Bean.
	 * @param type 类型
	 * @param <T> 泛型
	 * @return Bean
	 */
	public static <T> T getBean(Class<T> type) {
		return applicationContext.getBean(type);
	}

	/**
	 * 根据名称和类型获取Bean.
	 * @param name 名称
	 * @param type 类型
	 * @param <T> 泛型
	 * @return Bean
	 */
	public static <T> T getBean(String name, Class<T> type) {
		return applicationContext.getBean(name, type);
	}

	/**
	 * 根据名称获取类.
	 * @param name 名称
	 * @return 类
	 */
	public static Class<?> getType(String name) {
		Class<?> clazz = applicationContext.getType(name);
		Assert.notNull(clazz, "Bean is null");
		return clazz;
	}

	/**
	 * 根据类型获取类【哈希表】.
	 * @param type 类型
	 * @param <T> 泛型
	 * @return 类
	 */
	public static <T> Map<String, T> getType(Class<T> type) {
		return applicationContext.getBeansOfType(type);
	}

	/**
	 * 注册Bean.
	 * @param clazz 类
	 * @param name 名称
	 * @param <T> 泛型
	 */
	public static <T> void registerBean(String name, Class<T> clazz) {
		DefaultListableBeanFactory beanFactory = getFactory();
		if (!beanFactory.containsBeanDefinition(name)) {
			beanFactory.registerBeanDefinition(name,
					BeanDefinitionBuilder.genericBeanDefinition(clazz).getBeanDefinition());
		}
	}

	/**
	 * 移除Bean.
	 * @param name 名称
	 */
	public static void removeBean(String name) {
		DefaultListableBeanFactory beanFactory = getFactory();
		if (beanFactory.containsBeanDefinition(name)) {
			beanFactory.removeBeanDefinition(name);
		}
	}

	public static <T> T getBeanAndNotExistToCreate(Class<T> type)
			throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
		try {
			return getBean(type);
		}
		catch (Exception e) {
			return ReflectionUtils.accessibleConstructor(type).newInstance();
		}
	}

	/**
	 * 推送事件.
	 * @param event 事件
	 */
	public static void publishEvent(ApplicationEvent event) {
		applicationContext.publishEvent(event);
	}

	public static String getServiceId() {
		return applicationContext.getEnvironment().getProperty(APPLICATION_NAME, DEFAULT_SERVICE_ID);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringContextUtils.applicationContext = applicationContext;
	}

	/**
	 * 销毁上下文.
	 */
	@Override
	public void destroy() {
		applicationContext = null;
	}

}

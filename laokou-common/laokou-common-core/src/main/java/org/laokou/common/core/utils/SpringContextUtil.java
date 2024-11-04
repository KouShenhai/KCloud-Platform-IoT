/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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
import lombok.Getter;
import org.laokou.common.i18n.utils.ObjectUtil;
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

import java.util.Map;

/**
 * spring上下文工具类.
 *
 * @author laokou
 */
@Component
@NonNullApi
@Order(Ordered.HIGHEST_PRECEDENCE)
public final class SpringContextUtil implements ApplicationContextAware, DisposableBean {

	public static final String APPLICATION_NAME = "spring.application.name";

	public static final String DEFAULT_SERVICE_ID = "application";

	@Getter
	private volatile static ApplicationContext applicationContext = null;

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
	 * @param requiredType 类型
	 * @param <T> 泛型
	 * @return Bean
	 */
	public static <T> T getBean(Class<T> requiredType) {
		return applicationContext.getBean(requiredType);
	}

	/**
	 * 根据名称和类型获取Bean.
	 * @param name 名称
	 * @param requiredType 类型
	 * @param <T> 泛型
	 * @return Bean
	 */
	public static <T> T getBean(String name, Class<T> requiredType) {
		return applicationContext.getBean(name, requiredType);
	}

	/**
	 * 根据名称获取类.
	 * @param name 名称
	 * @return 类
	 */
	public static Class<?> getType(String name) {
		return ObjectUtil.requireNotNull(applicationContext.getType(name));
	}

	/**
	 * 根据类型获取类.
	 * @param requiredType 类型
	 * @param <T> 泛型
	 * @return 类
	 */
	public static <T> Map<String, T> getType(Class<T> requiredType) {
		return applicationContext.getBeansOfType(requiredType);
	}

	/**
	 * 注册Bean.
	 * @param clazz 类
	 * @param beanName 名称
	 * @param <T> 泛型
	 */
	public static <T> void registerBean(Class<T> clazz, String beanName) {
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
		getFactory().registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());
	}

	/**
	 * 注销Bean.
	 * @param beanName 名称
	 */
	public static void removeBean(String beanName) {
		DefaultListableBeanFactory beanFactory = getFactory();
		if (beanFactory.containsBeanDefinition(beanName)) {
			beanFactory.removeBeanDefinition(beanName);
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
		SpringContextUtil.applicationContext = applicationContext;
	}

	/**
	 * 销毁上下文.
	 */
	@Override
	public void destroy() {
		applicationContext = null;
	}

}

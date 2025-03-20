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

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.rocketmq.spring.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Slf4j
@Configuration
public class RocketMQTransactionConfiguration implements ApplicationContextAware, SmartInitializingSingleton {

	private ConfigurableApplicationContext applicationContext;

	private volatile ExecutorService virtualThreadExecutor;

	@Override
	public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = (ConfigurableApplicationContext) applicationContext;
		virtualThreadExecutor = this.applicationContext.getBean(ExecutorService.class);
	}

	@Override
	public void afterSingletonsInstantiated() {
		Map<String, Object> beans = this.applicationContext.getBeansWithAnnotation(RocketMQTransactionListener.class)
			.entrySet()
			.stream()
			.filter(entry -> !ScopedProxyUtils.isScopedTarget(entry.getKey()))
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		beans.forEach(this::registerTransactionListener);
	}

	private void registerTransactionListener(String beanName, Object bean) {
		Class<?> clazz = AopProxyUtils.ultimateTargetClass(bean);

		if (!RocketMQLocalTransactionListener.class.isAssignableFrom(bean.getClass())) {
			throw new IllegalStateException(
					clazz + " is not instance of " + RocketMQLocalTransactionListener.class.getName());
		}
		RocketMQTransactionListener annotation = clazz.getAnnotation(RocketMQTransactionListener.class);
		RocketMQTemplate rocketMQTemplate = (RocketMQTemplate) applicationContext
			.getBean(annotation.rocketMQTemplateBeanName());
		TransactionMQProducer producer = (TransactionMQProducer) rocketMQTemplate.getProducer();
		if (producer.getTransactionListener() != null) {
			throw new IllegalStateException(
					annotation.rocketMQTemplateBeanName() + " already exists RocketMQLocalTransactionListener");
		}
		// 虚拟线程
		producer.setExecutorService(virtualThreadExecutor);
		producer.setCallbackExecutor(virtualThreadExecutor);
		producer.setAsyncSenderExecutor(virtualThreadExecutor);
		producer.setTransactionListener(RocketMQUtil.convert((RocketMQLocalTransactionListener) bean));
		log.debug("RocketMQLocalTransactionListener {} register to {} success", clazz.getName(),
				annotation.rocketMQTemplateBeanName());
	}

}

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

package org.laokou.common.core.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import java.util.concurrent.Executor;

/**
 * 异步配置.
 *
 * @author laokou
 */
@Slf4j
public class SpringTaskExecutorConfig {

	/**
	 * 线程池名称.
	 */
	public static final String THREAD_POOL_TASK_EXECUTOR_NAME = "ttl-task-executor";

	@Bean(value = THREAD_POOL_TASK_EXECUTOR_NAME, bootstrap = Bean.Bootstrap.BACKGROUND)
	public Executor executor(SpringTaskExecutionProperties properties) {
		log.info("{} => Initializing Executor", Thread.currentThread().getName());
		return new SpringTaskExecutorBuilder().withPool(properties.getPool())
			.disableDaemon()
			.withPrefix("ttl-task-")
			.build(true);
	}

	@Bean
	public Executor bootstrapExecutor(SpringTaskExecutionProperties properties) {
		log.info("{} => Initializing BootstrapExecutor", Thread.currentThread().getName());
		return new SpringTaskExecutorBuilder().withPool(properties.getPool())
			.enableDaemon()
			.withPriority(5)
			.withPrefix("bootstrap-task-")
			.build(false);
	}

}

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

import com.alibaba.ttl.threadpool.TtlExecutors;
import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.core.task.VirtualThreadTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.laokou.common.i18n.common.constant.StringConstant.TRUE;

/**
 * 异步配置.
 *
 * @author laokou
 */
@EnableAsync
@AutoConfiguration
@RequiredArgsConstructor
public class TaskExecutorAutoConfig {

	/**
	 * 线程池名称.
	 */
	public static final String THREAD_POOL_TASK_EXECUTOR_NAME = "executor";

	/**
	 * 虚拟线程开关.
	 */
	private static final String THREADS_VIRTUAL_ENABLED = "spring.threads.virtual.enabled";

	@Bean
	public Executor workStealingPoolExecutor(SpringTaskExecutionProperties springTaskExecutionProperties) {
		return Executors.newWorkStealingPool(springTaskExecutionProperties.getForkJoinPool().getCoreSize());
	}

	@Bean(value = THREAD_POOL_TASK_EXECUTOR_NAME)
	public Executor executor(SpringTaskExecutionProperties springTaskExecutionProperties, Environment environment) {
		String threadNamePrefix = springTaskExecutionProperties.getThreadNamePrefix();
		String enabled = environment.getProperty(THREADS_VIRTUAL_ENABLED);
		if (ObjectUtil.equals(TRUE, enabled)) {
			// 虚拟线程
			return new VirtualThreadTaskExecutor(threadNamePrefix);
		}
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		// 核心池大小
		executor.setCorePoolSize(springTaskExecutionProperties.getPool().getCoreSize());
		// 最大线程数
		executor.setMaxPoolSize(springTaskExecutionProperties.getPool().getMaxSize());
		// 队列容量
		executor.setQueueCapacity(springTaskExecutionProperties.getPool().getQueueCapacity());
		executor.setThreadPriority(Thread.MAX_PRIORITY);
		executor.setDaemon(false);
		executor.setAllowCoreThreadTimeOut(springTaskExecutionProperties.getPool().isAllowCoreThreadTimeout());
		// 线程空闲时间
		executor.setKeepAliveSeconds((int) springTaskExecutionProperties.getPool().getKeepAlive().toSeconds());
		// 线程名字前缀
		executor.setThreadNamePrefix(threadNamePrefix);
		// 初始化
		executor.initialize();
		return TtlExecutors.getTtlExecutorService(executor.getThreadPoolExecutor());
	}

}

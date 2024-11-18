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
import org.laokou.common.core.utils.ConvertUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.Assert;

import java.util.concurrent.Executor;

/**
 * 异步配置.
 *
 * @author laokou
 */
public class TaskExecutorConfig {

	/**
	 * 线程池名称.
	 */
	public static final String THREAD_POOL_TASK_EXECUTOR_NAME = "executor";

	@Bean(value = THREAD_POOL_TASK_EXECUTOR_NAME)
	public Executor executor(SpringTaskExecutionProperties springTaskExecutionProperties) {
		return getExecutor(springTaskExecutionProperties, true);
	}

	@Bean
	public Executor bootstrapExecutor(SpringTaskExecutionProperties springTaskExecutionProperties) {
		SpringTaskExecutionProperties properties = ConvertUtil.sourceToTarget(springTaskExecutionProperties,
				SpringTaskExecutionProperties.class);
		Assert.notNull(properties, "properties must not be null");
		SpringTaskExecutionProperties.Pool pool = properties.getPool();
		// 开启守护线程，用于后台bean初始化
		pool.setDaemon(true);
		// 优先级
		pool.setThreadPriority(5);
		// 线程名称前缀
		pool.setThreadNamePrefix("bootstrap-task-");
		return getExecutor(properties, false);
	}

	private Executor getExecutor(SpringTaskExecutionProperties springTaskExecutionProperties, boolean isTTL) {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		SpringTaskExecutionProperties.Pool pool = springTaskExecutionProperties.getPool();
		// 核心池大小
		executor.setCorePoolSize(pool.getCorePoolSize());
		// 最大线程数
		executor.setMaxPoolSize(pool.getMaxPoolSize());
		// 队列容量
		executor.setQueueCapacity(pool.getQueueCapacity());
		// 线程池优先级
		executor.setThreadPriority(pool.getThreadPriority());
		// 开启守护线程[不影响程序退出] => 数据库操作关闭守护线程
		executor.setDaemon(pool.isDaemon());
		// 允许核心线程在空闲时超时并被销毁[任务始终有一定负载或者你需要一直保持线程池中的线程活动，则禁用该选项会更合适]
		executor.setAllowCoreThreadTimeOut(pool.isAllowCoreThreadTimeout());
		// 线程空闲时间
		executor.setKeepAliveSeconds((int) pool.getKeepAlive().toSeconds());
		// 线程名称前缀
		executor.setThreadNamePrefix(pool.getThreadNamePrefix());
		// 初始化线程池
		executor.initialize();
		// TTL
		if (isTTL) {
			return TtlExecutors.getTtlExecutorService(executor.getThreadPoolExecutor());
		}
		return executor;
	}

}

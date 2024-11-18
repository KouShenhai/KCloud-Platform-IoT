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
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @author laokou
 */
public class SpringTaskExecutorBuilder {

	private static final ThreadPoolTaskExecutor EXECUTOR = new ThreadPoolTaskExecutor();

	public SpringTaskExecutorBuilder withPool(SpringTaskExecutionProperties.Pool pool) {
		// 核心池大小
		EXECUTOR.setCorePoolSize(pool.getCorePoolSize());
		// 最大线程数
		EXECUTOR.setMaxPoolSize(pool.getMaxPoolSize());
		// 队列容量
		EXECUTOR.setQueueCapacity(pool.getQueueCapacity());
		// 允许核心线程在空闲时超时并被销毁[任务始终有一定负载或者你需要一直保持线程池中的线程活动，则禁用该选项会更合适]
		EXECUTOR.setAllowCoreThreadTimeOut(pool.isAllowCoreThreadTimeout());
		// 线程空闲时间
		EXECUTOR.setKeepAliveSeconds((int) pool.getKeepAlive().toSeconds());
		// 线程池优先级
		EXECUTOR.setThreadPriority(pool.getThreadPriority());
		return this;
	}

	public SpringTaskExecutorBuilder disableDaemon() {
		EXECUTOR.setDaemon(false);
		return this;
	}

	public SpringTaskExecutorBuilder enableDaemon() {
		// 开启守护线程[不影响程序退出] => 数据库操作关闭守护线程
		EXECUTOR.setDaemon(true);
		return this;
	}

	public SpringTaskExecutorBuilder withPriority(int priority) {
		EXECUTOR.setThreadPriority(priority);
		return this;
	}

	public SpringTaskExecutorBuilder withPrefix(String prefix) {
		// 线程名称前缀
		EXECUTOR.setThreadNamePrefix(prefix);
		return this;
	}

	public Executor build(boolean ttl) {
		// 初始化线程池
		EXECUTOR.initialize();
		return ttl ? TtlExecutors.getTtlExecutorService(EXECUTOR.getThreadPoolExecutor()) : EXECUTOR;
	}

}

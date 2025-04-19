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

package org.laokou.common.core.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.time.Duration;
import java.util.concurrent.Executor;

/**
 * 异步配置.
 *
 * @author laokou
 */
@Slf4j
@Configuration
public class SpringTaskExecutorConfig {

	@Bean
	public Executor bootstrapExecutor() {
		log.info("{} => Initializing BootstrapExecutor", Thread.currentThread().getName());
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		// 核心池大小
		executor.setCorePoolSize(32);
		// 最大线程数
		executor.setMaxPoolSize(64);
		// 队列容量
		executor.setQueueCapacity(500);
		// 允许核心线程在空闲时超时并被销毁[任务始终有一定负载或者你需要一直保持线程池中的线程活动，则禁用该选项会更合适]
		executor.setAllowCoreThreadTimeOut(false);
		// 线程空闲时间
		executor.setKeepAliveSeconds((int) Duration.ofSeconds(60L).toSeconds());
		// 线程池优先级
		executor.setThreadPriority(5);
		// 线程池前缀名词
		executor.setThreadNamePrefix("bootstrap-task-");
		// 开启守护线程[不影响程序退出]
		// 注意：数据库操作关闭守护线程
		executor.setDaemon(true);
		// 初始化线程池
		executor.initialize();
		return executor;
	}

}

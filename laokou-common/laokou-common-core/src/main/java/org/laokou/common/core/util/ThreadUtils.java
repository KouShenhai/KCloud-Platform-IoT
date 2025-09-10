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

import com.alibaba.ttl.threadpool.TtlExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.util.ObjectUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 线程池工具类.
 *
 * @author laokou
 */
@Slf4j
public final class ThreadUtils {

	private ThreadUtils() {
	}

	/**
	 * 关闭线程池.
	 * @param executorService 执行器.
	 * @param timeout 超时时间
	 */
	public static void shutdown(ExecutorService executorService, int timeout) {
		log.info("开始关闭线程池");
		if (ObjectUtils.isNotNull(executorService) && !executorService.isShutdown()) {
			executorService.shutdown();
			try {
				if (!executorService.awaitTermination(timeout, TimeUnit.SECONDS)) {
					executorService.shutdownNow();
				}
			}
			catch (InterruptedException e) {
				executorService.shutdownNow();
				Thread.currentThread().interrupt();
			}
			finally {
				log.info("关闭线程池结束");
			}
		}
	}

	/**
	 * 新建一个虚拟线程池.
	 */
	public static ExecutorService newVirtualTaskExecutor() {
		return Executors.newThreadPerTaskExecutor(VirtualThreadFactory.INSTANCE);
	}

	public static ExecutorService newTtlVirtualTaskExecutor() {
		return TtlExecutors.getTtlExecutorService(newVirtualTaskExecutor());
	}

	public static ScheduledExecutorService newScheduledThreadPool(int coreSize) {
		return Executors.newScheduledThreadPool(coreSize,
				new ThreadFactoryBuilder().setNameFormat("laokou-scheduled-").build());
	}

}

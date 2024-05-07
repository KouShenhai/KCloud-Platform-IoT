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

package org.laokou.admin;

import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.utils.ObjectUtil;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author laokou
 */
@Slf4j
public class ThreadTest {

	public static void main(String[] args) {
		AtomicInteger atomic = new AtomicInteger(0);
		int len = 10;
		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 2, 60, TimeUnit.SECONDS,
				new LinkedBlockingQueue<>());
		for (int i = 0; i < len; i++) {
			CompletableFuture.runAsync(() -> {
				int val = atomic.incrementAndGet();
				log.info("子线程运行完毕，" + Thread.currentThread().getName() + "，val = " + val);
			}, threadPoolExecutor);
		}
		log.info("主线程运行完毕");
		log.info("执行关闭线程池");
		shutdown(threadPoolExecutor);
	}

	private static void shutdown(ExecutorService executorService) {
		if (ObjectUtil.isNotNull(executorService) && !executorService.isShutdown()) {
			executorService.shutdown();
			try {
				if (executorService.awaitTermination(60, TimeUnit.SECONDS)) {
					executorService.shutdownNow();
				}
			}
			catch (InterruptedException e) {
				executorService.shutdownNow();
				Thread.currentThread().interrupt();
			}
		}
	}

}

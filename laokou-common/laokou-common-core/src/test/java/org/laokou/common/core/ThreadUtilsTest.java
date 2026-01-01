/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.common.core;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.core.util.ThreadUtils;

import java.util.concurrent.ExecutorService;

/**
 * @author laokou
 */
class ThreadUtilsTest {

	@Test
	void test_shutdownThreadExecutor() {
		ExecutorService virtualTaskExecutor = ThreadUtils.newVirtualTaskExecutor();
		ExecutorService ttlVirtualTaskExecutor = ThreadUtils.newTtlVirtualTaskExecutor();
		ThreadUtils.shutdown(virtualTaskExecutor, 1);
		ThreadUtils.shutdown(ttlVirtualTaskExecutor, 1);
		Assertions.assertThat(virtualTaskExecutor.isShutdown()).isTrue();
		Assertions.assertThat(ttlVirtualTaskExecutor.isShutdown()).isTrue();
	}

	@Test
	void test_newVirtualTaskExecutor() {
		try (ExecutorService executorService = ThreadUtils.newVirtualTaskExecutor()) {
			executorService.execute(() -> Assertions.assertThat(executorService).isNotNull());
		}
	}

	@Test
	void test_newTtlVirtualTaskExecutor() {
		try (ExecutorService executorService = ThreadUtils.newTtlVirtualTaskExecutor()) {
			executorService.execute(() -> Assertions.assertThat(executorService).isNotNull());
		}
	}

	@Test
	void test_newScheduledThreadPool() {
		try (ExecutorService executorService = ThreadUtils.newScheduledThreadPool(1)) {
			executorService.execute(() -> Assertions.assertThat(executorService).isNotNull());
		}
	}

}

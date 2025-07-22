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

package org.laokou.common.core;

import org.junit.jupiter.api.Test;
import org.laokou.common.core.util.ThreadUtils;

import java.util.concurrent.ExecutorService;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author laokou
 */
class ThreadUtilsTest {

	@Test
	void testShutdownThreadExecutor() {
		ExecutorService virtualTaskExecutor = ThreadUtils.newVirtualTaskExecutor();
		ExecutorService ttlVirtualTaskExecutor = ThreadUtils.newTtlVirtualTaskExecutor();
		ThreadUtils.shutdown(virtualTaskExecutor, 1);
		ThreadUtils.shutdown(ttlVirtualTaskExecutor, 1);
		assertThat(virtualTaskExecutor.isShutdown()).isTrue();
		assertThat(ttlVirtualTaskExecutor.isShutdown()).isTrue();
	}

	@Test
	void testNewVirtualTaskExecutor() {
		ExecutorService executorService = ThreadUtils.newVirtualTaskExecutor();
		executorService.execute(() -> assertThat(executorService).isNotNull());
	}

	@Test
	void testNewTtlVirtualTaskExecutor() {
		ExecutorService executorService = ThreadUtils.newTtlVirtualTaskExecutor();
		executorService.execute(() -> assertThat(executorService).isNotNull());
	}

	@Test
	void testNewScheduledThreadPool() {
		try (ExecutorService executorService = ThreadUtils.newScheduledThreadPool(1)) {
			executorService.execute(() -> assertThat(executorService).isNotNull());
		}
	}

}

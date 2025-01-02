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

package org.laokou.common.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.core.utils.ThreadUtil;

import java.util.concurrent.ExecutorService;

/**
 * @author laokou
 */
class ThreadUtilTest {

	@Test
	void testShutdownThreadExecutor() {
		ExecutorService virtualTaskExecutor = ThreadUtil.newVirtualTaskExecutor();
		ExecutorService ttlVirtualTaskExecutor = ThreadUtil.newTtlVirtualTaskExecutor();
		ThreadUtil.shutdown(virtualTaskExecutor, 1);
		ThreadUtil.shutdown(ttlVirtualTaskExecutor, 1);
		Assertions.assertTrue(virtualTaskExecutor.isShutdown());
		Assertions.assertTrue(ttlVirtualTaskExecutor.isShutdown());
	}

	@Test
	void testNewVirtualTaskExecutor() {
		try (ExecutorService executorService = ThreadUtil.newVirtualTaskExecutor()) {
			executorService.execute(() -> Assertions.assertNotNull(executorService));
		}
	}

	@Test
	void testNewTtlVirtualTaskExecutor() {
		try (ExecutorService executorService = ThreadUtil.newTtlVirtualTaskExecutor()) {
			executorService.execute(() -> Assertions.assertNotNull(executorService));
		}
	}

}

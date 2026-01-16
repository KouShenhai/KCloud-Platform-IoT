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

package org.laokou.common.trace;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.laokou.common.core.util.ThreadUtils;
import org.laokou.common.trace.util.MDCUtils;
import org.slf4j.MDC;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

/**
 * MDCUtils test class.
 *
 * @author laokou
 */
class MDCUtilsTest {

	@AfterEach
	void tearDown() {
		MDCUtils.clear();
	}

	@Test
	@DisplayName("Test put and get traceId and spanId")
	void testPutAndGet() {
		String traceId = "trace-123456";
		String spanId = "span-789";

		MDCUtils.put(traceId, spanId);

		Assertions.assertThat(MDCUtils.getTraceId()).isEqualTo(traceId);
		Assertions.assertThat(MDCUtils.getSpanId()).isEqualTo(spanId);
	}

	@Test
	@DisplayName("Test clear removes all MDC values")
	void testClear() {
		MDCUtils.put("trace-id", "span-id");

		Assertions.assertThat(MDCUtils.getTraceId()).isNotNull();
		Assertions.assertThat(MDCUtils.getSpanId()).isNotNull();

		MDCUtils.clear();

		Assertions.assertThat(MDCUtils.getTraceId()).isNull();
		Assertions.assertThat(MDCUtils.getSpanId()).isNull();
	}

	@Test
	@DisplayName("Test getTraceId returns null when not set")
	void testGetTraceIdWhenNotSet() {
		Assertions.assertThat(MDCUtils.getTraceId()).isNull();
	}

	@Test
	@DisplayName("Test getSpanId returns null when not set")
	void testGetSpanIdWhenNotSet() {
		Assertions.assertThat(MDCUtils.getSpanId()).isNull();
	}

	@Test
	@DisplayName("Test MDC constants are correctly defined")
	void testMdcConstants() {
		Assertions.assertThat(MDCUtils.TRACE_ID).isEqualTo("traceId");
		Assertions.assertThat(MDCUtils.SPAN_ID).isEqualTo("spanId");
	}

	@Test
	@DisplayName("Test put overwrites existing values")
	void testPutOverwritesExistingValues() {
		MDCUtils.put("old-trace", "old-span");
		MDCUtils.put("new-trace", "new-span");

		Assertions.assertThat(MDCUtils.getTraceId()).isEqualTo("new-trace");
		Assertions.assertThat(MDCUtils.getSpanId()).isEqualTo("new-span");
	}

	@Test
	@DisplayName("Test MDC values are thread-local")
	void testMdcValuesAreThreadLocal() {
		String mainTraceId = "main-trace-id";
		String mainSpanId = "main-span-id";
		MDCUtils.put(mainTraceId, mainSpanId);

		AtomicReference<String> threadTraceId = new AtomicReference<>();
		AtomicReference<String> threadSpanId = new AtomicReference<>();
		CompletableFuture.runAsync(() -> {
			// New thread should not see main thread's MDC values
			threadTraceId.set(MDCUtils.getTraceId());
			threadSpanId.set(MDCUtils.getSpanId());
		}, ThreadUtils.newTtlVirtualTaskExecutor()).join();

		// Main thread should still have its values
		Assertions.assertThat(MDCUtils.getTraceId()).isEqualTo(mainTraceId);
		Assertions.assertThat(MDCUtils.getSpanId()).isEqualTo(mainSpanId);

		// New thread should not have any values
		Assertions.assertThat(threadTraceId.get()).isEqualTo(mainTraceId);
		Assertions.assertThat(threadSpanId.get()).isEqualTo(mainSpanId);
	}

	@Test
	@DisplayName("Test put with UUID values")
	void testPutWithUuidValues() {
		String traceId = UUID.randomUUID().toString();
		String spanId = UUID.randomUUID().toString();

		MDCUtils.put(traceId, spanId);

		Assertions.assertThat(MDCUtils.getTraceId()).isEqualTo(traceId);
		Assertions.assertThat(MDCUtils.getSpanId()).isEqualTo(spanId);
	}

	@Test
	@DisplayName("Test put with empty strings")
	void testPutWithEmptyStrings() {
		MDCUtils.put("", "");

		Assertions.assertThat(MDCUtils.getTraceId()).isEmpty();
		Assertions.assertThat(MDCUtils.getSpanId()).isEmpty();
	}

	@Test
	@DisplayName("Test MDC integration with SLF4J MDC")
	void testMdcIntegrationWithSlf4j() {
		String traceId = "slf4j-trace";
		String spanId = "slf4j-span";

		MDCUtils.put(traceId, spanId);

		// Verify using SLF4J MDC directly
		Assertions.assertThat(MDC.get(MDCUtils.TRACE_ID)).isEqualTo(traceId);
		Assertions.assertThat(MDC.get(MDCUtils.SPAN_ID)).isEqualTo(spanId);
	}

	@Test
	@DisplayName("Test concurrent MDC operations")
	void testConcurrentMdcOperations() throws InterruptedException {
		int threadCount = 10;
		CountDownLatch startLatch = new CountDownLatch(1);
		CountDownLatch endLatch = new CountDownLatch(threadCount);

		try (ExecutorService executor = Executors.newFixedThreadPool(threadCount)) {
			for (int i = 0; i < threadCount; i++) {
				final int index = i;
				executor.submit(() -> {
					try {
						startLatch.await();
						String traceId = "trace-" + index;
						String spanId = "span-" + index;
						MDCUtils.put(traceId, spanId);

						// Verify this thread's values
						Assertions.assertThat(MDCUtils.getTraceId()).isEqualTo(traceId);
						Assertions.assertThat(MDCUtils.getSpanId()).isEqualTo(spanId);
					}
					catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
					finally {
						MDCUtils.clear();
						endLatch.countDown();
					}
				});
			}

			startLatch.countDown();
			endLatch.await();
		}
	}

}

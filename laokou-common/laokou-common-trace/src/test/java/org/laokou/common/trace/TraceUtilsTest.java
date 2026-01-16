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

import io.prometheus.metrics.tracer.common.SpanContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.laokou.common.trace.util.MDCUtils;
import org.laokou.common.trace.util.TraceUtils;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * TraceUtils test class.
 *
 * @author laokou
 */
class TraceUtilsTest {

	@Mock
	private SpanContext spanContext;

	private TraceUtils traceUtils;

	private AutoCloseable mocks;

	@BeforeEach
	void setUp() {
		mocks = MockitoAnnotations.openMocks(this);
		traceUtils = new TraceUtils(spanContext);
	}

	@AfterEach
	void tearDown() throws Exception {
		MDCUtils.clear();
		if (mocks != null) {
			mocks.close();
		}
	}

	@Test
	@DisplayName("Test getTraceId returns value from SpanContext")
	void testGetTraceIdFromSpanContext() {
		String expectedTraceId = "span-context-trace-id";
		Mockito.when(spanContext.getCurrentTraceId()).thenReturn(expectedTraceId);

		String traceId = traceUtils.getTraceId();

		Assertions.assertThat(traceId).isEqualTo(expectedTraceId);
	}

	@Test
	@DisplayName("Test getSpanId returns value from SpanContext")
	void testGetSpanIdFromSpanContext() {
		String expectedSpanId = "span-context-span-id";
		Mockito.when(spanContext.getCurrentSpanId()).thenReturn(expectedSpanId);

		String spanId = traceUtils.getSpanId();

		Assertions.assertThat(spanId).isEqualTo(expectedSpanId);
	}

	@Test
	@DisplayName("Test getTraceId falls back to MDC when SpanContext throws exception")
	void testGetTraceIdFallbackToMdc() {
		String mdcTraceId = "mdc-trace-id";
		MDCUtils.put(mdcTraceId, "span-id");
		Mockito.when(spanContext.getCurrentTraceId()).thenThrow(new RuntimeException("SpanContext error"));

		String traceId = traceUtils.getTraceId();

		Assertions.assertThat(traceId).isEqualTo(mdcTraceId);
	}

	@Test
	@DisplayName("Test getSpanId falls back to MDC when SpanContext throws exception")
	void testGetSpanIdFallbackToMdc() {
		String mdcSpanId = "mdc-span-id";
		MDCUtils.put("trace-id", mdcSpanId);
		Mockito.when(spanContext.getCurrentSpanId()).thenThrow(new RuntimeException("SpanContext error"));

		String spanId = traceUtils.getSpanId();

		Assertions.assertThat(spanId).isEqualTo(mdcSpanId);
	}

	@Test
	@DisplayName("Test getTraceId returns null when both SpanContext and MDC are empty")
	void testGetTraceIdReturnsNullWhenBothEmpty() {
		Mockito.when(spanContext.getCurrentTraceId()).thenThrow(new RuntimeException("SpanContext error"));

		String traceId = traceUtils.getTraceId();

		Assertions.assertThat(traceId).isNull();
	}

	@Test
	@DisplayName("Test getSpanId returns null when both SpanContext and MDC are empty")
	void testGetSpanIdReturnsNullWhenBothEmpty() {
		Mockito.when(spanContext.getCurrentSpanId()).thenThrow(new RuntimeException("SpanContext error"));

		String spanId = traceUtils.getSpanId();

		Assertions.assertThat(spanId).isNull();
	}

	@Test
	@DisplayName("Test getTraceId handles null from SpanContext")
	void testGetTraceIdHandlesNullFromSpanContext() {
		Mockito.when(spanContext.getCurrentTraceId()).thenReturn(null);

		String traceId = traceUtils.getTraceId();

		Assertions.assertThat(traceId).isNull();
	}

	@Test
	@DisplayName("Test getSpanId handles null from SpanContext")
	void testGetSpanIdHandlesNullFromSpanContext() {
		Mockito.when(spanContext.getCurrentSpanId()).thenReturn(null);

		String spanId = traceUtils.getSpanId();

		Assertions.assertThat(spanId).isNull();
	}

	@Test
	@DisplayName("Test TraceUtils with valid SpanContext values")
	void testTraceUtilsWithValidSpanContextValues() {
		String expectedTraceId = "abc123def456";
		String expectedSpanId = "span789xyz";
		Mockito.when(spanContext.getCurrentTraceId()).thenReturn(expectedTraceId);
		Mockito.when(spanContext.getCurrentSpanId()).thenReturn(expectedSpanId);

		Assertions.assertThat(traceUtils.getTraceId()).isEqualTo(expectedTraceId);
		Assertions.assertThat(traceUtils.getSpanId()).isEqualTo(expectedSpanId);
	}

}

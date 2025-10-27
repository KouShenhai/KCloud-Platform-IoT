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

/*
 * Copyright 2012-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.actuate.autoconfigure.tracing.prometheus;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import io.prometheus.metrics.tracer.common.SpanContext;
import org.laokou.common.core.util.SpringContextUtils;
import org.laokou.common.i18n.util.ObjectUtils;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.micrometer.metrics.autoconfigure.export.prometheus.PrometheusMetricsExportAutoConfiguration;
import org.springframework.boot.micrometer.tracing.autoconfigure.MicrometerTracingAutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Prometheus Exemplars with
 * Micrometer Tracing.
 *
 * @author Jonatan Ivanov
 * @author laokou
 * @since 3.0.0
 */
@AutoConfiguration(before = PrometheusMetricsExportAutoConfiguration.class,
		after = MicrometerTracingAutoConfiguration.class)
@ConditionalOnBean(Tracer.class)
@ConditionalOnClass({ Tracer.class, SpanContext.class })
public class PrometheusExemplarsAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	SpanContext spanContext() {
		return new TracingSpanContext();
	}

	/**
	 * Since the MeterRegistry can depend on the {@link Tracer} (Exemplars) and the
	 * {@link Tracer} can depend on the MeterRegistry (recording metrics), this
	 * {@link SpanContext} breaks the cycle by lazily loading the {@link Tracer}.
	 */
	static class TracingSpanContext implements SpanContext {

		private volatile Span currentSpan;

		private static final Object LOCK = new Object();

		@Override
		public String getCurrentTraceId() {
			Span currentSpan = currentSpan();
			return ObjectUtils.isNotNull(currentSpan) ? currentSpan.context().traceId() : null;
		}

		@Override
		public String getCurrentSpanId() {
			Span currentSpan = currentSpan();
			return ObjectUtils.isNotNull(currentSpan) ? currentSpan.context().spanId() : null;
		}

		@Override
		public boolean isCurrentSpanSampled() {
			Span currentSpan = currentSpan();
			if (ObjectUtils.isNull(currentSpan)) {
				return false;
			}
			return Boolean.TRUE.equals(currentSpan.context().sampled());
		}

		@Override
		public void markCurrentSpanAsExemplar() {
		}

		private Span currentSpan() {
			try {
				// 双检锁
				if (ObjectUtils.isNull(currentSpan)) {
					synchronized (LOCK) {
						if (ObjectUtils.isNull(currentSpan)) {
							currentSpan = SpringContextUtils.getBean(Tracer.class).currentSpan();
						}
					}
				}
				return currentSpan;
			}
			catch (Exception e) {
				return null;
			}
		}

	}

}

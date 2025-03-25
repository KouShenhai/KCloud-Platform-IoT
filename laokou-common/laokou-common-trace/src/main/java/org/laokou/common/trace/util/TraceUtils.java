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

package org.laokou.common.trace.util;

import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.laokou.common.core.util.MDCUtils;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class TraceUtils {

	private final Tracer tracer;

	public String getTraceId() {
		try {
			TraceContext context = getContext();
			return context.traceId();
		}
		catch (Exception e) {
			return MDCUtils.getTraceId();
		}
	}

	public String getSpanId() {
		try {
			TraceContext context = getContext();
			return context.spanId();
		}
		catch (Exception e) {
			return MDCUtils.getSpanId();
		}
	}

	private TraceContext getContext() {
		return tracer.currentTraceContext().context();
	}

}

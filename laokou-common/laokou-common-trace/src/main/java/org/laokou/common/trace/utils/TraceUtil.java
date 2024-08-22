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

package org.laokou.common.trace.utils;

import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.springframework.stereotype.Component;

import static org.laokou.common.i18n.common.constant.StringConstant.EMPTY;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class TraceUtil {

	private final Tracer tracer;

	public String getTraceId() {
		TraceContext context = getContext();
		if (ObjectUtil.isNull(context)) {
			return EMPTY;
		}
		return context.traceId();
	}

	public String getSpanId() {
		TraceContext context = getContext();
		if (ObjectUtil.isNull(context)) {
			return EMPTY;
		}
		return context.spanId();
	}

	private TraceContext getContext() {
		return tracer.currentTraceContext().context();
	}

}

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

package org.laokou.common.core.utils;

import org.slf4j.MDC;

/**
 * MDC工具类.
 *
 * @author laokou
 */
public final class MDCUtil {

	private static final String TRACE_ID = "traceId";

	private static final String SPAN_ID = "spanId";

	/**
	 * 写入MDC.
	 * @param traceId 链路ID
	 * @param spanId 标签ID
	 */
	public static void put(String traceId, String spanId) {
		MDC.put(TRACE_ID, traceId);
		MDC.put(SPAN_ID, spanId);
	}

	/**
	 * 获取链路ID.
	 */
	public static String getTraceId() {
		return MDC.get(TRACE_ID);
	}

	/**
	 * 获取标签ID.
	 */
	public static String getSpanId() {
		return MDC.get(SPAN_ID);
	}

	/**
	 * 清除MDC.
	 */
	public static void clear() {
		MDC.clear();
	}

}

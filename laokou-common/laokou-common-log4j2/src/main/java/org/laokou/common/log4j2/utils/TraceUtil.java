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

package org.laokou.common.log4j2.utils;

import org.apache.logging.log4j.ThreadContext;

import static org.laokou.common.i18n.common.constant.TraceConstant.*;

/**
 * @author laokou
 */
public class TraceUtil {

	public static void putContext(String traceId, String userId, String tenantId, String username, String spanId) {
		ThreadContext.put(TRACE_ID, traceId);
		ThreadContext.put(USER_ID, userId);
		ThreadContext.put(TENANT_ID, tenantId);
		ThreadContext.put(USER_NAME, username);
		ThreadContext.put(SPAN_ID, spanId);
	}

	public static void clearContext() {
		ThreadContext.clearMap();
	}

}

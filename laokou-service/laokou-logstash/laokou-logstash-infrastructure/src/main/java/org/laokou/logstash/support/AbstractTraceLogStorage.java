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

package org.laokou.logstash.support;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.common.constant.DateConstants;
import org.laokou.common.i18n.common.constant.StringConstants;
import org.laokou.common.i18n.util.InstantUtils;
import org.laokou.common.i18n.util.JacksonUtils;
import org.laokou.common.i18n.util.ObjectUtils;
import org.laokou.logstash.gatewayimpl.database.dataobject.TraceLogIndex;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractTraceLogStorage implements TraceLogStorage {

	protected static final String TRACE_INDEX = "trace_log";

	protected String getIndexName() {
		return TRACE_INDEX + StringConstants.UNDER + InstantUtils.format(InstantUtils.now(), DateConstants.YYYYMMDD);
	}

	protected TraceLogIndex getTraceLogIndex(Object obj) {
		if (obj instanceof byte[] buff) {
			TraceLogIndex traceLogIndex = JacksonUtils.toBean(buff, TraceLogIndex.class);
			if (!ObjectUtils.equals(traceLogIndex.getTraceId(), "%X{traceId}")
					&& !ObjectUtils.equals(traceLogIndex.getSpanId(), "%X{spanId}")) {
				return traceLogIndex;
			}
			return null;
		}
		return null;
	}

}

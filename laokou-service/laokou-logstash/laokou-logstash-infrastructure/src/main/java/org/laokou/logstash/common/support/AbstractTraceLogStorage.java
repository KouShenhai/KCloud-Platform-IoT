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

package org.laokou.logstash.common.support;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.common.constant.StringConstants;
import org.laokou.common.i18n.util.DateUtils;
import org.laokou.common.i18n.util.JacksonUtils;
import org.laokou.common.i18n.util.StringUtils;
import org.laokou.logstash.gatewayimpl.database.dataobject.TraceLogIndex;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractTraceLogStorage implements TraceLogStorage {

	protected static final String TRACE_INDEX = "laokou_trace";

	protected String getIndexName() {
		return TRACE_INDEX + StringConstants.UNDER + DateUtils.format(DateUtils.nowDate(), DateUtils.YYYYMMDD);
	}

	protected TraceLogIndex getTraceLogIndex(String str) {
		try {
			TraceLogIndex traceLogIndex = JacksonUtils.toBean(str, TraceLogIndex.class);
			String traceId = traceLogIndex.getTraceId();
			String spanId = traceLogIndex.getSpanId();
			if (StringUtils.isNotEmpty(spanId) && StringUtils.isNotEmpty(traceId)) {
				return traceLogIndex;
			}
		}
		catch (Exception ex) {
			log.error("分布式链路日志JSON转换失败，错误信息：{}", ex.getMessage());
		}
		return null;
	}

}

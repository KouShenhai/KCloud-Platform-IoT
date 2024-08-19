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

package org.laokou.logstash.handler;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.core.utils.MapUtil;
import org.laokou.common.elasticsearch.template.ElasticsearchTemplate;
import org.laokou.common.i18n.utils.DateUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.laokou.common.i18n.common.constant.StringConstant.DOLLAR;
import static org.laokou.common.i18n.common.constant.StringConstant.UNDER;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TraceHandler {

	private static final String UNDEFINED = "undefined";

	private static final String TRACE = "laokou_trace";

	private final ElasticsearchTemplate elasticsearchTemplate;

	@KafkaListener(topics = "laokou_trace_topic", groupId = "laokou_trace_consumer_group")
	public void kafkaConsumer(List<String> messages, Acknowledgment ack) {
		Map<String, Object> dataMap = messages
			.stream()
			.map(this::getTraceIndex)
			.toList()
			.stream()
			.collect(Collectors.toMap(TraceIndex::getId, traceIndex -> traceIndex));
		if (MapUtil.isNotEmpty(dataMap)) {
			elasticsearchTemplate.asyncCreateIndex(getIndexName(), TRACE, TraceIndex.class)
				.thenAcceptAsync(res -> elasticsearchTemplate.asyncBulkCreateDocument(getIndexName(), dataMap));
		}
		ack.acknowledge();
	}

	private TraceIndex getTraceIndex(String str) {
		TraceIndex traceIndex = JacksonUtil.toBean(str, TraceIndex.class);
		String traceId = traceIndex.getTraceId();
		String spanId = traceIndex.getSpanId();
		if (isTrace(traceId, spanId)) {
			traceIndex.setId(String.valueOf(IdGenerator.defaultSnowflakeId()));
			traceIndex.setTenantId(replaceValue(traceIndex.getTenantId()));
			traceIndex.setUserId(replaceValue(traceIndex.getUserId()));
			traceIndex.setUsername(replaceValue(traceIndex.getUsername()));
			return traceIndex;
		}
		return null;
	}

	private boolean isTrace(String traceId, String spanId) {
		return isTrace(traceId) && isTrace(spanId);
	}

	private boolean isTrace(String str) {
		return StringUtil.isNotEmpty(str)
			&& str.startsWith("${")
			&& str.endsWith("}");
	}

	private String getIndexName() {
		return TRACE + UNDER + DateUtil.format(DateUtil.nowDate(), DateUtil.YYYYMMDD);
	}

	private String replaceValue(String value) {
		if (value.startsWith(DOLLAR) || UNDEFINED.equals(value)) {
			return "";
		}
		return value;
	}

	@Data
	public final static class TraceIndex implements Serializable {

		private String id;

		private String appName;

		private String profile;

		private Instant dateTime;

		private String userId;

		private String username;

		private String tenantId;

		private String traceId;

		private String spanId;

		private String ip;

		private String level;

		private String thread;

		private String logger;

		private String msg;

	}

}

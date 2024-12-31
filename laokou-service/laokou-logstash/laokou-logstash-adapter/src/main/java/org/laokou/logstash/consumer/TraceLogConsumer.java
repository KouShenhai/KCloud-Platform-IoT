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

package org.laokou.logstash.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.UniqueIdGenerator;
import org.laokou.common.i18n.utils.JacksonUtil;
import org.laokou.common.core.utils.MapUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.logstash.common.support.TraceLogStorage;
import org.laokou.logstash.gateway.database.dataobject.TraceLogIndex;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TraceLogConsumer {

	private final TraceLogStorage traceLogStorage;

	@KafkaListener(topics = "laokou_trace_topic", groupId = "laokou_trace_consumer_group")
	public CompletableFuture<Void> kafkaConsumer(List<String> messages, Acknowledgment ack) {
		try {
			Map<String, Object> dataMap = messages.stream()
				.map(this::getTraceLogIndex)
				.filter(Objects::nonNull)
				.collect(Collectors.toMap(TraceLogIndex::getId, traceLogIndex -> traceLogIndex));
			if (MapUtil.isNotEmpty(dataMap)) {
				return traceLogStorage.batchSave(dataMap);
			}
		}
		catch (Exception e) {
			log.error("分布式链路写入失败，错误信息：{}", e.getMessage());
		}
		finally {
			ack.acknowledge();
		}
		return null;
	}

	private TraceLogIndex getTraceLogIndex(String str) {
		try {
			TraceLogIndex traceLogIndex = JacksonUtil.toBean(str, TraceLogIndex.class);
			String traceId = traceLogIndex.getTraceId();
			String spanId = traceLogIndex.getSpanId();
			if (isTraceLog(traceId, spanId)) {
				traceLogIndex.setId(UniqueIdGenerator.generateId());
				return traceLogIndex;
			}
		}
		catch (Exception ex) {
			log.error("分布式链路日志JSON转换失败，错误信息：{}", ex.getMessage());
		}
		return null;
	}

	private boolean isTraceLog(String traceId, String spanId) {
		return isTraceLog(traceId) && isTraceLog(spanId);
	}

	private boolean isTraceLog(String str) {
		return StringUtil.isNotEmpty(str) && !str.startsWith("${") && !str.endsWith("}");
	}

}

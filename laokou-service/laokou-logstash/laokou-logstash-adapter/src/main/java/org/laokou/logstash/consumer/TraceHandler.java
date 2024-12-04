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

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.utils.IdGenerator;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.core.utils.MapUtil;
import org.laokou.common.core.utils.ThreadUtil;
import org.laokou.common.elasticsearch.annotation.Field;
import org.laokou.common.elasticsearch.annotation.Index;
import org.laokou.common.elasticsearch.annotation.Setting;
import org.laokou.common.elasticsearch.annotation.Type;
import org.laokou.common.elasticsearch.template.ElasticsearchTemplate;
import org.laokou.common.i18n.common.constant.StringConstant;
import org.laokou.common.i18n.utils.DateUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TraceHandler {

	private static final String TRACE = "laokou_trace";

	private final ElasticsearchTemplate elasticsearchTemplate;

	@KafkaListener(topics = "laokou_trace_topic", groupId = "laokou_trace_consumer_group")
	public void kafkaConsumer(List<String> messages, Acknowledgment ack) {
		try (ExecutorService executor = ThreadUtil.newVirtualTaskExecutor()) {
			Map<String, Object> dataMap = messages.stream()
				.map(this::getTraceIndex)
				.filter(Objects::nonNull)
				.toList()
				.stream()
				.collect(Collectors.toMap(TraceIndex::getId, traceIndex -> traceIndex));
			if (MapUtil.isNotEmpty(dataMap)) {
				elasticsearchTemplate.asyncCreateIndex(getIndexName(), TRACE, TraceIndex.class, executor)
					.thenAcceptAsync(
							res -> elasticsearchTemplate.asyncBulkCreateDocument(getIndexName(), dataMap, executor),
							executor);
			}
		}
		catch (Throwable e) {
			log.error("分布式链路写入失败，错误信息：{}", e.getMessage(), e);
		}
		finally {
			ack.acknowledge();
		}
	}

	private TraceIndex getTraceIndex(String str) {
		try {
			TraceIndex traceIndex = JacksonUtil.toBean(str, TraceIndex.class);
			String traceId = traceIndex.getTraceId();
			String spanId = traceIndex.getSpanId();
			if (isTraceLog(traceId, spanId)) {
				traceIndex.setId(String.valueOf(IdGenerator.defaultSnowflakeId()));
				return traceIndex;
			}
		}
		catch (Exception ex) {
			log.error("分布式链路日志JSON转换失败，错误信息：{}", ex.getMessage(), ex);
		}
		return null;
	}

	private boolean isTraceLog(String traceId, String spanId) {
		return isTraceLog(traceId) && isTraceLog(spanId);
	}

	private boolean isTraceLog(String str) {
		return StringUtil.isNotEmpty(str) && !str.startsWith("${") && !str.endsWith("}");
	}

	private String getIndexName() {
		return TRACE + StringConstant.UNDER + DateUtil.format(DateUtil.nowDate(), DateUtil.YYYYMMDD);
	}

	@Data
	@Index(setting = @Setting(refreshInterval = "-1"))
	public final static class TraceIndex implements Serializable {

		@Field(type = Type.LONG)
		private String id;

		@Field(type = Type.KEYWORD)
		private String serviceId;

		@Field(type = Type.KEYWORD)
		private String profile;

		@Field(type = Type.DATE, format = DateUtil.Constant.YYYY_ROD_MM_ROD_DD_SPACE_HH_RISK_HH_RISK_SS_SSS)
		private String dateTime;

		@Field(type = Type.KEYWORD, index = true)
		private String traceId;

		@Field(type = Type.KEYWORD, index = true)
		private String spanId;

		@Field(type = Type.KEYWORD)
		private String address;

		@Field(type = Type.KEYWORD)
		private String level;

		@Field(type = Type.KEYWORD)
		private String threadName;

		@Field(type = Type.KEYWORD)
		private String packageName;

		@Field(type = Type.KEYWORD)
		private String message;

		@Field(type = Type.KEYWORD)
		private String stacktrace;

	}

}

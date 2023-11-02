/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.core.utils.RegexUtil;
import org.laokou.common.elasticsearch.template.ElasticsearchTemplate;
import org.laokou.common.i18n.utils.DateUtil;
import org.laokou.logstash.gatewayimpl.database.dataobject.TraceIndex;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static org.laokou.common.i18n.common.Constant.EMPTY;
import static org.laokou.common.i18n.common.Constant.UNDER;
import static org.laokou.common.kafka.constant.MqConstant.LAOKOU_LOGSTASH_CONSUMER_GROUP;
import static org.laokou.common.kafka.constant.MqConstant.LAOKOU_TRACE_TOPIC;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TraceConsumer {

	private final ElasticsearchTemplate elasticsearchTemplate;

	private static final String TRACE_INDEX = "laokou_trace";

	@KafkaListener(topics = LAOKOU_TRACE_TOPIC, groupId = LAOKOU_LOGSTASH_CONSUMER_GROUP)
	public void kafkaConsumerTest(List<String> messages, Acknowledgment ack) {
		messages.parallelStream().forEach(this::saveIndex);
		ack.acknowledge();
	}

	/**
	 * 每个月最后一天的23：50：00创建下一个月的索引
	 */
	@Scheduled(cron = "0 50 23 L * ?")
	public void scheduleCreateIndex() {
		createIndex(DateUtil.plusDays(DateUtil.now(), 1));
	}

	private void saveIndex(String s) {
		try {
			TraceIndex traceIndex = JacksonUtil.toBean(s, TraceIndex.class);
			if (RegexUtil.numberRegex(traceIndex.getTraceId())) {
				try {
					String indexName = getIndexName(DateUtil.now());
					elasticsearchTemplate.syncIndexAsync(EMPTY, indexName, s);
				}
				catch (Exception e) {
					log.error("同步数据报错：{}", e.getMessage());
				}
			}
		}
		catch (Exception ignored) {
		}
	}

	@PostConstruct
	private void createTraceIndex() {
		createIndex(DateUtil.now());
	}

	private String getIndexName(LocalDateTime localDateTime) {
		String ym = DateUtil.format(localDateTime, DateUtil.YYYYMM);
		return TRACE_INDEX + UNDER + ym;
	}

	@SneakyThrows
	private void createIndex(LocalDateTime localDateTime) {
		String indexName = getIndexName(localDateTime);
		if (!elasticsearchTemplate.isIndexExists(indexName)) {
			elasticsearchTemplate.createAsyncIndex(indexName, TRACE_INDEX, TraceIndex.class);
		}
	}

}

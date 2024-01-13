/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.core.utils.RegexUtil;
import org.laokou.common.elasticsearch.template.ElasticsearchTemplate;
import org.laokou.common.i18n.utils.DateUtil;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.logstash.gatewayimpl.database.dataobject.TraceIndex;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

import static org.laokou.common.i18n.common.IndexConstants.TRACE;
import static org.laokou.common.i18n.common.StringConstants.*;
import static org.laokou.common.i18n.common.SysConstants.EMPTY_LOG_MSG;
import static org.laokou.common.i18n.common.SysConstants.UNDEFINED;
import static org.laokou.common.i18n.common.KafkaConstants.LAOKOU_LOGSTASH_CONSUMER_GROUP;
import static org.laokou.common.i18n.common.KafkaConstants.LAOKOU_TRACE_TOPIC;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TraceConsumer {

	private final ElasticsearchTemplate elasticsearchTemplate;

	@KafkaListener(topics = LAOKOU_TRACE_TOPIC, groupId = LAOKOU_LOGSTASH_CONSUMER_GROUP)
	public void kafkaConsumer(List<String> messages, Acknowledgment ack) {
		//messages.parallelStream().forEach(this::saveIndex);
		ack.acknowledge();
	}

	/**
	 * 每天23：50：00创建下一个月的索引
	 */
	//@Scheduled(cron = "0 50 23 * * ?")
	public void createTraceIndexJob() {
		// LocalDate localDate = DateUtil.plusDays(DateUtil.getLastDayOfMonth(DateUtil.nowDate()), 1);
		// log(createIndex(localDate), localDate);
	}

	private void saveIndex(String s) {
		try {
			TraceIndex traceIndex = JacksonUtil.toBean(s, TraceIndex.class);
			if (StringUtil.isNotEmpty(traceIndex.getTraceId()) && RegexUtil.numberRegex(traceIndex.getTraceId())) {
				try {
					traceIndex.setId(IdGenerator.defaultSnowflakeId());
					traceIndex.setTenantId(replaceValue(traceIndex.getTenantId()));
					traceIndex.setUserId(replaceValue(traceIndex.getUserId()));
					traceIndex.setUsername(replaceValue(traceIndex.getUsername()));
					String indexName = getIndexName(DateUtil.nowDate());
					elasticsearchTemplate.syncIndexAsync(EMPTY, indexName, JacksonUtil.toJsonStr(traceIndex));
				}
				catch (Exception e) {
					log.error("同步数据报错", e);
				}
			}
		}
		catch (Exception ignored) {
		}
	}

	@PostConstruct
	private void createTraceIndex() {
		LocalDate localDate = DateUtil.nowDate();
		log(createIndex(localDate), localDate);
	}

	private String getIndexName(LocalDate localDate) {
		String ym = DateUtil.format(localDate, DateUtil.YYYYMM);
		return TRACE + UNDER + ym;
	}

	private void log(boolean flag, LocalDate localDate) {
		if (flag) {
			log.info("索引【{}】创建成功", getIndexName(localDate));
		}
	}

	private String replaceValue(String value) {
		if (value.startsWith(DOLLAR) || UNDEFINED.equals(value)) {
			return EMPTY_LOG_MSG;
		}
		return value;
	}

	@SneakyThrows
	private boolean createIndex(LocalDate localDate) {
		String indexName = getIndexName(localDate);
		try {
			if (!elasticsearchTemplate.isIndexExists(indexName)) {
				elasticsearchTemplate.createAsyncIndex(indexName, TRACE, TraceIndex.class);
				return true;
			}
			else {
				log.info("索引【{}】已存在", indexName);
				return false;
			}
		}
		catch (Exception e) {
			log.error("创建索引【{}】失败，错误信息：{}，详情见日志", indexName, LogUtil.result(e.getMessage()), e);
			return false;
		}
	}

}

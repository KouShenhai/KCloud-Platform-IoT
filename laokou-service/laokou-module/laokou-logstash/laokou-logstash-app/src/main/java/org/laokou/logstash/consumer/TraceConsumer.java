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

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.core.utils.RegexUtil;
import org.laokou.common.elasticsearch.template.ElasticsearchTemplate;
import org.laokou.common.i18n.utils.DateUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.logstash.gatewayimpl.database.dataobject.TraceIndex;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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

	private static final String ERROR = "ERROR";

	@KafkaListener(topics = LAOKOU_TRACE_TOPIC, groupId = LAOKOU_LOGSTASH_CONSUMER_GROUP)
	public void kafkaConsumer(List<String> messages, Acknowledgment ack) {
		messages.parallelStream().forEach(this::saveIndex);
		ack.acknowledge();
	}

	/**
	 * 每个月最后一天的23：50：00创建下一个月的索引
	 */
	@XxlJob(value = "traceJobHandler")
	public void createTraceIndexJob() {
		// 单个参数
		String param = XxlJobHelper.getJobParam();
		log.info("接收调度中心参数：{}", param);
		LocalDate localDate = StringUtil.isEmpty(param) ? DateUtil.nowDate()
				: DateUtil.parseDate(param, DateUtil.YYYY_BAR_MM_BAR_DD);
		localDate = DateUtil.plusDays(DateUtil.getFirstDayOfMonth(localDate), 1);
		try {
			log(createIndex(localDate), localDate);
			XxlJobHelper.handleSuccess("创建索引【{" + getIndexName(localDate) + "}】执行成功");
			XxlJobHelper.log("创建索引【{" + getIndexName(localDate) + "}】执行成功");
		}
		catch (Exception e) {
			log.error("错误信息", e);
			XxlJobHelper.log("创建索引【{" + getIndexName(localDate) + "}】执行失败");
			XxlJobHelper.handleFail("创建索引【{" + getIndexName(localDate) + "}】执行失败");
		}
	}

	private void saveIndex(String s) {
		try {
			TraceIndex traceIndex = JacksonUtil.toBean(s, TraceIndex.class);
			if (RegexUtil.numberRegex(traceIndex.getTraceId()) || ERROR.equals(traceIndex.getLevel())) {
				try {
					String indexName = getIndexName(DateUtil.nowDate());
					elasticsearchTemplate.syncIndexAsync(EMPTY, indexName, s);
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
		return TRACE_INDEX + UNDER + ym;
	}

	private void log(boolean flag, LocalDate localDate) {
		if (flag) {
			log.info("索引【{}】创建成功", getIndexName(localDate));
		}
	}

	@SneakyThrows
	private boolean createIndex(LocalDate localDate) {
		String indexName = getIndexName(localDate);
		try {
			if (!elasticsearchTemplate.isIndexExists(indexName)) {
				elasticsearchTemplate.createAsyncIndex(indexName, TRACE_INDEX, TraceIndex.class);
				return true;
			}
			else {
				log.info("索引【{}】已存在", indexName);
				return false;
			}
		}
		catch (Exception e) {
			log.error("创建索引【{}】失败,错误信息", indexName, e);
			return false;
		}
	}

}

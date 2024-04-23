/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.core.utils.RegexUtil;
import org.laokou.common.i18n.utils.DateUtil;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.logstash.gatewayimpl.database.dataobject.TraceIndex;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.laokou.common.i18n.common.ElasticsearchIndexConstant.TRACE;
import static org.laokou.common.i18n.common.KafkaConstant.LAOKOU_LOGSTASH_CONSUMER_GROUP;
import static org.laokou.common.i18n.common.KafkaConstant.LAOKOU_TRACE_TOPIC;
import static org.laokou.common.i18n.common.StringConstant.DOLLAR;
import static org.laokou.common.i18n.common.StringConstant.UNDER;
import static org.laokou.common.i18n.common.SysConstant.UNDEFINED;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TraceConsumer {

	@KafkaListener(topics = LAOKOU_TRACE_TOPIC, groupId = LAOKOU_LOGSTASH_CONSUMER_GROUP)
	public void kafkaConsumer(List<String> messages, Acknowledgment ack) {
		messages.parallelStream().forEach(this::saveIndex);
		ack.acknowledge();
	}

	/**
	 * 每个月最后一天的23：50：00创建下一个月的索引.
	 */
	@XxlJob("createTraceIndexJobHandler")
	public void createTraceIndexJob() {
		// 单个参数
		String ym = XxlJobHelper.getJobParam();
		log.info("接收调度中心参数：{}", ym);
		if (StringUtil.isEmpty(ym)) {
			ym = DateUtil.format(DateUtil.plusMonths(DateUtil.nowDate(), 1), DateUtil.YYYYMM);
		}
		else {
			if (!RegexUtil.numberRegex(ym) || ym.length() != 6) {
				XxlJobHelper.log("时间格式错误");
				XxlJobHelper.handleFail("时间格式错误");
				return;
			}
		}
		try {
			// 创建索引
			createIndex(ym);
			XxlJobHelper.handleSuccess("创建索引【{" + getIndexName(ym) + "}】执行成功");
			XxlJobHelper.log("创建索引【{" + getIndexName(ym) + "}】执行成功");
		}
		catch (Exception e) {
			log.error("错误信息：{}，详情见日志", LogUtil.record(e.getMessage()), e);
			XxlJobHelper.log("创建索引【{" + getIndexName(ym) + "}】执行失败");
			XxlJobHelper.handleFail("创建索引【{" + getIndexName(ym) + "}】执行失败");
		}
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
					String indexName = getIndexName(DateUtil.format(DateUtil.nowDate(), DateUtil.YYYYMM));
					// elasticsearchTemplate.syncIndexAsync(EMPTY, indexName,
					// JacksonUtil.toJsonStr(traceIndex));
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
	public void createTraceIndex() {
		String ym = DateUtil.format(DateUtil.nowDate(), DateUtil.YYYYMM);
		// 创建索引
		createIndex(ym);
	}

	private String getIndexName(String ym) {
		return TRACE + UNDER + ym;
	}

	private String replaceValue(String value) {
		if (value.startsWith(DOLLAR) || UNDEFINED.equals(value)) {
			return "暂无信息";
		}
		return value;
	}

	@SneakyThrows
	private void createIndex(String ym) {
		String indexName = getIndexName(ym);
		// try {
		// if (!elasticsearchTemplate.isIndexExists(indexName)) {
		// elasticsearchTemplate.createAsyncIndex(indexName, TRACE, TraceIndex.class);
		// log.info("索引【{}】创建成功", indexName);
		// }
		// else {
		// log.info("索引【{}】已存在", indexName);
		// }
		// }
		// catch (Exception e) {
		// log.error("创建索引【{}】失败，错误信息：{}，详情见日志", indexName,
		// LogUtil.record(e.getMessage()), e);
		// }
	}

}

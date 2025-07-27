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

package org.laokou.oss.handler;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.laokou.oss.api.OssLogsServiceI;
import org.laokou.oss.convertor.OssLogConvertor;
import org.laokou.oss.dto.OssLogSaveCmd;
import org.laokou.oss.dto.domainevent.OssUploadEvent;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.laokou.common.tenant.constant.DSConstants.DOMAIN;
import static org.laokou.oss.model.MqEnum.OSS_LOG_CONSUMER_GROUP;
import static org.laokou.oss.model.MqEnum.OSS_LOG_TOPIC;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DomainEventHandler {

	private final OssLogsServiceI ossLogsServiceI;

	@KafkaListener(topics = OSS_LOG_TOPIC, groupId = "${spring.kafka.consumer.group-id}-" + OSS_LOG_CONSUMER_GROUP)
	public void handleLoginLog(List<ConsumerRecord<String, Object>> messages, Acknowledgment acknowledgment) {
		try {
			DynamicDataSourceContextHolder.push(DOMAIN);
			for (ConsumerRecord<String, Object> record : messages) {
				ossLogsServiceI.saveOssLog(new OssLogSaveCmd(OssLogConvertor.toClientObject((OssUploadEvent) record.value())));
			}
		} catch (DuplicateKeyException ex) {
			log.error("保存失败，oss日志重复：{}", ex.getMessage());
		}
		finally {
			acknowledgment.acknowledge();
			DynamicDataSourceContextHolder.clear();
		}
	}

}

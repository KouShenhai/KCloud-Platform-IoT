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

package org.laokou.logstash.consumer.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.laokou.common.log4j2.model.MqEnum;
import org.laokou.logstash.api.TraceLogServiceI;
import org.laokou.logstash.dto.TraceLogSaveCmd;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.KafkaListeners;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TraceLogHandler {

	private final TraceLogServiceI traceLogServiceI;

	@KafkaListeners(value = {
			@KafkaListener(topics = MqEnum.DISTRIBUTED_IDENTIFIER_TRACE_LOG_TOPIC,
					groupId = "${spring.kafka.consumer.group-id}-"
							+ MqEnum.DISTRIBUTED_IDENTIFIER_TRACE_LOG_CONSUMER_GROUP),
			@KafkaListener(topics = MqEnum.GATEWAY_TRACE_LOG_TOPIC,
					groupId = "${spring.kafka.consumer.group-id}-" + MqEnum.GATEWAY_TRACE_LOG_COSUMER_GROUP),
			@KafkaListener(topics = MqEnum.AUTH_TRACE_LOG_TOPIC,
					groupId = "${spring.kafka.consumer.group-id}-" + MqEnum.AUTH_TRACE_LOG_CONSUMER_GROUP),
			@KafkaListener(topics = MqEnum.ADMIN_TRACE_LOG_TOPIC,
					groupId = "${spring.kafka.consumer.group-id}-" + MqEnum.ADMIN_TRACE_LOG_CONSUMER_GROUP),
			@KafkaListener(topics = MqEnum.IOT_TRACE_LOG_TOPIC,
					groupId = "${spring.kafka.consumer.group-id}-" + MqEnum.IOT_TRACE_LOG_CONSUMER_GROUP),
			@KafkaListener(topics = MqEnum.OSS_TRACE_LOG_TOPIC,
					groupId = "${spring.kafka.consumer.group-id}-" + MqEnum.OSS_TRACE_LOG_CONSUMER_GROUP),
			@KafkaListener(topics = MqEnum.GENERATOR_TRACE_LOG_TOPIC,
					groupId = "${spring.kafka.consumer.group-id}-" + MqEnum.GENERATOR_TRACE_LOG_CONSUMER_GROUP),
			@KafkaListener(topics = MqEnum.MQTT_TRACE_LOG_TOPIC,
					groupId = "${spring.kafka.consumer.group-id}-" + MqEnum.MQTT_TRACE_LOG_CONSUMER_GROUP),
			@KafkaListener(topics = MqEnum.UDP_TRACE_LOG_TOPIC,
					groupId = "${spring.kafka.consumer.group-id}-" + MqEnum.UDP_TRACE_LOG_CONSUMER_GROUP),
			@KafkaListener(topics = MqEnum.HTTP_TRACE_LOG_TOPIC,
					groupId = "${spring.kafka.consumer.group-id}-" + MqEnum.HTTP_TRACE_LOG_CONSUMER_GROUP),
			@KafkaListener(topics = MqEnum.TCP_TRACE_LOG_TOPIC,
					groupId = "${spring.kafka.consumer.group-id}-" + MqEnum.TCP_TRACE_LOG_CONSUMER_GROUP),
			@KafkaListener(topics = MqEnum.REPORT_TRACE_LOG_TOPIC,
					groupId = "${spring.kafka.consumer.group-id}-" + MqEnum.REPORT_TRACE_LOG_CONSUMER_GROUP) })
	public void handleTraceLog(List<ConsumerRecord<String, Object>> messages, Acknowledgment acknowledgment) {
		try {
			traceLogServiceI.saveTraceLog(new TraceLogSaveCmd(messages.stream().map(ConsumerRecord::value).toList()));
		}
		finally {
			acknowledgment.acknowledge();
		}
	}

}

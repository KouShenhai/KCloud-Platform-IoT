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

package org.laokou.auth.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.laokou.auth.model.MqEnum.LOGIN_LOG_TOPIC;

/**
 * @author laokou
 */
@Slf4j
@Component
public class DomainEventHandler {

	@KafkaListener(topics = LOGIN_LOG_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
	public void loginLogHandler(List<ConsumerRecords<String, Object>> messages, Acknowledgment acknowledgment) {
		log.info("Received login log messages: {}", messages.getFirst());
		acknowledgment.acknowledge();
	}

}

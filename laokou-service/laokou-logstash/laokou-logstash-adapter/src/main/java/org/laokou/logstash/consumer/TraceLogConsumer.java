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

package org.laokou.logstash.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.logstash.api.TraceLogServiceI;
import org.laokou.logstash.dto.TraceLogSaveCmd;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TraceLogConsumer {

	private final TraceLogServiceI traceLogServiceI;

	@KafkaListener(topics = "laokou_trace_topic", groupId = "laokou_trace_consumer_group")
	public Mono<Void> kafkaConsumer(Mono<List<String>> messages, Acknowledgment ack) {
		return traceLogServiceI.save(new TraceLogSaveCmd(messages)).then(Mono.fromRunnable(ack::acknowledge));
	}

}

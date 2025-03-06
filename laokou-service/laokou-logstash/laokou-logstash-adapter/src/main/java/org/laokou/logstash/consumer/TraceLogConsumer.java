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
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.laokou.logstash.api.TraceLogServiceI;
import org.laokou.logstash.dto.TraceLogSaveCmd;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.internals.DefaultKafkaReceiver;

import java.time.Duration;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TraceLogConsumer {

	private final TraceLogServiceI traceLogServiceI;

	private final DefaultKafkaReceiver<String, String> reactiveKafkaReceiver;

	public Flux<Void> consumeMessages() {
		return reactiveKafkaReceiver.receiveBatch(50)
			// 控制消费速率（背压）
			.delayElements(Duration.ofMillis(100))
			.flatMap(records -> traceLogServiceI.save(new TraceLogSaveCmd(
					records.doOnNext(record -> record.receiverOffset().acknowledge()).map(ConsumerRecord::value))))
			.onErrorResume(e -> {
				log.error("Kafka消费失败，错误信息：{}", e.getMessage(), e);
				return Mono.error(e);
			});
	}

}

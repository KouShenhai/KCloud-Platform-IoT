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

package org.laokou.logstash.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.internals.ConsumerFactory;
import reactor.kafka.receiver.internals.DefaultKafkaReceiver;
import java.util.List;

/**
 * @author laokou
 */
@Configuration
public class ReactiveKafkaConfig {

	@Bean("reactiveKafkaReceiver")
	public DefaultKafkaReceiver<String, Object> reactiveKafkaReceiver(ReceiverOptions<String, Object> receiverOptions) {
		return new DefaultKafkaReceiver<>(ConsumerFactory.INSTANCE,
				receiverOptions.subscription(List.of("distributed-identifier-trace-log", "gateway-trace-log",
						"auth-trace-log", "admin-trace-log", "iot-trace-log", "oss-trace-log", "generator-trace-log",
						"mqtt-trace-log", "udp-trace-log", "http-trace-log", "tcp-trace-log", "report-trace-log")));
	}

}

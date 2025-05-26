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
public class ReactiveKafkaConsumerConfig {

	@Bean("reactiveKafkaReceiver")
	public DefaultKafkaReceiver<String, String> reactiveKafkaReceiver(ReceiverOptions<String, String> receiverOptions) {
		return new DefaultKafkaReceiver<>(ConsumerFactory.INSTANCE,
				receiverOptions
					.subscription(List.of("laokou_gateway_trace_log", "laokou_auth_trace_log", "laokou_admin_trace_log",
							"laokou_iot_trace_log", "laokou_oss_trace_log", "laokou_distributed_identifier_trace_log",
							"laokou_generator_trace_log", "laokou_mqtt_trace_log", "laokou_udp_trace_log",
							"laokou_http_trace_log", "laokou_tcp_trace_log", "laokou_report_trace_log")));
	}

}

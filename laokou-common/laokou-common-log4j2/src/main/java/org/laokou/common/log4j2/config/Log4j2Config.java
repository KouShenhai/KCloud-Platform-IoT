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

package org.laokou.common.log4j2.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import static org.laokou.common.log4j2.model.MqEnum.*;

/**
 * @author laokou
 */
@Configuration
public class Log4j2Config {

	@Bean("traceLogNewTopics")
	public KafkaAdmin.NewTopics newTopics() {
		return new KafkaAdmin.NewTopics(new NewTopic(GATEWAY_TRACE_LOG_TOPIC, 3, (short) 1),
				new NewTopic(AUTH_TRACE_LOG_TOPIC, 3, (short) 1), new NewTopic(ADMIN_TRACE_LOG_TOPIC, 3, (short) 1),
				new NewTopic(IOT_TRACE_LOG_TOPIC, 3, (short) 1), new NewTopic(OSS_TRACE_LOG_TOPIC, 3, (short) 1),
				new NewTopic(GENERATOR_TRACE_LOG_TOPIC, 3, (short) 1), new NewTopic(MQTT_TRACE_LOG_TOPIC, 3, (short) 1),
				new NewTopic(UDP_TRACE_LOG_TOPIC, 3, (short) 1), new NewTopic(HTTP_TRACE_LOG_TOPIC, 3, (short) 1),
				new NewTopic(TCP_TRACE_LOG_TOPIC, 3, (short) 1), new NewTopic(REPORT_TRACE_LOG_TOPIC, 3, (short) 1),
				new NewTopic(DISTRIBUTED_IDENTIFIER_TRACE_LOG_TOPIC, 3, (short) 1));
	}

}

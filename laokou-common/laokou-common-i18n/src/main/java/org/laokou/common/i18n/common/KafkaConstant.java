/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

package org.laokou.common.i18n.common;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author laokou
 */
@Schema(name = "KafkaConstants", description = "Kafka消息常量")
public final class KafkaConstant {

	private KafkaConstant() {
	}

	@Schema(name = "LAOKOU_TRACE_TOPIC", description = "分布式链路主题")
	public static final String LAOKOU_TRACE_TOPIC = "laokou_trace_topic";

	@Schema(name = "LAOKOU_LOGSTASH_CONSUMER_GROUP", description = "日志存储消费者组")
	public static final String LAOKOU_LOGSTASH_CONSUMER_GROUP = "laokou_logstash_consumer_group";

}

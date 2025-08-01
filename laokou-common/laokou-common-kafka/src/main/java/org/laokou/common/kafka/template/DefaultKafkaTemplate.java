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

package org.laokou.common.kafka.template;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * kafka发送消息模板.
 *
 * @author laokou
 */
@RequiredArgsConstructor
public class DefaultKafkaTemplate {

	private final KafkaTemplate<String, Object> kafkaTemplate;

	/**
	 * 发送消息.
	 * @param topic 主题
	 * @param payload 内容
	 */
	public <T> void send(String topic, T payload) {
		kafkaTemplate.send(topic, payload);
	}

}

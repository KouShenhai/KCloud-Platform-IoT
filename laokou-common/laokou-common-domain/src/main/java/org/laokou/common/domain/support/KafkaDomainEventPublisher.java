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

package org.laokou.common.domain.support;

import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.dto.DomainEvent;
import org.laokou.common.kafka.template.DefaultKafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class KafkaDomainEventPublisher implements DomainEventPublisher {

	private final DefaultKafkaTemplate defaultKafkaTemplate;

	@Override
	public void publish(String topic, DomainEvent payload) {
		defaultKafkaTemplate.send(topic, payload);
	}

}

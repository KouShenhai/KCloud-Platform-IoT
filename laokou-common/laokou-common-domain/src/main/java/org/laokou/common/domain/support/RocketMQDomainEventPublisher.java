/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.i18n.dto.DomainEvent;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.rocketmq.template.RocketMqTemplate;
import org.springframework.stereotype.Component;

import static org.laokou.common.domain.constant.MqConstant.LAOKOU_CREATE_EVENT_TOPIC;
import static org.laokou.common.domain.constant.MqConstant.LAOKOU_MODIFY_EVENT_TOPIC;

@RequiredArgsConstructor
@Component("domainEventPublisher")
public class RocketMQDomainEventPublisher implements DomainEventPublisher {

	private final RocketMqTemplate rocketMqTemplate;

	@Override
	public <ID> void publishToCreate(DomainEvent<ID> payload) {
		String traceId = String.valueOf(IdGenerator.defaultSnowflakeId());
		rocketMqTemplate.sendAsyncMessage(LAOKOU_CREATE_EVENT_TOPIC, payload, traceId);

	}

	@Override
	public <ID> void publishToModify(DomainEvent<ID> payload) {
		String traceId = String.valueOf(IdGenerator.defaultSnowflakeId());
		rocketMqTemplate.sendAsyncMessage(LAOKOU_MODIFY_EVENT_TOPIC, payload, traceId);
	}

	@Override
	public void publish(String topic, String tag, Object payload) {
		String traceId = String.valueOf(IdGenerator.defaultSnowflakeId());
		if (StringUtil.isNotEmpty(tag)) {
			rocketMqTemplate.sendAsyncMessage(topic, tag, payload, traceId);
		}
		else {
			rocketMqTemplate.sendAsyncMessage(topic, payload, traceId);
		}
	}

}

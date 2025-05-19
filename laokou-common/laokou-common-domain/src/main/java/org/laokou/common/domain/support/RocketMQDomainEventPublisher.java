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
import org.laokou.common.domain.config.RocketMQFuryFactory;
import org.laokou.common.i18n.dto.DomainEvent;
import org.laokou.common.rocketmq.template.RocketMqTemplate;
import org.laokou.common.rocketmq.template.SendMessageTypeEnum;
import org.laokou.common.trace.util.TraceUtils;
import org.springframework.stereotype.Component;

// @formatter:off
@Component
@RequiredArgsConstructor
public class RocketMQDomainEventPublisher implements DomainEventPublisher {

	private final RocketMqTemplate rocketMqTemplate;

	private final TraceUtils traceUtils;

	@Override
	public void publish(DomainEvent payload, SendMessageTypeEnum type) {
		RocketMQFuryFactory furyFactory = RocketMQFuryFactory.getFuryFactory();
		switch (type) {
			case SYNC -> rocketMqTemplate.sendSyncMessage(payload.getTopic(), payload.getTag(), furyFactory.serialize(payload), traceUtils.getTraceId(), traceUtils.getSpanId());
			case ASYNC -> rocketMqTemplate.sendAsyncMessage(payload.getTopic(), payload.getTag(), furyFactory.serialize(payload), traceUtils.getTraceId(), traceUtils.getSpanId());
			case ONE_WAY -> rocketMqTemplate.sendOneWayMessage(payload.getTopic(), payload.getTag(), furyFactory.serialize(payload), traceUtils.getTraceId(), traceUtils.getSpanId());
			case TRANSACTION -> rocketMqTemplate.sendTransactionMessage(payload.getTopic(), payload.getTag(), furyFactory.serialize(payload), payload.getId(), traceUtils.getTraceId(), traceUtils.getSpanId());
			default -> {}
		}
	}
}
// @formatter:on

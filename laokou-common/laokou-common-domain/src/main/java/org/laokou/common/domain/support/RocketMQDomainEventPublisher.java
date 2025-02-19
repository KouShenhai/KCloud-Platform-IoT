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
import org.laokou.common.rocketmq.template.RocketMqTemplate;
import org.laokou.common.rocketmq.template.SendMessageType;
import org.laokou.common.trace.utils.TraceUtil;
import org.springframework.stereotype.Component;

// @formatter:off
@Component
@RequiredArgsConstructor
public class RocketMQDomainEventPublisher implements DomainEventPublisher {

	private final RocketMqTemplate rocketMqTemplate;

	private final TraceUtil traceUtil;

	@Override
	public void publish(DomainEvent payload, SendMessageType type) {
		switch (type) {
			case SYNC -> rocketMqTemplate.sendSyncMessage(payload.getTopic(), payload.getTag(), payload, traceUtil.getTraceId(), traceUtil.getSpanId());
			case ASYNC -> rocketMqTemplate.sendAsyncMessage(payload.getTopic(), payload.getTag(), payload, traceUtil.getTraceId(), traceUtil.getSpanId());
			case ONE_WAY -> rocketMqTemplate.sendOneWayMessage(payload.getTopic(), payload.getTag(), payload, traceUtil.getTraceId(), traceUtil.getSpanId());
			case TRANSACTION -> rocketMqTemplate.sendTransactionMessage(payload.getTopic(), payload.getTag(), payload, payload.getId(), traceUtil.getTraceId(), traceUtil.getSpanId());
			default -> {}
		}
	}
}
// @formatter:on

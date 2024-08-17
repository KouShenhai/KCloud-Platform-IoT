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
import org.apache.logging.log4j.ThreadContext;
import org.laokou.common.i18n.dto.DomainEvent;
import org.laokou.common.rocketmq.template.RocketMqTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import static org.laokou.common.core.config.TaskExecutorAutoConfig.THREAD_POOL_TASK_EXECUTOR_NAME;
import static org.laokou.common.i18n.common.constant.TraceConstant.SPAN_ID;
import static org.laokou.common.i18n.common.constant.TraceConstant.TRACE_ID;

@RequiredArgsConstructor
@Component("domainEventPublisher")
public class RocketMQDomainEventPublisher implements DomainEventPublisher {

	private final RocketMqTemplate rocketMqTemplate;

	@Override
	@Async(THREAD_POOL_TASK_EXECUTOR_NAME)
	public void publishToCreate(DomainEvent<Long> payload) {
		rocketMqTemplate.sendTransactionMessage(payload.getTopic(), payload.getTag(), payload, payload.getId(),
				ThreadContext.get(TRACE_ID), ThreadContext.get(SPAN_ID));
	}

}

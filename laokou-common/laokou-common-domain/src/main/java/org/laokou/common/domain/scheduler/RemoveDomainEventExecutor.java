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

package org.laokou.common.domain.scheduler;

import lombok.RequiredArgsConstructor;
import org.laokou.common.domain.entity.DomainEvent;
import org.laokou.common.lock.annotation.Lock4j;
import org.laokou.common.rocketmq.template.RocketMqTemplate;
import org.laokou.common.trace.utils.TraceUtil;
import org.springframework.stereotype.Component;

import static org.laokou.common.domain.constant.MqConstant.LAOKOU_DOMAIN_EVENT_TOPIC;
import static org.laokou.common.domain.entity.Type.REMOVE;
import static org.laokou.common.lock.Type.FENCED_LOCK;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class RemoveDomainEventExecutor {

	private final RocketMqTemplate rocketMqTemplate;

	private final TraceUtil traceUtil;

	@Lock4j(name = "REMOVE_DOMAIN_EVENT", key = "#serviceId", timeout = 100, retry = 0, type = FENCED_LOCK)
	public void execute(String serviceId) {
		rocketMqTemplate.sendAsyncMessage(LAOKOU_DOMAIN_EVENT_TOPIC, new DomainEvent(serviceId, REMOVE),
				traceUtil.getTraceId(), traceUtil.getSpanId());
	}

}

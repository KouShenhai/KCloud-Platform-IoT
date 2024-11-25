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

package org.laokou.common.domain.consumer;

import io.micrometer.common.lang.NonNullApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.domain.service.DomainEventService;
import org.laokou.common.domain.support.DomainEventPublisher;
import org.laokou.common.i18n.dto.DefaultDomainEvent;
import org.springframework.stereotype.Component;

import static org.apache.rocketmq.spring.annotation.ConsumeMode.CONCURRENTLY;
import static org.apache.rocketmq.spring.annotation.MessageModel.CLUSTERING;
import static org.laokou.common.domain.constant.MqConstant.*;

/**
 * @author laokou
 */
@Slf4j
@Component
@NonNullApi
@RocketMQMessageListener(consumerGroup = LAOKOU_DOMAIN_EVENT_CONSUMER_GROUP, topic = LAOKOU_DOMAIN_EVENT_TOPIC,
		selectorExpression = REMOVE_TAG, messageModel = CLUSTERING, consumeMode = CONCURRENTLY)
public class RemoveDomainEventConsumer extends AbstractDomainEventConsumer {

	private final DomainEventService domainEventService;

	public RemoveDomainEventConsumer(DomainEventPublisher rocketMQDomainEventPublisher,
			DomainEventService domainEventService) {
		super(rocketMQDomainEventPublisher);
		this.domainEventService = domainEventService;
	}

	@Override
	protected void handleDomainEvent(DefaultDomainEvent domainEvent) {
		domainEventService.deleteOldByServiceIdOfThreeMonths(domainEvent.getServiceId());
	}

	@Override
	protected DefaultDomainEvent convert(String msg) {
		return JacksonUtil.toBean(msg, DefaultDomainEvent.class);
	}

}

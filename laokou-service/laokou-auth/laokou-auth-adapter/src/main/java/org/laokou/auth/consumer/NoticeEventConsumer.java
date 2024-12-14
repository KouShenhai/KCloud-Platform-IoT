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

package org.laokou.auth.consumer;

import io.micrometer.common.lang.NonNullApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.laokou.auth.ability.DomainService;
import org.laokou.auth.dto.domainevent.NoticeMessageEvent;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.domain.consumer.AbstractDomainEventConsumer;
import org.laokou.common.domain.support.DomainEventPublisher;
import org.laokou.common.i18n.dto.DefaultDomainEvent;
import org.springframework.stereotype.Component;

import static org.apache.rocketmq.spring.annotation.ConsumeMode.CONCURRENTLY;
import static org.apache.rocketmq.spring.annotation.MessageModel.CLUSTERING;
import static org.laokou.auth.common.constant.MqConstant.*;

/**
 * @author laokou
 */
@Slf4j
@Component
@NonNullApi
@RocketMQMessageListener(consumerGroup = LAOKOU_NOTICE_LOG_CONSUMER_GROUP, topic = LAOKOU_LOG_TOPIC,
		selectorExpression = NOTICE_TAG, messageModel = CLUSTERING, consumeMode = CONCURRENTLY)
public class NoticeEventConsumer extends AbstractDomainEventConsumer {

	private final DomainService domainService;

	public NoticeEventConsumer(DomainEventPublisher domainEventPublisher, DomainService domainService) {
		super(domainEventPublisher);
		this.domainService = domainService;
	}

	@Override
	protected void handleDomainEvent(DefaultDomainEvent domainEvent) {
		// authDomainService.recordNoticeLog(domainEvent);
	}

	@Override
	protected DefaultDomainEvent convert(String msg) {
		return JacksonUtil.toBean(msg, NoticeMessageEvent.class);
	}

}

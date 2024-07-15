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

package org.laokou.common.domain.handler;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.ThreadContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.domain.model.DomainEventA;
import org.laokou.common.domain.service.DomainEventService;
import org.laokou.common.domain.support.DomainEventPublisher;
import org.laokou.common.i18n.dto.DefaultDomainEvent;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static org.apache.rocketmq.spring.annotation.ConsumeMode.CONCURRENTLY;
import static org.apache.rocketmq.spring.annotation.MessageModel.CLUSTERING;
import static org.laokou.common.domain.constant.MqConstant.LAOKOU_CREATE_EVENT_CONSUMER_GROUP;
import static org.laokou.common.domain.constant.MqConstant.LAOKOU_CREATE_EVENT_TOPIC;
import static org.laokou.common.i18n.common.constant.TraceConstant.TRACE_ID;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
@RocketMQMessageListener(consumerGroup = LAOKOU_CREATE_EVENT_CONSUMER_GROUP, topic = LAOKOU_CREATE_EVENT_TOPIC,
		messageModel = CLUSTERING, consumeMode = CONCURRENTLY)
public class CreateDomainEventHandler implements RocketMQListener<MessageExt> {

	private final DomainEventService domainEventService;

	private final Executor executor;

	private final DomainEventPublisher domainEventPublisher;

	@Override
	public void onMessage(MessageExt messageExt) {
		String traceId = messageExt.getProperty(TRACE_ID);
		ThreadContext.put(TRACE_ID, traceId);
		String msg = new String(messageExt.getBody(), StandardCharsets.UTF_8);
		try {
			CompletableFuture.supplyAsync(() -> {
				DefaultDomainEvent defaultDomainEvent = JacksonUtil.toBean(msg, DefaultDomainEvent.class);
				domainEventService.create(new DomainEventA(msg, defaultDomainEvent));
				return defaultDomainEvent;
			}, executor)
				.thenAcceptAsync(defaultDomainEvent -> domainEventPublisher.publish(defaultDomainEvent.getTopic(),
						defaultDomainEvent.getTag(), msg), executor);
		}
		finally {
			ThreadContext.clearMap();
		}
	}

}

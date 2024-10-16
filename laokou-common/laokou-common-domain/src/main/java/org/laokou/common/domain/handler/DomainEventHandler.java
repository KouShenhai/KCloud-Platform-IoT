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

import io.micrometer.common.lang.NonNullApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.domain.entity.DomainEvent;
import org.laokou.common.domain.service.DomainEventService;
import org.laokou.common.trace.utils.MDCUtil;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

import static org.apache.rocketmq.spring.annotation.ConsumeMode.CONCURRENTLY;
import static org.apache.rocketmq.spring.annotation.MessageModel.CLUSTERING;
import static org.laokou.common.domain.constant.MqConstant.LAOKOU_DOMAIN_EVENT_CONSUMER_GROUP;
import static org.laokou.common.domain.constant.MqConstant.LAOKOU_DOMAIN_EVENT_TOPIC;
import static org.laokou.common.i18n.common.constant.TraceConstant.SPAN_ID;
import static org.laokou.common.i18n.common.constant.TraceConstant.TRACE_ID;

/**
 * @author laokou
 */
@Slf4j
@Component
@NonNullApi
@RequiredArgsConstructor
@RocketMQMessageListener(consumerGroup = LAOKOU_DOMAIN_EVENT_CONSUMER_GROUP, topic = LAOKOU_DOMAIN_EVENT_TOPIC,
		messageModel = CLUSTERING, consumeMode = CONCURRENTLY)
public class DomainEventHandler implements RocketMQListener<MessageExt> {

	private final DomainEventService domainEventService;

	@Override
	public void onMessage(MessageExt messageExt) {
		String traceId = messageExt.getProperty(TRACE_ID);
		String spanId = messageExt.getProperty(SPAN_ID);
		MDCUtil.put(traceId, spanId);
		try {
			String msg = new String(messageExt.getBody(), StandardCharsets.UTF_8);
			execute(JacksonUtil.toBean(msg, DomainEvent.class));
		}
		catch (Exception e) {
			log.error("消费失败，主题Topic：{}，偏移量Offset：{}，错误信息：{}", messageExt.getTopic(), messageExt.getCommitLogOffset(),
					e.getMessage(), e);
			throw e;
		}
		finally {
			MDCUtil.clear();
		}
	}

	private void execute(DomainEvent domainEvent) {
		switch (domainEvent.getType()) {
			case REMOVE -> domainEventService.deleteOldByServiceIdOfThreeMonths(domainEvent.getServiceId());
			case UPDATE -> {
			}
			default -> {
			}
		}
	}

}

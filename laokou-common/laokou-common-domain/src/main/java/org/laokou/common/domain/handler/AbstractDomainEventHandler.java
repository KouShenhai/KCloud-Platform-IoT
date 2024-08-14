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
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.message.MessageView;
import org.apache.rocketmq.client.core.RocketMQListener;
import org.laokou.common.domain.support.DomainEventPublisher;
import org.laokou.common.i18n.dto.DefaultDomainEvent;

import java.nio.charset.StandardCharsets;

import static org.laokou.common.i18n.common.constant.TraceConstant.TRACE_ID;

/**
 * @author laokou
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractDomainEventHandler implements RocketMQListener {

	protected final DomainEventPublisher domainEventPublisher;

	@Override
	public ConsumeResult consume(MessageView messageView) {
		try {
			String traceId = messageView.getProperties().get(TRACE_ID);
			ThreadContext.put(TRACE_ID, traceId);
			String msg = new String(messageView.getBody().array(), StandardCharsets.UTF_8);
			handleDomainEvent(convert(msg));
		} catch (Exception e) {
			log.error("消费失败，主题Topic：{}，错误信息：{}", messageView.getTopic(),
				e.getMessage(), e);
			throw e;
		} finally {
			ThreadContext.clearMap();
		}
		return ConsumeResult.SUCCESS;
	}

	protected abstract void handleDomainEvent(DefaultDomainEvent domainEvent);

	protected abstract DefaultDomainEvent convert(String msg);

}

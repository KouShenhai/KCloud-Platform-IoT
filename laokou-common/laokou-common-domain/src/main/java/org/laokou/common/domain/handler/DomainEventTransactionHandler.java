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
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.domain.model.DomainEventA;
import org.laokou.common.domain.service.DomainEventService;
import org.laokou.common.i18n.dto.DefaultDomainEvent;
import org.laokou.common.rocketmq.handler.AbstractTransactionHandler;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
@RocketMQTransactionListener
public class DomainEventTransactionHandler extends AbstractTransactionHandler {

	private final DomainEventService domainEventService;

	@Override
	protected void executeExtLocalTransaction(Message message, Object args) {
		byte[] payload = (byte[]) message.getPayload();
		domainEventService.create(new DomainEventA(payload, JacksonUtil.toBean(payload, DefaultDomainEvent.class)));
	}

	@Override
	protected boolean checkExtLocalTransaction(Message message) {
		String txId = Objects.requireNonNull(message.getHeaders().get(RocketMQHeaders.TRANSACTION_ID)).toString();
		return domainEventService.count(Long.parseLong(txId)) > 0;
	}

}

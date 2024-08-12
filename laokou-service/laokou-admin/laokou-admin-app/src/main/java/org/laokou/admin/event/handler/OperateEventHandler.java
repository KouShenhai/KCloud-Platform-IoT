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

package org.laokou.admin.event.handler;

import io.micrometer.common.lang.NonNullApi;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.domain.handler.AbstractDomainEventHandler;
import org.laokou.common.domain.support.DomainEventPublisher;
import org.laokou.common.i18n.dto.DefaultDomainEvent;
import org.laokou.common.log.domainevent.OperateEvent;
import org.springframework.stereotype.Component;

/**
 * 操作日志处理.
 *
 * @author laokou
 */
@Slf4j
@Component
@NonNullApi
// @RocketMQMessageListener(consumerGroup = LAOKOU_OPERATE_EVENT_CONSUMER_GROUP, topic =
// LAOKOU_OPERATE_EVENT_TOPIC,
// messageModel = CLUSTERING, consumeMode = ORDERLY)
public class OperateEventHandler extends AbstractDomainEventHandler {

	public OperateEventHandler(DomainEventPublisher domainEventPublisher) {
		super(domainEventPublisher);
	}

	// @Override
	// protected void handleDomainEvent(DefaultDomainEvent evt, String attribute) {
	// try {
	// OperateEvent event = JacksonUtil.toBean(attribute, OperateEvent.class);
	// DynamicDataSourceContextHolder.push(evt.getSourceName());
	// // logGateway.create(event, evt);
	// }
	// finally {
	// DynamicDataSourceContextHolder.clear();
	// }
	// }

	@Override
	protected void handleDomainEvent(DefaultDomainEvent domainEvent) {

	}

	@Override
	protected DefaultDomainEvent convert(String msg) {
		return JacksonUtil.toBean(msg, OperateEvent.class);
	}

}

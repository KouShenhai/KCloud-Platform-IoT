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

package org.laokou.auth.event.handler;

import org.laokou.auth.dto.domainevent.CallApiEvent;
import org.laokou.auth.dto.domainevent.SendCaptchaEvent;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.domain.handler.AbstractDomainEventHandler;
import org.laokou.common.domain.support.DomainEventPublisher;
import org.laokou.common.i18n.dto.ApiLog;
import org.laokou.common.i18n.dto.DefaultDomainEvent;

import static org.laokou.auth.common.constant.MqConstant.API_TAG;
import static org.laokou.auth.common.constant.MqConstant.LAOKOU_LOG_TOPIC;
import static org.laokou.common.i18n.common.constant.EventType.API;

/**
 * @author laokou
 */
public abstract class AbstractSendCaptchaEventHandler extends AbstractDomainEventHandler {

	protected AbstractSendCaptchaEventHandler(DomainEventPublisher domainEventPublisher) {
		super(domainEventPublisher);
	}

	@Override
	protected void handleDomainEvent(DefaultDomainEvent domainEvent) {
		SendCaptchaEvent event = (SendCaptchaEvent) domainEvent;
		ApiLog apiLog = getApiLog(event);
		CallApiEvent callApiEvent = new CallApiEvent(apiLog, LAOKOU_LOG_TOPIC, API_TAG, API, event.getServiceId(),
			event.getSourceName(), event.getAggregateId());
		rocketMQDomainEventPublisher.publish(callApiEvent);
	}

	@Override
	protected DefaultDomainEvent convert(String msg) {
		return JacksonUtil.toBean(msg, SendCaptchaEvent.class);
	}

	protected abstract ApiLog getApiLog(SendCaptchaEvent event);

}

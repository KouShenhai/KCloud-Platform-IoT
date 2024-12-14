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

import org.laokou.auth.dto.domainevent.SendCaptchaEvent;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.domain.consumer.AbstractDomainEventConsumer;
import org.laokou.common.domain.support.DomainEventPublisher;
import org.laokou.common.i18n.dto.DefaultDomainEvent;

/**
 * @author laokou
 */
public abstract class AbstractSendCaptchaEventConsumer extends AbstractDomainEventConsumer {

	protected AbstractSendCaptchaEventConsumer(DomainEventPublisher domainEventPublisher) {
		super(domainEventPublisher);
	}

	@Override
	protected void handleDomainEvent(DefaultDomainEvent domainEvent) {
		SendCaptchaEvent event = (SendCaptchaEvent) domainEvent;
		// NoticeMessageEvent noticeMessageEvent = new
		// NoticeMessageEvent(LAOKOU_LOG_TOPIC, NOTICE_TAG, SEND_NOTICE,
		// event.getServiceId(), event.getSourcePrefix(), event.getAggregateId(),
		// event.getTenantId());
		// rocketMQDomainEventPublisher.publish(noticeMessageEvent,
		// SendMessageType.ONE_WAY);
	}

	@Override
	protected DefaultDomainEvent convert(String msg) {
		return JacksonUtil.toBean(msg, SendCaptchaEvent.class);
	}

	/*
	 * protected abstract NoticeLog getNoticeLog(SendCaptchaEvent event);
	 */

}

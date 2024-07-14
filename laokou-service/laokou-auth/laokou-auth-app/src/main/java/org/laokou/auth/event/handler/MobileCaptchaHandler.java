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

import io.micrometer.common.lang.NonNullApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.laokou.auth.dto.domainevent.SendCaptchaEvent;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.domain.handler.AbstractDomainEventHandler;
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
@RocketMQMessageListener(consumerGroup = LAOKOU_MOBILE_CAPTCHA_CONSUMER_GROUP, topic = LAOKOU_CAPTCHA_TOPIC,
	selectorExpression = MOBILE_TAG, messageModel = CLUSTERING, consumeMode = CONCURRENTLY)
public class MobileCaptchaHandler extends AbstractDomainEventHandler {

	public MobileCaptchaHandler(DomainEventPublisher domainEventPublisher) {
		super(domainEventPublisher);
	}

	@Override
	protected void handleDomainEvent(DefaultDomainEvent domainEvent) {
		log.info("手机号：{}", domainEvent);
	}

	@Override
	protected DefaultDomainEvent convert(String msg) {
		return JacksonUtil.toBean(msg, SendCaptchaEvent.class);
	}
}

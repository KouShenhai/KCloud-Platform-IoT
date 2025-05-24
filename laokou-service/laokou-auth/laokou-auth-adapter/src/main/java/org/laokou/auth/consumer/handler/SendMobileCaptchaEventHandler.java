/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.auth.consumer.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.micrometer.common.lang.NonNullApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.laokou.auth.api.NoticeLogServiceI;
import org.laokou.auth.convertor.NoticeLogConvertor;
import org.laokou.auth.dto.NoticeLogSaveCmd;
import org.laokou.auth.dto.domainevent.SendCaptchaEvent;
import org.laokou.common.domain.handler.AbstractDomainEventHandler;
import org.laokou.common.i18n.dto.DomainEvent;
import org.laokou.common.i18n.util.JacksonUtils;
import org.laokou.common.sms.service.SmsService;
import org.springframework.stereotype.Component;

import static org.apache.rocketmq.spring.annotation.ConsumeMode.CONCURRENTLY;
import static org.apache.rocketmq.spring.annotation.MessageModel.CLUSTERING;
import static org.laokou.auth.model.MqEnum.*;

/**
 * @author laokou
 */
@Slf4j
@Component
@NonNullApi
@RocketMQMessageListener(consumerGroup = MOBILE_CAPTCHA_CONSUMER_GROUP, topic = CAPTCHA_TOPIC,
		selectorExpression = MOBILE_CAPTCHA_TAG, messageModel = CLUSTERING, consumeMode = CONCURRENTLY,
		consumeThreadMax = 128, consumeThreadNumber = 64)
public class SendMobileCaptchaEventHandler extends AbstractDomainEventHandler {

	private final SmsService smsService;

	private final NoticeLogServiceI noticeLogServiceI;

	public SendMobileCaptchaEventHandler(SmsService smsService, NoticeLogServiceI noticeLogServiceI) {
		this.smsService = smsService;
		this.noticeLogServiceI = noticeLogServiceI;
	}

	@Override
	protected void handleDomainEvent(DomainEvent domainEvent) throws JsonProcessingException {
		SendCaptchaEvent evt = JacksonUtils.toBean(domainEvent.getPayload(), SendCaptchaEvent.class);
		noticeLogServiceI.save(new NoticeLogSaveCmd(
				NoticeLogConvertor.toClientObject(domainEvent, smsService.send(evt.uuid()), evt.uuid())));
	}

}

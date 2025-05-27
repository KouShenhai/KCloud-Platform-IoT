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
import org.laokou.auth.api.NoticeLogServiceI;
import org.laokou.common.i18n.dto.DomainEvent;
import org.laokou.common.sms.service.SmsService;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Slf4j
@Component
@NonNullApi
public class SendMobileCaptchaEventHandler {

	private final SmsService smsService;

	private final NoticeLogServiceI noticeLogServiceI;

	public SendMobileCaptchaEventHandler(SmsService smsService, NoticeLogServiceI noticeLogServiceI) {
		this.smsService = smsService;
		this.noticeLogServiceI = noticeLogServiceI;
	}

	protected void handleDomainEvent(DomainEvent domainEvent) throws JsonProcessingException {
		// SendCaptchaEvent evt = JacksonUtils.toBean(domainEvent.getPayload(),
		// SendCaptchaEvent.class);
		// noticeLogServiceI.save(new NoticeLogSaveCmd(
		// NoticeLogConvertor.toClientObject(domainEvent, smsService.send(evt.uuid()),
		// evt.uuid())));
	}

}

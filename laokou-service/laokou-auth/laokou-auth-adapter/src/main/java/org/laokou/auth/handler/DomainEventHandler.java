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

package org.laokou.auth.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.laokou.auth.api.LoginLogServiceI;
import org.laokou.auth.api.NoticeLogServiceI;
import org.laokou.auth.convertor.LoginLogConvertor;
import org.laokou.auth.convertor.NoticeLogConvertor;
import org.laokou.auth.dto.LoginLogSaveCmd;
import org.laokou.auth.dto.NoticeLogSaveCmd;
import org.laokou.auth.dto.domainevent.LoginEvent;
import org.laokou.auth.dto.domainevent.SendCaptchaEvent;
import org.laokou.auth.model.MqEnum;
import org.laokou.common.mail.service.MailService;
import org.laokou.common.sms.service.SmsService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DomainEventHandler {

	private final LoginLogServiceI loginLogServiceI;

	private final MailService mailService;

	private final SmsService smsService;

	private final NoticeLogServiceI noticeLogServiceI;

	@KafkaListener(topics = MqEnum.LOGIN_LOG_TOPIC,
			groupId = "${spring.kafka.consumer.group-id}-" + MqEnum.LOGIN_LOG_CONSUMER_GROUP)
	public void handleLoginLog(List<ConsumerRecord<String, Object>> messages, Acknowledgment acknowledgment) {
		try {
			for (ConsumerRecord<String, Object> record : messages) {
				loginLogServiceI
					.saveLoginLog(new LoginLogSaveCmd(LoginLogConvertor.toClientObject((LoginEvent) record.value())));
			}
		}
		finally {
			acknowledgment.acknowledge();
		}
	}

	@KafkaListener(topics = MqEnum.MAIL_CAPTCHA_TOPIC,
			groupId = "${spring.kafka.consumer.group-id}-" + MqEnum.MAIL_CAPTCHA_CONSUMER_GROUP)
	public void handleMailCaptcha(List<ConsumerRecord<String, Object>> messages, Acknowledgment acknowledgment) {
		try {
			for (ConsumerRecord<String, Object> record : messages) {
				SendCaptchaEvent evt = (SendCaptchaEvent) record.value();
				noticeLogServiceI.saveNoticeLog(
						new NoticeLogSaveCmd(NoticeLogConvertor.toClientObject(evt, mailService.send(evt.getUuid()))));
			}
		}
		finally {
			acknowledgment.acknowledge();
		}
	}

	@KafkaListener(topics = MqEnum.MOBILE_CAPTCHA_TOPIC,
			groupId = "${spring.kafka.consumer.group-id}-" + MqEnum.MOBILE_CAPTCHA_CONSUMER_GROUP)
	public void handleMobileCaptcha(List<ConsumerRecord<String, Object>> messages, Acknowledgment acknowledgment) {
		try {
			for (ConsumerRecord<String, Object> record : messages) {
				SendCaptchaEvent evt = (SendCaptchaEvent) record.value();
				noticeLogServiceI.saveNoticeLog(
						new NoticeLogSaveCmd(NoticeLogConvertor.toClientObject(evt, smsService.send(evt.getUuid()))));
			}
		}
		finally {
			acknowledgment.acknowledge();
		}
	}

}

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

package org.laokou.common.mail.service.impl;

import lombok.RequiredArgsConstructor;
import org.laokou.common.mail.service.MailService;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * @author laokou
 */
@RequiredArgsConstructor
public abstract class AbstractMailServiceImpl implements MailService {

	private final MailProperties mailProperties;

	protected void sendMail(String subject, String content, String toMail) {
		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		sender.setHost(mailProperties.getHost());
		sender.setUsername(mailProperties.getUsername());
		sender.setPassword(mailProperties.getPassword());
		mailMessage.setTo(toMail);
		mailMessage.setSubject(subject);
		mailMessage.setFrom(mailProperties.getUsername());
		mailMessage.setText(content);
		sender.send(mailMessage);
	}

}

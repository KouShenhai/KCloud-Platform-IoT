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
import org.apache.rocketmq.client.annotation.RocketMQMessageListener;
import org.laokou.auth.command.LoginLogCmdExe;
import org.laokou.auth.dto.LoginLogSaveCmd;
import org.laokou.auth.dto.domainevent.LoginEvent;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.domain.handler.AbstractDomainEventHandler;
import org.laokou.common.domain.support.DomainEventPublisher;
import org.laokou.common.i18n.dto.DefaultDomainEvent;
import org.springframework.stereotype.Component;

import static org.laokou.auth.common.constant.MqConstant.*;

/**
 * 登录日志处理器.
 *
 * @author laokou
 */
@Component
@NonNullApi
@RocketMQMessageListener(consumerGroup = LAOKOU_LOGIN_LOG_CONSUMER_GROUP, topic = LAOKOU_LOG_TOPIC, tag = LOGIN_TAG)
public class LoginEventHandler extends AbstractDomainEventHandler {

	private final LoginLogCmdExe loginLogCmdExe;

	public LoginEventHandler(DomainEventPublisher domainEventPublisher, LoginLogCmdExe loginLogCmdExe) {
		super(domainEventPublisher);
		this.loginLogCmdExe = loginLogCmdExe;
	}

	@Override
	protected void handleDomainEvent(DefaultDomainEvent domainEvent) {
		loginLogCmdExe.executeVoid(new LoginLogSaveCmd(domainEvent));
	}

	@Override
	protected DefaultDomainEvent convert(String msg) {
		return JacksonUtil.toBean(msg, LoginEvent.class);
	}

}

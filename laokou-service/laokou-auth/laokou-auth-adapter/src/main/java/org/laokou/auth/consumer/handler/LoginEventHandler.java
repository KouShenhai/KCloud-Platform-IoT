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
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.laokou.auth.api.LoginLogServiceI;
import org.laokou.auth.convertor.LoginLogConvertor;
import org.laokou.auth.dto.LoginLogSaveCmd;
import org.laokou.common.domain.handler.AbstractDomainEventHandler;
import org.laokou.common.i18n.dto.DomainEvent;
import org.springframework.stereotype.Component;

import static org.apache.rocketmq.spring.annotation.ConsumeMode.CONCURRENTLY;
import static org.apache.rocketmq.spring.annotation.MessageModel.CLUSTERING;
import static org.laokou.auth.model.Constant.LAOKOU_LOGIN_LOG_CONSUMER_GROUP;
import static org.laokou.auth.model.Constant.LAOKOU_LOG_TOPIC;
import static org.laokou.auth.model.Constant.LOGIN_TAG;

/**
 * 登录日志处理器.
 *
 * @author laokou
 */
@Component
@NonNullApi
@RocketMQMessageListener(consumerGroup = LAOKOU_LOGIN_LOG_CONSUMER_GROUP, topic = LAOKOU_LOG_TOPIC,
		selectorExpression = LOGIN_TAG, messageModel = CLUSTERING, consumeMode = CONCURRENTLY)
public class LoginEventHandler extends AbstractDomainEventHandler {

	private final LoginLogServiceI loginLogServiceI;

	public LoginEventHandler(LoginLogServiceI loginLogServiceI) {
		this.loginLogServiceI = loginLogServiceI;
	}

	@Override
	protected void handleDomainEvent(DomainEvent domainEvent) throws JsonProcessingException {
		loginLogServiceI.save(new LoginLogSaveCmd(LoginLogConvertor.toClientObject(domainEvent)));
	}

}

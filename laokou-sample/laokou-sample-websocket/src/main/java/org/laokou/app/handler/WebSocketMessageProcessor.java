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

package org.laokou.app.handler;

import io.netty.channel.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.client.dto.clientobject.MessageCO;
import org.laokou.client.dto.clientobject.PayloadCO;
import org.laokou.client.dto.domainevent.PublishMessageEvent;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.core.utils.SpringUtil;
import org.laokou.common.domain.support.DomainEventPublisher;
import org.laokou.common.netty.config.WebSocketSessionManager;
import org.laokou.common.rocketmq.template.SendMessageType;
import org.laokou.domain.model.MessageType;
import org.springframework.stereotype.Component;

import static org.laokou.common.i18n.common.constant.StringConstant.EMPTY;
import static org.laokou.infrastructure.common.constant.MqConstant.LAOKOU_MESSAGE_TOPIC;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
final class WebSocketMessageProcessor {

	private final DomainEventPublisher rocketMQDomainEventPublisher;

	private final SpringUtil springUtil;

	public void processMessage(MessageCO message, Channel channel) {
		switch (MessageType.valueOf(message.getType().toUpperCase())) {
			case PONG -> log.info("接收{}心跳{}", channel.id().asLongText(), message.getPayload());
			case CONNECT -> {
				log.info("已连接ClientID：{}", message.getPayload());
				WebSocketSessionManager.add(message.getPayload().toString(), channel);
			}
			case MESSAGE -> {
				log.info("接收消息：{}", message.getPayload());
				publishMessage(message.getPayload());
			}
		}
	}

	private void publishMessage(Object payload) {
		rocketMQDomainEventPublisher.publish(new PublishMessageEvent(LAOKOU_MESSAGE_TOPIC, EMPTY,
				JacksonUtil.toValue(payload, PayloadCO.class), springUtil.getServiceId()), SendMessageType.TRANSACTION);
	}

}

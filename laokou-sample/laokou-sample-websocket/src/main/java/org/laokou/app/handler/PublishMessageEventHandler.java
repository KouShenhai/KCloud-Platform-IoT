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

import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.laokou.client.dto.clientobject.PayloadCO;
import org.laokou.client.dto.domainevent.PublishMessageEvent;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.core.utils.ThreadUtil;
import org.laokou.common.domain.handler.AbstractDomainEventHandler;
import org.laokou.common.domain.support.DomainEventPublisher;
import org.laokou.common.i18n.dto.DefaultDomainEvent;
import org.laokou.common.netty.config.Server;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import static org.apache.rocketmq.spring.annotation.ConsumeMode.CONCURRENTLY;
import static org.apache.rocketmq.spring.annotation.MessageModel.BROADCASTING;
import static org.laokou.infrastructure.common.constant.MqConstant.LAOKOU_MESSAGE_CONSUMER_GROUP;
import static org.laokou.infrastructure.common.constant.MqConstant.LAOKOU_MESSAGE_TOPIC;

// @formatter:off
/**
 * @author laokou
 */
@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = LAOKOU_MESSAGE_CONSUMER_GROUP, topic = LAOKOU_MESSAGE_TOPIC, messageModel = BROADCASTING, consumeMode = CONCURRENTLY)
public class PublishMessageEventHandler extends AbstractDomainEventHandler {

	private final Server webSocketServer;

	public PublishMessageEventHandler(DomainEventPublisher rocketMQDomainEventPublisher, Server webSocketServer) {
		super(rocketMQDomainEventPublisher);
		this.webSocketServer = webSocketServer;
	}

	@Override
	protected void handleDomainEvent(DefaultDomainEvent domainEvent) {
		try (ExecutorService executor = ThreadUtil.newVirtualTaskExecutor()) {
			PublishMessageEvent event = (PublishMessageEvent) domainEvent;
			PayloadCO co = event.getCo();
			TextWebSocketFrame webSocketFrame = new TextWebSocketFrame(JacksonUtil.toJsonStr(co.getContent()));
			co.getReceivers().parallelStream().forEach(clientId -> CompletableFuture.runAsync(() -> webSocketServer.send(clientId, webSocketFrame), executor));
		}
	}

	@Override
	protected DefaultDomainEvent convert(String msg) {
		return JacksonUtil.toBean(msg, PublishMessageEvent.class);
	}

}
// @formatter:on

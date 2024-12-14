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

package org.laokou.adapter.consumer;

import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.laokou.client.dto.clientobject.PayloadCO;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.core.utils.ThreadUtil;
import org.laokou.common.netty.config.Server;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
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
public class SubscribeMessageConsumer implements RocketMQListener<MessageExt> {

	private final Server webSocketServer;

	public SubscribeMessageConsumer(Server webSocketServer) {
		this.webSocketServer = webSocketServer;
	}

	@Override
	public void onMessage(MessageExt message) {
		try (ExecutorService executor = ThreadUtil.newVirtualTaskExecutor()) {
			String msg = new String(message.getBody(), StandardCharsets.UTF_8);
			PayloadCO co = JacksonUtil.toBean(msg, PayloadCO.class);
			TextWebSocketFrame webSocketFrame = new TextWebSocketFrame(JacksonUtil.toJsonStr(co.getContent()));
			co.getReceivers().parallelStream().forEach(clientId -> CompletableFuture.runAsync(() -> webSocketServer.send(clientId, webSocketFrame), executor));
		}
    }

}
// @formatter:on

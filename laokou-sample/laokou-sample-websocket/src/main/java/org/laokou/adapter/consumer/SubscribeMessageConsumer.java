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

package org.laokou.adapter.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.laokou.client.dto.clientobject.PayloadCO;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.util.JacksonUtils;
import org.laokou.common.netty.config.Server;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import static org.apache.rocketmq.spring.annotation.ConsumeMode.CONCURRENTLY;
import static org.apache.rocketmq.spring.annotation.MessageModel.BROADCASTING;
import static org.laokou.infrastructure.common.constant.MqConstant.*;

// @formatter:off
/**
 * @author laokou
 */
@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = LAOKOU_WS_MESSAGE_CONSUMER_GROUP, topic = LAOKOU_WS_MESSAGE_TOPIC, messageModel = BROADCASTING, consumeMode = CONCURRENTLY)
public class SubscribeMessageConsumer implements RocketMQListener<MessageExt> {

	private final Server webSocketServer;

	private final ExecutorService virtualThreadExecutor;

	public SubscribeMessageConsumer(Server webSocketServer, ExecutorService virtualThreadExecutor) {
		this.webSocketServer = webSocketServer;
		this.virtualThreadExecutor = virtualThreadExecutor;
	}

	@Override
	public void onMessage(MessageExt message) {
		try {
			String msg = new String(message.getBody(), StandardCharsets.UTF_8);
			PayloadCO co = JacksonUtils.toBean(msg, PayloadCO.class);
			TextWebSocketFrame webSocketFrame = new TextWebSocketFrame(co.getContent());
			List<Callable<Future<Void>>> callableList = co.getReceivers().stream().map(clientId -> (Callable<Future<Void>>) () -> webSocketServer.send(clientId, webSocketFrame)).toList();
			virtualThreadExecutor.invokeAll(callableList);
		} catch (InterruptedException | JsonProcessingException e) {
			Thread.currentThread().interrupt();
			log.error("错误信息：{}", e.getMessage());
			throw new SystemException("S_UnKnow_Error", e.getMessage());
		}
    }

}
// @formatter:on

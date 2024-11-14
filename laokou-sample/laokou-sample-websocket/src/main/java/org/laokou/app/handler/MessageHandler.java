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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.laokou.common.core.utils.ThreadUtil;
import org.laokou.common.netty.config.Server;
import org.laokou.common.rocketmq.handler.TraceHandler;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;

import static org.apache.rocketmq.spring.annotation.ConsumeMode.CONCURRENTLY;
import static org.apache.rocketmq.spring.annotation.MessageModel.BROADCASTING;
import static org.laokou.infrastructure.common.constant.MqConstant.LAOKOU_MESSAGE_CONSUMER_GROUP;
import static org.laokou.infrastructure.common.constant.MqConstant.LAOKOU_MESSAGE_TOPIC;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
@RocketMQMessageListener(consumerGroup = LAOKOU_MESSAGE_CONSUMER_GROUP, topic = LAOKOU_MESSAGE_TOPIC,
		messageModel = BROADCASTING, consumeMode = CONCURRENTLY)
public class MessageHandler extends TraceHandler implements RocketMQListener<MessageExt> {

	private final Server webSocketServer;

	@Override
	public void onMessage(MessageExt messageExt) {
		try (ExecutorService executor = ThreadUtil.newVirtualTaskExecutor()) {
			putTrace(messageExt);
			String message = new String(messageExt.getBody(), StandardCharsets.UTF_8);
//			Message msg = JacksonUtil.toBean(message, Message.class);
//			String payload = msg.getPayload();
//			Set<String> receiver = msg.getReceiver();
//			log.info("接收消息：{}", message);
//			TextWebSocketFrame webSocketFrame = new TextWebSocketFrame(JacksonUtil.toJsonStr(Result.ok(payload)));
//			receiver.parallelStream()
//				.forEach(clientId -> CompletableFuture.runAsync(() -> webSocketServer.send(clientId, webSocketFrame),
//						executor));
		}
		finally {
			clearTrace();
		}
	}

}

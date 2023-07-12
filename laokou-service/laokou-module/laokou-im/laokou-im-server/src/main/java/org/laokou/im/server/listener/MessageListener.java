/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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

package org.laokou.im.server.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.rocketmq.dto.MqDTO;
import org.laokou.im.client.WsMsgDTO;
import org.laokou.im.server.config.WebSocketServer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

import static org.laokou.common.rocketmq.constant.MqConstant.LAOKOU_MESSAGE_CONSUMER_GROUP;
import static org.laokou.common.rocketmq.constant.MqConstant.LAOKOU_MESSAGE_TOPIC;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
@RocketMQMessageListener(consumerGroup = LAOKOU_MESSAGE_CONSUMER_GROUP, topic = LAOKOU_MESSAGE_TOPIC,
		messageModel = MessageModel.BROADCASTING, consumeMode = ConsumeMode.CONCURRENTLY)
public class MessageListener implements RocketMQListener<MessageExt> {

	private final WebSocketServer websocketServer;

	private final ThreadPoolTaskExecutor taskExecutor;

	@Override
	public void onMessage(MessageExt messageExt) {
		String message = new String(messageExt.getBody(), StandardCharsets.UTF_8);
		if (StringUtil.isEmpty(message)) {
			return;
		}
		MqDTO dto = JacksonUtil.toBean(message, MqDTO.class);
		String body = dto.getBody();
		WsMsgDTO msgDTO = JacksonUtil.toBean(body, WsMsgDTO.class);
		for (String userId : msgDTO.getReceiver()) {
			CompletableFuture.runAsync(() -> websocketServer.send(userId, msgDTO.getMsg()), taskExecutor);
		}
	}

}

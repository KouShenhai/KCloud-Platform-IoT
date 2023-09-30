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

package org.laokou.im.common.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.laokou.im.common.utils.MessageUtil;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

import static org.laokou.common.rocketmq.constant.MqConstant.*;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
@RocketMQMessageListener(consumerGroup = LAOKOU_NOTICE_MESSAGE_CONSUMER_GROUP, topic = LAOKOU_MESSAGE_TOPIC,
		selectorExpression = LAOKOU_NOTICE_MESSAGE_TAG, messageModel = MessageModel.BROADCASTING,
		consumeMode = ConsumeMode.CONCURRENTLY)
public class NoticeMessageListener implements RocketMQListener<MessageExt> {

	private final MessageUtil messageUtil;

	@Override
	public void onMessage(MessageExt messageExt) {
		String message = new String(messageExt.getBody(), StandardCharsets.UTF_8);
		log.info("接收到通知消息：{}", message);
		messageUtil.send(message);
	}

}

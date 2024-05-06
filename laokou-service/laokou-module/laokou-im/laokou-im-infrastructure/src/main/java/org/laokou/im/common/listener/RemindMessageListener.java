/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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
import org.apache.logging.log4j.ThreadContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.laokou.im.common.utils.MessageUtil;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

import static org.apache.rocketmq.spring.annotation.ConsumeMode.CONCURRENTLY;
import static org.apache.rocketmq.spring.annotation.MessageModel.BROADCASTING;
import static org.laokou.common.i18n.common.RocketMqConstant.*;
import static org.laokou.common.i18n.common.constants.TraceConstant.TRACE_ID;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
@RocketMQMessageListener(consumerGroup = LAOKOU_REMIND_MESSAGE_CONSUMER_GROUP, topic = LAOKOU_MESSAGE_TOPIC,
		selectorExpression = LAOKOU_REMIND_MESSAGE_TAG, messageModel = BROADCASTING, consumeMode = CONCURRENTLY)
public class RemindMessageListener implements RocketMQListener<MessageExt> {

	private final MessageUtil messageUtil;

	@Override
	public void onMessage(MessageExt messageExt) {
		try {
			String msg = new String(messageExt.getBody(), StandardCharsets.UTF_8);
			String traceId = messageExt.getProperty(TRACE_ID);
			ThreadContext.put(TRACE_ID, traceId);
			log.info("接收到提醒消息：{}", msg);
			messageUtil.send(msg);
		}
		finally {
			ThreadContext.clearMap();
		}
	}

}

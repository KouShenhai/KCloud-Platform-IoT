/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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
import org.apache.rocketmq.client.annotation.RocketMQMessageListener;
import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.message.MessageView;
import org.apache.rocketmq.client.core.RocketMQListener;
import org.laokou.im.common.utils.MessageUtil;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

import static org.laokou.common.i18n.common.RocketMqConstants.*;
import static org.laokou.common.i18n.common.TraceConstants.TRACE_ID;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
@RocketMQMessageListener(consumerGroup = LAOKOU_REMIND_MESSAGE_CONSUMER_GROUP, topic = LAOKOU_MESSAGE_TOPIC,
		tag = LAOKOU_REMIND_MESSAGE_TAG)
public class RemindMessageListener implements RocketMQListener {

	private final MessageUtil messageUtil;

	@Override
	public ConsumeResult consume(MessageView messageView) {
		try {
			String message = new String(messageView.getBody().array(), StandardCharsets.UTF_8);
			String traceId = messageView.getProperties().get(TRACE_ID);
			ThreadContext.put(TRACE_ID, traceId);
			log.info("接收到提醒消息：{}", message);
			messageUtil.send(message);
		}
		finally {
			ThreadContext.clearMap();
		}
		return ConsumeResult.SUCCESS;
	}

}

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

package org.laokou.im.handler;

import lombok.extern.slf4j.Slf4j;
import org.laokou.common.netty.config.Server;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;

/**
 * @author laokou
 */
@Slf4j
@Component
// @RocketMQMessageListener(consumerGroup = LAOKOU_NOTICE_MESSAGE_CONSUMER_GROUP, topic =
// LAOKOU_MESSAGE_TOPIC,
// selectorExpression = LAOKOU_NOTICE_MESSAGE_TAG, messageModel = BROADCASTING,
// consumeMode = CONCURRENTLY)
public class NoticeMessageHandler extends AbstractMessageHandler {

	public NoticeMessageHandler(Server webSocketServer, Executor executor) {
		super(webSocketServer, executor);
	}

	@Override
	protected void log(String msg) {
		log.info("接收到通知消息：{}", msg);
	}

}

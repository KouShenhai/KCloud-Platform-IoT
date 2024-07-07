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

import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.ThreadContext;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.i18n.dto.Message;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.netty.config.Server;

import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static org.laokou.common.i18n.common.constant.TraceConstant.TRACE_ID;

@RequiredArgsConstructor
public abstract class AbstractMessageHandler implements RocketMQListener<MessageExt> {

	private final Server webSocketServer;

	private final Executor executor;

	@Override
	public void onMessage(MessageExt messageExt) {
		try {
			String msg = new String(messageExt.getBody(), StandardCharsets.UTF_8);
			String traceId = messageExt.getProperty(TRACE_ID);
			ThreadContext.put(TRACE_ID, traceId);
			log(msg);
			send(msg);
		}
		finally {
			ThreadContext.clearMap();
		}
	}

	abstract protected void log(String msg);

	private void send(String message) {
		if (StringUtil.isEmpty(message)) {
			return;
		}
		Message msg = JacksonUtil.toBean(message, Message.class);
		String payload = msg.getPayload();
		Set<String> receiver = msg.getReceiver();
		TextWebSocketFrame webSocketFrame = new TextWebSocketFrame(JacksonUtil.toJsonStr(Result.ok(payload)));
		receiver.parallelStream()
			.forEach(clientId -> CompletableFuture.runAsync(() -> webSocketServer.send(clientId, webSocketFrame),
					executor));
	}

}

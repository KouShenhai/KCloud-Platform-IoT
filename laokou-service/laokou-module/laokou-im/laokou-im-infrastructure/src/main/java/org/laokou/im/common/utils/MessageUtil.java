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

package org.laokou.im.common.utils;

import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.RequiredArgsConstructor;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.netty.config.Server;
import org.laokou.common.rocketmq.clientobject.MqCO;
import org.laokou.im.dto.message.clientobject.WsMsgCO;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class MessageUtil {

	private final Server websocketServer;

	private final ThreadPoolTaskExecutor taskExecutor;

	public void send(String message) {
		if (StringUtil.isEmpty(message)) {
			return;
		}
		MqCO dto = JacksonUtil.toBean(message, MqCO.class);
		String body = dto.getBody();
		WsMsgCO msgDTO = JacksonUtil.toBean(body, WsMsgCO.class);
		String msg = msgDTO.getMsg();
		Set<String> receiver = msgDTO.getReceiver();
		TextWebSocketFrame webSocketFrame = new TextWebSocketFrame(msg);
		for (String clientId : receiver) {
			CompletableFuture.runAsync(() -> websocketServer.send(clientId, webSocketFrame), taskExecutor);
		}
	}

}

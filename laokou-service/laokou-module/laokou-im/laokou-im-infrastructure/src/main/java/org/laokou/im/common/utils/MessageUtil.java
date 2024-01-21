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

package org.laokou.im.common.utils;

import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.RequiredArgsConstructor;
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.netty.config.Server;
import org.laokou.im.dto.message.clientobject.MsgCO;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static org.laokou.common.i18n.common.StatusCodes.OK;
import static org.laokou.common.i18n.common.SysConstants.THREAD_POOL_TASK_EXECUTOR_NAME;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class MessageUtil {

	private final Server websocketServer;

	private final Executor ttlTaskExecutor;

	@Async(THREAD_POOL_TASK_EXECUTOR_NAME)
	public void send(String message) {
		if (StringUtil.isEmpty(message)) {
			return;
		}
		MsgCO msgDTO = JacksonUtil.toBean(message, MsgCO.class);
		String msg = msgDTO.getMsg();
		Set<String> receiver = msgDTO.getReceiver();
		TextWebSocketFrame webSocketFrame = new TextWebSocketFrame(JacksonUtil.toJsonStr(Result.of(OK, msg)));
		receiver.parallelStream()
			.forEach(clientId -> CompletableFuture.runAsync(() -> websocketServer.send(clientId, webSocketFrame),
					ttlTaskExecutor));
	}

}

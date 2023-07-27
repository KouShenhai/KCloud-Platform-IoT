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
import org.laokou.common.core.utils.JacksonUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.netty.config.Server;
import org.laokou.common.rocketmq.dto.MqDTO;
import org.laokou.im.client.WsMsgDTO;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class MessageUtil {

	private final Server websocketServer;

	private final ThreadPoolTaskExecutor taskExecutor;

	public void pushMessage(String message) {
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

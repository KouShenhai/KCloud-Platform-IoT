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

package org.laokou.im.module.websocket;

import org.laokou.common.netty.config.Server;
import org.laokou.im.config.WebsocketProperties;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author laokou
 */
@Configuration
@RefreshScope
public class WebSocketConfig {

	@Bean(name = "websocketServer", initMethod = "start", destroyMethod = "stop")
	public Server websocketServer(WebsocketProperties websocketProperties,
			WebsocketChannelInitializer websocketChannelInitializer, TaskExecutionProperties taskExecutionProperties) {
		return new WebSocketServer(websocketProperties.getPort(), websocketProperties.getPoolName(),
				websocketChannelInitializer, taskExecutionProperties);
	}

}

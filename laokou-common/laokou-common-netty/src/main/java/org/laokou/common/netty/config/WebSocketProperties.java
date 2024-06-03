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

package org.laokou.common.netty.config;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.net.URI;

/**
 * @author laokou
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.websocket")
@Schema(name = "WebSocketProperties", description = "WebSocket属性配置")
public class WebSocketProperties {

	private Client client;

	private Server server;

	@Data
	public static class Client {

		private String uri;

	}

	@Data
	public static class Server {

		/**
		 * IP.
		 */
		private String ip;

		/**
		 * 端口.
		 */
		private int port;

		/**
		 * 应用名称.
		 */
		private String appName;

	}

	public int getPort(URI uri) {
		if (uri.getPort() == -1) {
			if (uri.getScheme().startsWith("wss")) {
				return 443;
			} else if (uri.getScheme().startsWith("ws")) {
				return 80;
			} else {
				return -1;
			}
		} else {
			return uri.getPort();
		}
	}

}

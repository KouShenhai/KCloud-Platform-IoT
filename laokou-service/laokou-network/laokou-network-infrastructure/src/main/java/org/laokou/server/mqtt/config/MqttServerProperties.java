/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.server.mqtt.config;

import lombok.Data;

import java.util.concurrent.TimeUnit;

/**
 * @author laokou
 */
@Data
public class MqttServerProperties {

	private boolean auth = true;

	private String username = "vertx";

	private String password = "laokou123";

	private String host = "0.0.0.0";

	private int port = 11883;

	private int maxMessageSize = 10000;

	private boolean isAutoClientId = true;

	private int maxClientIdLength = 30;

	private int timeoutOnConnect = 90;

	private boolean useWebSocket = false;

	private int webSocketMaxFrameSize = 65536;

	private boolean perFrameWebSocketCompressionSupported = true;

	private boolean perMessageWebSocketCompressionSupported = true;

	private int webSocketCompressionLevel = 6;

	private boolean webSocketAllowServerNoContext = false;

	private boolean webSocketPreferredClientNoContext = false;

	private boolean tcpNoDelay = true;

	private boolean tcpKeepAlive = true;

	private int soLinger = -1;

	private int idleTimeout = 0;

	private int readIdleTimeout = 0;

	private int writeIdleTimeout = 0;

	private TimeUnit idleTimeoutUnit = TimeUnit.SECONDS;

	private boolean ssl = false;

	private boolean tcpFastOpen = false;

	private boolean tcpCork = false;

	private boolean tcpQuickAck = false;

	private int tcpUserTimeout = 0;

}

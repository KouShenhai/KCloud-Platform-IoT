/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.network.config.tcp;

import io.vertx.core.http.ClientAuth;
import lombok.Data;

import java.util.concurrent.TimeUnit;

/**
 * @author laokou
 */
@Data
public class TcpServerProperties {

	private String host = "0.0.0.0";

	private Integer port = 10100;

	private int acceptBacklog = -1;

	private ClientAuth clientAuth = ClientAuth.NONE;

	private boolean sni = false;

	private boolean useProxyProtocol = false;

	private long proxyProtocolTimeout = 30L;

	private TimeUnit proxyProtocolTimeoutUnit = TimeUnit.SECONDS;

	private boolean registerWriteHandler = false;

}

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

package org.laokou.tcp.server.config;

import io.vertx.core.http.ClientAuth;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author laokou
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.tcp-server")
public class SpringTcpServerProperties {

	private String host = "0.0.0.0";

	private Set<Integer> ports = new HashSet<>(0);

	private int acceptBacklog = -1;

	private ClientAuth clientAuth = ClientAuth.NONE;

	private boolean sni = false;

	private boolean useProxyProtocol = false;

	private long proxyProtocolTimeout = 30L;

	private TimeUnit proxyProtocolTimeoutUnit = TimeUnit.SECONDS;

	private boolean registerWriteHandler = false;

}

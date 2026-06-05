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

package org.laokou.network.config.rabbitmq;

import lombok.Data;

/**
 * RabbitMQ Vert.x client config.
 *
 * @author laokou
 */
@Data
public class RabbitmqConfig {

	private String host = "127.0.0.1";

	private int port = 5672;

	private String virtualHost = "/";

	private String username = "guest";

	private String password = "guest";

	private String connectionName = "laokou-network";

	private String exchange = "";

	private String routingKey = "";

	private String queue = "";

	private int requestedHeartbeat = 60;

	private int connectionTimeout = 60000;

	private long networkRecoveryInterval = 5000L;

	private boolean automaticRecoveryEnabled = true;

	private boolean ssl = false;

	private boolean trustAll = false;

}

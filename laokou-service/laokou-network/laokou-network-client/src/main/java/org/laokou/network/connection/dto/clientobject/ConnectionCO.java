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

package org.laokou.network.connection.dto.clientobject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.common.i18n.dto.ClientObject;

import java.time.Instant;

/**
 * Network connection client object.
 *
 * @author laokou
 */
@Data
@Schema(name = "ConnectionCO", description = "Network connection client object")
public class ConnectionCO extends ClientObject {

	@Schema(name = "ID", description = "ID")
	private Long id;

	@Schema(name = "Name", description = "Connection name")
	private String name;

	@Schema(name = "Type", description = "1 MQTT Server, 2 HTTP Server, 3 MQTT Client, 4 Kafka, 5 RabbitMQ")
	private Integer type;

	@Schema(name = "Host", description = "Connection host")
	private String host;

	@Schema(name = "Port", description = "Connection port")
	private Integer port;

	@Schema(name = "Enabled", description = "0 enabled, 1 disabled")
	private Integer enabled;

	@Schema(name = "Config", description = "Type-specific JSON config")
	private String config;

	@Schema(name = "Remark", description = "Remark")
	private String remark;

	@Schema(name = "Create time", description = "Create time")
	private Instant createTime;

}

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

package org.laokou.common.modbus4j.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.modbus")
public class SpringModbusProperties {

	private ModbusTypeEnum type = ModbusTypeEnum.TCP_MASTER;

	private int timeout = 1000;

	private int retries = 3;

	private ModbusTcp tcp = new ModbusTcp();

	private ModbusUdp udp = new ModbusUdp();

	@Data
	public static class ModbusTcp {

		private String host = "0.0.0.0";

		private int port = 502;

		private boolean keepAlive = false;

	}

	@Data
	public static class ModbusUdp {

		private String host = "0.0.0.0";

		private int port = 502;

	}

}

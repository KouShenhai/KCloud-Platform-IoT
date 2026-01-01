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

	private int timeout = 5000;

	private int retries = 5;

	private ModbusTcp tcp = new ModbusTcp();

	private ModbusUdp udp = new ModbusUdp();

	private ModbusRtu rtu = new ModbusRtu();

	private ModbusAscii ascii = new ModbusAscii();

	@Data
	public static class ModbusAscii extends ModbusRtu {

	}

	@Data
	public static class ModbusRtu {

		private String commPortId = "COM1";

		private int baudRate = 9600;

		private int dataBits = 8;

		private int stopBits = 1;

		private int parity = 0;

		private int timeout = 5000;

	}

	@Data
	public static class ModbusTcp {

		private String host = "127.0.0.1";

		private int port = 502;

		private boolean keepAlive = false;

	}

	@Data
	public static class ModbusUdp {

		private String host = "127.0.0.1";

		private int port = 502;

	}

}

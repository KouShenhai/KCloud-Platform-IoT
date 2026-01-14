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

package org.laokou.common.modbus4j;

import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.msg.ModbusResponse;
import com.serotonin.modbus4j.msg.ReadCoilsResponse;
import com.serotonin.modbus4j.msg.ReadDiscreteInputsResponse;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersResponse;
import com.serotonin.modbus4j.msg.ReadInputRegistersResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.laokou.common.modbus4j.config.Modbus;
import org.laokou.common.modbus4j.config.ModbusTypeEnum;
import org.laokou.common.modbus4j.config.SpringModbusProperties;
import org.laokou.common.testcontainers.container.ModbusContainer;
import org.laokou.common.testcontainers.util.DockerImageNames;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * 基于 Testcontainers 的 Modbus 协议测试用例。
 * <p>
 * 测试覆盖：
 * <ul>
 * <li>TCP - 使用 iotechsys/pymodbus-sim Docker 容器</li>
 * <li>UDP - 使用 koushenhai/modbus-sim Docker 容器（需先构建）</li>
 * <li>RTU - 需要物理串口或虚拟串口软件</li>
 * <li>ASCII - 需要物理串口或虚拟串口软件</li>
 * </ul>
 * </p>
 * @author laokou
 */
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ModbusTest {

	/**
	 * Modbus TCP 服务器容器 (iotechsys/pymodbus-sim)。 支持 Modbus TCP 协议，默认端口 5020。
	 */
	@Container
	static ModbusContainer tcpContainer = ModbusContainer.tcp(DockerImageNames.pymodbus(), ModbusContainer.PYMODBUS_PORT);

	/**
	 * Modbus UDP 服务器容器 (koushenhai/modbus-sim)。 支持 Modbus UDP 协议，端口 502。 需要先构建镜像。
	 */
	@Container
	static ModbusContainer udpContainer = ModbusContainer.udp(DockerImageNames.modbusSim(),
			ModbusContainer.MODBUS_STANDARD_PORT);

	private ModbusFactory modbusFactory;

	private SpringModbusProperties springModbusProperties;

	@BeforeEach
	void setUp() {
		modbusFactory = new ModbusFactory();
		springModbusProperties = new SpringModbusProperties();
		springModbusProperties.setTimeout(5000);
		springModbusProperties.setRetries(2);
	}

	// ==================== TCP 协议测试 ====================

	@Test
	@Order(1)
	@DisplayName("Test Modbus TCP - Read Holding Registers")
	void test_tcp_read_holding_registers() throws ModbusInitException, ModbusTransportException {
		configureTcp(tcpContainer.getHost(), tcpContainer.getModbusTcpPort());
		Modbus modbus = createModbus(ModbusTypeEnum.TCP_MASTER);
		try {
			modbus.open();
			ModbusResponse response = modbus.sendReadHoldingRegistersRequest(1, 0, 1);
			Assertions.assertThat(response).isNotNull();
			if (response instanceof ReadHoldingRegistersResponse holdingResponse) {
				Assertions.assertThat(holdingResponse.getShortData()).isNotNull();
			}
		}
		finally {
			modbus.close();
		}
	}

	@Test
	@Order(2)
	@DisplayName("Test Modbus TCP - Read Coils")
	void test_tcp_read_coils() throws ModbusInitException, ModbusTransportException {
		configureTcp(tcpContainer.getHost(), tcpContainer.getModbusTcpPort());
		Modbus modbus = createModbus(ModbusTypeEnum.TCP_MASTER);
		try {
			modbus.open();
			ModbusResponse response = modbus.sendReadCoilsRequest(1, 0, 1);
			Assertions.assertThat(response).isNotNull();
			if (response instanceof ReadCoilsResponse coilsResponse) {
				Assertions.assertThat(coilsResponse.getBooleanData()).isNotNull();
			}
		}
		finally {
			modbus.close();
		}
	}

	@Test
	@Order(3)
	@DisplayName("Test Modbus TCP - Read Input Registers")
	void test_tcp_read_input_registers() throws ModbusInitException, ModbusTransportException {
		configureTcp(tcpContainer.getHost(), tcpContainer.getModbusTcpPort());
		Modbus modbus = createModbus(ModbusTypeEnum.TCP_MASTER);
		try {
			modbus.open();
			ModbusResponse response = modbus.sendReadInputRegistersRequest(1, 0, 1);
			Assertions.assertThat(response).isNotNull();
			if (response instanceof ReadInputRegistersResponse inputResponse) {
				Assertions.assertThat(inputResponse.getShortData()).isNotNull();
			}
		}
		finally {
			modbus.close();
		}
	}

	@Test
	@Order(4)
	@DisplayName("Test Modbus TCP - Read Discrete Inputs")
	void test_tcp_read_discrete_inputs() throws ModbusInitException, ModbusTransportException {
		configureTcp(tcpContainer.getHost(), tcpContainer.getModbusTcpPort());
		Modbus modbus = createModbus(ModbusTypeEnum.TCP_MASTER);
		try {
			modbus.open();
			ModbusResponse response = modbus.sendReadDiscreteInputsRequest(1, 0, 1);
			Assertions.assertThat(response).isNotNull();
			if (response instanceof ReadDiscreteInputsResponse discreteResponse) {
				Assertions.assertThat(discreteResponse.getBooleanData()).isNotNull();
			}
		}
		finally {
			modbus.close();
		}
	}

	// ==================== UDP 协议测试 ====================

	@Test
	@Order(5)
	@DisplayName("Test Modbus UDP - Read Holding Registers")
	void test_udp_read_holding_registers() throws ModbusInitException, ModbusTransportException {
		configureUdp(udpContainer.getHost(), udpContainer.getModbusPort());
		Modbus modbus = createModbus(ModbusTypeEnum.UDP_MASTER);
		try {
			modbus.open();
			ModbusResponse response = modbus.sendReadHoldingRegistersRequest(1, 0, 1);
			Assertions.assertThat(response).isNotNull();
			if (response instanceof ReadHoldingRegistersResponse holdingResponse) {
				short[] data = holdingResponse.getShortData();
				Assertions.assertThat(data).isNotNull();
				Assertions.assertThat(data[0]).isEqualTo((short) 101);
			}
		}
		finally {
			modbus.close();
		}
	}

	@Test
	@Order(6)
	@DisplayName("Test Modbus UDP - Read Coils")
	void test_udp_read_coils() throws ModbusInitException, ModbusTransportException {
		configureUdp(udpContainer.getHost(), udpContainer.getModbusPort());
		Modbus modbus = createModbus(ModbusTypeEnum.UDP_MASTER);
		try {
			modbus.open();
			ModbusResponse response = modbus.sendReadCoilsRequest(1, 0, 1);
			Assertions.assertThat(response).isNotNull();
			if (response instanceof ReadCoilsResponse coilsResponse) {
				Assertions.assertThat(coilsResponse.getBooleanData()).isNotNull();
			}
		}
		finally {
			modbus.close();
		}
	}

	@Test
	@Order(7)
	@DisplayName("Test Modbus UDP - Read Input Registers")
	void test_udp_read_input_registers() throws ModbusInitException, ModbusTransportException {
		configureUdp(udpContainer.getHost(), udpContainer.getModbusPort());
		Modbus modbus = createModbus(ModbusTypeEnum.UDP_MASTER);
		try {
			modbus.open();
			ModbusResponse response = modbus.sendReadInputRegistersRequest(1, 0, 1);
			Assertions.assertThat(response).isNotNull();
			if (response instanceof ReadInputRegistersResponse inputResponse) {
				Assertions.assertThat(inputResponse.getShortData()).isNotNull();
			}
		}
		finally {
			modbus.close();
		}
	}

	@Test
	@Order(8)
	@DisplayName("Test Modbus UDP - Read Discrete Inputs")
	void test_udp_read_discrete_inputs() throws ModbusInitException, ModbusTransportException {
		configureUdp(udpContainer.getHost(), udpContainer.getModbusPort());
		Modbus modbus = createModbus(ModbusTypeEnum.UDP_MASTER);
		try {
			modbus.open();
			ModbusResponse response = modbus.sendReadDiscreteInputsRequest(1, 0, 1);
			Assertions.assertThat(response).isNotNull();
			if (response instanceof ReadDiscreteInputsResponse discreteResponse) {
				Assertions.assertThat(discreteResponse.getBooleanData()).isNotNull();
			}
		}
		finally {
			modbus.close();
		}
	}

	// ==================== RTU 协议测试 ====================

	@Test
	@Order(9)
	@Disabled("RTU requires physical or virtual serial ports")
	@DisplayName("Test Modbus RTU - Read Holding Registers")
	void test_rtu_read_holding_registers() throws ModbusInitException, ModbusTransportException {
		springModbusProperties.getRtu().setCommPortId("COM1");
		springModbusProperties.getRtu().setBaudRate(9600);
		Modbus modbus = createModbus(ModbusTypeEnum.RTU_MASTER);
		try {
			modbus.open();
			ModbusResponse response = modbus.sendReadHoldingRegistersRequest(1, 0, 1);
			Assertions.assertThat(response).isNotNull();
		}
		finally {
			modbus.close();
		}
	}

	@Test
	@Order(10)
	@Disabled("RTU requires physical or virtual serial ports")
	@DisplayName("Test Modbus RTU - Read Coils")
	void test_rtu_read_coils() throws ModbusInitException, ModbusTransportException {
		springModbusProperties.getRtu().setCommPortId("COM1");
		Modbus modbus = createModbus(ModbusTypeEnum.RTU_MASTER);
		try {
			modbus.open();
			ModbusResponse response = modbus.sendReadCoilsRequest(1, 0, 1);
			Assertions.assertThat(response).isNotNull();
		}
		finally {
			modbus.close();
		}
	}

	// ==================== ASCII 协议测试 ====================

	@Test
	@Order(11)
	@Disabled("ASCII requires physical or virtual serial ports")
	@DisplayName("Test Modbus ASCII - Read Holding Registers")
	void test_ascii_read_holding_registers() throws ModbusInitException, ModbusTransportException {
		springModbusProperties.getAscii().setCommPortId("COM1");
		Modbus modbus = createModbus(ModbusTypeEnum.ASCII_MASTER);
		try {
			modbus.open();
			ModbusResponse response = modbus.sendReadHoldingRegistersRequest(1, 0, 1);
			Assertions.assertThat(response).isNotNull();
		}
		finally {
			modbus.close();
		}
	}

	@Test
	@Order(12)
	@Disabled("ASCII requires physical or virtual serial ports")
	@DisplayName("Test Modbus ASCII - Read Coils")
	void test_ascii_read_coils() throws ModbusInitException, ModbusTransportException {
		springModbusProperties.getAscii().setCommPortId("COM1");
		Modbus modbus = createModbus(ModbusTypeEnum.ASCII_MASTER);
		try {
			modbus.open();
			ModbusResponse response = modbus.sendReadCoilsRequest(1, 0, 1);
			Assertions.assertThat(response).isNotNull();
		}
		finally {
			modbus.close();
		}
	}

	// ==================== 辅助方法 ====================

	private void configureTcp(String host, int port) {
		springModbusProperties.getTcp().setHost(host);
		springModbusProperties.getTcp().setPort(port);
	}

	private void configureUdp(String host, int port) {
		springModbusProperties.getUdp().setHost(host);
		springModbusProperties.getUdp().setPort(port);
	}

	private Modbus createModbus(ModbusTypeEnum typeEnum) {
		springModbusProperties.setType(typeEnum);
		return typeEnum.getModbus(modbusFactory, springModbusProperties);
	}

}

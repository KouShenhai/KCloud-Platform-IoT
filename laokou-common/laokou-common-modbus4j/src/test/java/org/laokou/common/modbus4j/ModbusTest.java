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

package org.laokou.common.modbus4j;

import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.msg.ModbusResponse;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.modbus4j.config.ModbusTcpMaster;
import org.laokou.common.modbus4j.config.ModbusUdpMaster;
import org.laokou.common.modbus4j.config.SpringModbusProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

/**
 * @author laokou
 */
@Slf4j
@SpringBootTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class ModbusTest {

	private final ModbusFactory modbusFactory;

	private final SpringModbusProperties springModbusProperties;

	@Test
	void testUdp() throws ModbusInitException, ModbusTransportException {
		ModbusUdpMaster udpMaster = new ModbusUdpMaster(modbusFactory, springModbusProperties);
		udpMaster.open();
		ModbusResponse modbusResponse = udpMaster.sendRequest(1, 0, 1);
		if (modbusResponse instanceof ReadHoldingRegistersResponse readRegistersResponse) {
			Assertions.assertEquals(111, readRegistersResponse.getShortData()[0]);
		}
		udpMaster.close();
	}

	@Test
	void testTcp() throws ModbusInitException, ModbusTransportException {
		ModbusTcpMaster tcpMaster = new ModbusTcpMaster(modbusFactory, springModbusProperties);
		tcpMaster.open();
		ModbusResponse modbusResponse = tcpMaster.sendRequest(1, 0, 1);
		if (modbusResponse instanceof ReadHoldingRegistersResponse readRegistersResponse) {
			Assertions.assertEquals(32767, readRegistersResponse.getShortData()[0]);
		}
		tcpMaster.close();
	}

}

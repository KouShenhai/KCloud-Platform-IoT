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
import com.serotonin.modbus4j.msg.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.laokou.common.core.util.ConvertUtils;
import org.laokou.common.modbus4j.config.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import static org.assertj.core.api.Assertions.assertThat;

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
	void test_ascii() throws ModbusInitException, ModbusTransportException {
		test(ModbusTypeEnum.ASCII_MASTER);
	}

	@Test
	void test_rtu() throws ModbusInitException, ModbusTransportException {
		test(ModbusTypeEnum.RTU_MASTER);
	}

	@Test
	void test_udp() throws ModbusInitException, ModbusTransportException {
		test(ModbusTypeEnum.UDP_MASTER);
	}

	@Test
	void test_tcp() throws ModbusInitException, ModbusTransportException {
		test(ModbusTypeEnum.TCP_MASTER);
	}

	private void test(ModbusTypeEnum typeEnum) throws ModbusInitException, ModbusTransportException {
		SpringModbusProperties properties = ConvertUtils.sourceToTarget(springModbusProperties,
				SpringModbusProperties.class);
		assertThat(properties).isNotNull();
		properties.setType(typeEnum);
		Modbus modbus = properties.getType().getModbus(modbusFactory, properties);
		modbus.open();
		ModbusResponse modbusResponse = modbus.sendReadHoldingRegistersRequest(1, 0, 1);
		if (modbusResponse instanceof ReadHoldingRegistersResponse readRegistersResponse) {
			assertThat(readRegistersResponse.getShortData()[0]).isEqualTo(1);
		}
		modbusResponse = modbus.sendReadCoilsRequest(1, 0, 1);
		if (modbusResponse instanceof ReadCoilsResponse readCoilsResponse) {
			assertThat(readCoilsResponse.getBooleanData()[0]).isTrue();
		}
		modbusResponse = modbus.sendReadInputRegistersRequest(1, 0, 1);
		if (modbusResponse instanceof ReadInputRegistersResponse readInputRegistersResponse) {
			assertThat(readInputRegistersResponse.getShortData()[0]).isEqualTo(1);
		}
		modbusResponse = modbus.sendReadDiscreteInputsRequest(1, 0, 1);
		if (modbusResponse instanceof ReadDiscreteInputsResponse readDiscreteInputsResponse) {
			assertThat(readDiscreteInputsResponse.getBooleanData()[0]).isTrue();
		}
		modbus.close();
	}

}

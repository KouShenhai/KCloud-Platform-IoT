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
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.laokou.common.core.util.ConvertUtils;
import org.laokou.common.modbus4j.config.Modbus;
import org.laokou.common.modbus4j.config.ModbusTypeEnum;
import org.laokou.common.modbus4j.config.SpringModbusProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestConstructor;

/**
 * @author laokou
 */
@TestConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor
@ContextConfiguration(classes = { ModbusFactory.class, SpringModbusProperties.class })
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
		Assertions.assertThat(properties).isNotNull();
		properties.setType(typeEnum);
		Modbus modbus = properties.getType().getModbus(modbusFactory, properties);
		modbus.open();
		ModbusResponse modbusResponse = modbus.sendReadHoldingRegistersRequest(1, 0, 1);
		if (modbusResponse instanceof ReadHoldingRegistersResponse readRegistersResponse) {
			Assertions.assertThat(readRegistersResponse.getShortData()[0] == 1).isTrue();
		}
		modbusResponse = modbus.sendReadCoilsRequest(1, 0, 1);
		if (modbusResponse instanceof ReadCoilsResponse readCoilsResponse) {
			Assertions.assertThat(readCoilsResponse.getBooleanData()[0]).isTrue();
		}
		modbusResponse = modbus.sendReadInputRegistersRequest(1, 0, 1);
		if (modbusResponse instanceof ReadInputRegistersResponse readInputRegistersResponse) {
			Assertions.assertThat(readInputRegistersResponse.getShortData()[0] == 1).isTrue();
		}
		modbusResponse = modbus.sendReadDiscreteInputsRequest(1, 0, 1);
		if (modbusResponse instanceof ReadDiscreteInputsResponse readDiscreteInputsResponse) {
			Assertions.assertThat(readDiscreteInputsResponse.getBooleanData()[0]).isTrue();
		}
		modbus.close();
	}

}

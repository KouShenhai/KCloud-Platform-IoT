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

import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.msg.ModbusResponse;
import com.serotonin.modbus4j.msg.ReadCoilsRequest;
import com.serotonin.modbus4j.msg.ReadDiscreteInputsRequest;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersRequest;
import com.serotonin.modbus4j.msg.ReadInputRegistersRequest;

public abstract class AbstractModbus implements Modbus {

	protected final SpringModbusProperties properties;

	protected volatile ModbusMaster modbusMaster;

	protected AbstractModbus(SpringModbusProperties properties) {
		this.properties = properties;
	}

	@Override
	public synchronized void open() throws ModbusInitException {
		modbusMaster.setTimeout(properties.getTimeout());
		modbusMaster.setRetries(properties.getRetries());
		modbusMaster.init();
	}

	@Override
	public synchronized void close() {
		modbusMaster.destroy();
	}

	@Override
	public ModbusResponse sendReadHoldingRegistersRequest(int slaveId, int startOffset, int numberOfRegisters)
			throws ModbusTransportException {
		return modbusMaster.send(new ReadHoldingRegistersRequest(slaveId, startOffset, numberOfRegisters));
	}

	@Override
	public ModbusResponse sendReadInputRegistersRequest(int slaveId, int startOffset, int numberOfRegisters)
			throws ModbusTransportException {
		return modbusMaster.send(new ReadInputRegistersRequest(slaveId, startOffset, numberOfRegisters));
	}

	@Override
	public ModbusResponse sendReadCoilsRequest(int slaveId, int startOffset, int numberOfRegisters)
			throws ModbusTransportException {
		return modbusMaster.send(new ReadCoilsRequest(slaveId, startOffset, numberOfRegisters));
	}

	@Override
	public ModbusResponse sendReadDiscreteInputsRequest(int slaveId, int startOffset, int numberOfRegisters)
			throws ModbusTransportException {
		return modbusMaster.send(new ReadDiscreteInputsRequest(slaveId, startOffset, numberOfRegisters));
	}

}

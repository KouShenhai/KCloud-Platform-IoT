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

import com.fazecast.jSerialComm.SerialPort;
import com.serotonin.modbus4j.serial.SerialPortWrapper;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author laokou
 */
public final class CustomSerialPortWrapper implements SerialPortWrapper {

	private final int baudRate;

	private final int dataBits;

	private final int stopBits;

	private final int parity;

	private final int timeout;

	private final SerialPort serialPort;

	public CustomSerialPortWrapper(String commPortId, int baudRate, int dataBits, int stopBits, int parity,
			int timeout) {
		this.baudRate = baudRate;
		this.dataBits = dataBits;
		this.stopBits = stopBits;
		this.parity = parity;
		this.timeout = timeout;
		this.serialPort = SerialPort.getCommPort(commPortId);
	}

	@Override
	public void close() {
		serialPort.closePort();
	}

	@Override
	public void open() {
		serialPort.openPort();
		serialPort.setComPortParameters(baudRate, dataBits, stopBits, parity);
		serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, timeout, 0);
	}

	@Override
	public InputStream getInputStream() {
		return serialPort.getInputStream();
	}

	@Override
	public OutputStream getOutputStream() {
		return serialPort.getOutputStream();
	}

	@Override
	public int getBaudRate() {
		return this.baudRate;
	}

	@Override
	public int getDataBits() {
		return this.dataBits;
	}

	@Override
	public int getStopBits() {
		return this.stopBits;
	}

	@Override
	public int getParity() {
		return this.parity;
	}

}

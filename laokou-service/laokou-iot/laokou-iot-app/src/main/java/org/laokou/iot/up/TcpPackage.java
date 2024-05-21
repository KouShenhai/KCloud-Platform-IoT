/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.iot.up;

import io.netty.buffer.ByteBuf;
import org.laokou.iot.model.SensorA;

import java.util.HashMap;
import java.util.Map;

/**
 * 55 50 18 05 11 0E 34 11 79 00 9F 55 51 EE FF DA FF FF 07 6A 0C E8.
 *
 * @author laokou
 */
public abstract class TcpPackage {

	private static final Map<String, Object> MAP = new HashMap<>(16);

	public static void put(String key, Object value) {
		MAP.put(key, value);
	}

	public static Map<String, Object> getMap() {
		return MAP;
	}

	public static void clear() {
		MAP.clear();
	}

	public static final byte START_BIT = 0x55;

	abstract public void convert(ByteBuf buf, SensorA sensorA);

	protected static boolean crc(ByteBuf buf) {
		// 55 52 00 00 00 00 00 00 6A 0C 1D
		short start = buf.readUnsignedByte();
		short type = buf.readByte();
		short data1l = buf.readByte();
		short data1h = buf.readByte();
		short data2l = buf.readByte();
		short data2h = buf.readByte();
		short data3l = buf.readByte();
		short data3h = buf.readByte();
		short data4l = buf.readByte();
		short data4h = buf.readByte();
		short crc = buf.readUnsignedByte();
		// crc = 0x55+TYPE+DATA1L+DATA1H+DATA2L+DATA2H+DATA3L+DATA3H+DATA4L+DATA4H
		int sum = start + type + data1l + data1h + data2l + data2h + data3l + data3h + data4l + data4h;
		String hexSum = Integer.toHexString(sum);
		String hexCrc = Integer.toHexString(crc);
		return hexSum.contains(hexCrc);
	}

}

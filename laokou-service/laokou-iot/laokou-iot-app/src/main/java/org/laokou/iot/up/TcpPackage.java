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
import io.netty.util.ReferenceCountUtil;
import org.laokou.iot.model.SensorA;

/**
 * 55 50 18 05 11 0E 34 11 79 00 9F 55 51 EE FF DA FF FF 07 6A 0C E8.
 *
 * @author laokou
 */
public abstract class TcpPackage {

	public static final byte START_BIT = 0x55;

	abstract public void convert(ByteBuf buf, SensorA sensorA);

	public static boolean crc(ByteBuf buf) {
		// 55 52 00 00 00 00 00 00 6A 0C 1D
		short type = buf.skipBytes(1).readByte();
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
		int sum = START_BIT + type + data1l + data1h + data2l + data2h + data3l + data3h + data4l + data4h;
		String hexSum = Integer.toHexString(sum);
		String hexCrc = Integer.toHexString(crc);
		// 释放
		ReferenceCountUtil.release(buf);
		return hexSum.contains(hexCrc);
	}

}

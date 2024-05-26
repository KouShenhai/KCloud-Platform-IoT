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
import lombok.extern.slf4j.Slf4j;
import org.laokou.iot.model.SensorA;

/**
 * @author laokou
 */
@Slf4j
public class Up0x52 extends TcpPackage {

	@Override
	public void convert(ByteBuf buf, SensorA sensorA) {
		short data1l = buf.readByte();
		short data1h = buf.readByte();
		short data2l = buf.readByte();
		short data2h = buf.readByte();
		short data3l = buf.readByte();
		short data3h = buf.readByte();
		short data4l = buf.readUnsignedByte();
		short data4h = buf.readUnsignedByte();
		double wX = (double) ((data1h << 8) | data1l) / 32768 * 2000;
		double wY = (double) ((data2h << 8) | data2l) / 32768 * 2000;
		double wZ = (double) ((data3h << 8) | data3l) / 32768 * 2000;
		double vol = (double) ((data4h << 8) | data4l) / 100;
		sensorA.setWX(wX);
		sensorA.setWY(wY);
		sensorA.setWZ(wZ);
		sensorA.setVol(vol);
		// 跳过
		buf.skipBytes(1);
	}

}

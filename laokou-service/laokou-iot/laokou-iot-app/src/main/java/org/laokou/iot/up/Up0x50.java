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
 *
 * @author laokou
 */
@Slf4j
public class Up0x50 extends TcpPackage {

	@Override
	public void convert(ByteBuf buf, SensorA sensorA) {
		short data1l = buf.readByte();
		short data1h = buf.readByte();
		short data2l = buf.readByte();
		short data2h = buf.readByte();
		short data3l = buf.readByte();
		short data3h = buf.readByte();
		short data4l = buf.readByte();
		short data4h = buf.readByte();
		String year = "20" + data1l;
		String month = data1h > 9 ? "" + data1h : "0" + data1h;
		String day = data2l > 9 ? "" + data2l : "0" + data2l;
		String hour = data2h > 9 ? "" + data2h : "0" + data2h;
		String minute = data3l > 9 ? "" + data3l : "0" + data3l;
		String second = data3h > 9 ? "" + data3h : "0" + data3h;
		int msc = ((data4h << 8)|data4l);
		String millisecond;
		if (msc < 10) {
			millisecond = "00" + msc;
		} else if (msc < 100) {
			millisecond = "0" + msc;
		} else {
			millisecond = "" + msc;
		}
		String dateTime = String.format("%s-%s-%s %s:%s:%s.%s", year, month, day, hour, minute, second, millisecond);
		log.info(dateTime);
		// 跳过
		buf.skipBytes(1);
	}

}

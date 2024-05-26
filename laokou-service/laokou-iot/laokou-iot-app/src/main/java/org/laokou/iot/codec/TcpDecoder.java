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

package org.laokou.iot.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import org.laokou.iot.factory.PackageFactory;
import org.laokou.iot.model.SensorA;
import org.laokou.iot.up.TcpPackage;

import java.util.List;

/**
 * @author laokou
 */
@Slf4j
public class TcpDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
		SensorA sensorA = new SensorA();
		for (int i = 0; i < 5; i++) {
			ByteBuf tempBuf = Unpooled.copiedBuffer(in);
			if (!TcpPackage.crc(tempBuf)) {
				break;
			}
			byte type = in.skipBytes(1).readByte();
			PackageFactory.getType(type).convert(in, sensorA);
		}
		in.clear();
		out.add(sensorA);
	}

}

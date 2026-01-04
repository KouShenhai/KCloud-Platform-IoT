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

package org.laokou.common.redis.config;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import org.apache.fory.io.ForyStreamReader;
import org.apache.fory.memory.MemoryBuffer;
import org.apache.fory.memory.MemoryUtils;
import org.laokou.common.fory.config.ForyFactory;
import org.redisson.client.codec.BaseCodec;
import org.redisson.client.protocol.Decoder;
import org.redisson.client.protocol.Encoder;

/**
 * <a href="https://github.com/apache/fory">Apache Fory</a> codec
 * <p>
 * Fully thread-safe.
 *
 * @author Nikita Koksharov
 * @author laokou
 *
 */
final class ForyCodec extends BaseCodec {

	public static final ForyCodec INSTANCE = new ForyCodec();

	private final Decoder<Object> decoder = (buf, _) -> {
		if (buf.nioBufferCount() == 1) {
			MemoryBuffer furyBuffer = MemoryUtils.wrap(buf.nioBuffer());
			try {
				return ForyFactory.INSTANCE.getFory().deserialize(furyBuffer);
			}
			finally {
				buf.readerIndex(buf.readerIndex() + furyBuffer.readerIndex());
			}
		}
		else {
			return ForyFactory.INSTANCE.getFory().deserialize(ForyStreamReader.of(new ByteBufInputStream(buf)));
		}
	};

	private final Encoder encoder = in -> {
		ByteBuf out = ByteBufAllocator.DEFAULT.buffer();
		MemoryBuffer furyBuffer = null;
		int remainingSize = out.capacity() - out.writerIndex();
		if (out.hasArray()) {
			furyBuffer = MemoryUtils.wrap(out.array(), out.arrayOffset() + out.writerIndex(), remainingSize);
		}
		else if (out.hasMemoryAddress()) {
			furyBuffer = MemoryUtils.buffer(out.memoryAddress() + out.writerIndex(), remainingSize);
		}
		if (furyBuffer != null) {
			int size = furyBuffer.size();
			ForyFactory.INSTANCE.getFory().serialize(furyBuffer, in);
			if (furyBuffer.size() > size) {
				out.writeBytes(furyBuffer.getHeapMemory(), 0, furyBuffer.size());
			}
			else {
				out.writerIndex(out.writerIndex() + furyBuffer.writerIndex());
			}
			return out;
		}
		else {
			try {
				ByteBufOutputStream baos = new ByteBufOutputStream(out);
				ForyFactory.INSTANCE.getFory().serialize(baos, in);
				return baos.buffer();
			}
			catch (Exception e) {
				out.release();
				throw e;
			}

		}
	};

	@Override
	public Decoder<Object> getValueDecoder() {
		return decoder;
	}

	@Override
	public Encoder getValueEncoder() {
		return encoder;
	}

}

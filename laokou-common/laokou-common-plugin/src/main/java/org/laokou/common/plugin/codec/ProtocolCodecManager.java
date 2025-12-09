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

package org.laokou.common.plugin.codec;

import io.vertx.core.buffer.Buffer;
import org.laokou.common.plugin.codec.mqtt.MqttCodec;
import org.laokou.common.plugin.codec.tcp.TcpCodec;
import org.laokou.common.plugin.model.ProtocolType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author laokou
 */
public final class ProtocolCodecManager {

	private ProtocolCodecManager() {
		PROTOCOL_CODEC_MAP.put(ProtocolType.MQTT, new MqttCodec());
		PROTOCOL_CODEC_MAP.put(ProtocolType.TCP, new TcpCodec());
	}

	private static final Map<ProtocolType, ProtocolCodec<?>> PROTOCOL_CODEC_MAP = HashMap.newHashMap(2);

	/**
	 * 解码字节流.
	 * @param type 协议类型
	 * @param buffer 字节缓冲区
	 * @return 消息对象
	 */
	public static Object decode(ProtocolType type, Buffer buffer) {
		ProtocolCodec<?> codec = getCodec(type);
		if (codec == null) {
			throw new IllegalArgumentException("No codec found for protocol: " + type);
		}
		return codec.decode(buffer);
	}

	/**
	 * 编码消息.
	 * @param type 协议类型
	 * @param message 消息对象
	 * @return 字节缓冲区
	 */
	public static <T> Buffer encode(ProtocolType type, T message) {
		ProtocolCodec<T> codec = getCodec(type);
		if (codec == null) {
			throw new IllegalArgumentException("No codec found for protocol: " + type);
		}
		return codec.encode(message);
	}

	/**
	 * 获取编解码器.
	 * @param type 协议类型
	 * @return 编解码器
	 */
	@SuppressWarnings("unchecked")
	private static <T> ProtocolCodec<T> getCodec(ProtocolType type) {
		return (ProtocolCodec<T>) PROTOCOL_CODEC_MAP.get(type);
	}

}

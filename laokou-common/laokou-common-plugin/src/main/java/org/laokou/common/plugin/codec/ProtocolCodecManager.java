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

package org.laokou.common.plugin.codec;

import io.vertx.core.buffer.Buffer;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.plugin.model.ProtocolType;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 协议编解码器管理器.
 *
 * <p>
 * 支持动态注册/注销 {@link ProtocolCodec}，插件加载时调用 {@link #registerCodec(ProtocolCodec)} 将 Codec
 * 注入， 插件卸载时调用 {@link #unregisterCodec(ProtocolType)} 移除。
 * </p>
 *
 * @author laokou
 */
@Slf4j
public final class ProtocolCodecManager {

	private static final ConcurrentMap<ProtocolType, ProtocolCodec<?>> PROTOCOL_CODECS = new ConcurrentHashMap<>();

	private ProtocolCodecManager() {
	}

	/**
	 * 动态注册编解码器（幂等，重复注册时跳过并警告）.
	 * @param codec 编解码器实例
	 */
	public static void registerCodec(ProtocolCodec<?> codec) {
		ProtocolType type = codec.getProtocolType();
		ProtocolCodec<?> existing = PROTOCOL_CODECS.putIfAbsent(type, codec);
		if (existing != null) {
			log.warn("[编解码器] 已存在同类型编解码器，跳过注册: type={}", type);
		}
		else {
			log.info("[编解码器] 注册成功: type={}, codec={}", type, codec.getClass().getName());
		}
	}

	/**
	 * 动态注销编解码器.
	 * @param type 协议类型
	 */
	public static void unregisterCodec(ProtocolType type) {
		if (PROTOCOL_CODECS.remove(type) != null) {
			log.info("[编解码器] 注销成功: type={}", type);
		}
	}

	/**
	 * 解码字节流.
	 * @param type 协议类型
	 * @param buffer 字节缓冲区
	 * @return 消息对象
	 * @throws IllegalArgumentException 若未注册对应编解码器
	 */
	public static Object decode(ProtocolType type, Buffer buffer) {
		return getCodec(type).decode(buffer);
	}

	/**
	 * 编码消息.
	 * @param type 协议类型
	 * @param message 消息对象
	 * @return 字节缓冲区
	 * @throws IllegalArgumentException 若未注册对应编解码器
	 */
	public static <T> Buffer encode(ProtocolType type, T message) {
		ProtocolCodec<T> codec = getCodec(type);
		return codec.encode(message);
	}

	/**
	 * 检查是否已注册指定类型的编解码器.
	 * @param type 协议类型
	 * @return 是否存在
	 */
	public static boolean hasCodec(ProtocolType type) {
		return PROTOCOL_CODECS.containsKey(type);
	}

	@SuppressWarnings("unchecked")
	private static <T> ProtocolCodec<T> getCodec(ProtocolType type) {
		ProtocolCodec<?> codec = PROTOCOL_CODECS.get(type);
		if (codec == null) {
			throw new IllegalArgumentException("No codec registered for protocol: " + type);
		}
		return (ProtocolCodec<T>) codec;
	}

}

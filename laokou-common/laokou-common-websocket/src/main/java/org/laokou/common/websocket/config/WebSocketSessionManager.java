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

package org.laokou.common.websocket.config;

import io.netty.channel.Channel;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket会话管理器.
 * <p>
 * 使用ConcurrentHashMap保证线程安全，添加反向索引实现O(1)时间复杂度的Channel删除.
 *
 * @author laokou
 */
public final class WebSocketSessionManager {

	private WebSocketSessionManager() {
	}

	/**
	 * 客户端ID -> Channel集合映射.
	 */
	private static final Map<Long, Set<Channel>> CLIENT_CACHE = new ConcurrentHashMap<>(8192);

	/**
	 * Channel ID -> 客户端ID 反向索引，用于快速删除.
	 */
	private static final Map<String, Long> CHANNEL_TO_CLIENT = new ConcurrentHashMap<>(8192);

	/**
	 * 添加Channel到指定客户端.
	 * @param clientId 客户端ID
	 * @param channel Channel
	 */
	public static void add(Long clientId, Channel channel) {
		CLIENT_CACHE.computeIfAbsent(clientId, _ -> ConcurrentHashMap.newKeySet(256)).add(channel);
		CHANNEL_TO_CLIENT.put(channel.id().asLongText(), clientId);
	}

	/**
	 * 获取指定客户端的所有Channel.
	 * @param clientId 客户端ID
	 * @return Channel集合（不可变）
	 */
	public static Set<Channel> get(Long clientId) {
		return CLIENT_CACHE.getOrDefault(clientId, Collections.emptySet());
	}

	/**
	 * 移除指定Channel.
	 * @param channel Channel
	 */
	public static void remove(Channel channel) {
		String channelId = channel.id().asLongText();
		Long clientId = CHANNEL_TO_CLIENT.remove(channelId);
		if (clientId != null) {
			Set<Channel> channels = CLIENT_CACHE.get(clientId);
			if (channels != null) {
				channels.remove(channel);
				// 如果客户端没有任何Channel了，移除整个条目
				if (channels.isEmpty()) {
					CLIENT_CACHE.remove(clientId);
				}
			}
		}
	}

	/**
	 * 获取当前在线客户端数量.
	 * @return 客户端数量
	 */
	public static int getClientCount() {
		return CLIENT_CACHE.size();
	}

	/**
	 * 获取当前在线Channel总数.
	 * @return Channel总数
	 */
	public static int getChannelCount() {
		return CHANNEL_TO_CLIENT.size();
	}

}

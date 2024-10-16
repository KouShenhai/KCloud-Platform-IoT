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

package org.laokou.common.netty.config;

import io.netty.channel.Channel;
import org.laokou.common.i18n.utils.StringUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author laokou
 */
public final class WebSocketSessionManager {

	private static final Map<String, Channel> CLIENT_CACHE = new ConcurrentHashMap<>();

	private static final Map<String, String> CHANNEL_CACHE = new ConcurrentHashMap<>();

	public static void add(String clientId, Channel channel) {
		CLIENT_CACHE.put(clientId, channel);
		CHANNEL_CACHE.put(channel.id().asLongText(), clientId);
	}

	public static Channel get(String clientId) {
		return CLIENT_CACHE.get(clientId);
	}

	public static void remove(String channelId) {
		String clientId = CHANNEL_CACHE.get(channelId);
		if (StringUtil.isNotEmpty(clientId)) {
			CLIENT_CACHE.remove(clientId);
			CHANNEL_CACHE.remove(channelId);
		}
	}

}

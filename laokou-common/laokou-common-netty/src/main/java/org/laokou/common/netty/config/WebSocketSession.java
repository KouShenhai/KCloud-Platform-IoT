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

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.netty.channel.Channel;
import org.laokou.common.i18n.utils.StringUtil;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @author laokou
 */
public final class WebSocketSession {

	private static final Cache<String, Channel> CLIENT_CACHE = Caffeine.newBuilder()
		.expireAfterAccess(3600, SECONDS)
		.initialCapacity(1024)
		.build();

	private static final Cache<String, String> CHANNEL_CACHE = Caffeine.newBuilder()
		.expireAfterAccess(3600, SECONDS)
		.initialCapacity(1024)
		.build();

	public static void put(String clientId, Channel channel) {
		CLIENT_CACHE.put(clientId, channel);
		CHANNEL_CACHE.put(channel.id().asLongText(), clientId);
	}

	public static Channel get(String clientId) {
		return CLIENT_CACHE.getIfPresent(clientId);
	}

	public static void remove(String channelId) {
		String clientId = CHANNEL_CACHE.getIfPresent(channelId);
		if (StringUtil.isNotEmpty(clientId)) {
			CLIENT_CACHE.invalidate(clientId);
			CHANNEL_CACHE.invalidate(channelId);
		}
	}

}

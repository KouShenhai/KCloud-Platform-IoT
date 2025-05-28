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

package org.laokou.common.websocket.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author laokou
 */
public final class WebSocketSessionHeartBeatManager {

	private WebSocketSessionHeartBeatManager() {
	}

	private static final Map<String, AtomicInteger> HEART_BEAT_CACHE = new ConcurrentHashMap<>(8192);

	public static void increment(String channelId) {
		HEART_BEAT_CACHE.get(channelId).incrementAndGet();
	}

	public static void reset(String channelId) {
		HEART_BEAT_CACHE.get(channelId).set(0);
	}

	public static void remove(String channelId) {
		HEART_BEAT_CACHE.remove(channelId);
	}

	public static int get(String channelId) {
		return HEART_BEAT_CACHE
			.getOrDefault(channelId, HEART_BEAT_CACHE.computeIfAbsent(clientId, k -> new AtomicInteger(0)))
			.get();
	}

}

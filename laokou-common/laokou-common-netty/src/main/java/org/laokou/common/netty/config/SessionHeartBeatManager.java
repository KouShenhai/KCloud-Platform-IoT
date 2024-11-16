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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author laokou
 */
public class SessionHeartBeatManager {

	private static final Map<String, AtomicInteger> HEART_BEAT_MAP = new ConcurrentHashMap<>(4096);

	public static void incrementHeartBeat(String clientId) {
		HEART_BEAT_MAP.get(clientId).incrementAndGet();
	}

	public static void decrementHeartBeat(String clientId) {
		HEART_BEAT_MAP.get(clientId).decrementAndGet();
	}

	public static int getHeartBeat(String clientId) {
		return HEART_BEAT_MAP.get(clientId).get();
	}

	public static void removeHeartBeat(String clientId) {
		HEART_BEAT_MAP.remove(clientId);
	}

	public static void initHeartbeat(String clientId) {
		HEART_BEAT_MAP.putIfAbsent(clientId, new AtomicInteger(0));
	}

}

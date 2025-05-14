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

import io.netty.channel.Channel;
import org.laokou.common.i18n.util.ObjectUtils;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author laokou
 */
public final class WebSocketSessionManager {

	private WebSocketSessionManager() {
	}

	private static final Map<String, Set<Channel>> CLIENT_CACHE = new ConcurrentHashMap<>(8192);

	private static final ReentrantReadWriteLock READ_WRITE_LOCK = new ReentrantReadWriteLock();

	private static final Lock WRITE_LOCK = READ_WRITE_LOCK.writeLock();

	private static final Lock READ_LOCK = READ_WRITE_LOCK.readLock();

	public static void add(String clientId, Channel channel) throws InterruptedException {
		boolean isLocked = false;
		int retry = 10;
		try {
			do {
				isLocked = WRITE_LOCK.tryLock(50, TimeUnit.MILLISECONDS);
			}
			while (!isLocked && --retry > 0);
			if (isLocked) {
				CLIENT_CACHE.computeIfAbsent(clientId, k -> new HashSet<>(256)).add(channel);
			}
		}
		finally {
			if (isLocked) {
				WRITE_LOCK.unlock();
			}
		}
	}

	public static Set<Channel> get(String clientId) throws InterruptedException {
		boolean isLocked = false;
		int retry = 10;
		try {
			do {
				// 防止读到中间状态数据，保证读取的完整性【数据强一致性】
				// 在数据读取频繁的场景（如缓存、数据库查询），读锁允许大量读操作并行执行，避免线程因等待锁而阻塞
				isLocked = READ_LOCK.tryLock(50, TimeUnit.MILLISECONDS);
			}
			while (!isLocked && --retry > 0);
			if (isLocked) {
				return CLIENT_CACHE.getOrDefault(clientId, Collections.emptySet());
			}
			return Collections.emptySet();
		}
		finally {
			if (isLocked) {
				READ_LOCK.unlock();
			}
		}
	}

	public static void remove(Channel channel) throws InterruptedException {
		boolean isLocked = false;
		int retry = 10;
		try {
			do {
				isLocked = WRITE_LOCK.tryLock(50, TimeUnit.MILLISECONDS);
			}
			while (!isLocked && --retry > 0);
			if (isLocked) {
				String channelId = channel.id().asLongText();
				CLIENT_CACHE.values()
					.forEach(set -> set.removeIf(c -> ObjectUtils.equals(c.id().asLongText(), channelId)));
			}
		}
		finally {
			if (isLocked) {
				WRITE_LOCK.unlock();
			}
		}
	}

}

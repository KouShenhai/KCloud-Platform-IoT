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
import lombok.SneakyThrows;
import org.laokou.common.i18n.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author laokou
 */
public class SessionManager {

	private static final Map<String, Channel> CLIENT_CACHE = new HashMap<>(4096);

	private static final Map<String, String> CHANNEL_CACHE = new HashMap<>(4096);

	private static final ReentrantReadWriteLock READ_WRITE_LOCK = new ReentrantReadWriteLock();

	private static final Lock WRITE_LOCK = READ_WRITE_LOCK.writeLock();

	private static final Lock READ_LOCK = READ_WRITE_LOCK.readLock();

	@SneakyThrows
	public static void add(String clientId, Channel channel) {
		boolean isLocked = false;
		int retry = 3;
		try {
			do {
				isLocked = WRITE_LOCK.tryLock(50, TimeUnit.MILLISECONDS);
			}
			while (!isLocked && --retry > 0);
			if (isLocked) {
				CLIENT_CACHE.put(clientId, channel);
				CHANNEL_CACHE.put(channel.id().asLongText(), clientId);
			}
		}
		finally {
			if (isLocked) {
				WRITE_LOCK.unlock();
			}
		}
	}

	@SneakyThrows
	public static Channel get(String clientId) {
		boolean isLocked = false;
		int retry = 3;
		try {
			do {
				// 使用 读锁 可以在多个线程同时调用 getChannel 时避免阻塞，提高并发性能.
				isLocked = READ_LOCK.tryLock(50, TimeUnit.MILLISECONDS);
			}
			while (!isLocked && --retry > 0);
			if (isLocked) {
				return CLIENT_CACHE.get(clientId);
			}
			return null;
		}
		finally {
			if (isLocked) {
				READ_LOCK.unlock();
			}
		}
	}

	@SneakyThrows
	public static void remove(String channelId) {
		boolean isLocked = false;
		int retry = 3;
		try {
			do {
				isLocked = WRITE_LOCK.tryLock(50, TimeUnit.MILLISECONDS);
			}
			while (!isLocked && --retry > 0);
			if (isLocked) {
				String clientId = CHANNEL_CACHE.get(channelId);
				if (StringUtil.isNotEmpty(clientId)) {
					CLIENT_CACHE.remove(clientId);
					CHANNEL_CACHE.remove(channelId);
				}
			}
		}
		finally {
			if (isLocked) {
				WRITE_LOCK.unlock();
			}
		}
	}

}

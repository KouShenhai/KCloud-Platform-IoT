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

package org.laokou.common.core.context;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 优雅停机.
 *
 * @author laokou
 */
public final class ShutdownHolder {

	/**
	 * 请求计数器.
	 */
	private static final AtomicLong REQUEST_COUNTER = new AtomicLong(0);

	/**
	 * 优雅停机挡板.
	 */
	private static final AtomicBoolean BAFFLE = new AtomicBoolean(false);

	private ShutdownHolder() {
	}

	public static void add() {
		REQUEST_COUNTER.incrementAndGet();
	}

	public static void sub() {
		REQUEST_COUNTER.decrementAndGet();
	}

	public static boolean open() {
		return BAFFLE.compareAndSet(false, true);
	}

	public static boolean status() {
		return BAFFLE.get();
	}

	public static long get() {
		return REQUEST_COUNTER.get();
	}

}

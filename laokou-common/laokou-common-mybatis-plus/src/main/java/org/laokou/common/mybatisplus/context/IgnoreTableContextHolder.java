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

package org.laokou.common.mybatisplus.context;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * @author laokou
 */
public final class IgnoreTableContextHolder {

	private static final ThreadLocal<String> LOCAL = new TransmittableThreadLocal<>();

	private IgnoreTableContextHolder() {
	}

	public static void set(String tableName) {
		LOCAL.set(tableName);
	}

	public static void clear() {
		LOCAL.remove();
	}

	public static String get() {
		return LOCAL.get();
	}

}

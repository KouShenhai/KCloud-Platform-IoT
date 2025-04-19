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

package org.laokou.common.graalvmjs.config;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Value;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author laokou
 */
public class JsExecutor implements Executor {

	private volatile Context context;

	private final Object lock = new Object();

	private final Map<String, Value> cacheMap = new ConcurrentHashMap<>(4096);

	@Override
	public void init() {
		if (context == null) {
			synchronized (lock) {
				if (context == null) {
					context = Context.newBuilder("js")
						.allowHostAccess(HostAccess.ALL)
						.allowHostClassLookup(className -> true)
						.build();
				}
			}
		}
	}

	@Override
	public void close() {
		if (context != null) {
			context.close();
		}
	}

	@Override
	public Value execute(String script, Object... arguments) {
		return cacheMap.computeIfAbsent(script, k -> context.eval("js", script)).execute(arguments);
	}

}

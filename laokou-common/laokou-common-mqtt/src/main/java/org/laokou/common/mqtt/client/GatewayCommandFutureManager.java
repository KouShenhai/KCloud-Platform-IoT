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

package org.laokou.common.mqtt.client;

import org.laokou.common.mqtt.client.message.UpCommandReplyGatewayMessage;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public final class GatewayCommandFutureManager {

	private final Map<Long, CompletableFuture<UpCommandReplyGatewayMessage>> futures = new ConcurrentHashMap<>(8192);

	public CompletableFuture<UpCommandReplyGatewayMessage> put(Long id,
			CompletableFuture<UpCommandReplyGatewayMessage> future) {
		futures.put(id, future);
		return future;
	}

	public CompletableFuture<UpCommandReplyGatewayMessage> remove(Long id) {
		return futures.remove(id);
	}

	public void close() throws Exception {
		futures.values().forEach(future -> future.cancel(false));
		futures.clear();
	}

}

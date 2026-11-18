package org.laokou.common.mqtt.client;

import org.laokou.common.mqtt.client.message.UpCommandReplyGatewayMessage;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public final class GatewayCommandFutureManager {

	private final Map<Long, CompletableFuture<UpCommandReplyGatewayMessage>> futures = new ConcurrentHashMap<>(8192);

	public CompletableFuture<UpCommandReplyGatewayMessage> put(Long id) {
		return futures.put(id, new CompletableFuture<>());
	}

	public CompletableFuture<UpCommandReplyGatewayMessage> remove(Long id) {
		return futures.remove(id);
	}

	public void close() throws Exception {
		futures.values().forEach(future -> future.cancel(false));
		futures.clear();
	}

}

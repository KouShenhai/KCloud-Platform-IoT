package org.laokou.logstash.common.support;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class TraceLogLokiStorage extends AbstractTraceLogStorage {

	@Override
	public CompletableFuture<Void> batchSave(Map<String, Object> map) {
		return null;
	}

	@Override
	public CompletableFuture<Void> save(Object obj) {
		throw new UnsupportedOperationException();
	}

}

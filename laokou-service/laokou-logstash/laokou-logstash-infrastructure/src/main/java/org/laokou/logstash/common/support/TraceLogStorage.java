package org.laokou.logstash.common.support;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface TraceLogStorage {

	CompletableFuture<Void> batchSave(Map<String, Object> map);

	CompletableFuture<Void> save(Object obj);

}

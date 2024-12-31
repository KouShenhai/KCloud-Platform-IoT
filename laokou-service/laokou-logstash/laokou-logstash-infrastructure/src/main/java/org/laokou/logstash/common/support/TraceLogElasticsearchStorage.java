package org.laokou.logstash.common.support;

import lombok.RequiredArgsConstructor;
import org.laokou.common.elasticsearch.template.ElasticsearchTemplate;
import org.laokou.logstash.gateway.database.dataobject.TraceLogIndex;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class TraceLogElasticsearchStorage extends AbstractTraceLogStorage {

	private final ElasticsearchTemplate elasticsearchTemplate;


	@Override
	public CompletableFuture<Void> batchSave(Map<String, Object> map) {
		return elasticsearchTemplate.asyncCreateIndex(getIndexName(), TRACE_INDEX, TraceLogIndex.class, EXECUTOR)
			.thenAcceptAsync(
				res -> elasticsearchTemplate.asyncBulkCreateDocument(getIndexName(), map, EXECUTOR),
				EXECUTOR);
	}

	@Override
	public CompletableFuture<Void> save(Object obj) {
		throw new UnsupportedOperationException();
	}

}

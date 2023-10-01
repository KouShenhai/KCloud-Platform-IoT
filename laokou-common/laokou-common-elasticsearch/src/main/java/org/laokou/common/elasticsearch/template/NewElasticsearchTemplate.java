/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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
package org.laokou.common.elasticsearch.template;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.mapping.DynamicMapping;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import co.elastic.clients.util.ObjectBuilder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.elasticsearch.constant.EsConstant;
import org.laokou.common.elasticsearch.utils.FieldMapping;
import org.laokou.common.elasticsearch.utils.FieldMappingUtil;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NewElasticsearchTemplate {

	private final ElasticsearchClient elasticsearchClient;

	private static final String PRIMARY_KEY_NAME = "id";

	private static final String HIGHLIGHT_PRE_TAGS = "<span style='color:red;'>";

	private static final String HIGHLIGHT_POST_TAGS = "</span>";

	// /**
	// * 批量同步数据到ES
	// * @param indexName 索引名称
	// * @param jsonDataList 数据列表
	// * @throws IOException IOException
	// */
	// public Boolean syncBatchIndex(String indexName, String jsonDataList) throws
	// IOException {
	// // 判空
	// if (EmptyUtil.isEmpty(jsonDataList)) {
	// log.error("数据为空，无法批量同步数据");
	// return false;
	// }
	// // 判断索引是否存在
	// boolean indexExists = isIndexExists(indexName);
	// if (!indexExists) {
	// log.error("索引【{}】不存在，批量同步失败",indexName);
	// return false;
	// }
	// // 批量操作Request
	// BulkRequest bulkRequest = packBulkIndexRequest(indexName, jsonDataList);
	// if (bulkRequest.requests().isEmpty()) {
	// log.error("组件的数据为空，无法批量同步数据");
	// return false;
	// }
	// final BulkResponse bulk = elasticsearchClient.bulk(bulkRequest);
	// if (bulk.hasFailures()) {
	// for (BulkItemResponse item : bulk.getItems()) {
	// log.error("索引[{}],主键[{}]更新操作失败，状态为:[{}],错误信息:{}",indexName,item.getId(),item.status(),item.getFailureMessage());
	// }
	// return false;
	// }
	// // 记录索引新增与修改数量
	// Integer createdCount = 0;
	// Integer updatedCount = 0;
	// for (BulkItemResponse item : bulk.getItems()) {
	// if (DocWriteResponse.Result.CREATED.equals(item.getResponse().getResult())) {
	// createdCount++;
	// } else if (DocWriteResponse.Result.UPDATED.equals(item.getResponse().getResult())){
	// updatedCount++;
	// }
	// }
	// log.info("索引[{}]批量同步更新成功，共新增[{}]个，修改[{}]个",indexName,createdCount,updatedCount);
	// return true;
	// }
	//
	// /**
	// * 批量修改ES
	// * @param indexName 索引名称
	// * @param jsonDataList 数据列表
	// * @param clazz 类型
	// */
	// public Boolean updateBatchIndex(String indexName, String jsonDataList, Class<?>
	// clazz) {
	// //判空
	// if (EmptyUtil.isEmpty(jsonDataList)) {
	// log.error("数据为空，无法批量修改数据");
	// return false;
	// }
	// // 判断索引是否存在
	// boolean indexExists = isIndexExists(indexName);
	// if (!indexExists) {
	// log.error("索引【{}】不存在，批量修改失败",indexName);
	// return false;
	// }
	// BulkRequest bulkRequest = packBulkUpdateRequest(indexName, jsonDataList);
	// if (bulkRequest.requests().isEmpty()) {
	// log.error("组件的数据为空，无法批量修改数据");
	// return false;
	// }
	// try {
	// //同步执行
	// BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
	// if (bulk.hasFailures()) {
	// for (BulkItemResponse item : bulk.getItems()) {
	// log.error("索引【{}】,主键[{}]修改操作失败，状态为【{}】,错误信息：{}",indexName,item.getId(),item.status(),item.getFailureMessage());
	// }
	// return false;
	// }
	// //记录索引新增与修改数量
	// Integer createCount = 0;
	// Integer updateCount = 0;
	// for (BulkItemResponse item : bulk.getItems()) {
	// if (DocWriteResponse.Result.CREATED.equals(item.getResponse().getResult())) {
	// createCount++;
	// } else if (DocWriteResponse.Result.UPDATED.equals(item.getResponse().getResult()))
	// {
	// updateCount++;
	// }
	// }
	// log.info("索引【{}】批量修改更新成功，共新增[{}]个，修改[{}]个",indexName,createCount,updateCount);
	// } catch (IOException e) {
	// e.printStackTrace();
	// log.error("索引【{}】批量修改更新出现异常",indexName);
	// return false;
	// }
	// return true;
	// }
	//
	// /**
	// * ES修改数据
	// * @param indexName 索引名称
	// * @param id 主键
	// * @param paramJson 参数JSON
	// */
	// public Boolean updateIndex(String indexName, String id, String paramJson) {
	// // 判断索引是否存在
	// boolean indexExists = isIndexExists(indexName);
	// if (!indexExists) {
	// log.error("索引【{}】不存在，修改失败",indexName);
	// return false;
	// }
	// UpdateRequest updateRequest = new UpdateRequest(indexName, id);
	// //如果修改索引中不存在则进行新增
	// updateRequest.docAsUpsert(true);
	// //立即刷新数据
	// updateRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
	// updateRequest.doc(paramJson,XContentType.JSON);
	// try {
	// UpdateResponse updateResponse = restHighLevelClient.update(updateRequest,
	// RequestOptions.DEFAULT);
	// log.info("索引[{}]主键【{}】，操作结果:[{}]",indexName,id,updateResponse.getResult());
	// if (DocWriteResponse.Result.CREATED.equals(updateResponse.getResult())) {
	// //新增
	// log.info("索引【{}】主键【{}】，新增成功",indexName,id);
	// } else if (DocWriteResponse.Result.UPDATED.equals(updateResponse.getResult())) {
	// //修改
	// log.info("索引【{}】主键【{}】，修改成功",indexName, id);
	// } else if (DocWriteResponse.Result.NOOP.equals(updateResponse.getResult())) {
	// //无变化
	// log.info("索引[{}]主键[{}]，无变化",indexName, id);
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// log.error("索引[{}]主键【{}】，更新异常:[{}]",indexName, id,e);
	// }
	// return true;
	// }
	//
	// /**
	// * 删除数据
	// * @param indexName 索引名称
	// * @param id 主键
	// * @return Boolean
	// */
	// public Boolean deleteById(String indexName, String id) {
	// boolean indexExists = isIndexExists(indexName);
	// if (!indexExists) {
	// log.error("索引【{}】不存在，删除失败",indexName);
	// return false;
	// }
	// DeleteRequest deleteRequest = new DeleteRequest(indexName);
	// deleteRequest.id(id);
	// deleteRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
	// try {
	// DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest,
	// RequestOptions.DEFAULT);
	// if (DocWriteResponse.Result.NOT_FOUND.equals(deleteResponse.getResult())) {
	// log.error("索引【{}】主键【{}】删除失败",indexName, id);
	// } else {
	// log.info("索引【{}】主键【{}】删除成功",indexName, id);
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// log.error("删除索引【{}】出现异常[{}]",indexName,e);
	// }
	// return true;
	// }
	//
	// /**
	// * 批量删除ES
	// * @param indexName 索引名称
	// * @param ids id列表
	// * @return Boolean
	// */
	// public Boolean deleteBatchIndex(String indexName, List<String> ids) {
	// if (EmptyUtil.isEmpty(ids)) {
	// log.error("ids为空，不能批量删除数据");
	// return false;
	// }
	// // 判断索引是否存在
	// boolean indexExists = isIndexExists(indexName);
	// if (!indexExists) {
	// log.error("索引【{}】不存在，批量删除失败",indexName);
	// return false;
	// }
	// BulkRequest bulkRequest = packBulkDeleteRequest(indexName, ids);
	// try {
	// BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
	// if (bulk.hasFailures()) {
	// for (BulkItemResponse item : bulk.getItems()) {
	// log.error("删除索引:[{}],主键：{}失败，信息：{}",indexName,item.getId(),item.getFailureMessage());
	// }
	// return false;
	// }
	// // 记录索引新增与修改数量
	// Integer deleteCount = 0;
	// for (BulkItemResponse item : bulk.getItems()) {
	// if (DocWriteResponse.Result.DELETED.equals(item.getResponse().getResult())) {
	// deleteCount++;
	// }
	// }
	// log.info("批量删除索引[{}]成功，共删除[{}]个",indexName,deleteCount);
	// } catch (IOException e) {
	// e.printStackTrace();
	// log.error("删除索引：【{}】出现异常:{}",indexName,e);
	// }
	// return true;
	// }
	//
	// /**
	// * 组装删除操作
	// * @param indexName 索引名称
	// * @param ids id列表
	// * @return BulkRequest
	// */
	// private BulkRequest packBulkDeleteRequest(String indexName, List<String> ids) {
	// BulkRequest bulkRequest = new BulkRequest();
	// bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
	// ids.forEach(id -> {
	// DeleteRequest deleteRequest = new DeleteRequest(indexName);
	// deleteRequest.id(id);
	// bulkRequest.add(deleteRequest);
	// });
	// return bulkRequest;
	// }
	//
	// /**
	// * 组装bulkUpdate
	// * @param indexName 索引名称
	// * @param jsonDataList 数据列表
	// * @return BulkRequest
	// */
	// private BulkRequest packBulkUpdateRequest(String indexName,String jsonDataList) {
	// BulkRequest bulkRequest = new BulkRequest();
	// bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
	// List<Object> jsonList = JacksonUtil.toList(jsonDataList, Object.class);
	// if (jsonList.isEmpty()) {
	// return bulkRequest;
	// }
	// // 循环数据封装bulkRequest
	// jsonList.forEach(obj ->{
	// Map<String, Object> map = (Map<String, Object>) obj;
	// UpdateRequest updateRequest = new
	// UpdateRequest(indexName,map.get(PRIMARY_KEY_NAME).toString());
	// // 修改索引中不存在就新增
	// updateRequest.docAsUpsert(true);
	// updateRequest.doc(JacksonUtil.toJsonStr(obj), XContentType.JSON);
	// bulkRequest.add(updateRequest);
	// });
	// return bulkRequest;
	// }
	//
	// /**
	// * 根据主键查询ES
	// * @param indexName 索引名称
	// * @param id 主键
	// * @return String
	// */
	// public String getIndexById(String indexName,String id) {
	// // 判断索引是否存在
	// boolean indexExists = isIndexExists(indexName);
	// if (!indexExists) {
	// log.error("索引【{}】不存在，查询失败",indexName);
	// return null;
	// }
	// GetRequest getRequest = new GetRequest(indexName, id);
	// try {
	// GetResponse getResponse = restHighLevelClient.get(getRequest,
	// RequestOptions.DEFAULT);
	// String resultJson = getResponse.getSourceAsString();
	// log.info("索引【{}】主键【{}】，查询结果：【{}】",indexName,id,resultJson);
	// return resultJson;
	// } catch (IOException e) {
	// e.printStackTrace();
	// log.error("索引【{}】主键[{}]，查询异常：{}",indexName,id,e);
	// return null;
	// }
	// }
	//
	// /**
	// * 清空内容
	// * @param indexName 索引名称
	// */
	// public Boolean deleteAll(String indexName) {
	// // 判断索引是否存在
	// boolean indexExists = isIndexExists(indexName);
	// if (!indexExists) {
	// log.error("索引【{}】不存在，删除失败",indexName);
	// return false;
	// }
	// DeleteRequest deleteRequest = new DeleteRequest(indexName);
	// deleteRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
	// try {
	// DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest,
	// RequestOptions.DEFAULT);
	// if (DocWriteResponse.Result.NOT_FOUND.equals(deleteResponse.getResult())) {
	// log.error("索引【{}】删除失败",indexName);
	// return false;
	// }
	// log.info("索引【{}】删除成功",indexName);
	// } catch (IOException e) {
	// e.printStackTrace();
	// log.error("删除索引[{}]，出现异常[{}]",indexName,e);
	// }
	// return true;
	// }
	//
	// /**
	// * 批量数据保存到ES-异步
	// * @param indexName 索引名称
	// * @param jsonDataList 数据列表
	// */
	// public void syncAsyncBatchIndex(String indexName, String jsonDataList) {
	// // 判空
	// if (EmptyUtil.isEmpty(jsonDataList)) {
	// log.error("数据为空，无法批量异步同步数据");
	// return;
	// }
	// // 判断索引是否存在
	// boolean indexExists = isIndexExists(indexName);
	// if (!indexExists) {
	// log.error("索引【{}】不存在，批量异步同步失败",indexName);
	// return;
	// }
	// // 批量操作Request
	// BulkRequest bulkRequest = packBulkIndexRequest(indexName, jsonDataList);
	// if (bulkRequest.requests().isEmpty()) {
	// log.error("组装数据为空，无法批量异步同步数据");
	// return;
	// }
	// // 异步执行
	// ActionListener<BulkResponse> listener = new ActionListener<>() {
	// @Override
	// public void onResponse(BulkResponse bulkItemResponses) {
	// if (bulkItemResponses.hasFailures()) {
	// for (BulkItemResponse item : bulkItemResponses.getItems()) {
	// log.error("索引【{}】,主键【{}】更新失败，状态【{}】，错误信息：{}", indexName, item.getId(),
	// item.status(), item.getFailureMessage());
	// }
	// }
	// }
	//
	// // 失败操作
	// @Override
	// public void onFailure(Exception e) {
	// log.error("索引【{}】批量异步更新出现异常:{}", indexName, e);
	// }
	// };
	// restHighLevelClient.bulkAsync(bulkRequest,RequestOptions.DEFAULT,listener);
	// log.info("索引批量更新索引【{}】中",indexName);
	// }
	//
	// /**
	// * 删除索引
	// * @param indexName 索引名称
	// * @return Boolean
	// * @throws IOException IOException
	// */
	// public Boolean deleteIndex(String indexName) throws IOException {
	// // 判断索引是否存在
	// boolean indexExists = isIndexExists(indexName);
	// if (!indexExists) {
	// log.error("索引【{}】不存在，删除失败",indexName);
	// return false;
	// }
	// // 删除操作Request
	// DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(indexName);
	// deleteIndexRequest.indicesOptions(IndicesOptions.LENIENT_EXPAND_OPEN);
	// AcknowledgedResponse acknowledgedResponse =
	// restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
	// if (!acknowledgedResponse.isAcknowledged()) {
	// log.error("索引【{}】删除失败",indexName);
	// return false;
	// }
	// log.info("索引【{}】删除成功",indexName);
	// return true;
	// }
	//
	// /**
	// * 异步删除索引
	// * @param indexName 索引名称
	// */
	// public void deleteAsyncIndex(String indexName) {
	// boolean indexExists = isIndexExists(indexName);
	// if (!indexExists) {
	// log.error("索引【{}】不存在，异步删除失败",indexName);
	// return;
	// }
	// DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(indexName);
	// deleteIndexRequest.indicesOptions(IndicesOptions.LENIENT_EXPAND_OPEN);
	// ActionListener<AcknowledgedResponse> listener = new ActionListener<>() {
	// @Override
	// public void onResponse(AcknowledgedResponse acknowledgedResponse) {
	// if (acknowledgedResponse.isAcknowledged()) {
	// log.info("索引【{}】删除成功", indexName);
	// } else {
	// log.error("索引【{}】删除失败", indexName);
	// }
	// }
	//
	// @Override
	// public void onFailure(Exception e) {
	// log.error("索引【{}】删除失败，失败信息:{}", indexName, e);
	// }
	// };
	// restHighLevelClient.indices().deleteAsync(deleteIndexRequest,RequestOptions.DEFAULT,listener);
	// }
	// /**
	// * 批量操作的Request
	// * @param indexName 索引名称
	// * @param jsonDataList json数据列表
	// * @return BulkRequest
	// */
	// private BulkRequest packBulkIndexRequest(String indexName,String jsonDataList) {
	// BulkRequest bulkRequest = new BulkRequest();
	// // IMMEDIATE > 请求向es提交数据，立即进行数据刷新<实时性高，资源消耗高>
	// // WAIT_UNTIL > 请求向es提交数据，等待数据完成刷新<实时性高，资源消耗低>
	// // NONE > 默认策略<实时性低>
	// bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
	// List<Object> jsonList = JacksonUtil.toList(jsonDataList, Object.class);
	// if (jsonList.isEmpty()) {
	// return bulkRequest;
	// }
	// // 循环数据封装bulkRequest
	// jsonList.forEach(obj ->{
	// Map<String, Object> map = (Map<String, Object>) obj;
	// IndexRequest indexRequest = new IndexRequest(indexName);
	// indexRequest.source(JacksonUtil.toJsonStr(obj),XContentType.JSON);
	// indexRequest.id(map.get(PRIMARY_KEY_NAME).toString());
	// bulkRequest.add(indexRequest);
	// });
	// return bulkRequest;
	// }
	//
	//
	// /**
	// * 异步创建ES索引
	// * @param indexName 索引名称
	// * @param indexAlias 别名
	// * @param clazz 类型
	// * @throws IOException IOException
	// */
	// public void createAsyncIndex(String indexName,String indexAlias, Class<?> clazz)
	// throws IOException {
	// boolean indexExists = isIndexExists(indexName);
	// if (indexExists) {
	// log.error("索引【{}】已存在，异步创建失败",indexName);
	// return;
	// }
	// CreateIndexRequest createIndexRequest = getCreateIndexRequest(indexName,
	// indexAlias, FieldMappingUtil.getFieldInfo(clazz));
	// // 异步方式创建索引
	// ActionListener<CreateIndexResponse> listener = new ActionListener<>() {
	// @Override
	// public void onResponse(CreateIndexResponse createIndexResponse) {
	// boolean acknowledged = createIndexResponse.isAcknowledged();
	// if (acknowledged) {
	// log.info("索引【{}】创建成功", indexName);
	// } else {
	// log.error("索引【{}】创建失败", indexName);
	// }
	// }
	// @Override
	// public void onFailure(Exception e) {
	// log.error("索引【{}】创建失败，错误信息:{}", indexName, e);
	// }
	// };
	// // 异步执行
	// restHighLevelClient.indices().createAsync(createIndexRequest,
	// RequestOptions.DEFAULT, listener);
	// }
	//
	// private CreateIndexRequest getCreateIndexRequest(String indexName, String
	// indexAlias, List<FieldMapping> fieldMappingList) throws IOException {
	// // 封装es索引的mapping
	// XContentBuilder mapping = packEsMapping(fieldMappingList);
	// mapping.endObject().endObject();
	// mapping.close();
	// // 索引创建
	// CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
	// // 配置分词器
	// XContentBuilder settings = packSettingMapping();
	// // 别名
	// XContentBuilder aliases = packEsAliases(indexAlias);
	// createIndexRequest.settings(settings);
	// createIndexRequest.mapping(mapping);
	// createIndexRequest.aliases(aliases);
	// return createIndexRequest;
	// }
	//
	// /**
	// * 数据同步到ES
	// * @param id 主键
	// * @param indexName 索引名称
	// * @param jsonData json数据
	// */
	// public Boolean syncIndex(String id, String indexName, String jsonData) throws
	// IOException {
	// // 判断索引是否存在
	// boolean indexExists = isIndexExists(indexName);
	// if (!indexExists) {
	// log.error("索引【{}】不存在，同步失败",indexName);
	// return false;
	// }
	// // 创建操作Request
	// IndexRequest indexRequest = new IndexRequest(indexName);
	// // 配置相关信息
	// indexRequest.source(jsonData, XContentType.JSON);
	// // IMMEDIATE > 立即刷新
	// indexRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
	// indexRequest.id(id);
	// IndexResponse response = restHighLevelClient.index(indexRequest,
	// RequestOptions.DEFAULT);
	// // 判断索引是新增还是修改
	// if (IndexResponse.Result.CREATED.equals(response.getResult())) {
	// log.info("索引【{}】保存成功",indexName);
	// } else if (IndexResponse.Result.UPDATED.equals(response.getResult())) {
	// log.info("索引【{}】修改成功",indexName);
	// }
	// return true;
	// }
	//
	// /**
	// * 异步同步
	// * @param id 编号
	// * @param indexName 索引名称
	// * @param jsonData 数据
	// */
	// public void syncIndexAsync(String id, String indexName, String jsonData) {
	// // 判断索引是否存在
	// boolean indexExists = isIndexExists(indexName);
	// if (!indexExists) {
	// log.error("索引【{}】不存在，异步同步失败",indexName);
	// return;
	// }
	// // 创建操作Request
	// IndexRequest indexRequest = new IndexRequest(indexName);
	// // 配置相关信息
	// indexRequest.source(jsonData, XContentType.JSON);
	// // IMMEDIATE > 立即刷新
	// indexRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
	// indexRequest.id(id);
	// ActionListener<IndexResponse> actionListener = new ActionListener<>() {
	// @Override
	// public void onResponse(IndexResponse indexResponse) {
	// if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
	// log.info("索引【{}】异步同步成功", indexName);
	// } else {
	// log.error("索引【{}】异步同步失败", indexName);
	// }
	// }
	//
	// @Override
	// public void onFailure(Exception e) {
	// log.error("索引【{}】异步同步出现异常:{}", indexName, e);
	// }
	// };
	// restHighLevelClient.indexAsync(indexRequest, RequestOptions.DEFAULT,
	// actionListener);
	// }

	/**
	 * 索引是否存在
	 * @param indexNames 索引集合名称
	 * @return boolean
	 */
	public boolean isIndexExists(List<String> indexNames) {
		try {
			ExistsRequest existsRequest = ExistsRequest.of(exists -> exists.index(indexNames));
			return elasticsearchClient.indices().exists(existsRequest).value();
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 索引是否存在
	 * @param indexName 索引名称
	 * @return boolean
	 */
	public boolean isIndexExists(String indexName) {
		return isIndexExists(List.of(indexName));
	}

	/**
	 * <a href=
	 * "https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/8.6/package-structure.html">...</a>
	 * 创建ES索引
	 * @param indexName 索引名称
	 */
	@SneakyThrows
	public <TDocument> void createIndex(String indexName, String indexAlias, Class<TDocument> clazz) {
		// 判断索引是否存在
		boolean indexExists = isIndexExists(indexName);
		if (indexExists) {
			log.error("索引【{}】已存在，创建失败", indexName);
			return;
		}
		elasticsearchClient.indices()
			.create(request -> request.index(indexName)
				.aliases(indexAlias, fn -> fn.isWriteIndex(true))
				.mappings(getMapping(clazz)));
	}

	private <TDocument> TypeMapping getMapping(Class<TDocument> clazz) {
		List<FieldMapping> fieldInfo = FieldMappingUtil.getFieldInfo(clazz);
		TypeMapping.Builder builder = new TypeMapping.Builder();
		builder.dynamic(DynamicMapping.True);
		fieldInfo.forEach(item -> properties(builder, item));
		return builder.build();
	}

	private void properties(TypeMapping.Builder builder, FieldMapping mapping) {
		String field = mapping.getField();
		String type = mapping.getType();
		Integer participle = mapping.getParticiple();
		if (EsConstant.IK_INDEX.equals(participle)) {
			builder.properties(field, fn -> fn.text(t -> t.searchAnalyzer("ik_smart").analyzer("ik_max_word")));
		}
		else {
			builder.properties(field, fn -> fn.keyword(k -> k));
		}
	}

	// /**
	// * 创建索引设置相关配置信息
	// * @param indexName 索引名称
	// * @param indexAlias 索引别名
	// * @param fieldMappingList 数据列表
	// * @return Boolean
	// * @throws IOException IOException
	// */
	// private Boolean createIndexAndCreateMapping(String indexName,String indexAlias,
	// List<FieldMapping> fieldMappingList) throws IOException {
	// // 封装es索引的mapping
	// CreateIndexRequest createIndexRequest = getCreateIndexRequest(indexName,
	// indexAlias, fieldMappingList);
	// // 同步方式创建索引
	// CreateIndexResponse createIndexResponse =
	// restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
	// boolean acknowledged = createIndexResponse.isAcknowledged();
	// if (acknowledged) {
	// log.info("索引:{}创建成功", indexName);
	// return true;
	// } else {
	// log.error("索引:{}创建失败", indexName);
	// return false;
	// }
	// }
	//
	// /**
	// * 配置ES别名
	// * @author laokou
	// * @param alias 别名
	// * @return XContentBuilder
	// * @throws IOException IOException
	// */
	// private XContentBuilder packEsAliases(String alias) throws IOException{
	// XContentBuilder aliases = XContentFactory.jsonBuilder().startObject()
	// .startObject(alias)
	// .field("is_write_index",false)
	// .endObject();
	// aliases.endObject();
	// aliases.close();
	// return aliases;
	// }
	//
	// /**
	// * 配置Mapping
	// * @param fieldMappingList 组装的实体类信息
	// * @return XContentBuilder
	// * @throws IOException IOException
	// */
	// private XContentBuilder packEsMapping(List<FieldMapping> fieldMappingList) throws
	// IOException {
	// XContentBuilder mapping = XContentFactory.jsonBuilder().startObject()
	// .field("dynamic",true)
	// .startObject("properties");
	// // 循环实体对象的类型集合封装ES的Mapping
	// for (FieldMapping fieldMapping : fieldMappingList) {
	// String field = fieldMapping.getField();
	// String dataType = fieldMapping.getType();
	// Integer participle = fieldMapping.getParticiple();
	// // 设置分词规则
	// if (EsConstant.NOT_ANALYZED.equals(participle)) {
	// if ("date".equals(dataType)) {
	// mapping.startObject(field)
	// .field("type", dataType)
	// .field("format","yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis")
	// .endObject();
	// } else {
	// mapping.startObject(field)
	// .field("type", dataType)
	// .endObject();
	// }
	// } else if (EsConstant.IK_INDEX.equals(participle)) {
	// mapping.startObject(field)
	// .field("type",dataType)
	// .field("eager_global_ordinals",true)
	// // fielddata=true 用来解决text字段不能进行聚合操作
	// .field("fielddata",true)
	// .field("analyzer","ik_pinyin")
	// .field("search_analyzer","ik_max_word")
	// .endObject();
	// }
	// }
	// return mapping;
	// }
	//
	// /**
	// * 配置Settings
	// * @return XContentBuilder
	// * @throws IOException IOException
	// */
	// private XContentBuilder packSettingMapping() throws IOException {
	// XContentBuilder setting = XContentFactory.jsonBuilder().startObject()
	// .startObject("index")
	// .field("number_of_shards",1)
	// .field("number_of_replicas",1)
	// .field("refresh_interval","30s")
	// .startObject("analysis");
	// // ik分词拼音
	// setting.startObject("analyzer")
	// .startObject("ik_pinyin")
	// .field("tokenizer","ik_max_word")
	// .field("filter", "laokou_pinyin")
	// .endObject();
	// setting.endObject();
	// // 设置拼音分词器分词
	// setting.startObject("filter")
	// .startObject("laokou_pinyin")
	// .field("type", "pinyin")
	// // 不会这样划分：刘德华 > [liu,de,hua]
	// .field("keep_full_pinyin", false)
	// // 这样划分：刘德华 > [liudehua]
	// .field("keep_joined_full_pinyin",true)
	// // 保留原始中文
	// .field("keep_original", true)
	// .field("limit_first_letter_length", 16)
	// .field("remove_duplicated_term", true)
	// .field("none_chinese_pinyin_tokenize", false)
	// .endObject()
	// .endObject();
	// setting.endObject().endObject().endObject();
	// setting.close();
	// return setting;
	// }
	//
	// /**
	// * 关键字高亮显示
	// * @param searchQo 查询实体类
	// * @return SearchVO
	// */
	// public SearchVO<Map<String,Object>> highlightSearchIndex(SearchQo searchQo) {
	// try {
	// final String[] indexNames = searchQo.getIndexNames();
	// // 用于搜索文档，聚合，定制查询有关操作
	// SearchRequest searchRequest = new SearchRequest();
	// searchRequest.indices(indexNames);
	// searchRequest.source(buildSearchSource(searchQo,true,null));
	// SearchHits hits = restHighLevelClient.search(searchRequest,
	// RequestOptions.DEFAULT).getHits();
	// List<Map<String,Object>> data = new ArrayList<>();
	// for (SearchHit hit : hits){
	// Map<String,Object> sourceData = hit.getSourceAsMap();
	// Map<String, HighlightField> highlightFields = hit.getHighlightFields();
	// for (String key : highlightFields.keySet()){
	// sourceData.put(key,highlightFields.get(key).getFragments()[0].string());
	// }
	// data.add(sourceData);
	// }
	// SearchVO<Map<String,Object>> vo = new SearchVO<>();
	// final long total = hits.getTotalHits().value;
	// vo.setRecords(data);
	// vo.setTotal(total);
	// vo.setPageNum(searchQo.getPageNum());
	// vo.setPageSize(searchQo.getPageSize());
	// return vo;
	// } catch (Exception e) {
	// throw new CustomException("搜索失败");
	// }
	// }
	//
	// /**
	// * 构建query
	// * @param searchQo 查询参数
	// * @return BoolQueryBuilder
	// */
	// private BoolQueryBuilder buildBoolQuery(SearchQo searchQo) {
	// BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
	// // 分词查询
	// final List<SearchDTO> queryStringList = searchQo.getQueryStringList();
	// // or查询
	// final List<SearchDTO> orSearchList = searchQo.getOrSearchList();
	// if (EmptyUtil.isNotEmpty(orSearchList)) {
	// // or查询
	// BoolQueryBuilder orQuery = QueryBuilders.boolQuery();
	// for (SearchDTO dto : orSearchList) {
	// orQuery.should(QueryBuilders.termQuery(dto.getField(), dto.getValue()));
	// }
	// boolQueryBuilder.must(orQuery);
	// }
	// if (EmptyUtil.isNotEmpty(queryStringList)) {
	// // 分词查询
	// BoolQueryBuilder analysisQuery = QueryBuilders.boolQuery();
	// for (SearchDTO dto : queryStringList) {
	// final String field = dto.getField();
	// // 清除左右空格并处理特殊字符
	// final String keyword = QueryParser.escape(dto.getValue().trim());
	// analysisQuery.should(QueryBuilders.queryStringQuery(keyword).field(field));
	// }
	// boolQueryBuilder.must(analysisQuery);
	// }
	// return boolQueryBuilder;
	// }
	//
	// /**
	// * 构建搜索
	// * @param searchQo 查询参数
	// * @param isHighlightSearchFlag 是否高亮搜索
	// * @param aggregationBuilder 聚合参数
	// * @return SearchSourceBuilder
	// */
	// private SearchSourceBuilder buildSearchSource(SearchQo searchQo, boolean
	// isHighlightSearchFlag, TermsAggregationBuilder aggregationBuilder) {
	// SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
	// final Integer pageNum = searchQo.getPageNum();
	// final Integer pageSize = searchQo.getPageSize();
	// final List<SearchDTO> sortFieldList = searchQo.getSortFieldList();
	// if (isHighlightSearchFlag) {
	// final List<String> highlightFieldList = searchQo.getHighlightFieldList();
	// // 高亮显示数据
	// HighlightBuilder highlightBuilder = new HighlightBuilder();
	// // 设置关键字显示颜色
	// highlightBuilder.preTags(HIGHLIGHT_PRE_TAGS);
	// highlightBuilder.postTags(HIGHLIGHT_POST_TAGS);
	// //设置显示的关键字
	// for (String field : highlightFieldList) {
	// highlightBuilder.field(field, 0, 0);
	// }
	// // 多个字段高亮,这项要为false
	// highlightBuilder.requireFieldMatch(false);
	// // 设置高亮
	// searchSourceBuilder.highlighter(highlightBuilder);
	// }
	// // 分页
	// if (searchQo.isNeedPage()) {
	// final int pageIndex = (pageNum - 1) * pageSize;
	// searchSourceBuilder.from(pageIndex);
	// searchSourceBuilder.size(pageSize);
	// }
	// // 追踪分数开启
	// searchSourceBuilder.trackScores(true);
	// // 注解
	// searchSourceBuilder.explain(true);
	// // 匹配度倒排，数值越大匹配度越高
	// searchSourceBuilder.sort("_score",SortOrder.DESC);
	// // 排序
	// if (EmptyUtil.isNotEmpty(sortFieldList)) {
	// for (SearchDTO dto : sortFieldList) {
	// SortOrder sortOrder;
	// final String desc = "desc";
	// final String value = dto.getValue();
	// final String field = dto.getField();
	// if (desc.equalsIgnoreCase(value)) {
	// sortOrder = SortOrder.DESC;
	// } else {
	// sortOrder = SortOrder.ASC;
	// }
	// searchSourceBuilder.sort(field, sortOrder);
	// }
	// }
	// searchSourceBuilder.query(buildBoolQuery(searchQo));
	// //获取真实总数
	// searchSourceBuilder.trackTotalHits(true);
	// //聚合对象
	// if (null != aggregationBuilder) {
	// searchSourceBuilder.aggregation(aggregationBuilder);
	// }
	// return searchSourceBuilder;
	// }
	//
	// /**
	// * 聚合查询
	// * @param searchQo 搜索
	// * @return SearchVO
	// * @throws IOException IOException
	// */
	// public SearchVO<Map<String,Long>> aggregationSearchIndex(SearchQo searchQo) throws
	// IOException {
	// SearchVO<Map<String,Long>> vo = new SearchVO();
	// List<Map<String,Long>> list = new ArrayList<>(5);
	// String[] indexNames = searchQo.getIndexNames();
	// AggregationDTO aggregationKey = searchQo.getAggregationKey();
	// String field = aggregationKey.getField();
	// String groupKey = aggregationKey.getGroupKey();
	// String script = aggregationKey.getScript();
	// TermsAggregationBuilder aggregationBuilder;
	// if (EmptyUtil.isNotEmpty(field)) {
	// aggregationBuilder = AggregationBuilders.terms(groupKey).field(field).size(100000);
	// } else {
	// aggregationBuilder = AggregationBuilders.terms(groupKey).script(new
	// Script(script)).size(100000);
	// }
	// // 用于搜索文档，聚合，定制查询有关操作
	// SearchRequest searchRequest = new SearchRequest();
	// searchRequest.indices(indexNames);
	// searchRequest.source(buildSearchSource(searchQo, false, aggregationBuilder));
	// SearchResponse searchResponse = restHighLevelClient.search(searchRequest,
	// RequestOptions.DEFAULT);
	// Aggregations aggregations = searchResponse.getAggregations();
	// Terms aggregation = aggregations.get(groupKey);
	// List<? extends Terms.Bucket> buckets = aggregation.getBuckets();
	// for (Terms.Bucket bucket : buckets) {
	// Map<String,Long> dataMap = new HashMap<>(1);
	// dataMap.put(bucket.getKeyAsString(),bucket.getDocCount());
	// list.add(dataMap);
	// }
	// vo.setRecords(list);
	// vo.setPageNum(searchQo.getPageNum());
	// vo.setPageSize(searchQo.getPageSize());
	// vo.setTotal((long) list.size());
	// return vo;
	// }

}

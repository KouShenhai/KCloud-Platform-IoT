/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 *   http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laokou.elasticsearch.server.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.laokou.common.i18n.core.HttpResult;
import org.laokou.elasticsearch.client.qo.SearchQo;
import org.laokou.elasticsearch.client.dto.CreateIndexDTO;
import org.laokou.elasticsearch.client.dto.ElasticsearchDTO;
import org.laokou.elasticsearch.client.vo.SearchVO;
import org.laokou.elasticsearch.client.utils.ElasticsearchFieldUtil;
import org.laokou.elasticsearch.server.support.ElasticsearchTemplate;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;
/**
 * @author laokou
 */
@RestController
@RequestMapping("/api")
@Tag(name = "Elasticsearch API",description = "分布式搜索API")
@RequiredArgsConstructor
public class ElasticsearchApiController {
    private final ElasticsearchTemplate elasticsearchTemplate;

    @PostMapping("/create")
    @Operation(summary = "分布式搜索>创建索引",description = "分布式搜索>创建索引")
    public HttpResult<Boolean> create(@RequestBody final CreateIndexDTO model) throws IOException {
        String indexName = model.getIndexName();
        String indexAlias = model.getIndexAlias();
        Class<?> clazz = ElasticsearchFieldUtil.getClazz(indexAlias);
        return new HttpResult<Boolean>().ok(elasticsearchTemplate.createIndex(indexName,indexAlias,clazz));
    }

    @PostMapping("/createAsync")
    @Operation(summary = "分布式搜索>异步创建索引",description = "分布式搜索>异步创建索引")
    public void createAsync(@RequestBody final CreateIndexDTO model) throws IOException {
        String indexName = model.getIndexName();
        String indexAlias = model.getIndexAlias();
        Class<?> clazz = ElasticsearchFieldUtil.getClazz(indexAlias);
        elasticsearchTemplate.createAsyncIndex(indexName,indexAlias,clazz);
    }

    @PostMapping("/sync")
    @Operation(summary = "分布式搜索>同步索引",description = "分布式搜索>同步索引")
    public HttpResult<Boolean> sync(@RequestBody final ElasticsearchDTO model) throws IOException {
        String id = model.getId();
        String indexName = model.getIndexName();
        String indexAlias = model.getIndexAlias();
        String jsonData = model.getData();
        Class<?> clazz = ElasticsearchFieldUtil.getClazz(indexAlias);
        return new HttpResult<Boolean>().ok(elasticsearchTemplate.syncIndex(id,indexName,indexAlias,jsonData,clazz));
    }

    @PostMapping("/syncAsyncBatch")
    @Operation(summary = "分布式搜索>批量异步同步索引",description = "分布式搜索>批量异步同步索引")
    public void syncAsyncBatch(@RequestBody final ElasticsearchDTO model) {
        String indexName = model.getIndexName();
        String jsonDataList = model.getData();
        elasticsearchTemplate.syncAsyncBatchIndex(indexName,jsonDataList);
    }

    @PostMapping("/syncBatch")
    @Operation(summary = "分布式搜索>批量同步索引",description = "分布式搜索>批量同步索引")
    public HttpResult<Boolean> syncBatch(@RequestBody final ElasticsearchDTO model) throws IOException {
        String indexName = model.getIndexName();
        String jsonDataList = model.getData();
        return new HttpResult<Boolean>().ok(elasticsearchTemplate.syncBatchIndex(indexName,jsonDataList));
    }

    @GetMapping("/detail")
    @Operation(summary = "分布式搜索>查看",description = "分布式搜索>查看")
    public HttpResult<String> detail(@RequestParam("indexName")final String indexName, @RequestParam("id")final String id) {
        return new HttpResult<String>().ok(elasticsearchTemplate.getIndexById(indexName,id));
    }

    @PutMapping("/updateBatch")
    @Operation(summary = "分布式搜索>批量修改",description = "分布式搜索>批量修改")
    public HttpResult<Boolean> updateBatch(@RequestBody final ElasticsearchDTO model) throws IOException {
        String indexName = model.getIndexName();
        String indexAlias = model.getIndexAlias();
        String jsonDataList = model.getData();
        Class<?> clazz = ElasticsearchFieldUtil.getClazz(indexAlias);
        return new HttpResult<Boolean>().ok(elasticsearchTemplate.updateBatchIndex(indexName,indexAlias,jsonDataList,clazz));
    }

    @PutMapping("/update")
    @Operation(summary = "分布式搜索>修改",description = "分布式搜索>修改")
    public HttpResult<Boolean> update(@RequestBody final ElasticsearchDTO model) {
        String id = model.getId();
        String indexName = model.getIndexName();
        String paramJson = model.getData();
        return new HttpResult<Boolean>().ok(elasticsearchTemplate.updateIndex(indexName,id,paramJson));
    }

    @DeleteMapping("/deleteBatch")
    @Operation(summary = "分布式搜索>批量删除",description = "分布式搜索>批量删除")
    public HttpResult<Boolean> deleteBatch(@RequestParam("indexName")final String indexName, @RequestParam("ids")final List<String> ids) {
        return new HttpResult<Boolean>().ok(elasticsearchTemplate.deleteBatchIndex(indexName,ids));
    }

    @DeleteMapping("/deleteById")
    @Operation(summary = "分布式搜索>删除",description = "分布式搜索>删除")
    public HttpResult<Boolean> deleteById(@RequestParam("indexName")final String indexName, @RequestParam("id")final String id) {
        return new HttpResult<Boolean>().ok(elasticsearchTemplate.deleteById(indexName,id));
    }

    @DeleteMapping("/deleteAsync")
    @Operation(summary = "分布式搜索>异步删除",description = "分布式搜索>异步删除")
    public void deleteAsync(@RequestParam("indexName")final String indexName) {
        elasticsearchTemplate.deleteAsyncIndex(indexName);
    }

    @DeleteMapping("/deleteAll")
    @Operation(summary = "分布式搜索>清空",description = "分布式搜索>清空")
    public HttpResult<Boolean> deleteAll(@RequestParam("indexName")final String indexName) {
        return new HttpResult<Boolean>().ok(elasticsearchTemplate.deleteAll(indexName));
    }

    @DeleteMapping("/delete")
    @Operation(summary = "分布式搜索>删除索引",description = "分布式搜索>删除索引")
    public HttpResult<Boolean> delete(@RequestParam("indexName")final String indexName) throws IOException {
        return new HttpResult<Boolean>().ok(elasticsearchTemplate.deleteIndex(indexName));
    }

    @PostMapping("/highlightSearch")
    @Operation(summary = "分布式搜索>高亮搜索",description = "分布式搜索>高亮搜索")
    public HttpResult<SearchVO<Map<String,Object>>> highlightSearch(@RequestBody final SearchQo searchQo) {
        return new HttpResult<SearchVO<Map<String,Object>>>().ok(elasticsearchTemplate.highlightSearchIndex(searchQo));
    }

    @PostMapping("/aggregationSearch")
    @Operation(summary = "分布式搜索>聚合查询",description = "分布式搜索>聚合查询")
    public HttpResult<SearchVO<Map<String,Long>>> aggregationSearch(@RequestBody final SearchQo searchQo) throws IOException {
        return new HttpResult<SearchVO<Map<String,Long>>>().ok(elasticsearchTemplate.aggregationSearchIndex(searchQo));
    }

}

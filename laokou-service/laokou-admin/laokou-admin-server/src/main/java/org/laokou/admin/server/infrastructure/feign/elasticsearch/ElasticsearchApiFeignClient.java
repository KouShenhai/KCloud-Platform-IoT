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
package org.laokou.admin.server.infrastructure.feign.elasticsearch;
import org.laokou.admin.server.infrastructure.feign.elasticsearch.factory.ElasticsearchApiFeignClientFallbackFactory;
import org.laokou.common.core.constant.ServiceConstant;
import org.laokou.common.swagger.utils.HttpResult;
import org.laokou.elasticsearch.client.dto.CreateIndexDTO;
import org.laokou.elasticsearch.client.dto.ElasticsearchDTO;
import org.laokou.elasticsearch.client.qo.SearchQo;
import org.laokou.elasticsearch.client.vo.SearchVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
/**
 * @author laokou
 */
@FeignClient(value = ServiceConstant.LAOKOU_ELASTICSEARCH, fallbackFactory = ElasticsearchApiFeignClientFallbackFactory.class)
@Service
public interface ElasticsearchApiFeignClient {

    /**
     * 高亮搜索
     * @param searchQo
     * @return
     */
    @PostMapping("/api/highlightSearch")
    HttpResult<SearchVO<Map<String,Object>>> highlightSearch(@RequestBody final SearchQo searchQo);

    /**
     * 异步批量同步索引
     * @param model
     * @return
     */
    @PostMapping("/api/syncBatch")
    HttpResult<Boolean> syncBatch(@RequestBody final ElasticsearchDTO model);

    /**
     * 异步创建索引
     * @param model
     * @return
     */
    @PostMapping("/api/create")
    HttpResult<Boolean> create(@RequestBody final CreateIndexDTO model);

    /**
     * 异步删除索引
     * @param indexName
     * @return
     */
    @DeleteMapping("/api/delete")
    HttpResult<Boolean> delete(@RequestParam("indexName")final String indexName);

}

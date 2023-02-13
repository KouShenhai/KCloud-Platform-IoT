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
package org.laokou.admin.server.infrastructure.feign.elasticsearch.fallback;
import org.laokou.admin.server.infrastructure.feign.elasticsearch.ElasticsearchApiFeignClient;
import org.laokou.common.core.utils.HttpResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.elasticsearch.client.dto.CreateIndexDTO;
import org.laokou.elasticsearch.client.dto.ElasticsearchDTO;
import org.laokou.elasticsearch.client.qo.SearchQo;
import org.laokou.elasticsearch.client.vo.SearchVO;
import java.util.Map;
/**
 * 服务降级
 * @author laokou
 */
@Slf4j
@AllArgsConstructor
public class ElasticsearchApiFeignClientFallback implements ElasticsearchApiFeignClient {

    private final Throwable throwable;

    @Override
    public HttpResult<SearchVO<Map<String,Object>>> highlightSearch(SearchQo searchQo) {
        log.error("服务调用失败，报错原因：{}",throwable.getMessage());
        return new HttpResult<SearchVO<Map<String,Object>>>().error("搜索服务未启动，请联系管理员");
    }

    @Override
    public HttpResult<Boolean> syncBatch(ElasticsearchDTO model) {
        return new HttpResult<Boolean>().error("同步索引失败，请联系管理员");
    }

    @Override
    public HttpResult<Boolean> create(CreateIndexDTO model) {
        return new HttpResult<Boolean>().error("索引创建失败，请联系管理员");
    }

    @Override
    public HttpResult<Boolean> delete(String indexName) {
        return new HttpResult<Boolean>().error("索引删除失败，请联系管理员");
    }

}
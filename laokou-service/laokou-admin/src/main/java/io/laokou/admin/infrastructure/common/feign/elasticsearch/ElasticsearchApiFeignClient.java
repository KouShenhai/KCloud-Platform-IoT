/**
 * Copyright (c) 2022 KCloud-Platform Authors. All Rights Reserved.
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
 */
package io.laokou.admin.infrastructure.common.feign.elasticsearch;
import io.laokou.admin.infrastructure.common.feign.elasticsearch.factory.ElasticsearchApiFeignClientFallbackFactory;
import io.laokou.admin.infrastructure.common.feign.elasticsearch.form.SearchForm;
import io.laokou.admin.infrastructure.common.feign.elasticsearch.form.SearchVO;
import io.laokou.admin.infrastructure.common.model.CreateIndexModel;
import io.laokou.admin.infrastructure.common.model.ElasticsearchModel;
import io.laokou.common.constant.ServiceConstant;
import io.laokou.common.utils.HttpResultUtil;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
/**
 * @author Kou Shenhai
 */
@FeignClient(url = "${service.elasticsearch.uri}",name = "${service.elasticsearch.uri}", fallbackFactory = ElasticsearchApiFeignClientFallbackFactory.class)
@Service
public interface ElasticsearchApiFeignClient {

    @PostMapping("/api/create")
    void create(@RequestBody final CreateIndexModel model);

    @PostMapping("/api/syncAsyncBatch")
    void syncAsyncBatch(@RequestBody final ElasticsearchModel model);

    @PostMapping("/api/highlightSearch")
    HttpResultUtil<SearchVO<Map<String,Object>>> highlightSearch(@RequestBody final SearchForm searchForm);
}

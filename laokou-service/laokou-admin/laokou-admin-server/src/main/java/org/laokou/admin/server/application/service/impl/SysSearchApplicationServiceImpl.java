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
package org.laokou.admin.server.application.service.impl;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.server.application.service.SysSearchApplicationService;
import org.laokou.admin.server.infrastructure.feign.elasticsearch.ElasticsearchApiFeignClient;
import org.laokou.common.swagger.utils.HttpResult;
import org.laokou.elasticsearch.client.form.SearchForm;
import org.laokou.elasticsearch.client.vo.SearchVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @author laokou
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SysSearchApplicationServiceImpl implements SysSearchApplicationService {

    private final ElasticsearchApiFeignClient elasticsearchApiFeignClient;

    @Override
    public SearchVO<Map<String,Object>> searchResource(SearchForm form) {
        try {
            HttpResult<SearchVO<Map<String, Object>>> result = elasticsearchApiFeignClient.highlightSearch(form);
            return result.getData();
        } catch (FeignException ex) {
            log.error("错误信息：{}",ex.getMessage());
        }
        return new SearchVO<>();
    }
}

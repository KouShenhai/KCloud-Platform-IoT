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
package org.laokou.rocketmq.consumer.feign.elasticsearch.fallback;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.swagger.utils.HttpResult;
import org.laokou.elasticsearch.client.dto.CreateIndexDTO;
import org.laokou.elasticsearch.client.dto.ElasticsearchDTO;
import org.laokou.rocketmq.consumer.feign.elasticsearch.ElasticsearchApiFeignClient;

/**
 * 服务降级
 * @author laokou
 * @version 1.0
 * @date 2020/9/5 0005 上午 12:12
 */
@Slf4j
@AllArgsConstructor
public class ElasticsearchApiFeignClientFallback implements ElasticsearchApiFeignClient {

    private final Throwable throwable;

    @Override
    public HttpResult<Boolean> syncBatch(ElasticsearchDTO model) {
        log.error("服务调用失败，报错原因：{}",throwable.getMessage());
        return new HttpResult<Boolean>().error("服务调用失败，请联系管理员");
    }

    @Override
    public HttpResult<Boolean> create(CreateIndexDTO model) {
        log.error("服务调用失败，报错原因：{}",throwable.getMessage());
        return new HttpResult<Boolean>().error("服务调用失败，请联系管理员");
    }

    @Override
    public HttpResult<Boolean> delete(String indexName) {
        log.error("服务调用失败，报错原因：{}",throwable.getMessage());
        return new HttpResult<Boolean>().error("服务调用失败，请联系管理员");
    }

}
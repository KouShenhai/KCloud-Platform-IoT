/**
 * Copyright 2020-2022 Kou Shenhai
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
package io.laokou.admin.infrastructure.common.feign.elasticsearch.factory;
import io.laokou.admin.infrastructure.common.feign.elasticsearch.fallback.ElasticsearchApiFeignClientFallback;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 回调工厂
 * @author Kou Shenhai
 * @version 1.0
 * @date 2020/9/5 0005 上午 12:12
 */
@Component
public class ElasticsearchApiFeignClientFallbackFactory implements FallbackFactory<ElasticsearchApiFeignClientFallback> {
    @Override
    public ElasticsearchApiFeignClientFallback create(Throwable throwable) {
        return new ElasticsearchApiFeignClientFallback(throwable);
    }
}
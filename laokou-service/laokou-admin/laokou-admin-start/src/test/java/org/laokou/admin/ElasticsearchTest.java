/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.admin;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.laokou.common.elasticsearch.template.ElasticsearchTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.test.context.TestConstructor;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

/**
 * @author laokou
 */
@Slf4j
@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class ElasticsearchTest extends CommonTest {

    private final ElasticsearchTemplate elasticsearchTemplate;

    public ElasticsearchTest(WebApplicationContext webApplicationContext,
                              OAuth2AuthorizationService oAuth2AuthorizationService,
                             ElasticsearchTemplate elasticsearchTemplate) {
        super(webApplicationContext, oAuth2AuthorizationService);
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Test
    public void searchProperties() {
        Map<String, String> stringStringMap = elasticsearchTemplate.getIndexNames(new String[]{"laokou_resource"});
        stringStringMap.forEach((k,v) -> log.info("key：{}，value：{}", k,v));
        Map<String, Object> map = elasticsearchTemplate.getIndexProperties("laokou_resource_202110");
        log.info("获取索引属性：{}", map);
    }

}

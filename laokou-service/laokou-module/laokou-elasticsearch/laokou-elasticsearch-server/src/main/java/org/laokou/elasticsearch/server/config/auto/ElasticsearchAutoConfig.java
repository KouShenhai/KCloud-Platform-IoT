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
package org.laokou.elasticsearch.server.config.auto;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.laokou.elasticsearch.server.config.CustomElasticsearchProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
/**
 * es配置文件
 * @author laokou
 */
@AutoConfiguration
@Slf4j
public class ElasticsearchAutoConfig {

    @Bean("elasticsearchRestClientBuilder")
    @ConditionalOnMissingBean(RestClientBuilder.class)
    public RestClientBuilder elasticsearchRestClientBuilder(CustomElasticsearchProperties properties) {
        HttpHost[] hosts = properties.getUris().stream().map(this::createHttpHost).toArray(HttpHost[]::new);
        // 配置权限验证
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(properties.getUsername(), properties.getPassword()));
        return RestClient.builder(hosts)
                .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
                        .setMaxConnPerRoute(100)
                        .setKeepAliveStrategy((response, context) -> Duration.ofMinutes(5).toMillis())
                        //最大连接数
                        .setMaxConnTotal(100))
                .setRequestConfigCallback(builder -> {
                    builder.setConnectTimeout(-1);
                    builder.setSocketTimeout(60000);
                    builder.setConnectionRequestTimeout(-1);
                    return builder;
                });
    }

    private HttpHost createHttpHost(String uri) {
        try {
            return createHttpHost(URI.create(uri));
        }
        catch (IllegalArgumentException ex) {
            return HttpHost.create(uri);
        }
    }

    private HttpHost createHttpHost(URI uri) {
        if (!StringUtils.hasLength(uri.getUserInfo())) {
            return HttpHost.create(uri.toString());
        }
        try {
            return HttpHost.create(new URI(uri.getScheme(), null, uri.getHost(), uri.getPort(), uri.getPath(),
                    uri.getQuery(), uri.getFragment())
                    .toString());
        }
        catch (URISyntaxException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Bean(name = "restHighLevelClient")
    @ConditionalOnMissingBean(RestHighLevelClient.class)
    @ConditionalOnClass(RestClientBuilder.class)
    public RestHighLevelClient restHighLevelClient(RestClientBuilder elasticsearchRestClientBuilder) {
        return new RestHighLevelClient(elasticsearchRestClientBuilder);
    }

}

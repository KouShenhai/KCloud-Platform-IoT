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
package org.laokou.redis.config;
import org.laokou.redis.utils.RedisKeyUtil;
import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * @EnableConfigurationProperties -> ConfigurationProperties的类进行一次注入
 * AutoConfiguration -> 给插件使用
 * Configuration -> 直接使用
 * @ConditionalOnBean -> spring容器中存在指定的class实例对象，对应的配置才生效
 * @ConditionalOnMissingBean -> ConditionalOnMissingBean 保证只有一个bean被注入
 * 某个class位于类路径上，才会实例化一个bean
 * @author laokou
 */
@ConditionalOnClass(Redisson.class)
@AutoConfiguration(before = RedisAutoConfiguration.class)
@EnableConfigurationProperties(RedisProperties.class)
public class RedisSessionConfig {

    private static final String REDIS_PROTOCOL_PREFIX = "redis://";

    private static final String REDISS_PROTOCOL_PREFIX = "rediss://";

    /**
     * ConditionalOnMissingBean -> 相同类型的bean被注入，保证bean只有一个
     */
    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean(RedissonClient.class)
    public RedissonClient redisClient(RedisProperties properties) {
        Config config;
        final Duration duration = properties.getTimeout();
        int timeout = duration == null ? 0 : (int) duration.toMillis();
        if (properties.getSentinel() != null) {
            config = new Config();
            config.useSentinelServers()
                    .setMasterName(properties.getSentinel().getMaster())
                    .addSentinelAddress(convertNodes(properties.isSsl(),properties.getSentinel().getNodes()))
                    .setDatabase(properties.getDatabase())
                    .setConnectTimeout(timeout)
                    .setPassword(properties.getPassword());
        } else if (properties.getCluster() != null) {
            config = new Config();
            config.useClusterServers()
                    .addNodeAddress(convertNodes(properties.isSsl(),properties.getCluster().getNodes()))
                    .setPassword(properties.getPassword())
                    .setTimeout(timeout);
        } else {
            config = new Config();
            config.useSingleServer()
                    .setAddress(convertAddress(properties.isSsl(),properties.getHost() ,properties.getPort()))
                    .setDatabase(properties.getDatabase())
                    .setPassword(properties.getPassword())
                    .setTimeout(timeout);
        }
        //使用json序列化方式
        Codec codec = CustomJsonJacksonCodec.INSTANCE;
        config.setCodec(codec);
        return Redisson.create(config);
    }

    @Bean
    public RBloomFilter<String> bloomFilter(RedissonClient redisson) {
        RBloomFilter<String> bloomFilter = redisson.getBloomFilter(RedisKeyUtil.getBloomFilterKey());
        bloomFilter.tryInit(10000,0.01);
        return bloomFilter;
    }

    private String getProtocolPrefix(boolean isSsl) {
        return isSsl ? REDISS_PROTOCOL_PREFIX : REDIS_PROTOCOL_PREFIX;
    }

    private String convertAddress(boolean isSsl,String host,int port) {
        return getProtocolPrefix(isSsl) + host + ":" + port;
    }

    private String[] convertNodes(boolean isSsl,List<String> nodeList) {
        List<String> nodes = new ArrayList<>(nodeList.size());
        for (String node : nodeList) {
            if (node.startsWith(REDISS_PROTOCOL_PREFIX) || node.startsWith(REDIS_PROTOCOL_PREFIX)) {
                nodes.add(node);
            } else {
                nodes.add(getProtocolPrefix(isSsl) + node);
            }
        }
        return nodes.toArray(new String[0]);
    }

}

/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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
package org.laokou.common.redis.config;

import org.laokou.common.redis.utils.RedisKeyUtil;
import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author livk
 * @author laokou
 */
@ConditionalOnClass(Redisson.class)
@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class RedissonConfig {

	private static final String REDIS_PROTOCOL_PREFIX = "redis://";

	private static final String REDISS_PROTOCOL_PREFIX = "rediss://";

	@Bean
	public RBloomFilter<String> bloomFilter(RedissonClient redisson) {
		RBloomFilter<String> bloomFilter = redisson.getBloomFilter(RedisKeyUtil.getBloomFilterKey());
		bloomFilter.tryInit(10000, 0.01);
		return bloomFilter;
	}

	/**
	 * redisson配置
	 * @param properties redis配置文件
	 * @return RedissonClient
	 */
	@Bean(destroyMethod = "shutdown")
	@ConditionalOnMissingBean(RedissonClient.class)
	public RedissonClient redisClient(ThreadPoolTaskExecutor taskExecutor, RedisProperties properties) {
		Config config = new Config();
		int timeout = (int) properties.getTimeout().toMillis();
		int connectTimeout = (int) properties.getConnectTimeout().toMillis();
		boolean isSsl = properties.getSsl().isEnabled();
		if (properties.getSentinel() != null) {
			config.useSentinelServers()
				.setMasterName(properties.getSentinel().getMaster())
				.addSentinelAddress(convertNodes(isSsl, properties.getSentinel().getNodes()))
				.setDatabase(properties.getDatabase())
				.setTimeout(timeout)
				.setConnectTimeout(connectTimeout)
				.setPassword(properties.getPassword());
		}
		else if (properties.getCluster() != null) {
			config.useClusterServers()
				.addNodeAddress(convertNodes(isSsl, properties.getCluster().getNodes()))
				.setPassword(properties.getPassword())
				.setTimeout(timeout)
				.setConnectTimeout(connectTimeout);
		}
		else {
			config.useSingleServer()
				.setAddress(convertAddress(isSsl, properties.getHost(), properties.getPort()))
				.setDatabase(properties.getDatabase())
				.setPassword(properties.getPassword())
				.setConnectTimeout(connectTimeout)
				.setTimeout(timeout);
		}
		config.setExecutor(taskExecutor.getThreadPoolExecutor());
		// 使用json序列化方式
		config.setCodec(CustomJsonJacksonCodec.INSTANCE);
		return Redisson.create(config);
	}

	private String getProtocolPrefix(boolean isSsl) {
		return isSsl ? REDISS_PROTOCOL_PREFIX : REDIS_PROTOCOL_PREFIX;
	}

	private String convertAddress(boolean isSsl, String host, int port) {
		return getProtocolPrefix(isSsl) + host + ":" + port;
	}

	private String[] convertNodes(boolean isSsl, List<String> nodeList) {
		List<String> nodes = new ArrayList<>(nodeList.size());
		for (String node : nodeList) {
			if (node.startsWith(REDISS_PROTOCOL_PREFIX) || node.startsWith(REDIS_PROTOCOL_PREFIX)) {
				nodes.add(node);
			}
			else {
				nodes.add(getProtocolPrefix(isSsl) + node);
			}
		}
		return nodes.toArray(new String[0]);
	}

}

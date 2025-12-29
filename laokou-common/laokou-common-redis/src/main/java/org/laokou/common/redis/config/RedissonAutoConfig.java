/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

import org.laokou.common.i18n.common.constant.StringConstants;
import org.laokou.common.i18n.util.ObjectUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.data.redis.autoconfigure.DataRedisProperties;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author livk
 * @author laokou
 */
@AutoConfiguration
@ConditionalOnClass(Redisson.class)
@EnableConfigurationProperties(DataRedisProperties.class)
public class RedissonAutoConfig {

	/**
	 * Redis普通连接.
	 */
	private static final String REDIS_PROTOCOL_PREFIX = "redis://";

	/**
	 * Redis加密连接.
	 */
	private static final String REDISS_PROTOCOL_PREFIX = "rediss://";

	/**
	 * redisson配置.
	 * @param properties redis配置文件
	 * @return RedissonClient
	 */
	@Bean(name = "redisClient", destroyMethod = "shutdown")
	public RedissonClient redisClient(DataRedisProperties properties) {
		Config config = new Config();
		config.setPassword(properties.getPassword());
		int timeout = (int) properties.getTimeout().toMillis();
		int connectTimeout = (int) properties.getConnectTimeout().toMillis();
		boolean isSsl = properties.getSsl().isEnabled();
		if (ObjectUtils.isNotNull(properties.getSentinel())) {
			config.useSentinelServers()
				.setMasterName(properties.getSentinel().getMaster())
				.addSentinelAddress(convertNodes(isSsl, properties.getSentinel().getNodes()))
				.setDatabase(properties.getDatabase())
				.setTimeout(timeout)
				.setConnectTimeout(connectTimeout);
		}
		else if (ObjectUtils.isNotNull(properties.getCluster())) {
			config.useClusterServers()
				.addNodeAddress(convertNodes(isSsl, properties.getCluster().getNodes()))
				.setTimeout(timeout)
				.setConnectTimeout(connectTimeout);
		}
		else {
			config.useSingleServer()
				.setAddress(convertAddress(isSsl, properties.getHost(), properties.getPort()))
				.setDatabase(properties.getDatabase())
				.setConnectTimeout(connectTimeout)
				.setTimeout(timeout);
		}
		return Redisson.create(config);
	}

	private String getProtocolPrefix(boolean isSsl) {
		return isSsl ? REDISS_PROTOCOL_PREFIX : REDIS_PROTOCOL_PREFIX;
	}

	private String convertAddress(boolean isSsl, String host, int port) {
		return getProtocolPrefix(isSsl) + host + StringConstants.RISK + port;
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
		return nodes.toArray(String[]::new);
	}

}

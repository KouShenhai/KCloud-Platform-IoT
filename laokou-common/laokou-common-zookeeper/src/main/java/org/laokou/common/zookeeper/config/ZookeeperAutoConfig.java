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

package org.laokou.common.zookeeper.config;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @author laokou
 */
@AutoConfiguration
public class ZookeeperAutoConfig {

	@Bean(initMethod = "start", destroyMethod = "close")
	public CuratorFramework curatorFramework(ZookeeperProperties zookeeperProperties) {
		return CuratorFrameworkFactory.builder()
			.connectString(zookeeperProperties.getAddress())
			.sessionTimeoutMs(zookeeperProperties.getSessionTimeoutMs())
			.connectionTimeoutMs(zookeeperProperties.getConnectionTimeoutMs())
			.retryPolicy(new ExponentialBackoffRetry(1000, 3))
			.build();
	}

}

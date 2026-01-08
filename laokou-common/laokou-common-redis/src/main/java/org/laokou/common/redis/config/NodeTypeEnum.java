/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

import io.netty.channel.MultiThreadIoEventLoopGroup;
import io.netty.channel.nio.NioIoHandler;
import lombok.Getter;
import org.redisson.config.Config;

import java.util.concurrent.ExecutorService;

@Getter
public enum NodeTypeEnum {

	SINGLE("single", "单机模式") {
		@Override
		Config getConfig(ExecutorService virtualThreadExecutor, SpringRedissonProperties springRedissonProperties) {
			return useSingleServer(virtualThreadExecutor, springRedissonProperties);
		}
	},

	CLUSTER("cluster", "集群模式") {
		@Override
		Config getConfig(ExecutorService virtualThreadExecutor, SpringRedissonProperties springRedissonProperties) {
			return useClusterServers(virtualThreadExecutor, springRedissonProperties);
		}
	},

	SENTINEL("sentinel", "哨兵模式") {
		@Override
		Config getConfig(ExecutorService virtualThreadExecutor, SpringRedissonProperties springRedissonProperties) {
			return useSentinelServers(virtualThreadExecutor, springRedissonProperties);
		}
	},

	MASTER_SLAVE("master_slave", "主从模式") {
		@Override
		Config getConfig(ExecutorService virtualThreadExecutor, SpringRedissonProperties springRedissonProperties) {
			throw new UnsupportedOperationException("Unsupported master and slave");
		}
	},

	REPLICATED("replicated", "复制模式") {
		@Override
		Config getConfig(ExecutorService virtualThreadExecutor, SpringRedissonProperties springRedissonProperties) {
			throw new UnsupportedOperationException("Unsupported replicated");
		}
	};

	private final String code;

	private final String desc;

	NodeTypeEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	abstract Config getConfig(ExecutorService virtualThreadExecutor, SpringRedissonProperties springRedissonProperties);

	private static Config useSingleServer(ExecutorService virtualThreadExecutor,
			SpringRedissonProperties springRedissonProperties) {
		Config config = createDefaultConfig(virtualThreadExecutor, springRedissonProperties);
		SpringRedissonProperties.Single single = springRedissonProperties.getNode().getSingle();
		config.useSingleServer()
			.setAddress(single.getAddress())
			.setConnectTimeout(single.getConnectTimeout())
			.setTimeout(single.getTimeout())
			.setSubscriptionTimeout(single.getSubscriptionTimeout())
			.setRetryAttempts(single.getRetryAttempts())
			.setSubscriptionsPerConnection(single.getSubscriptionsPerConnection())
			.setClientName(single.getClientName())
			.setPingConnectionInterval(single.getPingConnectionInterval())
			.setSubscriptionConnectionMinimumIdleSize(single.getSubscriptionConnectionMinimumIdleSize())
			.setSubscriptionConnectionPoolSize(single.getSubscriptionConnectionPoolSize())
			.setConnectionMinimumIdleSize(single.getConnectionMinimumIdleSize())
			.setConnectionPoolSize(single.getConnectionPoolSize())
			.setDnsMonitoringInterval(single.getDnsMonitoringInterval())
			.setDatabase(single.getDatabase());
		return config;
	}

	private static Config useClusterServers(ExecutorService virtualThreadExecutor,
			SpringRedissonProperties springRedissonProperties) {
		Config config = createDefaultConfig(virtualThreadExecutor, springRedissonProperties);
		SpringRedissonProperties.Cluster cluster = springRedissonProperties.getNode().getCluster();
		config.useClusterServers()
			.addNodeAddress(cluster.getNodeAddresses().toArray(String[]::new))
			.setConnectTimeout(cluster.getConnectTimeout())
			.setSubscriptionTimeout(cluster.getSubscriptionTimeout())
			.setRetryAttempts(cluster.getRetryAttempts())
			.setSubscriptionsPerConnection(cluster.getSubscriptionsPerConnection())
			.setClientName(cluster.getClientName())
			.setPingConnectionInterval(cluster.getPingConnectionInterval())
			.setSubscriptionConnectionMinimumIdleSize(cluster.getSubscriptionConnectionMinimumIdleSize())
			.setSubscriptionConnectionPoolSize(cluster.getSubscriptionConnectionPoolSize())
			.setIdleConnectionTimeout(cluster.getIdleConnectionTimeout())
			.setTimeout(cluster.getTimeout())
			.setLoadBalancer(cluster.getLoadBalancer().get())
			.setSlaveConnectionMinimumIdleSize(cluster.getSlaveConnectionMinimumIdleSize())
			.setSlaveConnectionPoolSize(cluster.getSlaveConnectionPoolSize())
			.setFailedSlaveReconnectionInterval(cluster.getFailedSlaveReconnectionInterval())
			.setMasterConnectionMinimumIdleSize(cluster.getMasterConnectionMinimumIdleSize())
			.setMasterConnectionPoolSize(cluster.getMasterConnectionPoolSize())
			.setReadMode(cluster.getReadMode())
			.setSubscriptionMode(cluster.getSubscriptionMode())
			.setDnsMonitoringInterval(cluster.getDnsMonitoringInterval())
			.setScanInterval(cluster.getScanInterval())
			.setCheckSlotsCoverage(cluster.isCheckSlotsCoverage())
			.setCheckMasterLinkStatus(cluster.isCheckMasterLinkStatus())
			.setShardedSubscriptionMode(cluster.getShardedSubscriptionMode())
			.setDatabase(cluster.getDatabase());
		return config;
	}

	private static Config useSentinelServers(ExecutorService virtualThreadExecutor,
			SpringRedissonProperties springRedissonProperties) {
		Config config = createDefaultConfig(virtualThreadExecutor, springRedissonProperties);
		SpringRedissonProperties.Sentinel sentinel = springRedissonProperties.getNode().getSentinel();
		config.useSentinelServers()
			.setMasterName(sentinel.getMasterName())
			.addSentinelAddress(sentinel.getSentinelAddresses().toArray(String[]::new))
			.setIdleConnectionTimeout(sentinel.getIdleConnectionTimeout())
			.setConnectTimeout(sentinel.getConnectTimeout())
			.setTimeout(sentinel.getTimeout())
			.setSubscriptionTimeout(sentinel.getSubscriptionTimeout())
			.setRetryAttempts(sentinel.getRetryAttempts())
			.setSubscriptionsPerConnection(sentinel.getSubscriptionsPerConnection())
			.setClientName(sentinel.getClientName())
			.setPingConnectionInterval(sentinel.getPingConnectionInterval())
			.setLoadBalancer(sentinel.getLoadBalancer().get())
			.setSlaveConnectionMinimumIdleSize(sentinel.getSlaveConnectionMinimumIdleSize())
			.setSlaveConnectionPoolSize(sentinel.getSlaveConnectionPoolSize())
			.setFailedSlaveReconnectionInterval(sentinel.getFailedSlaveReconnectionInterval())
			.setMasterConnectionMinimumIdleSize(sentinel.getMasterConnectionMinimumIdleSize())
			.setMasterConnectionPoolSize(sentinel.getMasterConnectionPoolSize())
			.setReadMode(sentinel.getReadMode())
			.setSubscriptionMode(sentinel.getSubscriptionMode())
			.setSubscriptionConnectionMinimumIdleSize(sentinel.getSubscriptionConnectionMinimumIdleSize())
			.setSubscriptionConnectionPoolSize(sentinel.getSubscriptionConnectionPoolSize())
			.setDnsMonitoringInterval(sentinel.getDnsMonitoringInterval())
			.setSentinelUsername(sentinel.getSentinelUsername())
			.setSentinelPassword(sentinel.getSentinelPassword())
			.setDatabase(sentinel.getDatabase())
			.setScanInterval(sentinel.getScanInterval())
			.setCheckSentinelsList(sentinel.isCheckSentinelsList())
			.setCheckSlaveStatusWithSyncing(sentinel.isCheckSlaveStatusWithSyncing())
			.setSentinelsDiscovery(sentinel.isSentinelsDiscovery());
		return config;
	}

	private static Config createDefaultConfig(ExecutorService virtualThreadExecutor,
			SpringRedissonProperties springRedissonProperties) {
		Config config = new Config();
		config.setPassword(springRedissonProperties.getPassword());
		config.setThreads(springRedissonProperties.getThreads());
		config.setCodec(springRedissonProperties.getCodec().getCodec());
		config.setTransportMode(springRedissonProperties.getTransportMode());
		config.setExecutor(virtualThreadExecutor);
		config.setEventLoopGroup(new MultiThreadIoEventLoopGroup(springRedissonProperties.getNettyThreads(),
				virtualThreadExecutor, NioIoHandler.newFactory()));
		config.setNettyExecutor(virtualThreadExecutor);
		config.setReferenceEnabled(springRedissonProperties.isReferenceEnabled());
		config.setNettyThreads(springRedissonProperties.getNettyThreads());
		config.setLockWatchdogTimeout(springRedissonProperties.getLockWatchdogTimeout());
		config.setFairLockWaitTimeout(springRedissonProperties.getFairLockWaitTimeout());
		config.setLockWatchdogBatchSize(springRedissonProperties.getLockWatchdogBatchSize());
		config.setCheckLockSyncedSlaves(springRedissonProperties.isCheckLockSyncedSlaves());
		config.setKeepPubSubOrder(springRedissonProperties.isKeepPubSubOrder());
		config.setUseScriptCache(springRedissonProperties.isUseScriptCache());
		config.setMinCleanUpDelay(springRedissonProperties.getMinCleanUpDelay());
		config.setMaxCleanUpDelay(springRedissonProperties.getMaxCleanUpDelay());
		config.setCleanUpKeysAmount(springRedissonProperties.getCleanUpKeysAmount());
		config.setUseThreadClassLoader(springRedissonProperties.isUseThreadClassLoader());
		config.setReliableTopicWatchdogTimeout(springRedissonProperties.getReliableTopicWatchdogTimeout());
		config.setSlavesSyncTimeout(springRedissonProperties.getSlavesSyncTimeout());
		config.setTcpKeepAlive(springRedissonProperties.isTcpKeepAlive());
		config.setTcpKeepAliveCount(springRedissonProperties.getTcpKeepAliveCount());
		config.setTcpKeepAliveIdle(springRedissonProperties.getTcpKeepAliveIdle());
		config.setTcpKeepAliveInterval(springRedissonProperties.getTcpKeepAliveInterval());
		config.setTcpUserTimeout(springRedissonProperties.getTcpUserTimeout());
		config.setTcpNoDelay(springRedissonProperties.isTcpNoDelay());
		return config;
	}

}

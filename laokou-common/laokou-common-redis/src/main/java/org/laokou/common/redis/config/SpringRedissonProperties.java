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

import lombok.Data;
import org.laokou.common.i18n.util.StringExtUtils;
import org.redisson.config.ReadMode;
import org.redisson.config.ShardedSubscriptionMode;
import org.redisson.config.SubscriptionMode;
import org.redisson.config.TransportMode;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author laokou
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.redisson")
public class SpringRedissonProperties implements InitializingBean {

	private String password;

	private String username;

	private boolean tcpKeepAlive;

	private int tcpKeepAliveCount;

	private int tcpKeepAliveIdle;

	private int tcpKeepAliveInterval;

	private int tcpUserTimeout;

	private boolean tcpNoDelay = true;

	private int threads = 16;

	private int nettyThreads = 32;

	private boolean referenceEnabled = true;

	private TransportMode transportMode = TransportMode.NIO;

	private long lockWatchdogTimeout = 30 * 1000;

	private int lockWatchdogBatchSize = 100;

	private long fairLockWaitTimeout = 5 * 60000;

	private boolean checkLockSyncedSlaves = true;

	private long slavesSyncTimeout = 1000;

	private long reliableTopicWatchdogTimeout = TimeUnit.MINUTES.toMillis(10);

	private boolean keepPubSubOrder = true;

	private boolean useScriptCache = true;

	private int minCleanUpDelay = 5;

	private int maxCleanUpDelay = 30 * 60;

	private int cleanUpKeysAmount = 100;

	private boolean useThreadClassLoader = true;

	private CodecTypeEnum codec = CodecTypeEnum.FORY;

	private Node node;

	@Override
	public void afterPropertiesSet() {
		if (StringExtUtils.isEmpty(this.password)) {
			throw new IllegalStateException("password must not be empty.");
		}
		if (StringExtUtils.isEmpty(this.username)) {
			throw new IllegalStateException("username must not be empty.");
		}
		if (ObjectUtils.isEmpty(this.node)) {
			throw new IllegalStateException("node must not be empty.");
		}
	}

	@Data
	static class Base {

		/**
		 * If pooled connection not used for a <code>timeout</code> time and current
		 * connections amount bigger than minimum idle connections pool size, then it will
		 * closed and removed from pool. Value in milliseconds.
		 *
		 */
		private int idleConnectionTimeout = 10000;

		/**
		 * Timeout during connecting to any Redis server. Value in milliseconds.
		 *
		 */
		private int connectTimeout = 10000;

		/**
		 * Redis server response timeout. Starts to countdown when Redis command was
		 * succesfully sent. Value in milliseconds.
		 *
		 */
		private int timeout = 3000;

		private int subscriptionTimeout = 7500;

		private int retryAttempts = 4;

		/**
		 * Subscriptions per Redis connection limit.
		 */
		private int subscriptionsPerConnection = 5;

		/**
		 * Name of client connection.
		 */
		private String clientName;

		private int pingConnectionInterval = 30000;

	}

	@Data
	public static class Node {

		private NodeTypeEnum type;

		private Single single;

		private Cluster cluster;

		private Sentinel sentinel;

		private MasterSlave masterSlave;

		private Replicated replicated;

	}

	@Data
	public static class Single extends Base {

		/**
		 * Redis server address.
		 */
		private String address;

		/**
		 * Minimum idle subscription connection amount.
		 */
		private int subscriptionConnectionMinimumIdleSize = 1;

		/**
		 * Redis subscription connection maximum pool size.
		 *
		 */
		private int subscriptionConnectionPoolSize = 50;

		/**
		 * Minimum idle Redis connection amount.
		 */
		private int connectionMinimumIdleSize = 24;

		/**
		 * Redis connection maximum pool size.
		 */
		private int connectionPoolSize = 64;

		/**
		 * Database index used for Redis connection.
		 */
		private int database = 0;

		/**
		 * Interval in milliseconds to check DNS.
		 */
		private long dnsMonitoringInterval = 5000;

	}

	@Data
	public static class Cluster extends BaseMasterSlave {

		/**
		 * Redis cluster node urls list.
		 */
		private List<String> nodeAddresses = new ArrayList<>();

		/**
		 * Redis cluster scan interval in milliseconds.
		 */
		private int scanInterval = 5000;

		private boolean checkSlotsCoverage = true;

		private boolean checkMasterLinkStatus = false;

		private ShardedSubscriptionMode shardedSubscriptionMode = ShardedSubscriptionMode.AUTO;

		private int database = 0;

	}

	@Data
	public static class Sentinel extends BaseMasterSlave {

		private List<String> sentinelAddresses = new ArrayList<>();

		private String masterName;

		private String sentinelUsername;

		private String sentinelPassword;

		/**
		 * Database index used for Redis connection.
		 */
		private int database = 0;

		/**
		 * Sentinel scan interval in milliseconds.
		 */
		private int scanInterval = 1000;

		private boolean checkSentinelsList = true;

		private boolean checkSlaveStatusWithSyncing = true;

		private boolean sentinelsDiscovery = true;

	}

	@Data
	static class BaseMasterSlave extends Base {

		/**
		 * Сonnection load balancer for multiple Redis slave servers.
		 */
		private LoadBalancerTypeEnum loadBalancer;

		/**
		 * Redis 'slave' node minimum idle connection amount for <b>each</b> slave node.
		 */
		private int slaveConnectionMinimumIdleSize = 24;

		/**
		 * Redis 'slave' node maximum connection pool size for <b>each</b> slave node.
		 */
		private int slaveConnectionPoolSize = 64;

		private int failedSlaveReconnectionInterval = 3000;

		@Deprecated
		private int failedSlaveCheckInterval = 180000;

		/**
		 * Redis 'master' node minimum idle connection amount for <b>each</b> slave node.
		 */
		private int masterConnectionMinimumIdleSize = 24;

		/**
		 * Redis 'master' node maximum connection pool size.
		 */
		private int masterConnectionPoolSize = 64;

		private ReadMode readMode = ReadMode.SLAVE;

		private SubscriptionMode subscriptionMode = SubscriptionMode.MASTER;

		/**
		 * Redis 'slave' node minimum idle subscription (pub/sub) connection amount for
		 * <b>each</b> slave node.
		 */
		private int subscriptionConnectionMinimumIdleSize = 1;

		/**
		 * Redis 'slave' node maximum subscription (pub/sub) connection pool size for
		 * <b>each</b> slave node.
		 */
		private int subscriptionConnectionPoolSize = 50;

		private long dnsMonitoringInterval = 5000;

	}

	@Data
	public static class MasterSlave extends BaseMasterSlave {

		/**
		 * Redis slave servers addresses.
		 */
		private Set<String> slaveAddresses = new HashSet<>();

		/**
		 * Redis master server address.
		 */
		private String masterAddress;

		/**
		 * Database index used for Redis connection.
		 */
		private int database = 0;

	}

	@Data
	public static class Replicated extends BaseMasterSlave {

		/**
		 * Replication group node urls list.
		 */
		private List<String> nodeAddresses = new ArrayList<>();

		/**
		 * Replication group scan interval in milliseconds.
		 */
		private int scanInterval = 1000;

		/**
		 * Database index used for Redis connection.
		 */
		private int database = 0;

		private boolean monitorIPChanges = false;

	}

}

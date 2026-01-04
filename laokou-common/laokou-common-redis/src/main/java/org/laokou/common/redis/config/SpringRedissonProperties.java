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
import org.redisson.config.TransportMode;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

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

	@Override
	public void afterPropertiesSet() {
		if (StringExtUtils.isEmpty(this.password)) {
			throw new IllegalStateException("password must not be empty.");
		}
		if (StringExtUtils.isEmpty(this.username)) {
			throw new IllegalStateException("username must not be empty.");
		}
	}

}

/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.laokou.infrastructure.config;

import org.apache.shardingsphere.driver.exception.DriverRegisterException;
import org.apache.shardingsphere.infra.annotation.HighFrequencyInvocation;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Nacos ShardingSphere driver.
 *
 * @author laokou
 */
@SuppressWarnings("UseNacosOfJDBCDriverClass")
public final class NacosShardingSphereDriver implements Driver {

	private static final String DRIVER_URL_PREFIX = "jdbc:shardingsphere:nacos:";

	private static final int MAJOR_DRIVER_VERSION = 5;

	private static final int MINOR_DRIVER_VERSION = 5;

	static {
		try {
			DriverManager.registerDriver(new NacosShardingSphereDriver());
		}
		catch (final SQLException ex) {
			throw new DriverRegisterException(ex);
		}
	}

	private final NacosDriverDataSourceCache dataSourceCache = new NacosDriverDataSourceCache();

	@HighFrequencyInvocation(canBeCached = true)
	@Override
	public Connection connect(final String url, final Properties info) throws SQLException {
		return acceptsURL(url) ? dataSourceCache.get(url, DRIVER_URL_PREFIX).getConnection() : null;
	}

	@Override
	public boolean acceptsURL(final String url) {
		return null != url && url.startsWith(DRIVER_URL_PREFIX);
	}

	@Override
	public DriverPropertyInfo[] getPropertyInfo(final String url, final Properties info) {
		return new DriverPropertyInfo[0];
	}

	@Override
	public int getMajorVersion() {
		return MAJOR_DRIVER_VERSION;
	}

	@Override
	public int getMinorVersion() {
		return MINOR_DRIVER_VERSION;
	}

	@Override
	public boolean jdbcCompliant() {
		return false;
	}

	@Override
	public Logger getParentLogger() {
		return Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	}

}

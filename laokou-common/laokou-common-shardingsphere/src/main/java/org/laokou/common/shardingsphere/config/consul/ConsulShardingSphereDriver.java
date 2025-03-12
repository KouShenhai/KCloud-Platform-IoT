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

package org.laokou.common.shardingsphere.config.consul;

import org.apache.shardingsphere.driver.exception.DriverRegisterException;
import org.laokou.common.shardingsphere.config.AbstractDriverDataSourceCache;
import org.laokou.common.shardingsphere.config.AbstractShardingSphereDriver;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Consul ShardingSphere driver.
 *
 * @author laokou
 */
@SuppressWarnings("UseConsulOfJDBCDriverClass")
public final class ConsulShardingSphereDriver extends AbstractShardingSphereDriver {

	private static final String DRIVER_URL_PREFIX = "jdbc:shardingsphere:consul:";

	static {
		try {
			DriverManager.registerDriver(new ConsulShardingSphereDriver());
		}
		catch (final SQLException ex) {
			throw new DriverRegisterException(ex);
		}
	}

	private final CosulDriverDataSourceCache dataSourceCache = new CosulDriverDataSourceCache();

	@Override
	protected AbstractDriverDataSourceCache getDriverDataSourceCache() {
		return dataSourceCache;
	}

	@Override
	protected String getDriverUrlPrefix() {
		return DRIVER_URL_PREFIX;
	}

}

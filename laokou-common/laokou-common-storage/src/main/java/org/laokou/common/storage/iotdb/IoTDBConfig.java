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

package org.laokou.common.storage.iotdb;

import org.laokou.common.storage.Config;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author laokou
 */
public class IoTDBConfig extends Config {

	private static final int DEFAULT_PORT = 6667;

	private static final Pattern ENDPOINT_PATTERN = Pattern
		.compile("^(?<host>\\[[0-9A-Fa-f:.%]+]|[A-Za-z0-9](?:[A-Za-z0-9.-]*[A-Za-z0-9])?)(?::(?<port>[0-9]{1,5}))?$");

	private static final Pattern DATABASE_PATTERN = Pattern
		.compile("[A-Za-z_][A-Za-z0-9_]*(?:\\.[A-Za-z_][A-Za-z0-9_]*)*");

	private final String jdbcUrl;

	private final String databasePath;

	public IoTDBConfig(String endpoint, String username, String password, String dbName) {
		super(normalizeEndpoint(endpoint), requireValue(username, "username"), requireValue(password, "password"),
				normalizeDatabaseName(dbName));
		this.jdbcUrl = "jdbc:iotdb://" + getEndpoint() + "/";
		this.databasePath = "root." + getDbName();
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public String getDatabasePath() {
		return databasePath;
	}

	private static String normalizeEndpoint(String endpoint) {
		if (endpoint == null || endpoint.isBlank()) {
			throw new IllegalArgumentException("IoTDB endpoint is required");
		}
		Matcher matcher = ENDPOINT_PATTERN.matcher(endpoint.trim());
		if (!matcher.matches()) {
			throw new IllegalArgumentException("IoTDB endpoint must be a host or host:port value");
		}
		String port = matcher.group("port");
		int portNumber = port == null ? DEFAULT_PORT : Integer.parseInt(port);
		if (portNumber < 1 || portNumber > 65535) {
			throw new IllegalArgumentException("IoTDB endpoint port must be between 1 and 65535");
		}
		return matcher.group("host") + ":" + portNumber;
	}

	private static String normalizeDatabaseName(String dbName) {
		String value = requireValue(dbName, "database name");
		if (value.startsWith("root.")) {
			value = value.substring("root.".length());
		}
		if (!DATABASE_PATTERN.matcher(value).matches()) {
			throw new IllegalArgumentException("IoTDB database name must contain valid path nodes");
		}
		return value;
	}

	private static String requireValue(String value, String name) {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException("IoTDB " + name + " is required");
		}
		return value.trim();
	}

}

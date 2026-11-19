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

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.laokou.common.storage.Table;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Set;

class IoTDBRemoteIntegrationTest {

	@Test
	void shouldCreateSchemaWriteAndReadFromRemoteIoTDB() throws Exception {
		String endpoint = System.getenv("IOTDB_TEST_ENDPOINT");
		Assumptions.assumeTrue(endpoint != null && !endpoint.isBlank(), "IOTDB_TEST_ENDPOINT is not configured");
		String username = valueOrDefault(System.getenv("IOTDB_TEST_USERNAME"), "root");
		String password = valueOrDefault(System.getenv("IOTDB_TEST_PASSWORD"), "root");
		String database = "dbeco19_smoke_" + Long.toUnsignedString(System.nanoTime());
		IoTDBConfig config = new IoTDBConfig(endpoint, username, password, database);
		IoTDB dataSource = new IoTDB(config);
		long timestamp = System.currentTimeMillis();
		String devicePath = config.getDatabasePath() + ".device_1";
		try {
			dataSource.open();
			dataSource.test();
			Table.Column temperature = new Table.Column();
			temperature.setName("temperature");
			temperature.setType("float");
			Table table = new Table();
			table.setName("device_1");
			table.setColumns(Set.of(temperature));
			dataSource.create(table);
			dataSource.create(table);

			try (Connection connection = DriverManager.getConnection(config.getJdbcUrl(), username, password);
					Statement statement = connection.createStatement()) {
				statement.execute("INSERT INTO " + devicePath + "(time, temperature) VALUES (" + timestamp + ", 23.5)");
				try (ResultSet result = statement
					.executeQuery("SELECT temperature FROM " + devicePath + " WHERE time=" + timestamp)) {
					Assertions.assertThat(result.next()).isTrue();
					ResultSetMetaData metadata = result.getMetaData();
					Assertions.assertThat(metadata.getColumnCount()).isEqualTo(2);
					Assertions.assertThat(result.getFloat(metadata.getColumnLabel(2))).isEqualTo(23.5F);
				}
			}
		}
		finally {
			dataSource.close();
			dropDatabaseIfPresent(config, username, password);
		}
	}

	private static void dropDatabaseIfPresent(IoTDBConfig config, String username, String password) throws Exception {
		try (Connection connection = DriverManager.getConnection(config.getJdbcUrl(), username, password);
				Statement statement = connection.createStatement()) {
			boolean exists = false;
			try (ResultSet databases = statement.executeQuery("SHOW DATABASES")) {
				while (databases.next()) {
					if (config.getDatabasePath().equals(databases.getString("Database"))) {
						exists = true;
						break;
					}
				}
			}
			if (exists) {
				statement.execute("DROP DATABASE " + config.getDatabasePath());
			}
		}
	}

	private static String valueOrDefault(String value, String defaultValue) {
		return value == null || value.isBlank() ? defaultValue : value;
	}

}

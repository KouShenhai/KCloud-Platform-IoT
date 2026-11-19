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
import org.junit.jupiter.api.Test;
import org.laokou.common.storage.Table;
import org.laokou.common.storage.model.enums.IoTDBColumnType;
import org.laokou.common.storage.model.enums.StoragePolicy;
import org.laokou.common.storage.model.valueobject.SourceV;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

/**
 * @author laokou
 */
class IoTDBTest {

	@Test
	void shouldNormalizeEndpointAndDatabasePath() {
		IoTDBConfig config = new IoTDBConfig("localhost", "root", "secret", "root.factory.line1");

		Assertions.assertThat(config.getJdbcUrl()).isEqualTo("jdbc:iotdb://localhost:6667/");
		Assertions.assertThat(config.getDatabasePath()).isEqualTo("root.factory.line1");
	}

	@Test
	void shouldRejectInvalidEndpointAndDatabaseName() {
		Assertions.assertThatThrownBy(() -> new IoTDBConfig("localhost:66666", "root", "secret", "factory"))
			.isInstanceOf(IllegalArgumentException.class);
		Assertions.assertThatThrownBy(() -> new IoTDBConfig("localhost:6667?user=root", "root", "secret", "factory"))
			.isInstanceOf(IllegalArgumentException.class);
		Assertions.assertThatThrownBy(() -> new IoTDBConfig("localhost", "root", "secret", "factory;DROP"))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void shouldBuildTimeseriesStatementsWithoutDatabaseDdlForTreeModel() {
		IoTDBConfig config = new IoTDBConfig("iotdb:6667", "root", "secret", "factory");
		Table.Column temperature = new Table.Column();
		temperature.setName("temperature");
		temperature.setType("float");
		Table.Column online = new Table.Column();
		online.setName("online");
		online.setType("boolean");
		Table table = new Table();
		table.setName("line_1.sensor_2");
		table.setColumns(Set.of(temperature, online));

		Assertions.assertThat(IoTDB.buildCreateStatements(config, table))
			.hasSize(2)
			.noneMatch(sql -> sql.startsWith("CREATE DATABASE") || sql.contains("IF NOT EXISTS"))
			.contains(
					"CREATE TIMESERIES root.factory.line_1.sensor_2.temperature WITH DATATYPE=FLOAT, ENCODING=GORILLA")
			.contains("CREATE TIMESERIES root.factory.line_1.sensor_2.online WITH DATATYPE=BOOLEAN, ENCODING=RLE");
	}

	@Test
	void shouldSkipDatabaseCreationWhenDatabaseAlreadyExists() throws SQLException {
		Statement statement = Mockito.mock(Statement.class);
		ResultSet databases = Mockito.mock(ResultSet.class);
		Mockito.when(statement.executeQuery("SHOW DATABASES")).thenReturn(databases);
		Mockito.when(databases.next()).thenReturn(true, false);
		Mockito.when(databases.getString(1)).thenReturn(" root.factory ");

		IoTDB.ensureDatabase(statement, "root.factory");

		Mockito.verify(statement).executeQuery("SHOW DATABASES");
		Mockito.verify(statement, Mockito.never()).execute("CREATE DATABASE root.factory");
	}

	@Test
	void shouldCreateDatabaseWhenItIsMissing() throws SQLException {
		Statement statement = Mockito.mock(Statement.class);
		ResultSet databases = Mockito.mock(ResultSet.class);
		Mockito.when(statement.executeQuery("SHOW DATABASES")).thenReturn(databases);
		Mockito.when(databases.next()).thenReturn(false);

		IoTDB.ensureDatabase(statement, "root.factory");

		Mockito.verify(statement).execute("CREATE DATABASE root.factory");
	}

	@Test
	void shouldTreatConcurrentDatabaseCreationAsSuccessWhenRecheckFindsIt() throws SQLException {
		Statement statement = Mockito.mock(Statement.class);
		ResultSet beforeCreate = Mockito.mock(ResultSet.class);
		ResultSet afterCreate = Mockito.mock(ResultSet.class);
		SQLException alreadyCreated = new SQLException("database already exists");
		Mockito.when(statement.executeQuery("SHOW DATABASES")).thenReturn(beforeCreate, afterCreate);
		Mockito.when(beforeCreate.next()).thenReturn(false);
		Mockito.when(afterCreate.next()).thenReturn(true, false);
		Mockito.when(afterCreate.getString(1)).thenReturn("root.factory");
		Mockito.when(statement.execute("CREATE DATABASE root.factory")).thenThrow(alreadyCreated);

		IoTDB.ensureDatabase(statement, "root.factory");

		Mockito.verify(statement, Mockito.times(2)).executeQuery("SHOW DATABASES");
	}

	@Test
	void shouldRethrowDatabaseCreationFailureWhenDatabaseRemainsMissing() throws SQLException {
		Statement statement = Mockito.mock(Statement.class);
		ResultSet databases = Mockito.mock(ResultSet.class);
		SQLException createFailure = new SQLException("permission denied");
		Mockito.when(statement.executeQuery("SHOW DATABASES")).thenReturn(databases);
		Mockito.when(databases.next()).thenReturn(false);
		Mockito.when(statement.execute("CREATE DATABASE root.factory")).thenThrow(createFailure);

		Assertions.assertThatThrownBy(() -> IoTDB.ensureDatabase(statement, "root.factory")).isSameAs(createFailure);
	}

	@Test
	void shouldSkipTimeseriesCreationWhenDeviceWildcardShowsItExists() throws SQLException {
		Statement statement = Mockito.mock(Statement.class);
		String devicePath = "root.factory.line_1.sensor_2";
		String timeseriesPath = devicePath + ".temperature";
		Set<String> existingTimeseries = new HashSet<>(Set.of(timeseriesPath));

		IoTDB.ensureTimeseries(statement, devicePath, temperatureSql(timeseriesPath), existingTimeseries);

		Mockito.verify(statement, Mockito.never()).execute(temperatureSql(timeseriesPath));
	}

	@Test
	void shouldCreateMissingTimeseriesAndRememberIt() throws SQLException {
		Statement statement = Mockito.mock(Statement.class);
		String devicePath = "root.factory.line_1.sensor_2";
		String timeseriesPath = devicePath + ".temperature";
		Set<String> existingTimeseries = new HashSet<>();

		IoTDB.ensureTimeseries(statement, devicePath, temperatureSql(timeseriesPath), existingTimeseries);

		Mockito.verify(statement).execute(temperatureSql(timeseriesPath));
		Assertions.assertThat(existingTimeseries).contains(timeseriesPath);
	}

	@Test
	void shouldTreatConcurrentTimeseriesCreationAsSuccessWhenWildcardRecheckFindsIt() throws SQLException {
		Statement statement = Mockito.mock(Statement.class);
		ResultSet afterCreate = Mockito.mock(ResultSet.class);
		String devicePath = "root.factory.line_1.sensor_2";
		String timeseriesPath = devicePath + ".temperature";
		String showSql = "SHOW TIMESERIES " + devicePath + ".*";
		String createSql = temperatureSql(timeseriesPath);
		SQLException alreadyCreated = new SQLException("timeseries already exists");
		Mockito.when(statement.executeQuery(showSql)).thenReturn(afterCreate);
		Mockito.when(afterCreate.next()).thenReturn(true, false);
		Mockito.when(afterCreate.getString(1)).thenReturn(timeseriesPath);
		Mockito.when(statement.execute(createSql)).thenThrow(alreadyCreated);
		Set<String> existingTimeseries = new HashSet<>();

		IoTDB.ensureTimeseries(statement, devicePath, createSql, existingTimeseries);

		Mockito.verify(statement).executeQuery(showSql);
		Assertions.assertThat(existingTimeseries).contains(timeseriesPath);
	}

	@Test
	void shouldRethrowTimeseriesCreationFailureWhenWildcardRecheckDoesNotFindIt() throws SQLException {
		Statement statement = Mockito.mock(Statement.class);
		ResultSet afterCreate = Mockito.mock(ResultSet.class);
		String devicePath = "root.factory.line_1.sensor_2";
		String timeseriesPath = devicePath + ".temperature";
		String showSql = "SHOW TIMESERIES " + devicePath + ".*";
		String createSql = temperatureSql(timeseriesPath);
		SQLException createFailure = new SQLException("permission denied");
		Mockito.when(statement.executeQuery(showSql)).thenReturn(afterCreate);
		Mockito.when(afterCreate.next()).thenReturn(false);
		Mockito.when(statement.execute(createSql)).thenThrow(createFailure);

		Assertions.assertThatThrownBy(() -> IoTDB.ensureTimeseries(statement, devicePath, createSql, new HashSet<>()))
			.isSameAs(createFailure);
	}

	@Test
	void shouldRejectDevicePathOutsideConfiguredDatabase() {
		IoTDBConfig config = new IoTDBConfig("localhost", "root", "secret", "factory");

		Assertions.assertThatThrownBy(() -> IoTDB.resolveDevicePath(config.getDatabasePath(), "root.other.sensor"))
			.isInstanceOf(IllegalArgumentException.class);
		Assertions.assertThatThrownBy(() -> IoTDB.resolveDevicePath(config.getDatabasePath(), "sensor;DROP"))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void shouldMapPlatformTypesToIoTDBTypes() {
		Assertions.assertThat(IoTDBColumnType.fromCode("int")).isEqualTo(IoTDBColumnType.INT32);
		Assertions.assertThat(IoTDBColumnType.fromCode("long")).isEqualTo(IoTDBColumnType.INT64);
		Assertions.assertThat(IoTDBColumnType.fromCode("enum")).isEqualTo(IoTDBColumnType.STRING);
		Assertions.assertThatThrownBy(() -> IoTDBColumnType.fromCode("decimal"))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void shouldResolveTheIotdbStoragePolicy() {
		SourceV source = new SourceV("localhost", "factory", "root", "secret");

		Assertions.assertThat(StoragePolicy.getByCode("iotdb").toDataSource(source)).isInstanceOf(IoTDB.class);
	}

	private static String temperatureSql(String path) {
		return "CREATE TIMESERIES " + path + " WITH DATATYPE=FLOAT, ENCODING=GORILLA";
	}

}

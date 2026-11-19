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

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.i18n.common.exception.BizException;
import org.laokou.common.storage.AbstractDataSource;
import org.laokou.common.storage.Table;
import org.laokou.common.storage.model.enums.IoTDBColumnType;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Apache IoTDB adapter for the Tree data model.
 *
 * @author laokou
 */
@Slf4j
public class IoTDB extends AbstractDataSource {

	private static final Pattern PATH_PATTERN = Pattern.compile("[A-Za-z_][A-Za-z0-9_]*(?:\\.[A-Za-z_][A-Za-z0-9_]*)*");

	private static final int QUERY_TIMEOUT_SECONDS = 5;

	private static final String SHOW_DATABASES_SQL = "SHOW DATABASES";

	private volatile HikariDataSource hikariDataSource;

	public IoTDB(IoTDBConfig config) {
		super(config);
	}

	@Override
	public synchronized void open() {
		if (hikariDataSource != null && !hikariDataSource.isClosed()) {
			return;
		}
		hikariDataSource = getHikariDataSource();
		log.info("IoTDB DataSource opened successfully");
	}

	@Override
	public synchronized void close() {
		HikariDataSource currentDataSource = hikariDataSource;
		hikariDataSource = null;
		if (currentDataSource != null && !currentDataSource.isClosed()) {
			currentDataSource.close();
			log.info("IoTDB DataSource closed successfully");
		}
	}

	/**
	 * Creates the configured database and one Tree-model device schema. The table name is
	 * a device path relative to the configured database; columns are measurements beneath
	 * that device.
	 */
	@Override
	public void create(Table table) {
		IoTDBConfig iotdbConfig = (IoTDBConfig) config;
		List<String> statements = buildCreateStatements(iotdbConfig, table);
		String devicePath = resolveDevicePath(iotdbConfig.getDatabasePath(), table.getName());
		HikariDataSource dataSource = requireOpenDataSource();
		try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement()) {
			statement.setQueryTimeout(QUERY_TIMEOUT_SECONDS);
			ensureDatabase(statement, iotdbConfig.getDatabasePath());
			Set<String> existingTimeseries = getTimeseriesPaths(statement, devicePath);
			for (String sql : statements) {
				ensureTimeseries(statement, devicePath, sql, existingTimeseries);
			}
		}
		catch (SQLException ex) {
			log.error("IoTDB schema creation failed: {}", ex.getMessage(), ex);
			throw new BizException("B_Datasource_IoTDBSchemaCreationFailed", "创建 IoTDB 数据库或时序结构失败", ex);
		}
	}

	@Override
	public void test() {
		HikariDataSource dataSource = hikariDataSource;
		boolean temporaryDataSource = dataSource == null || dataSource.isClosed();
		if (temporaryDataSource) {
			dataSource = getHikariDataSource();
		}
		try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement()) {
			statement.setQueryTimeout(QUERY_TIMEOUT_SECONDS);
			statement.execute("SHOW DATABASES");
			log.info("测试 IoTDB 数据源连接成功");
		}
		catch (SQLException ex) {
			log.error("测试 IoTDB 数据源连接失败，错误信息：{}", ex.getMessage(), ex);
			throw new BizException("B_Datasource_IoTDBConnectionFailed", "连接 IoTDB 数据源失败，请检查数据源配置", ex);
		}
		finally {
			if (temporaryDataSource && dataSource != null && !dataSource.isClosed()) {
				dataSource.close();
			}
		}
	}

	static List<String> buildCreateStatements(IoTDBConfig config, Table table) {
		if (table == null) {
			throw new IllegalArgumentException("IoTDB table definition is required");
		}
		String devicePath = resolveDevicePath(config.getDatabasePath(), table.getName());
		Set<Table.Column> columns = table.getColumns();
		if (columns == null || columns.isEmpty()) {
			throw new IllegalArgumentException("IoTDB device must contain at least one measurement");
		}
		List<String> statements = new ArrayList<>(columns.size());
		for (Table.Column column : columns) {
			if (column == null) {
				throw new IllegalArgumentException("IoTDB measurement definition is required");
			}
			String measurementName = validatePath(column.getName(), "measurement name");
			if (measurementName.contains(".")) {
				throw new IllegalArgumentException("IoTDB measurement name must be a single path node");
			}
			IoTDBColumnType type = IoTDBColumnType.fromCode(column.getType());
			statements.add(createTimeseriesSql(devicePath + "." + measurementName, type));
		}
		return List.copyOf(statements);
	}

	static void ensureDatabase(Statement statement, String databasePath) throws SQLException {
		ensurePath(statement, SHOW_DATABASES_SQL, databasePath, "CREATE DATABASE " + databasePath);
	}

	static void ensureTimeseries(Statement statement, String devicePath, String createSql,
			Set<String> existingTimeseries) throws SQLException {
		String timeseriesPath = getTimeseriesPath(createSql);
		if (existingTimeseries.contains(timeseriesPath)) {
			return;
		}
		try {
			statement.execute(createSql);
			existingTimeseries.add(timeseriesPath);
		}
		catch (SQLException createException) {
			try {
				Set<String> currentTimeseries = getTimeseriesPaths(statement, devicePath);
				if (currentTimeseries.contains(timeseriesPath)) {
					existingTimeseries.add(timeseriesPath);
					return;
				}
			}
			catch (SQLException verificationException) {
				createException.addSuppressed(verificationException);
			}
			throw createException;
		}
	}

	private static String getTimeseriesPath(String createSql) {
		String createPrefix = "CREATE TIMESERIES ";
		String datatypeClause = " WITH DATATYPE=";
		int pathStart = createPrefix.length();
		int pathEnd = createSql.indexOf(datatypeClause);
		if (!createSql.startsWith(createPrefix) || pathEnd <= pathStart) {
			throw new IllegalArgumentException("Invalid IoTDB timeseries creation statement");
		}
		return createSql.substring(pathStart, pathEnd);
	}

	private static void ensurePath(Statement statement, String showSql, String path, String createSql)
			throws SQLException {
		if (pathExists(statement, showSql, path)) {
			return;
		}
		try {
			statement.execute(createSql);
		}
		catch (SQLException createException) {
			try {
				if (pathExists(statement, showSql, path)) {
					return;
				}
			}
			catch (SQLException verificationException) {
				createException.addSuppressed(verificationException);
			}
			throw createException;
		}
	}

	private static Set<String> getTimeseriesPaths(Statement statement, String devicePath) throws SQLException {
		Set<String> paths = new HashSet<>();
		try (ResultSet results = statement.executeQuery("SHOW TIMESERIES " + devicePath + ".*")) {
			while (results.next()) {
				String path = results.getString(1);
				if (path != null && !path.isBlank()) {
					paths.add(path.trim());
				}
			}
		}
		return paths;
	}

	private static boolean pathExists(Statement statement, String showSql, String path) throws SQLException {
		try (ResultSet results = statement.executeQuery(showSql)) {
			while (results.next()) {
				String existingPath = results.getString(1);
				if (path.equals(existingPath == null ? null : existingPath.trim())) {
					return true;
				}
			}
		}
		return false;
	}

	static String resolveDevicePath(String databasePath, String tableName) {
		String validName = validatePath(tableName, "device name");
		String databasePrefix = databasePath + ".";
		if (validName.startsWith("root.")) {
			if (!validName.startsWith(databasePrefix) || validName.length() == databasePrefix.length()) {
				throw new IllegalArgumentException("IoTDB device path must stay inside the configured database");
			}
			return validName;
		}
		return databasePrefix + validName;
	}

	static String createTimeseriesSql(String path, IoTDBColumnType type) {
		String validPath = validatePath(path, "timeseries path");
		return "CREATE TIMESERIES " + validPath + " WITH DATATYPE=" + type.getDataType() + ", ENCODING="
				+ type.getEncoding();
	}

	private static String validatePath(String value, String name) {
		if (value == null || !PATH_PATTERN.matcher(value.trim()).matches()) {
			throw new IllegalArgumentException("IoTDB " + name + " must contain valid path nodes");
		}
		return value.trim();
	}

	private HikariDataSource requireOpenDataSource() {
		HikariDataSource dataSource = hikariDataSource;
		if (dataSource == null || dataSource.isClosed()) {
			throw new IllegalStateException("IoTDB data source is not open");
		}
		return dataSource;
	}

	private HikariDataSource getHikariDataSource() {
		IoTDBConfig iotdbConfig = (IoTDBConfig) config;
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setDriverClassName("org.apache.iotdb.jdbc.IoTDBDriver");
		hikariConfig.setJdbcUrl(iotdbConfig.getJdbcUrl());
		hikariConfig.setUsername(iotdbConfig.getUsername());
		hikariConfig.setPassword(iotdbConfig.getPassword());
		hikariConfig.setPoolName(String.format("iot_iotdb_%d", System.currentTimeMillis()));
		hikariConfig.setAutoCommit(true);
		hikariConfig.setConnectionTestQuery("SHOW DATABASES");
		hikariConfig.setConnectionTimeout(10000);
		hikariConfig.setValidationTimeout(3000);
		hikariConfig.setIdleTimeout(300000);
		hikariConfig.setMaxLifetime(900000);
		hikariConfig.setMinimumIdle(1);
		hikariConfig.setMaximumPoolSize(10);
		hikariConfig.setInitializationFailTimeout(1000);
		return new HikariDataSource(hikariConfig);
	}

}

/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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

package org.laokou.flyway;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author laokou
 */
public class CreateTable {

	private static final String URL = "jdbc:mysql://192.168.30.133:3306/?useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai&useSSL=false";

	private static final String USERNAME = "root";

	private static final String PASSWORD = "laokou123";

	public static void main(String[] args) {
		List<String> tableList = List.of("kcloud_platform_alibaba", "kcloud_platform_alibaba_flowable",
				"kcloud_platform_alibaba_nacos", "kcloud_platform_alibaba_tenant", "kcloud_platform_alibaba_user",
				"kcloud_platform_alibaba_xxl_job", "kcloud_platform_alibaba_seata",
				"kcloud_platform_alibaba_login_log");
		tableList.forEach(item -> {
			Connection connection;
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
			}
			catch (Exception e) {
				throw new RuntimeException("数据源驱动加载失败，请检查相关配置");
			}
			try {
				connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			}
			catch (Exception e) {
				throw new RuntimeException("数据源连接失败，请检查相关配置");
			}
			try {
				Statement statement;
				try {
					statement = connection.createStatement();
				}
				catch (SQLException e) {
					throw new RuntimeException(e);
				}
				try {
					statement.executeUpdate(createDB(item));
				}
				catch (SQLException e) {
					throw new RuntimeException(e);
				}
				try {
					statement.executeUpdate(createTable(item));
				}
				catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
			finally {
				if (connection != null) {
					try {
						connection.close();
					}
					catch (SQLException e) {
						throw new RuntimeException(e);
					}
				}
			}
		});
	}

	private static String createDB(String dbName) {
		return String.format("""
				create database if not exists `%s`;
				""", dbName);
	}

	private static String createTable(String dbName) {
		return String.format("""
				CREATE TABLE IF NOT EXISTS `%s`.`flyway_schema_history` (
				                                         `installed_rank` int NOT NULL,
				                                         `version` varchar(50) DEFAULT NULL,
				                                         `description` varchar(200) NOT NULL,
				                                         `type` varchar(20) NOT NULL,
				                                         `script` varchar(1000) NOT NULL,
				                                         `checksum` int DEFAULT NULL,
				                                         `installed_by` varchar(100) NOT NULL,
				                                         `installed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
				                                         `execution_time` int NOT NULL,
				                                         `success` tinyint(1) NOT NULL,
				                                         PRIMARY KEY (`installed_rank`),
				                                         KEY `flyway_schema_history_s_idx` (`success`)
				) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
				""", dbName);
	}

}

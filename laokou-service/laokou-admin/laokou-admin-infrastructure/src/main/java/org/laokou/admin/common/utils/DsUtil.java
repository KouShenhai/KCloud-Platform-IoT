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

package org.laokou.admin.common.utils;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DataSourceProperty;
import com.baomidou.dynamic.datasource.creator.hikaricp.HikariDataSourceCreator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.gatewayimpl.database.SourceMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.SourceDO;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.i18n.common.GlobalException;
import org.laokou.common.i18n.utils.StringUtil;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.laokou.common.mybatisplus.template.DsConstant.*;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DsUtil {

	private final SourceMapper sourceMapper;

	private final DynamicUtil dynamicUtil;

	private static final String SHOW_TABLES = "show tables";

	private static final List<String> TABLES = List.of(BOOT_SYS_DICT, BOOT_SYS_MESSAGE, BOOT_SYS_MESSAGE_DETAIL,
			BOOT_SYS_OSS, BOOT_SYS_OSS_LOG);

	public String loadDs(String sourceName) {
		if (StringUtil.isEmpty(sourceName)) {
			throw new GlobalException("数据源名称不能为空");
		}
		if (!validateDs(sourceName)) {
			addDs(sourceName);
		}
		return sourceName;
	}

	private void addDs(String sourceName) {
		SourceDO source = sourceMapper.getSourceByName(sourceName);
		DataSourceProperty properties = new DataSourceProperty();
		properties.setUsername(source.getUsername());
		properties.setPassword(source.getPassword());
		properties.setUrl(source.getUrl());
		properties.setDriverClassName(source.getDriverClassName());
		// 连接数据源
		connectDs(properties);
		DynamicRoutingDataSource dynamicRoutingDataSource = dynamicUtil.getDataSource();
		HikariDataSourceCreator hikariDataSourceCreator = dynamicUtil.getHikariDataSourceCreator();
		DataSource dataSource = hikariDataSourceCreator.createDataSource(properties);
		dynamicRoutingDataSource.addDataSource(sourceName, dataSource);
	}

	private boolean validateDs(String sourceName) {
		Map<String, DataSource> dataSources = dynamicUtil.getDataSources();
		return dataSources.containsKey(sourceName);
	}

	/**
	 * 连接数据库
	 */
	@SneakyThrows
	private void connectDs(DataSourceProperty properties) {
		Connection connection;
		try {
			Class.forName(properties.getDriverClassName());
		}
		catch (Exception e) {
			log.error("数据源驱动加载失败，错误信息：{}", e.getMessage());
			throw new GlobalException("数据源驱动加载失败，请检查相关配置");
		}
		try {
			// 5秒后连接超时
			DriverManager.setLoginTimeout(5);
			connection = DriverManager.getConnection(properties.getUrl(), properties.getUsername(),
					properties.getPassword());
		}
		catch (Exception e) {
			log.error("数据源连接超时，错误信息：{}", e.getMessage());
			throw new GlobalException("数据源连接超时，请检查相关配置");
		}
		try {
			ResultSet rs = connection.prepareStatement(SHOW_TABLES).executeQuery();
			List<String> tables = new ArrayList<>(TABLES.size());
			while (rs.next()) {
				String tableName = rs.getString(1);
				if (TABLES.contains(tableName)) {
					tables.add(tableName);
				}
			}
			if (CollectionUtil.isEmpty(tables)) {
				throw new GlobalException("未初始化表结构");
			}
			List<String> list = TABLES.stream().filter(i -> !tables.contains(i)).toList();
			if (CollectionUtil.isNotEmpty(list)) {
				throw new GlobalException(String.format("%s不存在，请检查数据表", String.join("、", list)));
			}
		}
		finally {
			if (connection != null) {
				connection.close();
			}
		}
	}

}

/*
 * Copyright (c) 2022-2024 KCloud-Platform-IOT Author or Authors. All Rights Reserved.
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

import com.baomidou.dynamic.datasource.creator.DataSourceProperty;
import com.baomidou.dynamic.datasource.creator.hikaricp.HikariDataSourceCreator;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.config.DefaultConfigProperties;
import org.laokou.admin.gatewayimpl.database.SourceMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.SourceDO;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.mybatisplus.utils.DynamicUtil;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 数据源工具类.
 *
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DsUtil {

	private static final String SHOW_TABLES = "show tables";

	private final SourceMapper sourceMapper;

	private final DynamicUtil dynamicUtil;

	private final DefaultConfigProperties defaultConfigProperties;

	/**
	 * 根据数据源名称加载数据源至本地内存.
	 * @param sourceName 数据源名称
	 * @return 数据源名称
	 */
	public String loadDs(String sourceName) {
		if (StringUtil.isEmpty(sourceName)) {
			throw new RuntimeException("数据源名称不能为空");
		}
		if (validateDs(sourceName)) {
			addDs(sourceName);
		}
		return sourceName;
	}

	public void addDs(String sourceName, DataSourceProperty properties) {
		// 校验数据源
		validateDs(properties);
		dynamicUtil.getDataSource().addDataSource(sourceName, dataSource(properties));
	}

	public DataSourceProperty properties(SourceDO source) {
		DataSourceProperty properties = new DataSourceProperty();
		properties.setUsername(source.getUsername());
		properties.setPassword(source.getPassword());
		properties.setUrl(source.getUrl());
		properties.setDriverClassName(source.getDriverClassName());
		return properties;
	}

	private DataSource dataSource(DataSourceProperty properties) {
		HikariDataSourceCreator hikariDataSourceCreator = dynamicUtil.getHikariDataSourceCreator();
		return hikariDataSourceCreator.createDataSource(properties);
	}

	private void addDs(String sourceName) {
		SourceDO source = sourceMapper
			.selectOne(Wrappers.lambdaQuery(SourceDO.class).eq(SourceDO::getName, sourceName));
		addDs(sourceName, properties(source));
	}

	public boolean validateDs(String sourceName) {
		return !dynamicUtil.getDataSources().containsKey(sourceName);
	}

	@SneakyThrows
	private void validateDs(DataSourceProperty properties) {
		Connection connection;
		PreparedStatement ps = null;
		try {
			Class.forName(properties.getDriverClassName());
		}
		catch (Exception e) {
			log.error("加载数据源驱动失败，错误信息：{}，详情见日志", LogUtil.record(e.getMessage()), e);
			// throw new DataSourceException(CUSTOM_SERVER_ERROR, "加载数据源驱动失败");
		}
		try {
			// 1秒后连接超时
			DriverManager.setLoginTimeout(1);
			connection = DriverManager.getConnection(properties.getUrl(), properties.getUsername(),
					properties.getPassword());
		}
		catch (Exception e) {
			log.error("数据源连接超时，错误信息：{}，详情见日志", LogUtil.record(e.getMessage()), e);
			throw e;
			// throw new RuntimeException(CUSTOM_SERVER_ERROR, "数据源连接超时");
		}
		try {
			ps = connection.prepareStatement(SHOW_TABLES);
			ResultSet rs = ps.executeQuery();
			Set<String> defaultTenantTables = defaultConfigProperties.getTenantTables();
			Set<String> tables = new HashSet<>(defaultTenantTables.size());
			while (rs.next()) {
				String tableName = rs.getString(1);
				if (defaultTenantTables.contains(tableName)) {
					tables.add(tableName);
				}
			}
			Set<String> list;
			if (CollectionUtil.isNotEmpty(tables)) {
				list = defaultTenantTables.parallelStream()
					.filter(table -> !tables.contains(table))
					.collect(Collectors.toSet());
			}
			else {
				list = defaultTenantTables;
			}
			if (CollectionUtil.isNotEmpty(list)) {
				// throw new DataSourceException(
				// String.format("表 %s 不存在", StringUtil.collectionToDelimitedString(list,
				// DROP)));
			}
		}
		finally {
			if (ObjectUtil.isNotNull(connection)) {
				connection.close();
			}
			JdbcUtils.closeStatement(ps);
		}
	}

}

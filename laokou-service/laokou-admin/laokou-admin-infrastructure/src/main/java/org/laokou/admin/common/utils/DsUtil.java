/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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
import org.laokou.common.i18n.common.exception.DataSourceException;
import org.laokou.common.i18n.utils.StringUtil;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static org.laokou.common.i18n.common.Constant.DROP;
import static org.laokou.common.mybatisplus.constant.DsConstant.*;

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
			throw new DataSourceException("数据源名称不能为空");
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
		DynamicRoutingDataSource dynamicRoutingDataSource = dynamicUtil.getDataSource();
		HikariDataSourceCreator hikariDataSourceCreator = dynamicUtil.getHikariDataSourceCreator();
		DataSource dataSource = hikariDataSourceCreator.createDataSource(properties);
		// 连接数据源
		validateDs(dataSource);
		dynamicRoutingDataSource.addDataSource(sourceName, dataSource);
	}

	private boolean validateDs(String sourceName) {
		return dynamicUtil.getDataSources().containsKey(sourceName);
	}

	/**
	 * 连接数据库
	 */
	@SneakyThrows
	private void validateDs(DataSource dataSource) {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = DataSourceUtils.getConnection(dataSource);
			ps = connection.prepareStatement(SHOW_TABLES);
			ResultSet rs = ps.executeQuery();
			List<String> tables = new ArrayList<>(TABLES.size());
			while (rs.next()) {
				String tableName = rs.getString(1);
				if (TABLES.contains(tableName)) {
					tables.add(tableName);
				}
			}
			List<String> list;
			if (CollectionUtil.isNotEmpty(tables)) {
				list = TABLES.parallelStream().filter(table -> !tables.contains(table)).toList();
			} else {
				list = TABLES;
			}
			if (CollectionUtil.isNotEmpty(list)) {
				throw new DataSourceException(String.format("%s不存在", String.join(DROP, list)));
			}
		}
		catch (Exception e) {
			log.error("数据源连接超时，错误信息：{}", e.getMessage());
			throw new DataSourceException("数据源连接超时");
		} finally {
			JdbcUtils.closeStatement(ps);
			DataSourceUtils.releaseConnection(connection, dataSource);
		}
	}

}

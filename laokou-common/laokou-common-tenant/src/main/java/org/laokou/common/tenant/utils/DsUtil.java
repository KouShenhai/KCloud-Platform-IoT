/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.laokou.common.tenant.utils;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DataSourceProperty;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.i18n.utils.StringUtil;
import org.laokou.common.i18n.core.CustomException;
import org.laokou.common.mybatisplus.utils.DynamicUtil;
import org.laokou.common.tenant.service.SysSourceService;
import org.laokou.common.tenant.vo.SysSourceVO;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DsUtil {

	private final SysSourceService sysSourceService;

	private final DynamicUtil dynamicUtil;

	private static final List<String> TABLES = List.of("boot_sys_dict", "boot_sys_message", "boot_sys_message_detail",
			"boot_sys_oss", "boot_sys_oss_log");

	public String loadDs(String sourceName) {
		if (StringUtil.isEmpty(sourceName)) {
			throw new CustomException("数据源名称不能为空");
		}
		if (!checkDs(sourceName)) {
			addDs(sourceName);
		}
		return sourceName;
	}

	private void addDs(String sourceName) {
		SysSourceVO sourceVO = sysSourceService.querySource(sourceName);
		DataSourceProperty properties = new DataSourceProperty();
		properties.setUsername(sourceVO.getUsername());
		properties.setPassword(sourceVO.getPassword());
		properties.setUrl(sourceVO.getUrl());
		properties.setDriverClassName(sourceVO.getDriverClassName());
		// 验证数据源
		connDs(properties);
		DynamicRoutingDataSource dynamicRoutingDataSource = dynamicUtil.getDataSource();
		DefaultDataSourceCreator dataSourceCreator = dynamicUtil.getDefaultDataSourceCreator();
		DataSource dataSource = dataSourceCreator.createDataSource(properties);
		dynamicRoutingDataSource.addDataSource(sourceName, dataSource);
	}

	private boolean checkDs(String sourceName) {
		Map<String, DataSource> dataSources = dynamicUtil.getDataSources();
		return dataSources.containsKey(sourceName);
	}

	/**
	 * 连接数据库
	 */
	@SneakyThrows
	private void connDs(DataSourceProperty properties) {
		Connection connection;
		try {
			Class.forName(properties.getDriverClassName());
		}
		catch (Exception e) {
			log.error("数据源驱动加载失败，错误信息：{}", e.getMessage());
			throw new CustomException("数据源驱动加载失败，请检查相关配置");
		}
		try {
			connection = DriverManager.getConnection(properties.getUrl(), properties.getUsername(),
					properties.getPassword());
		}
		catch (Exception e) {
			log.error("数据源连接失败，错误信息：{}", e.getMessage());
			throw new CustomException("数据源连接失败，请检查相关配置");
		}
		try {
			String sql = """
						select table_name from information_schema.tables where table_schema = (select database())
						""";
			ResultSet resultSet = connection.prepareStatement(sql).executeQuery();
			List<String> tables = new ArrayList<>(TABLES.size());
			while (resultSet.next()) {
				String tableName = resultSet.getString("table_name");
				tables.add(tableName);
			}
			List<String> list = TABLES.stream().filter(item -> !tables.contains(item)).collect(Collectors.toList());
			if (CollectionUtil.isNotEmpty(list)) {
				throw new CustomException(String.format("%s不存在，请检查数据库表", String.join("、", list)));
			}
		}
		finally {
			if (connection != null) {
				connection.close();
			}
		}
	}

}

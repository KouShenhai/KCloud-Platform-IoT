/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.auth.gatewayimpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.auth.gateway.SourceGateway;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.tenant.mapper.SourceDO;
import org.laokou.common.tenant.mapper.SourceMapper;
import org.springframework.stereotype.Component;

/**
 * 数据源.
 *
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SourceGatewayImpl implements SourceGateway {

	private final SourceMapper sourceMapper;

	/**
	 * 查看数据源.
	 * @param tenantCode 租户编号
	 * @return 数据源
	 */
	@Override
	public String getPrefix(String tenantCode) {
		SourceDO sourceDO = sourceMapper.selectOneByTenantCode(tenantCode);
		return ObjectUtil.isNull(sourceDO) ? null : sourceDO.getName();
	}

	// /**
	// * 根据租户ID查看数据源名称.
	// * @param sourceDO 数据源数据模型
	// * @return 数据源名称
	// */
	// private String getSourceName(SourceDO sourceDO) {
	// String sourceName = sourceDO.getName();
	// try {
	// if (!dynamicUtil.getDataSources().containsKey(sourceName)) {
	// DataSourceProperty properties = properties(sourceDO);
	// // 检查数据库连接
	// checkConnect(properties);
	// dynamicUtil.getDataSource().addDataSource(sourceName, toDataSource(properties));
	// }
	// }
	// finally {
	// DynamicDataSourceContextHolder.push(sourceName);
	// }
	// return sourceName;
	// }
	//
	// private DataSource toDataSource(DataSourceProperty properties) {
	// HikariDataSourceCreator hikariDataSourceCreator =
	// return hikariDataSourceCreator.createDataSource(properties);
	// }
	//
	// /**
	// * 验证码数据源.
	// * @param properties 数据源属性配置
	// */
	// @SneakyThrows
	// private void checkConnect(DataSourceProperty properties) {
	// Connection connection = null;
	// try {
	// Class.forName(properties.getDriverClassName());
	// }
	// catch (Exception e) {
	// log.error("加载数据源驱动失败，错误信息：{}", StringUtil.isEmpty(e.getMessage()) ?
	// e.getMessage());
	// // throw new DataSourceException(CUSTOM_SERVER_ERROR, "加载数据源驱动失败");
	// }
	// try {
	// // 1秒后连接超时
	// DriverManager.setLoginTimeout(1);
	// connection = DriverManager.getConnection(properties.getUrl(),
	// properties.getUsername(),
	// properties.getPassword());
	// }
	// catch (Exception e) {
	// log.error("数据源连接超时，错误信息：{}", e.getMessage());
	// // throw new DataSourceException(CUSTOM_SERVER_ERROR, "数据源连接超时");
	// }
	// finally {
	// if (ObjectUtil.isNotNull(connection)) {
	// connection.close();
	// }
	// }
	// }

}

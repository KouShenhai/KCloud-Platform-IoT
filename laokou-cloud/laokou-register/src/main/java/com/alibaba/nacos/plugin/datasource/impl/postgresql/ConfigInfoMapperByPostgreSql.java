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

/*
 * Copyright 1999-2022 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.nacos.plugin.datasource.impl.postgresql;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacos.common.utils.NamespaceUtil;
import com.alibaba.nacos.common.utils.StringUtils;
import com.alibaba.nacos.plugin.datasource.constants.DataSourceConstant;
import com.alibaba.nacos.plugin.datasource.mapper.AbstractMapper;
import com.alibaba.nacos.plugin.datasource.mapper.ConfigInfoMapper;
import com.alibaba.nacos.plugin.datasource.model.MapperContext;
import com.alibaba.nacos.plugin.datasource.model.MapperResult;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The postgresql implementation of ConfigInfoMapper.
 *
 * @author hyx
 * @author laokou
 **/

public class ConfigInfoMapperByPostgreSql extends AbstractMapper implements ConfigInfoMapper {

	@Override
	public String getDataSource() {
		return DataSourceConstant.POSTGRESQL;
	}

	@Override
	public MapperResult findConfigInfoByAppFetchRows(MapperContext context) {
		int startRow = context.getStartRow();
		int pageSize = context.getPageSize();
		String appName = (String) context.getWhereParameter("app_name");
		String tenantId = (String) context.getWhereParameter("tenantId");
		String sql = "SELECT id,data_id,group_id,tenant_id,app_name,content FROM config_info"
				+ " WHERE tenant_id LIKE ? AND app_name= ?" + " LIMIT " + pageSize + " OFFSET " + startRow;
		;
		return new MapperResult(sql, CollectionUtils.list(new Object[] { tenantId, appName }));
	}

	@Override
	public MapperResult getTenantIdList(MapperContext context) {
		int startRow = context.getStartRow();
		int pageSize = context.getPageSize();
		String sql = "SELECT tenant_id FROM config_info WHERE tenant_id != '" + NamespaceUtil.getNamespaceDefaultId()
				+ "' GROUP BY tenant_id LIMIT " + pageSize + " OFFSET " + startRow;
		return new MapperResult(sql, Collections.emptyList());
	}

	@Override
	public MapperResult getGroupIdList(MapperContext context) {
		int startRow = context.getStartRow();
		int pageSize = context.getPageSize();
		String sql = "SELECT group_id FROM config_info WHERE tenant_id ='" + NamespaceUtil.getNamespaceDefaultId()
				+ "' GROUP BY group_id LIMIT " + pageSize + " OFFSET " + startRow;
		return new MapperResult(sql, Collections.emptyList());
	}

	@Override
	public MapperResult findAllConfigKey(MapperContext context) {
		int startRow = context.getStartRow();
		int pageSize = context.getPageSize();
		String sql = " SELECT data_id,group_id,app_name  FROM ( "
				+ " SELECT id FROM config_info WHERE tenant_id LIKE ? ORDER BY id LIMIT " + pageSize + " OFFSET "
				+ startRow + " )" + " g, config_info t WHERE g.id = t.id  ";
		;
		return new MapperResult(sql, CollectionUtils.list(context.getWhereParameter("tenantId")));
	}

	@Override
	public MapperResult findAllConfigInfoBaseFetchRows(MapperContext mapperContext) {
		String sql = "SELECT t.id,data_id,group_id,content,md5"
				+ " FROM ( SELECT id FROM config_info ORDER BY id LIMIT ? OFFSET ?  ) "
				+ " g, config_info t  WHERE g.id = t.id ";
		return new MapperResult(sql, Collections.emptyList());
	}

	@Override
	public MapperResult findAllConfigInfoFragment(MapperContext context) {
		int startRow = context.getStartRow();
		int pageSize = context.getPageSize();
		String sql = "SELECT id,data_id,group_id,tenant_id,app_name,content,md5,gmt_modified,type,encrypted_data_key "
				+ "FROM config_info WHERE id > ? ORDER BY id ASC LIMIT " + pageSize + " OFFSET " + startRow;
		return new MapperResult(sql, CollectionUtils.list(context.getWhereParameter("id")));
	}

	@Override
	public MapperResult findChangeConfigFetchRows(MapperContext context) {
		int pageSize = context.getPageSize();
		String tenant = (String) context.getWhereParameter("tenantId");
		String dataId = (String) context.getWhereParameter("dataId");
		String group = (String) context.getWhereParameter("groupId");
		String appName = (String) context.getWhereParameter("app_name");
		String tenantTmp = StringUtils.isBlank(tenant) ? "" : tenant;
		Timestamp startTime = (Timestamp) context.getWhereParameter("startTime");
		Timestamp endTime = (Timestamp) context.getWhereParameter("endTime");
		String lastMaxId = (String) context.getWhereParameter("lastMaxId");
		List<Object> paramList = new ArrayList<>(6);
		String sqlFetchRows = "SELECT id,data_id,group_id,tenant_id,app_name,content,type,md5,gmt_modified FROM config_info WHERE ";
		String where = " 1=1 ";
		if (!StringUtils.isBlank(dataId)) {
			where += " AND data_id LIKE ? ";
			paramList.add(dataId);
		}
		if (!StringUtils.isBlank(group)) {
			where += " AND group_id LIKE ? ";
			paramList.add(group);
		}
		if (!StringUtils.isBlank(tenantTmp)) {
			where += " AND tenant_id = ? ";
			paramList.add(tenantTmp);
		}
		if (!StringUtils.isBlank(appName)) {
			where += " AND app_name = ? ";
			paramList.add(appName);
		}
		if (startTime != null) {
			where += " AND gmt_modified >= ? ";
			paramList.add(startTime);
		}
		if (endTime != null) {
			where += " AND gmt_modified <= ? ";
			paramList.add(endTime);
		}
		sqlFetchRows = sqlFetchRows + where + " AND id > " + lastMaxId + " ORDER BY id ASC" + " LIMIT " + pageSize
				+ " OFFSET " + 0;
		return new MapperResult(sqlFetchRows, paramList);
	}

	@Override
	public MapperResult listGroupKeyMd5ByPageFetchRows(MapperContext context) {
		int startRow = context.getStartRow();
		int pageSize = context.getPageSize();
		String sql = "SELECT t.id,data_id,group_id,tenant_id,app_name,md5,type,gmt_modified,encrypted_data_key FROM "
				+ "( SELECT id FROM config_info ORDER BY id LIMIT " + pageSize + " OFFSET " + startRow
				+ " ) g, config_info t WHERE g.id = t.id";
		;
		return new MapperResult(sql, Collections.emptyList());
	}

	@Override
	public MapperResult findConfigInfoBaseLikeFetchRows(MapperContext context) {
		int startRow = context.getStartRow();
		int pageSize = context.getPageSize();
		String dataId = (String) context.getWhereParameter("dataId");
		String group = (String) context.getWhereParameter("groupId");
		String content = (String) context.getWhereParameter("content");
		List<Object> paramList = new ArrayList<>(3);
		String sqlFetchRows = "SELECT id,data_id,group_id,tenant_id,content FROM config_info WHERE ";
		String where = " 1=1 AND tenant_id='" + NamespaceUtil.getNamespaceDefaultId() + "' ";
		if (!StringUtils.isBlank(dataId)) {
			where += " AND data_id LIKE ? ";
			paramList.add(dataId);
		}
		if (!StringUtils.isBlank(group)) {
			where += " AND group_id LIKE ";
			paramList.add(group);
		}
		if (!StringUtils.isBlank(content)) {
			where += " AND content LIKE ? ";
			paramList.add(content);
		}
		sqlFetchRows = sqlFetchRows + where + " LIMIT " + pageSize + " OFFSET " + startRow;
		return new MapperResult(sqlFetchRows, paramList);
	}

	@Override
	public MapperResult findConfigInfo4PageFetchRows(MapperContext context) {
		int startRow = context.getStartRow();
		int pageSize = context.getPageSize();
		String tenant = (String) context.getWhereParameter("tenantId");
		String dataId = (String) context.getWhereParameter("dataId");
		String group = (String) context.getWhereParameter("groupId");
		String appName = (String) context.getWhereParameter("app_name");
		String content = (String) context.getWhereParameter("content");
		List<Object> paramList = new ArrayList<>(5);
		String sql = "SELECT id,data_id,group_id,tenant_id,app_name,content,type,encrypted_data_key FROM config_info";
		StringBuilder where = new StringBuilder(" WHERE ");
		where.append(" tenant_id = ? ");
		paramList.add(tenant);
		if (StringUtils.isNotBlank(dataId)) {
			where.append(" AND data_id = ? ");
			paramList.add(dataId);
		}
		if (StringUtils.isNotBlank(group)) {
			where.append(" AND group_id = ? ");
			paramList.add(group);
		}
		if (StringUtils.isNotBlank(appName)) {
			where.append(" AND app_name = ? ");
			paramList.add(appName);
		}
		if (!StringUtils.isBlank(content)) {
			where.append(" AND content LIKE ? ");
			paramList.add(content);
		}
		sql = sql + where + " LIMIT " + pageSize + " OFFSET " + startRow;
		return new MapperResult(sql, paramList);
	}

	@Override
	public MapperResult findConfigInfoBaseByGroupFetchRows(MapperContext context) {
		int startRow = context.getStartRow();
		int pageSize = context.getPageSize();
		String sql = "SELECT id,data_id,group_id,content FROM config_info WHERE group_id=? AND tenant_id=?" + " LIMIT "
				+ pageSize + " OFFSET " + startRow;
		;
		return new MapperResult(sql,
				CollectionUtils.list(context.getWhereParameter("groupId"), context.getWhereParameter("tenantId")));
	}

	@Override
	public MapperResult findConfigInfoLike4PageFetchRows(MapperContext context) {
		int startRow = context.getStartRow();
		int pageSize = context.getPageSize();
		String tenant = (String) context.getWhereParameter("tenantId");
		String dataId = (String) context.getWhereParameter("dataId");
		String group = (String) context.getWhereParameter("groupId");
		String appName = (String) context.getWhereParameter("app_name");
		String content = (String) context.getWhereParameter("content");
		List<Object> paramList = new ArrayList<>(5);
		String sqlFetchRows = "SELECT id,data_id,group_id,tenant_id,app_name,content,encrypted_data_key FROM config_info";
		StringBuilder where = new StringBuilder(" WHERE ");
		where.append(" tenant_id LIKE ? ");
		paramList.add(tenant);
		if (!StringUtils.isBlank(dataId)) {
			where.append(" AND data_id LIKE ? ");
			paramList.add(dataId);
		}
		if (!StringUtils.isBlank(group)) {
			where.append(" AND group_id LIKE ? ");
			paramList.add(group);
		}
		if (!StringUtils.isBlank(appName)) {
			where.append(" AND app_name = ? ");
			paramList.add(appName);
		}
		if (!StringUtils.isBlank(content)) {
			where.append(" AND content LIKE ? ");
			paramList.add(content);
		}
		sqlFetchRows = sqlFetchRows + where + " LIMIT " + pageSize + " OFFSET " + startRow;
		return new MapperResult(sqlFetchRows, paramList);
	}

	@Override
	public MapperResult findAllConfigInfoFetchRows(MapperContext context) {
		String sql = "SELECT t.id,data_id,group_id,tenant_id,app_name,content,md5 "
				+ " FROM (  SELECT id FROM config_info WHERE tenant_id LIKE ? ORDER BY id LIMIT ? OFFSET ? )"
				+ " g, config_info t  WHERE g.id = t.id ";
		return new MapperResult(sql, CollectionUtils.list(context.getWhereParameter("tenantId"), context.getPageSize(),
				context.getStartRow()));
	}

}

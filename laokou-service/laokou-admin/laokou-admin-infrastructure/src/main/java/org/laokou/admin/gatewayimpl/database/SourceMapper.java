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
package org.laokou.admin.gatewayimpl.database;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.laokou.admin.gatewayimpl.database.dataobject.SourceDO;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.mybatisplus.database.BatchMapper;
import org.springframework.stereotype.Repository;

import static org.laokou.common.i18n.dto.PageQuery.PAGE_QUERY;
import static org.laokou.common.mybatisplus.database.dataobject.BaseDO.TENANT_ID;

/**
 * @author laokou
 */
@Mapper
@Repository
public interface SourceMapper extends BatchMapper<SourceDO> {

	/**
	 * 根据名称查询数据源
	 * @param name 数据源名称
	 * @return SourceDO
	 */
	SourceDO getSourceByName(@Param("name") String name);

	IPage<SourceDO> getSourceListFilter(IPage<SourceDO> page, @Param("name") String name,
			@Param(PAGE_QUERY) PageQuery pageQuery);

	/**
	 * 根据租户ID查询数据源
	 * @param tenantId 租户ID
	 * @return String
	 */
	SourceDO getSourceByTenantId(@Param(TENANT_ID) Long tenantId);

}

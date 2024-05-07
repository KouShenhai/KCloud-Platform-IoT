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

package org.laokou.auth.gatewayimpl.database;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.laokou.auth.gatewayimpl.database.dataobject.SourceDO;
import org.laokou.common.mybatisplus.mapper.CrudMapper;
import org.springframework.stereotype.Repository;

/**
 * 数据源.
 *
 * @author laokou
 */
@Repository
@Mapper
public interface SourceMapper extends CrudMapper<Long, Integer, SourceDO> {

	/**
	 * 根据租户ID查询数据源.
	 * @param tenantId 租户ID
	 * @return 数据源
	 */
	SourceDO selectByTenantId(@Param("tenantId") Long tenantId);

}

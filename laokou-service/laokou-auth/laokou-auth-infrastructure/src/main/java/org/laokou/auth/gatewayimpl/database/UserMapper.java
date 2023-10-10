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

package org.laokou.auth.gatewayimpl.database;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.laokou.auth.gatewayimpl.database.dataobject.UserDO;
import org.laokou.common.mybatisplus.database.BatchMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.laokou.common.mybatisplus.database.dataobject.BaseDO.TENANT_ID;

/**
 * @author laokou
 */
@Mapper
@Repository
public interface UserMapper extends BatchMapper<UserDO> {

	/**
	 * 根据用户名和租户ID查询用户
	 * @param tables 动态表名
	 * @param username 用户名
	 * @param tenantId 租户ID
	 * @param type 登录类型
	 * @return UserDO
	 */
	UserDO getUserByUsernameAndTenantId(@Param("tables") List<String> tables, @Param("username") String username,
			@Param(TENANT_ID) Long tenantId, @Param("type") String type);

}

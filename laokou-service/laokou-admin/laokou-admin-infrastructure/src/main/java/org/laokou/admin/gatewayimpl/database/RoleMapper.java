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

package org.laokou.admin.gatewayimpl.database;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.laokou.admin.gatewayimpl.database.dataobject.RoleDO;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.mybatisplus.mapper.CrudMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.laokou.common.i18n.dto.PageQuery.PAGE_QUERY;

/**
 * 角色.
 *
 * @author laokou
 */
@Repository
@Mapper
public interface RoleMapper extends CrudMapper<Long, Integer, RoleDO> {

	/**
	 * 查询角色列表.
	 * @param role 角色对象
	 * @param pageQuery 分页参数
	 * @return 角色列表
	 */
	List<RoleDO> selectListByCondition(@Param("role") RoleDO role, @Param(PAGE_QUERY) PageQuery pageQuery);

	/**
	 * 查询角色数量.
	 * @param role 角色对象
	 * @param pageQuery 分页参数
	 * @return 角色数量
	 */
	long selectCountByCondition(@Param("role") RoleDO role, @Param(PAGE_QUERY) PageQuery pageQuery);

	/**
	 * 查看角色IDS.
	 * @return 角色IDS
	 */
	List<Long> selectRoleIds();

}

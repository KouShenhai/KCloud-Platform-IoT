/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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
import org.laokou.admin.gatewayimpl.database.dataobject.DeptDO;
import org.laokou.common.mybatisplus.database.BatchMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 部门.
 *
 * @author laokou
 */
@Repository
@Mapper
public interface DeptMapper extends BatchMapper<DeptDO> {

	/**
	 * 查询部门列表.
	 * @param deptDO 部门数据模型
	 * @return 部门列表
	 */
	List<DeptDO> getDeptList(@Param("deptDO") DeptDO deptDO);

	/**
	 * 根据角色ID查看部门.
	 * @param roleId 角色ID
	 * @return 部门
	 */
	List<Long> getDeptIdsByRoleId(@Param("roleId") Long roleId);

	/**
	 * 根据部门父节点ID查看部门.
	 * @param pid 部门子节点ID
	 * @return 部门
	 */
	String getDeptPathByPid(@Param("pid") Long pid);

	/**
	 * 根据PATH模糊查询部门子节点列表.
	 * @param path 部门PATH
	 * @return 部门子节点列表
	 */
	List<DeptDO> getDeptChildrenListByLikePath(@Param("path") String path);

}

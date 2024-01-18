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

package org.laokou.admin.domain.gateway;

import io.swagger.v3.oas.annotations.media.Schema;
import org.laokou.admin.domain.dept.Dept;

import java.util.List;

/**
 * @author laokou
 */
@Schema(name = "DeptGateway", description = "部门网关")
public interface DeptGateway {

	/**
	 * 查询部门列表.
	 * @param dept 部门对象
	 * @return 部门列表
	 */
	List<Dept> list(Dept dept);

	/**
	 * 新增部门.
	 * @param dept 部门对象
	 * @return 新增结果
	 */
	Boolean insert(Dept dept);

	/**
	 * 修改部门.
	 * @param dept 部门对象
	 * @return 修改结果
	 */
	Boolean update(Dept dept);

	/**
	 * 根据角色IDS查询部门IDS.
	 * @param roleId 角色IDS
	 * @return 部门IDS
	 */
	List<Long> getDeptIds(Long roleId);

	/**
	 * 根据ID删除部门.
	 * @param id ID
	 * @return 删除结果
	 */
	Boolean deleteById(Long id);

	/**
	 * 根据ID查看部门.
	 * @param id ID
	 * @return 部门
	 */
	Dept getById(Long id);

}

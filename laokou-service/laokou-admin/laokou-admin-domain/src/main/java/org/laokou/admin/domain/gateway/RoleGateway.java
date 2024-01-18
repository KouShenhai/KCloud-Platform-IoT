/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.admin.domain.gateway;

import io.swagger.v3.oas.annotations.media.Schema;
import org.laokou.admin.domain.role.Role;
import org.laokou.admin.domain.user.User;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.PageQuery;

/**
 * @author laokou
 */
@Schema(name = "RoleGateway", description = "角色网关")
public interface RoleGateway {

	/**
	 * 新增角色
	 * @param role 角色对象
	 * @param user 用户对象
	 * @return 新增结果
	 */
	Boolean insert(Role role, User user);

	/**
	 * 修改角色
	 * @param role 角色对象
	 * @param user 用户对象
	 * @return 修改结果
	 */
	Boolean update(Role role, User user);

	/**
	 * 根据ID查看角色
	 * @param id ID
	 * @return 角色
	 */
	Role getById(Long id);

	/**
	 * 根据ID删除角色
	 * @param id ID
	 * @return 删除结果
	 */
	Boolean deleteById(Long id);

	/**
	 * 查询角色列表
	 * @param role 角色对象
	 * @param pageQuery 分页参数
	 * @return 角色列表
	 */
	Datas<Role> list(Role role, PageQuery pageQuery);

}

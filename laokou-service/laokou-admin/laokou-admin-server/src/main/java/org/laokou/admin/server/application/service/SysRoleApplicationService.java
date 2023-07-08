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
package org.laokou.admin.server.application.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.laokou.admin.client.dto.SysRoleDTO;
import org.laokou.admin.server.interfaces.qo.SysRoleQo;
import org.laokou.admin.client.vo.SysRoleVO;
import java.util.List;

/**
 * @author laokou
 */
public interface SysRoleApplicationService {

	/**
	 * 分页查询角色
	 * @param qo
	 * @return
	 */
	IPage<SysRoleVO> queryRolePage(SysRoleQo qo);

	/**
	 * 查询角色列表
	 * @param qo
	 * @return
	 */
	List<SysRoleVO> getRoleList(SysRoleQo qo);

	/**
	 * 根据id获取角色
	 * @param id
	 * @return
	 */
	SysRoleVO getRoleById(Long id);

	/**
	 * 新增角色
	 * @param dto
	 * @return
	 */
	Boolean insertRole(SysRoleDTO dto);

	/**
	 * 修改角色
	 * @param dto
	 * @return
	 */
	Boolean updateRole(SysRoleDTO dto);

	/**
	 * 根据id删除角色
	 * @param id
	 * @return
	 */
	Boolean deleteRole(Long id);

}

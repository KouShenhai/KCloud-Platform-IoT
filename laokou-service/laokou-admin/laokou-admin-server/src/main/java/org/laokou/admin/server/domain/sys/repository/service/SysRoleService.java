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
package org.laokou.admin.server.domain.sys.repository.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.laokou.admin.server.domain.sys.entity.SysRoleDO;
import org.laokou.admin.server.interfaces.qo.SysRoleQo;
import org.laokou.admin.vo.SysRoleVO;

import java.util.List;

/**
 * @author laokou
 */
public interface SysRoleService extends IService<SysRoleDO> {

	/**
	 * 通过用户Id查询角色Ids
	 * @param userId
	 * @return
	 */
	List<Long> getRoleIdsByUserId(Long userId);

	/**
	 * 分页查询角色
	 * @param page
	 * @param qo
	 * @return
	 */
	IPage<SysRoleVO> getRolePage(IPage<SysRoleVO> page, SysRoleQo qo);

	/**
	 * 查询角色列表
	 * @param qo
	 * @return
	 */
	List<SysRoleVO> getRoleList(SysRoleQo qo);

	/**
	 * 根据id查询角色
	 * @param id
	 * @return
	 */
	SysRoleVO getRoleById(Long id);

	/**
	 * 根据id删除角色
	 * @param id
	 */
	void deleteRole(Long id);

	/**
	 * 获取版本号
	 * @param id
	 * @return
	 */
	Integer getVersion(Long id);

}

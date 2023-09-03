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

package org.laokou.admin.gatewayimpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.convertor.RoleConvertor;
import org.laokou.admin.domain.gateway.RoleGateway;
import org.laokou.admin.domain.role.Role;
import org.laokou.admin.gatewayimpl.database.RoleDeptMapper;
import org.laokou.admin.gatewayimpl.database.RoleMapper;
import org.laokou.admin.gatewayimpl.database.RoleMenuMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.RoleDO;
import org.laokou.admin.gatewayimpl.database.dataobject.RoleDeptDO;
import org.laokou.admin.gatewayimpl.database.dataobject.RoleMenuDO;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.mybatisplus.utils.BatchUtil;
import org.laokou.common.mybatisplus.utils.IdUtil;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.laokou.common.security.utils.UserUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RoleGatewayImpl implements RoleGateway {

	private final RoleMapper roleMapper;

	private final RoleMenuMapper roleMenuMapper;

	private final RoleDeptMapper roleDeptMapper;

	private final BatchUtil batchUtil;

	private final TransactionalUtil transactionalUtil;

	@Override
	public Boolean insert(Role role) {
		RoleDO roleDO = RoleConvertor.toDataObject(role);
		roleDO.setDeptId(UserUtil.getDeptId());
		roleDO.setTenantId(UserUtil.getTenantId());
		return insertRole(roleDO, role);
	}

	@Override
	public Boolean update(Role role) {
		Long id = role.getId();
		RoleDO roleDO = RoleConvertor.toDataObject(role);
		List<Long> ids1 = roleMenuMapper.getIdsByRoleId(id);
		List<Long> ids2 = roleDeptMapper.getIdsByRoleId(id);
		return updateRole(roleDO, role, ids1, ids2);
	}

	private Boolean updateRole(RoleDO roleDO, Role role, List<Long> ids1, List<Long> ids2) {
		return transactionalUtil.execute(rollback -> {
			try {
				return roleMapper.updateById(roleDO) > 0 && updateRoleMenu(roleDO.getId(), role.getMenuIds(), ids1)
						&& updateRoleDept(roleDO.getId(), role.getDeptIds(), ids2);
			}
			catch (Exception e) {
				log.error("错误信息：{}", e.getMessage());
				rollback.setRollbackOnly();
				return false;
			}
		});
	}

	private Boolean updateRoleMenu(Long roleId, List<Long> menuIds, List<Long> ids) {
		boolean flag = true;
		if (CollectionUtil.isNotEmpty(ids)) {
			flag = roleMenuMapper.deleteBatchIds(ids) > 0;
		}
		return flag && insertRoleMenu(roleId, menuIds);
	}

	private Boolean updateRoleDept(Long roleId, List<Long> deptIds, List<Long> ids) {
		boolean flag = true;
		if (CollectionUtil.isNotEmpty(ids)) {
			flag = roleDeptMapper.deleteBatchIds(ids) > 0;
		}
		return flag && insertRoleDept(roleId, deptIds);
	}

	private Boolean insertRole(RoleDO roleDO, Role role) {
		return transactionalUtil.execute(rollback -> {
			try {
				return roleMapper.insert(roleDO) > 0 && insertRoleMenu(roleDO.getId(), role.getMenuIds())
						&& insertRoleDept(roleDO.getId(), role.getDeptIds());
			}
			catch (Exception e) {
				log.error("错误信息：{}", e.getMessage());
				rollback.setRollbackOnly();
				return false;
			}
		});
	}

	private Boolean insertRoleMenu(Long roleId, List<Long> menuIds) {
		if (CollectionUtil.isNotEmpty(menuIds)) {
			List<RoleMenuDO> list = new ArrayList<>(menuIds.size());
			for (Long menuId : menuIds) {
				RoleMenuDO roleMenuDO = new RoleMenuDO();
				roleMenuDO.setRoleId(roleId);
				roleMenuDO.setMenuId(menuId);
				roleMenuDO.setId(IdUtil.defaultId());
				list.add(roleMenuDO);
			}
			batchUtil.insertBatch(list, roleMenuMapper::insertBatch);
			return true;
		}
		return false;
	}

	private Boolean insertRoleDept(Long roleId, List<Long> deptIds) {
		if (CollectionUtil.isNotEmpty(deptIds)) {
			List<RoleDeptDO> list = new ArrayList<>(deptIds.size());
			for (Long deptId : deptIds) {
				RoleDeptDO roleDeptDO = new RoleDeptDO();
				roleDeptDO.setRoleId(roleId);
				roleDeptDO.setDeptId(deptId);
				roleDeptDO.setId(IdUtil.defaultId());
				list.add(roleDeptDO);
			}
			batchUtil.insertBatch(list, roleDeptMapper::insertBatch);
			return true;
		}
		return false;
	}

}

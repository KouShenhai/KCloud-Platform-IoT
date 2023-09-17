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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.convertor.RoleConvertor;
import org.laokou.admin.domain.annotation.DataFilter;
import org.laokou.admin.domain.gateway.RoleGateway;
import org.laokou.admin.domain.role.Role;
import org.laokou.admin.domain.user.User;
import org.laokou.admin.gatewayimpl.database.RoleDeptMapper;
import org.laokou.admin.gatewayimpl.database.RoleMapper;
import org.laokou.admin.gatewayimpl.database.RoleMenuMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.RoleDO;
import org.laokou.admin.gatewayimpl.database.dataobject.RoleDeptDO;
import org.laokou.admin.gatewayimpl.database.dataobject.RoleMenuDO;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.mybatisplus.utils.BatchUtil;
import org.laokou.common.mybatisplus.utils.IdUtil;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.laokou.common.security.utils.UserUtil;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.laokou.admin.common.DsConstant.BOOT_SYS_ROLE;

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
		return insertRole(roleDO, role);
	}

	@Override
	public Boolean update(Role role) {
		Long id = role.getId();
		RoleDO roleDO = RoleConvertor.toDataObject(role);
		roleDO.setVersion(roleMapper.getVersion(id, RoleDO.class));
		List<Long> ids1 = roleMenuMapper.getIdsByRoleId(id);
		List<Long> ids2 = roleDeptMapper.getIdsByRoleId(id);
		return updateRole(roleDO, role, ids1, ids2);
	}

	@Override
	public Role getById(Long id) {
		RoleDO roleDO = roleMapper.selectById(id);
		return ConvertUtil.sourceToTarget(roleDO, Role.class);
	}

	@Override
	public Boolean deleteById(Long id) {
		return transactionalUtil.execute(r -> {
			try {
				return roleMapper.deleteById(id) > 0;
			}
			catch (Exception e) {
				log.error("错误信息：{}", e.getMessage());
				r.setRollbackOnly();
				return false;
			}
		});
	}

	@Override
	@DataFilter(alias = BOOT_SYS_ROLE)
	public Datas<Role> list(User user, Role role, PageQuery pageQuery) {
		IPage<RoleDO> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
		IPage<RoleDO> newPage = roleMapper.getRoleListByTenantIdAndLikeNameFilter(page, user.getTenantId(),
				role.getName(), pageQuery.getSqlFilter());
		Datas<Role> datas = new Datas<>();
		datas.setRecords(ConvertUtil.sourceToTarget(newPage.getRecords(), Role.class));
		datas.setTotal(newPage.getTotal());
		return datas;
	}

	@Transactional(rollbackFor = Exception.class)
	public Boolean insertRole(RoleDO roleDO, Role role) {
		boolean flag = roleMapper.insert(roleDO) > 0;
		flag = flag && insertRoleMenu(roleDO.getId(), role.getMenuIds());
		flag = flag && insertRoleDept(roleDO.getId(), role.getDeptIds());
		return flag;
	}

	@Transactional(rollbackFor = Exception.class)
	public Boolean updateRole(RoleDO roleDO, Role role, List<Long> ids1, List<Long> ids2) {
		boolean flag = roleMapper.updateById(roleDO) > 0;
		flag = flag && updateRoleMenu(roleDO.getId(), role.getMenuIds(), ids1);
		flag = flag && updateRoleDept(roleDO.getId(), role.getDeptIds(), ids2);
		return flag;
	}

	private Boolean updateRoleMenu(Long roleId, List<Long> menuIds, List<Long> ids) {
		boolean flag = true;
		if (CollectionUtil.isNotEmpty(ids)) {
			flag = roleMenuMapper.deleteRoleMenuByIds(ids) > 0;
		}
		return flag && insertRoleMenu(roleId, menuIds);
	}

	private Boolean updateRoleDept(Long roleId, List<Long> deptIds, List<Long> ids) {
		boolean flag = true;
		if (CollectionUtil.isNotEmpty(ids)) {
			flag = roleDeptMapper.deleteRoleDeptByIds(ids) > 0;
		}
		return flag && insertRoleDept(roleId, deptIds);
	}

	private Boolean insertRoleMenu(Long roleId, List<Long> menuIds) {
		if (CollectionUtil.isNotEmpty(menuIds)) {
			List<RoleMenuDO> list = new ArrayList<>(menuIds.size());
			for (Long menuId : menuIds) {
				RoleMenuDO roleMenuDO = new RoleMenuDO();
				roleMenuDO.setRoleId(roleId);
				roleMenuDO.setMenuId(menuId);
				roleMenuDO.setId(IdUtil.defaultId());
				roleMenuDO.setDeptId(UserUtil.getDeptId());
				roleMenuDO.setTenantId(UserUtil.getTenantId());
				roleMenuDO.setCreator(UserUtil.getUserId());
				roleMenuDO.setDeptPath(UserUtil.getDeptPath());
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
				roleDeptDO.setTenantId(UserUtil.getTenantId());
				roleDeptDO.setCreator(UserUtil.getUserId());
				roleDeptDO.setDeptPath(UserUtil.getDeptPath());
				list.add(roleDeptDO);
			}
			batchUtil.insertBatch(list, roleDeptMapper::insertBatch);
			return true;
		}
		return false;
	}

}

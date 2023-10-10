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
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.mybatisplus.utils.BatchUtil;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.laokou.common.mybatisplus.template.DsConstant.BOOT_SYS_ROLE;

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
	public Boolean insert(Role role, User user) {
		RoleDO roleDO = RoleConvertor.toDataObject(role);
		return insertRole(roleDO, role, user);
	}

	@Override
	public Boolean update(Role role, User user) {
		Long id = role.getId();
		RoleDO roleDO = RoleConvertor.toDataObject(role);
		roleDO.setVersion(roleMapper.getVersion(id, RoleDO.class));
		List<Long> ids1 = roleMenuMapper.getIdsByRoleId(id);
		List<Long> ids2 = roleDeptMapper.getIdsByRoleId(id);
		return updateRole(roleDO, role, ids1, ids2, user);
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
				role.getName(), pageQuery);
		Datas<Role> datas = new Datas<>();
		datas.setRecords(ConvertUtil.sourceToTarget(newPage.getRecords(), Role.class));
		datas.setTotal(newPage.getTotal());
		return datas;
	}

	@Transactional(rollbackFor = Exception.class)
	public Boolean insertRole(RoleDO roleDO, Role role, User user) {
		boolean flag = roleMapper.insertTable(roleDO);
		flag = flag && insertRoleMenu(roleDO.getId(), role.getMenuIds(), user);
		flag = flag && insertRoleDept(roleDO.getId(), role.getDeptIds(), user);
		return flag;
	}

	@Transactional(rollbackFor = Exception.class)
	public Boolean updateRole(RoleDO roleDO, Role role, List<Long> ids1, List<Long> ids2, User user) {
		boolean flag = roleMapper.updateById(roleDO) > 0;
		flag = flag && updateRoleMenu(roleDO.getId(), role.getMenuIds(), ids1, user);
		flag = flag && updateRoleDept(roleDO.getId(), role.getDeptIds(), ids2, user);
		return flag;
	}

	private Boolean updateRoleMenu(Long roleId, List<Long> menuIds, List<Long> ids, User user) {
		boolean flag = true;
		if (CollectionUtil.isNotEmpty(ids)) {
			flag = roleMenuMapper.deleteRoleMenuByIds(ids) > 0;
		}
		return flag && insertRoleMenu(roleId, menuIds, user);
	}

	private Boolean updateRoleDept(Long roleId, List<Long> deptIds, List<Long> ids, User user) {
		boolean flag = true;
		if (CollectionUtil.isNotEmpty(ids)) {
			flag = roleDeptMapper.deleteRoleDeptByIds(ids) > 0;
		}
		return flag && insertRoleDept(roleId, deptIds, user);
	}

	private Boolean insertRoleMenu(Long roleId, List<Long> menuIds, User user) {
		if (CollectionUtil.isNotEmpty(menuIds)) {
			List<RoleMenuDO> list = new ArrayList<>(menuIds.size());
			for (Long menuId : menuIds) {
				list.add(toRoleMenuDO(roleId, menuId, user));
			}
			batchUtil.insertBatch(list, roleMenuMapper::insertBatch);
			return true;
		}
		return false;
	}

	private Boolean insertRoleDept(Long roleId, List<Long> deptIds, User user) {
		if (CollectionUtil.isNotEmpty(deptIds)) {
			List<RoleDeptDO> list = new ArrayList<>(deptIds.size());
			for (Long deptId : deptIds) {
				list.add(toRoleDeptDO(roleId, deptId, user));
			}
			batchUtil.insertBatch(list, roleDeptMapper::insertBatch);
			return true;
		}
		return false;
	}

	private RoleMenuDO toRoleMenuDO(Long roleId, Long menuId, User user) {
		RoleMenuDO roleMenuDO = new RoleMenuDO();
		roleMenuDO.setRoleId(roleId);
		roleMenuDO.setMenuId(menuId);
		roleMenuDO.setId(IdGenerator.defaultSnowflakeId());
		roleMenuDO.setDeptId(user.getDeptId());
		roleMenuDO.setTenantId(user.getTenantId());
		roleMenuDO.setCreator(user.getId());
		roleMenuDO.setDeptPath(user.getDeptPath());
		return roleMenuDO;
	}

	private RoleDeptDO toRoleDeptDO(Long roleId, Long deptId, User user) {
		RoleDeptDO roleDeptDO = new RoleDeptDO();
		roleDeptDO.setRoleId(roleId);
		roleDeptDO.setDeptId(deptId);
		roleDeptDO.setId(IdGenerator.defaultSnowflakeId());
		roleDeptDO.setTenantId(user.getTenantId());
		roleDeptDO.setCreator(user.getId());
		roleDeptDO.setDeptPath(user.getDeptPath());
		return roleDeptDO;
	}

}

/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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
import org.laokou.common.core.holder.UserContextHolder;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.common.mybatisplus.utils.MybatisUtil;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.laokou.common.mybatisplus.constant.DsConstant.BOOT_SYS_ROLE;

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

	private final MybatisUtil mybatisUtil;

	private final TransactionalUtil transactionalUtil;

	private final RoleConvertor roleConvertor;

	@Override
	public Boolean insert(Role role, User user) {
		RoleDO roleDO = roleConvertor.toDataObject(role);
		return insertRole(roleDO, role, user);
	}

	@Override
	public Boolean update(Role role, User user) {
		Long id = role.getId();
		RoleDO roleDO = roleConvertor.toDataObject(role);
		roleDO.setVersion(roleMapper.getVersion(id, RoleDO.class));
		return updateRole(roleDO, role, user);
	}

	@Override
	public Role getById(Long id) {
		return roleConvertor.convertEntity(roleMapper.selectById(id));
	}

	@Override
	public Boolean deleteById(Long id) {
		return transactionalUtil.defaultExecute(r -> {
			try {
				return roleMapper.deleteById(id) > 0;
			}
			catch (Exception e) {
				log.error("错误信息：{}，详情见日志", LogUtil.error(e.getMessage()), e);
				r.setRollbackOnly();
				throw new SystemException(e.getMessage());
			}
		});
	}

	@Override
	@DataFilter(alias = BOOT_SYS_ROLE)
	public Datas<Role> list(Role role, PageQuery pageQuery) {
		IPage<RoleDO> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
		IPage<RoleDO> newPage = roleMapper.getRoleListFilter(page, role.getName(), pageQuery);
		Datas<Role> datas = new Datas<>();
		datas.setRecords(roleConvertor.convertEntityList(newPage.getRecords()));
		datas.setTotal(newPage.getTotal());
		return datas;
	}

	private Boolean insertRole(RoleDO roleDO, Role role, User user) {
		return transactionalUtil.defaultExecute(r -> {
			try {
				roleMapper.insertTable(roleDO);
				insertRoleMenu(roleDO.getId(), role.getMenuIds(), user);
				insertRoleDept(roleDO.getId(), role.getDeptIds(), user);
				return true;
			}
			catch (Exception e) {
				log.error("错误信息：{}，详情见日志", LogUtil.error(e.getMessage()), e);
				r.setRollbackOnly();
				throw new SystemException(e.getMessage());
			}
		});

	}

	private Boolean updateRole(RoleDO roleDO, Role role, User user) {
		return transactionalUtil.defaultExecute(r -> {
			try {
				roleMapper.updateById(roleDO);
				updateRoleDept(roleDO.getId(), role.getDeptIds(), user);
				updateRoleMenu(roleDO.getId(), role.getMenuIds(), user);
				return true;
			}
			catch (Exception e) {
				log.error("错误信息", e);
				r.setRollbackOnly();
				throw new SystemException(e.getMessage());
			}
		});
	}

	private void updateRoleMenu(Long roleId, List<Long> menuIds, User user) {
		roleMenuMapper.deleteRoleMenuByRoleId(roleId);
		insertRoleMenu(roleId, menuIds, user);
	}

	private void updateRoleDept(Long roleId, List<Long> deptIds, User user) {
		roleDeptMapper.deleteRoleDeptByRoleId(roleId);
		insertRoleDept(roleId, deptIds, user);
	}

	private void insertRoleMenu(Long roleId, List<Long> menuIds, User user) {
		if (CollectionUtil.isNotEmpty(menuIds)) {
			List<RoleMenuDO> list = menuIds.parallelStream().map(menuId -> toRoleMenuDO(roleId, menuId, user)).toList();
			mybatisUtil.batch(list, RoleMenuMapper.class, UserContextHolder.get().getSourceName(),
					RoleMenuMapper::save);
		}
	}

	private void insertRoleDept(Long roleId, List<Long> deptIds, User user) {
		if (CollectionUtil.isNotEmpty(deptIds)) {
			List<RoleDeptDO> list = deptIds.parallelStream().map(deptId -> toRoleDeptDO(roleId, deptId, user)).toList();
			mybatisUtil.batch(list, RoleDeptMapper.class, UserContextHolder.get().getSourceName(),
					RoleDeptMapper::save);
		}
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

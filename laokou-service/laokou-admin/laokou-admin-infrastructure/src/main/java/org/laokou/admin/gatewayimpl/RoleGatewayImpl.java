/*
 * Copyright (c) 2022-2024 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.domain.gateway.RoleGateway;
import org.laokou.admin.domain.role.Role;
import org.laokou.admin.gatewayimpl.database.RoleDeptMapper;
import org.laokou.admin.gatewayimpl.database.RoleMapper;
import org.laokou.admin.gatewayimpl.database.RoleMenuMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.RoleDO;
import org.laokou.admin.gatewayimpl.database.dataobject.RoleDeptDO;
import org.laokou.admin.gatewayimpl.database.dataobject.RoleMenuDO;
import org.laokou.common.core.context.UserContextHolder;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.core.utils.IdGenerator;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.common.mybatisplus.utils.MybatisUtil;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.laokou.common.security.utils.UserUtil;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 角色管理.
 *
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

	/**
	 * 新增角色.
	 * @param role 角色对象
	 */
	@Override
	public void create(Role role) {
		long count = roleMapper.selectCount(Wrappers.lambdaQuery(RoleDO.class).eq(RoleDO::getName, role.getName()));
		role.checkName(count);
		// RoleDO roleDO = roleConvertor.toDataObject(role);
		// create(roleDO, role);
	}

	/**
	 * 修改角色.
	 * @param role 角色对象
	 */
	@Override
	public void modify(Role role) {
		// role.checkNullId();
		long count = roleMapper.selectCount(
				Wrappers.lambdaQuery(RoleDO.class).eq(RoleDO::getName, role.getName()).ne(RoleDO::getId, role.getId()));
		role.checkName(count);
		// RoleDO roleDO = roleConvertor.toDataObject(role);
		// 版本号
		// roleDO.setVersion(roleMapper.selectVersion(roleDO.getId()));
		// modify(roleDO, role);
	}

	/**
	 * 根据IDS删除角色.
	 * @param ids IDS
	 */
	@Override
	public void remove(Long[] ids) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				roleMapper.deleteByIds(Arrays.asList(ids));
			}
			catch (Exception e) {
				log.error("错误信息：{}，详情见日志", LogUtil.record(e.getMessage()), e);
				r.setRollbackOnly();
				throw new SystemException(LogUtil.record(e.getMessage()));
			}
		});
	}

	/**
	 * 新增角色.
	 * @param roleDO 角色数据模型
	 * @param role 角色对象
	 */
	private void create(RoleDO roleDO, Role role) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				roleMapper.insert(roleDO);
				createRoleDept(roleDO, role.getDeptIds());
				createRoleMenu(roleDO, role.getMenuIds());
			}
			catch (Exception e) {
				String msg = LogUtil.record(e.getMessage());
				log.error("错误信息：{}，详情见日志", msg, e);
				r.setRollbackOnly();
				throw new SystemException(msg);
			}
		});

	}

	/**
	 * 修改角色.
	 * @param roleDO 角色数据模型
	 * @param role 角色对象
	 */
	private void modify(RoleDO roleDO, Role role) {
		transactionalUtil.defaultExecuteWithoutResult(r -> {
			try {
				roleMapper.updateById(roleDO);
				modifyRoleDept(roleDO, role.getDeptIds());
				modifyRoleMenu(roleDO, role.getMenuIds());
			}
			catch (Exception e) {
				String msg = LogUtil.record(e.getMessage());
				log.error("错误信息：{}，详情见日志", msg, e);
				r.setRollbackOnly();
				throw new SystemException(msg);
			}
		});
	}

	/**
	 * 新增角色部门.
	 * @param roleDO 角色对象
	 * @param deptIds 部门IDS
	 */
	private void createRoleDept(RoleDO roleDO, List<Long> deptIds) {
		List<RoleDeptDO> list = deptIds.parallelStream().map(deptId -> to(roleDO.getId(), deptId)).toList();
		mybatisUtil.batch(list, RoleDeptMapper.class, UserContextHolder.get().getSourceName(),
				RoleDeptMapper::insertOne);
	}

	/**
	 * 新增角色菜单.
	 * @param roleDO 角色对象
	 * @param menuIds 菜单IDS
	 */
	private void createRoleMenu(RoleDO roleDO, List<Long> menuIds) {
		List<RoleMenuDO> list = menuIds.parallelStream().map(menuId -> convert(roleDO.getId(), menuId)).toList();
		mybatisUtil.batch(list, RoleMenuMapper.class, UserContextHolder.get().getSourceName(),
				RoleMenuMapper::insertOne);
	}

	private void modifyRoleMenu(RoleDO roleDO, List<Long> menuIds) {
		if (CollectionUtil.isNotEmpty(menuIds)) {
			// 删除
			removeRoleMenu(roleDO);
			// 新增
			createRoleMenu(roleDO, menuIds);
		}
	}

	private void modifyRoleDept(RoleDO roleDO, List<Long> deptIds) {
		if (CollectionUtil.isNotEmpty(deptIds)) {
			// 删除
			removeRoleDept(roleDO);
			// 新增
			createRoleDept(roleDO, deptIds);
		}
	}

	private void removeRoleMenu(RoleDO roleDO) {
		roleMenuMapper.deleteByRoleId(roleDO.getId());
	}

	private void removeRoleDept(RoleDO roleDO) {
		roleDeptMapper.deleteByRoleId(roleDO.getId());
	}

	/**
	 * 转换为角色部门数据模型.
	 * @param roleId 角色ID
	 * @param deptId 部门ID
	 * @return 角色部门数据模型
	 */
	private RoleDeptDO to(Long roleId, Long deptId) {
		RoleDeptDO roleDeptDO = new RoleDeptDO();
		roleDeptDO.setRoleId(roleId);
		roleDeptDO.setDeptId(deptId);
		roleDeptDO.setId(IdGenerator.defaultSnowflakeId());
		roleDeptDO.setTenantId(UserUtil.getTenantId());
		roleDeptDO.setCreator(UserUtil.getUserId());
		roleDeptDO.setDeptPath(UserUtil.getDeptPath());
		return roleDeptDO;
	}

	/**
	 * 转换为角色菜单数据模型.
	 * @param roleId 角色ID
	 * @param menuId 菜单ID
	 * @return 角色菜单数据模型
	 */
	private RoleMenuDO convert(Long roleId, Long menuId) {
		RoleMenuDO roleMenuDO = new RoleMenuDO();
		roleMenuDO.setRoleId(roleId);
		roleMenuDO.setMenuId(menuId);
		roleMenuDO.setId(IdGenerator.defaultSnowflakeId());
		roleMenuDO.setDeptId(UserUtil.getDeptId());
		roleMenuDO.setTenantId(UserUtil.getTenantId());
		roleMenuDO.setCreator(UserUtil.getUserId());
		roleMenuDO.setDeptPath(UserUtil.getDeptPath());
		return roleMenuDO;
	}

}

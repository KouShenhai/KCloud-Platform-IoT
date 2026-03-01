/*
 * Copyright (c) 2022-2026 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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

package org.laokou.admin.role.convertor;

import org.laokou.admin.role.dto.clientobject.RoleCO;
import org.laokou.admin.role.factory.RoleDomainFactory;
import org.laokou.admin.role.gatewayimpl.database.dataobject.RoleDO;
import org.laokou.admin.role.gatewayimpl.database.dataobject.RoleDeptDO;
import org.laokou.admin.role.gatewayimpl.database.dataobject.RoleMenuDO;
import org.laokou.admin.role.model.RoleA;
import org.laokou.admin.role.model.entity.RoleE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 角色转换器.
 *
 * @author laokou
 */
public class RoleConvertor {

	public static RoleDO toDataObject(RoleA roleA) {
		RoleE roleE = roleA.getRoleE();
		RoleDO roleDO = new RoleDO();
		roleDO.setId(roleA.getId());
		roleDO.setName(roleE.getName());
		roleDO.setSort(roleE.getSort());
		roleDO.setDataScope(roleE.getDataScope());
		roleDO.setCreateTime(roleA.getCreateTime());
		return roleDO;
	}

	public static List<RoleMenuDO> toDataObjects(RoleA roleA) {
		Long roleId = roleA.getId();
		List<String> menuIds = roleA.getRoleE().getMenuIds();
		int num = menuIds.size();
		List<Long> primaryKeys = roleA.getIds(num);
		List<RoleMenuDO> list = new ArrayList<>(num);
		for (int i = 0; i < num; i++) {
			RoleMenuDO roleMenuDO = new RoleMenuDO();
			roleMenuDO.setId(primaryKeys.get(i));
			roleMenuDO.setRoleId(roleId);
			roleMenuDO.setMenuId(Long.valueOf(menuIds.get(i)));
			list.add(roleMenuDO);
		}
		return list;
	}

	public static List<RoleMenuDO> toDataObjects(List<Long> roleMenuIds) {
		return roleMenuIds.stream().map(id -> {
			RoleMenuDO roleMenuDO = new RoleMenuDO();
			roleMenuDO.setId(id);
			return roleMenuDO;
		}).toList();
	}

	public static List<RoleDeptDO> toDataObjs(RoleA roleA) {
		Long roleId = roleA.getId();
		List<String> deptIds = roleA.getRoleE().getDeptIds();
		int num = deptIds.size();
		List<Long> primaryKeys = roleA.getIds(num);
		List<RoleDeptDO> list = new ArrayList<>(num);
		for (int i = 0; i < num; i++) {
			RoleDeptDO roleDeptDO = new RoleDeptDO();
			roleDeptDO.setId(primaryKeys.get(i));
			roleDeptDO.setRoleId(roleId);
			roleDeptDO.setDeptId(Long.valueOf(deptIds.get(i)));
			list.add(roleDeptDO);
		}
		return list;
	}

	public static List<RoleDeptDO> toDataObjs(List<Long> roleDeptIds) {
		return roleDeptIds.stream().map(id -> {
			RoleDeptDO roleDeptDO = new RoleDeptDO();
			roleDeptDO.setId(id);
			return roleDeptDO;
		}).toList();
	}

	public static List<RoleCO> toClientObjects(List<RoleDO> roleDOList) {
		return roleDOList.stream().map(RoleConvertor::toClientObject).toList();
	}

	public static RoleCO toClientObject(RoleDO roleDO) {
		RoleCO roleCO = new RoleCO();
		roleCO.setId(roleDO.getId());
		roleCO.setName(roleDO.getName());
		roleCO.setSort(roleDO.getSort());
		roleCO.setDataScope(roleDO.getDataScope());
		roleCO.setCreateTime(roleDO.getCreateTime());
		return roleCO;
	}

	public static RoleE toEntity(RoleCO roleCO) {
		return RoleDomainFactory.createRoleE()
			.toBuilder()
			.id(roleCO.getId())
			.name(roleCO.getName())
			.sort(roleCO.getSort())
			.menuIds(roleCO.getMenuIds())
			.dataScope(roleCO.getDataScope())
			.deptIds(roleCO.getDeptIds())
			.roleIds(Collections.singletonList(roleCO.getId()))
			.build();
	}

}

/*
 * Copyright (c) 2022-2025 KCloud-Platform-IoT Author or Authors. All Rights Reserved.
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
import org.laokou.admin.role.gatewayimpl.database.dataobject.RoleDO;
import org.laokou.admin.role.gatewayimpl.database.dataobject.RoleMenuDO;
import org.laokou.admin.role.model.RoleE;
import org.laokou.common.core.utils.IdGenerator;

import java.util.List;

/**
 * 角色转换器.
 *
 * @author laokou
 */
public class RoleConvertor {

	public static RoleDO toDataObject(RoleE roleE, boolean isInsert) {
		RoleDO roleDO = new RoleDO();
		if (isInsert) {
			roleDO.setId(IdGenerator.defaultSnowflakeId());
		}
		else {
			roleDO.setId(roleE.getId());
		}
		roleDO.setName(roleE.getName());
		roleDO.setSort(roleE.getSort());
		roleDO.setDataScope(roleE.getDataScope());
		return roleDO;
	}

	public static List<RoleMenuDO> toDataObject(RoleE roleE, Long roleId) {
		return roleE.getMenuIds().stream().map(menuId -> {
			RoleMenuDO roleMenuDO = new RoleMenuDO();
			roleMenuDO.setId(IdGenerator.defaultSnowflakeId());
			roleMenuDO.setRoleId(roleId);
			roleMenuDO.setMenuId(Long.valueOf(menuId));
			return roleMenuDO;
		}).toList();
	}

	public static List<RoleMenuDO> toDataObject(RoleE roleE) {
		return roleE.getRoleMenuIds().stream().map(id -> {
			RoleMenuDO roleMenuDO = new RoleMenuDO();
			roleMenuDO.setId(id);
			return roleMenuDO;
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
		RoleE roleE = new RoleE();
		roleE.setId(roleCO.getId());
		roleE.setName(roleCO.getName());
		roleE.setSort(roleCO.getSort());
		roleE.setDataScope(roleCO.getDataScope());
		roleE.setMenuIds(roleCO.getMenuIds());
		return roleE;
	}

}

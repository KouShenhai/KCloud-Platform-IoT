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

package org.laokou.admin.role.gatewayimpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.role.convertor.RoleConvertor;
import org.laokou.admin.role.gateway.RoleMenuGateway;
import org.laokou.admin.role.gatewayimpl.database.RoleMenuMapper;
import org.laokou.admin.role.gatewayimpl.database.dataobject.RoleMenuDO;
import org.laokou.admin.role.model.RoleE;
import org.laokou.common.core.util.CollectionUtils;
import org.laokou.common.dubbo.rpc.DistributedIdentifierWrapperRpc;
import org.laokou.common.mybatisplus.util.MybatisUtils;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RoleMenuGatewayImpl implements RoleMenuGateway {

	private final MybatisUtils mybatisUtils;

	private final RoleMenuMapper roleMenuMapper;

	private final DistributedIdentifierWrapperRpc distributedIdentifierWrapperRpc;

	@Override
	public void updateRoleMenu(RoleE roleE) {
		deleteRoleMenu(getRoleMenuIds(roleE.getRoleIds()));
		insertRoleMenu(roleE.getMenuIds(), roleE.getId());
	}

	@Override
	public void deleteRoleMenu(Long[] roleIds) {
		deleteRoleMenu(Arrays.asList(roleIds));
	}

	private void insertRoleMenu(List<String> menuIds, Long roleId) {
		// 新增角色菜单关联表
		List<RoleMenuDO> list = RoleConvertor.toDataObjects(distributedIdentifierWrapperRpc::getId, menuIds, roleId);
		if (CollectionUtils.isNotEmpty(list)) {
			mybatisUtils.batch(list, RoleMenuMapper.class, RoleMenuMapper::insert);
		}
	}

	private void deleteRoleMenu(List<Long> roleMenuIds) {
		// 删除角色菜单关联表
		List<RoleMenuDO> list = RoleConvertor.toDataObjects(roleMenuIds);
		if (CollectionUtils.isNotEmpty(list)) {
			mybatisUtils.batch(list, RoleMenuMapper.class, RoleMenuMapper::deleteRoleMenuById);
		}
	}

	private List<Long> getRoleMenuIds(List<Long> roleIds) {
		return roleMenuMapper.selectRoleMenuIdsByRoleIds(roleIds); // 增强型指数退避策略
	}

}

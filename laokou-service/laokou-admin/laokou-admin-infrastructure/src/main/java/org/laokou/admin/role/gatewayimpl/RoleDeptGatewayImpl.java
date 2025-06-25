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
import org.laokou.admin.role.gateway.RoleDeptGateway;
import org.laokou.admin.role.gatewayimpl.database.RoleDeptMapper;
import org.laokou.admin.role.gatewayimpl.database.dataobject.RoleDeptDO;
import org.laokou.admin.role.model.RoleE;
import org.laokou.common.core.util.CollectionUtils;
import org.laokou.common.dubbo.rpc.DistributedIdentifierRpc;
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
public class RoleDeptGatewayImpl implements RoleDeptGateway {

	private final RoleDeptMapper roleDeptMapper;

	private final MybatisUtils mybatisUtils;

	private final DistributedIdentifierRpc distributedIdentifierRpc;

	@Override
	public void updateRoleDept(RoleE roleE) {
		deleteRoleDept(getRoleDeptIds(roleE.getRoleIds()));
		insertRoleDept(roleE.getDeptIds(), roleE.getId());
	}

	@Override
	public void deleteRoleDept(Long[] roleIds) {
		deleteRoleDept(getRoleDeptIds(Arrays.asList(roleIds)));
	}

	private void insertRoleDept(List<String> deptIds, Long roleId) {
		// 新增角色菜单关联表
		List<RoleDeptDO> list = RoleConvertor.toDataObjs(distributedIdentifierRpc::getId, deptIds, roleId);
		if (CollectionUtils.isNotEmpty(list)) {
			mybatisUtils.batch(list, RoleDeptMapper.class, RoleDeptMapper::insert);
		}
	}

	private void deleteRoleDept(List<Long> roleDeptIds) {
		// 删除角色菜单关联表
		List<RoleDeptDO> list = RoleConvertor.toDataObjs(roleDeptIds);
		if (CollectionUtils.isNotEmpty(list)) {
			mybatisUtils.batch(list, RoleDeptMapper.class, RoleDeptMapper::deleteRoleDeptById);
		}
	}

	private List<Long> getRoleDeptIds(List<Long> roleIds) {
		return roleDeptMapper.selectRoleDeptIdsByRoleIds(roleIds);
	}

}

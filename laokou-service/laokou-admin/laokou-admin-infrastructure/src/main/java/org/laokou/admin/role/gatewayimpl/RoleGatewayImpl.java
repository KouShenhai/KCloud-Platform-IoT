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
import org.laokou.admin.role.gateway.RoleGateway;
import org.laokou.admin.role.gatewayimpl.database.RoleMapper;
import org.laokou.admin.role.gatewayimpl.database.dataobject.RoleDO;
import org.laokou.admin.role.model.RoleE;
import org.laokou.common.dubbo.rpc.DistributedIdentifierRpc;
import org.springframework.stereotype.Component;
import java.util.Arrays;

/**
 * 角色网关实现.
 *
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RoleGatewayImpl implements RoleGateway {

	private final RoleMapper roleMapper;

	private final DistributedIdentifierRpc distributedIdentifierRpc;

	@Override
	public void createRole(RoleE roleE) {
		RoleDO roleDO = RoleConvertor.toDataObject(distributedIdentifierRpc.getId(), roleE, true);
		roleMapper.insert(roleDO);
	}

	@Override
	public void updateRole(RoleE roleE) {
		RoleDO roleDO = RoleConvertor.toDataObject(null, roleE, false);
		roleDO.setVersion(getVersion(roleE.getId()));
		roleMapper.updateById(roleDO);
	}

	@Override
	public void deleteRole(Long[] ids) {
		roleMapper.deleteByIds(Arrays.asList(ids));
	}

	private Integer getVersion(Long id) {
		return roleMapper.selectVersion(id);
	}

}

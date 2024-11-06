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

package org.laokou.admin.role.gatewayimpl;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.role.model.RoleE;
import org.springframework.stereotype.Component;
import org.laokou.admin.role.gateway.RoleGateway;
import org.laokou.admin.role.gatewayimpl.database.RoleMapper;

import java.util.Arrays;

import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.laokou.admin.role.convertor.RoleConvertor;
import org.laokou.admin.role.gatewayimpl.database.dataobject.RoleDO;

/**
 * 角色网关实现.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class RoleGatewayImpl implements RoleGateway {

	private final RoleMapper roleMapper;

	private final TransactionalUtil transactionalUtil;

	@Override
	public void create(RoleE roleE) {
		transactionalUtil.executeInTransaction(() -> roleMapper.insert(RoleConvertor.toDataObject(roleE)));
	}

	@Override
	public void update(RoleE roleE) {
		RoleDO roleDO = RoleConvertor.toDataObject(roleE);
		roleDO.setVersion(roleMapper.selectVersion(roleE.getId()));
		update(roleDO);
	}

	@Override
	public void delete(Long[] ids) {
		transactionalUtil.executeInTransaction(() -> roleMapper.deleteByIds(Arrays.asList(ids)));
	}

	private void update(RoleDO roleDO) {
		transactionalUtil.executeInTransaction(() -> roleMapper.updateById(roleDO));
	}

}

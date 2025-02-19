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
import org.laokou.admin.role.convertor.RoleConvertor;
import org.laokou.admin.role.gateway.RoleMenuGateway;
import org.laokou.admin.role.gatewayimpl.database.RoleMenuMapper;
import org.laokou.admin.role.model.RoleE;
import org.laokou.common.mybatisplus.utils.MybatisUtil;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class RoleMenuGatewayImpl implements RoleMenuGateway {

	private final MybatisUtil mybatisUtil;

	@Override
	public void create(RoleE roleE) {
		// 新增角色菜单关联表
		mybatisUtil.batch(RoleConvertor.toDataObjects(roleE, roleE.getId()), RoleMenuMapper.class,
				RoleMenuMapper::insert);
	}

	@Override
	public void update(RoleE roleE) {
		// 删除角色菜单关联表
		mybatisUtil.batch(RoleConvertor.toDataObjects(roleE), RoleMenuMapper.class, RoleMenuMapper::deleteObjById);
		// 新增角色菜单关联表
		mybatisUtil.batch(RoleConvertor.toDataObjects(roleE, roleE.getId()), RoleMenuMapper.class,
				RoleMenuMapper::insert);
	}

}

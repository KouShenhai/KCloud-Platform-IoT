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

package org.laokou.admin.user.gatewayimpl;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.user.convertor.UserConvertor;
import org.laokou.admin.user.gateway.UserRoleGateway;
import org.laokou.admin.user.gatewayimpl.database.UserRoleMapper;
import org.laokou.admin.user.model.UserE;
import org.laokou.common.mybatisplus.utils.MybatisUtil;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class UserRoleGatewayImpl implements UserRoleGateway {

	private final MybatisUtil mybatisUtil;

	@Override
	public void create(UserE userE) {
		// 新增用户角色关联表
		mybatisUtil.batch(UserConvertor.toDataObjects(userE, userE.getId()), UserRoleMapper.class,
				UserRoleMapper::insert);
	}

	@Override
	public void update(UserE userE) {
		// 删除用户角色关联表
		mybatisUtil.batch(UserConvertor.toDataObjects(userE), UserRoleMapper.class, UserRoleMapper::deleteObjById);
		// 新增用户角色关联表
		mybatisUtil.batch(UserConvertor.toDataObjects(userE, userE.getId()), UserRoleMapper.class,
				UserRoleMapper::insert);
	}

}

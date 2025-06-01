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
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.user.convertor.UserConvertor;
import org.laokou.admin.user.gateway.UserRoleGateway;
import org.laokou.admin.user.gatewayimpl.database.UserRoleMapper;
import org.laokou.admin.user.gatewayimpl.database.dataobject.UserRoleDO;
import org.laokou.admin.user.model.UserE;
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
public class UserRoleGatewayImpl implements UserRoleGateway {

	private final MybatisUtils mybatisUtils;

	private final UserRoleMapper userRoleMapper;

	private final DistributedIdentifierWrapperRpc distributedIdentifierWrapperRpc;

	@Override
	public void updateUserRole(UserE userE) {
		deleteUserRole(getUserRoleIds(userE.getUserIds()));
		insertUserRole(userE.getRoleIds(), userE.getId());
	}

	@Override
	public void deleteUserRole(Long[] userIds) {
		deleteUserRole(getUserRoleIds(Arrays.asList(userIds)));
	}

	private void insertUserRole(List<String> roleIds, Long userId) {
		// 新增用户角色关联表
		List<UserRoleDO> list = UserConvertor.toDataObjects(distributedIdentifierWrapperRpc.getId(), roleIds, userId);
		if (CollectionUtils.isNotEmpty(list)) {
			mybatisUtils.batch(list, UserRoleMapper.class, UserRoleMapper::insert);
		}
	}

	private void deleteUserRole(List<Long> userRoleIds) {
		// 删除用户角色关联表
		List<UserRoleDO> list = UserConvertor.toDataObjects(userRoleIds);
		if (CollectionUtils.isNotEmpty(list)) {
			mybatisUtils.batch(list, UserRoleMapper.class, UserRoleMapper::deleteUserRoleById);
		}
	}

	private List<Long> getUserRoleIds(List<Long> userIds) {
		return userRoleMapper.selectUserRoleIdsByUserIds(userIds);
	}

}

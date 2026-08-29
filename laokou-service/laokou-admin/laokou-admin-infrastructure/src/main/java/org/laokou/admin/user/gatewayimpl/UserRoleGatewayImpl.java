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

package org.laokou.admin.user.gatewayimpl;

import com.baomidou.mybatisplus.core.batch.MybatisBatch;
import com.baomidou.mybatisplus.core.toolkit.MybatisBatchUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.laokou.admin.user.convertor.UserConvertor;
import org.laokou.admin.user.gateway.UserRoleGateway;
import org.laokou.admin.user.gatewayimpl.database.UserRoleMapper;
import org.laokou.admin.user.gatewayimpl.database.dataobject.UserRoleDO;
import org.laokou.admin.user.model.UserA;
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

	private final UserRoleMapper userRoleMapper;

	private final SqlSessionFactory sqlSessionFactory;

	@Override
	public void updateUserRole(UserA userA) {
		deleteUserRole(userA.getUserE().getId());
		insertUserRole(userA);
	}

	@Override
	public void deleteUserRole(Long[] userIds) {
		deleteUserRoleBatch(Arrays.asList(userIds));
	}

	private void insertUserRole(UserA userA) {
		MybatisBatch.Method<UserRoleDO> mapperMethod = new MybatisBatch.Method<>(UserRoleMapper.class);
		MybatisBatchUtils.execute(sqlSessionFactory, UserConvertor.toDataObjects(userA), mapperMethod.insert());
	}

	private void deleteUserRole(Long userId) {
		userRoleMapper.update(Wrappers.lambdaUpdate(UserRoleDO.class)
			.set(UserRoleDO::getDelFlag, 1)
			.set(UserRoleDO::getVersion, 1)
			.eq(UserRoleDO::getRoleId, userId)
			.eq(UserRoleDO::getVersion, 0)
			.eq(UserRoleDO::getDelFlag, 0));
	}

	private void deleteUserRoleBatch(List<Long> userIds) {
		userRoleMapper.update(Wrappers.lambdaUpdate(UserRoleDO.class)
			.set(UserRoleDO::getDelFlag, 1)
			.set(UserRoleDO::getVersion, 1)
			.in(UserRoleDO::getRoleId, userIds)
			.eq(UserRoleDO::getVersion, 0)
			.eq(UserRoleDO::getDelFlag, 0));
	}

}

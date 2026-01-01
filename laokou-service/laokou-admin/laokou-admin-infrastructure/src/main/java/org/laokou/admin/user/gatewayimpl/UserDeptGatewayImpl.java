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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.user.convertor.UserConvertor;
import org.laokou.admin.user.gateway.UserDeptGateway;
import org.laokou.admin.user.gatewayimpl.database.UserDeptMapper;
import org.laokou.admin.user.gatewayimpl.database.dataobject.UserDeptDO;
import org.laokou.admin.user.model.UserE;
import org.laokou.common.core.util.CollectionExtUtils;
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
public class UserDeptGatewayImpl implements UserDeptGateway {

	private final MybatisUtils mybatisUtils;

	private final UserDeptMapper userDeptMapper;

	@Override
	public void updateUserDept(UserE userE) {
		deleteUserDept(getUserDeptIds(userE.getUserIds()));
		insertUserDept(userE);
	}

	@Override
	public void deleteUserDept(Long[] userIds) {
		deleteUserDept(getUserDeptIds(Arrays.asList(userIds)));
	}

	private void insertUserDept(UserE userE) {
		// 新增用户部门关联表
		List<UserDeptDO> list = UserConvertor.toDataObjs(userE);
		if (CollectionExtUtils.isNotEmpty(list)) {
			mybatisUtils.batch(list, UserDeptMapper.class, UserDeptMapper::insert);
		}
	}

	private void deleteUserDept(List<Long> userDeptIds) {
		// 删除用户部门关联表
		List<UserDeptDO> list = UserConvertor.toDataObjs(userDeptIds);
		if (CollectionExtUtils.isNotEmpty(list)) {
			mybatisUtils.batch(list, UserDeptMapper.class, UserDeptMapper::deleteUserDeptById);
		}
	}

	private List<Long> getUserDeptIds(List<Long> userIds) {
		return userDeptMapper.selectUserDeptIdsByUserIds(userIds);
	}

}

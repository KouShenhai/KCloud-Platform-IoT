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
import org.laokou.admin.user.gatewayimpl.database.dataobject.UserRoleDO;
import org.laokou.admin.user.model.UserE;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.mybatisplus.utils.MybatisUtil;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class UserRoleGatewayImpl implements UserRoleGateway {

	private final MybatisUtil mybatisUtil;

	private final ExecutorService virtualThreadExecutor;

	private final UserRoleMapper userRoleMapper;

	@Override
	public Mono<Void> update(UserE userE) {
		return getUserRoleIds(userE.getUserIds()).map(ids -> {
			userE.setUserRoleIds(ids);
			return userE;
		}).doOnNext(this::deleteUserRole).doOnNext(this::insertUserRole).then();
	}

	@Override
	public Mono<Void> delete(Long[] userIds) {
		return getUserRoleIds(Arrays.asList(userIds)).map(ids -> {
			UserE userE = new UserE();
			userE.setUserRoleIds(ids);
			return userE;
		}).doOnNext(this::deleteUserRole).then();
	}

	private void insertUserRole(UserE userE) {
		// 新增用户角色关联表
		List<UserRoleDO> list = UserConvertor.toDataObjects(userE, userE.getId());
		if (CollectionUtil.isNotEmpty(list)) {
			mybatisUtil.batch(list, UserRoleMapper.class, UserRoleMapper::insert);
		}
	}

	private void deleteUserRole(UserE userE) {
		// 删除用户角色关联表
		List<UserRoleDO> list = UserConvertor.toDataObjects(userE);
		if (CollectionUtil.isNotEmpty(list)) {
			mybatisUtil.batch(list, UserRoleMapper.class, UserRoleMapper::deleteObjById);
		}
	}

	private Mono<List<Long>> getUserRoleIds(List<Long> userIds) {
		return Mono.fromCallable(() -> userRoleMapper.selectIdsByUserIds(userIds))
			.subscribeOn(Schedulers.fromExecutorService(virtualThreadExecutor));
	}

}

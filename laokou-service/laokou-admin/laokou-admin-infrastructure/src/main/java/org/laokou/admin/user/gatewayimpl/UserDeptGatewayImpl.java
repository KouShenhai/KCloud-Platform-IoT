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
import org.laokou.admin.user.gateway.UserDeptGateway;
import org.laokou.admin.user.gatewayimpl.database.UserDeptMapper;
import org.laokou.admin.user.model.UserE;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.mybatisplus.utils.MybatisUtil;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.ExecutorService;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class UserDeptGatewayImpl implements UserDeptGateway {

	private final MybatisUtil mybatisUtil;

	private final ExecutorService virtualThreadExecutor;

	@Override
	public Mono<Void> create(UserE userE) {
		return insertUserDept(userE).then();
	}

	@Override
	public Mono<Void> update(UserE userE) {
		if (CollectionUtil.isNotEmpty(userE.getDeptIds())) {
			return deleteUserDept(userE).then(Mono.defer(() -> create(userE)));
		}
		return Mono.empty();
	}

	@Override
	public Mono<Void> delete(UserE userE) {
		return deleteUserDept(userE).then();
	}

	private Mono<Object> insertUserDept(UserE userE) {
		// 新增用户部门关联表
		return Mono.fromCallable(() -> {
			mybatisUtil.batch(UserConvertor.toDataObjs(userE, userE.getId()), UserDeptMapper.class,
					UserDeptMapper::insert);
			return null;
		}).subscribeOn(Schedulers.fromExecutorService(virtualThreadExecutor));
	}

	private Mono<Object> deleteUserDept(UserE userE) {
		// 删除用户部门关联表
		return Mono.fromCallable(() -> {
			mybatisUtil.batch(UserConvertor.toDataObjs(userE), UserDeptMapper.class, UserDeptMapper::deleteObjById);
			return null;
		}).subscribeOn(Schedulers.fromExecutorService(virtualThreadExecutor));
	}

}

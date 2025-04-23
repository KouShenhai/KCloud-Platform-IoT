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
import org.laokou.admin.user.gateway.UserDeptGateway;
import org.laokou.admin.user.gatewayimpl.database.UserDeptMapper;
import org.laokou.admin.user.gatewayimpl.database.dataobject.UserDeptDO;
import org.laokou.admin.user.model.UserE;
import org.laokou.common.core.util.CollectionUtils;
import org.laokou.common.core.util.ThreadUtils;
import org.laokou.common.mybatisplus.util.MybatisUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

import java.time.Duration;
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
	public Mono<Void> update(UserE userE) {
		return getUserDeptIds(userE.getUserIds()).map(ids -> {
			userE.setUserDeptIds(ids);
			return userE;
		}).doOnNext(this::deleteUserDept).doOnNext(this::insertUserDept).then();
	}

	@Override
	public Mono<Void> delete(Long[] userIds) {
		return getUserDeptIds(Arrays.asList(userIds)).map(ids -> {
			UserE userE = new UserE();
			userE.setUserDeptIds(ids);
			return userE;
		}).doOnNext(this::deleteUserDept).then();
	}

	private void insertUserDept(UserE userE) {
		// 新增用户部门关联表
		List<UserDeptDO> list = UserConvertor.toDataObjs(userE, userE.getId());
		if (CollectionUtils.isNotEmpty(list)) {
			mybatisUtils.batch(list, UserDeptMapper.class, UserDeptMapper::insert);
		}
	}

	private void deleteUserDept(UserE userE) {
		// 删除用户部门关联表
		List<UserDeptDO> list = UserConvertor.toDataObjs(userE);
		if (CollectionUtils.isNotEmpty(list)) {
			mybatisUtils.batch(list, UserDeptMapper.class, UserDeptMapper::deleteObjById);
		}
	}

	private Mono<List<Long>> getUserDeptIds(List<Long> userIds) {
		return Mono.fromCallable(() -> userDeptMapper.selectIdsByUserIds(userIds))
			.subscribeOn(Schedulers.fromExecutor(ThreadUtils.newVirtualTaskExecutor()))
			.retryWhen(Retry.backoff(5, Duration.ofMillis(100))
				.maxBackoff(Duration.ofSeconds(1))
				.jitter(0.5)
				.doBeforeRetry(retry -> log.info("Retry attempt #{}", retry.totalRetriesInARow()))); // 增强型指数退避策略

	}

}

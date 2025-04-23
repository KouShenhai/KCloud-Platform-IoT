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
import org.laokou.admin.role.gateway.RoleMenuGateway;
import org.laokou.admin.role.gatewayimpl.database.RoleMenuMapper;
import org.laokou.admin.role.gatewayimpl.database.dataobject.RoleMenuDO;
import org.laokou.admin.role.model.RoleE;
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
public class RoleMenuGatewayImpl implements RoleMenuGateway {

	private final MybatisUtils mybatisUtils;

	private final RoleMenuMapper roleMenuMapper;

	@Override
	public Mono<Void> update(RoleE roleE) {
		return getRoleMenuIds(roleE.getRoleIds()).map(ids -> {
			roleE.setRoleMenuIds(ids);
			return roleE;
		}).doOnNext(this::deleteRoleMenu).doOnNext(this::insertRoleMenu).then();
	}

	@Override
	public Mono<Void> delete(Long[] roleIds) {
		return getRoleMenuIds(Arrays.asList(roleIds)).map(ids -> {
			RoleE roleE = new RoleE();
			roleE.setRoleMenuIds(ids);
			return roleE;
		}).doOnNext(this::deleteRoleMenu).then();
	}

	private void insertRoleMenu(RoleE roleE) {
		// 新增角色菜单关联表
		List<RoleMenuDO> list = RoleConvertor.toDataObjects(roleE, roleE.getId());
		if (CollectionUtils.isNotEmpty(list)) {
			mybatisUtils.batch(list, RoleMenuMapper.class, RoleMenuMapper::insert);
		}
	}

	private void deleteRoleMenu(RoleE roleE) {
		// 删除角色菜单关联表
		List<RoleMenuDO> list = RoleConvertor.toDataObjects(roleE);
		if (CollectionUtils.isNotEmpty(list)) {
			mybatisUtils.batch(list, RoleMenuMapper.class, RoleMenuMapper::deleteObjById);
		}
	}

	private Mono<List<Long>> getRoleMenuIds(List<Long> roleIds) {
		return Mono.fromCallable(() -> roleMenuMapper.selectIdsByRoleIds(roleIds))
			.subscribeOn(Schedulers.fromExecutor(ThreadUtils.newVirtualTaskExecutor()))
			.retryWhen(Retry.backoff(5, Duration.ofMillis(100))
				.maxBackoff(Duration.ofSeconds(1))
				.jitter(0.5)
				.doBeforeRetry(retry -> log.info("Retry attempt #{}", retry.totalRetriesInARow()))); // 增强型指数退避策略
	}

}

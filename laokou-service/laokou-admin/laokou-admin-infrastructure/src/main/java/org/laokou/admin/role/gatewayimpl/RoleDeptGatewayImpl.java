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
import org.laokou.admin.role.gateway.RoleDeptGateway;
import org.laokou.admin.role.gatewayimpl.database.RoleDeptMapper;
import org.laokou.admin.role.gatewayimpl.database.dataobject.RoleDeptDO;
import org.laokou.admin.role.model.RoleE;
import org.laokou.common.core.util.CollectionUtils;
import org.laokou.common.mybatisplus.util.MybatisUtils;
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
public class RoleDeptGatewayImpl implements RoleDeptGateway {

	private final RoleDeptMapper roleDeptMapper;

	private final MybatisUtils mybatisUtils;

	private final ExecutorService virtualThreadExecutor;

	@Override
	public Mono<Void> update(RoleE roleE) {
		return getRoleDeptIds(roleE.getRoleIds()).map(ids -> {
			roleE.setRoleDeptIds(ids);
			return roleE;
		}).doOnNext(this::deleteRoleDept).doOnNext(this::insertRoleDept).then();
	}

	@Override
	public Mono<Void> delete(Long[] roleIds) {
		return getRoleDeptIds(Arrays.asList(roleIds)).map(ids -> {
			RoleE roleE = new RoleE();
			roleE.setRoleDeptIds(ids);
			return roleE;
		}).doOnNext(this::deleteRoleDept).then();
	}

	private void insertRoleDept(RoleE roleE) {
		// 新增角色菜单关联表
		List<RoleDeptDO> list = RoleConvertor.toDataObjs(roleE, roleE.getId());
		if (CollectionUtils.isNotEmpty(list)) {
			mybatisUtils.batch(list, RoleDeptMapper.class, RoleDeptMapper::insert);
		}
	}

	private void deleteRoleDept(RoleE roleE) {
		// 删除角色菜单关联表
		List<RoleDeptDO> list = RoleConvertor.toDataObjs(roleE);
		if (CollectionUtils.isNotEmpty(list)) {
			mybatisUtils.batch(list, RoleDeptMapper.class, RoleDeptMapper::deleteObjById);
		}
	}

	private Mono<List<Long>> getRoleDeptIds(List<Long> roleIds) {
		return Mono.fromCallable(() -> roleDeptMapper.selectIdsByRoleIds(roleIds))
			.subscribeOn(Schedulers.fromExecutorService(virtualThreadExecutor));
	}

}

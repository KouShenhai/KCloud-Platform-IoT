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
import org.laokou.admin.role.gateway.RoleGateway;
import org.laokou.admin.role.gatewayimpl.database.RoleMapper;
import org.laokou.admin.role.gatewayimpl.database.dataobject.RoleDO;
import org.laokou.admin.role.model.RoleE;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;

/**
 * 角色网关实现.
 *
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class RoleGatewayImpl implements RoleGateway {

	private final RoleMapper roleMapper;

	private final ExecutorService virtualThreadExecutor;

	@Override
	public void create(RoleE roleE) {
		RoleDO roleDO = RoleConvertor.toDataObject(roleE, true);
		roleMapper.insert(roleDO);
	}

	@Override
	public Mono<Void> update(RoleE roleE) {
		return getVersion(roleE).map(version -> {
			RoleDO roleDO = RoleConvertor.toDataObject(roleE, false);
			roleDO.setVersion(version);
			return roleDO;
		}).doOnNext(roleMapper::updateById).then();
	}

	@Override
	public Mono<Void> delete(Long[] ids) {
		return Mono.fromCallable(() -> roleMapper.deleteByIds(Arrays.asList(ids)))
			.subscribeOn(Schedulers.fromExecutorService(virtualThreadExecutor))
			.then();
	}

	private Mono<Integer> getVersion(RoleE roleE) {
		return Mono.fromCallable(() -> roleMapper.selectVersion(roleE.getId()))
			.subscribeOn(Schedulers.fromExecutorService(virtualThreadExecutor));
	}

}
